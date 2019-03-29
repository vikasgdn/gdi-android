package com.gdi.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.model.actionplan.ActionPlanModel;
import com.gdi.model.audit.AddAttachment.AddAttachmentInfo;

import java.util.ArrayList;

public class AddAttachmentAdapter extends RecyclerView.Adapter<AddAttachmentAdapter.AddAttachmentViewHolder> {

    private Context context;
    private ArrayList<AddAttachmentInfo> orderData;

    public AddAttachmentAdapter(Context context, ArrayList<AddAttachmentInfo> orderData) {
        this.context = context;
        this.orderData = orderData;
    }

    @NonNull
    @Override
    public AddAttachmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_attachment_layout,
                parent, false);

        return new AddAttachmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddAttachmentViewHolder holder, int position) {
        AddAttachmentInfo actionPlanModel = orderData.get(position);
        //holder.cityName.setText("City : " + actionPlanModel.getCity());

    }

    @Override
    public int getItemCount() {
        return orderData.size();
    }

    public class AddAttachmentViewHolder extends RecyclerView.ViewHolder {

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

        public AddAttachmentViewHolder(View itemView) {
            super(itemView);

            cityName = (TextView)itemView.findViewById(R.id.city_name);
            hotelName = (TextView)itemView.findViewById(R.id.hotel_name);
            auditRound = (TextView)itemView.findViewById(R.id.audit_round);
            auditDate = (TextView)itemView.findViewById(R.id.audit_date);
            startDate = (TextView)itemView.findViewById(R.id.start_date);
            endDate = (TextView)itemView.findViewById(R.id.end_date);
            plannedStatus = (TextView)itemView.findViewById(R.id.planned_status);
            rejectedStatus = (TextView)itemView.findViewById(R.id.rejected_status);
            approvedStatus = (TextView)itemView.findViewById(R.id.approved_status);
            overdueStatus = (TextView)itemView.findViewById(R.id.overdue_status);
            completedStatus = (TextView)itemView.findViewById(R.id.completed_status);
        }
    }
}
