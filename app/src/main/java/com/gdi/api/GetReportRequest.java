package com.gdi.api;

import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.gdi.utils.AppConstant;
import com.gdi.utils.AppLogger;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 3/5/2018.
 */

public class GetReportRequest extends BaseStringRequest {

    //request params
    public static final String REQ_PARAM_ACCESS_TOKEN = "access-token";
    public static final String REQ_PARAM_DEVICE_ID = "device-id";
    public static final String REQ_PARAM_DEVICE_TYPE = "device-type";
    public static final String REQ_PARAM_DEVICE_VERSION = "device-version";

    private Map<String, String> params = new HashMap<>();
    private Map<String, String> headerParams = new HashMap<>();

    public GetReportRequest(String accessToken,String fireBaseToken,String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {

        super(Method.GET, url, listener, errorListener);



        headerParams.put(REQ_PARAM_ACCESS_TOKEN, accessToken);
        headerParams.put(AppConstant.AUTHORIZATION, "Bearer "+fireBaseToken);
        headerParams.put(REQ_PARAM_DEVICE_ID, AppConstant.DEVICE_ID);
        headerParams.put(REQ_PARAM_DEVICE_TYPE, "android");
        headerParams.put(REQ_PARAM_DEVICE_VERSION, "2");


        AppLogger.e("AuditActionParams", headerParams.toString());
        AppLogger.e("AuditActionUrl", url);
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
