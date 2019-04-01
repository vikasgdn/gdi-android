package com.gdi.api;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;

import java.util.HashMap;
import java.util.Map;

public class BaseMultipartRequest extends VolleyMultipartRequest {

    public BaseMultipartRequest(String url, Map<String, String> headers,
                                Response.Listener<NetworkResponse> listener, Response.ErrorListener errorListener) {
        super(url, headers, listener, errorListener);
    }

    public BaseMultipartRequest(String url, Response.Listener<NetworkResponse> listener,
                                Response.ErrorListener errorListener) {
        super(Request.Method.POST, url, listener, errorListener);
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        params.put("api_token", "gh659gjhvdyudo973823tt9gvjf7i6ric75r76");
        return params;
    }

    protected Map<String, DataPart> getByteData() {
        Map<String, DataPart> params = new HashMap<>();
        // file name could found file base or direct access from real path
        // for now just get bitmap data from ImageView
       /* params.put("imageName", new DataPart("bvhv.jpg",
                AppHelper.getFileDataFromDrawable(getBaseContext(), mAvatarImage.getDrawable()), "image/jpeg"));*/

        return params;
    }

}
