package com.gdi.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.activity.ReportDetailSummaryActivity;
import com.gdi.model.SampleModel;
import com.gdi.model.detailedsummary.LocationInfo;

import java.util.ArrayList;

public class DetailSummaryAdapter1 extends
        RecyclerView.Adapter<DetailSummaryAdapter1.DetailSummaryViewHolder> {

    private Context context;
    private ArrayList<LocationInfo> orderData;
    private ArrayList<SampleModel> sampleOrderData;
    private boolean expand = false;
    private static final String TAG = AuditAdapter.class.getSimpleName();

    public DetailSummaryAdapter1(Context context, ArrayList<LocationInfo> orderData) {
        this.context = context;
        this.orderData = orderData;
    }
    //TODO : Static data testing
    /*public DetailSummaryAdapter1(Context context, ArrayList<SampleModel> sampleOrderData) {
        this.context = context;
        this.sampleOrderData = sampleOrderData;
    }*/

    @Override
    public DetailSummaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_summary_layout,
                parent, false);

        return new DetailSummaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DetailSummaryViewHolder holder, int position) {
        //TODO : Static data testing
        /*SampleModel sampleModel = sampleOrderData.get(position);
        holder.expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!expand){
                    expand = true;
                    holder.detailSummaryList.setVisibility(View.VISIBLE);
                    holder.expandIcon.setImageResource(R.drawable.compress_icon);
                    ArrayList<SampleModel> sampleModels = SampleModel.createList(5);
                    DetailSummaryAdapter2 detailSummaryAdapter2 = new
                            DetailSummaryAdapter2(context, sampleModels);
                    holder.detailSummaryList.setLayoutManager(new LinearLayoutManager(context,
                            LinearLayoutManager.VERTICAL, false));
                    holder.detailSummaryList.setAdapter(detailSummaryAdapter2);
                }else if(expand){
                    expand = false;
                    holder.detailSummaryList.setVisibility(View.GONE);
                    holder.expandIcon.setImageResource(R.drawable.expand_icon);
                }

            }
        });*/
        final LocationInfo locationInfo = orderData.get(position);
        //final ReportDetailSummaryActivity detailSummaryActivity = new ReportDetailSummaryActivity();
        holder.hotelNameExpandText.setText(locationInfo.getLocation_name());
        holder.expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!expand){
                    expand = true;
                    holder.detailSummaryList.setVisibility(View.VISIBLE);
                    holder.expandIcon.setImageResource(R.drawable.compress_icon);
                    DetailSummaryAdapter2 detailSummaryAdapter2 = new
                            DetailSummaryAdapter2(context, locationInfo.getSection_groups());
                    holder.detailSummaryList.setLayoutManager(new LinearLayoutManager(context,
                            LinearLayoutManager.VERTICAL, false));
                    holder.detailSummaryList.setAdapter(detailSummaryAdapter2);
                }else if(expand){
                    expand = false;
                    holder.detailSummaryList.setVisibility(View.GONE);
                    holder.expandIcon.setImageResource(R.drawable.expand_icon);
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
