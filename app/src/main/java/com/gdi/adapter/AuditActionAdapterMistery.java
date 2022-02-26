package com.gdi.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.gdi.R;
import com.gdi.activity.Audit.AuditSectionsActivity;
import com.gdi.activity.Audit.AuditSectionsActivityMistrey;
import com.gdi.model.audit.AuditInfo;
import com.gdi.utils.AppLogger;
import com.gdi.utils.AppUtils;

import java.util.ArrayList;

public class AuditActionAdapterMistery extends
        RecyclerView.Adapter<AuditActionAdapterMistery.AuditActionViewHolder> {

    private Context context;
    private ArrayList<AuditInfo> data;
    private String status;

    public AuditActionAdapterMistery(Context context, ArrayList<AuditInfo> data, String status) {
        this.context = context;
        this.data = data;
        this.status = status;
    }

    @Override
    public AuditActionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.audit_action_layout_mystery, parent, false);
        return new AuditActionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AuditActionViewHolder holder, final int position) {
        //TODO : Static data testing

        final AuditInfo auditInfo = data.get(position);
        holder.tvAuditId.setText("" + auditInfo.getAudit_id());
        holder.tvAuditName.setText(auditInfo.getAudit_name());
        holder.tvBrand.setText(auditInfo.getBrand_name());
        holder.tvLocation.setText(auditInfo.getLocation_title());
        holder.tvDueDate.setText(auditInfo.getAudit_due_date());
        holder.tvAuditorName.setText(auditInfo.getAuditor_fname() + " " + auditInfo.getAuditor_lname());
        if (AppUtils.isStringEmpty(auditInfo.getReviewer_fname())) {
            holder.tvReviewerName.setVisibility(View.GONE);
        }else {
            holder.tvReviewerName.setText(auditInfo.getReviewer_fname() + " " + auditInfo.getReviewer_lname());
        }
        if (status.equals("1")){
            holder.btnViewQuestion.setText(context.getResources().getString(R.string.start));
            holder.btnViewQuestion.setTextColor(context.getResources().getColor(R.color.colorWhite));
            holder.btnViewQuestion.setBackgroundColor(context.getResources().getColor(R.color.appThemeColour));
        }else if (status.equals("2")){
            holder.btnViewQuestion.setText(context.getResources().getString(R.string.resume));
            holder.btnViewQuestion.setTextColor(context.getResources().getColor(R.color.colorWhite));
            holder.btnViewQuestion.setBackgroundColor(context.getResources().getColor(R.color.colorOrange));
        }else if (status.equals("3")){
            holder.btnViewQuestion.setText(context.getResources().getString(R.string.submitted));
            holder.btnViewQuestion.setTextColor(context.getResources().getColor(R.color.colorWhite));
            holder.btnViewQuestion.setBackgroundColor(context.getResources().getColor(R.color.scoreGreen));
        }else if (status.equals("4")){
            holder.btnViewQuestion.setText(context.getResources().getString(R.string.rejected));
            holder.btnViewQuestion.setTextColor(context.getResources().getColor(R.color.colorWhite));
            holder.btnViewQuestion.setBackgroundColor(context.getResources().getColor(R.color.scoreRed));
        }

        if(status.equals("1")  || status.equals("2")) {
            holder.btnViewQuestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent startAudit = new Intent(context, AuditSectionsActivityMistrey.class);
                    startAudit.putExtra("brandName", auditInfo.getBrand_name());
                    startAudit.putExtra("locationName", auditInfo.getLocation_title());
                    startAudit.putExtra("auditName", auditInfo.getAudit_name());
                    startAudit.putExtra("auditId", "" + auditInfo.getAudit_id());
                    startAudit.putExtra("bsStatus", "" + auditInfo.getAudit_status());
                    AppLogger.e("bsStatus:", "-" + auditInfo.getAudit_status());
                    context.startActivity(startAudit);
                }
            });
        }
        else
        {
            holder.btnViewQuestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notificationDialog();
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class AuditActionViewHolder extends RecyclerView.ViewHolder {


        TextView tvAuditId;
        TextView tvAuditName;
        TextView tvBrand;
        TextView tvLocation;
        TextView tvDueDate;
        TextView tvAuditorName;
        TextView tvReviewerName;
        Button btnViewQuestion;
        LinearLayout reviewerContainer;

        public AuditActionViewHolder (View itemView) {
            super(itemView);

            tvAuditId = itemView.findViewById(R.id.tv_audit_id);
            tvAuditName = itemView.findViewById(R.id.tv_audit_name);
            tvBrand = itemView.findViewById(R.id.tv_brand);
            tvLocation = itemView.findViewById(R.id.tv_location);
            tvDueDate = itemView.findViewById(R.id.tv_due_date);
            tvAuditorName = itemView.findViewById(R.id.tv_auditor_name);
            tvReviewerName = itemView.findViewById(R.id.tv_reviewer_name);
            btnViewQuestion = itemView.findViewById(R.id.start_btn);
            reviewerContainer = itemView.findViewById(R.id.ll_reviewer_container);

        }
    }

    private void notificationDialog() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        dialog.setTitle("GDI");
        dialog.setMessage("You are not allowed to perform any function in this audit");

        dialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.create().show();
    }
}
