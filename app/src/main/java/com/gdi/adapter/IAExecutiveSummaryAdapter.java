package com.gdi.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.activity.IAReportExecutiveSummaryActivity;
import com.gdi.activity.ReportExecutiveSummaryActivity;
import com.gdi.model.SampleModel;
import com.gdi.model.executivesummary.ExecutiveLocationsInfo;

import java.util.ArrayList;

public class IAExecutiveSummaryAdapter extends RecyclerView.Adapter<IAExecutiveSummaryAdapter.ExecutiveSummaryViewHolder> {

    private Context context;
    private ArrayList<ExecutiveLocationsInfo> orderData;
    private ArrayList<SampleModel> sampleOrderData;
    private boolean expand = false;
    private static final String TAG = AuditAdapter.class.getSimpleName();

    public IAExecutiveSummaryAdapter(Context context, ArrayList<ExecutiveLocationsInfo> orderData) {
        this.context = context;
        this.orderData = orderData;
    }

    //TODO : Static data testing
    /*public ExecutiveSummaryAdapter(Context context, ArrayList<SampleModel> sampleOrderData) {
        this.context = context;
        this.sampleOrderData = sampleOrderData;
    }*/

    @Override
    public ExecutiveSummaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.executive_summary_layout,
                parent, false);

        return new ExecutiveSummaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ExecutiveSummaryViewHolder holder, int position) {
        //TODO : Static data testing
        /*holder.executiveSummaryExpandLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!expand){
                    expand = true;
                    holder.executiveSummaryTextLayout.setVisibility(View.VISIBLE);
                    holder.expandIcon.setImageResource(R.drawable.compress_icon);

                }else if(expand){
                    expand = false;
                    holder.executiveSummaryTextLayout.setVisibility(View.GONE);
                    holder.expandIcon.setImageResource(R.drawable.expand_icon);
                }

            }
        });*/
        final ExecutiveLocationsInfo locationInfo = orderData.get(position);
        holder.executiveSummaryHotelName.setText(locationInfo.getLocation_name());
        holder.executiveSummaryText.setText(locationInfo.getSummary());
        holder.score.setText("Score : " + locationInfo.getScore());
        holder.executiveSummaryExpandLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!expand){
                    expand = true;
                    holder.executiveSummaryTextLayout.setVisibility(View.VISIBLE);
                    holder.expandIcon.setImageResource(R.drawable.compress_icon);
                    holder.attachments.setVisibility(View.VISIBLE);
                    holder.attachmentTxt.setVisibility(View.VISIBLE);

                }else if(expand){
                    expand = false;
                    holder.executiveSummaryTextLayout.setVisibility(View.GONE);
                    holder.attachments.setVisibility(View.GONE);
                    holder.attachmentTxt.setVisibility(View.GONE);
                    holder.expandIcon.setImageResource(R.drawable.expand_icon);
                }

            }
        });

        ExecutiveSummaryAdapter2 executiveSummaryAdapter2 = new ExecutiveSummaryAdapter2(context, locationInfo.getAttachments());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,2
                , LinearLayoutManager.VERTICAL,false);
        holder.attachments.setLayoutManager(gridLayoutManager);
        holder.attachments.setAdapter(executiveSummaryAdapter2);

        holder.pdfIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((IAReportExecutiveSummaryActivity)context).downloadPdf(locationInfo.getReport_urls().getPdf());
            }
        });

        holder.mailIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((IAReportExecutiveSummaryActivity)context).emailAttachment(locationInfo.getReport_urls().getEmail());
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderData.size();
    }

    public class ExecutiveSummaryViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout executiveSummaryExpandLayout;
        RelativeLayout executiveSummaryTextLayout;
        TextView executiveSummaryHotelName;
        TextView executiveSummaryText;
        TextView score;
        ImageView pdfIcon;
        ImageView mailIcon;
        ImageView expandIcon;
        RecyclerView attachments;
        TextView attachmentTxt;

        public ExecutiveSummaryViewHolder(View itemView) {
            super(itemView);

            executiveSummaryExpandLayout = itemView.findViewById(R.id.executive_summary_expand_layout);
            executiveSummaryTextLayout = itemView.findViewById(R.id.executive_summary_text_layout);
            executiveSummaryHotelName = itemView.findViewById(R.id.executive_summary_hotel_name);
            executiveSummaryText = itemView.findViewById(R.id.executive_summary_text);
            score = itemView.findViewById(R.id.score_text);
            pdfIcon = itemView.findViewById(R.id.pdf_icon);
            mailIcon = itemView.findViewById(R.id.mail_icon);
            expandIcon = itemView.findViewById(R.id.expand_icon);
            attachments = itemView.findViewById(R.id.recycler_view_attachments);
            attachmentTxt = itemView.findViewById(R.id.tv_attachment);
        }
    }
}