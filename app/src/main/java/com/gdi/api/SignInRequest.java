package com.gdi.api;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.gdi.utils.AppConstant;
import com.gdi.utils.AppLogger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 3/5/2018.
 */

public class SignInRequest extends BaseStringRequest {

    //request params
    public static final String REQ_PARAM_USER = "user";
    public static final String REQ_PARAM_PASSWORD = "password";
    public static final String REQ_PARAM_MOBILE = "mobile";
    public static final String REQ_PARAM_DEVICE_ID = "device-id";
    public static final String REQ_PARAM_DEVICE_TYPE = "device-type";
    public static final String REQ_PARAM_DEVICE_VERSION = "device-version";


    private Map<String, String> params = new HashMap<>();
    private Map<String, String> headerParams = new HashMap<>();

    public SignInRequest(String username,
                         String password,
                         Response.Listener<String> listener,
                         Response.ErrorListener errorListener) {
        super(Request.Method.POST, ApiEndPoints.SIGNIN, listener, errorListener);
        params.put(REQ_PARAM_USER, username);
        params.put(REQ_PARAM_PASSWORD, password);
        params.put(REQ_PARAM_MOBILE, "1");

        headerParams.put(REQ_PARAM_DEVICE_ID, AppConstant.DEVICE_ID);
        headerParams.put(REQ_PARAM_DEVICE_TYPE, "android");
        headerParams.put(REQ_PARAM_DEVICE_VERSION, "2");

        AppLogger.e("SignInParams", params.toString());
        AppLogger.e("SignInHeaderParams", headerParams.toString());
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headerParams;
    }
}
