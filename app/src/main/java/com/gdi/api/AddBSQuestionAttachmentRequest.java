package com.gdi.api;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.gdi.utils.AppConstant;
import com.gdi.utils.AppLogger;

import java.util.HashMap;
import java.util.Map;

public class AddBSQuestionAttachmentRequest extends BaseStringRequest {

    //request params
    private static final String REQ_PARAM_AUDIT_ID = "audit_id";
    private static final String REQ_PARAM_SECTION_GROUP_ID = "section_group_id";
    private static final String REQ_PARAM_SECTION_ID = "section_id";
    private static final String REQ_PARAM_QUESTION_ID = "question_id";
    private static final String REQ_PARAM_DESCRIPTION = "description";
    private static final String REQ_PARAM_IS_CRITICAL = "is_critical";
    private static final String REQ_PARAM_IS_FILE = "file";
    public static final String REQ_PARAM_ACCESS_TOKEN = "access-token";
    public static final String REQ_PARAM_DEVICE_ID = "device-id";
    public static final String REQ_PARAM_DEVICE_TYPE = "device-type";
    public static final String REQ_PARAM_DEVICE_VERSION = "device-version";

    private Map<String, String> params = new HashMap<>();
    private Map<String, String> headerParams = new HashMap<>();
    Map<String, DataPart> multipartParams = new HashMap<>();

    public AddBSQuestionAttachmentRequest(String accessToken, String url, String fileName, byte[] byteData,
                                          String auditId, String sectionGroupId, String sectionId,
                                          String questionId, String description, String isCritical,
                                          Response.Listener<String> listener,
                                          Response.ErrorListener errorListener) {
        super(Method.POST, url, listener, errorListener);
        params.put(REQ_PARAM_AUDIT_ID, auditId);
        params.put(REQ_PARAM_SECTION_GROUP_ID, sectionGroupId);
        params.put(REQ_PARAM_SECTION_ID, sectionId);
        params.put(REQ_PARAM_QUESTION_ID, questionId);
        params.put(REQ_PARAM_DESCRIPTION, description);
        params.put(REQ_PARAM_IS_CRITICAL, isCritical);
        headerParams.put(REQ_PARAM_ACCESS_TOKEN, accessToken);
        headerParams.put(REQ_PARAM_DEVICE_ID, AppConstant.DEVICE_ID);
        headerParams.put(REQ_PARAM_DEVICE_TYPE, AppConstant.DEVICE_TYPE);
        headerParams.put(REQ_PARAM_DEVICE_VERSION, AppConstant.VERSION);

        DataPart dataPart = new DataPart();
        dataPart.setFileName(fileName);
        dataPart.setContent(byteData);
        dataPart.setType("image/jpeg");
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
