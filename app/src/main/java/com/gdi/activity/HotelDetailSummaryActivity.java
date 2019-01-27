package com.gdi.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.adapter.DetailSummaryAdapter4;
import com.gdi.attachmentactivity.DetailedAttachmentActivity;
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
    @BindView(R.id.tv_attachment_count)
    TextView attachmentCount;
    @BindView(R.id.attachment_layout)
    LinearLayout attachmentLayout;
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
        attachmentCount = (TextView) findViewById(R.id.tv_attachment_count);
        attachmentLayout = (LinearLayout) findViewById(R.id.attachment_layout);

        AppLogger.e(TAG, "Section name : " + sectionName );
        summaryText.setText(sectionsInfo.getSummary());
        staffNameText.setText(sectionsInfo.getStaff_name());
        dateText.setText(sectionsInfo.getDate());
        timeText.setText(sectionsInfo.getTime());
        keyPositiveText.setText(sectionsInfo.getKey_positives());
        keyNegativeText.setText(sectionsInfo.getKey_negatives());
        recommendationText.setText(sectionsInfo.getRecommendation());
        /*DetailSummaryAdapter4 detailSummaryAdapter4 = new DetailSummaryAdapter4(context, sectionsInfo.getAttachments());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,2
                , LinearLayoutManager.VERTICAL,false);
        attachments.setLayoutManager(gridLayoutManager);
        attachments.setAdapter(detailSummaryAdapter4);*/
        if (sectionsInfo.getAttachments() != null && sectionsInfo.getAttachments().size()>0) {
            attachmentLayout.setVisibility(View.VISIBLE);
            attachmentCount.setText("(" + sectionsInfo.getAttachments().size() + ")");
            attachmentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DetailedAttachmentActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("data", sectionsInfo.getAttachments());
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }else {
            attachmentLayout.setVisibility(View.GONE);
        }
    }

    private void setActionBar() {
        initToolbar(toolbar);
        setTitle(sectionName);
        enableBack(true);
        enableBackPressed();
    }
}
