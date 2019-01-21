package com.gdi.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
import com.gdi.utils.ImageUtils;
import com.gdi.utils.PermissionUtils;
import com.google.gson.GsonBuilder;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.graphics.Color.TRANSPARENT;

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
    private String mCurrentPhotoPath;
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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar();
        userProfileImage = (CircleImageView)findViewById(R.id.user_profile_image);
        userFirstName = (EditText)findViewById(R.id.user_first_name);
        userLastName = (EditText)findViewById(R.id.user_last_name);
        userEmailId = (EditText)findViewById(R.id.user_email_id);
        userPhone = (EditText)findViewById(R.id.user_phone_no);
        username = (EditText)findViewById(R.id.username);
        userRole = (EditText)findViewById(R.id.user_role);
        saveBtn = (Button)findViewById(R.id.save_btn);
        editProfile = (ImageView)findViewById(R.id.edit_profile);
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
                        userPhone.setEnabled(false);
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
                    .apply(new RequestOptions().placeholder(R.drawable.image_icon)
                            .dontAnimate().centerCrop())
                    .into(userProfileImage);
        } else userProfileImage.setImageResource(R.drawable.image_icon);
    }

    private void openDCRDialog(){
        customDialog = new CustomDialog(context, R.layout.upload_image_dailog);
        customDialog.setCancelable(true);
        //customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
        //CustomTypefaceTextView tv_subTile = (CustomTypefaceTextView) customDialog.findViewById(R.id.tv_subTile_footfall);
        //tv_subTile.setText("D C R");
        CustomTypefaceTextView tvGallery = (CustomTypefaceTextView) customDialog.findViewById(R.id.tv_gallery);
        CustomTypefaceTextView tvCamera = (CustomTypefaceTextView) customDialog.findViewById(R.id.tv_camera);
        CustomTypefaceTextView tvCancel = (CustomTypefaceTextView) customDialog.findViewById(R.id.tv_cancel);

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

        /*Intent intent = new Intent(this, AlbumSelectActivity.class);
        //set limit on number of images that can be selected, default is 10
        intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 5);
        startActivityForResult(intent, SELECT_IMAGES_FROM_GALLERY);*/


        /*Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)),
                SELECT_IMAGES_FROM_GALLERY);*/
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

        /*try {
            if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK)  {
                Log.e("Image photo path : ", "" + mCurrentPhotoPath);
                File file = new File(mCurrentPhotoPath);
                //Intent galleryIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                //galleryIntent.putExtra( MediaStore.EXTRA_OUTPUT, mCurrentPhotoPath );
                *//*File f = new File(mCurrentPhotoPath);
                Uri picUri = Uri.fromFile(f);
                galleryIntent.setData(picUri);
                this.sendBroadcast(galleryIntent);*//*
                Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
                //Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.fromFile(file));
                if (bitmap != null) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
                    byte[] imageByteData = byteArrayOutputStream.toByteArray();
                    Log.e("Image Byte Data : ", "" + imageByteData);
                    //uplaodedImageView.setVisibility(View.VISIBLE);
                    //uplaodedImageView.setImageBitmap(bitmap);
                    //captureImageBtn.setText("Capture Again");
                    //uploadImageBtn.setVisibility(View.VISIBLE);
                }
            }

        } catch (Exception error) {
            error.printStackTrace();
        }*/

        if (requestCode == SELECT_IMAGES_FROM_GALLERY && resultCode == RESULT_OK) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmapThumbnail = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    saveImage(bitmapThumbnail);
                    path = getBase64FromFile(bitmapThumbnail);
                    //Toast.makeText(MainActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    userProfileImage.setImageBitmap(bitmapThumbnail);

                } catch (IOException e) {
                    e.printStackTrace();
                    //Toast.makeText(MainActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        }

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Bitmap bitmapThumbnail = (Bitmap) data.getExtras().get("data");
            userProfileImage.setImageBitmap(bitmapThumbnail);
            path = getBase64FromFile(bitmapThumbnail);
            saveImage(bitmapThumbnail);
            //Toast.makeText(MainActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }


        /*if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            CropImage.activity(fullPhotoURI)
                    .setOutputCompressQuality(AppConstant.CROPPED_IMAGE_QUALITY)
                    .start(this);


        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);


            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                targetFile = null;
                try {
                    targetFile = ImageUtils.createImageFileCache(this);
                    if (targetFile != null) {
                        ImageUtils.saveImage(resultUri, targetFile.getAbsolutePath());
                        if (targetFile.getAbsolutePath() != null) {
                            uploadThumbnail(targetFile.getAbsolutePath());
                        }


                    } else {
                        AppLogger.e(TAG, "error in saving image");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                error.printStackTrace();
            }
        } else if (requestCode == SELECT_IMAGES_FROM_GALLERY && resultCode == RESULT_OK) {
            if (data != null) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    *//*CropImage.activity(selectedImageUri)
                            .setOutputCompressQuality(AppConstant.CROPPED_IMAGE_QUALITY)
                            .start(this);*//*
                }
            }
        }*/
    }

    public  String getBase64FromFile(Bitmap myBitmap) {
        //Bitmap bmp = null;
        ByteArrayOutputStream baos = null;
        byte[] byteData = null;
        String encodeString = null;
        try
        {
            //bmp = BitmapFactory.decodeFile(path);
            baos = new ByteArrayOutputStream();
            myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byteData = baos.toByteArray();
            encodeString = Base64.encodeToString(byteData, Base64.DEFAULT);
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

    private void uploadThumbnail(final String imagepath) {
        ((BaseActivity) context).showProgressDialog();
        try {
           /* fileName = AppPreferences.get_image_path(activity);
            if (AppUtils.isStringEmpty(fileName))*/
            fileName = UUID.randomUUID().toString();

            Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(imagepath),
                    AppConstant.THUMBSIZE, AppConstant.THUMBSIZE);
            String thumbFilename = "thumb_" + imagepath.substring(imagepath.lastIndexOf("/") + 1);
            File file = savebitmap(thumbFilename, ThumbImage);
            Log.w("", "thumbfilename " + thumbFilename);


        } catch (Exception e) {
            fileName = "";
            ((BaseActivity) context).hideProgressDialog();
            AppUtils.toast((BaseActivity) context,
                    e.getMessage());
        }

    }

    private File savebitmap(String filename, Bitmap thumb) {
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        OutputStream outStream = null;

        File file = new File(extStorageDirectory, filename);
        if (file.exists()) {
            file.delete();
            file = new File(extStorageDirectory, filename);
            Log.e("file exist", "" + file + ",Bitmap= " + filename);
        }
        try {
            // make a new bitmap from your file
            /*  Bitmap bitmap = BitmapFactory.decodeFile(file.getName());*/

            outStream = new FileOutputStream(file);
            thumb.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("file", "" + file);
        return file;

    }

    private void dispatchTakePictureIntent() {
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

    private void setActionBar() {
        initToolbar(toolbar);
        setTitle("User Profile");
        enableBack(true);
        enableBackPressed();
    }
}
