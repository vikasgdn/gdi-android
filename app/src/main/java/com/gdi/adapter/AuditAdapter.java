package com.gdi.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.activity.MysteryAuditReport.ReportAuditActivity;
import com.gdi.model.SampleModel;
import com.gdi.model.reportaudit.DepatmentOverallInfo;
import com.gdi.model.reportaudit.SectionInfo;
import com.gdi.utils.AppUtils;

import java.util.ArrayList;

public class AuditAdapter extends RecyclerView.Adapter<AuditAdapter.AuditViewHolder3> {

    private Context context;
    private ArrayList<DepatmentOverallInfo> orderData;

    public AuditAdapter(Context context, ArrayList<DepatmentOverallInfo> orderData) {
        this.context = context;
        this.orderData = orderData;
    }

    @Override
    public AuditViewHolder3 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.audit_layout,
                parent, false);

        return new AuditViewHolder3(view);
    }

    @Override
    public void onBindViewHolder(final AuditViewHolder3 holder, final int position) {
        final DepatmentOverallInfo depatmentOverallInfo = orderData.get(position);
        holder.auditDetailText.setText(depatmentOverallInfo.getSection_group_name());
        AppUtils.setScoreColor(depatmentOverallInfo.getScore(), holder.score, context);
        holder.score.setText("Avg. Score : " + depatmentOverallInfo.getScore());
        ArrayList<SectionInfo> sectionInfoArrayList = depatmentOverallInfo.getSections();
        AuditAdapter2 auditAdapter2 = new AuditAdapter2(context, sectionInfoArrayList);
        holder.departmentalList.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false));
        holder.departmentalList.setAdapter(auditAdapter2);

        if(!orderData.get(position).isExpand()){
            holder.gridLayout.setVisibility(View.GONE);
            holder.departmentalList.setVisibility(View.GONE);
            holder.dropIcon.setImageResource(R.drawable.expand_icon);
            orderData.get(position).setExpand(false);
        }else {
            holder.gridLayout.setVisibility(View.VISIBLE);
            holder.departmentalList.setVisibility(View.VISIBLE);
            holder.dropIcon.setImageResource(R.drawable.compress_icon);
            orderData.get(position).setExpand(true);
        }


        holder.expandLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(orderData.get(position).isExpand()){
                    holder.gridLayout.setVisibility(View.GONE);
                    holder.departmentalList.setVisibility(View.GONE);
                    holder.dropIcon.setImageResource(R.drawable.expand_icon);
                    orderData.get(position).setExpand(false);
                }else {
                    holder.gridLayout.setVisibility(View.VISIBLE);
                    holder.departmentalList.setVisibility(View.VISIBLE);
                    holder.dropIcon.setImageResource(R.drawable.compress_icon);
                    orderData.get(position).setExpand(true);
                }

                /*if (!expand){
                    expand = true;
                    holder.gridLayout.setVisibility(View.VISIBLE);
                    holder.departmentalList.setVisibility(View.VISIBLE);
                    holder.dropIcon.setImageResource(R.drawable.compress_icon);
                    ArrayList<SectionInfo> sectionInfoArrayList = depatmentOverallInfo.getSections();
                    AuditAdapter2 auditAdapter2 = new AuditAdapter2(context, sectionInfoArrayList);
                    holder.departmentalList.setLayoutManager(new LinearLayoutManager(context,
                            LinearLayoutManager.VERTICAL, false));
                    holder.departmentalList.setAdapter(auditAdapter2);

                }else if(expand){
                    expand = false;
                    holder.gridLayout.setVisibility(View.GONE);
                    holder.departmentalList.setVisibility(View.GONE);
                    holder.dropIcon.setImageResource(R.drawable.expand_icon);
                }*/
            }
        });

        holder.pdfIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ReportAuditActivity)context).downloadPdf(depatmentOverallInfo.getReport_urls().getPdf());
            }
        });

        holder.excelIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ReportAuditActivity)context).downloadExcel(depatmentOverallInfo.getReport_urls().getExcel());
            }
        });

        holder.mailIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ReportAuditActivity)context).sentEmail(depatmentOverallInfo.getReport_urls().getEmail());
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderData.size();
    }

    public class AuditViewHolder3 extends RecyclerView.ViewHolder {

        TextView auditDetailText;
        RecyclerView departmentalList;
        TextView score;
        ImageView dropIcon;
        ImageView pdfIcon;
        ImageView excelIcon;
        ImageView mailIcon;
        RelativeLayout expandLayout;
        LinearLayout gridLayout;

        public AuditViewHolder3(View itemView) {
            super(itemView);

            auditDetailText = itemView.findViewById(R.id.hotelDetailText);
            departmentalList = itemView.findViewById(R.id.audit_recycler);
            score = itemView.findViewById(R.id.score_text);
            dropIcon = itemView.findViewById(R.id.expand_icon);
            pdfIcon = itemView.findViewById(R.id.pdf_icon);
            excelIcon = itemView.findViewById(R.id.excel_icon);
            mailIcon = itemView.findViewById(R.id.mail_icon);
            expandLayout = itemView.findViewById(R.id.expand_Layout);
            gridLayout = itemView.findViewById(R.id.grid_layout);
        }
    }
}

