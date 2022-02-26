package com.gdi.activity.Audit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.gdi.R;
import com.gdi.activity.BaseActivity;
import com.gdi.utils.AppUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AuditSectionsActivityMistrey extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.ll_brand_standard)
    LinearLayout llBrandStandard;
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

    private static final int SaveBSRequest = 110;
    private String auditName = "";
    private String brandName = "";
    private String locationName = "";
    private String auditId = "";
    private String bsStatus = "";

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
        setContentView(R.layout.activity_audit_sections_mistery);
        context = this;
        ButterKnife.bind(AuditSectionsActivityMistrey.this);
        initView();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        setActionBar();
        llBrandStandard = findViewById(R.id.ll_brand_standard);

        findViewById(R.id.ll_auditname).setVisibility(View.GONE);

        tvBrandName = findViewById(R.id.tv_brand_name);
        tvLocationName = findViewById(R.id.tv_location_name);
        tvAuditName = findViewById(R.id.tv_audit_name);

        bsIcon = findViewById(R.id.brand_standard_icon);
        bsText = findViewById(R.id.brand_standard_text);
        bsStartBtn = findViewById(R.id.brand_standard_start_btn);


        auditName = getIntent().getStringExtra("auditName");
        brandName = getIntent().getStringExtra("brandName");
        locationName = getIntent().getStringExtra("locationName");
        auditId = getIntent().getStringExtra("auditId");
        bsStatus = getIntent().getStringExtra("bsStatus");
       // bsStatus="1";

        setData();
        setBrandStandard();

        llBrandStandard.setOnClickListener(this);

    }

    private void setData(){
        tvBrandName.setText(brandName);
        tvLocationName.setText(locationName);
        tvAuditName.setText(auditName);
    }

    private void setActionBar() {
        initToolbar(toolbar);
        setTitle(getString(R.string.audit_option));
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
                        Intent brandStandard = new Intent(context, SubSectionsActivityMistery.class);
                        brandStandard.putExtra("auditId", auditId);
                        brandStandard.putExtra("editable", "1");
                        startActivityForResult(brandStandard,SaveBSRequest);
                    }
                }else {
                    AppUtils.toast(AuditSectionsActivityMistrey.this, "Audit not accessible");
                }
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SaveBSRequest && resultCode == Activity.RESULT_OK){
            bsStatus = data.getStringExtra("status");
            setBrandStandard();
        }

    }

    private void setBrandStandard(){
        switch (bsStatus){
           /* case "0":
                llBrandStandard.setBackground(context.getResources().getDrawable(R.drawable.na_status_border));
                bsIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.na_status_icon));
                bsText.setTextColor(context.getResources().getColor(R.color.textGrey));
                bsStartBtn.setText(getResources().getString(R.string.na));
                bsStartBtn.setTextColor(context.getResources().getColor(R.color.textGrey));
                break;*/
            case "1":
                llBrandStandard.setBackground(context.getResources().getDrawable(R.drawable.created_status_border));
                bsIcon.setImageDrawable(context.getResources().getDrawable(R.mipmap.create_status_icon));
                bsText.setTextColor(context.getResources().getColor(R.color.colorBlack));
                bsStartBtn.setText(getResources().getString(R.string.start));
                bsStartBtn.setTextColor(context.getResources().getColor(R.color.colorBlack));
                break;
            case "2":
                llBrandStandard.setBackground(context.getResources().getDrawable(R.drawable.resume_status_border));
                bsIcon.setImageDrawable(context.getResources().getDrawable(R.mipmap.resume_status));
                bsText.setTextColor(context.getResources().getColor(R.color.colorOrange));
                bsStartBtn.setText(getResources().getString(R.string.resume));
                bsStartBtn.setTextColor(context.getResources().getColor(R.color.colorOrange));
                break;
            case "3":
                llBrandStandard.setBackground(context.getResources().getDrawable(R.drawable.rejected_status_border));
                bsIcon.setImageDrawable(context.getResources().getDrawable(R.mipmap.rejected_status_icon));
                bsText.setTextColor(context.getResources().getColor(R.color.scoreRed));
                bsStartBtn.setText(getResources().getString(R.string.rejected));
                bsStartBtn.setTextColor(context.getResources().getColor(R.color.scoreRed));
                break;
            case "4":
                llBrandStandard.setBackground(context.getResources().getDrawable(R.drawable.submitted_status_border));
                bsIcon.setImageDrawable(context.getResources().getDrawable(R.mipmap.submitted_status_icon));
                bsText.setTextColor(context.getResources().getColor(R.color.scoreGreen));
                bsStartBtn.setText(getResources().getString(R.string.submitted));
                bsStartBtn.setTextColor(context.getResources().getColor(R.color.scoreGreen));
                break;
            case "5":
                llBrandStandard.setBackground(context.getResources().getDrawable(R.drawable.reviewed_status_border));
                bsIcon.setImageDrawable(context.getResources().getDrawable(R.mipmap.reviewed_status_icon));
                bsText.setTextColor(context.getResources().getColor(R.color.reviewedColor));
                bsStartBtn.setText(getResources().getString(R.string.reviewed));
                bsStartBtn.setTextColor(context.getResources().getColor(R.color.reviewedColor));
                break;
        }
    }


}
