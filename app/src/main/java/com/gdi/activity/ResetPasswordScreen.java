package com.gdi.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gdi.R;
import com.gdi.api.ResetPasswordRequest;
import com.gdi.api.VolleyNetworkRequest;
import com.gdi.utils.ApiResponseKeys;
import com.gdi.utils.AppLogger;
import com.gdi.utils.AppUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResetPasswordScreen extends BaseActivity {

    @BindView(R.id.otpLayout)
    EditText otp;
    @BindView(R.id.password_text)
    EditText newPassword;
    @BindView(R.id.confirm_password_text)
    EditText confirmPassword;
    @BindView(R.id.resetButton)
    Button reset;
    @BindView(R.id.username_text)
    TextView usernametxt;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    String username = "";
    Context context;
    private static final String TAG = ResetPasswordScreen.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_screen);
        context = this;
        ButterKnife.bind(ResetPasswordScreen.this);
        initView();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar();
        otp = (EditText)findViewById(R.id.otpLayout);
        newPassword = (EditText)findViewById(R.id.password_text);
        confirmPassword = (EditText)findViewById(R.id.confirm_password_text);
        reset = (Button)findViewById(R.id.resetButton);
        usernametxt = (TextView) findViewById(R.id.username_text);
        username = getIntent().getStringExtra("username");
        usernametxt.setText("Username : " + username);
        reset.setOnClickListener(new View.OnClickListener() {
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
                        AppUtils.toast(ResetPasswordScreen.this, message);
                        finish();
                    }else
                        AppUtils.toast(ResetPasswordScreen.this, message);
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
                AppUtils.toast((BaseActivity) context, "Server temporary unavailable, Please try again");
            }
        };
        ResetPasswordRequest forgetPasswordRequest = new ResetPasswordRequest(username, otp.getText().toString(), newPassword.getText().toString(), stringListener, errorListener);
        VolleyNetworkRequest.getInstance(ResetPasswordScreen.this).addToRequestQueue(forgetPasswordRequest);
    }


    private boolean validateInputs() {
        boolean validate = true;

        if (otp.getText().toString().length() <= 0) {
            validate = false;
            otp.setError("Enter your otp");
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
        setTitle("Set New Password");
        enableBack(true);
        enableBackPressed();
    }
}
