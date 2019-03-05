package com.gdi.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gdi.R;
import com.gdi.api.ChangePasswordRequest;
import com.gdi.api.ResetPasswordRequest;
import com.gdi.api.VolleyNetworkRequest;
import com.gdi.utils.ApiResponseKeys;
import com.gdi.utils.AppLogger;
import com.gdi.utils.AppPrefs;
import com.gdi.utils.AppUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChangePasswordScreen extends BaseActivity {

    @BindView(R.id.tv_old_password)
    EditText oldPassword;
    @BindView(R.id.tv_new_password)
    EditText newPassword;
    @BindView(R.id.tv_confirm_password)
    EditText confirmPassword;
    @BindView(R.id.btn_reset)
    Button resetButton;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    String username = "";
    Context context;
    private static final String TAG = ChangePasswordScreen.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_screen);
        context = this;
        ButterKnife.bind(ChangePasswordScreen.this);
        initView();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar();
        oldPassword = (EditText)findViewById(R.id.tv_old_password);
        newPassword = (EditText)findViewById(R.id.tv_new_password);
        confirmPassword = (EditText)findViewById(R.id.tv_confirm_password);
        resetButton = (Button)findViewById(R.id.btn_reset);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateInputs()){
                    AppUtils.hideKeyboard(context,v);
                    changePassword();
                }
            }
        });
    }

    private void changePassword() {
        showProgressDialog();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLogger.e(TAG, "Change Password Response: " + response);

                try {
                    JSONObject object = new JSONObject(response);
                    String message = object.getString(ApiResponseKeys.RES_KEY_MESSAGE);
                    if (!object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        AppUtils.toast(ChangePasswordScreen.this, message);
                        finish();
                    }else
                        AppUtils.toast(ChangePasswordScreen.this, message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }catch (Exception e){
                    e.printStackTrace();
                }
                hideProgressDialog();
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                AppLogger.e(TAG, "ChangePasswordError: " + error.getMessage());
            }
        };
        ChangePasswordRequest forgetPasswordRequest = new ChangePasswordRequest(
                oldPassword.getText().toString(), newPassword.getText().toString(),
                AppPrefs.getAccessToken(context), stringListener, errorListener);
        VolleyNetworkRequest.getInstance(ChangePasswordScreen.this).addToRequestQueue(forgetPasswordRequest);
    }


    private boolean validateInputs() {
        boolean validate = true;

        if (oldPassword.getText().toString().length() <= 0) {
            validate = false;
            oldPassword.setError("Enter old password");
        } else if (newPassword.getText().toString().length() < 6) {
            validate = false;
            newPassword.setError(getString(R.string.enter_password));
        }
        else if (confirmPassword.getText().toString().length() <= 0) {
            validate = false;
            confirmPassword.setError(getString(R.string.re_enter_password));
        } else {
            if (newPassword.getText().toString().equalsIgnoreCase(confirmPassword.getText().toString())) {
                confirmPassword.setError(null);
            } else {
                validate = false;
                confirmPassword.setError(getString(R.string.password_not_same));
            }
        }
        return validate;
    }



    private void setActionBar() {
        initToolbar(toolbar);
        setTitle("Change Password");
        enableBack(true);
        enableBackPressed();
    }
}
