package com.gdi.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.gdi.R;
import com.gdi.utils.CustomDialog;
import com.gdi.utils.CustomTypefaceTextView;

import butterknife.BindView;

public class OpenDialogActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private CustomDialog customDialog;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_dialog);
        context = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setUpToolBar();
        openStandardReportDialog();
    }

    private void setUpToolBar() {
        initToolbar(toolbar);
        setTitle("Guest Delight International");
    }

    private void openStandardReportDialog() {
        customDialog = new CustomDialog(context, R.layout.standard_report_dailog);
        customDialog.setCancelable(false);
        CustomTypefaceTextView tvSectionReport = (CustomTypefaceTextView) customDialog.findViewById(R.id.tv_section_report);
        CustomTypefaceTextView tvTrendLocation = (CustomTypefaceTextView) customDialog.findViewById(R.id.tv_trend_location);
        CustomTypefaceTextView tvLocationCampaign = (CustomTypefaceTextView) customDialog.findViewById(R.id.tv_location_campaign);
        CustomTypefaceTextView tvCancel = (CustomTypefaceTextView) customDialog.findViewById(R.id.tv_cancel);

        tvSectionReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ReportSectionGroupActivity.class));
                //customDialog.dismiss();
            }
        });
        tvTrendLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ReportTrendLocationActivity.class));
                //customDialog.dismiss();
            }
        });
        tvLocationCampaign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ReportLocationCampaignActivity.class));
                //customDialog.dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
                finish();
            }
        });
        customDialog.show();

    }
}
