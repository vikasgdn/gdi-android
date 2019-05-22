package com.gdi.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.model.actionplan.ActionPlanModel;
import com.gdi.model.dashboard.IAMainDashboardInfo;
import com.gdi.utils.AppUtils;

import java.util.ArrayList;

public class InternalAuditDashboardAdapter extends RecyclerView.Adapter<InternalAuditDashboardAdapter.InternalAuditDashboardViewHolder> {

    private Context context;
    private ArrayList<IAMainDashboardInfo> orderData;

    public InternalAuditDashboardAdapter(Context context, ArrayList<IAMainDashboardInfo> orderData) {
        this.context = context;
        this.orderData = orderData;
    }

    @NonNull
    @Override
    public InternalAuditDashboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.internal_audit_dashboard_layout,
                parent, false);

        return new InternalAuditDashboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InternalAuditDashboardViewHolder holder, int position) {
        IAMainDashboardInfo iaMainDashboardInfo = orderData.get(position);
        holder.auditId.setText(iaMainDashboardInfo.getAudit_id());
        holder.hotelName.setText(iaMainDashboardInfo.getLocation_title());
        holder.auditname.setText(iaMainDashboardInfo.getAudit_name());
        holder.dueDate.setText(iaMainDashboardInfo.getAudit_due_date());
        if(iaMainDashboardInfo.getReport_status()==1){
            holder.status.setTextColor(context.getResources().getColor(R.color.scoreGreen));
            holder.status.setText("Completed");
        }else {
            holder.status.setTextColor(Color.RED);
            holder.status.setText("Pending");
        }

        holder.auditorname.setText(iaMainDashboardInfo.getAuditor_fname()
                + " "+iaMainDashboardInfo.getAuditor_lname());


    }

    @Override
    public int getItemCount() {
        return orderData.size();
    }

    public class InternalAuditDashboardViewHolder extends RecyclerView.ViewHolder {

        TextView auditId;
        TextView hotelName;
        TextView auditname;
        TextView dueDate;
        TextView status;
        TextView auditorname;

        public InternalAuditDashboardViewHolder(View itemView) {
            super(itemView);

            auditId = (TextView)itemView.findViewById(R.id.tv_audit_id);
            hotelName = (TextView)itemView.findViewById(R.id.tv_hotel_location);
            auditname = (TextView)itemView.findViewById(R.id.tv_audit_name);
            dueDate = (TextView)itemView.findViewById(R.id.tv_due_date);
            status = (TextView)itemView.findViewById(R.id.tv_status);
            auditorname = (TextView)itemView.findViewById(R.id.tv_auditor_name);
        }
    }
}
