package com.gdi.api;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.gdi.model.GetProfileModel;
import com.gdi.utils.AppLogger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 3/5/2018.
 */

public class UpdateProfileRequest extends BaseStringRequest {

    //request params
    public static final String REQ_PARAM_FIRST_NAME = "first_name";
    public static final String REQ_PARAM_LAST_NAME = "last_name";
    public static final String REQ_PARAM_USERNAME = "username";
    public static final String REQ_PARAM_EMAIL = "email";
    public static final String REQ_PARAM_PHONE = "phone";
    public static final String REQ_PARAM_IMAGE_URL = "image_url";
    public static final String REQ_PARAM_ACCESS_TOKEN = "access-token";


    private Map<String, String> params = new HashMap<>();
    private Map<String, String> headerParams = new HashMap<>();

    public UpdateProfileRequest(GetProfileModel getProfileModel,
                                String accessToken,
                                Response.Listener<String> listener,
                                Response.ErrorListener errorListener) {
        super(Method.POST, ApiEndPoints.GETPROFILE, listener, errorListener);
        params.put(REQ_PARAM_FIRST_NAME, getProfileModel.getFirst_name());
        params.put(REQ_PARAM_LAST_NAME, getProfileModel.getLast_name());
        params.put(REQ_PARAM_USERNAME, getProfileModel.getUsername());
        params.put(REQ_PARAM_EMAIL, getProfileModel.getEmail());
        params.put(REQ_PARAM_PHONE, getProfileModel.getPhone());
        params.put(REQ_PARAM_IMAGE_URL, getProfileModel.getImage_url());

        headerParams.put(REQ_PARAM_ACCESS_TOKEN, accessToken);

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
