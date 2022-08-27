package com.gdi.activity.Audit;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.asksira.bsimagepicker.BSImagePicker;
import com.bumptech.glide.Glide;
import com.gdi.activity.BaseActivity;
import com.gdi.activity.EditImageActivity;
import com.gdi.activity.GDIApplication;
import com.gdi.adapter.AddAttachmentAdapterMistery;
import com.gdi.api.AddBSAttachmentRequest;
import com.gdi.api.AddQuestionAttachmentRequest;
import com.gdi.api.ApiEndPoints;
import com.gdi.api.GetReportRequest;
import com.gdi.api.VolleyNetworkRequest;
import com.gdi.hotel.mystery.audits.R;
import com.gdi.model.audit.AddAttachment.AddAttachmentInfo;
import com.gdi.model.audit.AddAttachment.AddAttachmentRootObject;
import com.gdi.services.AppLocationService;
import com.gdi.utils.ApiResponseKeys;
import com.gdi.utils.AppLogger;
import com.gdi.utils.AppPrefs;
import com.gdi.utils.AppUtils;
import com.gdi.utils.CirclePagerIndicatorDecoration;
import com.gdi.utils.CustomDialog;
import com.gdi.utils.CustomTypefaceTextView;
import com.gdi.utils.ImageUtils;
import com.gdi.utils.PermissionUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddAttachmentActivityMistery extends BaseActivity implements View.OnClickListener,
        BSImagePicker.OnSingleImageSelectedListener,
        BSImagePicker.OnMultiImageSelectedListener,
        BSImagePicker.ImageLoaderDelegate {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_attachment_count)
    TextView tvAttachmentCount;
    @BindView(R.id.recycler_view_attachment_list)
    RecyclerView attachmentList;
    @BindView(R.id.add_attachment_layout)
    LinearLayout addAttachmentLayout;
    @BindView(R.id.floating_btn_add_attachment)
    FloatingActionButton addAttachmentBtn;
    @BindView(R.id.add_attachment_text)
    TextView add_attachment_text;
    private static final int REQUEST_FOR_CAMERA = 100;
    private static final int REQUEST_TAKE_PHOTO = 101;
    private static final int REQUEST_TAKE_VDO = 102;
    private static final int GALLERY_PERMISSION_REQUEST = 103;
    public int EDIT_IMAGE_POS=0;
    private static final int EDIT_IMAGE = 123;
    String mCurrentPhotoPath = "";
    String auditId = "";
    String sectionGroupId = "";
    String sectionId = "";
    String questionId = "";
    String attachType = "";
    String attachmentCount = "0";
    String longitude = "";
    String latitude = "";
    private String editable = "";
    String date = "";
    Context context;
    CustomDialog customDialog;
    private CustomDialog imageCustomDialog;
    AppLocationService appLocationService;
    private static final String TAG = AddAttachmentActivityMistery.class.getSimpleName();
    AddAttachmentListAdapter viewPagerAdapter;
    private int mImageCounter=0;

    @Override
    protected void onResume() {
        super.onResume();
        if (attachType.equals("bsSection")) {
            getBsAttachmentList();
        } else if (attachType.equals("bsQuestion")) {
            getQuestionAttachmentList();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_attachment);
        context = this;
        ButterKnife.bind(AddAttachmentActivityMistery.this);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 10);
        }
        appLocationService = new AppLocationService(context);
        initView();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent result = new Intent();
        result.putExtra("attachmentCount", attachmentCount);
        setResult(RESULT_CANCELED , result);
        finish();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        setActionBar();
        date = AppUtils.getDate(Calendar.getInstance().getTime());
        //customDialog = new CustomDialog(context, R.layout.add_attachment_layout);
        tvAttachmentCount = findViewById(R.id.tv_attachment_count);
        attachmentList = findViewById(R.id.recycler_view_attachment_list);
        addAttachmentLayout = findViewById(R.id.add_attachment_layout);
        add_attachment_text = findViewById(R.id.add_attachment_text);
        addAttachmentBtn = findViewById(R.id.floating_btn_add_attachment);
        addAttachmentBtn.setOnClickListener(this);
        auditId = getIntent().getStringExtra("auditId");
        attachType = getIntent().getStringExtra("attachType");
        sectionGroupId = getIntent().getStringExtra("sectionGroupId");
        sectionId = getIntent().getStringExtra("sectionId");
        questionId = getIntent().getStringExtra("questionId");
        editable = getIntent().getStringExtra("editable");
        AppLogger.e(TAG, "AttachType : " + attachType);


        if (editable.equals("1")) {
            addAttachmentBtn.setVisibility(View.GONE);
            add_attachment_text.setVisibility(View.GONE);
        } else {
            addAttachmentBtn.setVisibility(View.VISIBLE);
            add_attachment_text.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.floating_btn_add_attachment:
                ((GDIApplication)getApplicationContext()).setmAttachImageList(null);
                openDCRDialog();//TODO open dialog box to choose gallery or camera
                break;
        }
    }

    private void getLatLong() {
        Location location = appLocationService.getLocation(LocationManager.GPS_PROVIDER, context);

        if (location != null) {
            double lat = location.getLatitude();
            double log = location.getLongitude();
            /*latitude = "" + location.getLatitude();
            longitude = "" + location.getLongitude();*/
            latitude = String.format("%.6f", lat);
            longitude = String.format("%.6f", log);
            Log.e("latitude", "" + lat);
            Log.e("longitude", "" + log);
            Log.e("latitude", "" + latitude);
            Log.e("longitude", "" + longitude);

        } else {
            //showSettingsAlert();
            latitude = "";
            longitude = "";
        }
    }

    private void openDCRDialog() {
        imageCustomDialog = new CustomDialog(context, R.layout.upload_image_dailog);
        imageCustomDialog.setCancelable(true);
        CustomTypefaceTextView tvGallery = imageCustomDialog.findViewById(R.id.tv_gallery);
        CustomTypefaceTextView tvGalleryVDO = imageCustomDialog.findViewById(R.id.tv_gallery_vdo);
        CustomTypefaceTextView tvCamera = imageCustomDialog.findViewById(R.id.tv_camera);
        CustomTypefaceTextView tvCancel = imageCustomDialog.findViewById(R.id.tv_cancel);

        tvGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAndRequestGalleryPermissions()) {
                    chooseImagesFromGallery();
                }
                imageCustomDialog.dismiss();
            }
        });
        tvGalleryVDO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAndRequestGalleryPermissions()) {
                    chooseImagesFromGalleryVDO();
                }
                imageCustomDialog.dismiss();
            }
        });
        tvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraPermission();
                imageCustomDialog.dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageCustomDialog.dismiss();
            }
        });
        imageCustomDialog.show();

    }

    private boolean checkAndRequestGalleryPermissions() {

        int permissionStorageWrite = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionStorageRead = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (permissionStorageWrite != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (permissionStorageRead != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            PermissionUtils.requestPermission(this, listPermissionsNeeded,
                    GALLERY_PERMISSION_REQUEST);
            return false;
        }
        return true;
    }


    private void chooseImagesFromGallery() {
        System.gc();

        //Select from Gallery with multi selection
        BSImagePicker pickerDialog = new BSImagePicker.Builder("com.gdi.android.fileprovider")
                .setMaximumDisplayingImages(Integer.MAX_VALUE)
                .isMultiSelect()
                .setTag("")
                .setMinimumMultiSelectCount(1)
                .setMaximumMultiSelectCount(5)
                .build();
        pickerDialog.show(getSupportFragmentManager(), "picker");


    }

    private void chooseImagesFromGalleryVDO() {
        System.gc();
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, REQUEST_TAKE_VDO);
    }


    private void setAttachmentList(ArrayList<AddAttachmentInfo> arrayList) {
        ArrayList<AddAttachmentInfo> attachmentInfoArrayList = new ArrayList<>();

        for (int i = 0; i < arrayList.size(); i++) {
            AddAttachmentInfo info = arrayList.get(i);
            String fileType = info.getFile_type();
            if (fileType.contains("image/") || fileType.contains("video/") ) {
                attachmentInfoArrayList.add(info);
            }

        }
        //GridLayoutManager gridLayoutManager = new GridLayoutManager(context,2, LinearLayoutManager.VERTICAL,false);
        AddAttachmentAdapterMistery addAttachmentAdapter = new AddAttachmentAdapterMistery(context,
                attachmentInfoArrayList, attachType, auditId, sectionGroupId, sectionId, questionId, editable);
        attachmentList.setLayoutManager(new LinearLayoutManager(context));
        attachmentList.setAdapter(addAttachmentAdapter);
    }

    private void cameraPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(AddAttachmentActivityMistery.this,
                    new String[]{Manifest.permission.CAMERA}, REQUEST_FOR_CAMERA);

        } else {
            System.gc();
            takePhotoFromCamera();

        }
    }

    private void takePhotoFromCamera() {
        /*Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);*/

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.gdi.android.fileprovider",photoFile);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,  /* prefix */".jpg",         /* suffix */storageDir      /* directory */);

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_FOR_CAMERA:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    takePhotoFromCamera();
                }
                break;
            case GALLERY_PERMISSION_REQUEST:
                boolean isGalleryPermissionGranted = PermissionUtils.checkPermissionGrantedOrNot(
                        AddAttachmentActivityMistery.this, permissions, grantResults);
                if (isGalleryPermissionGranted) {
                    chooseImagesFromGallery();
                }
                break;
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            try{
                Uri uri = Uri.fromFile(new File(mCurrentPhotoPath));
                Log.e("URI in side result=>  ","+"+uri);

                if (uri != null) {
                    ArrayList<Uri> uriList = new ArrayList<>();
                    uriList.add(uri);
                    addDescriptionDialog(uriList);
                } else {
                    AppUtils.toast(AddAttachmentActivityMistery.this, "Image Not Attached" );
                }
            }catch (Exception e){
                AppUtils.toast(AddAttachmentActivityMistery.this, "Some technical error. Please try again." );
            }

        }else if(requestCode == EDIT_IMAGE && resultCode == RESULT_OK){
            Uri uri = Uri.fromFile(new File(data.getStringExtra("path")));
            viewPagerAdapter.updateImage(EDIT_IMAGE_POS,uri);
        }
        else
        {
            // Toast.makeText(this,"Uploading Video",Toast.LENGTH_SHORT).show();
            if (resultCode == RESULT_OK) {
                if (requestCode == REQUEST_TAKE_VDO) {
                    Uri selectedImageUri = data.getData();
                    if(selectedImageUri!=null) {

                        Log.e("URIIIIIII===>", "" + selectedImageUri);
                        try {
                            byte[] imageByteData = readBytes(selectedImageUri);
                            showAppProgressDialog();

                            if (attachType.equals("bsSection"))
                                addBsFileAttachment(imageByteData, "Description video", "video");
                            else
                                addQuestionFileAttachment(imageByteData, "Description video", "video");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        AppUtils.toast(AddAttachmentActivityMistery.this, "Video Not Attached" );
                    }
                }
            }

        }


    }

    public byte[] readBytes(Uri uri) throws IOException {
        // this dynamically extends to take the bytes you read
        InputStream inputStream = getContentResolver().openInputStream(uri);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        // this is storage overwritten on each iteration with bytes
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        // we need to know how may bytes were read to write them to the byteBuffer
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        // and then we can return your byte array.
        return byteBuffer.toByteArray();
    }
    private List listOfImageString=new ArrayList();

    private void addDescriptionDialog(ArrayList<Uri> uriList)
    {
        try
        {

            for(int i=0;i<uriList.size();i++)
                listOfImageString.add(uriList.get(i).toString());


            ((GDIApplication)getApplicationContext()).setmAttachImageList(listOfImageString);

            customDialog = new CustomDialog(context, R.layout.add_attachment_dailog);
            customDialog.setCancelable(false);

            getLatLong();

            //final ViewPager attachment_listing_viewpager = customDialog.findViewById(R.id.vp_attachment_listing);
            RecyclerView attach_List_recycler_view = customDialog.findViewById(R.id.rv_image_list);

            viewPagerAdapter = new AddAttachmentListAdapter(context, uriList);
            attach_List_recycler_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            attach_List_recycler_view.addItemDecoration(new CirclePagerIndicatorDecoration());
            attach_List_recycler_view.setAdapter(viewPagerAdapter);

            customDialog.show();}
        catch (Exception e)
        {
            Log.e("======> ERROR","Custome dialog");
            e.printStackTrace();
        }
    }


    @Override
    public void onSingleImageSelected(Uri uri, String tag) {
        //Uri contentURI = data.getData();
        //File file = new File(String.valueOf(contentURI));
        try {
            if (uri != null) {
                ArrayList<Uri> uriList = new ArrayList<>();
                uriList.add(uri);
                addDescriptionDialog(uriList);
            } else {
                AppUtils.toast(AddAttachmentActivityMistery.this, "Image Not Attached" + tag);
            }
        }catch (Exception e){
            AppUtils.toast(AddAttachmentActivityMistery.this, "Some technical error. Please try again." );
        }

    }

    @Override
    public void onMultiImageSelected(List<Uri> uriList, String tag) {
        try{
            if (uriList != null) {
                ArrayList<Uri> uris = new ArrayList<>();
                uris.addAll(uriList);
                addDescriptionDialog(uris);
            } else {
                AppUtils.toast(AddAttachmentActivityMistery.this, "Image Not Attached" + tag);
            }
        }catch (Exception e){
            AppUtils.toast(AddAttachmentActivityMistery.this, "Some technical error. Please try again." );
        }

    }

    @Override
    public void loadImage(File imageFile, ImageView ivImage) {
        Glide.with(AddAttachmentActivityMistery.this).load(imageFile).into(ivImage);
    }

    private void setActionBar() {
        initToolbar(toolbar);
        setTitle("Attachments");
        enableBack(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void getBsAttachmentList() {
        //showAppProgressDialog();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLogger.e(TAG, "GetAttachmentResponse: " + response);
                try {
                    JSONObject object = new JSONObject(response);

                    if (!object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        AddAttachmentRootObject addAttachmentRootObject = new GsonBuilder().create()
                                .fromJson(object.toString(), AddAttachmentRootObject.class);
                        if (addAttachmentRootObject.getData() != null &&
                                addAttachmentRootObject.getData().toString().length() > 0) {
                            setAttachmentList(addAttachmentRootObject.getData());
                            int size = addAttachmentRootObject.getData().size();
                            attachmentCount = "" + size;
                            tvAttachmentCount.setVisibility(View.VISIBLE);
                            tvAttachmentCount.setText("" + size + "/20 Uploaded");
                            if (size >= 20) {
                                addAttachmentLayout.setVisibility(View.GONE);
                            } else {
                                addAttachmentLayout.setVisibility(View.VISIBLE);
                            }
                            //brandStandardAuditAdapter.notifyDataSetChanged();
                        } else {
                            tvAttachmentCount.setVisibility(View.VISIBLE);
                        }
                    } else if (object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        AppUtils.toast((BaseActivity) context,
                                object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //hideProgressDialog();
            }

        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //hideProgressDialog();
                AppLogger.e(TAG, "GetAttachmentError: " + error.getMessage());
                //AppUtils.toast((BaseActivity) context, "Server temporary unavailable, Please try again");
                Toast.makeText(getApplicationContext(), "Server temporary unavailable, Please try again", Toast.LENGTH_SHORT).show();

            }
        };

        String url = ApiEndPoints.BSATTACHMENT_MISTERY + "?"
                + "audit_id=" + auditId + "&"
                + "section_group_id=" + sectionGroupId + "&"
                + "section_id=" + sectionId;
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().getCurrentUser().getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                GetReportRequest getReportRequest = new GetReportRequest(AppPrefs.getAccessToken(context),task.getResult().getToken(), url, stringListener, errorListener);
                                VolleyNetworkRequest.getInstance(context).addToRequestQueue(getReportRequest);
                            }

                        }
                    });

        }  }

    public void getQuestionAttachmentList() {
        //showAppProgressDialog();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLogger.e(TAG, "GetAttachmentResponse: " + response);
                try {
                    JSONObject object = new JSONObject(response);

                    if (!object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        AddAttachmentRootObject addAttachmentRootObject = new GsonBuilder().create()
                                .fromJson(object.toString(), AddAttachmentRootObject.class);
                        if (addAttachmentRootObject.getData() != null &&
                                addAttachmentRootObject.getData().toString().length() > 0) {
                            setAttachmentList(addAttachmentRootObject.getData());
                            int size = addAttachmentRootObject.getData().size();
                            attachmentCount = "" + size;
                            tvAttachmentCount.setVisibility(View.VISIBLE);
                            tvAttachmentCount.setText("" + size + "/20 Uploaded");
                            if (size >= 20) {
                                addAttachmentLayout.setVisibility(View.GONE);
                            } else {
                                addAttachmentLayout.setVisibility(View.VISIBLE);
                            }
                            //brandStandardAuditAdapter.notifyDataSetChanged();
                        } else {
                            tvAttachmentCount.setVisibility(View.VISIBLE);
                        }
                    } else if (object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        AppUtils.toast((BaseActivity) context,
                                object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //hideProgressDialog();
            }

        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //hideProgressDialog();
                AppLogger.e(TAG, "GetAttachmentError: " + error.getMessage());
                //AppUtils.toast((BaseActivity) context, "Server temporary unavailable, Please try again");
                Toast.makeText(getApplicationContext(), "Server temporary unavailable, Please try again", Toast.LENGTH_SHORT).show();
            }
        };

        String url = ApiEndPoints.BSATTACHMENT_MISTERY + "?"
                + "audit_id=" + auditId + "&"
                + "section_group_id=" + sectionGroupId + "&"
                + "section_id=" + sectionId + "&"
                + "question_id=" + questionId;
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().getCurrentUser().getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                GetReportRequest getReportRequest = new GetReportRequest(AppPrefs.getAccessToken(context),task.getResult().getToken(), url, stringListener, errorListener);
                                VolleyNetworkRequest.getInstance(context).addToRequestQueue(getReportRequest);
                            }

                        }
                    });

        }
    }




    private void addBsFileAttachment(byte[] imageByteData, String description,String type) {
        //showProgressDialog();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLogger.e(TAG, "AddAttachmentResponse: " + response);
                try {
                    JSONObject object = new JSONObject(response);

                    if (!object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        Toast.makeText(getApplicationContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();
                        getBsAttachmentList();
                    } else if (object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        AppUtils.toast((BaseActivity) context, object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hideProgressDialog();
            }

        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                AppLogger.e(TAG, "AddAttachmentError: " + error.getMessage());
                //AppUtils.toast((BaseActivity) context, "Server temporary unavailable, Please try again");
                Toast.makeText(getApplicationContext(), "Server temporary unavailable, Please try again", Toast.LENGTH_SHORT).show();

            }
        };

        String url = ApiEndPoints.BSATTACHMENT_MISTERY;
        String fileName = "GDI-" + date;






        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().getCurrentUser().getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                AddBSAttachmentRequest addBSAttachmentRequest = new AddBSAttachmentRequest(
                                        AppPrefs.getAccessToken(context),task.getResult().getToken(), url, fileName, imageByteData, auditId,
                                        sectionGroupId, sectionId, description, "0", latitude, longitude,type,
                                        stringListener, errorListener);
                                VolleyNetworkRequest.getInstance(context).addToRequestQueue(addBSAttachmentRequest);  }
                        }
                    });
        }
    }

    private void addQuestionFileAttachment(byte[] imageByteData, String description,String type) {
        //showProgressDialog();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLogger.e(TAG, "AddAttachmentResponse: " + response);
                try {
                    JSONObject object = new JSONObject(response);

                    if (!object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        Toast.makeText(getApplicationContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();
                        getQuestionAttachmentList();
                    } else if (object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        AppUtils.toast((BaseActivity) context,
                                object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hideProgressDialog();
            }

        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                AppLogger.e(TAG, "AddAttachmentError: " + error.getMessage());
                //AppUtils.toast((BaseActivity) context, "Server temporary unavailable, Please try again");
                Toast.makeText(getApplicationContext(), "Server temporary unavailable, Please try again", Toast.LENGTH_SHORT).show();

            }
        };

        String url = ApiEndPoints.BSATTACHMENT_MISTERY;
        String fileName = "GDI-" + date;


        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().getCurrentUser().getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                AddQuestionAttachmentRequest addBSAttachmentRequest = new AddQuestionAttachmentRequest(
                                        AppPrefs.getAccessToken(context),task.getResult().getToken(), url, fileName, imageByteData, auditId,
                                        sectionGroupId, sectionId, questionId, description, "0", latitude, longitude,type, stringListener, errorListener);
                                VolleyNetworkRequest.getInstance(context).addToRequestQueue(addBSAttachmentRequest);

                            }
                        }
                    });
        }
    }




    public class AddAttachmentListAdapter extends RecyclerView.Adapter<AddAttachmentListAdapter.AddAttachmentListViewHolder> {
        private ArrayList<Uri> imageURI;

        Context context;
        byte[] imageByteData = new byte[0];

        public AddAttachmentListAdapter(Context context, ArrayList<Uri> imageListURI) {
            this.imageURI = imageListURI;
            this.context = context;
        }

        public void updateImage(int pos,Uri uri){
            imageURI.set(pos,uri);
            notifyItemChanged(pos);
        }

        @NonNull
        @Override
        public AddAttachmentListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_add_attachment,
                    parent, false);

            return new AddAttachmentListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final AddAttachmentListViewHolder holder, final int position) {
            Uri uri = imageURI.get(position);
            Bitmap bitmap = null;

            try {
                //bitmap = decodeSampledBitmapFromResource(uri.getPath(),100,100);

                //bitmap = convertToMutable(MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri));
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                // bitmap= ImageUtils.resize(bitmap,800,600);
                bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

                if (bitmap != null) {
                    Canvas canvas = new Canvas(bitmap);
                    Paint paint = new Paint();
                    paint.setColor(Color.BLUE);
                    paint.setStyle(Paint.Style.FILL);
                    paint.setColor(getResources().getColor(android.R.color.white)); // Text Color
                    //paint.setTextSize(50); //Text Size
                    final float testTextSize = 50f;

                    // Get the bounds of the text, using our testTextSize.
                    paint.setTextSize(testTextSize);

                    /*Rect bounds = new Rect();
                    paint.getTextBounds(AppUtils.getCurrentDate(), 0, AppUtils.getCurrentDate().length(), bounds);
                    // Calculate the desired size as a proportion of our testTextSize.
                    float desiredTextSize = testTextSize * 600 / bounds.width();
                    // Set the paint for that size.
                    paint.setTextSize(desiredTextSize);
                    */canvas.drawText(AppUtils.getCurrentDate(), 20, bitmap.getHeight()-50, paint);
                    //canvas.drawText("Created by AndroidClarified",10,10,0,0,paint);
                    holder.imageView.setImageBitmap(bitmap);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
                    imageByteData = byteArrayOutputStream.toByteArray();

                }


            } catch (Exception e) {
                e.printStackTrace();
                AppUtils.toast((AddAttachmentActivityMistery)context,"**Some technical error. Please try again.");

            }

            holder.description.setText("");

            holder.submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mImageCounter++;

                    if (imageURI.size() == 1) {
                        AppUtils.hideKeyboard(context, view);
                    }
                    String text = "";
                    if (holder.description.getText().toString().length() > 0) {
                        text = holder.description.getText().toString();
                    }
                    switch (attachType) {
                        case "bsSection":
                            addBsFileAttachment(imageByteData, text,"image");
                            imageURI.remove(position);
                            int size = imageURI.size();
                            if (size > 0) {
                                AppUtils.toast((AddAttachmentActivityMistery) context, "" + size + " images left");
                            }
                            notifyDataSetChanged();
                            break;
                        case "bsQuestion":
                            addQuestionFileAttachment(imageByteData, text,"image");
                            imageURI.remove(position);
                            int bsSize = imageURI.size();
                            if (bsSize > 0) {
                                AppUtils.toast((AddAttachmentActivityMistery) context, "" + bsSize + " images left");
                            }
                            notifyDataSetChanged();
                            break;

                    }

                    if(imageURI.size()==0){

                        customDialog.cancel();
                        Intent result = new Intent();
                        result.putExtra("attachmentCount", attachmentCount);
                        setResult(RESULT_OK, result);
                        finish();
                    }
                }
            });
            holder.cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listOfImageString.remove(mImageCounter);

                    if (imageURI.size() <= 1)
                    {
                        // added by Vikas
                        Intent result = new Intent();
                        result.putExtra("attachmentCount", attachmentCount);
                        setResult(RESULT_OK, result);
                        finish();

                        customDialog.dismiss();

                    } else {
                        imageURI.remove(position);
                        int size = imageURI.size();
                        if (size > 0) {
                            AppUtils.toast((AddAttachmentActivityMistery) context, "" + size + " images left");
                        }
                        notifyDataSetChanged();
                    }
                }
            });

            final Bitmap finalBitmap = bitmap;
            holder.editImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EDIT_IMAGE_POS = position;
                    Intent intent = new Intent(context, EditImageActivity.class);
                    intent.putExtra("bitmap", imageURI.get(position).toString());
                    startActivityForResult(intent,EDIT_IMAGE);
                }
            });


        }

        @Override
        public int getItemCount() {
            return imageURI.size();
        }

        public class AddAttachmentListViewHolder extends RecyclerView.ViewHolder {

            ImageView imageView;
            EditText description;
            TextView submitButton;
            TextView cancelButton;
            ImageButton editImage;

            public AddAttachmentListViewHolder(View itemView) {
                super(itemView);

                imageView = itemView.findViewById(R.id.iv_attached_image);
                description = itemView.findViewById(R.id.et_description);
                submitButton = itemView.findViewById(R.id.tv_submit_btn);
                cancelButton = itemView.findViewById(R.id.tv_cancel_btn);
                editImage = itemView.findViewById(R.id.edit_image);
            }
        }
    }









}
