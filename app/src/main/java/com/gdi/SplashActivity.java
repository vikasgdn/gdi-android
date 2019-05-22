package com.gdi;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.crashlytics.android.Crashlytics;
import com.gdi.activity.AppTourPagerActivity;
import com.gdi.activity.BaseActivity;
import com.gdi.activity.MainActivity;
import com.gdi.activity.SignInActivity;
import com.gdi.api.ApiEndPoints;
import com.gdi.api.FilterRequest;
import com.gdi.api.ForceUpdateRequest;
import com.gdi.api.GetReportRequest;
import com.gdi.api.VolleyNetworkRequest;
import com.gdi.model.audit.DetailedSummary.DetailedSummaryRootObject;
import com.gdi.utils.ApiResponseKeys;
import com.gdi.utils.AppConstant;
import com.gdi.utils.AppLogger;
import com.gdi.utils.AppPrefs;
import com.gdi.utils.AppUtils;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import io.fabric.sdk.android.Fabric;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLACE_TIMEOUT = 1000;
    private static String TAG = SplashActivity.class.getSimpleName();
    boolean status = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        String deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        AppLogger.e("Android Id: ", deviceId);

        AppConstant.DEVICE_ID = deviceId;


        if(AppUtils.isNetworkConnected(SplashActivity.this)){
            forceUpdate();
        }else AppUtils.showDoNothingDialog(SplashActivity.this,"Ok",
                getResources().getString(R.string.alert_msg_no_internet));
    }

    private void initApp() {
        /*if (AppPrefs.isInstalled(this)){
            gotoPagerPage();
        }else {
            if (AppPrefs.isLoggedIn(this)) {
                gotoMainPage();
            } else {
                gotoSignInPage();
            }
        }*/
        if (AppPrefs.isLoggedIn(this)) {
            gotoMainPage();
        } else {
            gotoSignInPage();
        }
    }

    private void gotoSignInPage() {
        startActivity(new Intent(this, SignInActivity.class));
        finish();

    }

    private void gotoMainPage() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void gotoPagerPage() {
        startActivity(new Intent(this, AppTourPagerActivity.class));
        finish();
    }

    private void forceUpdate(){
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLogger.e("", "forceUpdateResponse: " + response);
                try {
                    JSONObject object = new JSONObject(response);

                    if (!object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        int version = object.getJSONObject("data").getInt("version");
                        boolean status = object.getJSONObject("data").getBoolean("force_update");
                        if (status){
                            notificationDialog();
                        }else {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    initApp();
                                }
                            }, SPLACE_TIMEOUT);

                        }

                    } else if (object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppLogger.e("", "forceUpdateError: " + error.getMessage());
                AppUtils.toast(SplashActivity.this, "Server temporary unavailable, Please try again");

            }
        };

        String Url = ApiEndPoints.FORCEUPDATE;
        ForceUpdateRequest getReportRequest = new ForceUpdateRequest(Url,
                stringListener, errorListener);
        VolleyNetworkRequest.getInstance(SplashActivity.this).addToRequestQueue(getReportRequest);
    }

    private void notificationDialog() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setCancelable(false);

        dialog.setTitle("GDI");
        dialog.setMessage("You are missing out some important features of GDI,so please update your application.");

        dialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                dialog.dismiss();
                finish();
            }
        });

        dialog.create().show();
    }
}
