package com.gdi.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.util.ArrayList;

public class SubSectionTabAdapter extends
        RecyclerView.Adapter<SubSectionTabAdapter.SubSectionTabViewHolder> {

    private Context context;
    private ArrayList<BrandStandardSection> data;
    CustomItemClickListener customItemClickListener;
    /*private int totalCount;
    private int count;
    private int isPartiallyFilled;*/
    //private boolean isPartiallyFilled = false;

    public SubSectionTabAdapter(Context context, ArrayList<BrandStandardSection> data,
                                CustomItemClickListener customItemClickListener) {
        this.context = context;
        this.data = data;
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
        holder.tvSubSectionTitle.setText(brandStandardSection.getSection_title());

        holder.tvQuestionCount.setText("" + count + "/" + totalCount);

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

        public SubSectionTabViewHolder (View itemView) {
            super(itemView);

            tvSubSectionTitle = itemView.findViewById(R.id.tv_sub_section_title);
            tvQuestionText = itemView.findViewById(R.id.tv_question_text);
            tvSubSectionStatus = itemView.findViewById(R.id.tv_sub_section_status);
            tvSubSectionIcon = itemView.findViewById(R.id.iv_sub_section_icon);
            tvQuestionCount = itemView.findViewById(R.id.tv_question_count);
            llSubSectionBorder = itemView.findViewById(R.id.ll_sub_section_border);
        }
    }

    private int[] questionCount(BrandStandardSection brandStandardSection){
        int totalCount = 0;
        int count = 0;
        int isPartiallyFilled = 0;

        //totalCount = brandStandardSection.getQuestions().size();

        for (int i = 0; i < brandStandardSection.getQuestions().size(); i++){
            if (brandStandardSection.getQuestions().get(i).getAudit_option_id().size() != 0
                    || brandStandardSection.getQuestions().get(i).getAudit_answer_na() == 1){
                count+=1;
            }
            if (brandStandardSection.getQuestions().get(i).getAnswer_status() == 3) {
                isPartiallyFilled = 1;
            }
            totalCount += 1;

        }

        for (int i = 0; i < brandStandardSection.getSub_sections().size(); i++){
            for (int j = 0 ; j < brandStandardSection.getSub_sections().get(i).getQuestions().size() ; j++) {
                if (brandStandardSection.getSub_sections().get(i).getQuestions().get(j).getAudit_option_id().size() != 0
                        || brandStandardSection.getSub_sections().get(i).getQuestions().get(j).getAudit_answer_na() == 1) {
                    count+=1;

                }
                if (brandStandardSection.getSub_sections().get(i).getQuestions().get(j).getAnswer_status() == 3) {
                    isPartiallyFilled = 1;
                }
                totalCount += 1;

            }
        }

        return new int[]{totalCount, count, isPartiallyFilled};
    }

    public interface CustomItemClickListener {
        /*void onItemClick(ArrayList<BrandStandardQuestion> questionArrayList,
                         ArrayList<BrandStandardSubSection> subSectionArrayList,
                         int sectionGroupId, int sectionId, String sectionTitle, int fileCount);*/
        void onItemClick(BrandStandardSection brandStandardSection, int fileCount);
    }

}
