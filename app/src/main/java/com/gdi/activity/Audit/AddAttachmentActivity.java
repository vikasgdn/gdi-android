package com.gdi.activity.Audit;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.activity.BaseActivity;
import com.gdi.utils.AppLogger;
import com.gdi.utils.AppUtils;
import com.gdi.utils.PermissionUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddAttachmentActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view_attachment_list)
    RecyclerView attachmentList;
    @BindView(R.id.floating_btn_add_attachment)
    FloatingActionButton addAttachmentBtn;
    private static final int REQUEST_FOR_CAMERA = 100;
    private static final int REQUEST_TAKE_PHOTO = 101;
    String mCurrentPhotoPath = "";
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_attachment);
        context = this;
        ButterKnife.bind(AddAttachmentActivity.this);
        initView();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar();
        attachmentList = (RecyclerView) findViewById(R.id.recycler_view_attachment_list);
        addAttachmentBtn = (FloatingActionButton) findViewById(R.id.floating_btn_add_attachment);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.recycler_view_attachment_list:
                break;
            case R.id.floating_btn_add_attachment:
                cameraPermission();
                break;
        }
    }

    private void cameraPermission(){
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
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
                imageByteData = byteArrayOutputStream.toByteArray();
                Log.e("Image Byte Data : ", "" + imageByteData);
            }

            addDescriptionDialog(imageByteData, bitmap);
        }
    }

    private void addDescriptionDialog(byte[] imageByteData, Bitmap bitmap) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.attachment_description_dailog, null);
        dialog.setView(view);

        final EditText description = (EditText) view.findViewById(R.id.et_description);
        TextView saveBtn = (TextView) view.findViewById(R.id.tv_save_btn);
        TextView cancelBtn = (TextView) view.findViewById(R.id.tv_cancel_btn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        dialog.create().show();
    }

    private void setActionBar() {
        initToolbar(toolbar);
        setTitle("Attachments");
        enableBack(true);
        enableBackPressed();
    }


}
