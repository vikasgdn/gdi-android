package com.gdi.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gdi.R;
import com.gdi.api.GetProfileRequest;
import com.gdi.api.UpdateProfileRequest;
import com.gdi.api.VolleyNetworkRequest;
import com.gdi.model.GetProfileModel;
import com.gdi.utils.ApiResponseKeys;
import com.gdi.utils.AppConstant;
import com.gdi.utils.AppLogger;
import com.gdi.utils.AppPrefs;
import com.gdi.utils.AppUtils;
import com.gdi.utils.CustomDialog;
import com.gdi.utils.CustomTypefaceTextView;
import com.gdi.utils.PermissionUtils;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.user_profile_image)
    CircleImageView userProfileImage;
    @BindView(R.id.user_first_name)
    EditText userFirstName;
    @BindView(R.id.user_last_name)
    EditText userLastName;
    @BindView(R.id.user_email_id)
    EditText userEmailId;
    @BindView(R.id.user_phone_no)
    EditText userPhone;
    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.user_role)
    EditText userRole;
    @BindView(R.id.save_btn)
    Button saveBtn;
    @BindView(R.id.edit_profile)
    ImageView editProfile;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    Context context;
    private CustomDialog customDialog;
    private static final int GALLERY_PERMISSION_REQUEST = 100;
    private static final int SELECT_IMAGES_FROM_GALLERY = 101;
    private static final int CAMERA_PERMISSION_REQUEST = 102;
    private static final int REQUEST_TAKE_PHOTO = 104;
    private String mCurrentFullPhotoPath;
    private Uri fullPhotoURI;
    private File targetFile = null;
    private String fileName = "";
    String path = "";
    private static final int REQUEST_FOR_CAMERA = 109;
    private String imageFileName;
    byte[] imageByteData;
    GetProfileModel getProfileModel = new GetProfileModel();
    private static final String TAG = UserProfileActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        context = this;
        ButterKnife.bind(UserProfileActivity.this);
        initView();
    }

    private void initView(){
        toolbar = findViewById(R.id.toolbar);
        setActionBar();
        userProfileImage = findViewById(R.id.user_profile_image);
        userFirstName = findViewById(R.id.user_first_name);
        userLastName = findViewById(R.id.user_last_name);
        userEmailId = findViewById(R.id.user_email_id);
        userPhone = findViewById(R.id.user_phone_no);
        username = findViewById(R.id.username);
        userRole = findViewById(R.id.user_role);
        saveBtn = findViewById(R.id.save_btn);
        editProfile = findViewById(R.id.edit_profile);
        editProfile.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        userProfileImage.setOnClickListener(this);
        getProfile();
        userFirstName.setSelection(userFirstName.getText().toString().length());

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.edit_profile:
                saveBtn.setVisibility(View.VISIBLE);
                editProfile.setVisibility(View.GONE);
                userFirstName.setEnabled(true);
                userLastName.setEnabled(true);
                userPhone.setEnabled(true);
                userEmailId.setEnabled(true);
                username.setEnabled(true);
                userPhone.setEnabled(true);
                userRole.setEnabled(true);
                userProfileImage.setEnabled(true);
                break;
            case R.id.save_btn:
                AppUtils.hideKeyboard(context, view);
                if (validateInputs()){
                    updateProfile();
                }
                break;
            case R.id.user_profile_image:
                openDCRDialog();
                break;
        }
    }

    public void getProfile() {
        showProgressDialog();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLogger.e(TAG, "Filter Response: " + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (!object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        GetProfileModel getProfileModel = new GsonBuilder().create()
                                .fromJson(""+object.get("data"), GetProfileModel.class);
                        setProfile(getProfileModel);
                        /*IAFilterRootObject filterRootObject = new GsonBuilder().create()
                                .fromJson(object.toString(), IAFilterRootObject.class);
                        if (filterRootObject.getData() != null &&
                                filterRootObject.getData().toString().length() > 0) {
                            filterInfo = filterRootObject.getData();
                            setFilter(filterInfo);
                        }*/

                    } else if (object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        /*AppUtils.toast((BaseActivity) context,
                                object.getString(ApiResponseKeys.RES_KEY_MESSAGE));*/
                        if (object.getInt(ApiResponseKeys.RES_KEY_CODE) == AppConstant.ERROR){
                            AppUtils.toast((BaseActivity) context,
                                    object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                            finish();
                            startActivity(new Intent(context, SignInActivity.class));
                        }
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
                AppLogger.e(TAG, "Filter Error: " + error.getMessage());

            }
        };
        GetProfileRequest getProfileRequest = new GetProfileRequest(
                AppPrefs.getAccessToken(context), stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(getProfileRequest);
    }

    private void updateProfile() {
        showProgressDialog();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onResponse(String response) {
                AppLogger.e(TAG, "UpdateProfileResponse: " + response);
                try {
                    JSONObject object = new JSONObject(response);
                    String message = object.getString(ApiResponseKeys.RES_KEY_MESSAGE);
                    if (!object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        AppUtils.toast((BaseActivity) context, message);
                        saveBtn.setVisibility(View.GONE);
                        editProfile.setVisibility(View.VISIBLE);
                        userFirstName.setEnabled(false);
                        userLastName.setEnabled(false);
                        userEmailId.setEnabled(false);
                        username.setEnabled(false);
                        userPhone.setEnabled(false);
                        userRole.setEnabled(false);
                        userProfileImage.setEnabled(true);
                    }else if (object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        AppUtils.toast((BaseActivity) context, message);

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
                error.printStackTrace();
            }
        };
        UpdateProfileRequest signInRequest = new UpdateProfileRequest(
                getProfileModel, AppPrefs.getAccessToken(context), stringListener, errorListener);
        VolleyNetworkRequest.getInstance(UserProfileActivity.this).addToRequestQueue(signInRequest);
    }

    private boolean validateInputs() {
        boolean validate = true;

        if (userFirstName.getText().toString().length() <= 0) {
            validate = false;
            userFirstName.setError(getString(R.string.enter_first_name));
        } else if (username.getText().toString().length() <= 0) {
            validate = false;
            username.setError(getString(R.string.enter_username));
        }else if (userPhone.getText().toString().length() <= 0) {
            validate = false;
            userPhone.setError(getString(R.string.enter_phone));
        }else if (!AppUtils.isValidPhoneNumber(userPhone.getText().toString())) {
            validate = false;
            userPhone.setError(getString(R.string.enter_valid_phone));
        }
        if(validate){
            getProfileModel.setFirst_name(userFirstName.getText().toString());
            getProfileModel.setLast_name(userLastName.getText().toString());
            getProfileModel.setEmail(userEmailId.getText().toString());
            getProfileModel.setUsername(username.getText().toString());
            getProfileModel.setPhone(userPhone.getText().toString());
            getProfileModel.setImage_url(path);
        }
        return validate;
    }

    private void setProfile(GetProfileModel getProfileModel){
        userFirstName.setText(getProfileModel.getFirst_name());
        userLastName.setText(getProfileModel.getLast_name());
        userEmailId.setText(getProfileModel.getEmail());
        username.setText(getProfileModel.getUsername());
        userPhone.setText(getProfileModel.getPhone());
        userRole.setText(getProfileModel.getRole());

        if (!AppUtils.isStringEmpty(getProfileModel.getImage_url())) {
            Glide.with(context)
                    .load(getProfileModel.getImage_url())
                    .apply(new RequestOptions().placeholder(R.mipmap.image_icon)
                            .dontAnimate().centerCrop())
                    .into(userProfileImage);
        } else userProfileImage.setImageResource(R.mipmap.image_icon);
    }

    private void openDCRDialog(){
        customDialog = new CustomDialog(context, R.layout.upload_image_dailog);
        customDialog.setCancelable(true);
        //customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
        //CustomTypefaceTextView tv_subTile = (CustomTypefaceTextView) customDialog.findViewById(R.id.tv_subTile_footfall);
        //tv_subTile.setText("D C R");
        CustomTypefaceTextView tvGallery = customDialog.findViewById(R.id.tv_gallery);
        CustomTypefaceTextView tvCamera = customDialog.findViewById(R.id.tv_camera);
        CustomTypefaceTextView tvCancel = customDialog.findViewById(R.id.tv_cancel);

        tvGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAndRequestGalleryPermissions()){
                    chooseImagesFromGallery();
                }
            }
        });
        tvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted
                    ActivityCompat.requestPermissions(UserProfileActivity.this,
                            new String[]{Manifest.permission.CAMERA}, REQUEST_FOR_CAMERA);

                } else {
                    //dispatchTakePictureIntent();
                    takePhotoFromCamera();

                }
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
            }
        });
        customDialog.show();

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

    private void chooseImagesFromGallery() {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, SELECT_IMAGES_FROM_GALLERY);
        customDialog.dismiss();

    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
        customDialog.dismiss();
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
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;

            case GALLERY_PERMISSION_REQUEST:
                boolean isGalleryPermissionGranted = PermissionUtils.checkPermissionGrantedOrNot(
                        UserProfileActivity.this, permissions, grantResults);
                if (isGalleryPermissionGranted) {
                    chooseImagesFromGallery();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_IMAGES_FROM_GALLERY && resultCode == RESULT_OK) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    /*InputStream mInputStream = getContentResolver().openInputStream(contentURI);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 8;
                    Bitmap bitmap = BitmapFactory.decodeStream(mInputStream,null,options);
                    path = "data:image/jpg;base64," + encodeTobase64(bitmap);
                    userProfileImage.setImageBitmap(bitmap);*/

                  /*  Bitmap bitmapThumbnail = MediaStore.Images.Thumbnails.getThumbnail(
                            getContentResolver(), contentURI,
                            MediaStore.Images.Thumbnails.MINI_KIND,
                            (BitmapFactory.Options) null );*/

                   /*Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                   Bitmap bitmapThumbnail =  ThumbnailUtils.extractThumbnail(bitmap,100,100 );*/

                   // saveImage(bitmapThumbnail);
                    //path = "data:image/jpg;base64," + encodeTobase64(bitmapThumbnail);
                    //path = "data:image/jpg;base64,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";
                    //AppLogger.d("Base64Image",  path);
                    //userProfileImage.setImageBitmap(bitmapThumbnail);



                    //Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
                    File file = new File(String.valueOf(contentURI));
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), contentURI);
                    if (bitmap != null) {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream);
                        //imageByteData = byteArrayOutputStream.toByteArray();
                        //String base64 = encodeTobase64(bitmap);
                        imageFileName = file.getName();
                        path = "data:image/jpg;base64," + encodeTobase64(bitmap);
                        AppLogger.e("Image Byte Data : ",  "" + imageByteData);
                        AppLogger.e("Base64Image",  path);
                        userProfileImage.setImageBitmap(bitmap);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Bitmap bitmapThumbnail = (Bitmap) data.getExtras().get("data");
            userProfileImage.setImageBitmap(bitmapThumbnail);
            path = "data:image/jpg;base64," + encodeTobase64(bitmapThumbnail);
            AppLogger.d("Base64Image",  path);
            saveImage(bitmapThumbnail);
        }
    }

    public String encodeTobase64(Bitmap image) {
        Bitmap immagex=image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        imageByteData = b;
        String imageEncoded = Base64.encodeToString(b,Base64.DEFAULT);
        String test = imageEncoded.replace("\n", "");

        Log.e("LOOK", imageEncoded);
        Log.e("LOOK", test);

        return test;
    }

    public  String getBase64FromFile(Bitmap myBitmap) {
        //Bitmap bmp = null;
        ByteArrayOutputStream baos = null;
        byte[] byteData = null;
        String encodeString = null;
        try
        {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();

             encodeString = Base64.encodeToString(byteArray, Base64.DEFAULT);

            /*//bmp = BitmapFactory.decodeFile(path);
            baos = new ByteArrayOutputStream();
            myBitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos);
            byteData = baos.toByteArray();
            encodeString = Base64.encodeToString(byteData, Base64.DEFAULT);*/
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return encodeString;
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + "/GDI Images");
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    private void setActionBar() {
        initToolbar(toolbar);
        setTitle("User Profile");
        enableBack(true);
        enableBackPressed();
    }
}
