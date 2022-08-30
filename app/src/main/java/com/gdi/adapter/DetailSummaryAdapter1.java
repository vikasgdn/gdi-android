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

import com.gdi.hotel.mystery.audits.R;
import com.gdi.activity.mysteryauditreport.ReportDetailSummaryActivity;
import com.gdi.model.reportdetailedsummary.LocationInfo;

import java.util.ArrayList;

public class DetailSummaryAdapter1 extends
        RecyclerView.Adapter<DetailSummaryAdapter1.DetailSummaryViewHolder> {

    private Context context;
    private ArrayList<LocationInfo> orderData;

    public DetailSummaryAdapter1(Context context, ArrayList<LocationInfo> orderData) {
        this.context = context;
        this.orderData = orderData;
    }

    @Override
    public DetailSummaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_summary_layout,
                parent, false);

        return new DetailSummaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DetailSummaryViewHolder holder, final int position) {

        final LocationInfo locationInfo = orderData.get(position);
        holder.hotelNameExpandText.setText(locationInfo.getLocation_name());
        DetailSummaryAdapter2 detailSummaryAdapter2 = new DetailSummaryAdapter2(context, locationInfo.getSection_groups());
        holder.detailSummaryList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        holder.detailSummaryList.setAdapter(detailSummaryAdapter2);

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


            }
        });

        holder.pdfIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ReportDetailSummaryActivity)context).downloadPdf(locationInfo.getReport_urls().getPdf());
            }
        });

        holder.mailIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ReportDetailSummaryActivity)context).emailAttachment(locationInfo.getReport_urls().getEmail());
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderData.size();
    }

    public class DetailSummaryViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout expand;
        TextView hotelNameExpandText;
        RecyclerView detailSummaryList;
        ImageView pdfIcon;
        ImageView mailIcon;
        ImageView expandIcon;

        public DetailSummaryViewHolder(View itemView) {
            super(itemView);

            hotelNameExpandText = itemView.findViewById(R.id.hotelNameText);
            expand = itemView.findViewById(R.id.expandLayout);
            detailSummaryList = itemView.findViewById(R.id.detail_summary_recycler);
            pdfIcon = itemView.findViewById(R.id.pdf_icon);
            mailIcon = itemView.findViewById(R.id.mail_icon);
            expandIcon = itemView.findViewById(R.id.expand_icon);
        }
    }
}
