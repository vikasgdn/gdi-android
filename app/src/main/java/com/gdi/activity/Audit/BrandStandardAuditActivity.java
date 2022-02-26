package com.gdi.activity.Audit;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.gdi.R;
import com.gdi.activity.BaseActivity;
import com.gdi.activity.GDIApplication;
import com.gdi.adapter.BrandStandardAuditAdapter;
import com.gdi.api.ApiEndPoints;
import com.gdi.api.BSSaveSubmitJsonRequest;
import com.gdi.api.VolleyNetworkRequest;
import com.gdi.model.audit.BrandStandard.BrandStandardQuestion;
import com.gdi.model.audit.BrandStandard.BrandStandardSection;
import com.gdi.model.audit.BrandStandard.BrandStandardSubSection;
import com.gdi.model.localDB.brandstandard.BrandStandardRoot;
import com.gdi.utils.ApiResponseKeys;
import com.gdi.utils.AppLogger;
import com.gdi.utils.AppPrefs;
import com.gdi.utils.AppUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BrandStandardAuditActivity extends BaseActivity implements View.OnClickListener, BrandStandardAuditAdapter.CustomItemClickListener {

    @BindView(R.id.rv_bs_question)
    RecyclerView questionListRecyclerView;
    @BindView(R.id.ll_bs_sub_section_question)
    LinearLayout subSectionQuestionLayout;
    @BindView(R.id.bs_save_btn)
    Button bsSaveBtn;
    @BindView(R.id.bs_add_btn)
    Button addBtn;
    @BindView(R.id.bs_add_file_btn)
    Button fileBtn;
    @BindView(R.id.next_btn)
    Button nextBtn;
    @BindView(R.id.prev_btn)
    Button prevBtn;
    @BindView(R.id.bs_attachment_count)
    TextView bsAttachmentCount;
    @BindView(R.id.tv_header_decription_btn)
    TextView headerDescriptionBtn;
    @BindView(R.id.timertext)
    TextView timerText;
    @BindView(R.id.score_text)
    TextView scoreText;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    Context context;
    private String status = "";
    private String editable = "";
    private String auditId = "";
    private String auditDate = "";
    private String sectionGroupId = "";
    private String sectionId = "";
    private String sectionTitle = "";
    private String fileCount = "";
    public LayoutInflater inflater;
    /*public ArrayList<BrandStandardQuestion> questionArrayList;
    public ArrayList<BrandStandardSubSection> subSectionArrayList;*/
    public int questionCount = 0;
    public int totalQuestionCount = 0;
    public int naCount = 0;
    public int answerCount = 0;
    public int positiveAnswerCount = 0;
    public float totalMarks = 0;
    public float marksObtained = 0;
    public ArrayList<Integer> optionId = new ArrayList<>();
    private static final String TAG = BrandStandardAuditActivity.class.getSimpleName();
    //BrandStandardAuditAdapter subSectionTabAdapter;
    private ArrayList<BrandStandardSection> brandStandardSectionArrayList = new ArrayList<>();
    BrandStandardSection brandStandardSection;
    BrandStandardAuditAdapter sectionTabAdapter;
    private static final int AttachmentRequest = 120;
    private static final int QuestionAttachmentRequest = 130;
    private ArrayList<BrandStandardAuditAdapter> brandStandardAuditAdapters;
    private int itemClickedPos = 0;
    private int currentSectionPosition = 0;
    private BrandStandardAuditAdapter currentBrandStandardAuditAdapter;
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L;
    Handler handler;
    private int Seconds, Minutes;

    private ArrayList<Uri> mImageListRecent;
    private  Map<Integer, List<Uri>> mListMap;


    @Override
    protected void onResume() {
        super.onResume();
        AppUtils.hideKeyboard(BrandStandardAuditActivity.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_standard_audit);
        ButterKnife.bind(BrandStandardAuditActivity.this);
        inflater = getLayoutInflater();
        AppLogger.e(TAG, "ONCreateCall");
        context = this;

        initView();
    }

    private void initView() {
        handler = new Handler();
        toolbar = findViewById(R.id.toolbar);
        questionListRecyclerView = findViewById(R.id.rv_bs_question);
        subSectionQuestionLayout = findViewById(R.id.ll_bs_sub_section_question);
        fileBtn = findViewById(R.id.bs_add_file_btn);
        bsSaveBtn = findViewById(R.id.bs_save_btn);
        nextBtn = findViewById(R.id.next_btn);
        prevBtn = findViewById(R.id.prev_btn);
        addBtn = findViewById(R.id.bs_add_btn);
        bsAttachmentCount = findViewById(R.id.bs_attachment_count);
        headerDescriptionBtn = findViewById(R.id.tv_header_decription_btn);
        timerText = findViewById(R.id.timertext);
        scoreText = findViewById(R.id.score_text);
        editable = getIntent().getStringExtra("editable");
        auditId = getIntent().getStringExtra("auditId");
        auditDate = getIntent().getStringExtra("auditDate");
        status = getIntent().getStringExtra("status");
        bsSaveBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        prevBtn.setOnClickListener(this);
        fileBtn.setOnClickListener(this);
        headerDescriptionBtn.setOnClickListener(this);
        //-----------------------------------------------------------------------------
        brandStandardSectionArrayList = new ArrayList<>();
        brandStandardSectionArrayList = getIntent().getParcelableArrayListExtra("sectionObject");
        currentSectionPosition = getIntent().getIntExtra("position", 0);
        brandStandardSection = brandStandardSectionArrayList.get(currentSectionPosition);


        if (brandStandardSectionArrayList.size() == 1) {
            nextBtn.setVisibility(View.GONE);
            prevBtn.setVisibility(View.GONE);
            nextBtn.setEnabled(false);
            prevBtn.setEnabled(false);
        } else if (brandStandardSectionArrayList.size() > 1) {
            if (currentSectionPosition == 0) {
                prevBtn.setEnabled(false);
                prevBtn.setText("N/A");
                nextBtn.setText("(" + (currentSectionPosition + 1) + "/" + brandStandardSectionArrayList.size() + ") Next >\n" + brandStandardSectionArrayList.get(currentSectionPosition + 1).getSection_title());
            } else if (currentSectionPosition == brandStandardSectionArrayList.size() - 1) {
                nextBtn.setEnabled(false);
                nextBtn.setText("N/A");
                prevBtn.setText("< Back" + " (" + (currentSectionPosition + 1) + "/" + brandStandardSectionArrayList.size() + ")\n" + brandStandardSectionArrayList.get(currentSectionPosition - 1).getSection_title());
            } else {
                nextBtn.setText("(" + (currentSectionPosition + 1) + "/" + brandStandardSectionArrayList.size() + ") Next >\n" + brandStandardSectionArrayList.get(currentSectionPosition + 1).getSection_title());
                prevBtn.setText("< Back" + " (" + (currentSectionPosition + 1) + "/" + brandStandardSectionArrayList.size() + ")\n" + brandStandardSectionArrayList.get(currentSectionPosition - 1).getSection_title());
            }
        }

        mImageListRecent=new ArrayList<>();
        mListMap=new HashMap<>();

        loadData();


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bs_save_btn:
                AppUtils.hideKeyboard(context, view);
                if (AppUtils.isNetworkConnected(context)) {
                    if (AppUtils.isStringEmpty(auditDate)) {
                        setAuditDate();
                    } else {
                        if (validateSaveQuestion()) {
                            saveBrandStandardQuestion();
                        }
                    }
                } else {
                    ArrayList<BrandStandardQuestion> brandStandardQuestions = sectionTabAdapter.getArrayList();
                    brandStandardSection.setQuestions(brandStandardQuestions);
                    localDataSaveDialog(brandStandardSection);
                }

                break;
            case R.id.bs_add_file_btn:
              //  Toast.makeText(this,"ON BUtton Click bsSection",Toast.LENGTH_SHORT).show();
                Intent addAttachment = new Intent(context, AddAttachmentActivity.class);
                addAttachment.putExtra("auditId", auditId);
                addAttachment.putExtra("sectionGroupId", sectionGroupId);
                addAttachment.putExtra("sectionId", sectionId);
                addAttachment.putExtra("questionId", "");
                addAttachment.putExtra("attachType", "bsSection");
                addAttachment.putExtra("status", status);
                addAttachment.putExtra("editable", editable);
                startActivityForResult(addAttachment, AttachmentRequest);
                break;
            case R.id.tv_header_decription_btn:
                AppUtils.showHeaderDescription(context, sectionTitle);
                break;
            case R.id.next_btn:
                mImageListRecent.clear();
                if (currentSectionPosition < brandStandardSectionArrayList.size() - 1) {
                    brandStandardSectionArrayList.set(currentSectionPosition, brandStandardSection);
                    currentSectionPosition++;
                    brandStandardSection = brandStandardSectionArrayList.get(currentSectionPosition);
                    loadData();

                    if (currentSectionPosition == brandStandardSectionArrayList.size() - 1) {
                        nextBtn.setText("N/A");
                        nextBtn.setEnabled(false);
                        prevBtn.setEnabled(true);
                        prevBtn.setText("< Back" + " (" + (currentSectionPosition + 1) + "/" + brandStandardSectionArrayList.size() + ")\n" + brandStandardSectionArrayList.get(currentSectionPosition - 1).getSection_title());
                    } else {
                        nextBtn.setText("(" + (currentSectionPosition + 1) + "/" + brandStandardSectionArrayList.size() + ") Next >\n" + brandStandardSectionArrayList.get(currentSectionPosition + 1).getSection_title());
                        prevBtn.setText("< Back" + " (" + (currentSectionPosition + 1) + "/" + brandStandardSectionArrayList.size() + ")\n" + brandStandardSectionArrayList.get(currentSectionPosition - 1).getSection_title());
                        nextBtn.setEnabled(true);
                        prevBtn.setEnabled(true);
                    }


                }
                break;
            case R.id.prev_btn:
                mImageListRecent.clear();
                if (currentSectionPosition > 0) {
                    brandStandardSectionArrayList.set(currentSectionPosition, brandStandardSection);
                    currentSectionPosition--;
                    brandStandardSection = brandStandardSectionArrayList.get(currentSectionPosition);
                    loadData();
                    if (currentSectionPosition == 0) {
                        prevBtn.setText("N/A");
                        prevBtn.setEnabled(false);
                        nextBtn.setText("(" + (currentSectionPosition + 1) + "/" + brandStandardSectionArrayList.size() + ") Next >\n" + brandStandardSectionArrayList.get(currentSectionPosition + 1).getSection_title());
                        nextBtn.setEnabled(true);
                    } else {
                        nextBtn.setText("(" + (currentSectionPosition + 1) + "/" + brandStandardSectionArrayList.size() + ") Next >\n" + brandStandardSectionArrayList.get(currentSectionPosition + 1).getSection_title());
                        prevBtn.setText("< Back" + " (" + (currentSectionPosition + 1) + "/" + brandStandardSectionArrayList.size() + ")\n" + brandStandardSectionArrayList.get(currentSectionPosition - 1).getSection_title());
                        nextBtn.setEnabled(true);
                        prevBtn.setEnabled(true);
                    }
                }
                break;
        }
    }

    private void loadData() {
        sectionTitle = brandStandardSection.getSection_title();
        brandStandardAuditAdapters = new ArrayList<>();
        setActionBar();
        questionCount = 0;
        sectionGroupId = "" + brandStandardSection.getSection_group_id();
        sectionId = "" + brandStandardSection.getSection_id();
        fileCount = "" + brandStandardSection.getAudit_section_file_cnt();

        setLocalJSON(brandStandardSection);
        bsAttachmentCount.setText(fileCount);
        if (editable.equals("0")) {
            addBtn.setText("+");
        } else {
            addBtn.setText("");
        }

        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
        countNA_Answers();
        StartTime = SystemClock.uptimeMillis();
        handler.postDelayed(runnable, 1000);


    }

    public void countNA_Answers() {
        totalMarks = 0;
        marksObtained = 0;
        answerCount = 0;
        positiveAnswerCount = 0;
        naCount = 0;
        totalQuestionCount = 0;
        for (int i = 0; i < brandStandardSection.getQuestions().size(); i++) {
            totalQuestionCount++;
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
                            if (brandStandardQuestion.getAudit_option_id().get(l) ==
                                    brandStandardQuestion.getOptions().get(m).getOption_id()) {
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
                totalQuestionCount++;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == AttachmentRequest && resultCode == Activity.RESULT_OK)
            {
                String attachmentCount = data.getStringExtra("attachmentCount");
                Log.e("AttachmentRequest ===> "," "+((GDIApplication)getApplicationContext()).getmAttachImageList().size());
                bsAttachmentCount.setText(attachmentCount);
            } else if (requestCode == QuestionAttachmentRequest && resultCode == Activity.RESULT_OK)
            {
                 mImageListRecent.clear();
                List<Uri> temp=new ArrayList<>();
                //questionCount = 0;
                String attachmentCount = data.getStringExtra("attachmentCount");
                Log.e("Question","AttachmentRequet "+((GDIApplication)getApplicationContext()).getmAttachImageList().size());


                List<Uri> tempList=mListMap.get(questionCount);


                if(tempList!=null && tempList.size()>0)
                {
                    System.out.println("all size Map  : " +tempList.size());
                    for(int i=0;i<tempList.size();i++)
                    {
                        temp.add(tempList.get(i));
                        mImageListRecent.add(tempList.get(i));
                    }
                }


             //   mImageListRecent.addAll(((GDIApplication)getApplicationContext()).getmAttachImageList());

               // temp.addAll(((GDIApplication)getApplicationContext()).getmAttachImageList());

                mListMap.put(questionCount,temp);
                AppLogger.e(TAG, "attachmentCount " + attachmentCount);
                if(currentBrandStandardAuditAdapter!=null && attachmentCount!=null)
                    currentBrandStandardAuditAdapter.setattachmentCount(Integer.parseInt(attachmentCount), itemClickedPos);


            }
        } catch (Exception e) {
            e.printStackTrace();
            AppLogger.e("AttachmentException", e.getMessage());
            AppUtils.showHeaderDescription(context, e.getMessage());
        }

    }

    private void setAuditDate() {
        Calendar auditMonthCal = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Calendar cal = Calendar.getInstance();
                        cal.set(year, monthOfYear, dayOfMonth);
                        auditDate = AppUtils.getAuditDate(cal.getTime());
                    }
                }, auditMonthCal.get(Calendar.YEAR), auditMonthCal.get(Calendar.MONTH),
                auditMonthCal.get(Calendar.YEAR));
        datePickerDialog.getDatePicker().setMaxDate(auditMonthCal.getTimeInMillis());
        datePickerDialog.setTitle("Select Audit Date");
        datePickerDialog.show();
    }

    private void setQuestionList(ArrayList<BrandStandardQuestion> questionArrayList) {
        for (int i = 0; i < questionArrayList.size(); i++) {
            BrandStandardQuestion question = questionArrayList.get(i);
            for (int j = 0; j < question.getAudit_option_id().size(); j++) {
                for (int k = 0; k < question.getOptions().size(); k++) {
                    if (question.getAudit_option_id().get(j) == question.getOptions().get(k).getOption_id()) {
                        question.getOptions().get(k).setSelected(1);
                    }
                }
            }
        }
        sectionTabAdapter = new BrandStandardAuditAdapter(context, questionArrayList, BrandStandardAuditActivity.this, editable, status,mImageListRecent);
        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        questionListRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        questionListRecyclerView.setAdapter(sectionTabAdapter);
        questionListRecyclerView.setHasFixedSize(true);
    }

    private void setSubSectionQuestionList(ArrayList<BrandStandardSubSection> subSectionArrayList) {
        subSectionQuestionLayout.removeAllViews();
        if (subSectionArrayList != null || subSectionArrayList.size() != 0) {
            for (int l = 0; l < subSectionArrayList.size(); l++) {
                BrandStandardSubSection brandStandardSubSection = subSectionArrayList.get(l);
                View view = inflater.inflate(R.layout.brand_standard_audit_layout2, null);
                TextView subSectionTitle = view.findViewById(R.id.tv_bs_sub_section_title);
                RecyclerView subSectionQuestionList = view.findViewById(R.id.rv_bs_sub_section_question);
                subSectionTitle.setText(brandStandardSubSection.getSub_section_title());

                for (int i = 0; i < brandStandardSubSection.getQuestions().size(); i++) {
                    BrandStandardQuestion question = brandStandardSubSection.getQuestions().get(i);
                    for (int j = 0; j < question.getAudit_option_id().size(); j++) {
                        for (int k = 0; k < question.getOptions().size(); k++) {
                            if (question.getAudit_option_id().get(j) == question.getOptions().get(k).getOption_id()) {
                                question.getOptions().get(k).setSelected(1);
                            }
                        }
                    }
                }
                BrandStandardAuditAdapter subSectionTabAdapter = new BrandStandardAuditAdapter(context, brandStandardSubSection.getQuestions(), BrandStandardAuditActivity.this, editable, status,mImageListRecent);
                subSectionQuestionList.setLayoutManager(new LinearLayoutManager(context));
                subSectionQuestionList.setAdapter(subSectionTabAdapter);
                brandStandardAuditAdapters.add(subSectionTabAdapter);

                subSectionQuestionLayout.addView(view);
            }
        }
    }

    private void saveBrandStandardQuestion() {
        showProgressDialog();
        JSONObject object = BSSaveSubmitJsonRequest.createInput(auditId, auditDate, "1", getQuestionsArray());
        AppLogger.e(TAG, "" + object);
        Response.Listener stringListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                AppLogger.e(TAG, "BSResponse: " + response);
                try {
                    if (!response.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        AppUtils.toast((BaseActivity) context, response.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                        status = "" + response.getJSONObject("data").getInt("brand_std_status");
                        /*Toast.makeText(context, "Answer Saved", Toast.LENGTH_SHORT).show();
                        Intent result = new Intent();
                        setResult(RESULT_OK, result);
                        finish();*/
                    } else if (response.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        AppUtils.toast((BaseActivity) context, response.getString(ApiResponseKeys.RES_KEY_MESSAGE));
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
                        /*if (context != null) { AppUtils.toast((BaseActivity) context, getString(R.string.alert_msg_invalid_response));  }*/
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                            /*if (context != null) {
                                AppUtils.toast((BaseActivity) context, getString(R.string.alert_msg_invalid_response));
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

    private JSONArray getQuestionsArray() {
        JSONArray jsonArray = new JSONArray();
        ArrayList<BrandStandardQuestion> brandStandardQuestions = sectionTabAdapter.getArrayList();
        ArrayList<BrandStandardQuestion> brandStandardsubsectionQuestions = new ArrayList<>();
        for (int i = 0; i < brandStandardAuditAdapters.size(); i++) {
            brandStandardsubsectionQuestions.addAll(brandStandardAuditAdapters.get(i).getArrayList());
        }


        for (int i = 0; i < brandStandardQuestions.size(); i++) {
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

        for (int i = 0; i < brandStandardsubsectionQuestions.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("question_id", brandStandardsubsectionQuestions.get(i).getQuestion_id());
                jsonObject.put("audit_answer_na", brandStandardsubsectionQuestions.get(i).getAudit_answer_na());
                jsonObject.put("audit_comment", brandStandardsubsectionQuestions.get(i).getAudit_comment());
                jsonObject.put("audit_option_id", getOptionIdArray(brandStandardsubsectionQuestions.get(i).getAudit_option_id()));
                jsonObject.put("audit_answer", brandStandardsubsectionQuestions.get(i).getAudit_answer());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        AppLogger.e("BrandStandardJson", "" + jsonArray);
        return jsonArray;
    }

    private JSONArray getOptionIdArray(ArrayList<Integer> arrayList) {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < arrayList.size(); i++) {
            jsonArray.put(arrayList.get(i));
        }
        AppLogger.e("BrandStandardJsonoption", "" + jsonArray);
        return jsonArray;
    }

    private void setActionBar() {
        initToolbar(toolbar);
        setTitle(sectionTitle);
        enableBack(true);
        enableBackPressed();
    }


    @Override
    public void onItemClick(int Count, BrandStandardAuditAdapter brandStandardAuditAdapter, int bsQuestionId, String attachtype, int position) {
        itemClickedPos = position;
        questionCount = Count;
      //  Toast.makeText(BrandStandardAuditActivity.this,"ITEM CLICK==> "+position,Toast.LENGTH_SHORT).show();
        currentBrandStandardAuditAdapter = brandStandardAuditAdapter;
        Intent addAttachment = new Intent(context, AddAttachmentActivity.class);
        addAttachment.putExtra("auditId", auditId);
        addAttachment.putExtra("sectionGroupId", sectionGroupId);
        addAttachment.putExtra("sectionId", sectionId);
        addAttachment.putExtra("questionId", "" + bsQuestionId);
        addAttachment.putExtra("attachType", attachtype);
        addAttachment.putExtra("status", status);
        addAttachment.putExtra("editable", editable);
        startActivityForResult(addAttachment, QuestionAttachmentRequest);
    }


    private void saveLocalDB(BrandStandardSection brandStandardSection) {
        String localDB = AppPrefs.getLocalDB(context);
        JSONArray jsonArray = null;
        try {
            if (!AppUtils.isStringEmpty(localDB)) {
                jsonArray = new JSONArray(localDB);
                AppPrefs.setLocalDB(context, "");
                if (jsonArray != null) {
                    boolean isAuditSaved = false;
                    ArrayList<BrandStandardRoot> arrayList = new ArrayList<>();
                    Type listType = new TypeToken<List<BrandStandardRoot>>() {
                    }.getType();

                    arrayList = new Gson().fromJson(localDB, listType);


                    BrandStandardRoot brandStandardRoot;
                    for (int i = 0; i < arrayList.size(); i++) {/*
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        BrandStandardRoot brandStandardRoot = new GsonBuilder().create()
                                .fromJson(jsonObject.toString(), BrandStandardRoot.class);*/
                        // arrayList.addAll(brandStandardRoot.getSections());
                        if (arrayList.get(i).getAuditId().equals(auditId)) {
                            for (int j = 0; j < arrayList.get(i).getSections().size(); j++) {
                                if (arrayList.get(i).getSections().get(j).getSection_id() == brandStandardSection.getSection_id()) {
                                    arrayList.get(i).getSections().remove(j);
                                    AppLogger.e(TAG, "replace db data of same section with new one");
                                    break;
                                }
                            }
                            arrayList.get(i).getSections().add(brandStandardSection);
                            isAuditSaved = true;
                            break;
                        }

                    }

                    if (!isAuditSaved) {
                        Gson gson = new Gson();
                        String jsonString = gson.toJson(brandStandardSection);
                        JSONObject jO = new JSONObject(jsonString);
                        JSONArray jA = new JSONArray();
                        jA.put(jO);
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("auditId", auditId);
                        jsonObject.put("sections", jA);
                        jsonArray.put(jsonObject);
                        AppLogger.e(TAG, "add new root object with new audit id");
                    } else {
                        Gson gson = new Gson();
                        String jsonString = gson.toJson(arrayList);
                        jsonArray = new JSONArray(jsonString);
                    }


                }
            } else {

                Gson gson = new Gson();
                String jsonString = gson.toJson(brandStandardSection);
                JSONObject jO = new JSONObject(jsonString);
                JSONArray jA = new JSONArray();
                jA.put(jO);

                /*ArrayList<BrandStandardSection> arrayList = new ArrayList<>();
                arrayList.add(brandStandardSection);*/
                jsonArray = new JSONArray();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("auditId", auditId);
                jsonObject.put("sections", jA);
                jsonArray.put(jsonObject);
                AppLogger.e(TAG, "add new root object with new audit id");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        AppPrefs.setLocalDB(context, "" + jsonArray);
        Intent result = new Intent();
        setResult(RESULT_OK, result);
        finish();
    }

    private void setLocalJSON(BrandStandardSection brandStandardSection) {
        boolean ifExist = false;
        String localDB = AppPrefs.getLocalDB(context);
        JSONArray jsonArray = null;
        try {
            if (!AppUtils.isStringEmpty(localDB)) {
                jsonArray = new JSONArray(localDB);

                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        AppLogger.e(TAG, "size : " + jsonArray.length());
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        BrandStandardRoot brandStandardRoot = new GsonBuilder().create()
                                .fromJson(jsonObject.toString(), BrandStandardRoot.class);
                        if (brandStandardRoot.getAuditId().equals(auditId)) {
                            ArrayList<BrandStandardSection> arrayList = new ArrayList<>();
                            arrayList.addAll(brandStandardRoot.getSections());
                            AppLogger.e(TAG, "Same audit Id");
                            for (int j = 0; j < arrayList.size(); j++) {
                                if (arrayList.get(j).getSection_id() == brandStandardSection.getSection_id()) {
                                    answerShowDialog(arrayList.get(j));
                                    AppLogger.e(TAG, "You have already saved data for this section id do you want to override it");
                                    //break;
                                } else {
                                    AppLogger.e(TAG, "Section Id not same");
                                    setQuestionList(brandStandardSection.getQuestions());
                                    setSubSectionQuestionList(brandStandardSection.getSub_sections());
                                    //break;
                                }
                            }
                        } else {
                            AppLogger.e(TAG, "Audit Id not same");
                            setQuestionList(brandStandardSection.getQuestions());
                            setSubSectionQuestionList(brandStandardSection.getSub_sections());
                            //break;
                        }
                    }
                }
            } else {
                setQuestionList(brandStandardSection.getQuestions());
                setSubSectionQuestionList(brandStandardSection.getSub_sections());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void localDataSaveDialog(final BrandStandardSection brandStandardSection) {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        dialog.setTitle("GDI");
        dialog.setMessage("No Internet Connection. Do you want to save data locally?");

        dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                 saveLocalDB(brandStandardSection);
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });
        dialog.create().show();
    }

    private void answerShowDialog(final BrandStandardSection brandStandardSection) {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        dialog.setTitle("GDI");
        dialog.setMessage("You have unsaved answered locally in your device. Would you like to sync them?");

        dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /*questionArrayList = brandStandardSection.getQuestions();
                subSectionArrayList = brandStandardSection.getSub_sections();*/
                setQuestionList(brandStandardSection.getQuestions());
                setSubSectionQuestionList(brandStandardSection.getSub_sections());
                AppLogger.e(TAG, "Replace it in adapter");
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                setQuestionList(brandStandardSection.getQuestions());
                setSubSectionQuestionList(brandStandardSection.getSub_sections());
                dialog.dismiss();
            }
        });
        dialog.create().show();
    }


    private boolean validateSaveQuestion() {
        boolean validate = true;
        int count = 0;
        ArrayList<BrandStandardQuestion> brandStandardQuestions = sectionTabAdapter.getArrayList();
        ArrayList<BrandStandardQuestion> brandStandardSubsectionQuestions = new ArrayList<>();
        for (int i = 0; i < brandStandardAuditAdapters.size(); i++) {
            brandStandardSubsectionQuestions.addAll(brandStandardAuditAdapters.get(i).getArrayList());
        }


        //totalCount = brandStandardSection.getQuestions().size();
        for (int i = 0; i < brandStandardQuestions.size(); i++) {
            BrandStandardQuestion question = brandStandardQuestions.get(i);
            count += 1;
            for (int j = 0; j < question.getOptions().size(); j++) {
                if (question.getOptions().get(j).getOption_mark() == 0) {
                    if (question.getAudit_option_id() != null && question.getAudit_option_id().size() > 0) {
                        if (question.getOptions().get(j).getOption_id() == question.getAudit_option_id().get(0)) {
                            /*if (AppUtils.isStringEmpty(question.getAudit_comment())) {
                                //validate = false;
                                AppUtils.toast(BrandStandardAuditActivity.this,"Please Enter comment for " +
                                        "question "+count);
                                return false;
                            }*/
                        }
                    }
                }
            }

        }

        for (int i = 0; i < brandStandardSubsectionQuestions.size(); i++) {
            BrandStandardQuestion subQuestion = brandStandardSubsectionQuestions.get(i);
            count += 1;
            for (int j = 0; j < subQuestion.getOptions().size(); j++) {
                if (subQuestion.getOptions().get(j).getOption_mark() == 0) {
                    if (subQuestion.getAudit_option_id() != null && subQuestion.getAudit_option_id().size() > 0) {
                        if (subQuestion.getOptions().get(j).getOption_id() == subQuestion.getAudit_option_id().get(0)) {
                            /*if (AppUtils.isStringEmpty(subQuestion.getAudit_comment())) {
                                //validate = false;
                                AppUtils.toast(BrandStandardAuditActivity.this,"Please Enter comment for " +
                                        "question "+count);
                                return false;
                            }*/
                        }
                    }
                }
            }

        }

        //answerArray = getQuestionsArray (brandStandardQuestionsSubmissions);
        return validate;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
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


}
