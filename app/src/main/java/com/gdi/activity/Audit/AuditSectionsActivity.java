package com.gdi.activity.Audit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.activity.BaseActivity;

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
    private String auditName = "";
    private String brandName = "";
    private String locationName = "";
    private String auditId = "";
    private String bsStatus = "";
    private String dsStatus = "";
    private String esStatus = "";
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit_sections);
        context = this;
        ButterKnife.bind(AuditSectionsActivity.this);
        initView();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar();
        llBrandStandard = (LinearLayout) findViewById(R.id.ll_brand_standard);
        llDetailedSummary = (LinearLayout) findViewById(R.id.ll_detailed_summary);
        llExecutiveSummary = (LinearLayout) findViewById(R.id.ll_executive_summary);
        tvBrandName = (TextView) findViewById(R.id.tv_brand_name);
        tvLocationName = (TextView) findViewById(R.id.tv_location_name);
        tvAuditName = (TextView) findViewById(R.id.tv_audit_name);

        bsIcon = (ImageView) findViewById(R.id.brand_standard_icon);
        bsText = (TextView) findViewById(R.id.brand_standard_text);
        bsStartBtn = (TextView) findViewById(R.id.brand_standard_start_btn);
        dsIcon = (ImageView) findViewById(R.id.detailed_summary_icon);
        dsText = (TextView) findViewById(R.id.detailed_summary_text);
        dsStartBtn = (TextView) findViewById(R.id.detailed_summary_start_btn);
        esIcon = (ImageView) findViewById(R.id.executive_summary_icon);
        esText = (TextView) findViewById(R.id.executive_summary_text);
        esStartBtn = (TextView) findViewById(R.id.executive_summary_start_btn);


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
                Intent brandStandard = new Intent(context, SubSectionsActivity.class);
                brandStandard.putExtra("auditId", auditId);
                startActivity(brandStandard);
                break;
            case R.id.ll_detailed_summary:
                Intent detailedSummary = new Intent(context, DetailedSummaryAuditActivity.class);
                detailedSummary.putExtra("auditId", auditId);
                startActivity(detailedSummary);
                break;
            case R.id.ll_executive_summary:
                Intent executiveSummary = new Intent(context, ExecutiveSummaryAuditActivity.class);
                executiveSummary.putExtra("auditId", auditId);
                startActivity(executiveSummary);
                break;
        }
    }

    private void setBrandStandard(){
        switch (bsStatus){
            case "0":
                llBrandStandard.setBackground(context.getResources().getDrawable(R.drawable.na_status_border));
                bsIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.na_status_icon));
                bsText.setTextColor(context.getResources().getColor(R.color.textGrey));
                bsStartBtn.setTextColor(context.getResources().getColor(R.color.textGrey));
                break;
            case "1":
                llBrandStandard.setBackground(context.getResources().getDrawable(R.drawable.created_status_border));
                bsIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.create_status_icon));
                bsText.setTextColor(context.getResources().getColor(R.color.colorBlack));
                bsStartBtn.setTextColor(context.getResources().getColor(R.color.colorBlack));
                break;
            case "2":
                llBrandStandard.setBackground(context.getResources().getDrawable(R.drawable.created_status_border));
                bsIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.create_status_icon));
                bsText.setTextColor(context.getResources().getColor(R.color.colorBlack));
                bsStartBtn.setTextColor(context.getResources().getColor(R.color.colorBlack));
                break;
            case "3":
                llBrandStandard.setBackground(context.getResources().getDrawable(R.drawable.rejected_status_border));
                bsIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.rejected_status_icon));
                bsText.setTextColor(context.getResources().getColor(R.color.scoreRed));
                bsStartBtn.setTextColor(context.getResources().getColor(R.color.scoreRed));
                break;
            case "4":
                llBrandStandard.setBackground(context.getResources().getDrawable(R.drawable.submitted_status_border));
                bsIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.submitted_status_icon));
                bsText.setTextColor(context.getResources().getColor(R.color.scoreGreen));
                bsStartBtn.setTextColor(context.getResources().getColor(R.color.scoreGreen));
                break;
            case "5":
                llBrandStandard.setBackground(context.getResources().getDrawable(R.drawable.reviewed_status_border));
                bsIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.reviewed_status_icon));
                bsText.setTextColor(context.getResources().getColor(R.color.reviewedColor));
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
                dsStartBtn.setTextColor(context.getResources().getColor(R.color.textGrey));
                break;
            case "1":
                llDetailedSummary.setBackground(context.getResources().getDrawable(R.drawable.created_status_border));
                dsIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.create_status_icon));
                dsText.setTextColor(context.getResources().getColor(R.color.colorBlack));
                dsStartBtn.setTextColor(context.getResources().getColor(R.color.colorBlack));
                break;
            case "2":
                llDetailedSummary.setBackground(context.getResources().getDrawable(R.drawable.created_status_border));
                dsIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.create_status_icon));
                dsText.setTextColor(context.getResources().getColor(R.color.colorBlack));
                dsStartBtn.setTextColor(context.getResources().getColor(R.color.colorBlack));
                break;
            case "3":
                llDetailedSummary.setBackground(context.getResources().getDrawable(R.drawable.rejected_status_border));
                dsIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.rejected_status_icon));
                dsText.setTextColor(context.getResources().getColor(R.color.scoreRed));
                dsStartBtn.setTextColor(context.getResources().getColor(R.color.scoreRed));
                break;
            case "4":
                llDetailedSummary.setBackground(context.getResources().getDrawable(R.drawable.submitted_status_border));
                dsIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.submitted_status_icon));
                dsText.setTextColor(context.getResources().getColor(R.color.scoreGreen));
                dsStartBtn.setTextColor(context.getResources().getColor(R.color.scoreGreen));
                break;
            case "5":
                llDetailedSummary.setBackground(context.getResources().getDrawable(R.drawable.reviewed_status_border));
                dsIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.reviewed_status_icon));
                dsText.setTextColor(context.getResources().getColor(R.color.reviewedColor));
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
                esStartBtn.setTextColor(context.getResources().getColor(R.color.textGrey));
                break;
            case "1":
                llExecutiveSummary.setBackground(context.getResources().getDrawable(R.drawable.created_status_border));
                esIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.create_status_icon));
                esText.setTextColor(context.getResources().getColor(R.color.colorBlack));
                esStartBtn.setTextColor(context.getResources().getColor(R.color.colorBlack));
                break;
            case "2":
                llExecutiveSummary.setBackground(context.getResources().getDrawable(R.drawable.created_status_border));
                esIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.create_status_icon));
                esText.setTextColor(context.getResources().getColor(R.color.colorBlack));
                esStartBtn.setTextColor(context.getResources().getColor(R.color.colorBlack));
                break;
            case "3":
                llExecutiveSummary.setBackground(context.getResources().getDrawable(R.drawable.rejected_status_border));
                esIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.rejected_status_icon));
                esText.setTextColor(context.getResources().getColor(R.color.scoreRed));
                esStartBtn.setTextColor(context.getResources().getColor(R.color.scoreRed));
                break;
            case "4":
                llExecutiveSummary.setBackground(context.getResources().getDrawable(R.drawable.submitted_status_border));
                esIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.submitted_status_icon));
                esText.setTextColor(context.getResources().getColor(R.color.scoreGreen));
                esStartBtn.setTextColor(context.getResources().getColor(R.color.scoreGreen));
                break;
            case "5":
                llExecutiveSummary.setBackground(context.getResources().getDrawable(R.drawable.reviewed_status_border));
                esIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.reviewed_status_icon));
                esText.setTextColor(context.getResources().getColor(R.color.reviewedColor));
                esStartBtn.setTextColor(context.getResources().getColor(R.color.reviewedColor));
                break;
        }
    }
}
