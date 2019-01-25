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
import com.gdi.model.sectiongroup.SectionGroupInfo;
import com.gdi.model.trendlocation.TrendLocationInfo;
import com.gdi.model.trendlocation.TrendLocationModel;

import java.util.ArrayList;

public class ReportTrendLocationAdapter1 extends RecyclerView.Adapter<ReportTrendLocationAdapter1.ReportTrendLocationViewHolder> {

    private Context context;
    private ArrayList<TrendLocationModel> orderData;

    public ReportTrendLocationAdapter1(Context context, ArrayList<TrendLocationModel> orderData) {
        this.context = context;
        this.orderData = orderData;
    }

    @NonNull
    @Override
    public ReportTrendLocationAdapter1.ReportTrendLocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trend_location_layout1,
                parent, false);

        return new ReportTrendLocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportTrendLocationAdapter1.ReportTrendLocationViewHolder holder, int position) {
        TrendLocationModel trendLocationModel = orderData.get(position);
        holder.hotelName.setText(trendLocationModel.getLocation());
        holder.cityName.setText(trendLocationModel.getCity());
        holder.countryName.setText(trendLocationModel.getCountry());
        holder.generalManager.setText(trendLocationModel.getGeneral_manager());
        holder.rank.setText("" + trendLocationModel.getRank());
        ReportTrendLocationAdapter2 trendLocationAdapter2 = new ReportTrendLocationAdapter2(context, trendLocationModel.getRounds());
        holder.recyclerViewTrendLocation.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerViewTrendLocation.setAdapter(trendLocationAdapter2);

    }

    @Override
    public int getItemCount() {
        return orderData.size();
    }

    public class ReportTrendLocationViewHolder extends RecyclerView.ViewHolder {

        TextView hotelName;
        TextView cityName;
        TextView countryName;
        TextView generalManager;
        TextView rank;
        RecyclerView recyclerViewTrendLocation;

        public ReportTrendLocationViewHolder(View itemView) {
            super(itemView);

            hotelName = (TextView)itemView.findViewById(R.id.hotel_name);
            cityName = (TextView)itemView.findViewById(R.id.city_name);
            countryName = (TextView)itemView.findViewById(R.id.country_name);
            generalManager = (TextView)itemView.findViewById(R.id.general_manager);
            rank = (TextView)itemView.findViewById(R.id.trend_location_rank);
            recyclerViewTrendLocation = (RecyclerView)itemView.findViewById(R.id.recycler_view_trend_location);
        }
    }
}
