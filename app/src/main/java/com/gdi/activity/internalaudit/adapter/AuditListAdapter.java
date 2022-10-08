package com.gdi.activity.internalaudit.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gdi.activity.internalaudit.AuditSubSectionsActivity;
import com.gdi.activity.internalaudit.model.audit.AuditInfo;
import com.gdi.hotel.mystery.audits.R;
import com.gdi.utils.AppConstant;
import com.gdi.utils.AppLogger;
import com.gdi.utils.AppUtils;
import com.timqi.sectorprogressview.SectorProgressView;

import java.util.List;


public class AuditListAdapter extends RecyclerView.Adapter<AuditListAdapter.AuditActionViewHolder> {

    private Context context;
    private List<AuditInfo> data;
    private int status;

    public AuditListAdapter(Context context, List<AuditInfo> data, int status) {
        this.context = context;
        this.data = data;
        this.status = status;
    }

    @Override
    public AuditActionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_auditlist, parent, false);
        return new AuditActionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AuditActionViewHolder holder, final int position) {
        //TODO : Static data testing

        final AuditInfo auditInfo = data.get(position);
        holder.mAuditTypeTV.setText(""+auditInfo.getAudit_type());
        holder.mAuditNameTV.setText(auditInfo.getAudit_name());
        holder.mLocationTV.setText(auditInfo.getLocation_title());
        holder.mTemplateTV.setText(""+auditInfo.getQuestionnaire_title());
        holder.mAuditorNameTV.setText(""+auditInfo.getAuditor_fname()+" "+auditInfo.getAuditor_lname());
        holder.mCompletePercentageTV.setText(""+Math.round(auditInfo.getCompletion_percent())+"%"+context.getResources().getString(R.string.text_complete));
        holder.mPieChartAuditPer.setPercent(auditInfo.getCompletion_percent());
        holder.mStatusTV.setSelected(true);
        holder.mAuditNameTV.setSelected(true);
        String dateOverDue=AppUtils.getFormatedDateDayMonth(auditInfo.getAudit_due_date());
        if (status==1){
            holder.mActionTV.setText(context.getResources().getString(R.string.text_start));
            holder.mActionTV.setTextColor(context.getResources().getColor(R.color.c_green));
            holder.mActionTV.setBackgroundResource(R.drawable.button_border_green);
            holder.mStatusTV.setTextColor(context.getResources().getColor(R.color.c_green));
            if (dateOverDue.equalsIgnoreCase("N/A"))
                holder.mStatusTV.setText(context.getResources().getString(R.string.text_overdue_days_notapplicable));
            else
               holder.mStatusTV.setText(context.getResources().getString(R.string.text_overdue_on)+" "+AppUtils.getFormatedDateDayMonth(auditInfo.getAudit_due_date()));
        }else if (status==2){
            holder.mActionTV.setText(context.getResources().getString(R.string.text_resume));
            holder.mActionTV.setTextColor(context.getResources().getColor(R.color.c_yellow));
            holder.mActionTV.setBackgroundResource(R.drawable.button_border_yello);
            holder.mStatusTV.setTextColor(context.getResources().getColor(R.color.c_yellow));
            if (dateOverDue.equalsIgnoreCase("N/A"))
                holder.mStatusTV.setText(context.getResources().getString(R.string.text_overdue_days_notapplicable));
            else
                holder.mStatusTV.setText(context.getResources().getString(R.string.text_overdue_on)+" "+AppUtils.getFormatedDateDayMonth(auditInfo.getAudit_due_date()));
        }else if (status==3){
            holder.mActionTV.setText(context.getResources().getString(R.string.s_overdue));
            holder.mActionTV.setTextColor(context.getResources().getColor(R.color.c_red));
            holder.mActionTV.setBackgroundResource(R.drawable.button_border_red);
            holder.mStatusTV.setTextColor(context.getResources().getColor(R.color.c_red));
            if(auditInfo.getOverdue_days()>1)
                holder.mStatusTV.setText(context.getResources().getString(R.string.text_overdue_by_days).replace("XXX",""+auditInfo.getOverdue_days()));
            else
                holder.mStatusTV.setText(context.getResources().getString(R.string.text_overdue_by_day).replace("XXX",""+auditInfo.getOverdue_days()));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startAudit = new Intent(context, AuditSubSectionsActivity.class);
                startAudit.putExtra(AppConstant.BRAND_NAME, auditInfo.getBrand_name());
                startAudit.putExtra(AppConstant.LOCATION_NAME, auditInfo.getLocation_title());
                startAudit.putExtra(AppConstant.AUDIT_NAME, auditInfo.getAudit_name());
                startAudit.putExtra(AppConstant.AUDIT_ID, "" + auditInfo.getAudit_id());
                startAudit.putExtra(AppConstant.BS_STATUS, "" + auditInfo.getBrand_std_status());
                AppLogger.e("bsStatus:", "-" + auditInfo.getAudit_status());
                context.startActivity(startAudit);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateStatus(int statusAudit)
    {
        status=statusAudit;
    }

    public class AuditActionViewHolder extends RecyclerView.ViewHolder {

        TextView mAuditTypeTV;
        TextView mTemplateTV;
        TextView mAuditNameTV;
        TextView mLocationTV;
        TextView mStatusTV;
        TextView mAuditorNameTV;
        TextView mActionTV;
        TextView mCompletePercentageTV;
        LinearLayout reviewerContainer;
        SectorProgressView mPieChartAuditPer;
        View itemView;

        public AuditActionViewHolder (View itemView) {
            super(itemView);

            this.itemView=itemView;
            mTemplateTV=itemView.findViewById(R.id.tv_template);
            mCompletePercentageTV= itemView.findViewById(R.id.tv_complete_per);
            mAuditNameTV = itemView.findViewById(R.id.tv_auditname);
            mAuditTypeTV = itemView.findViewById(R.id.tv_audittype);
            mLocationTV = itemView.findViewById(R.id.tv_location);
            mStatusTV = itemView.findViewById(R.id.tv_status);
            mActionTV = itemView.findViewById(R.id.tv_resume);
            mAuditorNameTV = itemView.findViewById(R.id.tv_auditor_name);
            mPieChartAuditPer = (SectorProgressView) itemView.findViewById(R.id.sv_complete_per);


        }
    }

}
