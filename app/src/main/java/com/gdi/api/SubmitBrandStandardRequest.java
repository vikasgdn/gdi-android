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

public class SubmitBrandStandardRequest extends BaseStringRequest {

    //request params
    public static final String REQ_PARAM_AUDIT_ID = "audit_id";
    public static final String REQ_PARAM_AUDIT_DATE = "audit_date";
    public static final String REQ_PARAM_SAVE = "save";
    public static final String REQ_PARAM_ANSWER = "answers";

    public static final String REQ_PARAM_ACCESS_TOKEN = "access-token";
    public static final String REQ_PARAM_DEVICE_ID = "device-id";
    public static final String REQ_PARAM_DEVICE_TYPE = "device-type";
    public static final String REQ_PARAM_DEVICE_VERSION = "device-version";


    private Map<String, String> params = new HashMap<>();
    private Map<String, String> headerParams = new HashMap<>();

    public SubmitBrandStandardRequest(String auditId,
                                      String auditDate,
                                      String save,
                                      String answer,
                                      String accessToken,String firebaseToken,
                                      Response.Listener<String> listener,
                                      Response.ErrorListener errorListener) {
        super(Method.POST, NetworkURL.BRANDSTANDARD, listener, errorListener);
        params.put(REQ_PARAM_AUDIT_ID, auditId);
        params.put(REQ_PARAM_AUDIT_DATE, auditDate);
        params.put(REQ_PARAM_SAVE, save);
        params.put(REQ_PARAM_ANSWER, answer);

        headerParams.put(REQ_PARAM_ACCESS_TOKEN, accessToken);
        headerParams.put(AppConstant.AUTHORIZATION, "Bearer "+firebaseToken);
        headerParams.put(REQ_PARAM_DEVICE_ID, AppConstant.DEVICE_ID);
        headerParams.put(REQ_PARAM_DEVICE_TYPE, "android");
        headerParams.put(REQ_PARAM_DEVICE_VERSION, "2");

        AppLogger.e("UpdateProfileParam", params.toString());
        AppLogger.e("UpdateProfileHeaderParam", headerParams.toString());
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
