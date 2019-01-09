package com.gdi.api;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.gdi.utils.AppLogger;

import java.util.HashMap;
import java.util.Map;

public class SendToEmailRequest extends BaseStringRequest {

    //request params
    public static final String REQ_PARAM_ACCESS_TOKEN = "access-token";


    private Map<String, String> params = new HashMap<>();
    private Map<String, String> headerParams = new HashMap<>();

    public SendToEmailRequest(String url,
                              String accessToken,
                              Response.Listener<String> listener,
                              Response.ErrorListener errorListener) {
        super(Method.GET, url, listener, errorListener);

        headerParams.put(REQ_PARAM_ACCESS_TOKEN, accessToken);

        AppLogger.e("SendToEmailParams", headerParams.toString());
        AppLogger.e("SendToEmailParamsUrl", url);
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
