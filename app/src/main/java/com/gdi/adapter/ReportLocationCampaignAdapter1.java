package com.gdi.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.model.locationcampaign.LocationCampaignModel;
import com.gdi.utils.AppUtils;

import java.util.ArrayList;

public class ReportLocationCampaignAdapter1 extends RecyclerView.Adapter<ReportLocationCampaignAdapter1.ReportLocationCampaignViewHolder> {

    private Context context;
    private ArrayList<LocationCampaignModel> orderData;

    public ReportLocationCampaignAdapter1(Context context, ArrayList<LocationCampaignModel> orderData) {
        this.context = context;
        this.orderData = orderData;
    }

    @NonNull
    @Override
    public ReportLocationCampaignAdapter1.ReportLocationCampaignViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_campaign_layout1,
                parent, false);

        return new ReportLocationCampaignViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportLocationCampaignAdapter1.ReportLocationCampaignViewHolder holder, int position) {
        LocationCampaignModel locationCampaignModel = orderData.get(position);
        holder.hotelName.setText(locationCampaignModel.getLocation());
        holder.cityName.setText(locationCampaignModel.getCity());
        holder.countryName.setText(locationCampaignModel.getCountry());
        holder.generalManager.setText(locationCampaignModel.getGeneral_manager());
        holder.brand.setText(locationCampaignModel.getBrand());
        AppUtils.setScoreColor(locationCampaignModel.getAvg_score(), holder.average_score, context);
        holder.average_score.setText(locationCampaignModel.getAvg_score());
        holder.rank.setText("" + locationCampaignModel.getRank());
        ReportLocationCampaignAdapter2 reportLocationCampaignAdapter2 = new ReportLocationCampaignAdapter2(context, locationCampaignModel.getRounds());
        holder.recyclerViewLocationCampaign.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerViewLocationCampaign.setAdapter(reportLocationCampaignAdapter2);

    }

    @Override
    public int getItemCount() {
        return orderData.size();
    }

    public class ReportLocationCampaignViewHolder extends RecyclerView.ViewHolder {

        TextView hotelName;
        TextView cityName;
        TextView countryName;
        TextView generalManager;
        TextView brand;
        TextView average_score;
        TextView rank;
        RecyclerView recyclerViewLocationCampaign;

        public ReportLocationCampaignViewHolder(View itemView) {
            super(itemView);

            hotelName = itemView.findViewById(R.id.location_campaign_hotel_name);
            cityName = itemView.findViewById(R.id.location_campaign_city_name);
            countryName = itemView.findViewById(R.id.location_campaign_country_name);
            generalManager = itemView.findViewById(R.id.location_campaign_general_manager);
            brand = itemView.findViewById(R.id.location_campaign_brand);
            average_score = itemView.findViewById(R.id.location_campaign_average_score);
            rank = itemView.findViewById(R.id.location_campaign_rank);
            recyclerViewLocationCampaign = itemView.findViewById(R.id.recycler_view_location_campaign);
        }
    }
}
