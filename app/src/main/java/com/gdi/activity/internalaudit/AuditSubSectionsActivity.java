package com.gdi.activity.internalaudit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.gdi.activity.BaseActivity;
import com.gdi.activity.GDIApplication;
import com.gdi.activity.MainActivity;
import com.gdi.activity.internalaudit.adapter.SubSectionAdapter;
import com.gdi.activity.internalaudit.model.audit.BrandStandard.BrandStandardInfo;
import com.gdi.activity.internalaudit.model.audit.BrandStandard.BrandStandardRootObject;
import com.gdi.activity.internalaudit.model.audit.BrandStandard.BrandStandardSection;
import com.gdi.api.BSSaveSubmitJsonRequest;
import com.gdi.api.NetworkURL;
import com.gdi.apppreferences.AppPreferences;
import com.gdi.hotel.mystery.audits.R;
import com.gdi.interfaces.INetworkEvent;
import com.gdi.localDB.bsoffline.BsOffLineDB;
import com.gdi.localDB.bsoffline.BsOfflineDBImpl;
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

public class AuditSubSectionsActivity extends BaseActivity implements SubSectionAdapter.CustomItemClickListener, INetworkEvent {

    @BindView(R.id.tv_header_title)
    TextView mHeaderTitleTV;
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
    @BindView(R.id.ll_parent_progress)
    RelativeLayout mSpinKitView;
    @BindView(R.id.tv_auditname)
    TextView mAuditNameTV;
    public static  boolean isDataSaved=true;
    private int isGalleryDisable=1;
    private String status = "",auditId = "",auditDate = "",mAuditName="",messsage="",mLocationName="",mCheckListName="";
    private int mAuditTimerSecond=0 ;
    private  Context context;
    //private  JSONArray answerArray ;
    private ArrayList<BrandStandardSection> brandStandardSections;
    private static final String TAG = AuditSubSectionsActivity.class.getSimpleName();
    private BsOffLineDB mBsOfflineDB;
    private TextView mOverallScoreTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_sections);
        ButterKnife.bind(this);
        context = this;
        isDataSaved=true;
        AppPreferences.INSTANCE.initAppPreferences(this);
        mBsOfflineDB= BsOfflineDBImpl.getInstance(this);
        initView();
        initVar();
        AppUtils.deleteCache(this);   // for clearing cache
    }
    private void initView() {
        mHeaderTitleTV = findViewById(R.id.tv_header_title);
        subSectionTabList = findViewById(R.id.rv_sub_section_tab);
        continueBtn = findViewById(R.id.continue_btn);
        statusProgressBar= findViewById(R.id.simpleProgressBar);
        completeProgressBar= findViewById(R.id.completeProgressBar);
        statusText= findViewById(R.id.tv_status_text);
        mSpinKitView=findViewById(R.id.ll_parent_progress);
        mAuditNameTV=findViewById(R.id.tv_auditname);
        mOverallScoreTV=findViewById(R.id.tv_overall_score);

        findViewById(R.id.iv_header_left).setOnClickListener(this);
        continueBtn.setOnClickListener(this);

    }

    private void initVar() {
        mHeaderTitleTV.setText(R.string.text_inspection);
        auditId = getIntent().getStringExtra(AppConstant.AUDIT_ID);
        mAuditName = getIntent().getStringExtra(AppConstant.AUDIT_NAME);
        mAuditNameTV.setText(mAuditName+" (ID:"+auditId+")");
    }
    @Override
    protected void onResume() {
        super.onResume();
      //  if (isDataSaved)
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
                if (brandStandardSections !=null)
                {
                    JSONArray finalAnswerJA = AppUtils.validateSubmitQuestionFinalSubmission(this, brandStandardSections);
                    if (finalAnswerJA != null)
                    {
                          submitFinalBrandStandardQuestion(finalAnswerJA);
                       // goForSignature();
                    }
                }
                break;
        }
    }

    private void getAuditQuestionsFromServer()
    {
        if (NetworkStatus.isNetworkConnected(this)) {
            mSpinKitView.setVisibility(View.VISIBLE);
            String questionListUrl = NetworkURL.BRANDSTANDARD + "?" + "audit_id=" + auditId ;
            System.out.println("==> mAuditTYpeID "+questionListUrl);
            NetworkService networkService = new NetworkService(questionListUrl, NetworkConstant.METHOD_GET, this,this);
            networkService.call( new HashMap<String, String>());
        } else
        {
            AppUtils.toast(this, this.getString(R.string.internet_error));
            //  processQuestionListResponse(mBsOfflineDB.getOffileQuestionJSONToDB(auditId));
        }
    }
    private void setQuestionList(BrandStandardInfo info){
        //newly added for overaall score
        if (!TextUtils.isEmpty(info.getScore()))
            mOverallScoreTV.setText(getResources().getString(R.string.text_overall_score)+": "+info.getScore()+"%");
        else
            mOverallScoreTV.setVisibility(View.GONE);

        brandStandardSections = new ArrayList<>();
        brandStandardSections.addAll(info.getSections());
        SubSectionAdapter subSectionAdapter = new SubSectionAdapter(this, brandStandardSections, AuditSubSectionsActivity.this);
        subSectionTabList.setAdapter(subSectionAdapter);
    }
    @Override
    public void onBackPressed() {
        if(!TextUtils.isEmpty(status) && status.equalsIgnoreCase("1"))
            finish();
        else
        {
            Intent intent = new Intent(this, InternalAuditDashboardActivity.class);
            intent.putExtra(AppConstant.FROMWHERE, AppConstant.AUDIT);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);
            this.finish();
        }
    }

    @Override
    public void onItemClick(ArrayList<BrandStandardSection> brandStandardSections, int fileCount, int pos) {
        try {
            Intent startAudit=null;
            startAudit = new Intent(context, BrandStandardAuditActivity.class);
            if (brandStandardSections!=null && brandStandardSections.size()==1 && (brandStandardSections.get(0).getSub_sections()==null || brandStandardSections.get(0).getSub_sections().size()==0) )
                startAudit = new Intent(context, BrandStandardAuditActivityPagingnation.class);

            //  startAudit.putParcelableArrayListExtra("sectionObject", brandStandardSections);
            ((GDIApplication)getApplication()).setmBrandStandardSectionList(brandStandardSections);
            String jsonString = new Gson().toJson(brandStandardSections);
            mBsOfflineDB.deleteBrandSectionJSONToDB("bs");
            mBsOfflineDB.saveBrandSectionJSONToDB("bs", jsonString);

            startAudit.putExtra("position", pos);
            startAudit.putExtra("auditId", auditId);
            startAudit.putExtra("auditDate", auditDate);
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
            mSpinKitView.setVisibility(View.VISIBLE);
            JSONObject object = BSSaveSubmitJsonRequest.createInput(auditId, auditDate, "0", answerArray);
            NetworkServiceJSON networkService = new NetworkServiceJSON(NetworkURL.BRANDSTANDARD_FINAL_SAVE_NEW, NetworkConstant.METHOD_POST, this, this);
            networkService.call(object);
        } else
        {
            AppUtils.toast(this, this.getString(R.string.internet_error));
        }
    }

    private void goForSignature() {
        Intent  intent=new Intent(this,AuditSubmitSignatureActivity.class);
        intent.putExtra(AppConstant.AUDIT_ID,auditId);
        startActivity(intent);
       // startActivityForResult(intent,5005);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK)
        {
            JSONArray finalAnswerJA = AppUtils.validateSubmitQuestionFinalSubmission(this, brandStandardSections);
            if (finalAnswerJA != null)
            {
                submitFinalBrandStandardQuestion(finalAnswerJA);
            }
        }
    }

    @Override
    public void onNetworkCallInitiated(String service) { }
    @Override
    public void onNetworkCallCompleted(String type, String service, String response)
    {
        if (service.equalsIgnoreCase(NetworkURL.BRANDSTANDARD_FINAL_SAVE_NEW))
        { try {
            JSONObject responseJson = new JSONObject(response);
            if (!responseJson.getBoolean(AppConstant.RES_KEY_ERROR)) {
                status = "" + responseJson.getJSONObject("data").getInt("brand_std_status");
                goForSignature();
            } else if (responseJson.getBoolean(AppConstant.RES_KEY_ERROR)) {
               JSONObject errorOBJ= responseJson.optJSONObject("d");
               String quesionName="";
               if (errorOBJ!=null)
                   quesionName=errorOBJ.optString("question_title");
                AppUtils.toastDisplayForLong((BaseActivity) context, responseJson.getString(AppConstant.RES_KEY_MESSAGE)+" for Question "+quesionName);
            }
        } catch (Exception e) { e.printStackTrace(); }
        }
        else
            processQuestionListResponse(response);

        mSpinKitView.setVisibility(View.GONE);

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
                    isGalleryDisable=brandStandardRootObject.getData().isGalleryDisable();
                    //new added for particular client 0 disable  1 enable
                    setQuestionList(brandStandardRootObject.getData());
                   getPercentage(brandStandardRootObject.getData().getSections());
                }
            } else if (object.getBoolean(AppConstant.RES_KEY_ERROR)) {
                AppUtils.toast((BaseActivity) context, object.getString(AppConstant.RES_KEY_MESSAGE)); }
        } catch (Exception e) {
            e.printStackTrace();
            AppUtils.toast((BaseActivity) context,messsage);
        }
    }

    @Override
    public void onNetworkCallError(String service, String errorMessage) {
        mSpinKitView.setVisibility(View.GONE);
        AppLogger.e(TAG, "AudioImageError: " + errorMessage);
        AppUtils.toast((BaseActivity) context, this.getString(R.string.oops));
    }

    private void getPercentage(ArrayList<BrandStandardSection> brandStandardSection){
        float totalAnsweredCount = 0;
        float totalQuestionCount = 0;

            for (int k = 0; k < brandStandardSection.size(); k++) {
                totalAnsweredCount+=brandStandardSection.get(k).getAnswered_question_count();
                totalQuestionCount+=brandStandardSection.get(k).getQuestion_count();
            }
            setProgressBar(totalAnsweredCount,totalQuestionCount);

    }
    private void setProgressBar(float filledQuestionCount, float totalQuestionCount){
        try {
            Log.e(" QUETION || ANSWER===> ",filledQuestionCount+" || "+totalQuestionCount);

            float percent = (filledQuestionCount / totalQuestionCount) * 100;
            DecimalFormat decimalFormat = new DecimalFormat("0.0");

            statusText.setText("" + decimalFormat.format(percent) + "%"+getString(R.string.text_complete));
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
                statusText.setTextColor(getResources().getColor(R.color.c_blue));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}