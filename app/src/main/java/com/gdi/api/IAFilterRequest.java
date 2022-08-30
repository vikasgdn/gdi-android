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

public class IAFilterRequest extends BaseStringRequest {

    //request params
    public static final String REQ_PARAM_ACCESS_TOKEN = "access-token";


    private Map<String, String> params = new HashMap<>();
    private Map<String, String> headerParams = new HashMap<>();

    public IAFilterRequest(String accessToken,String firebaseToken,
                           Response.Listener<String> listener,
                           Response.ErrorListener errorListener) {
        super(Method.GET, NetworkURL.IAFILTER, listener, errorListener);

        headerParams.put(REQ_PARAM_ACCESS_TOKEN, accessToken);
        headerParams.put(AppConstant.AUTHORIZATION, "Bearer "+firebaseToken);
        AppLogger.e("IAFilterParams", headerParams.toString());
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
