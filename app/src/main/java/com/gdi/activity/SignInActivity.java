package com.gdi.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gdi.R;
import com.gdi.api.SendOTPRequest;
import com.gdi.api.SignInRequest;
import com.gdi.api.VolleyNetworkRequest;
import com.gdi.model.signin.SignInRootObject;
import com.gdi.utils.ApiResponseKeys;
import com.gdi.utils.AppLogger;
import com.gdi.utils.AppPrefs;
import com.gdi.utils.AppUtils;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignInActivity extends BaseActivity {

    @BindView(R.id.usernameEditText)
    EditText username;
    @BindView(R.id.passwordEditText)
    EditText password;
    @BindView(R.id.signInButton)
    Button signInButton;
    @BindView(R.id.forgetPasswordTextView)
    TextView forgetPassword;
    @BindView(R.id.tour_button)
    Button tourButton;
    Context context;
    private static final String TAG = SignInActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        context = this;
        ButterKnife.bind(SignInActivity.this);
        initView();
    }

    private void initView() {
        AppPrefs.setFilterBrand(context, 0);
        AppPrefs.setFilterCampaign(context, 0);
        AppPrefs.setFilterCity(context, 0);
        AppPrefs.setFilterCountry(context, 0);
        AppPrefs.setFilterLocation(context, 0);
        username = (EditText)findViewById(R.id.usernameEditText);
        password = (EditText)findViewById(R.id.passwordEditText);
        signInButton = (Button)findViewById(R.id.signInButton);
        tourButton = (Button)findViewById(R.id.tour_button);
        forgetPassword = (TextView) findViewById(R.id.forgetPasswordTextView);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    AppUtils.hideKeyboard(context,v);
                    SignIn();
                }
            }
        });
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.hideKeyboard(context,v);
                forgetPasswordDialog();
            }
        });
        tourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.hideKeyboard(context,v);
                startActivity(new Intent(context, AppTourPagerActivity.class));
                finish();
            }
        });
    }

    private void SignIn() {
        showProgressDialog();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onResponse(String response) {
                AppLogger.e(TAG, "Sign In Response: " + response);

                try {
                    JSONObject object = new JSONObject(response);
                    String message = object.getString(ApiResponseKeys.RES_KEY_MESSAGE);
                    if (!object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        SignInRootObject signInRootObject = new GsonBuilder().create()
                                .fromJson(object.toString(), SignInRootObject.class);
                        if (signInRootObject.getData() != null &&
                                signInRootObject.getData().toString().length() > 0){
                            AppUtils.toast(SignInActivity.this, message);
                            AppPrefs.setLoggedIn(SignInActivity.this, true);
                            AppPrefs.setAccessToken(context, signInRootObject.getData()
                                    .getAccess_token());
                            finish();
                            startActivity(new Intent(SignInActivity.this,
                                    MainActivity.class));
                        }
                    }else if (object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        AppUtils.toast((BaseActivity) context,
                                object.getString(ApiResponseKeys.RES_KEY_MESSAGE));

                    }
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
                error.printStackTrace();
                AppUtils.toast(SignInActivity.this, "Server Error, Please try again");
                //serverNotRespondingAlert();
            }
        };
        SignInRequest signInRequest = new SignInRequest(username.getText().toString(),
                password.getText().toString(), stringListener, errorListener);
        VolleyNetworkRequest.getInstance(SignInActivity.this).addToRequestQueue(signInRequest);
    }

    private void sendOtp(final EditText emailId) {
        showProgressDialog();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLogger.e(TAG, "Forget Password Response: " + response);

                try {
                    JSONObject object = new JSONObject(response);
                    String message = object.getString(ApiResponseKeys.RES_KEY_MESSAGE);
                    if (!object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        AppLogger.e(TAG, "Forget Password Response: " + message);
                        AppUtils.toast(SignInActivity.this, message);
                        Intent intent = new Intent(context, ResetPasswordScreen.class);
                        intent.putExtra("username", emailId.getText().toString());
                        startActivity(intent);
                    }else
                        AppUtils.toast(SignInActivity.this, message);
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
                AppLogger.e(TAG, "Forget Password Error: " + error.getMessage());
            }
        };
        SendOTPRequest sendOTPRequest = new SendOTPRequest(
                emailId.getText().toString(), stringListener, errorListener);
        VolleyNetworkRequest.getInstance(SignInActivity.this).addToRequestQueue(sendOTPRequest);
    }

    boolean validate = true;

    private boolean validateInputs() {
        boolean validate = true;

        if (username.getText().toString().length() <= 0) {
            validate = false;
            username.setError(getString(R.string.enter_username));
        } else if (password.getText().toString().length() <= 0) {
            validate = false;
            password.setError(getString(R.string.enter_password));
        }
        return validate;
    }

    private boolean validateForgetPassword(EditText edit_email) {
        if (edit_email.getText().toString().length() <= 0) {
            validate = false;
            edit_email.setError(getString(R.string.enter_email));
            return false;
        }
        return true;
    }

    private void forgetPasswordDialog() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.send_email_layout, null);
        dialog.setView(view);

        final EditText emailId = (EditText) view.findViewById(R.id.send_email_edt_txt);

        dialog.setTitle("Enter Your Username");

        dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (validateForgetPassword(emailId)) {
                    AppUtils.hideKeyboard(context, view);
                    sendOtp(emailId);
                }
            }
        });
        dialog.create().show();
    }
}
