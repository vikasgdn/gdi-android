package com.gdi.activity.Audit;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gdi.R;
import com.gdi.activity.BaseActivity;
import com.gdi.api.ApiEndPoints;
import com.gdi.api.GetReportRequest;
import com.gdi.api.VolleyNetworkRequest;
import com.gdi.model.audit.ExecutiveSummary.ExecutiveSummaryInfo;
import com.gdi.model.audit.ExecutiveSummary.ExecutiveSummaryRootObject;
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

public class ExecutiveSummaryAuditActivity extends BaseActivity {

    @BindView(R.id.et_executive_summary_summary)
    EditText esSummary;
    @BindView(R.id.es_save_btn)
    Button esSaveBtn;
    @BindView(R.id.es_submit_btn)
    Button esSubmitBtn;
    @BindView(R.id.executive_summary_attachment_count)
    Button esAttachmentCount;
    @BindView(R.id.executive_summary_file_btn)
    Button esFileBtn;
    @BindView(R.id.executive_summary_add_file)
    LinearLayout asAddFile;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    Context context;
    private String auditId = "";
    private static final String TAG = ExecutiveSummaryAuditActivity.class.getSimpleName();

    @Override
    protected void onResume() {
        super.onResume();
        AppUtils.hideKeyboard(ExecutiveSummaryAuditActivity.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_executive_summary_audit);
        context = this;
        ButterKnife.bind(ExecutiveSummaryAuditActivity.this);
        initView();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar();
        esSummary = (EditText) findViewById(R.id.et_executive_summary_summary);
        esSaveBtn = (Button) findViewById(R.id.es_save_btn);
        esSubmitBtn = (Button) findViewById(R.id.es_submit_btn);
        esAttachmentCount = (Button) findViewById(R.id.executive_summary_attachment_count);
        esFileBtn = (Button) findViewById(R.id.executive_summary_file_btn);
        asAddFile = (LinearLayout) findViewById(R.id.executive_summary_add_file);
        auditId = getIntent().getStringExtra("auditId");
        setExecutiveSummary();
    }

    private void setExecutiveSummary(){
        showProgressDialog();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLogger.e(TAG, "AudioImageResponse: " + response);
                try {
                    JSONObject object = new JSONObject(response);

                    if (!object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        ExecutiveSummaryRootObject executiveSummaryRootObject = new GsonBuilder().create()
                                .fromJson(object.toString(), ExecutiveSummaryRootObject.class);
                        if (executiveSummaryRootObject.getData() != null &&
                                executiveSummaryRootObject.getData().toString().length() > 0) {
                            setData(executiveSummaryRootObject.getData());
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

        String integrityUrl = ApiEndPoints.AUDITEXECUTIVESUMMARY + "?"
                + "audit_id=" + auditId ;
        GetReportRequest getReportRequest = new GetReportRequest(AppPrefs.getAccessToken(context),
                integrityUrl, stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(getReportRequest);
    }

    private void setData(ExecutiveSummaryInfo executiveSummaryInfo){
        showViewAccStatus(executiveSummaryInfo);

        if (!AppUtils.isStringEmpty(executiveSummaryInfo.getExecutive_summary())) {
            esSummary.setText(executiveSummaryInfo.getExecutive_summary());
        }
        if (!AppUtils.isStringEmpty("" + executiveSummaryInfo.getFile_count())){
            esAttachmentCount.setText("" + executiveSummaryInfo.getFile_count());
        }
    }

    private void setActionBar() {
        initToolbar(toolbar);
        setTitle("Executive Summary");
        enableBack(true);
        enableBackPressed();
    }

    private void showViewAccStatus(ExecutiveSummaryInfo executiveSummaryInfo){
        switch (executiveSummaryInfo.getExec_sum_status()){
            case 0:
                break;
            case 1:
                esSummary.setEnabled(true);
                esFileBtn.setVisibility(View.GONE);
                asAddFile.setVisibility(View.VISIBLE);
                break;
            case 2:
                esSummary.setEnabled(true);
                esFileBtn.setVisibility(View.GONE);
                asAddFile.setVisibility(View.VISIBLE);
                break;
            case 3:
                esSummary.setEnabled(false);
                esFileBtn.setVisibility(View.VISIBLE);
                asAddFile.setVisibility(View.GONE);
                break;
            case 4:
                esSummary.setEnabled(false);
                esFileBtn.setVisibility(View.VISIBLE);
                asAddFile.setVisibility(View.GONE);
                break;
            case 5:
                esSummary.setEnabled(false);
                esFileBtn.setVisibility(View.VISIBLE);
                asAddFile.setVisibility(View.GONE);
                break;
        }
    }
}
