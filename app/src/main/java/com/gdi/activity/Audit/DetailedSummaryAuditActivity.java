package com.gdi.activity.Audit;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gdi.R;
import com.gdi.activity.BaseActivity;
import com.gdi.adapter.DetailedSummaryAuditAdapter;
import com.gdi.api.ApiEndPoints;
import com.gdi.api.GetReportRequest;
import com.gdi.api.VolleyNetworkRequest;
import com.gdi.model.audit.DetailedSummary.DetailedSummaryInfo;
import com.gdi.model.audit.DetailedSummary.DetailedSummaryRootObject;
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

public class DetailedSummaryAuditActivity extends BaseActivity {

    @BindView(R.id.rv_detailed_summary_audit)
    RecyclerView rvDetailedSummaryAudit;
    @BindView(R.id.ds_save_btn)
    Button dsSaveBtn;
    @BindView(R.id.ds_submit_btn)
    Button dsSubmitBtn;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    Context context;
    private String auditId = "";
    public LayoutInflater inflater;
    private DetailedSummaryAuditAdapter detailedSummaryAuditAdapter;
    private static final String TAG = DetailedSummaryAuditActivity.class.getSimpleName();

    @Override
    protected void onResume() {
        super.onResume();
        AppUtils.hideKeyboard(DetailedSummaryAuditActivity.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_summary_audit);
        inflater = getLayoutInflater();
        context = this;
        ButterKnife.bind(DetailedSummaryAuditActivity.this);
        initView();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar();
        rvDetailedSummaryAudit = (RecyclerView) findViewById(R.id.rv_detailed_summary_audit);
        dsSaveBtn = (Button) findViewById(R.id.ds_save_btn);
        dsSubmitBtn = (Button) findViewById(R.id.ds_submit_btn);
        auditId = getIntent().getStringExtra("auditId");
        setDetailedSummaryQuestion();
    }

    private void setDetailedSummaryQuestion(){
        showProgressDialog();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLogger.e(TAG, "AudioImageResponse: " + response);
                try {
                    JSONObject object = new JSONObject(response);

                    if (!object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        DetailedSummaryRootObject brandStandardRootObject = new GsonBuilder().create()
                                .fromJson(object.toString(), DetailedSummaryRootObject.class);
                        if (brandStandardRootObject.getData() != null &&
                                brandStandardRootObject.getData().toString().length() > 0) {
                            ArrayList<DetailedSummaryInfo> arrayList = new ArrayList<>();
                            arrayList.addAll(brandStandardRootObject.getData());
                            setSummaryData(arrayList);
                            //setData(brandStandardRootObject.getData());
                            detailedSummaryAuditAdapter.notifyDataSetChanged();
                        }
                    } else if (object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        AppUtils.toast((BaseActivity) context,
                                object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hideProgressDialog();
            }

        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                AppLogger.e(TAG, "AudioImageError: " + error.getMessage());
                AppUtils.toast((BaseActivity) context, "Server temporary unavailable, Please try again");

            }
        };

        String integrityUrl = ApiEndPoints.AUDITDETAILEDSUMMARY + "?"
                + "audit_id=" + auditId ;
        GetReportRequest getReportRequest = new GetReportRequest(AppPrefs.getAccessToken(context),
                integrityUrl, stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(getReportRequest);
    }

    private void setSummaryData(ArrayList<DetailedSummaryInfo> arrayList){
        ArrayList<DetailedSummaryInfo> integrityInfos = new ArrayList<>();
        integrityInfos.addAll(arrayList);
        detailedSummaryAuditAdapter = new DetailedSummaryAuditAdapter(context, integrityInfos);
        rvDetailedSummaryAudit.setLayoutManager(new LinearLayoutManager(context));
        rvDetailedSummaryAudit.setAdapter(detailedSummaryAuditAdapter);
    }

    private void setActionBar() {
        initToolbar(toolbar);
        setTitle("Detailed Summary");
        enableBack(true);
        enableBackPressed();
    }
}
