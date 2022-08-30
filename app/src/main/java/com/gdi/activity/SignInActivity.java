package com.gdi.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.gdi.api.NetworkURL;
import com.gdi.hotel.mystery.audits.R;
import com.gdi.adapter.CustomAdapter;
import com.gdi.api.SendOTPRequest;
import com.gdi.api.SignInRequest;
import com.gdi.api.VolleyNetworkRequest;
import com.gdi.interfaces.INetworkEvent;
import com.gdi.model.signin.SignInRootObject;
import com.gdi.network.NetworkConstant;
import com.gdi.network.NetworkService;
import com.gdi.network.NetworkStatus;
import com.gdi.utils.ApiResponseKeys;
import com.gdi.utils.AppConstant;
import com.gdi.utils.AppLogger;
import com.gdi.utils.AppPrefs;
import com.gdi.utils.AppUtils;
import com.gdi.utils.LocaleHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignInActivity extends BaseActivity implements INetworkEvent {

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
    @BindView(R.id.rl_password_container)
    RelativeLayout mPasswordContainerRL;
    @BindView(R.id.rl_email_container)
    RelativeLayout mEmailContainerRL;

    Context context;
    private static final String TAG = SignInActivity.class.getSimpleName();
    private ImageView showPassword;
    private int passwordNotVisible=0;
    private Spinner mLocaleTypeSPN;
    private List<String> mLocaleList;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        context = this;
        ButterKnife.bind(SignInActivity.this);
        firebaseAuth=FirebaseAuth.getInstance();
        initView();

    }


    private void initView() {
        AppPrefs.setFilterBrand(context, 0);
        AppPrefs.setFilterCampaign(context, 0);
        AppPrefs.setFilterCity(context, 0);
        AppPrefs.setFilterCountry(context, 0);
        AppPrefs.setFilterLocation(context, 0);
        AppPrefs.setIaFilterAuditType(context, 0);
        AppPrefs.setIaFilterMonth(context, "");
        AppPrefs.setIaFilterBrand(context, 0);
        AppPrefs.setIaFilterAuditName(context, 0);
        AppPrefs.setFilterLocation(context, 0);



        username = findViewById(R.id.usernameEditText);
        password = findViewById(R.id.passwordEditText);
        signInButton = findViewById(R.id.signInButton);
        tourButton = findViewById(R.id.tour_button);
        forgetPassword = findViewById(R.id.forgetPasswordTextView);
        mPasswordContainerRL=findViewById(R.id.rl_password_container);
        mEmailContainerRL=findViewById(R.id.rl_email_container);

        mPasswordContainerRL.setVisibility(View.GONE);


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  firebaseLogin("test@testgdi.com","Test@123");
                AppUtils.hideKeyboard(context, v);
                if (signInButton.getText().toString().equalsIgnoreCase("Next"))
                {
                    checkUserFromServer();
                }
                else {
                    if (validateInputs()) {
                        showProgressDialog();
                       firebaseLogin(username.getText().toString().trim(),password.getText().toString().trim());
                     //   firebaseLogin("test@testgdi.com","Test@123");
                    }
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
//                startActivity(new Intent(context, AppTourPagerActivity.class));
 //               finish();
            }
        });

        showPassword = (ImageView) findViewById(R.id.show_pass);
        showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordNotVisible == 1) {
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordNotVisible = 0;
                } else {
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordNotVisible = 1;
                }


                password.setSelection(password.length());

            }
        });

        mLocaleTypeSPN=(Spinner)findViewById(R.id.spn_localetype);
        mLocaleList = Arrays.asList(getResources().getStringArray(R.array.arr_staytype));


        CustomAdapter spinnerCountShoesArrayAdapter = new CustomAdapter(SignInActivity.this, R.layout.spinner_row, R.id.cust_view, mLocaleList);
        mLocaleTypeSPN.setAdapter(spinnerCountShoesArrayAdapter);


        mLocaleTypeSPN.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String locale=  ""+mLocaleTypeSPN.getItemAtPosition(position);
                if(locale.equalsIgnoreCase("Spanish"))
                {
                    LocaleHelper.setLocale(SignInActivity.this, "es");
                    AppPrefs.setLocaleSelected(SignInActivity.this,"es");
                    finish();
                    startActivity(getIntent());
                }
                else  if(locale.equalsIgnoreCase("French"))
                {
                    AppPrefs.setLocaleSelected(SignInActivity.this,"fr");
                    LocaleHelper.setLocale(SignInActivity.this, "fr");
                    finish();
                    startActivity(getIntent());
                }
                else if(locale.equalsIgnoreCase("English"))
                {
                    AppPrefs.setLocaleSelected(SignInActivity.this,"en");
                    LocaleHelper.setLocale(SignInActivity.this, "en");
                    finish();
                    startActivity(getIntent());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }



    private void firebaseLogin(String email,String pass)
    {
        firebaseAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
                            hideProgressDialog();
                            Task<GetTokenResult> token=   firebaseUser.getIdToken(false);
                            String tokenAuth=  token.getResult().getToken();
                            AppPrefs.setAccessToken(getApplicationContext(),"Bearer "+tokenAuth);

                            Log.e("FIREBASE SUCCESS ",""+firebaseUser.getEmail());
                            Toast.makeText(SignInActivity.this, "Login Successfully.", Toast.LENGTH_SHORT).show();
                            getUserProfile();

                        } else {
                            hideProgressDialog();
                            // If sign in fails, display a message to the user.
                            Log.e("FIREBASE ERROR ",""+task.getException().getMessage());
                            Log.e("FIREBASE ERROR ",""+task.getException().getStackTrace());
                            Toast.makeText(SignInActivity.this, " "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    private void getUserProfile() {
        if (NetworkStatus.isNetworkConnected(this)) {
            NetworkService networkService = new NetworkService(NetworkURL.SIGNIN_PROFILE, NetworkConstant.METHOD_GET, this, this);
            networkService.call(new HashMap<String, String>());
        } else {
            AppUtils.toast(this, getString(R.string.internet_error));

        }
    }
    private void checkUserFromServer() {
        showProgressDialog();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onResponse(String response) {
                AppLogger.e(TAG, "Sign In Response: " + response);

                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject dataObj=object.optJSONObject("data");
                    String message = object.getString(ApiResponseKeys.RES_KEY_MESSAGE);
                    if (!object.getBoolean(ApiResponseKeys.RES_KEY_ERROR))
                    {
                        JSONObject providerObj=dataObj.optJSONObject("provider");
                        //.,  {"error":false,"data":{"fname":"Test","lname":"","role_id":300,"provider":{"provider_id":null,"identity_provider_name":"firebase","name":"password","display_name":"Google: Email & Passowrd"}},"message":"Data found"}
                        if(providerObj.optString("name").equalsIgnoreCase(AppConstant.PROVIDER_PASSWORD))
                        {
                           mPasswordContainerRL.setVisibility(View.VISIBLE);
                           mEmailContainerRL.setVisibility(View.GONE);
                           signInButton.setText(getString(R.string.signin));
                        }
                        else
                        {

                        }
                    }else if (object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        AppUtils.toast((BaseActivity) context, object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                    }
                } catch (JSONException e) {
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
            }
        };
        SignInRequest signInRequest = new SignInRequest( NetworkURL.SIGNIN_CHECKUSER,username.getText().toString(), password.getText().toString(), stringListener, errorListener);
        VolleyNetworkRequest.getInstance(SignInActivity.this).addToRequestQueue(signInRequest);
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
                        SignInRootObject signInRootObject = new GsonBuilder().create().fromJson(object.toString(), SignInRootObject.class);
                        if (signInRootObject.getData() != null ){
                            AppUtils.toast(SignInActivity.this, message);
                            AppPrefs.setLoggedIn(SignInActivity.this, true);
                            AppPrefs.setAccessToken(context, signInRootObject.getData().getAccess_token());
                            AppPrefs.setUserRole(context, signInRootObject.getData().getRole_id());
                            AppPrefs.setClientRoleId(context, signInRootObject.getData().getClient_role_id());
                            AppPrefs.setClientRoleName(context, signInRootObject.getData().getClient_role_name());
                            AppPrefs.setFaqTitle(context, signInRootObject.getData().getFaq_report_name());
                            finish();
                            startActivity(new Intent(SignInActivity.this, MainActivity.class));
                        }
                    }else if (object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        AppUtils.toast((BaseActivity) context, object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                    }
                } catch (JSONException e) {
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
                //AppUtils.toast((BaseActivity) context, "Server temporary unavailable, Please try again");
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONObject obj = new JSONObject(res);
                        String message = obj.getString("message");
                        Log.e("Error: ", "" + obj);
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        //AppUtils.toast((BaseActivity) context, message);
                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        if (context != null) {
                            Toast.makeText(context, "Invalid Responce", Toast.LENGTH_SHORT).show();
                        }
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        if (context != null) {
                            Toast.makeText(context, "Invalid Responce", Toast.LENGTH_SHORT).show();
                        }
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }
            }
        };
        SignInRequest signInRequest = new SignInRequest( NetworkURL.SIGNIN,username.getText().toString(),
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
                AppLogger.e(TAG, "ForgetPasswordError: " + error.getMessage());
                AppUtils.toast((BaseActivity) context, "Server temporary unavailable, Please try again");
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
            AppUtils.toast(SignInActivity.this, getString(R.string.enter_username));
            //username.setError(getString(R.string.enter_username));
        } else if (password.getText().toString().length() < 6) {
            validate = false;
            AppUtils.toast(SignInActivity.this, getString(R.string.enter_password));
            //password.setError("Enter password between 6 to 16 character");
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

        final EditText emailId = view.findViewById(R.id.send_email_edt_txt);

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

    @Override
    public void onNetworkCallInitiated(String service) {
      showProgressDialog();
    }

    @Override
    public void onNetworkCallCompleted(String type, String service, String response) {
        hideProgressDialog();
        if (service.equalsIgnoreCase(NetworkURL.SIGNIN_PROFILE))
        {
            AppLogger.e(" Profile RESPONSE===>  ",""+response);
            try {
                JSONObject object = new JSONObject(response);
                if (!object.getBoolean(AppConstant.RES_KEY_ERROR))
                {
                    String roleId=object.getJSONObject("data").optString("role_id");
                    if (TextUtils.isEmpty(roleId))
                    {
                        AppUtils.toast((BaseActivity) context, "You dont have any access previleges yet. Please contact "+object.getJSONObject("data").optString("created_by_email"));
                    }
                    else {
                        AppUtils.toast(SignInActivity.this, object.optString("message"));
                        AppPrefs.setLoggedIn(SignInActivity.this, true);
                      //  AppPrefs.setAccessToken(context, signInRootObject.getData().getAccess_token());
                        AppPrefs.setUserRole(context,object.getJSONObject("data").optInt("role_id") );
                      //  AppPrefs.setClientRoleId(context, object.getJSONObject("data").optString("custom_role_id"));
                        AppPrefs.setClientRoleName(context,object.getJSONObject("data").optString("role_name"));
                      //  AppPrefs.setFaqTitle(context, signInRootObject.getData().getFaq_report_name());
                        finish();
                        startActivity(new Intent(SignInActivity.this, MainActivity.class));
  /*
                        startActivity(new Intent(SignInActivity.this, MainActivity.class));
                        AppPreferences.INSTANCE.setUserId(object.getJSONObject("data").getInt("user_id"), this);
                        AppPreferences.INSTANCE.setUserFName(object.getJSONObject("data").optString("fname"));
                        AppPreferences.INSTANCE.setUserLName(object.getJSONObject("data").optString("lname"),this);
                        AppPreferences.INSTANCE.setUserEmail(object.getJSONObject("data").optString("email"));
                        AppPreferences.INSTANCE.setUserRole(Integer.parseInt(roleId), this);
                        AppPreferences.INSTANCE.setLogin(true, this);
                        startActivity(new Intent(this, MainActivity.class));
                        finish();*/

                       // {"error":false,"data":{"user_id":2118,"uid":"s1GelOyJRzU3WbDW0GLsUtKnDsA2","role_id":300,"client_id":null,"fname":"Test","lname":"","email":"test@testgdi.com","image":"https:\/\/api.gdiworldwide.com\/assets\/profile\/dummy.svg","phone":null,"dob":"2022-03-01","gender":1,"address":"Test","city_id":707,"state_id":10,"country_id":101,"zone_id":194,"zipcode":"qw2221","user_status":1,"created_on":"2022-03-01 12:35:03","language_id":1,"language_name":"English","language_code":"en","gender_text":"male","custom_role_id":null,"custom_role_name":null,"country_name":"India","zone_name":"Asia\/Kolkata","state_name":"Delhi","city_name":"New Delhi","role_name":"Auditor","role_resource":"auditor","client_status":null,"industry_id":null,"login_provider_id":1,"created_by_id":2,"created_by_name":"Andreas Muller","created_by_email":"gauravpach@gdiworldwide.com","user_provisioning":0,"auditor_file_cnt":0,"company_contact_email":"info@gdiworldwide.com","company_support_email":"info@gdiworldwide.com"},"message":"Data found"}

                    }
                }
            }
            catch (Exception e){e.printStackTrace();}
        }
    }

    @Override
    public void onNetworkCallError(String service, String errorMessage) {
        hideProgressDialog();

    }
}
