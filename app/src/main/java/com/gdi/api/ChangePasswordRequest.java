package com.gdi.api;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
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

    private Map<String, String> params = new HashMap<>();
    private Map<String, String> headerParams = new HashMap<>();

    public ChangePasswordRequest(String oldPassword,
                                 String newPassword,
                                 String accessToken,
                                 Response.Listener<String> listener,
                                 Response.ErrorListener errorListener) {
        super(Method.POST, ApiEndPoints.CHANGEPASSWORD, listener, errorListener);
        params.put(REQ_PARAM_PASSWORD, oldPassword);
        params.put(REQ_PARAM_NEW_PASSWORD, newPassword);
        headerParams.put(REQ_PARAM_ACCESS_TOKEN, accessToken);

        AppLogger.e("ChangePasswordParams", params.toString());
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
