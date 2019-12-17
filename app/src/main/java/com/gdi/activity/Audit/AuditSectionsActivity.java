package com.gdi.activity.Audit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gdi.R;
import com.gdi.activity.BaseActivity;
import com.gdi.api.ApiEndPoints;
import com.gdi.api.GetReportRequest;
import com.gdi.api.VolleyNetworkRequest;
import com.gdi.model.audit.BrandStandard.BrandStandardRootObject;
import com.gdi.model.audit.DetailedSummary.DetailedSummaryRootObject;
import com.gdi.model.audit.ExecutiveSummary.ExecutiveSummaryRootObject;
import com.gdi.utils.ApiResponseKeys;
import com.gdi.utils.AppLogger;
import com.gdi.utils.AppPrefs;
import com.gdi.utils.AppUtils;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AuditSectionsActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.ll_brand_standard)
    LinearLayout llBrandStandard;
    @BindView(R.id.ll_detailed_summary)
    LinearLayout llDetailedSummary;
    @BindView(R.id.ll_executive_summary)
    LinearLayout llExecutiveSummary;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_brand_name)
    TextView tvBrandName;
    @BindView(R.id.tv_location_name)
    TextView tvLocationName;
    @BindView(R.id.tv_audit_name)
    TextView tvAuditName;
    @BindView(R.id.brand_standard_icon)
    ImageView bsIcon;
    @BindView(R.id.brand_standard_text)
    TextView bsText;
    @BindView(R.id.brand_standard_start_btn)
    TextView bsStartBtn;
    @BindView(R.id.detailed_summary_icon)
    ImageView dsIcon;
    @BindView(R.id.detailed_summary_text)
    TextView dsText;
    @BindView(R.id.detailed_summary_start_btn)
    TextView dsStartBtn;
    @BindView(R.id.executive_summary_icon)
    ImageView esIcon;
    @BindView(R.id.executive_summary_text)
    TextView esText;
    @BindView(R.id.executive_summary_start_btn)
    TextView esStartBtn;
    private static final int SaveBSRequest = 110;
    private static final int SaveDSRequest = 111;
    private static final int SaveESRequest = 112;
    private String auditName = "";
    private String brandName = "";
    private String locationName = "";
    private String auditId = "";
    private String bsStatus = "";
    private String dsStatus = "";
    private String esStatus = "";
    Context context;

    @Override
    protected void onResume() {
        super.onResume();
        if (!AppUtils.isNetworkConnected(context)){
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit_sections);
        context = this;
        ButterKnife.bind(AuditSectionsActivity.this);
        initView();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        setActionBar();
        llBrandStandard = findViewById(R.id.ll_brand_standard);
        llDetailedSummary = findViewById(R.id.ll_detailed_summary);
        llExecutiveSummary = findViewById(R.id.ll_executive_summary);
        tvBrandName = findViewById(R.id.tv_brand_name);
        tvLocationName = findViewById(R.id.tv_location_name);
        tvAuditName = findViewById(R.id.tv_audit_name);

        bsIcon = findViewById(R.id.brand_standard_icon);
        bsText = findViewById(R.id.brand_standard_text);
        bsStartBtn = findViewById(R.id.brand_standard_start_btn);
        dsIcon = findViewById(R.id.detailed_summary_icon);
        dsText = findViewById(R.id.detailed_summary_text);
        dsStartBtn = findViewById(R.id.detailed_summary_start_btn);
        esIcon = findViewById(R.id.executive_summary_icon);
        esText = findViewById(R.id.executive_summary_text);
        esStartBtn = findViewById(R.id.executive_summary_start_btn);


        auditName = getIntent().getStringExtra("auditName");
        brandName = getIntent().getStringExtra("brandName");
        locationName = getIntent().getStringExtra("locationName");
        auditId = getIntent().getStringExtra("auditId");
        bsStatus = getIntent().getStringExtra("bsStatus");
        dsStatus = getIntent().getStringExtra("dsStatus");
        esStatus = getIntent().getStringExtra("esStatus");
        setData();
        setBrandStandard();
        setDetailedSummary();
        setExecutiveSummary();
        llBrandStandard.setOnClickListener(this);
        llDetailedSummary.setOnClickListener(this);
        llExecutiveSummary.setOnClickListener(this);
    }

    private void setData(){
        tvBrandName.setText(brandName);
        tvLocationName.setText(locationName);
        tvAuditName.setText(auditName);
    }

    private void setActionBar() {
        initToolbar(toolbar);
        setTitle("Audit Option");
        enableBack(true);
        enableBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_brand_standard:
                if (Integer.valueOf(bsStatus) != 0) {
                    if (Integer.valueOf(bsStatus) < 4) {
                        Intent brandStandard = new Intent(context, SubSectionsActivity.class);
                        brandStandard.putExtra("auditId", auditId);
                        brandStandard.putExtra("editable", "0");
                        startActivityForResult(brandStandard,SaveBSRequest);
                    }else {
                        Intent brandStandard = new Intent(context, SubSectionsActivity.class);
                        brandStandard.putExtra("auditId", auditId);
                        brandStandard.putExtra("editable", "1");
                        startActivityForResult(brandStandard,SaveBSRequest);
                    }
                }else {
                    AppUtils.toast(AuditSectionsActivity.this, "Audit not accessible");
                }
                break;
            case R.id.ll_detailed_summary:
                if (Integer.valueOf(dsStatus) != 0) {
                    if (Integer.valueOf(dsStatus) < 4) {
                        Intent detailedSummary = new Intent(context, DetailedSummaryAuditActivity.class);
                        detailedSummary.putExtra("auditId", auditId);
                        detailedSummary.putExtra("editable", "0");
                        startActivityForResult(detailedSummary,SaveDSRequest);
                    }else {
                        Intent detailedSummary = new Intent(context, DetailedSummaryAuditActivity.class);
                        detailedSummary.putExtra("auditId", auditId);
                        detailedSummary.putExtra("editable", "1");
                        startActivityForResult(detailedSummary,SaveDSRequest);
                    }
                }else {
                    AppUtils.toast(AuditSectionsActivity.this, "Audit not accessible");
                }
                break;
            case R.id.ll_executive_summary:
                if (Integer.valueOf(esStatus) != 0) {
                    if (Integer.valueOf(esStatus) < 4) {
                        Intent executiveSummary = new Intent(context, ExecutiveSummaryAuditActivity.class);
                        executiveSummary.putExtra("auditId", auditId);
                        executiveSummary.putExtra("editable", "0");
                        startActivityForResult(executiveSummary, SaveESRequest);
                    }else {
                        Intent executiveSummary = new Intent(context, ExecutiveSummaryAuditActivity.class);
                        executiveSummary.putExtra("auditId", auditId);
                        executiveSummary.putExtra("editable", "1");
                        startActivityForResult(executiveSummary, SaveESRequest);
                    }
                }else {
                    AppUtils.toast(AuditSectionsActivity.this, "Audit not accessible");
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SaveBSRequest && resultCode == Activity.RESULT_OK){
            bsStatus = data.getStringExtra("status");
            //setBrandStandardQuestion();
            setBrandStandard();
            //finish();
            //answerSavedDialog();
            //AppUtils.toast(AuditSectionsActivity.this, "Answer Saved");
        }

        if (requestCode == SaveDSRequest && resultCode == Activity.RESULT_OK){
            dsStatus = data.getStringExtra("status");
            //setDetailedSummaryQuestion();
            setDetailedSummary();
            //finish();
            //answerSavedDialog();
            //AppUtils.toast(AuditSectionsActivity.this, "Answer Saved");
        }

        if (requestCode == SaveESRequest && resultCode == Activity.RESULT_OK){
            esStatus = data.getStringExtra("status");
            //setExecutiveSummaryapi();
            setExecutiveSummary();
            //finish();
            //answerSavedDialog();
            //AppUtils.toast(AuditSectionsActivity.this, "Answer Saved");
        }
    }

    private void setBrandStandard(){
        switch (bsStatus){
            case "0":
                llBrandStandard.setBackground(context.getResources().getDrawable(R.drawable.na_status_border));
                bsIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.na_status_icon));
                bsText.setTextColor(context.getResources().getColor(R.color.textGrey));
                bsStartBtn.setText(getResources().getString(R.string.na));
                bsStartBtn.setTextColor(context.getResources().getColor(R.color.textGrey));
                break;
            case "1":
                llBrandStandard.setBackground(context.getResources().getDrawable(R.drawable.created_status_border));
                bsIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.create_status_icon));
                bsText.setTextColor(context.getResources().getColor(R.color.colorBlack));
                bsStartBtn.setText(getResources().getString(R.string.start));
                bsStartBtn.setTextColor(context.getResources().getColor(R.color.colorBlack));
                break;
            case "2":
                llBrandStandard.setBackground(context.getResources().getDrawable(R.drawable.resume_status_border));
                bsIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.resume_status));
                bsText.setTextColor(context.getResources().getColor(R.color.colorOrange));
                bsStartBtn.setText(getResources().getString(R.string.resume));
                bsStartBtn.setTextColor(context.getResources().getColor(R.color.colorOrange));
                break;
            case "3":
                llBrandStandard.setBackground(context.getResources().getDrawable(R.drawable.rejected_status_border));
                bsIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.rejected_status_icon));
                bsText.setTextColor(context.getResources().getColor(R.color.scoreRed));
                bsStartBtn.setText(getResources().getString(R.string.rejected));
                bsStartBtn.setTextColor(context.getResources().getColor(R.color.scoreRed));
                break;
            case "4":
                llBrandStandard.setBackground(context.getResources().getDrawable(R.drawable.submitted_status_border));
                bsIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.submitted_status_icon));
                bsText.setTextColor(context.getResources().getColor(R.color.scoreGreen));
                bsStartBtn.setText(getResources().getString(R.string.submitted));
                bsStartBtn.setTextColor(context.getResources().getColor(R.color.scoreGreen));
                break;
            case "5":
                llBrandStandard.setBackground(context.getResources().getDrawable(R.drawable.reviewed_status_border));
                bsIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.reviewed_status_icon));
                bsText.setTextColor(context.getResources().getColor(R.color.reviewedColor));
                bsStartBtn.setText(getResources().getString(R.string.reviewed));
                bsStartBtn.setTextColor(context.getResources().getColor(R.color.reviewedColor));
                break;
        }
    }

    private void setDetailedSummary(){
        switch (dsStatus){
            case "0":
                llDetailedSummary.setBackground(context.getResources().getDrawable(R.drawable.na_status_border));
                dsIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.na_status_icon));
                dsText.setTextColor(context.getResources().getColor(R.color.textGrey));
                dsStartBtn.setText(getResources().getString(R.string.na));
                dsStartBtn.setTextColor(context.getResources().getColor(R.color.textGrey));
                break;
            case "1":
                llDetailedSummary.setBackground(context.getResources().getDrawable(R.drawable.created_status_border));
                dsIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.create_status_icon));
                dsText.setTextColor(context.getResources().getColor(R.color.colorBlack));
                dsStartBtn.setText(getResources().getString(R.string.start));
                dsStartBtn.setTextColor(context.getResources().getColor(R.color.colorBlack));
                break;
            case "2":
                llDetailedSummary.setBackground(context.getResources().getDrawable(R.drawable.resume_status_border));
                dsIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.resume_status));
                dsText.setTextColor(context.getResources().getColor(R.color.colorOrange));
                dsStartBtn.setText(getResources().getString(R.string.resume));
                dsStartBtn.setTextColor(context.getResources().getColor(R.color.colorOrange));
                break;
            case "3":
                llDetailedSummary.setBackground(context.getResources().getDrawable(R.drawable.rejected_status_border));
                dsIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.rejected_status_icon));
                dsText.setTextColor(context.getResources().getColor(R.color.scoreRed));
                dsStartBtn.setText(getResources().getString(R.string.rejected));
                dsStartBtn.setTextColor(context.getResources().getColor(R.color.scoreRed));
                break;
            case "4":
                llDetailedSummary.setBackground(context.getResources().getDrawable(R.drawable.submitted_status_border));
                dsIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.submitted_status_icon));
                dsText.setTextColor(context.getResources().getColor(R.color.scoreGreen));
                dsStartBtn.setText(getResources().getString(R.string.submitted));
                dsStartBtn.setTextColor(context.getResources().getColor(R.color.scoreGreen));
                break;
            case "5":
                llDetailedSummary.setBackground(context.getResources().getDrawable(R.drawable.reviewed_status_border));
                dsIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.reviewed_status_icon));
                dsText.setTextColor(context.getResources().getColor(R.color.reviewedColor));
                dsStartBtn.setText(getResources().getString(R.string.reviewed));
                dsStartBtn.setTextColor(context.getResources().getColor(R.color.reviewedColor));
                break;
        }
    }

    private void setExecutiveSummary(){
        switch (esStatus){
            case "0":
                llExecutiveSummary.setBackground(context.getResources().getDrawable(R.drawable.na_status_border));
                esIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.na_status_icon));
                esText.setTextColor(context.getResources().getColor(R.color.textGrey));
                esStartBtn.setText(getResources().getString(R.string.na));
                esStartBtn.setTextColor(context.getResources().getColor(R.color.textGrey));
                break;
            case "1":
                llExecutiveSummary.setBackground(context.getResources().getDrawable(R.drawable.created_status_border));
                esIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.create_status_icon));
                esText.setTextColor(context.getResources().getColor(R.color.colorBlack));
                esStartBtn.setText(getResources().getString(R.string.start));
                esStartBtn.setTextColor(context.getResources().getColor(R.color.colorBlack));
                break;
            case "2":
                llExecutiveSummary.setBackground(context.getResources().getDrawable(R.drawable.resume_status_border));
                esIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.resume_status));
                esText.setTextColor(context.getResources().getColor(R.color.colorOrange));
                esStartBtn.setText(getResources().getString(R.string.resume));
                esStartBtn.setTextColor(context.getResources().getColor(R.color.colorOrange));
                break;
            case "3":
                llExecutiveSummary.setBackground(context.getResources().getDrawable(R.drawable.rejected_status_border));
                esIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.rejected_status_icon));
                esText.setTextColor(context.getResources().getColor(R.color.statusRed));
                esStartBtn.setText(getResources().getString(R.string.rejected));
                esStartBtn.setTextColor(context.getResources().getColor(R.color.statusRed));
                break;
            case "4":
                llExecutiveSummary.setBackground(context.getResources().getDrawable(R.drawable.submitted_status_border));
                esIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.submitted_status_icon));
                esText.setTextColor(context.getResources().getColor(R.color.scoreGreen));
                esStartBtn.setText(getResources().getString(R.string.submitted));
                esStartBtn.setTextColor(context.getResources().getColor(R.color.scoreGreen));
                break;
            case "5":
                llExecutiveSummary.setBackground(context.getResources().getDrawable(R.drawable.reviewed_status_border));
                esIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.reviewed_status_icon));
                esText.setTextColor(context.getResources().getColor(R.color.reviewedColor));
                esStartBtn.setText(getResources().getString(R.string.reviewed));
                esStartBtn.setTextColor(context.getResources().getColor(R.color.reviewedColor));
                break;
        }
    }

    private void setDetailedSummaryQuestion(){
        showProgressDialog();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLogger.e("", "AudioImageResponse: " + response);
                try {
                    JSONObject object = new JSONObject(response);

                    if (!object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        DetailedSummaryRootObject brandStandardRootObject = new GsonBuilder().create()
                                .fromJson(object.toString(), DetailedSummaryRootObject.class);
                        if (brandStandardRootObject.getData() != null &&
                                brandStandardRootObject.getData().toString().length() > 0) {
                            dsStatus = "" + brandStandardRootObject.getData().get(0).getDetailed_sum_status();
                            setDetailedSummary();
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
                AppLogger.e("", "AudioImageError: " + error.getMessage());
                AppUtils.toast((BaseActivity) context, "Server temporary unavailable, Please try again");

            }
        };

        String integrityUrl = ApiEndPoints.AUDITDETAILEDSUMMARY + "?"
                + "audit_id=" + auditId ;
        GetReportRequest getReportRequest = new GetReportRequest(AppPrefs.getAccessToken(context),
                integrityUrl, stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(getReportRequest);
    }

    private void setExecutiveSummaryapi(){
        showProgressDialog();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLogger.e("", "AudioImageResponse: " + response);
                try {
                    JSONObject object = new JSONObject(response);

                    if (!object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        ExecutiveSummaryRootObject executiveSummaryRootObject = new GsonBuilder().create()
                                .fromJson(object.toString(), ExecutiveSummaryRootObject.class);
                        if (executiveSummaryRootObject.getData() != null &&
                                executiveSummaryRootObject.getData().toString().length() > 0) {
                            esStatus = "" + executiveSummaryRootObject.getData().getExec_sum_status();
                            setExecutiveSummary();
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
                AppLogger.e("", "AudioImageError: " + error.getMessage());
                AppUtils.toast((BaseActivity) context, "Server temporary unavailable, Please try again");

            }
        };

        String integrityUrl = ApiEndPoints.AUDITEXECUTIVESUMMARY + "?"
                + "audit_id=" + auditId ;
        GetReportRequest getReportRequest = new GetReportRequest(AppPrefs.getAccessToken(context),
                integrityUrl, stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(getReportRequest);
    }

    private void setBrandStandardQuestion(){
        showProgressDialog();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLogger.e("", "BrandStandardResponse: " + response);
                try {
                    JSONObject object = new JSONObject(response);

                    if (!object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        BrandStandardRootObject brandStandardRootObject = new GsonBuilder().create()
                                .fromJson(object.toString(), BrandStandardRootObject.class);
                        if (brandStandardRootObject.getData() != null &&
                                brandStandardRootObject.getData().toString().length() > 0) {
                            bsStatus = "" + brandStandardRootObject.getData().getBrand_std_status();
                            setBrandStandard();
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
                AppLogger.e("", "AudioImageError: " + error.getMessage());
                AppUtils.toast((BaseActivity) context, "Server temporary unavailable, Please try again");

            }
        };

        String integrityUrl = ApiEndPoints.BRANDSTANDARD + "?"
                + "audit_id=" + auditId ;
        GetReportRequest getReportRequest = new GetReportRequest(AppPrefs.getAccessToken(context),
                integrityUrl, stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(getReportRequest);
    }
}
