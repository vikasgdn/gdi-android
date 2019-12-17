package com.gdi.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.activity.MysteryAuditReport.ReportAudioImageActivity1;
import com.gdi.activity.MysteryAuditReport.ReportAuditActivity;
import com.gdi.activity.BaseActivity;
import com.gdi.activity.MysteryAuditReport.ReportBackHouseActivity;
import com.gdi.activity.MysteryAuditReport.ReportDetailSummaryActivity;
import com.gdi.activity.MysteryAuditReport.ReportExecutiveSummaryActivity;
import com.gdi.activity.MysteryAuditReport.ReportFAQActivity;
import com.gdi.activity.MysteryAuditReport.ReportHighlightActivity;
import com.gdi.activity.MainActivity;
import com.gdi.activity.MysteryAuditReport.ReportOverallBrandActivity;
import com.gdi.activity.MysteryAuditReport.ReportIntegrityActivity;
import com.gdi.model.filter.FilterInfo;
import com.gdi.utils.AppConstant;
import com.gdi.utils.AppPrefs;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportFragment extends Fragment implements View.OnClickListener {

    /*@BindView(R.id.dashboard_layout)
    LinearLayout dashboardLayout;*/
    @BindView(R.id.audit_layout)
    LinearLayout auditLayout;
    @BindView(R.id.overall_brand_layout)
    LinearLayout overallBrandLayout;
    @BindView(R.id.detailed_summary_layout)
    LinearLayout detailedSummaryLayout;
    @BindView(R.id.executive_summary_layout)
    LinearLayout executiveSummaryLayout;
    @BindView(R.id.highlights_layout)
    LinearLayout highlightsLayout;
    @BindView(R.id.audio_image_layout)
    LinearLayout audioImageLayout;
    @BindView(R.id.back_house_layout)
    LinearLayout backHouseLayout;
    @BindView(R.id.integrity_layout)
    LinearLayout integrityLayout;
    @BindView(R.id.faq_layout)
    LinearLayout faqLayout;
    @BindView(R.id.tv_faq_title)
    TextView faqTitle;
    private FilterInfo filterInfo;
    private Context context;
    public static final String TAG = ReportFragment.class.getSimpleName();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reports, container, false);
        ButterKnife.bind(this, view);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        setActionBar();
        auditLayout = view.findViewById(R.id.audit_layout);
        overallBrandLayout = view.findViewById(R.id.overall_brand_layout);
        detailedSummaryLayout = view.findViewById(R.id.detailed_summary_layout);
        executiveSummaryLayout = view.findViewById(R.id.executive_summary_layout);
        highlightsLayout = view.findViewById(R.id.highlights_layout);
        audioImageLayout = view.findViewById(R.id.audio_image_layout);
        backHouseLayout = view.findViewById(R.id.back_house_layout);
        integrityLayout = view.findViewById(R.id.integrity_layout);
        faqLayout = view.findViewById(R.id.faq_layout);
        faqTitle = view.findViewById(R.id.tv_faq_title);
        faqTitle.setText(AppPrefs.getFaqTitle(context));

        //set screen tabs layout
        auditLayout.setLayoutParams(new RelativeLayout.LayoutParams
                (AppConstant.boxSize,AppConstant.boxSize));
        overallBrandLayout.setLayoutParams(new RelativeLayout.LayoutParams
                (AppConstant.boxSize,AppConstant.boxSize));
        detailedSummaryLayout.setLayoutParams(new RelativeLayout.LayoutParams
                (AppConstant.boxSize,AppConstant.boxSize));
        executiveSummaryLayout.setLayoutParams(new RelativeLayout.LayoutParams
                (AppConstant.boxSize,AppConstant.boxSize));
        highlightsLayout.setLayoutParams(new RelativeLayout.LayoutParams
                (AppConstant.boxSize,AppConstant.boxSize));
        audioImageLayout.setLayoutParams(new RelativeLayout.LayoutParams
                (AppConstant.boxSize,AppConstant.boxSize));
        backHouseLayout.setLayoutParams(new RelativeLayout.LayoutParams
                (AppConstant.boxSize,AppConstant.boxSize));
        integrityLayout.setLayoutParams(new RelativeLayout.LayoutParams
                (AppConstant.boxSize,AppConstant.boxSize));
        faqLayout.setLayoutParams(new RelativeLayout.LayoutParams
                (AppConstant.boxSize,AppConstant.boxSize));


        auditLayout.setOnClickListener(this);
        overallBrandLayout.setOnClickListener(this);
        detailedSummaryLayout.setOnClickListener(this);
        executiveSummaryLayout.setOnClickListener(this);
        highlightsLayout.setOnClickListener(this);
        audioImageLayout.setOnClickListener(this);
        backHouseLayout.setOnClickListener(this);
        integrityLayout.setOnClickListener(this);
        faqLayout.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.audit_layout:
                startActivity(new Intent(context, ReportAuditActivity.class));
                break;
            case R.id.overall_brand_layout:
                startActivity(new Intent(context, ReportOverallBrandActivity.class));
                break;
            case R.id.detailed_summary_layout:
                startActivity(new Intent(context, ReportDetailSummaryActivity.class));
                break;
            case R.id.executive_summary_layout:
                startActivity(new Intent(context, ReportExecutiveSummaryActivity.class));
                break;
            case R.id.highlights_layout:
                startActivity(new Intent(context, ReportHighlightActivity.class));
                break;
            case R.id.audio_image_layout:
                startActivity(new Intent(context, ReportAudioImageActivity1.class));
                break;
            case R.id.back_house_layout:
                startActivity(new Intent(context, ReportBackHouseActivity.class));
                break;
            case R.id.integrity_layout:
                startActivity(new Intent(context, ReportIntegrityActivity.class));
                break;
            case R.id.faq_layout:
                startActivity(new Intent(context, ReportFAQActivity.class));
                break;
        }
    }

    private void setActionBar() {
        ((BaseActivity)context).setTitle("Report");
        ActionBar actionBar = ((MainActivity)context).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
            MainActivity.mDraweToggle.setDrawerIndicatorEnabled(false);
        }
    }

}
