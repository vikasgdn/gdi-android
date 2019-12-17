package com.gdi.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.activity.Audit.AuditSectionsActivity;
import com.gdi.model.audit.AuditInfo;
import com.gdi.utils.AppLogger;
import com.gdi.utils.AppUtils;

import java.util.ArrayList;

public class AuditActionAdapter extends
        RecyclerView.Adapter<AuditActionAdapter.AuditActionViewHolder> {

    private Context context;
    private ArrayList<AuditInfo> data;
    private String status;

    public AuditActionAdapter(Context context, ArrayList<AuditInfo> data, String status) {
        this.context = context;
        this.data = data;
        this.status = status;
    }

    @Override
    public AuditActionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.audit_action_layout,
                parent, false);

        return new AuditActionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AuditActionViewHolder holder, final int position) {
        //TODO : Static data testing

        final AuditInfo auditInfo = data.get(position);
        holder.tvAuditId.setText("" + auditInfo.getAudit_id());
        //holder.tvAuditType.setText(auditInfo.getAudit_type());
        holder.tvAuditName.setText(auditInfo.getAudit_name());
        holder.tvBrand.setText(auditInfo.getBrand_name());
        holder.tvLocation.setText(auditInfo.getLocation_title());
        holder.tvDueDate.setText(auditInfo.getAudit_due_date());
        //holder.tvCreatedBy.setText("" + auditInfo.getCreated_by());
        holder.tvAuditorName.setText(auditInfo.getAuditor_fname() + " " + auditInfo.getAuditor_lname());
        //holder.tvAuditorEmail.setText(auditInfo.getAuditor_email());
        if (AppUtils.isStringEmpty(auditInfo.getReviewer_fname())) {
            holder.tvReviewerName.setVisibility(View.GONE);
        }else {
            holder.tvReviewerName.setText(auditInfo.getReviewer_fname() + " " + auditInfo.getReviewer_lname());
        }
        //holder.tvReviewerEmail.setText(auditInfo.getReviewer_email());
        //holder.tvBrandStandardStatus.setText(auditInfo.getBrand_std_status_name());
        //holder.tvDetailedSummaryStatus.setText(auditInfo.getDetailed_sum_status_name());
        //holder.tvExecutiveSummaryStatus.setText(auditInfo.getExec_sum_status_name());
        //AppUtils.setStatusColor(auditInfo.getBrand_std_status(), holder.tvBrandStandardStatus, context);
        //AppUtils.setStatusColor(auditInfo.getDetailed_sum_status(), holder.tvDetailedSummaryStatus, context);
        //AppUtils.setStatusColor(auditInfo.getExec_sum_status(), holder.tvExecutiveSummaryStatus, context);

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

        /*holder.btnViewQuestion.setOnClickListener(new View.OnClickListener() {
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
        });*/

        if (auditInfo.getShow_data() == 1) {
            //holder.btnViewQuestion.setVisibility(View.VISIBLE);
            holder.btnViewQuestion.setOnClickListener(new View.OnClickListener() {
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
                    AppLogger.e("bsStatus:","-"+auditInfo.getBrand_std_status());
                    context.startActivity(startAudit);
                }
            });
        }else {
            //holder.btnViewQuestion.setVisibility(View.GONE);
            holder.btnViewQuestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notificationDialog();
                }
            });
        }

        /*holder.btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startAudit = new Intent(context, AuditInfoActivity.class);
                startAudit.putExtra("instruction", auditInfo.getInstructions());
                startAudit.putExtra("reviewerRating", "" + auditInfo.getReviewer_rating());
                startAudit.putExtra("reviewerFeedBack", auditInfo.getReviewer_feedback());
                context.startActivity(startAudit);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startAudit = new Intent(context, CreateAuditActivity.class);
                context.startActivity(startAudit);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class AuditActionViewHolder extends RecyclerView.ViewHolder {


        TextView tvAuditId;
        //TextView tvAuditType;
        TextView tvAuditName;
        TextView tvBrand;
        TextView tvLocation;
        TextView tvDueDate;
        //TextView tvCreatedBy;
        TextView tvAuditorName;
        //TextView tvAuditorEmail;
        TextView tvReviewerName;
        //TextView tvReviewerEmail;
        //TextView tvBrandStandardStatus;
        //TextView tvDetailedSummaryStatus;
        //TextView tvExecutiveSummaryStatus;
        //Button btnInfo;
        //Button btnDelete;
        Button btnViewQuestion;
        LinearLayout reviewerContainer;

        public AuditActionViewHolder (View itemView) {
            super(itemView);

            tvAuditId = itemView.findViewById(R.id.tv_audit_id);
            //tvAuditType = itemView.findViewById(R.id.tv_audit_type);
            tvAuditName = itemView.findViewById(R.id.tv_audit_name);
            tvBrand = itemView.findViewById(R.id.tv_brand);
            tvLocation = itemView.findViewById(R.id.tv_location);
            tvDueDate = itemView.findViewById(R.id.tv_due_date);
            //tvCreatedBy = itemView.findViewById(R.id.tv_created_by);
            tvAuditorName = itemView.findViewById(R.id.tv_auditor_name);
            //tvAuditorEmail = itemView.findViewById(R.id.tv_auditor_email);
            tvReviewerName = itemView.findViewById(R.id.tv_reviewer_name);
            //tvReviewerEmail = itemView.findViewById(R.id.tv_reviewer_email);
            //tvBrandStandardStatus = itemView.findViewById(R.id.tv_brand_standard_status);
            //tvDetailedSummaryStatus = itemView.findViewById(R.id.tv_detailed_summary_status);
            //tvExecutiveSummaryStatus = itemView.findViewById(R.id.tv_executive_summary_status);
            //btnInfo = itemView.findViewById(R.id.btn_info);
            //btnDelete = itemView.findViewById(R.id.btn_delete);
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
