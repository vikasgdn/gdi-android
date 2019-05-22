package com.gdi.api;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.gdi.utils.AppConstant;
import com.gdi.utils.AppLogger;

import java.util.HashMap;
import java.util.Map;

public class EditBSAttachmentRequest extends BaseStringRequest {

    //request params
    private static final String REQ_PARAM_AUDIT_ID = "audit_id";
    private static final String REQ_PARAM_SECTION_FILE_ID = "audit_section_file_id";
    private static final String REQ_PARAM_DESCRIPTION = "description";
    private static final String REQ_PARAM_IS_FILE_NAME = "file_name";
    private static final String REQ_PARAM_IS_CRITICAL = "is_critical";
    public static final String REQ_PARAM_ACCESS_TOKEN = "access-token";
    public static final String REQ_PARAM_DEVICE_ID = "device-id";
    public static final String REQ_PARAM_DEVICE_TYPE = "device-type";
    public static final String REQ_PARAM_DEVICE_VERSION = "device-version";

    private Map<String, String> params = new HashMap<>();
    private Map<String, String> headerParams = new HashMap<>();


    public EditBSAttachmentRequest(String accessToken, String url, String fileName, String auditId,
                                   int sectionFileId, String description,
                                   int isCritical, Response.Listener<String> listener,
                                   Response.ErrorListener errorListener) {
        super(Method.POST, url, listener, errorListener);
        params.put(REQ_PARAM_AUDIT_ID, auditId);
        params.put(REQ_PARAM_SECTION_FILE_ID, ""+sectionFileId);
        params.put(REQ_PARAM_DESCRIPTION, description);
        params.put(REQ_PARAM_IS_FILE_NAME, fileName);
        params.put(REQ_PARAM_IS_CRITICAL, "" +isCritical);
        headerParams.put(REQ_PARAM_ACCESS_TOKEN, accessToken);
        headerParams.put(REQ_PARAM_DEVICE_ID, AppConstant.DEVICE_ID);
        headerParams.put(REQ_PARAM_DEVICE_TYPE, AppConstant.DEVICE_TYPE);
        headerParams.put(REQ_PARAM_DEVICE_VERSION, AppConstant.VERSION);

        AppLogger.e("AttachmentParam", ""+params);
        AppLogger.e("AttachmentHeader", ""+headerParams);

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
