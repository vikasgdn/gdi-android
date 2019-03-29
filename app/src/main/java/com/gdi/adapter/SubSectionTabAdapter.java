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
import com.gdi.model.audit.AuditInfo;
import com.gdi.model.audit.BrandStandard.BrandStandardSection;

import java.util.ArrayList;

public class SubSectionTabAdapter extends
        RecyclerView.Adapter<SubSectionTabAdapter.SubSectionTabViewHolder> {

    private Context context;
    private ArrayList<BrandStandardSection> data;

    public SubSectionTabAdapter(Context context, ArrayList<BrandStandardSection> data) {
        this.context = context;
        this.data = data;
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
        holder.tvSubSectionTitle.setText(brandStandardSection.getSection_title());
        //holder.tvQuestionCount.setText(brandStandardSection.getSection_title());
        //holder.tvSubSectionStatus.setText(brandStandardSection.getSection_title());

        holder.llSubSectionBorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startAudit = new Intent(context, BrandStandardAuditActivity.class);
                startAudit.putParcelableArrayListExtra("questions", brandStandardSection.getQuestions());
                startAudit.putParcelableArrayListExtra("subSectionQuestions", brandStandardSection.getSub_sections());
                context.startActivity(startAudit);
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
}
