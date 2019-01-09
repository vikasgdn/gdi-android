package com.gdi.api;

import com.android.volley.Response;
import com.gdi.utils.AppLogger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 3/5/2018.
 */

public class SendOTPRequest extends BaseStringRequest {

    //request params
    public static final String REQ_PARAM_USER = "user";

    private Map<String, String> params = new HashMap<>();

    public SendOTPRequest(String username, Response.Listener<String> listener,
                          Response.ErrorListener errorListener) {
        super(Method.POST, ApiEndPoints.SENDOTP, listener, errorListener);
        params.put(REQ_PARAM_USER, username);

        AppLogger.e("Send OTP Params", params.toString());
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
