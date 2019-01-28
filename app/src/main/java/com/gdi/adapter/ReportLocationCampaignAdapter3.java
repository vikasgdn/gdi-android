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
import com.gdi.model.locationcampaign.LocationCampaignRound2;
import com.gdi.utils.AppUtils;

import java.util.ArrayList;

public class ReportLocationCampaignAdapter3 extends RecyclerView.Adapter<ReportLocationCampaignAdapter3.ReportLocationCampaignViewHolder3> {

    private Context context;
    private ArrayList<LocationCampaignRound2> orderData;

    public ReportLocationCampaignAdapter3(Context context, ArrayList<LocationCampaignRound2> orderData) {
        this.context = context;
        this.orderData = orderData;
    }

    @NonNull
    @Override
    public ReportLocationCampaignAdapter3.ReportLocationCampaignViewHolder3 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_campaign_layout2,
                parent, false);

        return new ReportLocationCampaignViewHolder3(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportLocationCampaignAdapter3.ReportLocationCampaignViewHolder3 holder, int position) {
        LocationCampaignRound2 locationCampaignRound2 = orderData.get(position);
        holder.campaign.setText(locationCampaignRound2.getRound_name());
        AppUtils.setScoreColor(locationCampaignRound2.getScore(), holder.campaignScore, context);
        holder.campaignScore.setText(locationCampaignRound2.getScore());
    }

    @Override
    public int getItemCount() {
        return orderData.size();
    }

    public class ReportLocationCampaignViewHolder3 extends RecyclerView.ViewHolder {

        TextView campaign;
        TextView campaignScore;

        public ReportLocationCampaignViewHolder3(View itemView) {
            super(itemView);

            campaign = (TextView)itemView.findViewById(R.id.location_campaign);
            campaignScore = (TextView)itemView.findViewById(R.id.location_campaign_score);

        }
    }
}
