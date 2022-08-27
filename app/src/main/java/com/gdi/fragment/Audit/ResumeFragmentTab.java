package com.gdi.fragment.Audit;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gdi.hotel.mystery.audits.R;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResumeFragmentTab extends Fragment {

    @BindView(R.id.rv_audit_listing)
    RecyclerView auditListing;
    @BindView(R.id.et_search_bar)
    EditText searchBar;
    @BindView(R.id.tv_no_audit_found)
    TextView noAuditFountTxt;
    private Context context;
    String brandId = "";
    String locationId = "";
    String typeId = "";
    ArrayList<AuditInfo> arrayList = new ArrayList<>();
    ArrayList<AuditInfo> mainArrayList;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(AppUtils.isNetworkConnected(context)) {
            setAuditList();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_audit_listing_tab, container, false);
        ButterKnife.bind(this, view);
        auditListing = view.findViewById(R.id.rv_audit_listing);
        searchBar = view.findViewById(R.id.et_search_bar);
        noAuditFountTxt = view.findViewById(R.id.tv_no_audit_found);
        brandId = getArguments().getString("brandId");
        locationId = getArguments().getString("locationId");
        typeId = getArguments().getString("typeId");
        mainArrayList = new ArrayList<>();
        if(AppUtils.isNetworkConnected(context)) {
            setAuditList();
        }

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String value = "" + charSequence;
                mainArrayList.clear();
                if (value.equals("")){
                    setAuditList(arrayList);
                }else {
                    for (int j = 0; j < arrayList.size(); j++) {
                        String auditId = "" + arrayList.get(j).getAudit_id();
                        String auditorEmail = arrayList.get(j).getAuditor_email();

                        if (auditId.contains(value) || auditorEmail.contains(value)) {
                            mainArrayList.add(arrayList.get(j));

                        }
                    }
                    setAuditList(mainArrayList);
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

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
                            arrayList.clear();
                            arrayList.addAll(auditRootObject.getData());
                            setAuditList(arrayList);
                            //auditActionAdapter.notifyDataSetChanged();
                        }else {
                            noAuditFountTxt.setVisibility(View.VISIBLE);
                        }
                    } else if (object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        noAuditFountTxt.setVisibility(View.VISIBLE);
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
                + "status=" + "2" + "&"
                + "type=" + "0" + "&"
                + "page=" + "1" + "&"
                + "overdue=" + "";
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().getCurrentUser().getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                GetReportRequest getReportRequest = new GetReportRequest(AppPrefs.getAccessToken(context),task.getResult().getToken(), integrityUrl, stringListener, errorListener);
                                VolleyNetworkRequest.getInstance(context).addToRequestQueue(getReportRequest);
                            }
                        }
                    });
        }
    }

    private void setAuditList(ArrayList<AuditInfo> arrayList){
        ArrayList<AuditInfo> integrityInfos = new ArrayList<>();
        for (int i = 0 ; i < arrayList.size() ; i++){
            if (arrayList.get(i).getShow_data() == 1) {
                integrityInfos.add(arrayList.get(i));
            }
        }
        if (integrityInfos.size() > 0){
            noAuditFountTxt.setVisibility(View.GONE);
        }else {
            noAuditFountTxt.setVisibility(View.VISIBLE);
        }
        AuditActionAdapter auditActionAdapter = new AuditActionAdapter(context, integrityInfos, "2");
        auditListing.setLayoutManager(new LinearLayoutManager(context));
        auditListing.setAdapter(auditActionAdapter);
    }
}
