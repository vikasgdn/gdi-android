package com.gdi.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.model.competetionbenchmarking.Ranking;

import java.util.ArrayList;

public class CompCityCompsetAdapter extends RecyclerView.Adapter<CompCityCompsetAdapter.CompCityCompsetViewHolder3> {

    private Context context;
    private ArrayList<Ranking> orderData;

    public CompCityCompsetAdapter(Context context, ArrayList<Ranking> orderData) {
        this.context = context;
        this.orderData = orderData;
    }

    @NonNull
    @Override
    public CompCityCompsetAdapter.CompCityCompsetViewHolder3 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_compset_layout,
                parent, false);

        return new CompCityCompsetViewHolder3(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompCityCompsetAdapter.CompCityCompsetViewHolder3 holder, int position) {
        Ranking ranking = orderData.get(position);
        holder.hotelName.setText(ranking.getHotel_name());
        holder.hotelScore.setText(ranking.getScore());
        if (ranking.isIs_my_hotel()){
            holder.hotelScore.setBackground(context.getResources().getDrawable(R.drawable.circle_city_compset_hotel_score));
        }else {
            holder.hotelScore.setBackground(context.getResources().getDrawable(R.drawable.golden_circle_city_compset_hotel_score));
        }
    }

    @Override
    public int getItemCount() {
        return orderData.size();
    }

    public class CompCityCompsetViewHolder3 extends RecyclerView.ViewHolder {

        TextView hotelName;
        TextView hotelScore;

        public CompCityCompsetViewHolder3(View itemView) {
            super(itemView);

            hotelName = (TextView)itemView.findViewById(R.id.hotel_name);
            hotelScore = (TextView)itemView.findViewById(R.id.hotel_score);
        }
    }
}
