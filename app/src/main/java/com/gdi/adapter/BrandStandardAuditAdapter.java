package com.gdi.adapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.activity.Audit.AddAttachmentActivity;
import com.gdi.activity.Audit.BrandStandardAuditActivity;
import com.gdi.activity.ImageViewActivity;
import com.gdi.activity.MysteryAuditReport.ReportFAQActivity;
import com.gdi.model.audit.AuditInfo;
import com.gdi.model.audit.BrandStandard.BrandStandardInfo;
import com.gdi.model.audit.BrandStandard.BrandStandardQuestion;
import com.gdi.model.audit.BrandStandard.BrandStandardQuestionsOption;
import com.gdi.model.audit.BrandStandard.BrandStandardSection;
import com.gdi.model.audit.BrandStandard.BrandStandardSubSection;
import com.gdi.model.reportfaq.FAQQuestionsInfo;
import com.gdi.utils.AppLogger;
import com.gdi.utils.AppUtils;

import java.util.ArrayList;

import static com.gdi.activity.Audit.BrandStandardAuditActivity.questionCount;

public class BrandStandardAuditAdapter extends
        RecyclerView.Adapter<BrandStandardAuditAdapter.BrandStandardAuditViewHolder> {

    private Context context;
    private ArrayList<BrandStandardQuestion> data;
    CustomItemClickListener customItemClickListener;
    private String editable;
    private String status;


    public BrandStandardAuditAdapter(Context context, ArrayList<BrandStandardQuestion> data, CustomItemClickListener customItemClickListener,
                                     String editable, String status) {
        this.context = context;
        this.data = data;
        this.editable = editable;
        this.status = status;
        this.customItemClickListener = customItemClickListener;

    }

    @Override
    public BrandStandardAuditViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.brand_standard_audit_layout,
                parent, false);

        return new BrandStandardAuditViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BrandStandardAuditViewHolder holder, final int position) {
        //TODO : Static data testing

        final BrandStandardQuestion brandStandardQuestion = data.get(position);
        questionCount = questionCount + 1;
        holder.questionTitle.setText("" + questionCount + ". " + brandStandardQuestion.getQuestion_title());
        holder.tvBrandStandardAttachCount.setText("" + brandStandardQuestion.getAudit_question_file_cnt());

        if (AppUtils.isStringEmpty(brandStandardQuestion.getAudit_comment())){
            holder.comment.setVisibility(View.INVISIBLE);
        }else {
            holder.comment.setVisibility(View.VISIBLE);
            holder.comment.setText(brandStandardQuestion.getAudit_comment());
        }

        if (AppUtils.isStringEmpty(brandStandardQuestion.getReviewer_answer_comment())){
            holder.rejectedComment.setVisibility(View.GONE);
        }else {
            holder.rejectedComment.setVisibility(View.VISIBLE);
            holder.rejectedComment.setText("Reviewer Comment:- " + brandStandardQuestion.getReviewer_answer_comment());
        }

        holder.bsLayout.setFocusable(true);
        holder.bsLayout.setFocusableInTouchMode(true);

        if (editable.equals("0")) {
            enableView(holder);
        } else {
            disableView(holder);
        }

        brandStandardQuestion.setAudit_comment(holder.comment.getText().toString());

        if (AppUtils.isStringEmpty(brandStandardQuestion.getRef_image_url())) {
            holder.referenceImageTab.setVisibility(View.GONE);
        } else {
            holder.referenceImageTab.setVisibility(View.VISIBLE);
        }
        holder.referenceImageTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ImageViewActivity.class);
                intent.putExtra("fileUrl", brandStandardQuestion.getRef_image_url());
                context.startActivity(intent);
            }
        });

        holder.comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                AppLogger.e("AuditCommment", "" + editable.toString());
                brandStandardQuestion.setAudit_comment("" + editable.toString());
            }
        });
        /*AppLogger.e("AuditCommment", "" + holder.comment.getText().toString());
        brandStandardQuestion.setAudit_comment(holder.comment.getText().toString());*/

        addAnswerList(brandStandardQuestion, holder,
                brandStandardQuestion.getQuestion_type(), brandStandardQuestion.getAudit_option_id());

        if (brandStandardQuestion.getAudit_answer_na() == 1) {
            holder.naCheckBox.setChecked(true);
        } else {
            holder.naCheckBox.setChecked(false);
        }


        holder.brandStandardAddFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customItemClickListener.onItemClick(BrandStandardAuditAdapter.this ,brandStandardQuestion.getQuestion_id(), "bsQuestion", position);

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class BrandStandardAuditViewHolder extends RecyclerView.ViewHolder {

        TextView questionTitle;
        TextView note;
        TextView rejectedComment;
        EditText comment;
        TextView referenceImageTab;
        CheckBox naCheckBox;
        public TextView tvBrandStandardAttachCount;
        Button brandStandardAddFile;
        Button addBtn;
        LinearLayout noteLayout;
        LinearLayout answerList;
        LinearLayout bsLayout;
        RelativeLayout btnLayout;


        public BrandStandardAuditViewHolder(View itemView) {
            super(itemView);

            questionTitle = itemView.findViewById(R.id.tv_bs_title);
            note = itemView.findViewById(R.id.tv_bs_note);
            rejectedComment = itemView.findViewById(R.id.tv_bs_rejected_comment);
            comment = itemView.findViewById(R.id.tv_bs_comment);
            referenceImageTab = itemView.findViewById(R.id.bs_reference_image_tab);
            answerList = itemView.findViewById(R.id.rv_brand_standard_answer);
            naCheckBox = itemView.findViewById(R.id.cb_brand_standard_na);
            addBtn = itemView.findViewById(R.id.bs_add_btn);
            brandStandardAddFile = itemView.findViewById(R.id.bs_add_file_btn);
            tvBrandStandardAttachCount = itemView.findViewById(R.id.bs_attachment_count);
            noteLayout = itemView.findViewById(R.id.ll_note_layout);
            bsLayout = itemView.findViewById(R.id.bs_layout);
            btnLayout = itemView.findViewById(R.id.reference_btn_layout);


        }
    }

    private void enableView(BrandStandardAuditViewHolder holder) {
        holder.naCheckBox.setEnabled(true);
        holder.comment.setEnabled(true);
        holder.addBtn.setText("+");
    }

    private void disableView(BrandStandardAuditViewHolder holder) {
        holder.naCheckBox.setEnabled(false);
        holder.comment.setEnabled(false);
        holder.addBtn.setText("");
    }

    private void addAnswerList(final BrandStandardQuestion brandStandardQuestion,
                               final BrandStandardAuditViewHolder holder, final String questionType,
                               final ArrayList<Integer> answerOptionId) {

        final ArrayList<BrandStandardQuestionsOption> arrayList = new ArrayList<>();
        arrayList.addAll(brandStandardQuestion.getOptions());
        holder.answerList.removeAllViews();

        for (int i = 0; i < arrayList.size(); i++) {

            final BrandStandardQuestionsOption brandStandardQuestionsOption = arrayList.get(i);
            final View view = ((BrandStandardAuditActivity) context)
                    .inflater.inflate(R.layout.brand_standard_audit_layout3, null);

            TextView radioText = view.findViewById(R.id.radio_text);

            if (editable.equals("0")) {
            } else {
            }
            radioText.setText(String.valueOf(brandStandardQuestionsOption.getOption_text()));
            if (questionType.equals("radio")) {

                //rbBrandStandardButton.setTag(brandStandardQuestionsOption.getOption_id());
                //rbBrandStandardButton.setId(i);
                if (answerOptionId.size() != 0 &&
                        answerOptionId.get(0) == brandStandardQuestionsOption.getOption_id()) {
                    //rbBrandStandardButton.setChecked(true);
                }
            } else {
                int optionId = brandStandardQuestionsOption.getOption_id();
                for(int j = 0;j<answerOptionId.size();j++){
                    if(optionId==answerOptionId.get(j)){
                        //cbBrandStandardAnswer.setChecked(true);
                        break;
                    }
                }
                //cbBrandStandardAnswer.setTag(brandStandardQuestionsOption.getOption_id());
                //cbBrandStandardAnswer.setId(i);
                holder.comment.setVisibility(View.GONE);
            }


            /*rbBrandStandardButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    *//*answerOptionId.add((int)rbBrandStandardButton.getTag());
                    AppLogger.e("selectedId", "" + answerOptionId.get(0));*//*
                    AppLogger.e("OptionId", "" + brandStandardQuestionsOption.getOption_id());
                    int optionId = brandStandardQuestionsOption.getOption_id();
                    holder.comment.setVisibility(View.VISIBLE);
                    for (int j = 0; j < arrayList.size(); j++) {
                        RadioButton radioButton = holder.answerList.findViewById(j);
                        if (radioButton.equals(rbBrandStandardButton)) {
                            holder.comment.setText("");
                            answerOptionId.clear();
                            answerOptionId.add(optionId);
                            AppLogger.e("selectedId", "" + answerOptionId.get(0));
                            radioButton.setChecked(true);
                        } else {
                            radioButton.setChecked(false);

                        }
                    }


                    //holder.answerList.findViewWithTag(rbBrandStandardButton.getTag());
                }
            });*/

            /*cbBrandStandardAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppLogger.e("OptionId", "" + brandStandardQuestionsOption.getOption_id());
                    int optionId = brandStandardQuestionsOption.getOption_id();
                    for (int j = 0; j < arrayList.size(); j++) {
                        CheckBox checkBox = holder.answerList.findViewById(j);

                        if (checkBox.equals(cbBrandStandardAnswer)) {
                            if(checkBox.isChecked()){
                                answerOptionId.add(optionId);
                            }else answerOptionId.remove(new Integer(optionId));
                            *//*AppLogger.e("selectedId", "" + answerOptionId.get(0));
                            checkBox.setChecked(true);*//*
                        }
                    }


                    //holder.answerList.findViewWithTag(rbBrandStandardButton.getTag());
                }
            });*/


            holder.naCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (questionType.equals("radio")) {
                        for (int i = 0; i < arrayList.size(); i++) {
                            RadioButton radioButtonYes = holder.answerList.findViewWithTag(arrayList.get(i).getOption_id());
                            radioButtonYes.setChecked(false);
                        }

                        if (holder.naCheckBox.isChecked()) {
                            brandStandardQuestion.setAudit_answer_na(1);
                            holder.comment.setVisibility(View.INVISIBLE);
                            holder.comment.setText("");
                            for (int i = 0; i < arrayList.size(); i++) {
                                RadioButton radioButtonYes = holder.answerList.findViewWithTag(arrayList.get(i).getOption_id());
                                radioButtonYes.setEnabled(false);
                            }

                        } else {
                            brandStandardQuestion.setAudit_answer_na(0);
                            holder.comment.setVisibility(View.INVISIBLE);
                            holder.comment.setText("");
                            for (int i = 0; i < arrayList.size(); i++) {
                                RadioButton radioButtonYes = holder.answerList.findViewWithTag(arrayList.get(i).getOption_id());
                                radioButtonYes.setEnabled(true);
                            }
                        }
                    }else {
                        for (int i = 0; i < arrayList.size(); i++) {
                            CheckBox checkBox = holder.answerList.findViewWithTag(arrayList.get(i).getOption_id());
                            checkBox.setChecked(false);
                        }

                        if (holder.naCheckBox.isChecked()) {
                            brandStandardQuestion.setAudit_answer_na(1);
                            for (int i = 0; i < arrayList.size(); i++) {
                                CheckBox checkBox = holder.answerList.findViewWithTag(arrayList.get(i).getOption_id());
                                checkBox.setEnabled(false);
                            }

                        } else {
                            brandStandardQuestion.setAudit_answer_na(0);
                            for (int i = 0; i < arrayList.size(); i++) {
                                CheckBox checkBox = holder.answerList.findViewWithTag(arrayList.get(i).getOption_id());
                                checkBox.setEnabled(true);
                            }
                        }
                    }
                }
            });

            /*if (status.equals("3")) {
                if (AppUtils.isStringEmpty(brandStandardQuestion.getReviewer_answer_comment())) {
                    if (questionType.equals("radio")) {
                        *//*for (int j = 0; j < arrayList.size(); j++) {
                            //RadioButton radioButton = holder.answerList.findViewWithTag(arrayList.get(i).getOption_id());
                            RadioButton radioButton = holder.answerList.findViewById(j);
                            radioButton.setEnabled(false);
                        }*//*
                        rbBrandStandardButton.setEnabled(false);
                        radioText.setTextColor(context.getResources().getColor(R.color.textGrey));
                        holder.comment.setEnabled(false);
                        holder.naCheckBox.setEnabled(false);
                    } else {
                        *//*for (int j = 0; j < arrayList.size(); j++) {
                            //CheckBox checkBox = holder.answerList.findViewWithTag(arrayList.get(i).getOption_id());
                            CheckBox checkBox = holder.answerList.findViewById(j);
                            checkBox.setEnabled(false);
                        }*//*
                        cbBrandStandardAnswer.setEnabled(false);
                        cbBrandStandardAnswer.setTextColor(context.getResources().getColor(R.color.textGrey));
                        holder.comment.setEnabled(false);
                        holder.naCheckBox.setEnabled(false);
                    }
                }
            }*/

            holder.answerList.addView(view);
        }

    }



    public ArrayList<BrandStandardQuestion> getArrayList() {
        return data;
    }

    public interface CustomItemClickListener {
        void onItemClick(BrandStandardAuditAdapter brandStandardAuditAdapter, int bsQuestionId, String attachtype, int position);
    }

    public void setattachmentCount(int count, int pos){

        data.get(pos).setAudit_question_file_cnt(count);
        notifyDataSetChanged();
    }

    /*private void addAnswerList(final BrandStandardQuestion brandStandardQuestion,
                               final BrandStandardAuditViewHolder holder, final String questionType,
                               final ArrayList<Integer> answerOptionId) {

        final ArrayList<BrandStandardQuestionsOption> arrayList = new ArrayList<>();
        arrayList.addAll(brandStandardQuestion.getOptions());
        holder.answerList.removeAllViews();

        for (int i = 0; i < arrayList.size(); i++) {

            final BrandStandardQuestionsOption brandStandardQuestionsOption = arrayList.get(i);
            final View view = ((BrandStandardAuditActivity) context)
                    .inflater.inflate(R.layout.brand_standard_audit_layout3, null);
            final CheckBox cbBrandStandardAnswer = view.findViewById(R.id.cb_brand_standard_answer);
            final RadioButton rbBrandStandardButton = view.findViewById(R.id.rb_brand_standard_answer);

            TextView radioText = view.findViewById(R.id.radio_text);

            if (editable.equals("0")) {
                rbBrandStandardButton.setEnabled(true);
                cbBrandStandardAnswer.setEnabled(true);
            } else {
                rbBrandStandardButton.setEnabled(false);
                cbBrandStandardAnswer.setEnabled(false);
            }
            radioText.setText(String.valueOf(brandStandardQuestionsOption.getOption_text()));
            if (questionType.equals("radio")) {

                rbBrandStandardButton.setTag(brandStandardQuestionsOption.getOption_id());
                rbBrandStandardButton.setId(i);
                rbBrandStandardButton.setVisibility(View.VISIBLE);
                cbBrandStandardAnswer.setVisibility(View.GONE);
                if (answerOptionId.size() != 0 &&
                        answerOptionId.get(0) == brandStandardQuestionsOption.getOption_id()) {
                    rbBrandStandardButton.setChecked(true);
                }
            } else {
                int optionId = brandStandardQuestionsOption.getOption_id();
                for(int j = 0;j<answerOptionId.size();j++){
                    if(optionId==answerOptionId.get(j)){
                        cbBrandStandardAnswer.setChecked(true);
                        break;
                    }
                }
                cbBrandStandardAnswer.setTag(brandStandardQuestionsOption.getOption_id());
                cbBrandStandardAnswer.setId(i);
                rbBrandStandardButton.setVisibility(View.GONE);
                cbBrandStandardAnswer.setVisibility(View.VISIBLE);
                holder.comment.setVisibility(View.GONE);
            }


            rbBrandStandardButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    *//*answerOptionId.add((int)rbBrandStandardButton.getTag());
                    AppLogger.e("selectedId", "" + answerOptionId.get(0));*//*
                    AppLogger.e("OptionId", "" + brandStandardQuestionsOption.getOption_id());
                    int optionId = brandStandardQuestionsOption.getOption_id();
                    holder.comment.setVisibility(View.VISIBLE);
                    for (int j = 0; j < arrayList.size(); j++) {
                        RadioButton radioButton = holder.answerList.findViewById(j);
                        if (radioButton.equals(rbBrandStandardButton)) {
                            holder.comment.setText("");
                            answerOptionId.clear();
                            answerOptionId.add(optionId);
                            AppLogger.e("selectedId", "" + answerOptionId.get(0));
                            radioButton.setChecked(true);
                        } else {
                            radioButton.setChecked(false);

                        }
                    }


                    //holder.answerList.findViewWithTag(rbBrandStandardButton.getTag());
                }
            });

            cbBrandStandardAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppLogger.e("OptionId", "" + brandStandardQuestionsOption.getOption_id());
                    int optionId = brandStandardQuestionsOption.getOption_id();
                    for (int j = 0; j < arrayList.size(); j++) {
                        CheckBox checkBox = holder.answerList.findViewById(j);

                        if (checkBox.equals(cbBrandStandardAnswer)) {
                            if(checkBox.isChecked()){
                                answerOptionId.add(optionId);
                            }else answerOptionId.remove(new Integer(optionId));
                            *//*AppLogger.e("selectedId", "" + answerOptionId.get(0));
                            checkBox.setChecked(true);*//*
                        }
                    }


                    //holder.answerList.findViewWithTag(rbBrandStandardButton.getTag());
                }
            });


            holder.naCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (questionType.equals("radio")) {
                        for (int i = 0; i < arrayList.size(); i++) {
                            RadioButton radioButtonYes = holder.answerList.findViewWithTag(arrayList.get(i).getOption_id());
                            radioButtonYes.setChecked(false);
                        }

                        if (holder.naCheckBox.isChecked()) {
                            brandStandardQuestion.setAudit_answer_na(1);
                            holder.comment.setVisibility(View.INVISIBLE);
                            holder.comment.setText("");
                            for (int i = 0; i < arrayList.size(); i++) {
                                RadioButton radioButtonYes = holder.answerList.findViewWithTag(arrayList.get(i).getOption_id());
                                radioButtonYes.setEnabled(false);
                            }

                        } else {
                            brandStandardQuestion.setAudit_answer_na(0);
                            holder.comment.setVisibility(View.INVISIBLE);
                            holder.comment.setText("");
                            for (int i = 0; i < arrayList.size(); i++) {
                                RadioButton radioButtonYes = holder.answerList.findViewWithTag(arrayList.get(i).getOption_id());
                                radioButtonYes.setEnabled(true);
                            }
                        }
                    }else {
                        for (int i = 0; i < arrayList.size(); i++) {
                            CheckBox checkBox = holder.answerList.findViewWithTag(arrayList.get(i).getOption_id());
                            checkBox.setChecked(false);
                        }

                        if (holder.naCheckBox.isChecked()) {
                            brandStandardQuestion.setAudit_answer_na(1);
                            for (int i = 0; i < arrayList.size(); i++) {
                                CheckBox checkBox = holder.answerList.findViewWithTag(arrayList.get(i).getOption_id());
                                checkBox.setEnabled(false);
                            }

                        } else {
                            brandStandardQuestion.setAudit_answer_na(0);
                            for (int i = 0; i < arrayList.size(); i++) {
                                CheckBox checkBox = holder.answerList.findViewWithTag(arrayList.get(i).getOption_id());
                                checkBox.setEnabled(true);
                            }
                        }
                    }
                }
            });

            if (status.equals("3")) {
                if (AppUtils.isStringEmpty(brandStandardQuestion.getReviewer_answer_comment())) {
                    if (questionType.equals("radio")) {
                        *//*for (int j = 0; j < arrayList.size(); j++) {
                            //RadioButton radioButton = holder.answerList.findViewWithTag(arrayList.get(i).getOption_id());
                            RadioButton radioButton = holder.answerList.findViewById(j);
                            radioButton.setEnabled(false);
                        }*//*
                        rbBrandStandardButton.setEnabled(false);
                        radioText.setTextColor(context.getResources().getColor(R.color.textGrey));
                        holder.comment.setEnabled(false);
                        holder.naCheckBox.setEnabled(false);
                    } else {
                        *//*for (int j = 0; j < arrayList.size(); j++) {
                            //CheckBox checkBox = holder.answerList.findViewWithTag(arrayList.get(i).getOption_id());
                            CheckBox checkBox = holder.answerList.findViewById(j);
                            checkBox.setEnabled(false);
                        }*//*
                        cbBrandStandardAnswer.setEnabled(false);
                        cbBrandStandardAnswer.setTextColor(context.getResources().getColor(R.color.textGrey));
                        holder.comment.setEnabled(false);
                        holder.naCheckBox.setEnabled(false);
                    }
                }
            }

            holder.answerList.addView(view);
        }

    }*/

}
