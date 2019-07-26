package com.gdi.api;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;

import java.util.HashMap;
import java.util.Map;

public class UploadImageRequest extends BaseMultipartRequest {

    String fName;
    byte[] byteData;

    public UploadImageRequest(String url, Map<String, String> headers, Response.Listener<NetworkResponse> listener, Response.ErrorListener errorListener) {
        super(url, headers, listener, errorListener);
    }

    public UploadImageRequest(String url, byte[]data, String fileName, Response.Listener<NetworkResponse>
            listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
        fName = fileName;
        byteData = data;
    }

    @Override
    protected Map<String, DataPart> getByteData() {
        Map<String, DataPart> params = new HashMap<>();
        DataPart dataPart = new DataPart();

        dataPart.setFileName(fName);
        dataPart.setContent(byteData);
        dataPart.setType("image/jpeg");
        params.put("imageName", dataPart);
        Log.e("UploadImageName", ""+fName);
        Log.e("UploadImageByteData", ""+byteData);
        Log.e("UploadImageParam", ""+params);
        return params;
    }
}
