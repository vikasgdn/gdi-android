package com.gdi.fragment.Audit;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gdi.R;
import com.gdi.activity.BaseActivity;
import com.gdi.adapter.AuditActionAdapter;
import com.gdi.api.ApiEndPoints;
import com.gdi.api.GetReportRequest;
import com.gdi.api.VolleyNetworkRequest;
import com.gdi.model.audit.AuditInfo;
import com.gdi.model.audit.AuditRootObject;
import com.gdi.utils.ApiResponseKeys;
import com.gdi.utils.AppLogger;
import com.gdi.utils.AppPrefs;
import com.gdi.utils.AppUtils;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RejectedFragmentTab extends Fragment {

    @BindView(R.id.rv_audit_listing)
    RecyclerView auditListing;
    private Context context;
    String brandId = "";
    String locationId = "";
    String typeId = "";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_audit_listing_tab, container, false);
        ButterKnife.bind(this, view);
        auditListing = (RecyclerView)view.findViewById(R.id.rv_audit_listing);
        brandId = getArguments().getString("brandId");
        locationId = getArguments().getString("locationId");
        typeId = getArguments().getString("typeId");
        setAuditList();
        return view;
    }

    private void setAuditList(){
        ((BaseActivity)context).showProgressDialog();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLogger.e("", "AuditListingResponse: " + response);
                try {
                    JSONObject object = new JSONObject(response);

                    if (!object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        AuditRootObject auditRootObject = new GsonBuilder().create()
                                .fromJson(object.toString(), AuditRootObject.class);
                        if (auditRootObject.getData() != null &&
                                auditRootObject.getData().toString().length() > 0) {
                            ArrayList<AuditInfo> arrayList = new ArrayList<>();
                            arrayList.addAll(auditRootObject.getData());
                            setAuditList(arrayList);
                            //auditActionAdapter.notifyDataSetChanged();
                        }
                    } else if (object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {

                        AppUtils.toast((BaseActivity) context,
                                object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ((BaseActivity)context).hideProgressDialog();
            }

        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((BaseActivity)context).hideProgressDialog();
                AppLogger.e("", "AuditListingError: " + error.getMessage());
                AppUtils.toast((BaseActivity) context, "Server temporary unavailable, Please try again");

            }
        };
        AppLogger.e("", "Brand Id: " + brandId);
        AppLogger.e("", "Location Id: " + locationId);
        String integrityUrl = ApiEndPoints.AUDITlIST + "?"
                + "brand_id=" + brandId + "&"
                + "location_id=" + locationId  + "&"
                + "audit_type_id=" + typeId + "&"
                + "filter_brand_std_status=" + "" + "&"
                + "filter_detailed_sum_status=" + "" + "&"
                + "filter_exec_sum_status=" + "" + "&"
                + "status=" + "3" + "&"
                + "type=" + "0" + "&"
                + "page=" + "1" + "&"
                + "overdue=" + "";
        GetReportRequest getReportRequest = new GetReportRequest(AppPrefs.getAccessToken(context),
                integrityUrl, stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(getReportRequest);
    }

    private void setAuditList(ArrayList<AuditInfo> arrayList){
        ArrayList<AuditInfo> integrityInfos = new ArrayList<>();
        integrityInfos.addAll(arrayList);
        AuditActionAdapter auditActionAdapter = new AuditActionAdapter(context, integrityInfos);
        auditListing.setLayoutManager(new LinearLayoutManager(context));
        auditListing.setAdapter(auditActionAdapter);
    }
}
