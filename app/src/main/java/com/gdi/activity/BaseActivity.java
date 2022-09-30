package com.gdi.activity;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.gdi.hotel.mystery.audits.R;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    protected Toolbar mToolbar;
    protected ProgressDialog mProgressDialog;
    public static final int READ_WRITE_STORAGE = 52;

    public void initToolbar(Toolbar toolbar) {
        mToolbar = toolbar;
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }

    }

    @Override
    public void onClick(View v) {

    }

    public void enableBackPressed() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                //startActivity(new Intent(BaseActivity.this, MainActivity.class));
            }
        });
    }

    public void enableBack(boolean goBack) {
        if (mToolbar == null) {
            return;
        }
        if (goBack) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
            }
        }
    }

    protected void setTitle(String title) {
        if (mToolbar != null && title != null) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(title);
            }
        }
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    public void showProgressDialog(String title, String message, boolean cancelable) {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        if (!isFinishing()) {

            mProgressDialog = ProgressDialog.show(this, title, message, false, cancelable);
        }
    }


    public void showAppProgressDialog(String title, String message, boolean cancelable) {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        if (!isFinishing()) {

            mProgressDialog = ProgressDialog.show(getApplicationContext(), title, message, false, cancelable);
        }
    }
    public void showProgressDialog() {
        showProgressDialog(null, getString(R.string.default_progress_dialog_message), true);
    }

    public void showAppProgressDialog() {
        showProgressDialog(null, getString(R.string.default_progress_dialog_message), false);
    }

    public AlertDialog.Builder buildAlertDialog(String title, String msg, boolean cancelable) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        if (!TextUtils.isEmpty(title)) {
            dialog.setTitle(title);
        }
        if (!TextUtils.isEmpty(msg)) {

            dialog.setMessage(msg);
        }
        dialog.setCancelable(cancelable);
        return dialog;
    }

    public AlertDialog.Builder buildAlertDialog(String title, String msg) {
        return buildAlertDialog(title, msg, false);
    }

    public boolean requestPermission(String permission) {
        boolean isGranted = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
        if (!isGranted) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{permission},
                    READ_WRITE_STORAGE);
        }
        return isGranted;
    }


    public void isPermissionGranted(boolean isGranted, String permission) {

    }
}
