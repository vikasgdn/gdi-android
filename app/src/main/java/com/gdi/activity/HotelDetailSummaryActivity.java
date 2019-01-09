package com.gdi.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.model.detailedsummary.SectionsInfo;
import com.gdi.utils.AppLogger;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HotelDetailSummaryActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.staffNameText)
    TextView staffNameText;
    @BindView(R.id.dateText)
    TextView dateText;
    @BindView(R.id.timeText)
    TextView timeText;
    @BindView(R.id.summary_text)
    TextView summaryText;
    @BindView(R.id.key_positives_text)
    TextView keyPositiveText;
    @BindView(R.id.key_negatives_text)
    TextView keyNegativeText;
    @BindView(R.id.recommendation_text)
    TextView recommendationText;
    private String sectionName;
    private SectionsInfo sectionsInfo = new SectionsInfo();
    Context context;
    private static final String TAG = HotelDetailSummaryActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_summary_activity);
        context = this;
        ButterKnife.bind(HotelDetailSummaryActivity.this);
        initView();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        sectionsInfo = (SectionsInfo) getIntent().getParcelableExtra("sectionInfo");
        sectionName = sectionsInfo.getSection_name();
        setActionBar();
        staffNameText = (TextView) findViewById(R.id.staffNameText);
        dateText = (TextView) findViewById(R.id.dateText);
        timeText = (TextView) findViewById(R.id.timeText);
        summaryText = (TextView) findViewById(R.id.summary_text);
        keyPositiveText = (TextView) findViewById(R.id.key_positives_text);
        keyNegativeText = (TextView) findViewById(R.id.key_negatives_text);
        recommendationText = (TextView) findViewById(R.id.recommendation_text);

        AppLogger.e(TAG, "Section name : " + sectionName );
        summaryText.setText(sectionsInfo.getSummary());
        staffNameText.setText(sectionsInfo.getStaff_name());
        dateText.setText(sectionsInfo.getDate());
        timeText.setText(sectionsInfo.getTime());
        keyPositiveText.setText(sectionsInfo.getKey_positives());
        keyNegativeText.setText(sectionsInfo.getKey_negatives());
        recommendationText.setText(sectionsInfo.getRecommendation());
    }

    private void setActionBar() {
        initToolbar(toolbar);
        setTitle(sectionName);
        enableBack(true);
        enableBackPressed();
    }
}
