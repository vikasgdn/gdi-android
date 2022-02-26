package com.gdi.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.gdi.R;
import com.gdi.activity.Audit.BrandStandardAuditActivity;
import com.gdi.adapter.SubSectionTabAdapter;
import com.gdi.api.ApiEndPoints;
import com.gdi.api.BSSaveSubmitJsonRequest;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubSectionsActivity extends BaseActivity implements SubSectionTabAdapter.CustomItemClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_sub_section_tab)
    RecyclerView subSectionTabList;
    @BindView(R.id.continue_btn)
    Button continueBtn;
    @BindView(R.id.simpleProgressBar)
    ProgressBar statusProgressBar;
    @BindView(R.id.completeProgressBar)
    ProgressBar completeProgressBar;
    @BindView(R.id.tv_status_text)
    TextView statusText;
    @BindView(R.id.rejected_comment_layout)
    LinearLayout rejectedCommentLayout;
    @BindView(R.id.tv_rejected_comment)
    TextView rejectedComment;
    private String status = "";
    private String auditId = "";
    private String auditDate = "";
    private String editable = "";
    private String bsStatus = "";
    private int totalQuestionCount = 0;
    private int filledQuestionCount = 0;
    Context context;
    private static final int FillQuestionRequest = 101;
    JSONArray answerArray ;
    ArrayList<BrandStandardSection> brandStandardSections;
    private static final String TAG = SubSectionsActivity.class.getSimpleName();

    @Override
    protected void onResume() {
        super.onResume();
        if (AppUtils.isNetworkConnected(context)){
            setBrandStandardQuestion();
        }else {
            finish();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_sections_gdi);
        ButterKnife.bind(this);
        context = this;
        initView();

        // change by Vikas
      /*  if (AppUtils.isNetworkConnected(context)){
            setBrandStandardQuestion();
        }else {
            finish();
        }*/
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent result = new Intent();
        result.putExtra("status", status);
        setResult(RESULT_OK, result);
        finish();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        setActionBar();
        subSectionTabList = findViewById(R.id.rv_sub_section_tab);
        continueBtn = findViewById(R.id.continue_btn);
        statusProgressBar= findViewById(R.id.simpleProgressBar);
        completeProgressBar= findViewById(R.id.completeProgressBar);
        statusText= findViewById(R.id.tv_status_text);
        rejectedComment= findViewById(R.id.tv_rejected_comment);
        rejectedCommentLayout= findViewById(R.id.rejected_comment_layout);
        auditId = getIntent().getStringExtra("auditId");
        editable = getIntent().getStringExtra("editable");

        if (editable.equals("0")){
            continueBtn.setVisibility(View.VISIBLE);
        }else {
            continueBtn.setVisibility(View.GONE);
        }

        setBrandStandardQuestion();

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //added by Vikas need to chnage

                if (validateSubmitQuestion(brandStandardSections))
                {
                    submitBrandStandardQuestion();
                }else {
                   // AppUtils.toast(SubSectionsActivity.this, "Please fill ");
                }
            }
        });
    }

    private void setBrandStandardQuestion(){
        showProgressDialog();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLogger.e(TAG, "BrandStandardResponse: " + response);
                try {
                    JSONObject object = new JSONObject(response);

                    if (!object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        BrandStandardRootObject brandStandardRootObject = new GsonBuilder().create().fromJson(object.toString(), BrandStandardRootObject.class);
                        if (brandStandardRootObject.getData() != null && brandStandardRootObject.getData().toString().length() > 0) {
                            auditDate = brandStandardRootObject.getData().getAudit_date();
                            status = "" + brandStandardRootObject.getData().getBrand_std_status();
                            setRejectedComment(brandStandardRootObject.getData());
                            setQuestionList(brandStandardRootObject.getData());
                            float count = 0;
                            float totalCount = 0;
                            int[] result = statusQuestionCount(brandStandardRootObject.getData().getSections());
                            count = (float) result[0];
                            totalCount = (float) result[1];
                            setProgressBar(count, totalCount);
                            //brandStandardAuditAdapter.notifyDataSetChanged();
                        }
                    } else if (object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        AppUtils.toast((BaseActivity) context,
                                object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hideProgressDialog();
            }

        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                AppLogger.e(TAG, "AudioImageError: " + error.getMessage());
                AppUtils.toast((BaseActivity) context, "Server temporary unavailable, Please try again");

            }
        };

        String integrityUrl = ApiEndPoints.BRANDSTANDARD + "?" + "audit_id=" + auditId ;
        GetReportRequest getReportRequest = new GetReportRequest(AppPrefs.getAccessToken(context),
                integrityUrl, stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(getReportRequest);
    }

    private void setQuestionList(BrandStandardInfo info){
        brandStandardSections = new ArrayList<>();
        brandStandardSections.addAll(info.getSections());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,3
                , LinearLayoutManager.VERTICAL,false);
        SubSectionTabAdapter subSectionTabAdapter = new SubSectionTabAdapter(context, brandStandardSections, editable, SubSectionsActivity.this);
        subSectionTabList.setLayoutManager(gridLayoutManager);
        subSectionTabList.setAdapter(subSectionTabAdapter);
    }

    private void setActionBar() {
        initToolbar(toolbar);
        setTitle("Audit Option");
        enableBack(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setProgressBar(float filledQuestionCount, float totalQuestionCount){
        try {
            float divide = filledQuestionCount / totalQuestionCount;
            float dpercent = divide * 100;
            float percent = (filledQuestionCount / totalQuestionCount) * 100;
            DecimalFormat decimalFormat = new DecimalFormat("0.0");

            AppLogger.e(TAG, "divide" + divide);
            AppLogger.e(TAG, "filledCount" + filledQuestionCount);
            AppLogger.e(TAG, "totalCount" + totalQuestionCount);
            AppLogger.e(TAG, "StatusPercent" + percent);
            AppLogger.e(TAG, "StatusDPercent" + dpercent);
            statusText.setText("" + decimalFormat.format(percent) + "% Completed");
            int intValue = (int)percent;
            AppLogger.e(TAG, "value" + intValue);

            if (intValue == 100){
                completeProgressBar.setVisibility(View.VISIBLE);
                statusProgressBar.setVisibility(View.GONE);
                completeProgressBar.setProgress(intValue);
                statusText.setTextColor(getResources().getColor(R.color.scoreGreen));
            }else {
                completeProgressBar.setVisibility(View.GONE);
                statusProgressBar.setVisibility(View.VISIBLE);
                statusProgressBar.setProgress(intValue);
                statusProgressBar.setMax(100);
                statusText.setTextColor(getResources().getColor(R.color.appThemeColour));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setRejectedComment(BrandStandardInfo brandStandardInfo){
        if (!AppUtils.isStringEmpty(brandStandardInfo.getReviewer_brand_std_comment())){
            rejectedCommentLayout.setVisibility(View.VISIBLE);
            rejectedComment.setText(brandStandardInfo.getReviewer_brand_std_comment());
        }else {
            rejectedCommentLayout.setVisibility(View.GONE);
        }
    }

    private int[] statusQuestionCount(ArrayList<BrandStandardSection> brandStandardSection){
        int totalCount = 0;
        int count = 0;

        //totalCount = brandStandardSection.getQuestions().size();
        for (int i = 0 ; i < brandStandardSection.size() ; i++ ) {
            ArrayList<BrandStandardQuestion> brandStandardQuestion = brandStandardSection.get(i).getQuestions();
            for (int j = 0; j < brandStandardQuestion.size(); j++) {

                if (brandStandardQuestion.get(j).getAudit_option_id().size() != 0
                        || brandStandardQuestion.get(j).getAudit_answer_na() == 1
                        || !AppUtils.isStringEmpty(brandStandardQuestion.get(j).getAudit_answer())) {
                    count += 1;
                }
                totalCount += 1;
            }

            ArrayList<BrandStandardSubSection> brandStandardSubSections = brandStandardSection.get(i).getSub_sections();

            for (int k = 0; k < brandStandardSubSections.size(); k++) {
                ArrayList<BrandStandardQuestion> brandStandardSubQuestion = brandStandardSubSections.get(k).getQuestions();
                for (int j = 0; j < brandStandardSubQuestion.size(); j++) {
                    if (brandStandardSubQuestion.get(j).getAudit_option_id().size() != 0
                            || brandStandardSubQuestion.get(j).getAudit_answer_na() == 1
                            || !AppUtils.isStringEmpty(brandStandardSubQuestion.get(j).getAudit_comment())) {
                        count += 1;
                    }
                    totalCount += 1;
                }
            }

        }
        filledQuestionCount = count;
        totalQuestionCount = totalCount;
        return new int[]{count, totalCount};

    }

    @Override
    public void onItemClick(ArrayList<BrandStandardSection> brandStandardSections, int fileCount, int pos) {
        Toast.makeText(this," Internal Audit",Toast.LENGTH_SHORT).show();

        Intent startAudit = new Intent(context, BrandStandardAuditActivity.class);
        startAudit.putParcelableArrayListExtra("sectionObject", brandStandardSections);
        startAudit.putExtra("position", pos);
        startAudit.putExtra("editable", editable);
        startAudit.putExtra("auditId", auditId);
        startAudit.putExtra("auditDate", auditDate);
        startAudit.putExtra("status", status);
        startAudit.putExtra("fileCount", ""+fileCount);
        startActivityForResult(startAudit, FillQuestionRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FillQuestionRequest && resultCode == Activity.RESULT_OK){
            //answerSavedDialog();
            //AppUtils.toast(SubSectionsActivity.this, "Answer Saved");
        }
    }


    private boolean validateSubmitQuestion(ArrayList<BrandStandardSection> brandStandardSection){
        boolean validate = true;
        int count = 0;
        ArrayList<BrandStandardQuestion> brandStandardQuestionsSubmissions = new ArrayList<>();


        //totalCount = brandStandardSection.getQuestions().size();
        for (int i = 0 ; i < brandStandardSection.size() ; i++ ) {
            ArrayList<BrandStandardQuestion> brandStandardQuestion = brandStandardSection.get(i).getQuestions();
            count = 0;
            for (int j = 0; j < brandStandardQuestion.size(); j++) {
                count += 1;
                brandStandardQuestionsSubmissions.add(brandStandardQuestion.get(j));
                if (brandStandardQuestion.get(j).getQuestion_type().equals("textarea")||
                        brandStandardQuestion.get(j).getQuestion_type().equals("text")){
                    if (AppUtils.isStringEmpty(brandStandardQuestion.get(j).getAudit_answer())
                            && brandStandardQuestion.get(j).getAudit_answer_na() == 0) {
                        AppUtils.toast(SubSectionsActivity.this, "You have not answered " +
                                "question " + count + " in " + brandStandardSection.get(i).getSection_group_title()
                                + " of section " + brandStandardSection.get(i).getSection_title());
                        return false;
                    }
                }else {
                    if (brandStandardQuestion.get(j).getAudit_option_id().size() == 0
                            && brandStandardQuestion.get(j).getAudit_answer_na() == 0) {
                        AppUtils.toast(SubSectionsActivity.this, "You have not answered " +
                                "question " + count + " in " + brandStandardSection.get(i).getSection_group_title()
                                + " of section " + brandStandardSection.get(i).getSection_title());
                        return false;
                    }
                }
            }

            ArrayList<BrandStandardSubSection> brandStandardSubSections = brandStandardSection.get(i).getSub_sections();
try {
    for (int k = 0; k < brandStandardSubSections.size(); k++) {
        ArrayList<BrandStandardQuestion> brandStandardSubQuestion = brandStandardSubSections.get(k).getQuestions();
        for (int j = 0; j < brandStandardSubQuestion.size(); j++) {
            brandStandardQuestionsSubmissions.add(brandStandardSubQuestion.get(j));
            count += 1;
            if (brandStandardSubQuestion.get(j).getQuestion_type().equals("textarea") || brandStandardQuestion.get(j).getQuestion_type().equals("text")) {
                if (AppUtils.isStringEmpty(brandStandardSubQuestion.get(j).getAudit_answer()) && brandStandardSubQuestion.get(j).getAudit_answer_na() == 0) {
                    AppUtils.toast(SubSectionsActivity.this, "You have not answered " + "question " + count + " in " + brandStandardSection.get(i).getSection_group_title() + " of section " + brandStandardSection.get(i).getSection_title());
                    return false;
                }
            } else {
                if (brandStandardSubQuestion.get(j).getAudit_option_id().size() == 0
                        && brandStandardSubQuestion.get(j).getAudit_answer_na() == 0) {
                    AppUtils.toast(SubSectionsActivity.this, "You have not answered " +
                            "question " + count + " in " + brandStandardSection.get(i).getSection_group_title()
                            + " of section " + brandStandardSection.get(i).getSection_title());
                    return false;
                }
            }
        }
    }
}
catch (Exception e){e.printStackTrace();}

        }

        answerArray = getQuestionsArray (brandStandardQuestionsSubmissions);
/*
        //totalCount = brandStandardSection.getQuestions().size();
        for (int a = 0 ; a < brandStandardSection.size() ; a++ ) {

            for (int i = 0; i < brandStandardSection.get(a).getQuestions().size(); i++) {
                if (brandStandardSection.get(a).getQuestions().get(i).getAudit_option_id().size() == 0
                        || brandStandardSection.get(a).getQuestions().get(i).getAudit_answer_na() == 0) {
                    String text = brandStandardSection.get(a).getSection_title();
                    count += 1;
                    validate = false;
                    break;
                }else {
                    brandStandardQuestions.add(brandStandardSection.get(a).getQuestions().get(i));
                }
            }

            for (int i = 0; i < brandStandardSection.get(a).getSub_sections().size(); i++) {
                for (int j = 0; j < brandStandardSection.get(a).getSub_sections().get(i).getQuestions().size(); j++) {
                    if (brandStandardSection.get(a).getQuestions().get(i).getAudit_option_id().size() != 0
                            || brandStandardSection.get(a).getQuestions().get(i).getAudit_answer_na() == 1) {
                        String text = brandStandardSection.get(a).getSection_title();
                        count += 1;
                        validate = false;
                        break;
                    }else {
                        brandStandardQuestions.add(brandStandardSection.get(a).getSub_sections().get(i).getQuestions().get(j));
                    }
                }
            }

        }*/

        return validate;

    }

    private JSONArray getQuestionsArray (ArrayList<BrandStandardQuestion> brandStandardQuestions){
        JSONArray jsonArray = new JSONArray();
        for (int i = 0 ; i < brandStandardQuestions.size() ; i++) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("question_id", brandStandardQuestions.get(i).getQuestion_id());
                jsonObject.put("audit_answer_na", brandStandardQuestions.get(i).getAudit_answer_na());
                jsonObject.put("audit_comment", brandStandardQuestions.get(i).getAudit_comment());
                jsonObject.put("audit_option_id", getOptionIdArray(brandStandardQuestions.get(i).getAudit_option_id()));
                jsonObject.put("audit_answer", brandStandardQuestions.get(i).getAudit_answer());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return jsonArray;
    }

    private String getOptionIdArray (ArrayList<Integer> arrayList){
        JSONArray jsonArray = new JSONArray();
        for (int i =0;i<arrayList.size();i++){
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("", arrayList.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return ""+jsonArray;
    }

    private void submitBrandStandardQuestion(){
        showProgressDialog();
        JSONObject object = BSSaveSubmitJsonRequest.createInput(auditId, auditDate, "0", answerArray);
        AppLogger.e(TAG, "" + object);
        Response.Listener stringListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                AppLogger.e(TAG, "BSResponse: " + response);
                try {
                    if (!response.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        //AppUtils.toast((BaseActivity) context, response.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                        Toast.makeText(context, "Answer Submitted", Toast.LENGTH_SHORT).show();
                        status = "" + response.getJSONObject("data").getInt("brand_std_status");
                        Intent result = new Intent();
                        result.putExtra("status", status);
                        setResult(RESULT_OK, result);
                        finish();
                    } else if (response.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        AppUtils.toast((BaseActivity) context,
                                response.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hideProgressDialog();
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONObject obj = new JSONObject(res);
                        String message = obj.getString("message");
                        AppLogger.e("Error: ", "" + obj);
                        AppUtils.toast((BaseActivity) context,
                                message);
                    } catch (UnsupportedEncodingException e1) {
                        //Couldn't properly decode data to string
                            /*if (context != null) {
                                AppUtils.toast((BaseActivity) context,
                                        getString(R.string.alert_msg_invalid_response));
                            }*/
                        e1.printStackTrace();
                    } catch (Exception e2) {
                            /*if (context != null) {
                                AppUtils.toast((BaseActivity) context,
                                        getString(R.string.alert_msg_invalid_response));
                            }*/
                        //returned data is not JSONObject?
                        e2.printStackTrace();
                    }

                }
            }
        };

        BSSaveSubmitJsonRequest bsSaveSubmitJsonRequest = new BSSaveSubmitJsonRequest(AppPrefs.getAccessToken(context), ApiEndPoints.BRANDSTANDARD, object, stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(bsSaveSubmitJsonRequest);

    }

}
