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


public class BrandStandardAuditAdapter extends
        RecyclerView.Adapter<BrandStandardAuditAdapter.BrandStandardAuditViewHolder> {

    private Context context;
    private ArrayList<BrandStandardQuestion> data;
    CustomItemClickListener customItemClickListener;
    private String editable;
    private String status;
    private boolean checked = false;


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
        ((BrandStandardAuditActivity)context).questionCount = ((BrandStandardAuditActivity)context).questionCount + 1;
        holder.questionTitle.setText("" + ((BrandStandardAuditActivity)context).questionCount + ". " + brandStandardQuestion.getQuestion_title());
        holder.tvBrandStandardAttachCount.setText("" + brandStandardQuestion.getAudit_question_file_cnt());

        /*if (AppUtils.isStringEmpty(brandStandardQuestion.getAudit_comment())){
            holder.comment.setVisibility(View.VISIBLE);
            //holder.comment.setMinLines(1);
        }else {
            holder.comment.setVisibility(View.VISIBLE);
            //holder.comment.setMinLines(2);
            holder.comment.setText(brandStandardQuestion.getAudit_comment());
        }*/

        if (!AppUtils.isStringEmpty(brandStandardQuestion.getHint())){
            holder.hintLayout.setVisibility(View.VISIBLE);
            holder.note.setText(brandStandardQuestion.getHint());
        }else {
            holder.hintLayout.setVisibility(View.GONE);
        }

        if (AppUtils.isStringEmpty(brandStandardQuestion.getReviewer_answer_comment())){
            holder.rejectedComment.setVisibility(View.INVISIBLE);

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

        //brandStandardQuestion.setAudit_comment(holder.comment.getText().toString());

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
                if (!AppUtils.isStringEmpty(brandStandardQuestion.getQuestion_type()) && brandStandardQuestion.getQuestion_type().equals("textarea")){
                    if (brandStandardQuestion.getAudit_answer_na() == 0) {
                        AppLogger.e("AuditCommment", "" + editable.toString());
                        brandStandardQuestion.setAudit_answer("" + editable.toString());
                    }else {
                        AppLogger.e("AuditCommment", "" + editable.toString());
                        brandStandardQuestion.setAudit_comment("" + editable.toString());
                    }
                }else {
                    AppLogger.e("AuditCommment", "" + editable.toString());
                    brandStandardQuestion.setAudit_comment("" + editable.toString());
                }
            }
        });
        /*AppLogger.e("AuditCommment", "" + holder.comment.getText().toString());
        brandStandardQuestion.setAudit_comment(holder.comment.getText().toString());*/

        addAnswerList(brandStandardQuestion, holder,
                brandStandardQuestion.getQuestion_type(), brandStandardQuestion.getAudit_option_id());

        /*if (brandStandardQuestion.getAudit_answer_na() == 1) {
            holder.naBtn.setBackground(context.getResources().getDrawable(R.drawable.brand_standard_na_select_btn));
            holder.naBtn.setTextColor(context.getResources().getColor(R.color.colorWhite));
            //holder.naCheckBox.setChecked(true);
        } else {
            //holder.naCheckBox.setChecked(false);
            holder.naBtn.setBackground(context.getResources().getDrawable(R.drawable.brand_standard_unselect_btn));
            holder.naBtn.setTextColor(context.getResources().getColor(R.color.colorBlack));
        }*/


        holder.brandStandardAddFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(holder.questionTitle.getText().toString().substring(0,
                        holder.questionTitle.getText().toString().indexOf(".")));
                AppLogger.e("Count", ""+ count);
                AppLogger.e("position", ""+ position);
                AppLogger.e("Count_position", ""+ (count-position-1));
                customItemClickListener.onItemClick(count-1,
                        BrandStandardAuditAdapter.this ,brandStandardQuestion.getQuestion_id(), "bsQuestion", position);

            }
        });

        if (!AppUtils.isStringEmpty(brandStandardQuestion.getQuestion_type()) && brandStandardQuestion.getQuestion_type().equals("textarea")){
            if (brandStandardQuestion.getAudit_answer_na() == 0) {
                holder.naBtn.setBackground(context.getResources().getDrawable(R.drawable.brand_standard_unselect_btn));
                holder.naBtn.setTextColor(context.getResources().getColor(R.color.colorBlack));
            }else {
                holder.naBtn.setBackground(context.getResources().getDrawable(R.drawable.brand_standard_na_select_btn));
                holder.naBtn.setTextColor(context.getResources().getColor(R.color.colorWhite));
            }
            holder.comment.setVisibility(View.VISIBLE);
            if (!AppUtils.isStringEmpty(brandStandardQuestion.getAudit_answer())){
                holder.comment.setText(brandStandardQuestion.getAudit_answer());
            }
            holder.naBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (brandStandardQuestion.getAudit_answer_na() == 1){
                        holder.naBtn.setBackground(context.getResources().getDrawable(R.drawable.brand_standard_unselect_btn));
                        holder.naBtn.setTextColor(context.getResources().getColor(R.color.colorBlack));
                        brandStandardQuestion.setAudit_answer_na(0);
                    }else {
                        holder.naBtn.setBackground(context.getResources().getDrawable(R.drawable.brand_standard_na_select_btn));
                        holder.naBtn.setTextColor(context.getResources().getColor(R.color.colorWhite));
                        brandStandardQuestion.setAudit_answer_na(1);
                        holder.comment.setText("");
                    }
                }
            });
        }else {
            if (!AppUtils.isStringEmpty(brandStandardQuestion.getAudit_comment())){
                holder.comment.setVisibility(View.VISIBLE);
                holder.comment.setText(brandStandardQuestion.getAudit_comment());
            }
        }
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
        //CheckBox naCheckBox;
        TextView naBtn;
        public TextView tvBrandStandardAttachCount;
        Button brandStandardAddFile;
        Button addBtn;
        LinearLayout noteLayout;
        LinearLayout optionListLinearLayout;
        LinearLayout bsLayout;
        RelativeLayout btnLayout;
        LinearLayout hintLayout;


        public BrandStandardAuditViewHolder(View itemView) {
            super(itemView);

            questionTitle = itemView.findViewById(R.id.tv_bs_title);
            note = itemView.findViewById(R.id.tv_bs_note);
            hintLayout = itemView.findViewById(R.id.ll_note_layout);
            rejectedComment = itemView.findViewById(R.id.tv_bs_rejected_comment);
            comment = itemView.findViewById(R.id.tv_bs_comment);
            referenceImageTab = itemView.findViewById(R.id.bs_reference_image_tab);
            optionListLinearLayout = itemView.findViewById(R.id.rv_brand_standard_answer);
            naBtn = itemView.findViewById(R.id.tv_na_btn);
            addBtn = itemView.findViewById(R.id.bs_add_btn);
            brandStandardAddFile = itemView.findViewById(R.id.bs_add_file_btn);
            tvBrandStandardAttachCount = itemView.findViewById(R.id.bs_attachment_count);
            //noteLayout = itemView.findViewById(R.id.ll_note_layout);
            bsLayout = itemView.findViewById(R.id.bs_layout);
            btnLayout = itemView.findViewById(R.id.reference_btn_layout);


        }
    }

    private void enableView(BrandStandardAuditViewHolder holder) {
        holder.naBtn.setEnabled(true);
        holder.comment.setEnabled(true);
        holder.addBtn.setText("+");
    }

    private void disableView(BrandStandardAuditViewHolder holder) {
        holder.naBtn.setEnabled(false);
        holder.comment.setEnabled(false);
        holder.addBtn.setText("");
    }

    private void addAnswerList(final BrandStandardQuestion brandStandardQuestion,
                               final BrandStandardAuditViewHolder holder, final String questionType,
                               final ArrayList<Integer> answerOptionId) {

        final ArrayList<BrandStandardQuestionsOption> arrayList = new ArrayList<>();
        arrayList.addAll(brandStandardQuestion.getOptions());
        holder.optionListLinearLayout.removeAllViews();

        for (int i = 0; i < arrayList.size(); i++) {

            final BrandStandardQuestionsOption brandStandardQuestionsOption = arrayList.get(i);
            final View view = ((BrandStandardAuditActivity) context)
                    .inflater.inflate(R.layout.brand_standard_audit_layout3, null);

            final TextView answerText = view.findViewById(R.id.radio_text);

            if (editable.equals("0")) {
                answerText.setEnabled(true);
            } else {
                answerText.setEnabled(false);
            }

            if (brandStandardQuestion.getAudit_answer_na() == 1) {
                holder.naBtn.setBackground(context.getResources().getDrawable(R.drawable.brand_standard_na_select_btn));
                holder.naBtn.setTextColor(context.getResources().getColor(R.color.colorWhite));
                answerText.setBackground(context.getResources().getDrawable(R.drawable.brand_standard_unselect_btn));
                answerText.setEnabled(false);
                //holder.naCheckBox.setChecked(true);
            } else {
                //holder.naCheckBox.setChecked(false);
                holder.naBtn.setBackground(context.getResources().getDrawable(R.drawable.brand_standard_unselect_btn));
                holder.naBtn.setTextColor(context.getResources().getColor(R.color.colorBlack));
                answerText.setBackground(context.getResources().getDrawable(R.drawable.brand_standard_btn_border));
                answerText.setEnabled(true);
            }

            answerText.setText(String.valueOf(brandStandardQuestionsOption.getOption_text()));
            if (questionType.equals("radio")) {

                if (brandStandardQuestion.getAudit_answer_na() == 0) {
                    if (brandStandardQuestionsOption.getSelected() == 1) {
                        if (brandStandardQuestionsOption.getOption_mark() == 1) {
                            answerText.setBackground(context.getResources().getDrawable(R.drawable.brand_standard_select_btn));
                            answerText.setTextColor(context.getResources().getColor(R.color.colorWhite));
                        } else {
                            answerText.setBackground(context.getResources().getDrawable(R.drawable.brand_standard_select_no_btn));
                            answerText.setTextColor(context.getResources().getColor(R.color.colorWhite));
                        }
                    }
                }

                answerText.setTag(brandStandardQuestionsOption.getOption_id());
                answerText.setId(i);

                answerText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.comment.setVisibility(View.VISIBLE);
                        //holder.comment.setMinLines(2);
                        holder.comment.setText("");
                        int optionId = brandStandardQuestionsOption.getOption_id();
                        for (int j = 0; j < arrayList.size(); j++) {
                            TextView radio_text = holder.optionListLinearLayout.findViewById(j);
                            if (radio_text.equals(answerText)){
                                answerOptionId.clear();
                                answerOptionId.add(optionId);
                                //holder.comment.setText("");
                                if (brandStandardQuestionsOption.getOption_mark() == 1){
                                    answerText.setBackground(context.getResources().getDrawable(R.drawable.brand_standard_select_btn));
                                    answerText.setTextColor(context.getResources().getColor(R.color.colorWhite));
                                }else {
                                    answerText.setBackground(context.getResources().getDrawable(R.drawable.brand_standard_select_no_btn));
                                    answerText.setTextColor(context.getResources().getColor(R.color.colorWhite));
                                }
                            }else {
                                radio_text.setBackground(context.getResources().getDrawable(R.drawable.brand_standard_btn_border));
                                radio_text.setTextColor(context.getResources().getColor(R.color.colorBlack));
                            }
                        }

                       /* answerOptionId.add((int)answerText.getTag());
                        AppLogger.e("selectedId", "" + answerOptionId.get(0));
                        AppLogger.e("OptionId", "" + brandStandardQuestionsOption.getOption_id());

                        holder.comment.setVisibility(View.VISIBLE);
                        for (int j = 0; j < arrayList.size(); j++) {
                            TextView radio_text = holder.optionListLinearLayout.findViewById(j);
                            if (radio_text.equals(answerText)) {
                                holder.comment.setText("");
                                answerOptionId.clear();
                                answerOptionId.add(optionId);
                                AppLogger.e("selectedId", "" + answerOptionId.get(0));
                                radio_text.setBackground(context.getResources().getDrawable(R.drawable.brand_standard_select_btn));
                                //radioButton.setChecked(true);
                            } else {
                                radio_text.setBackground(context.getResources().getDrawable(R.drawable.brand_standard_btn_border));
                                //radioButton.setChecked(false);

                            }
                        }*/
                        holder.naBtn.setBackground(context.getResources().getDrawable(R.drawable.brand_standard_unselect_btn));
                        holder.naBtn.setTextColor(context.getResources().getColor(R.color.colorBlack));
                        brandStandardQuestion.setAudit_answer_na(0);
                    }
                });

            } else {
                int optionId = brandStandardQuestionsOption.getOption_id();
                if (brandStandardQuestion.getAudit_answer_na() == 0) {
                    for (int j = 0; j < answerOptionId.size(); j++) {
                        if (optionId == answerOptionId.get(j)) {
                            answerText.setBackground(context.getResources().getDrawable(R.drawable.brand_standard_select_btn));
                            answerText.setTextColor(context.getResources().getColor(R.color.colorWhite));
                            for (int k = 0 ; k < arrayList.size() ; k++){
                                if (arrayList.get(k).getOption_id() == optionId){
                                    arrayList.get(k).setChecked(1);
                                    break;
                                }
                            }

                            break;
                        }
                    }
                }
                answerText.setTag(brandStandardQuestionsOption.getOption_id());
                answerText.setId(i);
                holder.comment.setVisibility(View.VISIBLE);
                answerText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AppLogger.e("OptionId", "" + brandStandardQuestionsOption.getOption_id());
                        int optionId = brandStandardQuestionsOption.getOption_id();
                        for (int j = 0; j < arrayList.size(); j++) {
                            TextView checkBoxText = holder.optionListLinearLayout.findViewById(j);

                            if (checkBoxText.equals(answerText)) {
                                if (arrayList.get(j).getChecked() == 0) {
                                    answerText.setBackground(context.getResources().getDrawable(R.drawable.brand_standard_select_btn));
                                    answerText.setTextColor(context.getResources().getColor(R.color.colorWhite));
                                    arrayList.get(j).setChecked(1);
                                    answerOptionId.add(optionId);
                                }else {
                                    answerText.setBackground(context.getResources().getDrawable(R.drawable.brand_standard_btn_border));
                                    answerText.setTextColor(context.getResources().getColor(R.color.colorBlack));
                                    arrayList.get(j).setChecked(0);
                                    //answerOptionId.remove(new Integer(optionId));
                                    for (int k = 0 ; k < answerOptionId.size() ; k++){
                                        if (answerOptionId.get(k) == optionId){
                                            answerOptionId.remove(k);
                                            break;
                                        }
                                    }
                                }
                            /*if(checkBox.isChecked()){
                                answerOptionId.add(optionId);
                            }else answerOptionId.remove(new Integer(optionId));
                            AppLogger.e("selectedId", "" + answerOptionId.get(0));
                            checkBox.setChecked(true);*/
                            }
                        }
                    }
                });
            }

            holder.naBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.comment.setText("");
                    if (brandStandardQuestion.getAudit_answer_na() == 1){
                        holder.comment.setVisibility(View.VISIBLE);
                        //holder.comment.setMinLines(1);
                        //answerOptionId.clear();
                        holder.naBtn.setBackground(context.getResources().getDrawable(R.drawable.brand_standard_unselect_btn));
                        holder.naBtn.setTextColor(context.getResources().getColor(R.color.colorBlack));
                        brandStandardQuestion.setAudit_answer_na(0);
                        if (questionType.equals("radio")) {
                            for (int i = 0; i < arrayList.size(); i++) {
                                TextView textRadio = holder.optionListLinearLayout.findViewWithTag(arrayList.get(i).getOption_id());
                                textRadio.setBackground(context.getResources().getDrawable(R.drawable.brand_standard_btn_border));
                                textRadio.setTextColor(context.getResources().getColor(R.color.colorBlack));
                                //textRadio.setEnabled(true);
                            }
                        }else {
                            for (int i = 0; i < arrayList.size(); i++) {
                                TextView textCheckBox = holder.optionListLinearLayout.findViewWithTag(arrayList.get(i).getOption_id());
                                textCheckBox.setBackground(context.getResources().getDrawable(R.drawable.brand_standard_btn_border));
                                textCheckBox.setTextColor(context.getResources().getColor(R.color.colorBlack));
                               // textCheckBox.setEnabled(true);
                            }
                        }
                    }else {
                        holder.naBtn.setBackground(context.getResources().getDrawable(R.drawable.brand_standard_na_select_btn));
                        holder.naBtn.setTextColor(context.getResources().getColor(R.color.colorWhite));
                        answerOptionId.clear();
                        brandStandardQuestion.setAudit_answer_na(1);
                        holder.comment.setVisibility(View.VISIBLE);
                        //holder.comment.setMinLines(2);
                        holder.comment.setText("");
                        if (questionType.equals("radio")) {
                            for (int i = 0; i < arrayList.size(); i++) {
                                TextView textRadio = holder.optionListLinearLayout.findViewWithTag(arrayList.get(i).getOption_id());
                                textRadio.setBackground(context.getResources().getDrawable(R.drawable.brand_standard_unselect_btn));
                                textRadio.setTextColor(context.getResources().getColor(R.color.colorBlack));
                                //textRadio.setEnabled(false);
                            }
                        }else {
                            for (int i = 0; i < arrayList.size(); i++) {
                                TextView textCheckBox = holder.optionListLinearLayout.findViewWithTag(arrayList.get(i).getOption_id());
                                textCheckBox.setBackground(context.getResources().getDrawable(R.drawable.brand_standard_unselect_btn));
                                textCheckBox.setTextColor(context.getResources().getColor(R.color.colorBlack));
                                arrayList.get(i).setChecked(0);
                                //textCheckBox.setEnabled(false);
                            }
                        }
                    }
                }
            });

            if (status.equals("3")) {
                if (AppUtils.isStringEmpty(brandStandardQuestion.getReviewer_answer_comment())) {
                    if (questionType.equals("radio")) {
                        for (int j = 0; j < arrayList.size(); j++) {
                            //RadioButton radioButton = holder.answerList.findViewWithTag(arrayList.get(i).getOption_id());
                            RadioButton radioButton = holder.optionListLinearLayout.findViewById(j);
                            radioButton.setEnabled(false);
                        }
                        answerText.setEnabled(false);
                        answerText.setTextColor(context.getResources().getColor(R.color.colorWhite));
                        holder.comment.setEnabled(false);
                        holder.naBtn.setEnabled(false);
                    } else {
                        for (int j = 0; j < arrayList.size(); j++) {
                            //CheckBox checkBox = holder.answerList.findViewWithTag(arrayList.get(i).getOption_id());
                            CheckBox checkBox = holder.optionListLinearLayout.findViewById(j);
                            checkBox.setEnabled(false);
                        }
                        answerText.setEnabled(false);
                        answerText.setTextColor(context.getResources().getColor(R.color.colorWhite));
                        holder.comment.setEnabled(false);
                        holder.naBtn.setEnabled(false);
                    }
                }
            }

            holder.optionListLinearLayout.addView(view);
        }

    }



    public ArrayList<BrandStandardQuestion> getArrayList() {
        return data;
    }

    public interface CustomItemClickListener {
        void onItemClick(int count,BrandStandardAuditAdapter brandStandardAuditAdapter, int bsQuestionId, String attachtype, int position);
    }

    public void setattachmentCount(int count, int pos){

        data.get(pos).setAudit_question_file_cnt(count);
        //notifyDataSetChanged();
        notifyItemChanged(pos);
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
