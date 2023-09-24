package com.gdi.activity.internalaudit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gdi.activity.BaseActivity;
import com.gdi.activity.GDIApplication;
import com.gdi.activity.internalaudit.adapter.BrandStandardAuditAdapterSingleSection;
import com.gdi.activity.internalaudit.model.audit.BrandStandard.BrandStandardQuestion;
import com.gdi.activity.internalaudit.model.audit.BrandStandard.BrandStandardQuestionsOption;
import com.gdi.activity.internalaudit.model.audit.BrandStandard.BrandStandardRefrence;
import com.gdi.activity.internalaudit.model.audit.BrandStandard.BrandStandardSection;
import com.gdi.api.BSSaveSubmitJsonRequest;
import com.gdi.api.NetworkURL;
import com.gdi.apppreferences.AppPreferences;
import com.gdi.dialog.AppDialog;
import com.gdi.hotel.mystery.audits.R;
import com.gdi.interfaces.INetworkEvent;
import com.gdi.localDB.bsoffline.BsOffLineDB;
import com.gdi.localDB.bsoffline.BsOfflineDBImpl;
import com.gdi.network.NetworkConstant;
import com.gdi.network.NetworkServiceJSON;
import com.gdi.network.NetworkStatus;
import com.gdi.utils.AppConstant;
import com.gdi.utils.AppLogger;
import com.gdi.utils.AppUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BrandStandardAuditActivityPagingnation extends BaseActivity implements View.OnClickListener, BrandStandardAuditAdapterSingleSection.CustomItemClickListener, INetworkEvent {

    @BindView(R.id.rv_bs_question)
    RecyclerView questionListRecyclerView;

    @BindView(R.id.bs_save_btn)
    Button bsSaveBtn;
    @BindView(R.id.bs_add_file_btn)
    Button fileBtn;

    @BindView(R.id.tv_header_title)
    TextView mTitleTV;
    @BindView(R.id.ll_parent_progress)
    private RelativeLayout mProgressRL;

    @BindView(R.id.timertext)
    TextView timerText;
    @BindView(R.id.score_text)
    TextView scoreText;
    @BindView(R.id.iv_header_right)
    ImageView mHeaderDescriptionIV;


    private static final String TAG = BrandStandardAuditActivityPagingnation.class.getSimpleName();
    private List<BrandStandardQuestion> mBrandStandardListCurrent;
    private  Context context;
    private String auditId = "",auditDate = "",sectionGroupId = "",sectionId = "",sectionTitle = "",mLocation = "",mChecklist = "",fileCount = "";
    private String sectionWeightage="";
    public LayoutInflater inflater;
    private ArrayList<BrandStandardSection> brandStandardSectionArrayList = new ArrayList<>();
    private   BrandStandardSection brandStandardSection;
    private BrandStandardAuditAdapterSingleSection sectionTabAdapter;
    private static final int AttachmentRequest = 120;
    private static final int QuestionAttachmentRequest = 130;
    private int  itemClickedPos = 0;
    // public int questionCount = 0;
    //private BrandStandardAuditAdapterSingleSection currentBrandStandardAuditAdapter;
    public static boolean isAnswerCliked=false;
    private boolean isSaveButtonClick=false,isBackButtonClick=false;
    private BsOffLineDB mBsOfflineDB;
    private float totalMarks = 0, marksObtained = 0;
    private long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L,Seconds, Minutes;;
    private Handler handler;
    private int mGalleryDisable=1;

    @Override
    protected void onResume() {
        super.onResume();
        AppUtils.hideKeyboard(this);
        //this is the first commit
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_standard_audit_pagignation);
        ButterKnife.bind(BrandStandardAuditActivityPagingnation.this);
        inflater = getLayoutInflater();
        context = this;
        initView();
        initVar();
    }


    protected void initView() {
        AppLogger.e(TAG, ":::: ON CreateCall BRAND STANDARD");

        mTitleTV = findViewById(R.id.tv_header_title);
        mProgressRL=findViewById(R.id.ll_parent_progress);
        questionListRecyclerView = findViewById(R.id.rv_bs_question);
        findViewById(R.id.iv_header_left).setOnClickListener(this);
        fileBtn = findViewById(R.id.bs_add_file_btn);
        bsSaveBtn = findViewById(R.id.bs_save_btn);
        mTitleTV.setSelected(true);  // for morque
        mHeaderDescriptionIV = findViewById(R.id.iv_header_right);
        timerText = findViewById(R.id.timertext);
        scoreText = findViewById(R.id.score_text);

        bsSaveBtn.setOnClickListener(this);
        fileBtn.setOnClickListener(this);


        mHeaderDescriptionIV.setVisibility(View.VISIBLE);
        mHeaderDescriptionIV.setImageResource(R.drawable.ic_info);
        mHeaderDescriptionIV.setOnClickListener(this);
        mTitleTV.setSelected(true);  // for morque


    }

    protected void initVar() {
        mBsOfflineDB= BsOfflineDBImpl.getInstance(this);
        Intent intent= getIntent();
        auditId =intent.getStringExtra("auditId");
        auditDate = intent.getStringExtra("auditDate");
        mLocation = intent.getStringExtra(AppConstant.LOCATION_NAME);
        mChecklist = intent.getStringExtra(AppConstant.AUDIT_CHECKLIST);
        mGalleryDisable = intent.getIntExtra(AppConstant.GALLERY_DISABLE,0);


        AppPreferences.INSTANCE.initAppPreferences(this);
        handler = new Handler();
        brandStandardSectionArrayList = new ArrayList<>();
        String json = mBsOfflineDB.getBrandSectionJSON("bs");
        Type type = new TypeToken<ArrayList<BrandStandardSection>>() {}.getType();
        brandStandardSectionArrayList =  new Gson().fromJson(json, type);

        brandStandardSection = brandStandardSectionArrayList.get(0);
        mBrandStandardListCurrent=new ArrayList<>();

        loadData();
    }


    private void loadData()
    {
        sectionTitle = brandStandardSection.getSection_title();
        mTitleTV.setText(""+AppUtils.capitalizeHeading(sectionTitle.toLowerCase()));
        // questionCount = 0;
        sectionGroupId = "" + brandStandardSection.getSection_group_id();
        sectionId = "" + brandStandardSection.getSection_id();
        fileCount = "" + brandStandardSection.getAudit_section_file_cnt();
        sectionWeightage=brandStandardSection.getSection_weightage();

        fileBtn.setText("+"+getString(R.string.text_photo)+"     "+fileCount);
        setLocalJSON(brandStandardSection);

        removeHandlerCallBck();
        countNA_Answers();
        StartTime = SystemClock.uptimeMillis();
        handler.postDelayed(runnable, 1000);
    }

    private void removeHandlerCallBck() {
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }

    @Override
    public void onItemClick(int questionNo, BrandStandardAuditAdapterSingleSection brandStandardAuditAdapter, int bsQuestionId, String attachtype, int position) {
        itemClickedPos = questionNo;

         mBrandStandardListCurrent=sectionTabAdapter.getArrayList();
        Intent addAttachment = new Intent(context, AddAttachmentActivity.class);
        addAttachment.putExtra("auditId", auditId);
        addAttachment.putExtra("sectionGroupId", sectionGroupId);
        addAttachment.putExtra("sectionId", sectionId);
        addAttachment.putExtra("questionId", "" + bsQuestionId);
        addAttachment.putExtra("attachType", attachtype);
        addAttachment.putExtra(AppConstant.GALLERY_DISABLE, mGalleryDisable);
        startActivityForResult(addAttachment, QuestionAttachmentRequest);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            AppUtils.deleteCache(this);   // for clearing cache

            if (requestCode == AttachmentRequest && resultCode == Activity.RESULT_OK) {
                try {
                    String attachmentCount = data.getStringExtra("attachmentCount");
                    Button button = this.fileBtn;
                    button.setText("+" + getString(R.string.text_photo) + "     " + attachmentCount);
                } catch (Exception e) {
                    e.printStackTrace();
                    AppLogger.e("AttachmentException", e.getMessage());
                    AppUtils.showHeaderDescription(this.context, e.getMessage());
                }
            } else if (requestCode == QuestionAttachmentRequest && resultCode == Activity.RESULT_OK) {
                isAnswerCliked = true;
                String attachmentCount2 = data.getStringExtra("attachmentCount");
                List<Uri> tempList = new ArrayList<>();
                if (!(this.mBrandStandardListCurrent == null || this.mBrandStandardListCurrent.get(this.itemClickedPos).getmImageList() == null || this.mBrandStandardListCurrent.get(this.itemClickedPos).getmImageList().size() <= 0)) {
                    tempList.addAll(this.mBrandStandardListCurrent.get(this.itemClickedPos).getmImageList());
                }
                tempList.addAll(((GDIApplication) getApplicationContext()).getmAttachImageList());
                this.mBrandStandardListCurrent.get(this.itemClickedPos).setmImageList(tempList);
                this.sectionTabAdapter.setattachmentCount(Integer.parseInt(attachmentCount2), this.itemClickedPos);
            } else if (requestCode == 1021 && resultCode == Activity.RESULT_OK) {
                isAnswerCliked = true;
                this.sectionTabAdapter.setActionCreatedFlag(this.itemClickedPos);
            }
        } catch (Exception e) {
            e.printStackTrace();
            AppLogger.e("AttachmentException", e.getMessage());
            AppUtils.showHeaderDescription(context, e.getMessage());
        }

    }
    public void setQuestionList(ArrayList<BrandStandardQuestion> questionArrayList) {
        sectionTabAdapter = new BrandStandardAuditAdapterSingleSection(context, questionArrayList, BrandStandardAuditActivityPagingnation.this);
        questionListRecyclerView.setAdapter(sectionTabAdapter);
    }
    public void sendToQuestionBasedActivity(ArrayList<BrandStandardQuestion> brandStandardQuestions)
    {
        Intent actionPlan = new Intent(this.context, BrandStandardOptionsBasedQuestionActivity.class);
        actionPlan.putExtra(AppConstant.AUDIT_ID,auditId);
        actionPlan.putExtra(AppConstant.SECTION_GROUPID,sectionGroupId);
        actionPlan.putExtra(AppConstant.SECTION_ID,sectionId);
        actionPlan.putExtra(AppConstant.GALLERY_DISABLE,mGalleryDisable);
        ((GDIApplication)context.getApplicationContext()).setmSubQuestionForOptions(brandStandardQuestions);
        startActivityForResult(actionPlan, AppConstant.SUBQUESTION_REQUESTCODE);
    }
    public void saveBrandStandardQuestion()
    {
        if (NetworkStatus.isNetworkConnected(this))
        {
            if(isSaveButtonClick)
                mProgressRL.setVisibility(View.VISIBLE);
            JSONObject object = BSSaveSubmitJsonRequest.createInputNew(auditId,sectionId,sectionGroupId, auditDate, "1", getQuestionsArray());
            NetworkServiceJSON networkService = new NetworkServiceJSON(NetworkURL.BRANDSTANDARD_SECTION_SAVE, NetworkConstant.METHOD_POST, this, this);
            networkService.call(object);
        } else
        {
            AppUtils.toast(this, getString(R.string.internet_error));
        }
    }
    private JSONArray  getQuestionsArray() {
        JSONArray jsonArray = new JSONArray();
        ArrayList<BrandStandardQuestion> brandStandardQuestions = sectionTabAdapter.getArrayList();

        for (int i = 0; i < brandStandardQuestions.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("question_id", brandStandardQuestions.get(i).getQuestion_id());
                jsonObject.put("audit_answer_na", brandStandardQuestions.get(i).getAudit_answer_na());
                jsonObject.put("audit_comment", brandStandardQuestions.get(i).getAudit_comment());
                jsonObject.put("audit_option_id", new JSONArray(brandStandardQuestions.get(i).getAudit_option_id()));
                jsonObject.put("options", AppUtils.getOptionQuestionArray(brandStandardQuestions.get(i).getOptions(),brandStandardQuestions.get(i).getAudit_option_id()));
                jsonObject.put("audit_answer", brandStandardQuestions.get(i).getAudit_answer());
                jsonArray.put(jsonObject);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return jsonArray;
    }

    private void setLocalJSON(BrandStandardSection brandStandardSection) {
        try{
            setQuestionList(brandStandardSection.getQuestions());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private boolean validateCommentOfQuestion() {
        boolean validate = true;
        int count = 0;
        ArrayList<BrandStandardQuestion> brandStandardQuestions = sectionTabAdapter.getArrayList();
        int mMediaCount=0,mCommentCount=0,mActionPlanRequred=0;

        for (int i = 0; i < brandStandardQuestions.size(); i++) {
            BrandStandardQuestion question = brandStandardQuestions.get(i);
            count += 1;
            mMediaCount=question.getMedia_count();
            mCommentCount=question.getHas_comment();
            mActionPlanRequred=0;
            if (question.getAudit_option_id()!=null && question.getAudit_option_id().size()>0)
            {
                for (int k = 0; k < question.getOptions().size(); k++) {
                    BrandStandardQuestionsOption option = question.getOptions().get(k);
                    if (question.getAudit_option_id() != null && question.getAudit_option_id().contains(new Integer(option.getOption_id()))) {
                        if (question.getQuestion_type().equalsIgnoreCase("checkbox"))
                        {
                            if (mActionPlanRequred==0)
                                mActionPlanRequred=option.getAction_plan_required();
                            if (mMediaCount<option.getMedia_count())
                                mMediaCount = option.getMedia_count();
                            if (mCommentCount<option.getCommentCount())
                                mCommentCount = option.getCommentCount();
                        }
                        else {
                            mMediaCount = option.getMedia_count();
                            mCommentCount = option.getCommentCount();
                            mActionPlanRequred=option.getAction_plan_required();
                            break;
                        }

                    }
                }
            }

            if (((question.getAudit_option_id()!=null && question.getAudit_option_id().size()>0) || !TextUtils.isEmpty(question.getAudit_answer()))) {

                if(mActionPlanRequred>0 && question.getAction_plan()==null)
                {
                   // String message = "Please Create the Action Plan for question no. " + count;
                    String message = getString(R.string.text_create_actionplanfor_question)+" " + count;
                    AppDialog.messageDialogWithYesNo(BrandStandardAuditActivityPagingnation.this, message);
                    return false;
                }
                if(mMediaCount>0 && question.getAudit_question_file_cnt() < mMediaCount)
                {
                  //  String message = "Please submit the required " + mMediaCount + " image(s) for question no. " + count+ " in section";
                    String message=getString(R.string.text_submit_requredmedia_count).replace("MMM",""+mMediaCount).replace("CCC",""+count);

                    AppDialog.messageDialogWithYesNo(BrandStandardAuditActivityPagingnation.this, message);
                    return false;
                }
                if (mCommentCount>0 &&  question.getAudit_comment().length() < mCommentCount)
                {
                  //  String message = "Please enter the  minimum required " + mCommentCount + " characters comment for question no. " + count;
                    String message=getString(R.string.text_enter_requredcomment_count).replace("XXX",""+mCommentCount).replace("CCC",""+count);
                    AppDialog.messageDialogWithYesNo(BrandStandardAuditActivityPagingnation.this, message);
                   return  false;
                }

            }
        }
        return validate;

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeHandlerCallBck();
    }



    @Override
    public void onBackPressed() {
        if(isAnswerCliked)
        {
            isBackButtonClick=true; // This is for saving last answer data and hold th page
            isSaveButtonClick=true; // This is only for showing progressBar
            saveSectionOrPagewiseData();
        }
        else
            finish();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_showhow:
                BrandStandardRefrence bsRefrence =(BrandStandardRefrence) view.getTag();
                if (bsRefrence!=null)
                {
                    if (bsRefrence.getFile_type().contains("image"))
                        ShowHowImageActivity.start(this,bsRefrence.getFile_url(),"");
                    else  if (bsRefrence.getFile_type().contains("audio"))
                        AudioPlayerActivity.start(this, bsRefrence.getFile_url());
                    else  if (bsRefrence.getFile_type().contains("video"))
                        ExoVideoPlayer.start(this, bsRefrence.getFile_url(),"");
                    else
                        ShowHowWebViewActivity.start(this,bsRefrence.getFile_url(),"");
                }
                break;
            case R.id.bs_save_btn:
                AppUtils.hideKeyboard(context, view);
                isSaveButtonClick=true;
                saveSectionOrPagewiseData();
                break;
            case R.id.ll_actioncreate:
              /*  BrandStandardQuestion bsQuestion = (BrandStandardQuestion) view.getTag();
                if (bsQuestion!=null && bsQuestion.isCan_create_action_plan()) {
                    this.itemClickedPos = bsQuestion.getmClickPosition();
                    Intent actionPlan = new Intent(this.context, ActionCreateActivity.class);
                    actionPlan.putExtra("auditid", this.auditId);
                    actionPlan.putExtra(AppConstant.SECTION_GROUPID, this.sectionGroupId);
                    actionPlan.putExtra(AppConstant.SECTION_ID, this.sectionId);
                    actionPlan.putExtra(AppConstant.QUESTION_ID, "" + bsQuestion.getQuestion_id());
                    actionPlan.putExtra(AppConstant.FROMWHERE, "Audit");
                    startActivityForResult(actionPlan, 1021);
                }
                else
                    AppUtils.toast(this, getString(R.string.text_actionplan_has_been_created_forthis_question));
*/
                break;
            case R.id.bs_add_file_btn:
                Intent addAttachment = new Intent(context, AddAttachmentActivity.class);
                addAttachment.putExtra("auditId", auditId);
                addAttachment.putExtra("sectionGroupId", sectionGroupId);
                addAttachment.putExtra("sectionId", sectionId);
                addAttachment.putExtra("questionId", "");
                addAttachment.putExtra("attachType", "bsSection");
                startActivityForResult(addAttachment, AttachmentRequest);
                break;
            case R.id.iv_header_left:
                onBackPressed();
                break;
            case R.id.iv_header_right:
                AppDialog.brandstandardTitleMessageDialog(this, sectionTitle,mLocation,mChecklist);
                break;

        }
    }


    private boolean saveSectionOrPagewiseData()
    {
        if (AppUtils.isStringEmpty(auditDate))
            auditDate=AppUtils.getAuditDateCurrent();
        if (validateCommentOfQuestion())
            saveBrandStandardQuestion();
        else
            return false;

        return  true;
    }


    @Override
    public void onNetworkCallInitiated(String service) { }
    @Override
    public void onNetworkCallCompleted(String type, String service, String responseStr) {
        isAnswerCliked=false; // because question is saved
        AuditSubSectionsActivity.isDataSaved=true;
        AppLogger.e(TAG, "BSResponse: " + responseStr);
        try {
            JSONObject response = new JSONObject(responseStr);
            if (!response.getBoolean(AppConstant.RES_KEY_ERROR))
            {
                AppUtils.toast((BaseActivity) context, response.getString(AppConstant.RES_KEY_MESSAGE));
                if (isBackButtonClick)
                {
                    isBackButtonClick=false; // This is only for showing progressBar
                    finish();
                }
            } else if (response.getBoolean(AppConstant.RES_KEY_ERROR)) {
                AppUtils.toast((BaseActivity) context, response.getString(AppConstant.RES_KEY_MESSAGE));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mProgressRL.setVisibility(View.GONE);
    }

    @Override
    public void onNetworkCallError(String service, String errorMessage) {
        mProgressRL.setVisibility(View.GONE);
        Log.e("onNetworkCallError","===>"+errorMessage);
        AppUtils.toast(this, this.getString(R.string.oops));
    }

    public void countNA_Answers() {
        totalMarks = 0;
        marksObtained = 0;
        for (int i = 0; i < brandStandardSection.getQuestions().size(); i++) {
            BrandStandardQuestion brandStandardQuestion = brandStandardSection.getQuestions().get(i);
            if (!(brandStandardQuestion.getAudit_answer_na() == 1)) {
                if (brandStandardQuestion.getQuestion_type().equals("radio")) {
                    totalMarks = totalMarks + brandStandardQuestion.getOptions().get(0).getOption_mark();
                } else if(brandStandardQuestion.getQuestion_type().equals("target") && !TextUtils.isEmpty(brandStandardQuestion.getMax_mark()))
                {
                    totalMarks = totalMarks + Float.parseFloat(brandStandardQuestion.getMax_mark());
                }
                else {
                    for (int k = 0; k < brandStandardQuestion.getOptions().size(); k++) {
                        totalMarks = totalMarks + brandStandardQuestion.getOptions().get(k).getOption_mark();
                    }
                }
                if (brandStandardQuestion.getQuestion_type().equalsIgnoreCase("target") && !TextUtils.isEmpty(brandStandardQuestion.getAudit_answer()))
                {
                    float targetScore=Float.parseFloat(brandStandardQuestion.getAudit_answer());
                    float maxMarks=Float.parseFloat(brandStandardQuestion.getMax_mark());
                    if (targetScore>maxMarks)
                        targetScore=maxMarks;

                    marksObtained=marksObtained+targetScore;
                }
                if (brandStandardQuestion.getAudit_option_id().size() > 0) {
                    for (int l = 0; l < brandStandardQuestion.getAudit_option_id().size(); l++) {
                        for (int m = 0; m < brandStandardQuestion.getOptions().size(); m++) {
                            if (brandStandardQuestion.getAudit_option_id().get(l) == brandStandardQuestion.getOptions().get(m).getOption_id()) {
                                marksObtained = marksObtained + brandStandardQuestion.getOptions().get(m).getOption_mark();
                                break;
                            }
                        }
                    }
                }
            } else {

            }
        }

        scoreText.setText(getString(R.string.text_score)+":" + (int) (((float) marksObtained / (float) totalMarks) * 100) + "% (" + marksObtained + "/" + totalMarks + ")");

    }
    //-----------------------Audit Times-------------------------------------
    public Runnable runnable = new Runnable() {

        public void run() {
            MillisecondTime = SystemClock.uptimeMillis() - StartTime;
            UpdateTime = TimeBuff + MillisecondTime;
            Seconds = (int) (UpdateTime / 1000);
            Minutes = Seconds / 60;
            Seconds = Seconds % 60;
            timerText.setText("" + String.format("%02d", Minutes) + ":" + String.format("%02d", Seconds));
            handler.postDelayed(this, 1000);
        }

    };
    public void saveSingleBrandStandardQuestionEveryClick(BrandStandardQuestion bsQuestion)
    {
        if (NetworkStatus.isNetworkConnected(this))
        {
            try {
                itemClickedPos=bsQuestion.getmClickPosition();
                if (bsQuestion.getQuestion_type().equalsIgnoreCase(AppConstant.QUESTION_TEXTAREA) || bsQuestion.getQuestion_type().equalsIgnoreCase(AppConstant.QUESTION_TEXT) || bsQuestion.getQuestion_type().equalsIgnoreCase(AppConstant.QUESTION_NUMBER) || bsQuestion.getQuestion_type().equalsIgnoreCase(AppConstant.QUESTION_MEASUREMENT) || bsQuestion.getQuestion_type().equalsIgnoreCase(AppConstant.QUESTION_TARGET) || bsQuestion.getQuestion_type().equalsIgnoreCase(AppConstant.QUESTION_TEMPRATURE) )
                    this.sectionTabAdapter.updatehParticularPosition(itemClickedPos);


                JSONObject object = new JSONObject();
                object.put("audit_id", auditId);
                object.put("question_id", bsQuestion.getQuestion_id());
                object.put("audit_answer", bsQuestion.getAudit_answer());
                object.put("audit_option_id", new JSONArray(bsQuestion.getAudit_option_id()));
                object.put("audit_comment", bsQuestion.getAudit_comment());
                Log.e("JSON QUESTION==> ",""+object.toString());
                NetworkServiceJSON networkService = new NetworkServiceJSON(NetworkURL.BRANDSTANDARD_QUESTIONWISE_ANSWER, NetworkConstant.METHOD_POST, this, this);
                networkService.call(object);
            }
            catch (Exception e) {e.printStackTrace();}
        } else
        {
            AppUtils.toast(this, getString(R.string.internet_error));
        }
    }


}
