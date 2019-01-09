package com.gdi.api;

import com.android.volley.Response;
import com.gdi.utils.AppLogger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 3/5/2018.
 */

public class ChangePasswordRequest extends BaseStringRequest {

    //request params
    private static final String REQ_PARAM_USER = "user";
    private static final String REQ_PARAM_DEVICE_TOKEN = "otp";
    private static final String REQ_PARAM_PASSWORD = "password";

    private Map<String, String> params = new HashMap<>();

    public ChangePasswordRequest(String username,
                                 String otp,
                                 String newPassword,
                                 Response.Listener<String> listener,
                                 Response.ErrorListener errorListener) {
        super(Method.POST, ApiEndPoints.CHANGEPASSWORD, listener, errorListener);
        params.put(REQ_PARAM_USER, username);
        params.put(REQ_PARAM_DEVICE_TOKEN, otp);
        params.put(REQ_PARAM_PASSWORD, newPassword);

        AppLogger.e("Change Password Params", params.toString());
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
