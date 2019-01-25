package com.gdi.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.model.trendlocation.TrendLocationRound2;

import java.util.ArrayList;

public class ReportTrendLocationAdapter3 extends RecyclerView.Adapter<ReportTrendLocationAdapter3.ReportTrendLocationViewHolder3> {

    private Context context;
    private ArrayList<TrendLocationRound2> orderData;

    public ReportTrendLocationAdapter3(Context context, ArrayList<TrendLocationRound2> orderData) {
        this.context = context;
        this.orderData = orderData;
    }

    @NonNull
    @Override
    public ReportTrendLocationAdapter3.ReportTrendLocationViewHolder3 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trend_location_layout3,
                parent, false);

        return new ReportTrendLocationViewHolder3(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportTrendLocationAdapter3.ReportTrendLocationViewHolder3 holder, int position) {
        TrendLocationRound2 trendLocationRound2 = orderData.get(position);
        holder.campaign.setText(trendLocationRound2.getRound_name());
        holder.campaignScore.setText(trendLocationRound2.getScore());
    }

    @Override
    public int getItemCount() {
        return orderData.size();
    }

    public class ReportTrendLocationViewHolder3 extends RecyclerView.ViewHolder {

        TextView campaign;
        TextView campaignScore;


        public ReportTrendLocationViewHolder3(View itemView) {
            super(itemView);

            campaign = (TextView)itemView.findViewById(R.id.trend_location_campaign);
            campaignScore = (TextView)itemView.findViewById(R.id.trend_location_campaign_score);

        }
    }
}
