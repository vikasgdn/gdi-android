package com.gdi.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.activity.HotelDetailSummaryActivity;
import com.gdi.activity.InternalAuditReport.IAReportDetailSummaryActivity;
import com.gdi.model.SampleModel;
import com.gdi.model.reportdetailedsummary.SectionsInfo;

import java.util.ArrayList;

public class IADetailSummaryAdapter3 extends
        RecyclerView.Adapter<IADetailSummaryAdapter3.DetailSummaryViewHolder3> {

    private Context context;
    private ArrayList<SectionsInfo> orderData;
    private ArrayList<SampleModel> sampleOrderData;
    private static final String TAG = IADetailSummaryAdapter3.class.getSimpleName();

    public IADetailSummaryAdapter3(Context context, ArrayList<SectionsInfo> orderData) {
        this.context = context;
        this.orderData = orderData;
    }

    @Override
    public DetailSummaryViewHolder3 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_summary_layout3, parent, false);

        return new DetailSummaryViewHolder3(view);
    }

    @Override
    public void onBindViewHolder(DetailSummaryViewHolder3 holder, int position) {
        final SectionsInfo sectionsInfo = orderData.get(position);
        holder.hotelDetailExpandText.setText(sectionsInfo.getSection_name());
        holder.score.setText("Avg Score : " +sectionsInfo.getScore());
        holder.expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HotelDetailSummaryActivity.class);
                intent.putExtra("sectionInfo", sectionsInfo);
                context.startActivity(intent);
            }
        });

        holder.pdfIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((IAReportDetailSummaryActivity)context).downloadPdf(sectionsInfo.getReport_urls().getPdf());
            }
        });

        holder.mailIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((IAReportDetailSummaryActivity)context).emailAttachment(sectionsInfo.getReport_urls().getEmail());
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderData.size();
    }

    public class DetailSummaryViewHolder3 extends RecyclerView.ViewHolder {

        RelativeLayout expand;
        TextView hotelDetailExpandText;
        TextView score;
        ImageView pdfIcon;
        ImageView mailIcon;

        public DetailSummaryViewHolder3(View itemView) {
            super(itemView);

            expand = itemView.findViewById(R.id.expand_layout);
            hotelDetailExpandText = itemView.findViewById(R.id.hotelDetailExpandText);
            score = itemView.findViewById(R.id.score_text);
            pdfIcon = itemView.findViewById(R.id.pdf_icon);
            mailIcon = itemView.findViewById(R.id.mail_icon);

        }
    }
}
