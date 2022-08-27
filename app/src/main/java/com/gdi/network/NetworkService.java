package com.gdi.network;


/**
 * Created by MAds on 3/24/2015.
 */

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gdi.activity.oditylychange.ApiRequest;
import com.gdi.api.VolleyNetworkRequest;
import com.gdi.interfaces.INetworkEvent;
import com.gdi.utils.AppLogger;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;


import java.util.Map;

import okhttp3.MediaType;


/**
 * Created by MAds on 3/24/2015.
 */
public class NetworkService {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private String mURL;
    private int mMethod;
    private INetworkEvent networkEvent;
    private Context mContext;

    public NetworkService(String url, int method, INetworkEvent networkEvent, Context context) {
        mURL = url;
        mMethod = method;
        this.networkEvent = networkEvent;
        this.mContext=context;
    }

    public void call(Map<String,String> request) {
        SignIn(request);
    }

    private void SignIn(Map<String,String> request) {
        //  showProgressDialog();

        com.android.volley.Response.Listener<String> stringListener = new com.android.volley.Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onResponse(String response) {
                AppLogger.e("TAG", " SUCCESS Response: " + response);

                networkEvent.onNetworkCallCompleted("",mURL, response);

            }
        };

        com.android.volley.Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                AppLogger.e("TAG", "ERROR Response: " + error);

                networkEvent.onNetworkCallError(mURL, error.toString());


            }
        };
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().getCurrentUser().getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                ApiRequest signInRequest = new ApiRequest(request, mMethod,task.getResult().getToken(), mURL, mContext, stringListener, errorListener);
                                VolleyNetworkRequest.getInstance(mContext).addToRequestQueue(signInRequest);
                                Log.e("Task isSuccessful : ", "" + task.getResult().getToken());
                            }
                        }
                    });
        }



    }


}
