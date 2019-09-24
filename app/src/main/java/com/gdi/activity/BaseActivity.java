package com.gdi.activity;

import android.app.ProgressDialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.gdi.R;
import com.gdi.utils.AppUtils;

public abstract class BaseActivity extends AppCompatActivity {

    protected Toolbar mToolbar;
    protected ProgressDialog mProgressDialog;

    public void initToolbar(Toolbar toolbar) {
        mToolbar = toolbar;
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }

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

    public void showProgressDialog() {
        showProgressDialog(null, getString(R.string.default_progress_dialog_message), false);
    }

    public void showAppProgressDialog(String title, String message, boolean cancelable) {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        if (!isFinishing()) {

            mProgressDialog = ProgressDialog.show(getApplicationContext(), title, message, false, cancelable);
        }
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
}
