package com.gdi.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.model.locationcampaign.LocationCampaignRound;
import com.gdi.model.trendlocation.TrendLocationRound;

import java.util.ArrayList;

public class ReportLocationCampaignAdapter2 extends RecyclerView.Adapter<ReportLocationCampaignAdapter2.ReportLocationCampaignViewHolder2> {

    private Context context;
    private ArrayList<LocationCampaignRound> orderData;

    public ReportLocationCampaignAdapter2(Context context, ArrayList<LocationCampaignRound> orderData) {
        this.context = context;
        this.orderData = orderData;
    }

    @NonNull
    @Override
    public ReportLocationCampaignAdapter2.ReportLocationCampaignViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_campaign_layout2,
                parent, false);

        return new ReportLocationCampaignViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportLocationCampaignAdapter2.ReportLocationCampaignViewHolder2 holder, int position) {
        LocationCampaignRound locationCampaignRound = orderData.get(position);
        holder.campaign.setText(locationCampaignRound.getRound_name());
        holder.campaignScore.setText(locationCampaignRound.getOverall_score());
    }

    @Override
    public int getItemCount() {
        return orderData.size();
    }

    public class ReportLocationCampaignViewHolder2 extends RecyclerView.ViewHolder {

        TextView campaign;
        TextView campaignScore;

        public ReportLocationCampaignViewHolder2(View itemView) {
            super(itemView);

            campaign = (TextView)itemView.findViewById(R.id.location_campaign);
            campaignScore = (TextView)itemView.findViewById(R.id.location_campaign_score);

        }
    }
}
