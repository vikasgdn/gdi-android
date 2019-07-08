package com.gdi.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.activity.Audit.AuditSectionsActivity;
import com.gdi.activity.Audit.BrandStandardAuditActivity;
import com.gdi.activity.Audit.SubSectionsActivity;
import com.gdi.model.audit.AuditInfo;
import com.gdi.model.audit.BrandStandard.BrandStandardQuestion;
import com.gdi.model.audit.BrandStandard.BrandStandardSection;
import com.gdi.model.audit.BrandStandard.BrandStandardSubSection;
import com.gdi.utils.AppUtils;

import java.util.ArrayList;

public class SubSectionTabAdapter extends
        RecyclerView.Adapter<SubSectionTabAdapter.SubSectionTabViewHolder> {

    private Context context;
    private ArrayList<BrandStandardSection> data;
    CustomItemClickListener customItemClickListener;
    private String editable = "";
    /*private int totalCount;
    private int count;
    private int isPartiallyFilled;*/
    //private boolean isPartiallyFilled = false;

    public SubSectionTabAdapter(Context context, ArrayList<BrandStandardSection> data, String editable,
                                CustomItemClickListener customItemClickListener) {
        this.context = context;
        this.data = data;
        this.editable = editable;
        this.customItemClickListener = customItemClickListener;
        /*this.totalCount = totalCount;
        this.count = count;
        this.isPartiallyFilled = isPartiallyFilled;*/
    }

    @Override
    public SubSectionTabViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_section_tab_layout,
                parent, false);

        return new SubSectionTabViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SubSectionTabViewHolder holder, final int position) {
        //TODO : Static data testing

        final BrandStandardSection brandStandardSection = data.get(position);
        int result[] = questionCount(brandStandardSection);
        int totalCount = result[0];
        int count = result[1];
        int isPartiallyFilled = result[2];
        int naFilled = result[3];
        holder.tvSubSectionTitle.setText(brandStandardSection.getSection_title());

        holder.tvQuestionCount.setText("Question: " + count + "/" + totalCount);

        if (editable.equals("0")){
            holder.naCheckBox.setEnabled(true);
        }else {
            holder.naCheckBox.setEnabled(false);
        }

        if (isPartiallyFilled == 1 || count < totalCount && count != 0) {
            holder.tvSubSectionStatus.setText("Pending");
            //holder.llSubSectionBorder.setBackground(context.getResources().getDrawable(R.drawable.resume_status_border));
            holder.tvSubSectionIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.pending_status));
            holder.tvSubSectionStatus.setTextColor(context.getResources().getColor(R.color.colorOrange));

        }else if (count == 0){
            holder.tvSubSectionStatus.setText("Start");
            //holder.llSubSectionBorder.setBackground(context.getResources().getDrawable(R.drawable.rejected_status_border));
            holder.tvSubSectionIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.start_status));
            holder.tvSubSectionStatus.setTextColor(context.getResources().getColor(R.color.statusRed));
        }else {
            holder.tvSubSectionStatus.setText("Completed");
            //holder.llSubSectionBorder.setBackground(context.getResources().getDrawable(R.drawable.submitted_status_border));
            holder.tvSubSectionIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.complete_status));
            holder.tvSubSectionStatus.setTextColor(context.getResources().getColor(R.color.scoreGreen));
        }

        if (naFilled == totalCount){
            holder.naCheckBox.setChecked(true);
        }

        holder.llSubSectionBorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*customItemClickListener.onItemClick(brandStandardSection.getQuestions(),
                        brandStandardSection.getSub_sections(),
                        brandStandardSection.getSection_group_id(),
                        brandStandardSection.getSection_id(),
                        brandStandardSection.getSection_title(),
                        brandStandardSection.getAudit_section_file_cnt());*/

                customItemClickListener.onItemClick(brandStandardSection,
                        brandStandardSection.getAudit_section_file_cnt());




            }
        });



        holder.naCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.naCheckBox.isChecked()){
                    holder.tvSubSectionStatus.setText("Completed");
                    //holder.llSubSectionBorder.setBackground(context.getResources().getDrawable(R.drawable.submitted_status_border));
                    holder.tvSubSectionIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.complete_status));
                    holder.tvSubSectionStatus.setTextColor(context.getResources().getColor(R.color.scoreGreen));
                    if (brandStandardSection.getQuestions() != null && brandStandardSection.getQuestions().size() > 0) {
                        for (int i = 0 ; i < brandStandardSection.getQuestions().size(); i++){
                            BrandStandardQuestion question = brandStandardSection.getQuestions().get(i);
                            question.setAudit_option_id(new ArrayList<Integer>());
                            question.setAudit_answer_na(1);
                        }
                    }
                    if (brandStandardSection.getSub_sections() != null && brandStandardSection.getSub_sections().size() > 0) {
                        for (int i = 0 ; i < brandStandardSection.getSub_sections().size(); i++){
                            BrandStandardSubSection subSection = brandStandardSection.getSub_sections().get(i);
                            if (subSection.getQuestions() != null && subSection.getQuestions().size() > 0) {
                                for (int j = 0 ; j < subSection.getQuestions().size(); j++){
                                    BrandStandardQuestion question = subSection.getQuestions().get(j);
                                    question.setAudit_option_id(new ArrayList<Integer>());
                                    question.setAudit_answer_na(1);
                                }
                            }
                        }
                    }
                }else {
                    holder.tvSubSectionStatus.setText("Start");
                    //holder.llSubSectionBorder.setBackground(context.getResources().getDrawable(R.drawable.rejected_status_border));
                    holder.tvSubSectionIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.start_status));
                    holder.tvSubSectionStatus.setTextColor(context.getResources().getColor(R.color.statusRed));
                    if (brandStandardSection.getQuestions() != null && brandStandardSection.getQuestions().size() > 0) {
                        for (int i = 0 ; i < brandStandardSection.getQuestions().size(); i++){
                            BrandStandardQuestion question = brandStandardSection.getQuestions().get(i);
                            question.setAudit_answer_na(0);
                        }
                    }
                    if (brandStandardSection.getSub_sections() != null && brandStandardSection.getSub_sections().size() > 0) {
                        for (int i = 0 ; i < brandStandardSection.getSub_sections().size(); i++){
                            BrandStandardSubSection subSection = brandStandardSection.getSub_sections().get(i);
                            if (subSection.getQuestions() != null && subSection.getQuestions().size() > 0) {
                                for (int j = 0 ; j < subSection.getQuestions().size(); j++){
                                    BrandStandardQuestion question = subSection.getQuestions().get(j);
                                    question.setAudit_answer_na(0);
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class SubSectionTabViewHolder extends RecyclerView.ViewHolder {


        TextView tvSubSectionTitle;
        TextView tvQuestionText;
        TextView tvSubSectionStatus;
        ImageView tvSubSectionIcon;
        TextView tvQuestionCount;
        LinearLayout llSubSectionBorder;
        CheckBox naCheckBox;

        public SubSectionTabViewHolder (View itemView) {
            super(itemView);

            tvSubSectionTitle = itemView.findViewById(R.id.tv_sub_section_title);
            tvQuestionText = itemView.findViewById(R.id.tv_question_text);
            tvSubSectionStatus = itemView.findViewById(R.id.tv_sub_section_status);
            tvSubSectionIcon = itemView.findViewById(R.id.iv_sub_section_icon);
            tvQuestionCount = itemView.findViewById(R.id.tv_question_count);
            llSubSectionBorder = itemView.findViewById(R.id.ll_sub_section_border);
            naCheckBox = itemView.findViewById(R.id.cb_brand_standard_na);
        }
    }

    private int[] questionCount(BrandStandardSection brandStandardSection){
        int totalCount = 0;
        int count = 0;
        int isPartiallyFilled = 0;
        int naFilled = 0;

        //totalCount = brandStandardSection.getQuestions().size();

        for (int i = 0; i < brandStandardSection.getQuestions().size(); i++){
            if (brandStandardSection.getQuestions().get(i).getAudit_option_id().size() != 0
                    || brandStandardSection.getQuestions().get(i).getAudit_answer_na() == 1
                    || !AppUtils.isStringEmpty(brandStandardSection.getQuestions().get(i).getAudit_answer())){
                count+=1;
            }
            if (brandStandardSection.getQuestions().get(i).getAudit_answer_na() == 1){
                naFilled+=1;
            }
            if (brandStandardSection.getQuestions().get(i).getAnswer_status() == 3) {
                isPartiallyFilled = 1;
            }
            /*if (brandStandardSection.getQuestions().get(i).getQuestion_type().equals("textarea")){
                if (!AppUtils.isStringEmpty(brandStandardSection.getQuestions().get(i).getAudit_answer())){
                    count+=1;
                }
                if (brandStandardSection.getQuestions().get(i).getAudit_answer_na() == 1
                        && !AppUtils.isStringEmpty(brandStandardSection.getQuestions().get(i).getAudit_comment())){
                    count-=1;
                }
            }*/
            totalCount += 1;

        }

        for (int i = 0; i < brandStandardSection.getSub_sections().size(); i++){
            for (int j = 0 ; j < brandStandardSection.getSub_sections().get(i).getQuestions().size() ; j++) {
                if (brandStandardSection.getSub_sections().get(i).getQuestions().get(j).getAudit_option_id().size() != 0
                        || brandStandardSection.getSub_sections().get(i).getQuestions().get(j).getAudit_answer_na() == 1
                        || !AppUtils.isStringEmpty(brandStandardSection.getSub_sections().get(i).getQuestions().get(j).getAudit_answer())) {
                    count+=1;

                }
                if (brandStandardSection.getSub_sections().get(i).getQuestions().get(j).getAudit_answer_na() == 1){
                    naFilled+=1;
                }
                if (brandStandardSection.getSub_sections().get(i).getQuestions().get(j).getAnswer_status() == 3) {
                    isPartiallyFilled = 1;
                }
                /*if (brandStandardSection.getSub_sections().get(i).getQuestions().get(j).getQuestion_type().equals("textarea")){
                    if (!AppUtils.isStringEmpty(brandStandardSection.getSub_sections().get(i).getQuestions().get(j).getAudit_answer())){
                        count+=1;
                    }
                }*/
                totalCount += 1;

            }
        }

        return new int[]{totalCount, count, isPartiallyFilled, naFilled};
    }

    public interface CustomItemClickListener {
        /*void onItemClick(ArrayList<BrandStandardQuestion> questionArrayList,
                         ArrayList<BrandStandardSubSection> subSectionArrayList,
                         int sectionGroupId, int sectionId, String sectionTitle, int fileCount);*/
        void onItemClick(BrandStandardSection brandStandardSection, int fileCount);
    }

}
