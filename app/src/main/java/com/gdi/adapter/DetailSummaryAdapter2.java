package com.gdi.adapter;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.activity.MysteryAuditReport.ReportDetailSummaryActivity;
import com.gdi.model.reportdetailedsummary.SectionGroupInfo;
import com.gdi.utils.AppUtils;

import java.util.ArrayList;

public class DetailSummaryAdapter2 extends
        RecyclerView.Adapter<DetailSummaryAdapter2.DetailSummaryViewHolder2> {

    private Context context;
    private ArrayList<SectionGroupInfo> orderData;

    public DetailSummaryAdapter2(Context context, ArrayList<SectionGroupInfo> orderData) {
        this.context = context;
        this.orderData = orderData;
    }

    @Override
    public DetailSummaryViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_summary_layout2, parent, false);

        return new DetailSummaryViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(final DetailSummaryViewHolder2 holder, final int position) {
        final SectionGroupInfo sectionGroupInfo = orderData.get(position);
        holder.hotelDetailExpandText.setText(sectionGroupInfo.getSection_group_name());
        AppUtils.setScoreColor(sectionGroupInfo.getScore(), holder.score, context);
        holder.score.setText("Score : " +sectionGroupInfo.getScore());
        DetailSummaryAdapter3 departmentalAdapter3 = new
                DetailSummaryAdapter3(context, sectionGroupInfo.getSections());
        holder.detailSummaryList.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false));
        holder.detailSummaryList.setAdapter(departmentalAdapter3);

        if(!orderData.get(position).isExpand()){
            holder.detailSummaryList.setVisibility(View.GONE);
            holder.expandIcon.setImageResource(R.drawable.expand_icon);
            orderData.get(position).setExpand(false);
        }else {
            holder.detailSummaryList.setVisibility(View.VISIBLE);
            holder.expandIcon.setImageResource(R.drawable.compress_icon);
            orderData.get(position).setExpand(true);
        }

        holder.expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(orderData.get(position).isExpand()){
                    holder.detailSummaryList.setVisibility(View.GONE);
                    holder.expandIcon.setImageResource(R.drawable.expand_icon);
                    orderData.get(position).setExpand(false);
                }else {
                    holder.detailSummaryList.setVisibility(View.VISIBLE);
                    holder.expandIcon.setImageResource(R.drawable.compress_icon);
                    orderData.get(position).setExpand(true);
                }

                /*if (!expand){
                    expand = true;
                    holder.detailSummaryList.setVisibility(View.VISIBLE);
                    holder.expandIcon.setImageResource(R.drawable.compress_icon);
                }else if(expand) {
                    expand = false;
                    holder.detailSummaryList.setVisibility(View.GONE);
                    holder.expandIcon.setImageResource(R.drawable.expand_icon);
                }*/

            }
        });

        holder.pdfIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ReportDetailSummaryActivity)context).downloadPdf(sectionGroupInfo.getReport_urls().getPdf());
            }
        });

        holder.mailIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ReportDetailSummaryActivity)context).emailAttachment(sectionGroupInfo.getReport_urls().getEmail());
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderData.size();
    }

    public class DetailSummaryViewHolder2 extends RecyclerView.ViewHolder {

        RelativeLayout expand;
        TextView hotelDetailExpandText;
        RecyclerView detailSummaryList;
        TextView score;
        ImageView pdfIcon;
        ImageView mailIcon;
        ImageView expandIcon;

        public DetailSummaryViewHolder2(View itemView) {
            super(itemView);

            hotelDetailExpandText = itemView.findViewById(R.id.hotelDetailText);
            expand = itemView.findViewById(R.id.expand_layout);
            detailSummaryList = itemView.findViewById(R.id.detail_summary_recycler);
            score = itemView.findViewById(R.id.score_text);
            pdfIcon = itemView.findViewById(R.id.pdf_icon);
            mailIcon = itemView.findViewById(R.id.mail_icon);
            expandIcon = itemView.findViewById(R.id.expand_icon);
        }
    }
}
