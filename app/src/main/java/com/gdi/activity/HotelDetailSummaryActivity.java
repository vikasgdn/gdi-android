package com.gdi.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.adapter.DetailSummaryAdapter4;
import com.gdi.attachmentactivity.DetailedAttachmentActivity;
import com.gdi.model.detailedsummary.SectionsInfo;
import com.gdi.utils.AppLogger;
import com.gdi.utils.AppUtils;

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
    @BindView(R.id.tv_summary)
    TextView tvSummary;
    @BindView(R.id.tv_key_negatives)
    TextView tvKeyNegatives;
    @BindView(R.id.tv_key_positives)
    TextView tvKeyPositives;
    @BindView(R.id.tv_recommendation)
    TextView tvRecommendation;
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
        tvSummary = (TextView) findViewById(R.id.tv_summary);
        tvKeyPositives = (TextView) findViewById(R.id.tv_key_positives);
        tvKeyNegatives = (TextView) findViewById(R.id.tv_key_negatives);
        tvRecommendation = (TextView) findViewById(R.id.tv_recommendation);
        attachmentCount = (TextView) findViewById(R.id.tv_attachment_count);
        attachmentLayout = (LinearLayout) findViewById(R.id.attachment_layout);

        AppLogger.e(TAG, "Section name : " + sectionName );
        String summary_text = Html.fromHtml(sectionsInfo.getSummary()).toString();
        String key_pos_text = Html.fromHtml(sectionsInfo.getKey_positives()).toString();
        String key_neg_text = Html.fromHtml(sectionsInfo.getKey_negatives()).toString();
        String recommendation_text = Html.fromHtml(sectionsInfo.getRecommendation()).toString();

        staffNameText.setText(sectionsInfo.getStaff_name());
        dateText.setText(sectionsInfo.getDate());
        timeText.setText(sectionsInfo.getTime());
        if (AppUtils.isStringEmpty(summary_text)){
            summaryText.setVisibility(View.GONE);
            tvSummary.setVisibility(View.GONE);
        }else {
            tvSummary.setVisibility(View.VISIBLE);
            summaryText.setVisibility(View.VISIBLE);
            summaryText.setText(summary_text);
        }

        if (AppUtils.isStringEmpty(key_pos_text)){
            keyPositiveText.setVisibility(View.GONE);
            tvKeyPositives.setVisibility(View.GONE);
        }else {
            tvSummary.setVisibility(View.VISIBLE);
            keyPositiveText.setVisibility(View.VISIBLE);
            keyPositiveText.setText(key_pos_text);
        }

        if (AppUtils.isStringEmpty(key_neg_text)){
            keyNegativeText.setVisibility(View.GONE);
            tvKeyNegatives.setVisibility(View.GONE);
        }else {
            tvSummary.setVisibility(View.VISIBLE);
            keyNegativeText.setVisibility(View.VISIBLE);
            keyNegativeText.setText(key_neg_text);
        }

        if (AppUtils.isStringEmpty(recommendation_text)){
            recommendationText.setVisibility(View.GONE);
            tvRecommendation.setVisibility(View.GONE);
        }else {
            tvSummary.setVisibility(View.VISIBLE);
            recommendationText.setVisibility(View.VISIBLE);
            recommendationText.setText(recommendation_text);
        }



        /*DetailSummaryAdapter4 detailSummaryAdapter4 = new DetailSummaryAdapter4(context, sectionsInfo.getAttachments());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,2
                , LinearLayoutManager.VERTICAL,false);
        attachments.setLayoutManager(gridLayoutManager);
        attachments.setAdapter(detailSummaryAdapter4);*/
        /*if (sectionsInfo.getAttachments() != null && sectionsInfo.getAttachments().size()>0) {
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
        }*/
    }

    private void setActionBar() {
        initToolbar(toolbar);
        setTitle(sectionName);
        enableBack(true);
        enableBackPressed();
    }
}
