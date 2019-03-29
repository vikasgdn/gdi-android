package com.gdi.activity.Audit;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.activity.BaseActivity;
import com.gdi.utils.AppUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AuditInfoActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_audit_info_instruction)
    TextView tvInstruction;
    @BindView(R.id.tv_audit_info_reviewer_rating)
    TextView tvReviewerRating;
    @BindView(R.id.tv_audit_info_reviewer_feedback)
    TextView tvReviewerFeedback;
    @BindView(R.id.tv_instruction_text)
    TextView tvInstructionText;
    @BindView(R.id.tv_reviewer_rating_text)
    TextView tvReviewerRatingText;
    @BindView(R.id.tv_reviewer_feedback_text)
    TextView tvReviewerFeedbackText;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit_info);
        context = this;
        ButterKnife.bind(AuditInfoActivity.this);
        initView();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar();
        tvInstruction = (TextView) findViewById(R.id.tv_audit_info_instruction);
        tvReviewerRating = (TextView) findViewById(R.id.tv_audit_info_reviewer_rating);
        tvReviewerFeedback = (TextView) findViewById(R.id.tv_audit_info_reviewer_feedback);
        tvInstructionText = (TextView) findViewById(R.id.tv_instruction_text);
        tvReviewerRatingText = (TextView) findViewById(R.id.tv_reviewer_rating_text);
        tvReviewerFeedbackText = (TextView) findViewById(R.id.tv_reviewer_feedback_text);
        String instruction = "";
        String reviewerRating = "";
        String reviewerFeedback = "";
        instruction = getIntent().getStringExtra("instruction");
        reviewerRating = getIntent().getStringExtra("reviewerRating");
        reviewerFeedback = getIntent().getStringExtra("reviewerFeedBack");
        if (AppUtils.isStringEmpty(instruction)) {
            tvInstruction.setVisibility(View.GONE);
            tvInstructionText.setVisibility(View.GONE);
        }else {
            tvInstruction.setVisibility(View.VISIBLE);
            tvInstructionText.setVisibility(View.VISIBLE);
            tvInstruction.setText(instruction);
        }
        if (AppUtils.isStringEmpty(reviewerRating) || reviewerRating.equals("0")) {
            tvReviewerRating.setVisibility(View.GONE);
            tvReviewerRatingText.setVisibility(View.GONE);
        }else {
            tvReviewerRating.setVisibility(View.VISIBLE);
            tvReviewerRatingText.setVisibility(View.VISIBLE);
            tvReviewerRating.setText(reviewerRating);
        }
        if (AppUtils.isStringEmpty(reviewerFeedback)) {
            tvReviewerFeedback.setVisibility(View.GONE);
            tvReviewerFeedbackText.setVisibility(View.GONE);
        }else {
            tvReviewerFeedback.setVisibility(View.VISIBLE);
            tvReviewerFeedbackText.setVisibility(View.VISIBLE);
            tvReviewerFeedback.setText(reviewerFeedback);
        }
    }

    private void setActionBar() {
        initToolbar(toolbar);
        setTitle("Audit Info");
        enableBack(true);
        enableBackPressed();
    }
}
