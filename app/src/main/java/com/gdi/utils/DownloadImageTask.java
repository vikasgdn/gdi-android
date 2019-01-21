package com.gdi.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadImageTask {

    private String downloadUrl = "";
    private String downloadFileName = "";
    private ProgressDialog progressDialog;
    private Context context;
    private static final String TAG = DownloadImageTask.class.getSimpleName();
    private ImageDownloadFinishedListner imageDownloadFinishedListner;


    public DownloadImageTask(Context context, String downloadUrl, ImageDownloadFinishedListner imageDownloadFinishedListner) {
        this.context = context;
        this.downloadUrl = downloadUrl;
        this.imageDownloadFinishedListner = imageDownloadFinishedListner;

        downloadFileName = downloadUrl.substring(downloadUrl.lastIndexOf( '/' ),downloadUrl.length());//Create file name by picking download file name from URL
        Log.e(TAG, downloadFileName);

        //Start Downloading Task
        new DownloadingTask().execute();
    }

    private class DownloadingTask extends AsyncTask<Void, Void, Void> {

        File apkStorage = null;
        File outputFile = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(context);
            progressDialog.setMessage("Downloading...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (outputFile != null) {
                    progressDialog.dismiss();
                    imageDownloadFinishedListner.onImageDownloadFinished(outputFile.getAbsolutePath());

                    //Toast.makeText(context, "Downloaded Successfully", Toast.LENGTH_SHORT).show();
                } else {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                        }
                    }, 3000);

                    Log.e(TAG, "Download Failed");

                }
            } catch (Exception e) {
                e.printStackTrace();

                //Change button text if exception occurs

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 3000);
                Log.e(TAG, "Download Failed with Exception - " + e.getLocalizedMessage());

            }
            super.onPostExecute(result);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                //String encodedAuth = Base64.encode(AppPrefs.getAccessToken(context).getBytes(),Base64.DEFAULT);
                URL url = new URL(downloadUrl);//Create Download URl
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();//Open Url Connection
                httpURLConnection.setRequestMethod("GET");//Set Request Method to "GET" since we are grtting data
                httpURLConnection.setRequestProperty ("access-token",AppPrefs.getAccessToken(context));
                httpURLConnection.setDoOutput(false);//connect the URL Connection

                //If Connection response is not OK then show Logs
                if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.e(TAG, "Server returned HTTP " + httpURLConnection.getResponseCode()
                            + " " + httpURLConnection.getResponseMessage());

                }


                //Create directory
                apkStorage = new File(
                        Environment.getExternalStorageDirectory() + "/" + "GDI Files");


                //If File is not present create directory
                if (!apkStorage.exists()) {
                    apkStorage.mkdir();
                    Log.e(TAG, "Directory Created.");
                }

                outputFile = new File(apkStorage, System.currentTimeMillis()+".jpg");//Create Output file in Main File

                //Create New File if not present
                if (!outputFile.exists()) {
                    outputFile.createNewFile();
                    Log.e(TAG, "File Created");
                }


                FileOutputStream fos = new FileOutputStream(outputFile);//Get OutputStream for NewFile Location
                InputStream is = httpURLConnection.getInputStream();//Get InputStream for connection

                byte[] buffer = new byte[1024];//Set buffer type
                int len1 = 0;//init length
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);//Write new file
                }

                //Close all connection after doing task
                fos.close();
                is.close();

            } catch (Exception e) {

                //Read exception if something went wrong
                e.printStackTrace();
                outputFile = null;
                Log.e(TAG, "Download Error Exception " + e.getMessage());
            }
            return null;
        }
    }

    public interface ImageDownloadFinishedListner {
        void onImageDownloadFinished(String file);
    }
}
