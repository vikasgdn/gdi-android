package com.gdi.network;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.gdi.utils.AppConstant;
import com.gdi.utils.AppLogger;
import com.gdi.utils.AppPrefs;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 3/5/2018.
 */

public class ApiRequestJSON extends BaseJsonObjectRequest {


    public static final String REQ_PARAM_DEVICE_ID = "device-id";
    public static final String REQ_PARAM_DEVICE_TYPE = "device-type";
    public static final String REQ_PARAM_DEVICE_VERSION = "device-version";
    public static final String REQ_PARAM_ACCESS_TOKEN = "access-token";


   // private Map<String, String> params = new HashMap<>();
    private Map<String, String> headerParams = new HashMap<>();

    public ApiRequestJSON(JSONObject param, int methode, String url, Context context, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(Request.Method.POST, url, param, listener, errorListener);

        headerParams.put(REQ_PARAM_ACCESS_TOKEN, AppPrefs.getAccessToken(context));
        headerParams.put(REQ_PARAM_DEVICE_ID, AppConstant.DEVICE_ID);
        headerParams.put(REQ_PARAM_DEVICE_TYPE, "android");
        headerParams.put(REQ_PARAM_DEVICE_VERSION, "2");

        AppLogger.e("API PARAMS URL ",url+" || "+param);
        AppLogger.e("API HEADERS", headerParams.toString());
    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headerParams;
    }

}
