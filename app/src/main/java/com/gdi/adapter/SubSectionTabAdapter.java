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
import com.gdi.model.audit.AuditInfo;

import java.util.ArrayList;

public class SubSectionTabAdapter extends
        RecyclerView.Adapter<SubSectionTabAdapter.SubSectionTabViewHolder> {

    private Context context;
    private ArrayList<AuditInfo> data;

    public SubSectionTabAdapter(Context context, ArrayList<AuditInfo> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public SubSectionTabViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.audit_action_layout,
                parent, false);

        return new SubSectionTabViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SubSectionTabViewHolder holder, final int position) {
        //TODO : Static data testing

        final AuditInfo auditInfo = data.get(position);

        holder.llSubSectionBorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startAudit = new Intent(context, AuditSectionsActivity.class);
                startAudit.putExtra("brandName", auditInfo.getBrand_name());
                startAudit.putExtra("locationName", auditInfo.getLocation_title());
                startAudit.putExtra("auditName", auditInfo.getAudit_name());
                startAudit.putExtra("auditId", "" + auditInfo.getAudit_id());
                startAudit.putExtra("bsStatus", "" + auditInfo.getBrand_std_status());
                startAudit.putExtra("esStatus", "" + auditInfo.getExec_sum_status());
                startAudit.putExtra("dsStatus", "" + auditInfo.getDetailed_sum_status());
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
