package com.gdi.activity.Audit;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gdi.R;
import com.gdi.activity.BaseActivity;
import com.gdi.adapter.BrandStandardAuditAdapter;
import com.gdi.adapter.SubSectionTabAdapter;
import com.gdi.api.ApiEndPoints;
import com.gdi.api.GetReportRequest;
import com.gdi.api.VolleyNetworkRequest;
import com.gdi.model.audit.BrandStandard.BrandStandardInfo;
import com.gdi.model.audit.BrandStandard.BrandStandardQuestion;
import com.gdi.model.audit.BrandStandard.BrandStandardRootObject;
import com.gdi.model.audit.BrandStandard.BrandStandardSection;
import com.gdi.model.audit.BrandStandard.BrandStandardSubSection;
import com.gdi.utils.ApiResponseKeys;
import com.gdi.utils.AppLogger;
import com.gdi.utils.AppPrefs;
import com.gdi.utils.AppUtils;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BrandStandardAuditActivity extends BaseActivity {

    @BindView(R.id.rv_bs_question)
    RecyclerView questionListRecyclerView;
    @BindView(R.id.ll_bs_sub_section_question)
    LinearLayout subSectionQuestionLayout;
    @BindView(R.id.bs_save_btn)
    Button bsSaveBtn;
    @BindView(R.id.bs_submit_btn)
    Button bsSubmitBtn;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    Context context;
    private String auditId = "";
    public LayoutInflater inflater;
    private BrandStandardAuditAdapter brandStandardAuditAdapter;
    ArrayList<BrandStandardQuestion> questionArrayList;
    ArrayList<BrandStandardSubSection> subSectionArrayList;
    private static final String TAG = BrandStandardAuditActivity.class.getSimpleName();

    @Override
    protected void onResume() {
        super.onResume();
        AppUtils.hideKeyboard(BrandStandardAuditActivity.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_standard_audit);
        inflater = getLayoutInflater();
        context = this;
        ButterKnife.bind(BrandStandardAuditActivity.this);
        initView();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar();
        questionListRecyclerView = (RecyclerView) findViewById(R.id.rv_bs_question);
        subSectionQuestionLayout = (LinearLayout) findViewById(R.id.ll_bs_sub_section_question);
        bsSaveBtn = (Button) findViewById(R.id.bs_save_btn);
        bsSubmitBtn = (Button) findViewById(R.id.bs_submit_btn);
        auditId = getIntent().getStringExtra("auditId");
        questionArrayList = new ArrayList<>();
        subSectionArrayList = new ArrayList<>();
        questionArrayList = getIntent().getParcelableArrayListExtra("questions");
        subSectionArrayList = getIntent().getParcelableArrayListExtra("subSectionQuestions");
        setQuestionList();
        setSubSectionQuestionList();

    }

    private void setQuestionList(){
        BrandStandardAuditAdapter subSectionTabAdapter = new BrandStandardAuditAdapter(context, questionArrayList);
        questionListRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        questionListRecyclerView.setAdapter(subSectionTabAdapter);
    }

    private void setSubSectionQuestionList(){
        subSectionQuestionLayout.removeAllViews();
        if (subSectionArrayList != null || subSectionArrayList.size() != 0) {
            for (int i = 0; i < subSectionArrayList.size(); i++) {
                BrandStandardSubSection brandStandardSubSection = subSectionArrayList.get(i);
                View view = inflater.inflate(R.layout.brand_standard_audit_layout2, null);
                TextView subSectionTitle = view.findViewById(R.id.tv_bs_sub_section_title);
                RecyclerView subSectionQuestionList = view.findViewById(R.id.rv_bs_sub_section_question);

                subSectionTitle.setText(brandStandardSubSection.getSub_section_title());

                BrandStandardAuditAdapter subSectionTabAdapter = new BrandStandardAuditAdapter(context, brandStandardSubSection.getQuestions());
                subSectionQuestionList.setLayoutManager(new LinearLayoutManager(context));
                subSectionQuestionList.setAdapter(subSectionTabAdapter);

                subSectionQuestionLayout.addView(view);
            }
        }
    }

    private void setActionBar() {
        initToolbar(toolbar);
        setTitle("Brand Standard");
        enableBack(true);
        enableBackPressed();
    }
}
