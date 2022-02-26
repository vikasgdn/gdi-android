package com.gdi.activity.oditylychange;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gdi.R;
import com.gdi.activity.Audit.AddAttachmentActivity;
import com.gdi.activity.ExoVideoPlayer;
import com.gdi.activity.GDIApplication;
import com.gdi.api.ApiEndPoints;
import com.gdi.api.BSSaveSubmitJsonRequest;
import com.gdi.dialog.AppDialog;
import com.gdi.interfaces.INetworkEvent;
import com.gdi.model.audit.BrandStandard.BrandStandardQuestion;
import com.gdi.model.audit.BrandStandard.BrandStandardQuestionsOption;
import com.gdi.model.audit.BrandStandard.BrandStandardRefrence;
import com.gdi.model.audit.BrandStandard.BrandStandardSection;
import com.gdi.model.audit.BrandStandard.BrandStandardSubSection;
import com.gdi.network.NetworkConstant;
import com.gdi.network.NetworkServiceJSON;
import com.gdi.network.NetworkStatus;
import com.gdi.utils.AppConstant;
import com.gdi.utils.AppLogger;
import com.gdi.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BrandStandardAuditActivityODT extends BaseActivityOditly implements View.OnClickListener, BrandStandardAuditAdapterODT.CustomItemClickListener, INetworkEvent {

    @BindView(R.id.rv_bs_question)
    RecyclerView questionListRecyclerView;
    @BindView(R.id.ll_bs_sub_section_question)
    LinearLayout subSectionQuestionLayout;
    @BindView(R.id.bs_save_btn)
    Button bsSaveBtn;
    @BindView(R.id.bs_addmedia)
    Button mediaAddBtn;
    @BindView(R.id.next_btn)
    Button nextBtn;
    @BindView(R.id.prev_btn)
    Button prevBtn;
    @BindView(R.id.iv_header_right)
    ImageView mHeaderDescriptionIV;
    @BindView(R.id.timertext)
    TextView timerText;
    @BindView(R.id.score_text)
    TextView scoreText;
    @BindView(R.id.toolbar)
    Toolbar mToolabr;
    @BindView(R.id.ns_nestedscroolview)
    private NestedScrollView mNestedScrollView;

    private static final String TAG = BrandStandardAuditActivityODT.class.getSimpleName();
    private List<BrandStandardQuestion> mBrandStandardListCurrent;
    private  Context context;
    private String auditId = "",auditDate = "",sectionGroupId = "",sectionId = "",sectionTitle = "",mLocation = "",mChecklist = "",fileCount = "";
    public LayoutInflater inflater;
    private float totalMarks = 0, marksObtained = 0;
    private ArrayList<BrandStandardSection> brandStandardSectionArrayList = new ArrayList<>();
    private   BrandStandardSection brandStandardSection;
    private BrandStandardAuditAdapterODT sectionTabAdapter;
    private static final int AttachmentRequest = 120;
    private static final int QuestionAttachmentRequest = 130;
    private ArrayList<BrandStandardAuditAdapterODT> bsSubSectionAuditAdaptersList;
    private int  itemClickedPos = 0,currentSectionPosition = 0,Seconds, Minutes;
    public int questionCount = 0;
    private BrandStandardAuditAdapterODT currentBrandStandardAuditAdapter;
    private long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L;
    private  Handler handler;
    public static boolean isAnswerCliked=false;
    private boolean isSaveButtonClick=false,isBackButtonClick=false;
    private int isGalleryDisasble=1;
    private String mEditable="0";
    private TextView mTitleTV;

    @Override
    protected void onResume() {
        super.onResume();
        AppUtils.hideKeyboard(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_standard_audit_odt);
        ButterKnife.bind(BrandStandardAuditActivityODT.this);
        inflater = getLayoutInflater();
        context = this;
        initView();
        initVar();
    }


    @Override
    protected void initView() {
        super.initView();
        AppLogger.e(TAG, ":::: ON CreateCall BRAND STANDARD");

        mNestedScrollView=findViewById(R.id.ns_nestedscroolview);
        mTitleTV=(TextView)findViewById(R.id.tv_header_title);
        questionListRecyclerView = findViewById(R.id.rv_bs_question);
        subSectionQuestionLayout = findViewById(R.id.ll_bs_sub_section_question);
        mediaAddBtn = findViewById(R.id.bs_addmedia);
        bsSaveBtn = findViewById(R.id.bs_save_btn);
        nextBtn = findViewById(R.id.next_btn);
        prevBtn = findViewById(R.id.prev_btn);
        mHeaderDescriptionIV = findViewById(R.id.iv_header_right);
        timerText = findViewById(R.id.timertext);
        scoreText = findViewById(R.id.score_text);

        bsSaveBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        prevBtn.setOnClickListener(this);
        mediaAddBtn.setOnClickListener(this);
        findViewById(R.id.iv_header_left).setOnClickListener(this);
        mHeaderDescriptionIV.setVisibility(View.VISIBLE);
        mHeaderDescriptionIV.setImageResource(R.drawable.ic_baseline_info_24);
        mHeaderDescriptionIV.setOnClickListener(this);
        mTitleTV.setSelected(true);  // for morque

    }

    @Override
    protected void initVar() {
        super.initVar();


        Intent intent= getIntent();
        auditId =intent.getStringExtra("auditId");
        auditDate = intent.getStringExtra("auditDate");
        mEditable = intent.getStringExtra(AppConstant.EDITABLE);
        mLocation = intent.getStringExtra(AppConstant.LOCATION_NAME);
        mChecklist = intent.getStringExtra(AppConstant.AUDIT_CHECKLIST);
        TimeBuff  = intent.getIntExtra(AppConstant.AUDIT_TIMER,0)*1000;
        isGalleryDisasble= intent.getIntExtra(AppConstant.GALLERY_DISABLE,1);



        handler = new Handler();
        brandStandardSectionArrayList = new ArrayList<>();
        brandStandardSectionArrayList = getIntent().getParcelableArrayListExtra("sectionObject");
        AppLogger.e("brandStandardSectionArrayList", " size "+brandStandardSectionArrayList);
        //  brandStandardSectionArrayList = ((OditlyApplication)getApplication()).getmBrandStandardSectionList();
        //  brandStandardSectionArrayList = AuditSubSectionsActivity.brandStandardSectionsStatic;

     /*   String json = mBsOfflineDB.getBrandSectionJSON("bs");
        Type type = new TypeToken<ArrayList<BrandStandardSection>>() {}.getType();
        brandStandardSectionArrayList =  new Gson().fromJson(json, type);*/

        AppLogger.e("brandStandardSectionArrayList", " size "+brandStandardSectionArrayList);
        currentSectionPosition = getIntent().getIntExtra("position", 0);
        AppLogger.e("Position", " position  "+currentSectionPosition);

        brandStandardSection = brandStandardSectionArrayList.get(currentSectionPosition);
        mBrandStandardListCurrent=new ArrayList<>();

        setPrevNextButton();
        loadData();
    }

    private void loadData()
    {
        mNestedScrollView.scrollTo(0,0); // new added
        sectionTitle = brandStandardSection.getSection_title();
        bsSubSectionAuditAdaptersList = new ArrayList<>();
        mTitleTV.setText(""+AppUtils.capitalizeHeading(sectionTitle.toLowerCase()));
        questionCount = 0;
        sectionGroupId = "" + brandStandardSection.getSection_group_id();
        sectionId = "" + brandStandardSection.getSection_id();
        fileCount = "" + brandStandardSection.getAudit_section_file_cnt();
        setLocalJSON(brandStandardSection);
        mediaAddBtn.setText("+"+getString(R.string.text_photo)+"     "+fileCount);
        removeHandlerCallBck();
        countNA_Answers();
        StartTime = SystemClock.uptimeMillis();
        handler.postDelayed(runnable, 1000);
    }


    @Override
    public void onItemClick(int questionNo, BrandStandardAuditAdapterODT brandStandardAuditAdapter, int bsQuestionId, String attachtype, int position) {
        itemClickedPos = position;
        questionCount = questionNo;
        currentBrandStandardAuditAdapter = brandStandardAuditAdapter;
        mBrandStandardListCurrent=currentBrandStandardAuditAdapter.getArrayList();
        Intent addAttachment = new Intent(context, AddAttachmentActivity.class);
        addAttachment.putExtra("auditId", auditId);
        addAttachment.putExtra("sectionGroupId", sectionGroupId);
        addAttachment.putExtra("sectionId", sectionId);
        addAttachment.putExtra("questionId", "" + bsQuestionId);
        addAttachment.putExtra("attachType", attachtype);
        addAttachment.putExtra(AppConstant.EDITABLE, mEditable);
        addAttachment.putExtra(AppConstant.GALLERY_DISABLE, isGalleryDisasble);
        startActivityForResult(addAttachment, QuestionAttachmentRequest);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == AttachmentRequest && resultCode == Activity.RESULT_OK)
            {
                String attachmentCount = data.getStringExtra("attachmentCount");
                mediaAddBtn.setText("+"+getString(R.string.text_photo)+"     "+attachmentCount);
            } else if (requestCode == QuestionAttachmentRequest && resultCode == Activity.RESULT_OK)
            {
                BrandStandardAuditActivityODT.isAnswerCliked=true;
                String attachmentCount = data.getStringExtra("attachmentCount");
                List<Uri> tempList=new ArrayList<>();
                if(mBrandStandardListCurrent!=null && mBrandStandardListCurrent.get(itemClickedPos).getmImageList()!=null && mBrandStandardListCurrent.get(itemClickedPos).getmImageList().size()>0)
                    tempList.addAll(mBrandStandardListCurrent.get(itemClickedPos).getmImageList());
                tempList.addAll(((GDIApplication)getApplicationContext()).getmAttachImageList());
                mBrandStandardListCurrent.get(itemClickedPos).setmImageList(tempList);

                if(currentBrandStandardAuditAdapter!=null && attachmentCount!=null)
                    currentBrandStandardAuditAdapter.setattachmentCount(Integer.parseInt(attachmentCount), itemClickedPos);
            }
        } catch (Exception e) {
            e.printStackTrace();
            AppLogger.e("AttachmentException", e.getMessage());
            AppUtils.showHeaderDescription(context, e.getMessage());
        }

    }
    public void setQuestionList(ArrayList<BrandStandardQuestion> questionArrayList) {
        sectionTabAdapter = new BrandStandardAuditAdapterODT(context, questionArrayList, BrandStandardAuditActivityODT.this);
        questionListRecyclerView.setAdapter(sectionTabAdapter);
        questionListRecyclerView.setHasFixedSize(true);
        questionListRecyclerView.setItemViewCacheSize(20);
        questionListRecyclerView.setDrawingCacheEnabled(true);
        questionListRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
    }

    public void setSubSectionQuestionList(ArrayList<BrandStandardSubSection> subSectionArrayList) {
        subSectionQuestionLayout.removeAllViews();
        if (subSectionArrayList != null || subSectionArrayList.size() != 0)
        {
            for (int l = 0; l < subSectionArrayList.size(); l++) {
                BrandStandardSubSection brandStandardSubSection = subSectionArrayList.get(l);
                View view = inflater.inflate(R.layout.brand_standard_audit_subsection, null);
                TextView subSectionTitle = view.findViewById(R.id.tv_bs_sub_section_title);
                RecyclerView subSectionQuestionListRV = view.findViewById(R.id.rv_bs_sub_section_question);
                subSectionTitle.setText(brandStandardSubSection.getSub_section_title());

                BrandStandardAuditAdapterODT subSectionTabAdapter = new BrandStandardAuditAdapterODT(context, brandStandardSubSection.getQuestions(), BrandStandardAuditActivityODT.this);
                subSectionQuestionListRV.setLayoutManager(new LinearLayoutManager(context));
                subSectionQuestionListRV.setAdapter(subSectionTabAdapter);

                bsSubSectionAuditAdaptersList.add(subSectionTabAdapter);
                subSectionQuestionLayout.addView(view);
            }
        }
    }

    private void mergJSONArray(JSONArray arrayJson)
    {
        try {
            JSONArray sourceArray = new JSONArray(arrayJson);
            JSONArray destinationArray = new JSONArray(arrayJson);
            for (int i = 0; i < sourceArray.length(); i++) {
                destinationArray.put(sourceArray.getJSONObject(i));
            }

            String s3 = destinationArray.toString();
        }
        catch (Exception e){e.printStackTrace();}
    }
    public void saveBrandStandardQuestion()
    {
        if (NetworkStatus.isNetworkConnected(this))
        {
            Log.e(";; answer array  :: ",""+getQuestionsArray());
            if(isSaveButtonClick)
                showAppProgressDialog();
            JSONObject object = BSSaveSubmitJsonRequest.createInput(auditId, auditDate, "1", getQuestionsArray());
            NetworkServiceJSON networkService = new NetworkServiceJSON(ApiEndPoints.BRANDSTANDARD, NetworkConstant.METHOD_POST, this, this);
            networkService.call(object);
        } else
        {
            AppUtils.toast(this, getString(R.string.internet_error));
        }
    }
    private JSONArray  getQuestionsArray() {
        JSONArray jsonArray = new JSONArray();
        ArrayList<BrandStandardQuestion> brandStandardQuestions = sectionTabAdapter.getArrayList();
        ArrayList<BrandStandardQuestion> brandStandardsubsectionQuestions = new ArrayList<>();
        for (int i = 0; i < bsSubSectionAuditAdaptersList.size(); i++) {
            brandStandardsubsectionQuestions.addAll(bsSubSectionAuditAdaptersList.get(i).getArrayList());
        }
        for (int i = 0; i < brandStandardQuestions.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("question_id", brandStandardQuestions.get(i).getQuestion_id());
                jsonObject.put("audit_answer_na", brandStandardQuestions.get(i).getAudit_answer_na());
                jsonObject.put("audit_comment", brandStandardQuestions.get(i).getAudit_comment());
                jsonObject.put("audit_option_id", new JSONArray(brandStandardQuestions.get(i).getAudit_option_id()));
                jsonObject.put("audit_answer", brandStandardQuestions.get(i).getAudit_answer());
                jsonArray.put(jsonObject);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < brandStandardsubsectionQuestions.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("question_id", brandStandardsubsectionQuestions.get(i).getQuestion_id());
                jsonObject.put("audit_answer_na", brandStandardsubsectionQuestions.get(i).getAudit_answer_na());
                jsonObject.put("audit_comment", brandStandardsubsectionQuestions.get(i).getAudit_comment());
                jsonObject.put("audit_option_id", new JSONArray(brandStandardsubsectionQuestions.get(i).getAudit_option_id()));
                jsonObject.put("audit_answer", brandStandardsubsectionQuestions.get(i).getAudit_answer());
                jsonArray.put(jsonObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        AppLogger.e("BrandStandardJson", "" + jsonArray);
        return jsonArray;
    }

    private void setLocalJSON(BrandStandardSection brandStandardSection) {
        try{
            setQuestionList(brandStandardSection.getQuestions());
            setSubSectionQuestionList(brandStandardSection.getSub_sections());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean validateCommentOfQuestion() {
        boolean validate = true;
        int count = 0;
        ArrayList<BrandStandardQuestion> brandStandardQuestions = sectionTabAdapter.getArrayList();
        ArrayList<BrandStandardQuestion> brandStandardSubsectionQuestions = new ArrayList<>();
        for (int i = 0; i < bsSubSectionAuditAdaptersList.size(); i++) {
            brandStandardSubsectionQuestions.addAll(bsSubSectionAuditAdaptersList.get(i).getArrayList());
        }
        for (int i = 0; i < brandStandardQuestions.size(); i++) {
            BrandStandardQuestion question = brandStandardQuestions.get(i);
            count += 1;
            if (question.getAudit_answer_na() == 0 && ((question.getAudit_option_id()!=null && question.getAudit_option_id().size()>0) || !TextUtils.isEmpty(question.getAudit_answer())))
            {
                if (question.getHas_comment() > 0 && (AppUtils.isStringEmpty(question.getAudit_comment()) || question.getAudit_comment().length() < question.getHas_comment())) {
                    validate = false;
                    AppUtils.toastDisplayForLong(BrandStandardAuditActivityODT.this, "Please enter the  minimum required " + question.getHas_comment() + " characters comment for question no. " + count);
                    return false;
                }
            }
        }

        for (int i = 0; i < brandStandardSubsectionQuestions.size(); i++) {
            BrandStandardQuestion subQuestion = brandStandardSubsectionQuestions.get(i);
            count += 1;
            if (subQuestion.getAudit_answer_na() == 0 && ((subQuestion.getAudit_option_id()!=null && subQuestion.getAudit_option_id().size()>0) || !TextUtils.isEmpty(subQuestion.getAudit_answer()))) {
                if (subQuestion.getHas_comment() > 0 && (AppUtils.isStringEmpty(subQuestion.getAudit_comment()) || subQuestion.getAudit_comment().length() < subQuestion.getHas_comment())) {
                    validate = false;
                    AppUtils.toastDisplayForLong(BrandStandardAuditActivityODT.this, "Please enter the minimum required " + subQuestion.getHas_comment() + " characters comment for question no." + count);
                    return false;
                }
            }
        }
        return validate;

    }



    private void removeHandlerCallBck() {
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
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
                BrandStandardRefrence bsRefrence = (BrandStandardRefrence) view.getTag();
                if (bsRefrence != null) {
                    if (bsRefrence.getFile_type().contains("image"))
                        ShowHowImageActivity.start(this, bsRefrence.getFile_url());
                    else if (bsRefrence.getFile_type().contains("audio"))
                        AudioPlayerActivity.start(this, bsRefrence.getFile_url());
                    else if (bsRefrence.getFile_type().contains("video"))
                        ExoVideoPlayer.start(this, bsRefrence.getFile_url());
                    else {
                        ShowHowWebViewActivity.start(this, bsRefrence.getFile_url());

                    }
                }
                break;
            case R.id.bs_save_btn:
                AppUtils.hideKeyboard(context, view);
                if (bsSaveBtn.getText().toString().equalsIgnoreCase(getString(R.string.text_submitgoto)))
                    onBackPressed();  // because we want to auto direct on SUbmit paeg like BackButton
                else {
                    isSaveButtonClick = true;
                    saveSectionOrPagewiseData();
                }

                break;
            case R.id.bs_addmedia:
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
            case R.id.next_btn:
                isSaveButtonClick=false;
                if(isAnswerCliked)
                {
                    if (saveSectionOrPagewiseData()) {
                        isAnswerCliked=false;
                        setNextButtonSetUP();
                    }
                }
                else
                    setNextButtonSetUP();

                break;
            case R.id.prev_btn:
                isSaveButtonClick=false;
                if(isAnswerCliked)
                {
                    if (saveSectionOrPagewiseData()) {
                        isAnswerCliked=false;
                        setPreviousButtonSetUP();
                    }
                }
                else
                    setPreviousButtonSetUP();

                break;
        }
    }

    private void setNextButtonSetUP() {
        if (currentSectionPosition < brandStandardSectionArrayList.size() - 1) {
            brandStandardSectionArrayList.set(currentSectionPosition, brandStandardSection);
            currentSectionPosition++;
            brandStandardSection = brandStandardSectionArrayList.get(currentSectionPosition);
            loadData();

            if (currentSectionPosition == brandStandardSectionArrayList.size() - 1) {
                changeSaveButtonToGoToSubmit(false);
                nextBtn.setText("N/A");
                nextBtn.setEnabled(false);
                prevBtn.setEnabled(true);
                prevBtn.setText("< Back" + " (" + (currentSectionPosition ) + "/" + brandStandardSectionArrayList.size() + ")\n" + brandStandardSectionArrayList.get(currentSectionPosition - 1).getSection_title());
            } else {
                changeSaveButtonToGoToSubmit(true);
                nextBtn.setText("(" + (currentSectionPosition + 2) + "/" + brandStandardSectionArrayList.size() + ") Next >\n" + brandStandardSectionArrayList.get(currentSectionPosition + 1).getSection_title());
                prevBtn.setText("< Back" + " (" + (currentSectionPosition ) + "/" + brandStandardSectionArrayList.size() + ")\n" + brandStandardSectionArrayList.get(currentSectionPosition - 1).getSection_title());
                nextBtn.setEnabled(true);
                prevBtn.setEnabled(true);
            }
        }
    }

    private void setPreviousButtonSetUP() {
        if (currentSectionPosition > 0) {
            brandStandardSectionArrayList.set(currentSectionPosition, brandStandardSection);
            currentSectionPosition--;
            brandStandardSection = brandStandardSectionArrayList.get(currentSectionPosition);
            loadData();
            if (currentSectionPosition == 0) {
                prevBtn.setText("N/A");
                prevBtn.setEnabled(false);
                nextBtn.setText("(" + (currentSectionPosition + 2) + "/" + brandStandardSectionArrayList.size() + ") Next >\n" + brandStandardSectionArrayList.get(currentSectionPosition + 1).getSection_title());
                nextBtn.setEnabled(true);
            } else {
                nextBtn.setText("(" + (currentSectionPosition + 2) + "/" + brandStandardSectionArrayList.size() + ") Next >\n" + brandStandardSectionArrayList.get(currentSectionPosition + 1).getSection_title());
                prevBtn.setText("< Back" + " (" + (currentSectionPosition ) + "/" + brandStandardSectionArrayList.size() + ")\n" + brandStandardSectionArrayList.get(currentSectionPosition - 1).getSection_title());
                nextBtn.setEnabled(true);
                prevBtn.setEnabled(true);
            }
        }
    }
    private void changeSaveButtonToGoToSubmit(boolean isSave)
    {
        if(isSave) {
            bsSaveBtn.setText(getString(R.string.text_save));
            mediaAddBtn.setVisibility(View.VISIBLE);
        }
        else {
            bsSaveBtn.setText(getString(R.string.text_submitgoto));
            mediaAddBtn.setVisibility(View.GONE);
        }
    }
    private void setPrevNextButton() {
        if (brandStandardSectionArrayList.size() == 1)
        {
            nextBtn.setVisibility(View.GONE);
            prevBtn.setVisibility(View.GONE);
            changeSaveButtonToGoToSubmit(false);


        } else if (brandStandardSectionArrayList.size() > 1)
        {
            if (currentSectionPosition == 0) {
                changeSaveButtonToGoToSubmit(true);
                prevBtn.setEnabled(false);
                prevBtn.setText("N/A");
                nextBtn.setText("(" + (currentSectionPosition + 2) + "/" + brandStandardSectionArrayList.size() + ") Next >\n" + brandStandardSectionArrayList.get(currentSectionPosition + 1).getSection_title());
            } else if (currentSectionPosition == brandStandardSectionArrayList.size() - 1) {
                changeSaveButtonToGoToSubmit(false);
                nextBtn.setEnabled(false);
                nextBtn.setText("N/A");
                prevBtn.setText("< Back" + " (" + (currentSectionPosition ) + "/" + brandStandardSectionArrayList.size() + ")\n" + brandStandardSectionArrayList.get(currentSectionPosition - 1).getSection_title());
            } else {
                changeSaveButtonToGoToSubmit(true);
                nextBtn.setText("(" + (currentSectionPosition + 2) + "/" + brandStandardSectionArrayList.size() + ") Next >\n" + brandStandardSectionArrayList.get(currentSectionPosition + 1).getSection_title());
                prevBtn.setText("< Back" + " (" + (currentSectionPosition ) + "/" + brandStandardSectionArrayList.size() + ")\n" + brandStandardSectionArrayList.get(currentSectionPosition - 1).getSection_title());
            }
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
    public void countNA_Answers() {
        totalMarks = 0;
        marksObtained = 0;
        for (int i = 0; i < brandStandardSection.getQuestions().size(); i++) {
            BrandStandardQuestion brandStandardQuestion = brandStandardSection.getQuestions().get(i);
            if (!(brandStandardQuestion.getAudit_answer_na() == 1)) {
                if (brandStandardQuestion.getQuestion_type().equals("radio")) {
                    totalMarks = totalMarks + brandStandardQuestion.getOptions().get(0).getOption_mark();
                } else {
                    for (int k = 0; k < brandStandardQuestion.getOptions().size(); k++) {
                        totalMarks = totalMarks + brandStandardQuestion.getOptions().get(k).getOption_mark();
                    }
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
        for (int j = 0; j < brandStandardSection.getSub_sections().size(); j++) {
            ArrayList<BrandStandardQuestion> brandStandardQuestions = brandStandardSection.
                    getSub_sections().get(j).getQuestions();
            for (int i = 0; i < brandStandardQuestions.size(); i++) {
                //totalQuestionCount++;
                BrandStandardQuestion brandStandardQuestion = brandStandardQuestions.get(i);

                if (!(brandStandardQuestion.getAudit_answer_na() == 1)) {
                    if (brandStandardQuestion.getQuestion_type().equals("radio")) {
                        totalMarks = totalMarks + brandStandardQuestion.getOptions().get(0).getOption_mark();
                    } else {
                        for (int k = 0; k < brandStandardQuestion.getOptions().size(); k++) {
                            totalMarks = totalMarks + brandStandardQuestion.getOptions().get(k).getOption_mark();
                        }
                    }
                    if (brandStandardQuestion.getAudit_option_id().size() > 0) {
                        for (int l = 0; l < brandStandardQuestion.getAudit_option_id().size(); l++) {
                            for (int m = 0; m < brandStandardQuestion.getOptions().size(); m++) {
                                if (brandStandardQuestion.getAudit_option_id().get(l) ==
                                        brandStandardQuestion.getOptions().get(m).getOption_id()) {
                                    marksObtained = marksObtained + brandStandardQuestion.getOptions().get(m).getOption_mark();
                                    break;
                                }
                            }
                        }
                    }
                }

            }
        }

        scoreText.setText("Score: " + (int) (((float) marksObtained / (float) totalMarks) * 100) + "% (" + marksObtained + "/" + totalMarks + ")");
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


    @Override
    public void onNetworkCallInitiated(String service) { }
    @Override
    public void onNetworkCallCompleted(String type, String service, String responseStr) {
        isAnswerCliked=false; // because question is saved
        AuditSubSectionsActivityODT.isDataSaved=true;
        AppLogger.e(TAG, "BSResponse: " + responseStr);
        try {
            JSONObject response = new JSONObject(responseStr);
            if (!response.getBoolean(AppConstant.RES_KEY_ERROR))
            {
                AppUtils.toast((BaseActivityOditly) context, response.getString(AppConstant.RES_KEY_MESSAGE));
                if (isBackButtonClick)
                {
                    isBackButtonClick=false; // This is only for showing progressBar
                    finish();
                }
            } else if (response.getBoolean(AppConstant.RES_KEY_ERROR)) {
                AppUtils.toast((BaseActivityOditly) context, response.getString(AppConstant.RES_KEY_MESSAGE));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        hideProgressDialog();
    }

    @Override
    public void onNetworkCallError(String service, String errorMessage) {
        hideProgressDialog();
        Log.e("onNetworkCallError","===>"+errorMessage);
        AppUtils.toast(this, this.getString(R.string.oops));
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("BRAND onStop",";;;;;onStop");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("BRAND onPause",";;;;;onPause");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("BRAND OnDestroy",";;;;;OnDestroy");
        removeHandlerCallBck();
    }

}
