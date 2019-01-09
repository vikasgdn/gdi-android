package com.gdi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.activity.OverallBrandActivity;
import com.gdi.model.SampleModel;
import com.gdi.model.overallbrand.LocationsInfo;

import java.util.ArrayList;

public class DepartmentalAdapter3 extends
        RecyclerView.Adapter<DepartmentalAdapter3.DepartmentalViewHolder3> {

    private Context context;
    private ArrayList<LocationsInfo> orderData;
    private ArrayList<SampleModel> sampleOrderData;
    private static final String TAG = DepartmentalAdapter2.class.getSimpleName();

    public DepartmentalAdapter3(Context context, ArrayList<LocationsInfo> orderData) {
        this.context = context;
        this.orderData = orderData;
    }

    //TODO : Static data testing
    /*public DepartmentalAdapter3(Context context, ArrayList<SampleModel> sampleOrderData) {
        this.context = context;
        this.sampleOrderData = sampleOrderData;
    }*/

    @Override
    public DepartmentalViewHolder3 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.departmental_layout3,
                parent, false);

        return new DepartmentalViewHolder3(view);
    }

    @Override
    public void onBindViewHolder(DepartmentalViewHolder3 holder, int position) {
        final LocationsInfo locationsInfo = orderData.get(position);
        holder.hotelName.setText(locationsInfo.getLocation_name());
        holder.score.setText(locationsInfo.getScore());

        holder.pdfIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OverallBrandActivity)context).downloadPdf(locationsInfo.getReport_urls().getPdf());
            }
        });

        holder.excelIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OverallBrandActivity)context).downloadExcel(locationsInfo.getReport_urls().getExcel());
            }
        });

        holder.mailIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OverallBrandActivity)context).emailAttachment(locationsInfo.getReport_urls().getEmail());
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderData.size();
    }

    public class DepartmentalViewHolder3 extends RecyclerView.ViewHolder {

        TextView hotelName;
        TextView score;
        ImageView pdfIcon;
        ImageView excelIcon;
        ImageView mailIcon;

        public DepartmentalViewHolder3(View itemView) {
            super(itemView);

            hotelName = itemView.findViewById(R.id.hotel_name);
            score = itemView.findViewById(R.id.score_txt);
            pdfIcon = itemView.findViewById(R.id.pdf_icon);
            excelIcon = itemView.findViewById(R.id.excel_icon);
            mailIcon = itemView.findViewById(R.id.mail_icon);

        }
    }
}