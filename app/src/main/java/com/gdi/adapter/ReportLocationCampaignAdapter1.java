package com.gdi.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.model.locationcampaign.LocationCampaignInfo;
import com.gdi.model.trendlocation.TrendLocationInfo;

import java.util.ArrayList;

public class ReportLocationCampaignAdapter1 extends RecyclerView.Adapter<ReportLocationCampaignAdapter1.ReportLocationCampaignViewHolder> {

    private Context context;
    private ArrayList<LocationCampaignInfo> orderData;

    public ReportLocationCampaignAdapter1(Context context, ArrayList<LocationCampaignInfo> orderData) {
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
        LocationCampaignInfo locationCampaignInfo = orderData.get(position);
        holder.hotelName.setText(locationCampaignInfo.getLocation());
        holder.cityName.setText(locationCampaignInfo.getCity());
        holder.countryName.setText(locationCampaignInfo.getCountry());
        holder.generalManager.setText(locationCampaignInfo.getGeneral_manager());
        holder.brand.setText(locationCampaignInfo.getGeneral_manager());
        holder.average_score.setText(locationCampaignInfo.getGeneral_manager());
        holder.rank.setText("" + locationCampaignInfo.getRank());
        ReportLocationCampaignAdapter2 reportLocationCampaignAdapter2 = new ReportLocationCampaignAdapter2(context, locationCampaignInfo.getRounds());
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

            hotelName = (TextView)itemView.findViewById(R.id.location_campaign_hotel_name);
            cityName = (TextView)itemView.findViewById(R.id.location_campaign_city_name);
            countryName = (TextView)itemView.findViewById(R.id.location_campaign_country_name);
            generalManager = (TextView)itemView.findViewById(R.id.location_campaign_general_manager);
            brand = (TextView)itemView.findViewById(R.id.location_campaign_brand);
            average_score = (TextView)itemView.findViewById(R.id.location_campaign_average_score);
            rank = (TextView)itemView.findViewById(R.id.location_campaign_rank);
            recyclerViewLocationCampaign = (RecyclerView)itemView.findViewById(R.id.recycler_view_location_campaign);
        }
    }
}
