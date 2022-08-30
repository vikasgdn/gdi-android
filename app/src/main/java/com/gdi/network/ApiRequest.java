package com.gdi.network;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.gdi.api.BaseStringRequest;
import com.gdi.utils.AppConstant;
import com.gdi.utils.AppLogger;
import com.gdi.utils.AppPrefs;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 3/5/2018.
 */

public class ApiRequest extends BaseStringRequest {


    public static final String REQ_PARAM_DEVICE_ID = "device-id";
    public static final String REQ_PARAM_DEVICE_TYPE = "device-type";
    public static final String REQ_PARAM_DEVICE_VERSION = "device-version";
    public static final String REQ_PARAM_ACCESS_TOKEN = "access-token";


    private Map<String, String> params = new HashMap<>();
    private Map<String, String> headerParams = new HashMap<>();

    public ApiRequest(Map<String,String> param, int methode,String firebaseToken, String url, Context context, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(methode, url, listener, errorListener);
        this.params=param;
        headerParams.put(REQ_PARAM_ACCESS_TOKEN, AppPrefs.getAccessToken(context));
        headerParams.put(AppConstant.AUTHORIZATION,"Bearer "+firebaseToken);
        headerParams.put(REQ_PARAM_DEVICE_ID, AppConstant.DEVICE_ID);
        headerParams.put(REQ_PARAM_DEVICE_TYPE, "android");
        headerParams.put(REQ_PARAM_DEVICE_VERSION, "2");

        AppLogger.e("API PARAMS URL ",url+" || "+ params.toString());
        AppLogger.e("API HEADERS", headerParams.toString());
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
