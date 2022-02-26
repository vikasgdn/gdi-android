package com.gdi.api;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.gdi.utils.AppConstant;
import com.gdi.utils.AppLogger;

import java.util.HashMap;
import java.util.Map;

public class AddESAttachmentRequest extends BaseStringRequest {

    //request params
    private static final String REQ_PARAM_AUDIT_ID = "audit_id";
    private static final String REQ_PARAM_DESCRIPTION = "description";
    private static final String REQ_PARAM_IS_FILE = "file";
    private static final String REQ_PARAM_LATITUDE = "latitude";
    private static final String REQ_PARAM_LONGITUDE = "longitude";
    public static final String REQ_PARAM_ACCESS_TOKEN = "access-token";
    public static final String REQ_PARAM_DEVICE_ID = "device-id";
    public static final String REQ_PARAM_DEVICE_TYPE = "device-type";
    public static final String REQ_PARAM_DEVICE_VERSION = "device-version";

    private Map<String, String> params = new HashMap<>();
    private Map<String, String> headerParams = new HashMap<>();
    Map<String, DataPart> multipartParams = new HashMap<>();

    public AddESAttachmentRequest(String accessToken, String url, String fileName, byte[] byteData,
                                  String auditId, String description, String latitude, String longitude,String type,
                                  Response.Listener<String> listener,
                                  Response.ErrorListener errorListener) {
        super(Method.POST, url, listener, errorListener);
        params.put(REQ_PARAM_AUDIT_ID, auditId);
        params.put(REQ_PARAM_DESCRIPTION, description);
        params.put(REQ_PARAM_LATITUDE, latitude);
        params.put(REQ_PARAM_LONGITUDE, longitude);
        headerParams.put(REQ_PARAM_ACCESS_TOKEN, accessToken);
        headerParams.put(REQ_PARAM_DEVICE_ID, AppConstant.DEVICE_ID);
        headerParams.put(REQ_PARAM_DEVICE_TYPE, AppConstant.DEVICE_TYPE);
        headerParams.put(REQ_PARAM_DEVICE_VERSION, AppConstant.VERSION);

        DataPart dataPart = new DataPart();

        if(type.equalsIgnoreCase("video"))
        {
            fileName=fileName+".mp4";
            dataPart.setType("video/*");
        }
        else {
            fileName=fileName+".jpeg";
            dataPart.setType("image/jpeg");
        }
        dataPart.setFileName(fileName);
        dataPart.setContent(byteData);
        multipartParams.put(REQ_PARAM_IS_FILE, dataPart);

        AppLogger.e("UploadImageName", ""+fileName);
        AppLogger.e("UploadImageByteData", ""+byteData);
        AppLogger.e("AttachmentParam", ""+params);
        AppLogger.e("AttachmentHeader", ""+headerParams);
        AppLogger.e("AttachmentMultiPartParam", ""+multipartParams);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headerParams;
    }

    @Override
    protected Map<String, DataPart> getByteData() throws AuthFailureError {
        return multipartParams;
    }
}
