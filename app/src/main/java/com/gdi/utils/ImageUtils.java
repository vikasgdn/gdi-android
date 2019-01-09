package com.gdi.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.style.ImageSpan;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by Amit on 28/09/17.
 */

public class ImageUtils {

    /**
     * Takes source image uri and save it to target path
     * @param sourceUri
     * @param targetPath
     */
    public static void saveImage(Uri sourceUri, String targetPath) {
        if (sourceUri == null){
            throw new IllegalArgumentException("sourceUri can not be null");
        }
        if (targetPath == null){
            throw new IllegalArgumentException("targetPath can not be null");
        }

        String sourceFilename = sourceUri.getPath();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {

            bis = new BufferedInputStream(new FileInputStream(sourceFilename));
            bos = new BufferedOutputStream(new FileOutputStream(targetPath, false));
            byte[] buf = new byte[1024];
            bis.read(buf);
            do {
                bos.write(buf);
            } while (bis.read(buf) != -1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) bis.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void saveImage(String sourcePath, String targetPath) {
        if (sourcePath == null){
            throw new IllegalArgumentException("sourcePath can not be null");
        }
        if (targetPath == null){
            throw new IllegalArgumentException("targetPath can not be null");
        }

        String sourceFilename = sourcePath;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {

            bis = new BufferedInputStream(new FileInputStream(sourceFilename));
            bos = new BufferedOutputStream(new FileOutputStream(targetPath, false));
            byte[] buf = new byte[1024];
            bis.read(buf);
            do {
                bos.write(buf);
            } while (bis.read(buf) != -1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) bis.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static File createImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }

    public static File createImageFileCache(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalCacheDir();
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }

    public static boolean deleteImage(String path){
        File file = new File(path);
        if (file.exists()){
            return  file.delete();
        }
        return false;
    }
    public static byte[] getFileDataFromPath(Context context, String path) {

        try {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            Uri uri = Uri.fromFile(new File(path));
            bitmap = rotateImageIfRequired(bitmap, context, uri);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
    /**
     * Turn drawable into byte array.
     *
     * @param drawable data
     * @return byte array
     */
    public static byte[] getFileDataFromDrawable(Context context, Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


    public static Bitmap rotateImageIfRequired(Bitmap img, Context context, Uri selectedImage) throws IOException {

        if (selectedImage.getScheme().equals("content")) {
            String[] projection = { MediaStore.Images.ImageColumns.ORIENTATION };
            Cursor c = context.getContentResolver().query(selectedImage, projection, null, null, null);
            if (c.moveToFirst()) {
                final int rotation = c.getInt(0);
                c.close();
                return rotateImage(img, rotation);
            }
            return img;
        } else {
            ExifInterface ei = new ExifInterface(selectedImage.getPath());
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return rotateImage(img, 90);
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return rotateImage(img, 180);
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return rotateImage(img, 270);
                default:
                    return img;
            }
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        return rotatedImg;
    }

    public static ImageSpan getImageSpan(int imageId, Context context){
        Drawable d = context.getResources().getDrawable(imageId);
        d.setBounds(0, 0, 64, 64);
        return new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);
    }

    public static File[] getIStickerLocalGalleryImages(Context context) {
        File folderName = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] files = folderName.listFiles();
        if (files != null && files.length > 0) {
            Arrays.sort(files, new Comparator<File>() {
                @Override
                public int compare(File object1, File object2) {
                    return (int) ((object1.lastModified() > object2.lastModified())
                            ? object1.lastModified() : object2.lastModified());
                }
            });
        }

        return files;
    }


}
