package com.gdi.activity.Audit;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gdi.R;
import com.gdi.activity.BaseActivity;
import com.gdi.adapter.AddAttachmentAdapter;
import com.gdi.api.AddBSAttachmentRequest;
import com.gdi.api.AddDSAttachmentRequest;
import com.gdi.api.AddESAttachmentRequest;
import com.gdi.api.AddQuestionAttachmentRequest;
import com.gdi.api.ApiEndPoints;
import com.gdi.api.DeleteBSAttachmentRequest;
import com.gdi.api.EditBSAttachmentRequest;
import com.gdi.api.EditDSAttachmentRequest;
import com.gdi.api.EditESAttachmentRequest;
import com.gdi.api.GetReportRequest;
import com.gdi.api.VolleyNetworkRequest;
import com.gdi.model.audit.AddAttachment.AddAttachmentInfo;
import com.gdi.model.audit.AddAttachment.AddAttachmentRootObject;
import com.gdi.utils.ApiResponseKeys;
import com.gdi.utils.AppLogger;
import com.gdi.utils.AppPrefs;
import com.gdi.utils.AppUtils;
import com.gdi.utils.CustomDialog;
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
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddAttachmentActivity extends BaseActivity implements View.OnClickListener {

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
    String mCurrentPhotoPath = "";
    String auditId = "";
    String sectionGroupId = "";
    String sectionId = "";
    String questionId = "";
    String attachType = "";
    String attachmentCount = "";
    private String editable = "";
    String date = "";
    Context context;
    CustomDialog customDialog;
    private static final String TAG = AddAttachmentActivity.class.getSimpleName();

    @Override
    protected void onResume() {
        super.onResume();
        if (attachType.equals("bsSection")) {
            getBsAttachmentList();
        } else if (attachType.equals("bsQuestion")) {
            getQuestionAttachmentList();
        } else if (attachType.equals("dsSection")) {
            getDsAttachmentList();
        } else if (attachType.equals("esSection")) {
            getEsAttachmentList();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_attachment);
        context = this;
        ButterKnife.bind(AddAttachmentActivity.this);
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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar();
        date = AppUtils.getDate(Calendar.getInstance().getTime());
        customDialog = new CustomDialog(context, R.layout.attachment_description_dailog);
        attachmentList = (RecyclerView) findViewById(R.id.recycler_view_attachment_list);
        addAttachmentLayout = (LinearLayout) findViewById(R.id.add_attachment_layout);
        add_attachment_text = (TextView) findViewById(R.id.add_attachment_text);
        addAttachmentBtn = (FloatingActionButton) findViewById(R.id.floating_btn_add_attachment);
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
                cameraPermission();
                break;
        }
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

            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
            byte[] imageByteData = new byte[0];
            //Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.fromFile(file));
            if (bitmap != null) {
                Bitmap rotateBitmap = callRotateImage(bitmap);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                rotateBitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
                imageByteData = byteArrayOutputStream.toByteArray();
                Log.e("Image Byte Data : ", "" + imageByteData);
            }

            addDescriptionDialog(imageByteData);
        }
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

    private void addDescriptionDialog(final byte[] imageByteData) {
        //final CustomDialog customDialog = new CustomDialog(context, R.layout.attachment_description_dailog);
        customDialog.setCancelable(false);

        final EditText description = (EditText) customDialog.findViewById(R.id.et_description);
        TextView saveBtn = (TextView) customDialog.findViewById(R.id.tv_save_btn);
        TextView cancelBtn = (TextView) customDialog.findViewById(R.id.tv_cancel_btn);

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
        showProgressDialog();
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
                            if (size >= 2) {
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
                AppUtils.toast((BaseActivity) context, "Server temporary unavailable, Please try again");

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
        showProgressDialog();
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
                            if (size >= 2) {
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
                AppUtils.toast((BaseActivity) context, "Server temporary unavailable, Please try again");

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
        showProgressDialog();
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
                            if (size >= 2) {
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
                AppUtils.toast((BaseActivity) context, "Server temporary unavailable, Please try again");

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
        showProgressDialog();
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
                            if (size >= 2) {
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
                AppUtils.toast((BaseActivity) context, "Server temporary unavailable, Please try again");

            }
        };

        String url = ApiEndPoints.ESATTACHMENT + "?"
                + "audit_id=" + auditId;
        GetReportRequest getReportRequest = new GetReportRequest(AppPrefs.getAccessToken(context),
                url, stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(getReportRequest);
    }

    private void addBsFileAttachment(byte[] imageByteData, String description) {
        showProgressDialog();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLogger.e(TAG, "AddAttachmentResponse: " + response);
                try {
                    JSONObject object = new JSONObject(response);

                    if (!object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        customDialog.dismiss();
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
                hideProgressDialog();
            }

        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                AppLogger.e(TAG, "AddAttachmentError: " + error.getMessage());
                AppUtils.toast((BaseActivity) context, "Server temporary unavailable, Please try again");

            }
        };

        String url = ApiEndPoints.BSATTACHMENT;
        String fileName = "GDI-" + date + ".jpeg";
        AddBSAttachmentRequest addBSAttachmentRequest = new AddBSAttachmentRequest(
                AppPrefs.getAccessToken(context), url, fileName, imageByteData, auditId,
                sectionGroupId, sectionId, description, "0", stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(addBSAttachmentRequest);
    }

    private void addQuestionFileAttachment(byte[] imageByteData, String description) {
        showProgressDialog();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLogger.e(TAG, "AddAttachmentResponse: " + response);
                try {
                    JSONObject object = new JSONObject(response);

                    if (!object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        customDialog.dismiss();
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
                hideProgressDialog();
            }

        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                AppLogger.e(TAG, "AddAttachmentError: " + error.getMessage());
                AppUtils.toast((BaseActivity) context, "Server temporary unavailable, Please try again");

            }
        };

        String url = ApiEndPoints.BSATTACHMENT;
        String fileName = "GDI-" + date + ".jpeg";
        AddQuestionAttachmentRequest addBSAttachmentRequest = new AddQuestionAttachmentRequest(
                AppPrefs.getAccessToken(context), url, fileName, imageByteData, auditId,
                sectionGroupId, sectionId, questionId, description, "0", stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(addBSAttachmentRequest);
    }

    private void addDsFileAttachment(byte[] imageByteData, String description) {
        showProgressDialog();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLogger.e(TAG, "AddAttachmentResponse: " + response);
                try {
                    JSONObject object = new JSONObject(response);

                    if (!object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        customDialog.dismiss();
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
                hideProgressDialog();
            }

        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                AppLogger.e(TAG, "AddAttachmentError: " + error.getMessage());
                AppUtils.toast((BaseActivity) context, "Server temporary unavailable, Please try again");

            }
        };

        String url = ApiEndPoints.DSATTACHMENT;
        String fileName = "GDI-" + date + ".jpeg";
        AddDSAttachmentRequest addBSAttachmentRequest = new AddDSAttachmentRequest(
                AppPrefs.getAccessToken(context), url, fileName, imageByteData, auditId,
                sectionGroupId, sectionId, description, stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(addBSAttachmentRequest);
    }

    private void addEsFileAttachment(byte[] imageByteData, String description) {
        showProgressDialog();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLogger.e(TAG, "AddAttachmentResponse: " + response);
                try {
                    JSONObject object = new JSONObject(response);

                    if (!object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        customDialog.dismiss();
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
                hideProgressDialog();
            }

        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                AppLogger.e(TAG, "AddAttachmentError: " + error.getMessage());
                AppUtils.toast((BaseActivity) context, "Server temporary unavailable, Please try again");

            }
        };

        String url = ApiEndPoints.ESATTACHMENT;
        String fileName = "GDI-" + date + ".jpeg";
        AddESAttachmentRequest addBSAttachmentRequest = new AddESAttachmentRequest(
                AppPrefs.getAccessToken(context), url, fileName, imageByteData, auditId,
                description, stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(addBSAttachmentRequest);
    }


}
