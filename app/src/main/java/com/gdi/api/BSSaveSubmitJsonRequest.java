package com.gdi.api;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.gdi.utils.AppLogger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by namitbajaj on 10/18/17.
 */

public class BSSaveSubmitJsonRequest extends BaseJsonObjectRequest {

    //request params
    private static final String REQ_PARAM_AUDIT_ID = "audit_id";
    private static final String REQ_PARAM_SECTION_ID = "section_id";
    private static final String REQ_PARAM_SECTION_GRP_ID = "section_group_id";
    private static final String REQ_PARAM_AUDIT_DATE = "audit_date";
    private static final String REQ_PARAM_SAVE = "save";
    private static final String REQ_PARAM_ANSWER = "answers";
    public static final String REQ_PARAM_ACCESS_TOKEN = "access-token";
    public static final String REQ_PARAM_DEVICE_ID = "device-id";
    public static final String REQ_PARAM_DEVICE_TYPE = "device-type";
    public static final String REQ_PARAM_DEVICE_VERSION = "device-version";

    HashMap<String, String> headers = new HashMap<String, String>();

    public BSSaveSubmitJsonRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    public static JSONObject createInput(String auditId, String auditDate, String save, JSONArray answer) {



        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(REQ_PARAM_AUDIT_ID, auditId);
            jsonObject.put(REQ_PARAM_AUDIT_DATE, auditDate);
            jsonObject.put(REQ_PARAM_SAVE, save);
            jsonObject.put(REQ_PARAM_ANSWER, answer);

        } catch (Exception e) {
            e.printStackTrace();
        }

        AppLogger.e("SearchObject: ",""+jsonObject);

        return jsonObject;
    }
    public static JSONObject createInputNew(String auditId, String sectionid, String sectionGroupid, String auditDate, String save, JSONArray answer) {



        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(REQ_PARAM_AUDIT_ID, auditId);
            jsonObject.put(REQ_PARAM_SECTION_ID, Integer.parseInt(sectionid));
            jsonObject.put(REQ_PARAM_SECTION_GRP_ID, Integer.parseInt(sectionGroupid));
            jsonObject.put(REQ_PARAM_AUDIT_DATE, auditDate);
            jsonObject.put(REQ_PARAM_SAVE, save);
            jsonObject.put(REQ_PARAM_ANSWER, answer);

        } catch (Exception e) {
            e.printStackTrace();
        }

        AppLogger.e("SearchObject: ",""+jsonObject);

        return jsonObject;
    }
    public static JSONObject createInputNew(String auditId, String sectionid, String sectionGroupid) {



        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(REQ_PARAM_AUDIT_ID, auditId);
            jsonObject.put(REQ_PARAM_SECTION_ID, Integer.parseInt(sectionid));
            jsonObject.put(REQ_PARAM_SECTION_GRP_ID, Integer.parseInt(sectionGroupid));
        } catch (Exception e) {
            e.printStackTrace();
        }

        AppLogger.e("SearchObject: ",""+jsonObject);

        return jsonObject;
    }
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        /*HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("REQ_PARAM_AUTHORIZATION", "");*/
        return headers;
    }
}