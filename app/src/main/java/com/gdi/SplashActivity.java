package com.gdi;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.crashlytics.android.Crashlytics;
import com.gdi.activity.MainActivity;
import com.gdi.activity.SignInActivity;

import io.fabric.sdk.android.Fabric;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLACE_TIMEOUT = 2000;
    private static String TAG = SplashActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initApp();
            }
        }, SPLACE_TIMEOUT);
    }

    private void initApp() {
        gotoMainPage();
        /*if (AppPrefs.isLoggedIn(this)) {
            gotoMainPage();
        } else {
            gotoSignInPage();
        }*/
    }

    private void gotoSignInPage() {
        startActivity(new Intent(this, SignInActivity.class));
        finish();

    }

    private void gotoMainPage() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
