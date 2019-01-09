package com.gdi.api;

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.gdi.utils.AppLogger;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nitish on 4/1/2018.
 */

public abstract class BaseStringRequest extends StringRequest {

    private static final int TIMEOUT_MS = 20000;
    private static final int MAX_RETRY = 0;

    public BaseStringRequest(int method,
                             String url,
                             Response.Listener<String> listener,
                             Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> params = new HashMap<>();
        return params;
    }

    @Override
    public Request<?> setRetryPolicy(RetryPolicy retryPolicy) {
        RetryPolicy myRetryPolycy = new DefaultRetryPolicy(TIMEOUT_MS, MAX_RETRY,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        return super.setRetryPolicy(myRetryPolycy);
    }



    protected String toParam(String val) {
        return TextUtils.isEmpty(val) ? "" : val;
    }

    protected String toParam(int val) {
        return "" + val;
    }

    protected void printParams(Map<String, String> params){
        try {
            String pStr = new JSONObject(params).toString();
            AppLogger.e("PARAMS", pStr);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
