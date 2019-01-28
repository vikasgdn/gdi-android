package com.gdi.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.model.trendlocation.TrendLocationRound;
import com.gdi.model.trendlocation.TrendLocationRound2;
import com.gdi.utils.AppUtils;

import java.util.ArrayList;

public class ReportTrendLocationAdapter2 extends RecyclerView.Adapter<ReportTrendLocationAdapter2.ReportTrendLocationViewHolder2> {

    private Context context;
    private ArrayList<TrendLocationRound> orderData;

    public ReportTrendLocationAdapter2(Context context, ArrayList<TrendLocationRound> orderData) {
        this.context = context;
        this.orderData = orderData;
    }

    @NonNull
    @Override
    public ReportTrendLocationAdapter2.ReportTrendLocationViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trend_location_layout2,
                parent, false);

        return new ReportTrendLocationViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportTrendLocationAdapter2.ReportTrendLocationViewHolder2 holder, int position) {
        TrendLocationRound trendLocationRound = orderData.get(position);
        holder.campaign.setText(trendLocationRound.getRound_name());
        AppUtils.setScoreColor(trendLocationRound.getOverall_score(), holder.campaignScore, context);
        holder.campaignScore.setText(trendLocationRound.getOverall_score());
        holder.campaignRank.setText(trendLocationRound.getRank());
    }

    @Override
    public int getItemCount() {
        return orderData.size();
    }

    public class ReportTrendLocationViewHolder2 extends RecyclerView.ViewHolder {

        TextView campaign;
        TextView campaignScore;
        TextView campaignRank;

        public ReportTrendLocationViewHolder2(View itemView) {
            super(itemView);

            campaign = (TextView)itemView.findViewById(R.id.trend_location_campaign);
            campaignScore = (TextView)itemView.findViewById(R.id.trend_location_campaign_score);
            campaignRank = (TextView)itemView.findViewById(R.id.trend_location_campaign_rank);

        }
    }
}
