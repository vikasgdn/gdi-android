package com.gdi.activity;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.model.reportdetailedsummary.SectionsInfo;
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
    @BindView(R.id.tv_header_decription_btn)
    TextView headerDescriptionBtn;
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
        toolbar = findViewById(R.id.toolbar);
        sectionsInfo = getIntent().getParcelableExtra("sectionInfo");
        sectionName = sectionsInfo.getSection_name();
        setActionBar();
        staffNameText = findViewById(R.id.staffNameText);
        dateText = findViewById(R.id.dateText);
        timeText = findViewById(R.id.timeText);
        summaryText = findViewById(R.id.summary_text);
        keyPositiveText = findViewById(R.id.key_positives_text);
        keyNegativeText = findViewById(R.id.key_negatives_text);
        recommendationText = findViewById(R.id.recommendation_text);
        tvSummary = findViewById(R.id.tv_summary);
        tvKeyPositives = findViewById(R.id.tv_key_positives);
        tvKeyNegatives = findViewById(R.id.tv_key_negatives);
        tvRecommendation = findViewById(R.id.tv_recommendation);
        attachmentCount = findViewById(R.id.tv_attachment_count);
        attachmentLayout = findViewById(R.id.attachment_layout);
        headerDescriptionBtn = findViewById(R.id.tv_header_decription_btn);
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

        headerDescriptionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtils.showHeaderDescription(context, sectionName);
            }
        });


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
