package com.gdi.api;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.gdi.model.GetProfileModel;
import com.gdi.utils.AppLogger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 3/5/2018.
 */

public class UpdateProfileMultipartRequest extends BaseMultipartRequest {

    //request params
    public static final String REQ_PARAM_FIRST_NAME = "first_name";
    public static final String REQ_PARAM_LAST_NAME = "last_name";
    public static final String REQ_PARAM_USERNAME = "username";
    public static final String REQ_PARAM_EMAIL = "email";
    public static final String REQ_PARAM_PHONE = "phone";
    public static final String REQ_PARAM_IMAGE_URL = "image";
    public static final String REQ_PARAM_ACCESS_TOKEN = "access-token";


    private Map<String, String> params = new HashMap<>();
    private Map<String, String> headerParams = new HashMap<>();
    private GetProfileModel getProfileModel ;
    byte[] byteData;
    String fName;

    public UpdateProfileMultipartRequest(GetProfileModel getProfileModel,
                                         String accessToken,
                                         byte[]data,
                                         String fileName,
                                         Response.Listener<NetworkResponse> listener,
                                         Response.ErrorListener errorListener) {
        super(ApiEndPoints.UPDATEPROFILE, listener, errorListener);
        params.put(REQ_PARAM_FIRST_NAME, getProfileModel.getFirst_name());
        params.put(REQ_PARAM_LAST_NAME, getProfileModel.getLast_name());
        params.put(REQ_PARAM_USERNAME, getProfileModel.getUsername());
        params.put(REQ_PARAM_EMAIL, getProfileModel.getEmail());
        params.put(REQ_PARAM_PHONE, getProfileModel.getPhone());
        params.put(REQ_PARAM_IMAGE_URL, getProfileModel.getImage_url());

        this.getProfileModel = getProfileModel;
        byteData = data;
        fName = fileName;
        headerParams.put(REQ_PARAM_ACCESS_TOKEN, accessToken);


        AppLogger.e("UpdateProfileHeaderParam", headerParams.toString());
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

    @Override
    protected Map<String, DataPart> getByteData() {
        Map<String, DataPart> params = new HashMap<>();
        DataPart dataPart = new DataPart();
        dataPart.setFileName(fName);
        dataPart.setContent(byteData);
        dataPart.setType("image/jpeg");
        params.put(REQ_PARAM_IMAGE_URL, dataPart);
        AppLogger.e("UpdateProfileParam", params.toString());
        return params;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headerParams;
    }
}
