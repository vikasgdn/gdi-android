package com.gdi.activity.oditylychange;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.gdi.hotel.mystery.audits.R;
import com.gdi.activity.Audit.AuditSubmitSignatureActivity;
import com.gdi.activity.BaseActivity;
import com.gdi.activity.MainActivity;
import com.gdi.api.ApiEndPoints;
import com.gdi.api.BSSaveSubmitJsonRequest;
import com.gdi.interfaces.INetworkEvent;
import com.gdi.model.audit.BrandStandard.BrandStandardInfo;
import com.gdi.model.audit.BrandStandard.BrandStandardQuestion;
import com.gdi.model.audit.BrandStandard.BrandStandardRootObject;
import com.gdi.model.audit.BrandStandard.BrandStandardSection;
import com.gdi.model.audit.BrandStandard.BrandStandardSubSection;
import com.gdi.network.NetworkConstant;
import com.gdi.network.NetworkService;
import com.gdi.network.NetworkServiceJSON;
import com.gdi.network.NetworkStatus;
import com.gdi.utils.AppConstant;
import com.gdi.utils.AppLogger;
import com.gdi.utils.AppUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AuditSubSectionsActivityODT extends BaseActivityOditly implements SubSectionAdapterODT.CustomItemClickListener, INetworkEvent {

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
    @BindView(R.id.tv_auditname)
    TextView mAuditNameTV;
    public static  boolean isDataSaved=true;
    private int isGalleryDisable=1;
    private String status = "",auditId = "",auditDate = "",mAuditName="",messsage="",mLocationName="",mCheckListName="";
    private int mAuditTimerSecond=0 ;
    private  Context context;
    //private  JSONArray answerArray ;
    private ArrayList<BrandStandardSection> brandStandardSections;
    private static final String TAG = AuditSubSectionsActivityODT.class.getSimpleName();
    private String mEditable="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_sections);
        ButterKnife.bind(this);
        context = this;
        isDataSaved=true;
      //  AppPreferences.INSTANCE.initAppPreferences(this);
        initView();
        initVar();
    }
    @Override
    protected void initView() {
        super.initView();
        subSectionTabList = findViewById(R.id.rv_sub_section_tab);
        continueBtn = findViewById(R.id.continue_btn);
        statusProgressBar= findViewById(R.id.simpleProgressBar);
        completeProgressBar= findViewById(R.id.completeProgressBar);
        statusText= findViewById(R.id.tv_status_text);
        rejectedComment= findViewById(R.id.tv_rejected_comment);
        rejectedCommentLayout= findViewById(R.id.rejected_comment_layout);
        mAuditNameTV=findViewById(R.id.tv_auditname);
        findViewById(R.id.iv_header_left).setOnClickListener(this);
        TextView headerTitleTV=(TextView)findViewById(R.id.tv_header_title);
        headerTitleTV.setText("Audit Option");
        continueBtn.setOnClickListener(this);

    }

    @Override
    protected void initVar() {
        super.initVar();
        auditId = getIntent().getStringExtra(AppConstant.AUDIT_ID);
        mAuditName = getIntent().getStringExtra(AppConstant.AUDIT_NAME);
        mEditable = getIntent().getStringExtra(AppConstant.EDITABLE);
        mAuditNameTV.setText(mAuditName+" (ID:"+auditId+")");
    }
    @Override
    protected void onResume() {
        super.onResume();
            if (isDataSaved)
                getAuditQuestionsFromServer();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId())
        {
            case R.id.iv_header_left:
                 onBackPressed();
                break;
            case R.id.continue_btn:
                JSONArray finalAnswerJA = AppUtils.validateSubmitQuestionFinalSubmission(this,brandStandardSections);
                if (brandStandardSections !=null && finalAnswerJA!=null)
                    submitFinalBrandStandardQuestion(finalAnswerJA);
                break;
        }
    }

    private void getAuditQuestionsFromServer()
    {
        if (NetworkStatus.isNetworkConnected(this)) {
            showAppProgressDialog();
            String questionListUrl = ApiEndPoints.BRANDSTANDARD + "?" + "audit_id=" + auditId ;
            System.out.println("==> mAuditTYpeID "+questionListUrl);
            NetworkService networkService = new NetworkService(questionListUrl, NetworkConstant.METHOD_GET, this,this);
            networkService.call(new HashMap<String, String>());
        } else
        {
            AppUtils.toast(this, this.getString(R.string.internet_error));
          //  processQuestionListResponse(mBsOfflineDB.getOffileQuestionJSONToDB(auditId));
        }
    }
    private void setQuestionList(BrandStandardInfo info){
        brandStandardSections = new ArrayList<>();
        brandStandardSections.addAll(info.getSections());
        SubSectionAdapterODT subSectionAdapter = new SubSectionAdapterODT(this, brandStandardSections, AuditSubSectionsActivityODT.this);
        subSectionTabList.setAdapter(subSectionAdapter);
    }
    @Override
    public void onBackPressed() {
        if(status.equalsIgnoreCase("1"))
            finish();
        else
        {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(AppConstant.FROMWHERE, AppConstant.AUDIT);
            this.startActivity(intent);
            this.finish();
        }
    }

    @Override
    public void onItemClick(ArrayList<BrandStandardSection> brandStandardSections, int fileCount, int pos) {
        try {

            Intent startAudit = new Intent(context, BrandStandardAuditActivityODT.class);
            startAudit.putParcelableArrayListExtra("sectionObject", brandStandardSections);
            // ((OditlyApplication)getApplication()).setmBrandStandardSectionList(brandStandardSections);
            String jsonString = new Gson().toJson(brandStandardSections);
          //  mBsOfflineDB.deleteBrandSectionJSONToDB("bs");
          //  mBsOfflineDB.saveBrandSectionJSONToDB("bs", jsonString);
            startAudit.putExtra("position", pos);
            startAudit.putExtra("auditId", auditId);
            startAudit.putExtra("auditDate", auditDate);
            startAudit.putExtra(AppConstant.EDITABLE, mEditable);
            startAudit.putExtra(AppConstant.AUDIT_TIMER, mAuditTimerSecond);
            startAudit.putExtra(AppConstant.GALLERY_DISABLE, isGalleryDisable);
            startAudit.putExtra(AppConstant.LOCATION_NAME, mLocationName);
            startAudit.putExtra(AppConstant.AUDIT_CHECKLIST, mCheckListName);
            startAudit.putExtra("status", status);
            startAudit.putExtra("fileCount", "" + fileCount);
            startActivity(startAudit);
        }
        catch (Exception e){e.printStackTrace();}
    }


    public void submitFinalBrandStandardQuestion(JSONArray answerArray)
    {
        if (NetworkStatus.isNetworkConnected(this))
        {
            showAppProgressDialog();
            JSONObject object = BSSaveSubmitJsonRequest.createInput(auditId, auditDate, "0", answerArray);
            NetworkServiceJSON networkService = new NetworkServiceJSON(ApiEndPoints.BRANDSTANDARD, NetworkConstant.METHOD_POST, this, this);
            networkService.call(object);
        } else
        {
            AppUtils.toast(this, this.getString(R.string.internet_error));
        }
    }

    private void goForSignature() {
        Intent  intent=new Intent(this, AuditSubmitSignatureActivity.class);
        intent.putExtra(AppConstant.AUDIT_ID,auditId);
        startActivity(intent);
        finish();
    }

    @Override
    public void onNetworkCallInitiated(String service) { }
    @Override
    public void onNetworkCallCompleted(String type, String service, String response)
    {
        if (service.equalsIgnoreCase(ApiEndPoints.BRANDSTANDARD))
        { try {
                JSONObject responseJson = new JSONObject(response);
                if (!responseJson.getBoolean(AppConstant.RES_KEY_ERROR)) {
                    status = "" + responseJson.getJSONObject("data").getInt("brand_std_status");
                    goForSignature();
                } else if (responseJson.getBoolean(AppConstant.RES_KEY_ERROR)) {
                    AppUtils.toast(AuditSubSectionsActivityODT.this, responseJson.getString(AppConstant.RES_KEY_MESSAGE));
                }
            } catch (Exception e) { e.printStackTrace(); }
        }
        else
          processQuestionListResponse(response);

        hideProgressDialog();
    }

    private void processQuestionListResponse(String response) {
        isDataSaved=false;
        AppLogger.e(TAG, "BrandStandardResponse: " + response);
        try {
            JSONObject object = new JSONObject(response);
            messsage =  object.getString(AppConstant.RES_KEY_MESSAGE);
            if (!object.getBoolean(AppConstant.RES_KEY_ERROR)) {
                BrandStandardRootObject brandStandardRootObject = new GsonBuilder().create().fromJson(object.toString(), BrandStandardRootObject.class);
                if (brandStandardRootObject.getData() != null && brandStandardRootObject.getData().toString().length() > 0) {
                    auditDate = brandStandardRootObject.getData().getAudit_date();
                    mLocationName = brandStandardRootObject.getData().getLocation_title();
                    mCheckListName = brandStandardRootObject.getData().getQuestionnaire_title();
                    status = "" + brandStandardRootObject.getData().getBrand_std_status();
                    mAuditTimerSecond=brandStandardRootObject.getData().getAuditTimer();  //new added
                    isGalleryDisable=brandStandardRootObject.getData().isGalleryDisable();  //new added for particular client
                    setRejectedComment(brandStandardRootObject.getData());
                    setQuestionList(brandStandardRootObject.getData());
                    float count = 0;
                    float totalCount = 0;
                    int[] result = statusQuestionCount(brandStandardRootObject.getData().getSections());
                    count = (float) result[0];
                    totalCount = (float) result[1];
                    setProgressBar(count, totalCount);
                }
            } else if (object.getBoolean(AppConstant.RES_KEY_ERROR)) {
                AppUtils.toast(AuditSubSectionsActivityODT.this, object.getString(AppConstant.RES_KEY_MESSAGE)); }
        } catch (Exception e) {
            e.printStackTrace();
            AppUtils.toast(AuditSubSectionsActivityODT.this,messsage);
        }
    }

    @Override
    public void onNetworkCallError(String service, String errorMessage) {
        hideProgressDialog();
        AppLogger.e(TAG, "AudioImageError: " + errorMessage);
        AppUtils.toast(AuditSubSectionsActivityODT.this, this.getString(R.string.oops));
    }


    private int[] statusQuestionCount(ArrayList<BrandStandardSection> brandStandardSection){
        int totalCount = 0;
        int count = 0;
        for (int i = 0 ; i < brandStandardSection.size() ; i++ ) {
            ArrayList<BrandStandardQuestion> brandStandardQuestion = brandStandardSection.get(i).getQuestions();
            for (int j = 0; j < brandStandardQuestion.size(); j++)
            {
                if (brandStandardQuestion.get(j).getAudit_option_id().size() != 0 || brandStandardQuestion.get(j).getAudit_answer_na() == 1 || !AppUtils.isStringEmpty(brandStandardQuestion.get(j).getAudit_answer())) {
                    count += 1;
                }
                totalCount += 1;
            }
            ArrayList<BrandStandardSubSection> brandStandardSubSections = brandStandardSection.get(i).getSub_sections();
            for (int k = 0; k < brandStandardSubSections.size(); k++) {
                ArrayList<BrandStandardQuestion> brandStandardSubQuestion = brandStandardSubSections.get(k).getQuestions();
                for (int j = 0; j < brandStandardSubQuestion.size(); j++) {
                    if (brandStandardSubQuestion.get(j).getAudit_option_id().size() != 0 || brandStandardSubQuestion.get(j).getAudit_answer_na() == 1 || !AppUtils.isStringEmpty(brandStandardSubQuestion.get(j).getAudit_answer())) {
                        count += 1;
                    }
                    totalCount += 1;
                }
            }
        }
        Log.e("count || Total Count ",""+count + "|| "+totalCount);
        return new int[]{count, totalCount};
    }
    private void setProgressBar(float filledQuestionCount, float totalQuestionCount){
        try {

            float percent = (filledQuestionCount / totalQuestionCount) * 100;
            DecimalFormat decimalFormat = new DecimalFormat("0.0");

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
                statusText.setTextColor(getResources().getColor(R.color.colorPrimary));
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

}
