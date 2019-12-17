package com.gdi.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.model.actionplan.ActionPlanModel;

import java.util.ArrayList;

public class ActionPlanAdapter extends RecyclerView.Adapter<ActionPlanAdapter.ActionPlanViewHolder3> {

    private Context context;
    private ArrayList<ActionPlanModel> orderData;

    public ActionPlanAdapter(Context context, ArrayList<ActionPlanModel> orderData) {
        this.context = context;
        this.orderData = orderData;
    }

    @NonNull
    @Override
    public ActionPlanAdapter.ActionPlanViewHolder3 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.action_plan_layout,
                parent, false);

        return new ActionPlanViewHolder3(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActionPlanAdapter.ActionPlanViewHolder3 holder, int position) {
        ActionPlanModel actionPlanModel = orderData.get(position);
        holder.cityName.setText("City : " + actionPlanModel.getCity());
        holder.hotelName.setText(actionPlanModel.getHotel_name());
        holder.auditRound.setText(actionPlanModel.getAudit_round());
        holder.auditDate.setText(actionPlanModel.getAudit_date());
        holder.startDate.setText(actionPlanModel.getStart_date());
        holder.endDate.setText(actionPlanModel.getEnd_date());
        holder.plannedStatus.setText(String.valueOf(actionPlanModel.getStatus().getPlanned()));
        holder.rejectedStatus.setText(String.valueOf(actionPlanModel.getStatus().getRejected()));
        holder.approvedStatus.setText(String.valueOf(actionPlanModel.getStatus().getApproved()));
        holder.overdueStatus.setText(String.valueOf(actionPlanModel.getStatus().getOverdue()));
        holder.completedStatus.setText(String.valueOf(actionPlanModel.getStatus().getCompleted()));
    }

    @Override
    public int getItemCount() {
        return orderData.size();
    }

    public class ActionPlanViewHolder3 extends RecyclerView.ViewHolder {

        TextView cityName;
        TextView hotelName;
        TextView auditRound;
        TextView auditDate;
        TextView startDate;
        TextView endDate;
        TextView plannedStatus;
        TextView rejectedStatus;
        TextView approvedStatus;
        TextView overdueStatus;
        TextView completedStatus;

        public ActionPlanViewHolder3(View itemView) {
            super(itemView);

            cityName = itemView.findViewById(R.id.city_name);
            hotelName = itemView.findViewById(R.id.hotel_name);
            auditRound = itemView.findViewById(R.id.audit_round);
            auditDate = itemView.findViewById(R.id.audit_date);
            startDate = itemView.findViewById(R.id.start_date);
            endDate = itemView.findViewById(R.id.end_date);
            plannedStatus = itemView.findViewById(R.id.planned_status);
            rejectedStatus = itemView.findViewById(R.id.rejected_status);
            approvedStatus = itemView.findViewById(R.id.approved_status);
            overdueStatus = itemView.findViewById(R.id.overdue_status);
            completedStatus = itemView.findViewById(R.id.completed_status);
        }
    }
}
