package com.gdi.activity.Audit;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.asksira.bsimagepicker.BSImagePicker;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gdi.R;
import com.gdi.activity.BaseActivity;
import com.gdi.activity.ImageViewActivity;
import com.gdi.adapter.AddAttachmentAdapter;
import com.gdi.api.AddBSAttachmentRequest;
import com.gdi.api.AddDSAttachmentRequest;
import com.gdi.api.AddESAttachmentRequest;
import com.gdi.api.AddQuestionAttachmentRequest;
import com.gdi.api.ApiEndPoints;
import com.gdi.api.GetReportRequest;
import com.gdi.api.VolleyNetworkRequest;
import com.gdi.fragment.AddAttachmentFragment;
import com.gdi.model.audit.AddAttachment.AddAttachmentInfo;
import com.gdi.model.audit.AddAttachment.AddAttachmentRootObject;
import com.gdi.services.AppLocationService;
import com.gdi.utils.ApiResponseKeys;
import com.gdi.utils.AppLogger;
import com.gdi.utils.AppPrefs;
import com.gdi.utils.AppUtils;
import com.gdi.utils.CustomDialog;
import com.gdi.utils.CustomTypefaceTextView;
import com.gdi.utils.LocationAddress;
import com.gdi.utils.PermissionUtils;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddAttachmentActivity extends BaseActivity implements View.OnClickListener,
        BSImagePicker.OnSingleImageSelectedListener,
        BSImagePicker.OnMultiImageSelectedListener,
        BSImagePicker.ImageLoaderDelegate {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
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
    private static final int GALLERY_PERMISSION_REQUEST = 103;
    private static final int SELECT_IMAGES_FROM_GALLERY = 104;
    private static final int LOCATION_PERMISSION_REQUEST = 105;
    String mCurrentPhotoPath = "";
    String auditId = "";
    String sectionGroupId = "";
    String sectionId = "";
    String questionId = "";
    String attachType = "";
    String attachmentCount = "";
    String longitude = "";
    String latitude = "";
    private String editable = "";
    String date = "";
    Context context;
    CustomDialog customDialog;
    private CustomDialog imageCustomDialog;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    AppLocationService appLocationService;
    private static final long MIN_DISTANCE_FOR_UPDATE = 10;
    private static final long MIN_TIME_FOR_UPDATE = 1000 * 60 * 2;
    private static final String TAG = AddAttachmentActivity.class.getSimpleName();

    @Override
    protected void onResume() {
        super.onResume();
        /*if (attachType.equals("bsSection")) {
            getBsAttachmentList();
        } else if (attachType.equals("bsQuestion")) {
            getQuestionAttachmentList();
        } else if (attachType.equals("dsSection")) {
            getDsAttachmentList();
        } else if (attachType.equals("esSection")) {
            getEsAttachmentList();
        }*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_attachment);
        context = this;
        ButterKnife.bind(AddAttachmentActivity.this);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION
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
        setResult(RESULT_OK, result);
        finish();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        setActionBar();
        date = AppUtils.getDate(Calendar.getInstance().getTime());
        //customDialog = new CustomDialog(context, R.layout.add_attachment_layout);
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

        if (attachType.equals("bsSection")) {
            getBsAttachmentList();
        } else if (attachType.equals("bsQuestion")) {
            getQuestionAttachmentList();
        } else if (attachType.equals("dsSection")) {
            getDsAttachmentList();
        } else if (attachType.equals("esSection")) {
            getEsAttachmentList();
        }

        if (editable.equals("1")) {
            addAttachmentBtn.setVisibility(View.GONE);
            add_attachment_text.setVisibility(View.GONE);
        } else {
            addAttachmentBtn.setVisibility(View.VISIBLE);
            add_attachment_text.setVisibility(View.VISIBLE);
        }



        /*switch (attachtype){
            case "bsSection":

                break;
            case "bsQuestion":

                break;
            case "dsSection":

                break;
            case "esSection":

                break;
        }*/
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.floating_btn_add_attachment:
                //openDCRDialog();TODO open dialog box to choose gallery or camera
                //To select images from gallery with multi image selection
                if (checkAndRequestGalleryPermissions()) {
                    chooseImagesFromGallery();
                }
                break;
        }
    }

    private void getLatLong(){
        Location location = appLocationService
                .getLocation(LocationManager.GPS_PROVIDER, context);
        //you can hard-code the lat & long if you have issues with getting it
        //remove the below if-condition and use the following couple of lines
        //double latitude = 37.422005;
        //double longitude = -122.084095

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
            /*LocationAddress locationAddress = new LocationAddress();
            locationAddress.getAddressFromLocation(latitude, longitude,
                    getApplicationContext(), new GeocoderHandler());*/
        } else {
            //showSettingsAlert();
            latitude = "";
            longitude = "";
        }
    }

    private void openDCRDialog() {
        imageCustomDialog = new CustomDialog(context, R.layout.upload_image_dailog);
        imageCustomDialog.setCancelable(true);
        //customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
        //CustomTypefaceTextView tv_subTile = (CustomTypefaceTextView) customDialog.findViewById(R.id.tv_subTile_footfall);
        //tv_subTile.setText("D C R");
        CustomTypefaceTextView tvGallery = imageCustomDialog.findViewById(R.id.tv_gallery);
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
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionStorageRead = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (permissionStorageWrite != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (permissionStorageRead != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            PermissionUtils.requestPermission(this, listPermissionsNeeded,
                    GALLERY_PERMISSION_REQUEST);
            return false;
        }
        return true;
    }

    private boolean checkAndRequestGeoTagPermissions() {

        int permissionLocation = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            PermissionUtils.requestPermission(this, listPermissionsNeeded,
                    LOCATION_PERMISSION_REQUEST);
            return false;
        }
        return true;
    }

    private void chooseImagesFromGallery() {
        //Select from Gallery with one selection
        /*Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, SELECT_IMAGES_FROM_GALLERY);
        imageCustomDialog.dismiss();*/

        //Select from Gallery with multi selection
        BSImagePicker pickerDialog = new BSImagePicker.Builder("com.gdi.android.fileprovider")
                .setMaximumDisplayingImages(Integer.MAX_VALUE)
                .isMultiSelect()
                .setMinimumMultiSelectCount(1)
                .setMaximumMultiSelectCount(5)
                .build();
        pickerDialog.show(getSupportFragmentManager(), "picker");


    }

    private void setAttachmentList(ArrayList<AddAttachmentInfo> arrayList) {
        ArrayList<AddAttachmentInfo> attachmentInfoArrayList = new ArrayList<>();

        for (int i = 0; i < arrayList.size(); i++) {
            AddAttachmentInfo info = arrayList.get(i);
            String fileType = info.getFile_type();
            if (fileType.contains("image/")) {
                attachmentInfoArrayList.add(info);
            }
        }
        //GridLayoutManager gridLayoutManager = new GridLayoutManager(context,2, LinearLayoutManager.VERTICAL,false);
        AddAttachmentAdapter addAttachmentAdapter = new AddAttachmentAdapter(context,
                attachmentInfoArrayList, attachType, auditId, sectionGroupId, sectionId, questionId, editable);
        attachmentList.setLayoutManager(new LinearLayoutManager(context));
        attachmentList.setAdapter(addAttachmentAdapter);
    }

    private void cameraPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(AddAttachmentActivity.this,
                    new String[]{Manifest.permission.CAMERA}, REQUEST_FOR_CAMERA);

        } else {
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
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.gdi.android.fileprovider",
                        photoFile);
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
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

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
                        AddAttachmentActivity.this, permissions, grantResults);
                if (isGalleryPermissionGranted) {
                    chooseImagesFromGallery();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            //Bitmap bitmapThumbnail = (Bitmap) data.getExtras().get("data");
            //userProfileImage.setImageBitmap(bitmapThumbnail);
            //path = "data:image/jpg;base64," + encodeTobase64(bitmapThumbnail);
            //AppLogger.d("Base64Image",  path);
            //saveImage(bitmapThumbnail);


            addCameraDescriptionDialog();
            //addDescriptionDialog(imageByteData);
        }

        /*if (requestCode == SELECT_IMAGES_FROM_GALLERY && resultCode == RESULT_OK) {
            if (data != null) {
                Uri contentURI = data.getData();

                File file = new File(String.valueOf(contentURI));
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), contentURI);
                    byte[] imageByteData = new byte[0];
                    //Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.fromFile(file));
                    if (bitmap != null) {
                        //Bitmap rotateBitmap = callRotateImage(bitmap);
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
                        imageByteData = byteArrayOutputStream.toByteArray();
                        Log.e("Image Byte Data : ", "" + imageByteData);
                    }

                    addDescriptionDialog(imageByteData);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

        }*/
    }

    private Bitmap callRotateImage(Bitmap bitmap) {
        ExifInterface ei = null;
        try {
            ei = new ExifInterface(mCurrentPhotoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        Bitmap rotatedBitmap = null;
        switch (orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = rotateImage(bitmap, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = rotateImage(bitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = rotateImage(bitmap, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                rotatedBitmap = bitmap;
        }
        return rotatedBitmap;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    private void addDescriptionDialog(ArrayList<Uri> uriList) {
        //final CustomDialog customDialog = new CustomDialog(context, R.layout.attachment_description_dailog);
        customDialog = new CustomDialog(context, R.layout.add_attachment_dailog);
        customDialog.setCancelable(true);

        getLatLong();

        //final ViewPager attachment_listing_viewpager = customDialog.findViewById(R.id.vp_attachment_listing);
        RecyclerView attach_List_recycler_view = customDialog.findViewById(R.id.rv_image_list);

        /*ArrayList<String> arrayList = new ArrayList();
        arrayList.add("String1");
        arrayList.add("String2");
        arrayList.add("String3");*/
        AddAttachmentListAdapter viewPagerAdapter = new AddAttachmentListAdapter(context, uriList);
        attach_List_recycler_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        attach_List_recycler_view.setAdapter(viewPagerAdapter);
        customDialog.show();
    }

    byte[] cameraImageByteData = new byte[0];
    private void addCameraDescriptionDialog() {
        customDialog = new CustomDialog(context, R.layout.fragment_add_attachment);
        customDialog.setCancelable(false);

        getLatLong();

        ImageView imageView = customDialog.findViewById(R.id.iv_attached_image);
        final EditText description = customDialog.findViewById(R.id.et_description);
        TextView submitButton = customDialog.findViewById(R.id.tv_submit_btn);
        TextView cancelButton = customDialog.findViewById(R.id.tv_cancel_btn);

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);

        //Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.fromFile(file));
        if (bitmap != null) {
            Bitmap rotateBitmap = callRotateImage(bitmap);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            rotateBitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
            cameraImageByteData = byteArrayOutputStream.toByteArray();
            Log.e("Image Byte Data : ", "" + cameraImageByteData);
            imageView.setImageBitmap(rotateBitmap);
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtils.hideKeyboard(context, view);
                String text = "Test";
                if (description.getText().toString().length() > 0){
                    text = description.getText().toString();
                }
                switch (attachType) {
                    case "bsSection":
                        addBsFileAttachment(cameraImageByteData, text);
                        break;
                    case "bsQuestion":
                        addQuestionFileAttachment(cameraImageByteData, text);
                        break;
                    case "dsSection":
                        addDsFileAttachment(cameraImageByteData, text);
                        break;
                    case "esSection":
                        addEsFileAttachment(cameraImageByteData, text);
                        break;
                }
                customDialog.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog.dismiss();
            }
        });
        customDialog.show();
    }

    @Override
    public void onSingleImageSelected(Uri uri, String tag) {
        //Uri contentURI = data.getData();
        //File file = new File(String.valueOf(contentURI));
        if (uri != null) {
            ArrayList<Uri> uriList = new ArrayList<>();
            uriList.add(uri);
            addDescriptionDialog(uriList);
        }else {
            AppUtils.toast(AddAttachmentActivity.this, "Image Not Attached" + tag);
        }
    }

    @Override
    public void onMultiImageSelected(List<Uri> uriList, String tag) {
        if (uriList != null) {
            ArrayList<Uri> uris = new ArrayList<>();
            uris.addAll(uriList);
            addDescriptionDialog(uris);
        }else {
            AppUtils.toast(AddAttachmentActivity.this, "Image Not Attached" + tag);
        }
    }

    @Override
    public void loadImage(File imageFile, ImageView ivImage) {
        Glide.with(AddAttachmentActivity.this).load(imageFile).into(ivImage);
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
        showAppProgressDialog();
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
                            if (size >= 20) {
                                addAttachmentLayout.setVisibility(View.GONE);
                            } else {
                                addAttachmentLayout.setVisibility(View.VISIBLE);
                            }
                            //brandStandardAuditAdapter.notifyDataSetChanged();
                        }
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
                AppLogger.e(TAG, "GetAttachmentError: " + error.getMessage());
                //AppUtils.toast((BaseActivity) context, "Server temporary unavailable, Please try again");
                Toast.makeText(getApplicationContext(), "Server temporary unavailable, Please try again", Toast.LENGTH_SHORT).show();

            }
        };

        String url = ApiEndPoints.BSATTACHMENT + "?"
                + "audit_id=" + auditId + "&"
                + "section_group_id=" + sectionGroupId + "&"
                + "section_id=" + sectionId;
        GetReportRequest getReportRequest = new GetReportRequest(AppPrefs.getAccessToken(context),
                url, stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(getReportRequest);
    }

    public void getQuestionAttachmentList() {
        showAppProgressDialog();
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
                            if (size >= 20) {
                                addAttachmentLayout.setVisibility(View.GONE);
                            } else {
                                addAttachmentLayout.setVisibility(View.VISIBLE);
                            }
                            //brandStandardAuditAdapter.notifyDataSetChanged();
                        }
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
                AppLogger.e(TAG, "GetAttachmentError: " + error.getMessage());
                //AppUtils.toast((BaseActivity) context, "Server temporary unavailable, Please try again");
                Toast.makeText(getApplicationContext(), "Server temporary unavailable, Please try again", Toast.LENGTH_SHORT).show();
            }
        };

        String url = ApiEndPoints.BSATTACHMENT + "?"
                + "audit_id=" + auditId + "&"
                + "section_group_id=" + sectionGroupId + "&"
                + "section_id=" + sectionId + "&"
                + "question_id=" + questionId;
        GetReportRequest getReportRequest = new GetReportRequest(AppPrefs.getAccessToken(context),
                url, stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(getReportRequest);
    }

    public void getDsAttachmentList() {
        showAppProgressDialog();
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
                            if (size >= 20) {
                                addAttachmentLayout.setVisibility(View.GONE);
                            } else {
                                addAttachmentLayout.setVisibility(View.VISIBLE);
                            }
                            //brandStandardAuditAdapter.notifyDataSetChanged();
                        }
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
                AppLogger.e(TAG, "GetAttachmentError: " + error.getMessage());
                //AppUtils.toast((BaseActivity) context, "Server temporary unavailable, Please try again");
                Toast.makeText(getApplicationContext(), "Server temporary unavailable, Please try again", Toast.LENGTH_SHORT).show();

            }
        };

        String url = ApiEndPoints.DSATTACHMENT + "?"
                + "audit_id=" + auditId + "&"
                + "section_group_id=" + sectionGroupId + "&"
                + "section_id=" + sectionId;
        GetReportRequest getReportRequest = new GetReportRequest(AppPrefs.getAccessToken(context),
                url, stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(getReportRequest);
    }

    public void getEsAttachmentList() {
        showAppProgressDialog();
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
                            if (size >= 20) {
                                addAttachmentLayout.setVisibility(View.GONE);
                            } else {
                                addAttachmentLayout.setVisibility(View.VISIBLE);
                            }
                            //brandStandardAuditAdapter.notifyDataSetChanged();
                        }
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
                AppLogger.e(TAG, "GetAttachmentError: " + error.getMessage());
                //AppUtils.toast((BaseActivity) context,"Server temporary unavailable, Please try again");
                Toast.makeText(getApplicationContext(), "Server temporary unavailable, Please try again", Toast.LENGTH_SHORT).show();

            }
        };

        String url = ApiEndPoints.ESATTACHMENT + "?"
                + "audit_id=" + auditId;
        GetReportRequest getReportRequest = new GetReportRequest(AppPrefs.getAccessToken(context),
                url, stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(getReportRequest);
    }

    private void addBsFileAttachment(byte[] imageByteData, String description) {
        //showProgressDialog();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLogger.e(TAG, "AddAttachmentResponse: " + response);
                try {
                    JSONObject object = new JSONObject(response);

                    if (!object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        Toast.makeText(getApplicationContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();
                        //customDialog.dismiss();
                        /*BrandStandardRootObject brandStandardRootObject = new GsonBuilder().create()
                                .fromJson(object.toString(), BrandStandardRootObject.class);
                        if (brandStandardRootObject.getData() != null &&
                                brandStandardRootObject.getData().toString().length() > 0) {
                            setQuestionList(brandStandardRootObject.getData());
                            //brandStandardAuditAdapter.notifyDataSetChanged();
                        }*/
                        getBsAttachmentList();
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
                AppLogger.e(TAG, "AddAttachmentError: " + error.getMessage());
                //AppUtils.toast((BaseActivity) context, "Server temporary unavailable, Please try again");
                Toast.makeText(getApplicationContext(), "Server temporary unavailable, Please try again", Toast.LENGTH_SHORT).show();

            }
        };

        String url = ApiEndPoints.BSATTACHMENT;
        String fileName = "GDI-" + date + ".jpeg";
        AddBSAttachmentRequest addBSAttachmentRequest = new AddBSAttachmentRequest(
                AppPrefs.getAccessToken(context), url, fileName, imageByteData, auditId,
                sectionGroupId, sectionId, description, "0", latitude, longitude,
                stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(addBSAttachmentRequest);
    }

    private void addQuestionFileAttachment(byte[] imageByteData, String description) {
        //showProgressDialog();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLogger.e(TAG, "AddAttachmentResponse: " + response);
                try {
                    JSONObject object = new JSONObject(response);

                    if (!object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        Toast.makeText(getApplicationContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();
                        //customDialog.dismiss();
                        /*BrandStandardRootObject brandStandardRootObject = new GsonBuilder().create()
                                .fromJson(object.toString(), BrandStandardRootObject.class);
                        if (brandStandardRootObject.getData() != null &&
                                brandStandardRootObject.getData().toString().length() > 0) {
                            setQuestionList(brandStandardRootObject.getData());
                            //brandStandardAuditAdapter.notifyDataSetChanged();
                        }*/
                        getQuestionAttachmentList();
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
                AppLogger.e(TAG, "AddAttachmentError: " + error.getMessage());
                //AppUtils.toast((BaseActivity) context, "Server temporary unavailable, Please try again");
                Toast.makeText(getApplicationContext(), "Server temporary unavailable, Please try again", Toast.LENGTH_SHORT).show();

            }
        };

        String url = ApiEndPoints.BSATTACHMENT;
        String fileName = "GDI-" + date + ".jpeg";
        AddQuestionAttachmentRequest addBSAttachmentRequest = new AddQuestionAttachmentRequest(
                AppPrefs.getAccessToken(context), url, fileName, imageByteData, auditId,
                sectionGroupId, sectionId, questionId, description, "0", latitude, longitude, stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(addBSAttachmentRequest);
    }

    private void addDsFileAttachment(byte[] imageByteData, String description) {
        //showProgressDialog();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLogger.e(TAG, "AddAttachmentResponse: " + response);
                try {
                    JSONObject object = new JSONObject(response);

                    if (!object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        Toast.makeText(getApplicationContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();
                        //customDialog.dismiss();
                        /*BrandStandardRootObject brandStandardRootObject = new GsonBuilder().create()
                                .fromJson(object.toString(), BrandStandardRootObject.class);
                        if (brandStandardRootObject.getData() != null &&
                                brandStandardRootObject.getData().toString().length() > 0) {
                            setQuestionList(brandStandardRootObject.getData());
                            //brandStandardAuditAdapter.notifyDataSetChanged();
                        }*/
                        getDsAttachmentList();
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
                AppLogger.e(TAG, "AddAttachmentError: " + error.getMessage());
                //AppUtils.toast((BaseActivity) context, "Server temporary unavailable, Please try again");
                Toast.makeText(getApplicationContext(), "Server temporary unavailable, Please try again", Toast.LENGTH_SHORT).show();

            }
        };

        String url = ApiEndPoints.DSATTACHMENT;
        String fileName = "GDI-" + date + ".jpeg";
        AddDSAttachmentRequest addBSAttachmentRequest = new AddDSAttachmentRequest(
                AppPrefs.getAccessToken(context), url, fileName, imageByteData, auditId,
                sectionGroupId, sectionId, description, latitude, longitude, stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(addBSAttachmentRequest);
    }

    private void addEsFileAttachment(byte[] imageByteData, String description) {
        //showProgressDialog();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLogger.e(TAG, "AddAttachmentResponse: " + response);
                try {
                    JSONObject object = new JSONObject(response);

                    if (!object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        Toast.makeText(getApplicationContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();
                        //customDialog.dismiss();
                        /*BrandStandardRootObject brandStandardRootObject = new GsonBuilder().create()
                                .fromJson(object.toString(), BrandStandardRootObject.class);
                        if (brandStandardRootObject.getData() != null &&
                                brandStandardRootObject.getData().toString().length() > 0) {
                            setQuestionList(brandStandardRootObject.getData());
                            //brandStandardAuditAdapter.notifyDataSetChanged();
                        }*/
                        getEsAttachmentList();
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
                AppLogger.e(TAG, "AddAttachmentError: " + error.getMessage());
                //AppUtils.toast((BaseActivity) context, "Server temporary unavailable, Please try again");
                Toast.makeText(getApplicationContext(), "Server temporary unavailable, Please try again", Toast.LENGTH_SHORT).show();

            }
        };

        String url = ApiEndPoints.ESATTACHMENT;
        String fileName = "GDI-" + date + ".jpeg";
        AddESAttachmentRequest addBSAttachmentRequest = new AddESAttachmentRequest(
                AppPrefs.getAccessToken(context), url, fileName, imageByteData, auditId,
                description, latitude, longitude, stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(addBSAttachmentRequest);
    }

    public class AddAttachmentListAdapter extends RecyclerView.Adapter<AddAttachmentListAdapter.AddAttachmentListViewHolder> {

        private ArrayList<Uri> imageURI;
        Context context;
        byte[] imageByteData = new byte[0];

        public AddAttachmentListAdapter(Context context, ArrayList<Uri> imageURI) {
            this.imageURI = imageURI;
            this.context = context;
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

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);

                if (bitmap != null) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
                    imageByteData = byteArrayOutputStream.toByteArray();
                    Log.e("Image Byte Data : ", "" + imageByteData);
                    holder.imageView.setImageBitmap(bitmap);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            holder.submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppUtils.hideKeyboard(context, view);
                    String text = "Test";
                    if (holder.description.getText().toString().length() > 0){
                        text = holder.description.getText().toString();
                    }
                    switch (attachType) {
                        case "bsSection":
                            addBsFileAttachment(imageByteData, text);
                            imageURI.remove(position);
                            notifyDataSetChanged();
                            break;
                        case "bsQuestion":
                            addQuestionFileAttachment(imageByteData, text);
                            imageURI.remove(position);
                            notifyDataSetChanged();
                            break;
                        case "dsSection":
                            addDsFileAttachment(imageByteData, text);
                            imageURI.remove(position);
                            notifyDataSetChanged();
                            break;
                        case "esSection":
                            addEsFileAttachment(imageByteData, text);
                            imageURI.remove(position);
                            notifyDataSetChanged();
                            break;
                    }
                }
            });
            holder.cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (imageURI.size() <= 1){
                        customDialog.dismiss();
                    }else {
                        imageURI.remove(position);
                        notifyDataSetChanged();
                    }
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

            public AddAttachmentListViewHolder(View itemView) {
                super(itemView);

                imageView = itemView.findViewById(R.id.iv_attached_image);
                description = itemView.findViewById(R.id.et_description);
                submitButton = itemView.findViewById(R.id.tv_submit_btn);
                cancelButton = itemView.findViewById(R.id.tv_cancel_btn);
            }
        }
    }

    /*public class ViewPagerAdapter extends PagerAdapter {

        private ArrayList<String> imageUrl;
        Context context;
        byte[] imageByteData = new byte[0];

        public ViewPagerAdapter(Context context, ArrayList<String> imageUrl) {
            this.imageUrl = imageUrl;
            this.context = context;
        }

        @Override
        public int getCount() {
            return imageUrl.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {
            View itemView = LayoutInflater.from(container.getContext())
                    .inflate(R.layout.fragment_add_attachment, container, false);
            ImageView imageView = itemView.findViewById(R.id.iv_attached_image);
            final EditText description = itemView.findViewById(R.id.et_description);
            TextView submitButton = itemView.findViewById(R.id.tv_submit_btn);
            TextView cancelButton = itemView.findViewById(R.id.tv_cancel_btn);

            *//*Uri uri = imageUrl.get(position);

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);

                if (bitmap != null) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
                    imageByteData = byteArrayOutputStream.toByteArray();
                    Log.e("Image Byte Data : ", "" + imageByteData);
                    imageView.setImageBitmap(bitmap);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }*//*

            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppUtils.hideKeyboard(context, view);
                    String text = "";
                    if (description.getText().toString().length() > 0){
                        text = description.getText().toString();
                    }
                    switch (attachType) {
                        case "bsSection":
                            addBsFileAttachment(imageByteData, text);
                            break;
                        case "bsQuestion":
                            addQuestionFileAttachment(imageByteData, text);
                            break;
                        case "dsSection":
                            addDsFileAttachment(imageByteData, text);
                            break;
                        case "esSection":
                            addEsFileAttachment(imageByteData, text);
                            break;
                    }
                }
            });
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout) object);
        }

        private void viewProductImage(Uri productImagesInfo){
            Intent intent = new Intent(context, ImageViewActivity.class);
            //intent.putExtra("image", productImagesInfo.getImageURl());
            context.startActivity(intent);
        }

    }*/


    /*private void addDescriptionDialog(final byte[] imageByteData) {
        //final CustomDialog customDialog = new CustomDialog(context, R.layout.attachment_description_dailog);
        customDialog.setCancelable(false);

        getLatLong();

        final EditText description = customDialog.findViewById(R.id.et_description);
        TextView saveBtn = customDialog.findViewById(R.id.tv_save_btn);
        TextView cancelBtn = customDialog.findViewById(R.id.tv_cancel_btn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtils.hideKeyboard(context, view);
                switch (attachType) {
                    case "bsSection":
                        addBsFileAttachment(imageByteData, description.getText().toString());
                        break;
                    case "bsQuestion":
                        addQuestionFileAttachment(imageByteData, description.getText().toString());
                        break;
                    case "dsSection":
                        addDsFileAttachment(imageByteData, description.getText().toString());
                        break;
                    case "esSection":
                        addEsFileAttachment(imageByteData, description.getText().toString());
                        break;
                }
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog.dismiss();
            }
        });
        customDialog.show();
    }*/

}
