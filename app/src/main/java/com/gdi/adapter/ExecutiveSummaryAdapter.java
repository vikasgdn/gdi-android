package com.gdi.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.activity.MysteryAuditReport.ReportExecutiveSummaryActivity;
import com.gdi.model.reportexecutivesummary.ExecutiveLocationsInfo;
import com.gdi.utils.AppUtils;

import java.util.ArrayList;

public class ExecutiveSummaryAdapter extends RecyclerView.Adapter<ExecutiveSummaryAdapter.ExecutiveSummaryViewHolder> {

    private Context context;
    private ArrayList<ExecutiveLocationsInfo> orderData;

    public ExecutiveSummaryAdapter(Context context, ArrayList<ExecutiveLocationsInfo> orderData) {
        this.context = context;
        this.orderData = orderData;
    }

    @Override
    public ExecutiveSummaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.executive_summary_layout,
                parent, false);

        return new ExecutiveSummaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ExecutiveSummaryViewHolder holder, final int position) {
        final ExecutiveLocationsInfo locationInfo = orderData.get(position);
        holder.executiveSummaryHotelName.setText(locationInfo.getLocation_name());
        String summary_text = Html.fromHtml(locationInfo.getSummary()).toString();
        holder.executiveSummaryText.setText(summary_text);
        AppUtils.setScoreColor(locationInfo.getScore(), holder.score, context);
        holder.score.setText("Score : " + locationInfo.getScore());

        if(!orderData.get(position).isExpand()){
            holder.executiveSummaryTextLayout.setVisibility(View.GONE);
            holder.expandIcon.setImageResource(R.drawable.expand_icon);
            orderData.get(position).setExpand(false);
        }else {
            holder.executiveSummaryTextLayout.setVisibility(View.VISIBLE);
            holder.expandIcon.setImageResource(R.drawable.compress_icon);
            orderData.get(position).setExpand(true);
        }

        holder.executiveSummaryExpandLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(orderData.get(position).isExpand()){
                    holder.executiveSummaryTextLayout.setVisibility(View.GONE);
                    holder.expandIcon.setImageResource(R.drawable.expand_icon);
                    orderData.get(position).setExpand(false);
                }else {
                    holder.executiveSummaryTextLayout.setVisibility(View.VISIBLE);
                    holder.expandIcon.setImageResource(R.drawable.compress_icon);
                    orderData.get(position).setExpand(true);
                }

                /*if (!expand){
                    expand = true;
                    holder.executiveSummaryTextLayout.setVisibility(View.VISIBLE);
                    holder.expandIcon.setImageResource(R.drawable.compress_icon);
                    *//*if (locationInfo.getAttachments() != null && locationInfo.getAttachments().size()>0) {
                        holder.attachmentLayout.setVisibility(View.VISIBLE);
                        holder.tvAttachmentCount.setText("(" + locationInfo.getAttachments().size() + ")");
                        holder.attachmentLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(context, ExecutiveAttachmentActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putParcelableArrayList("data", locationInfo.getAttachments());
                                intent.putExtras(bundle);
                                context.startActivity(intent);
                            }
                        });
                    }else {
                        holder.attachmentLayout.setVisibility(View.GONE);
                    }*//*

                }else if(expand){
                    expand = false;
                    holder.executiveSummaryTextLayout.setVisibility(View.GONE);
                    holder.expandIcon.setImageResource(R.drawable.expand_icon);
                }*/

            }
        });



        /*ExecutiveSummaryAdapter2 executiveSummaryAdapter2 = new ExecutiveSummaryAdapter2(context, locationInfo.getAttachments());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,2
                , LinearLayoutManager.VERTICAL,false);
        holder.attachments.setLayoutManager(gridLayoutManager);
        holder.attachments.setAdapter(executiveSummaryAdapter2);*/

        holder.pdfIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ReportExecutiveSummaryActivity)context).downloadPdf(locationInfo.getReport_urls().getPdf());
            }
        });

        holder.mailIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ReportExecutiveSummaryActivity)context).emailAttachment(locationInfo.getReport_urls().getEmail());
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
        /*RecyclerView attachments;
        TextView attachmentTxt;*/
        TextView tvAttachmentCount;
        LinearLayout attachmentLayout;

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
            /*attachments = itemView.findViewById(R.id.recycler_view_attachments);
            attachmentTxt = itemView.findViewById(R.id.tv_attachment);*/
            tvAttachmentCount = itemView.findViewById(R.id.tv_attachment_count);
            attachmentLayout = itemView.findViewById(R.id.attachment_layout);
        }
    }
}
