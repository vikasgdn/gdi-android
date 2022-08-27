package com.gdi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.gdi.hotel.mystery.audits.R;
import com.gdi.interfaces.OnRecyclerViewItemClickListener;
import com.gdi.model.audit.BrandStandard.BrandStandardSectionNew;

import java.util.List;

public class SubSectionTabAdapterNew extends RecyclerView.Adapter<SubSectionTabAdapterNew.SubSectionTabViewHolder> {

    private Context context;
    private List<BrandStandardSectionNew> data;
    private String editable = "";
    private OnRecyclerViewItemClickListener recyclerViewItemClickListener;

    public SubSectionTabAdapterNew(Context context, List<BrandStandardSectionNew> data, String editable, OnRecyclerViewItemClickListener customItemClickListener) {
        this.context = context;
        this.data = data;
        this.editable = editable;
        this.recyclerViewItemClickListener = customItemClickListener;

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

        final BrandStandardSectionNew brandStandardSection = data.get(position);

        holder.tvSubSectionTitle.setText(brandStandardSection.section_title);

        holder.tvQuestionCount.setText("Question: "+brandStandardSection.submitted_question_count+"/"+brandStandardSection.total_question_count );

        if (editable.equals("0")) {
            holder.naCheckBox.setEnabled(true);
        } else {
            holder.naCheckBox.setEnabled(false);
        }

        if (brandStandardSection.section_status==0) {
            holder.tvSubSectionStatus.setText("Pending");
            //holder.llSubSectionBorder.setBackground(context.getResources().getDrawable(R.drawable.resume_status_border));
            holder.tvSubSectionIcon.setImageDrawable(context.getResources().getDrawable(R.mipmap.pending_status));
            holder.tvSubSectionStatus.setTextColor(context.getResources().getColor(R.color.colorOrange));

        } else if (brandStandardSection.section_status == 1) {
            holder.tvSubSectionStatus.setText("Start");
            //holder.llSubSectionBorder.setBackground(context.getResources().getDrawable(R.drawable.rejected_status_border));
            holder.tvSubSectionIcon.setImageDrawable(context.getResources().getDrawable(R.mipmap.start_status));
            holder.tvSubSectionStatus.setTextColor(context.getResources().getColor(R.color.statusRed));
        }
        else if (brandStandardSection.section_status == 2)
        {
            holder.tvSubSectionStatus.setText("Pending");
            //holder.llSubSectionBorder.setBackground(context.getResources().getDrawable(R.drawable.resume_status_border));
            holder.tvSubSectionIcon.setImageDrawable(context.getResources().getDrawable(R.mipmap.pending_status));
            holder.tvSubSectionStatus.setTextColor(context.getResources().getColor(R.color.colorOrange));
        }

        else {
            holder.tvSubSectionStatus.setText("Completed");
            //holder.llSubSectionBorder.setBackground(context.getResources().getDrawable(R.drawable.submitted_status_border));
            holder.tvSubSectionIcon.setImageDrawable(context.getResources().getDrawable(R.mipmap.complete_status));
            holder.tvSubSectionStatus.setTextColor(context.getResources().getColor(R.color.scoreGreen));
        }





    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class SubSectionTabViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView tvSubSectionTitle;
        TextView tvQuestionText;
        TextView tvSubSectionStatus;
        ImageView tvSubSectionIcon;
        TextView tvQuestionCount;
        LinearLayout llSubSectionBorder;
        CheckBox naCheckBox;

        public SubSectionTabViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            tvSubSectionTitle = itemView.findViewById(R.id.tv_sub_section_title);
            tvQuestionText = itemView.findViewById(R.id.tv_question_text);
            tvSubSectionStatus = itemView.findViewById(R.id.tv_sub_section_status);
            tvSubSectionIcon = itemView.findViewById(R.id.iv_sub_section_icon);
            tvQuestionCount = itemView.findViewById(R.id.tv_question_count);
            llSubSectionBorder = itemView.findViewById(R.id.ll_sub_section_border);
            naCheckBox = itemView.findViewById(R.id.cb_brand_standard_na);
        }

        @Override
        public void onClick(View v) {
            recyclerViewItemClickListener.onItemClick(SubSectionTabAdapterNew.this, v, getLayoutPosition());

        }
    }



}
