package com.gdi.api;

import com.android.volley.Request;
import com.android.volley.Response;
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


    private Map<String, String> params = new HashMap<>();

    public SignInRequest(String username,
                         String password,
                         Response.Listener<String> listener,
                         Response.ErrorListener errorListener) {
        super(Request.Method.POST, ApiEndPoints.SIGNIN, listener, errorListener);
        params.put(REQ_PARAM_USER, username);
        params.put(REQ_PARAM_PASSWORD, password);
        params.put(REQ_PARAM_MOBILE, "1");

        AppLogger.e("SignInParams", params.toString());
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
