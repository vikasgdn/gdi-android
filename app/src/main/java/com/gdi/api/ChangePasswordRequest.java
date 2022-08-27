package com.gdi.api;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.gdi.utils.AppConstant;
import com.gdi.utils.AppLogger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 3/5/2018.
 */

public class ChangePasswordRequest extends BaseStringRequest {

    //request params
    private static final String REQ_PARAM_NEW_PASSWORD = "new_password";
    private static final String REQ_PARAM_PASSWORD = "password";
    public static final String REQ_PARAM_ACCESS_TOKEN = "access-token";
    public static final String REQ_PARAM_DEVICE_ID = "device-id";
    public static final String REQ_PARAM_DEVICE_TYPE = "device-type";
    public static final String REQ_PARAM_DEVICE_VERSION = "device-version";

    private Map<String, String> params = new HashMap<>();
    private Map<String, String> headerParams = new HashMap<>();

    public ChangePasswordRequest(String oldPassword,
                                 String newPassword,String firebaseToken,
                                 Response.Listener<String> listener,
                                 Response.ErrorListener errorListener) {
        super(Method.POST, ApiEndPoints.CHANGEPASSWORD, listener, errorListener);
        params.put(REQ_PARAM_PASSWORD, oldPassword);
        params.put(REQ_PARAM_NEW_PASSWORD, newPassword);
       // headerParams.put(REQ_PARAM_ACCESS_TOKEN, accessToken);
        headerParams.put(AppConstant.AUTHORIZATION, "Bearer "+firebaseToken);
        headerParams.put(REQ_PARAM_DEVICE_ID, AppConstant.DEVICE_ID);
        headerParams.put(REQ_PARAM_DEVICE_TYPE, "android");
        headerParams.put(REQ_PARAM_DEVICE_VERSION, "2");

        AppLogger.e("ChangePasswordParams", params.toString());
        AppLogger.e("ChangePasswordHeader", headerParams.toString());
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
