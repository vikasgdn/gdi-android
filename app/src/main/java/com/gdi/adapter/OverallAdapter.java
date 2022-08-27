package com.gdi.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.gdi.hotel.mystery.audits.R;
import com.gdi.activity.MysteryAuditReport.ReportOverallBrandActivity;
import com.gdi.model.SampleModel;
import com.gdi.model.reportoverallbrand.LocationsInfo;
import com.gdi.utils.AppUtils;

import java.util.ArrayList;

public class OverallAdapter extends RecyclerView.Adapter<OverallAdapter.OverallViewHolder> {

    private Context context;
    private ArrayList<LocationsInfo> orderData;
    private ArrayList<SampleModel> sampleOrderData;
    private static final String TAG = AuditAdapter.class.getSimpleName();

    public OverallAdapter(Context context, ArrayList<LocationsInfo> orderData) {
        this.context = context;
        this.orderData = orderData;
    }

    @Override
    public OverallViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.overall_layout,
                parent, false);

        return new OverallViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OverallViewHolder holder, int position) {
        final LocationsInfo locationsInfo = orderData.get(position);
        holder.hotelName.setText(locationsInfo.getLocation_name());
        AppUtils.setScoreColor(locationsInfo.getScore(), holder.score, context);
        holder.score.setText(locationsInfo.getScore());
        holder.pdfIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.pdfIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ReportOverallBrandActivity)context).downloadPdf(locationsInfo.getReport_urls().getPdf());
            }
        });

        holder.excelIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ReportOverallBrandActivity)context).downloadExcel(locationsInfo.getReport_urls().getExcel());
            }
        });

        holder.mailIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ReportOverallBrandActivity)context).emailAttachment(locationsInfo.getReport_urls().getEmail());
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderData.size();
    }

    public class OverallViewHolder extends RecyclerView.ViewHolder {

        TextView hotelName;
        TextView score;
        ImageView pdfIcon;
        ImageView excelIcon;
        ImageView mailIcon;

        public OverallViewHolder(View itemView) {
            super(itemView);

            hotelName = itemView.findViewById(R.id.htl_name_txt);
            score = itemView.findViewById(R.id.score_txt);
            pdfIcon = itemView.findViewById(R.id.pdf_icon);
            excelIcon = itemView.findViewById(R.id.excel_icon);
            mailIcon = itemView.findViewById(R.id.mail_icon);

        }
    }
}