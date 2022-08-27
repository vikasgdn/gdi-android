package com.gdi.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadExcelTask {

    private String downloadUrl = "";
    private String downloadFileName = "";
    private ProgressDialog progressDialog;
    private Context context;
    private static final String TAG = DownloadExcelTask.class.getSimpleName();
    private DownloadExcelFinishedListner downloadExcelFinishedListner;
    private String firebaseToken="";


    public DownloadExcelTask(Context context, String downloadUrl, DownloadExcelFinishedListner downloadExcelFinishedListner) {
        this.context = context;
        this.downloadUrl = downloadUrl;
        this.downloadExcelFinishedListner = downloadExcelFinishedListner;

        downloadFileName = downloadUrl.substring(downloadUrl.lastIndexOf( '/' ),downloadUrl.length());//Create file name by picking download file name from URL
        Log.e(TAG, downloadFileName);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().getCurrentUser().getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                firebaseToken=task.getResult().getToken();
                                new DownloadingTask().execute();                            }
                        }
                    });
        }

        //Start Downloading Task

    }

    private class DownloadingTask extends AsyncTask<Void, Void, Void> {

        File excelStorage = null;
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
                    downloadExcelFinishedListner.onExcelDownloadFinished(outputFile.getAbsolutePath());
                } else
                    downloadExcelFinishedListner.onExcelDownloadFinished("");
            } catch (Exception e) {
                e.printStackTrace();
                downloadExcelFinishedListner.onExcelDownloadFinished("");
            }
            progressDialog.dismiss();
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
                httpURLConnection.setRequestProperty(AppConstant.AUTHORIZATION, "Bearer "+firebaseToken);
                httpURLConnection.setDoOutput(false);//connect the URL Connection

                //If Connection response is not OK then show Logs
                if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.e(TAG, "Server returned HTTP " + httpURLConnection.getResponseCode()
                            + " " + httpURLConnection.getResponseMessage());

                }


                //Create directory
                if(Build.VERSION.SDK_INT >= 29) {
                    String downloadDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
                    outputFile = new File(downloadDir, System.currentTimeMillis() + ".xlsx");
                }
                else
                {
                    excelStorage = new File(Environment.getExternalStorageDirectory() +File.separator+ "GDI Files");
                    if (!excelStorage.exists()) {
                        boolean diectoryCreate = excelStorage.mkdirs();
                        Log.e(TAG, "Directory Created."+diectoryCreate);
                    }
                    outputFile = new File(excelStorage, System.currentTimeMillis()+".xlsx");//Create Output file in Main File
                    //Create New File if not presentCrea
                    if (!outputFile.exists()) {
                        outputFile.createNewFile();
                        Log.e(TAG, "File Created");
                    }
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

    public interface DownloadExcelFinishedListner {
        void onExcelDownloadFinished(String file);
    }
}
