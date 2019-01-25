package com.gdi.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.attachmentactivity.SectionGroupDetailActivity;
import com.gdi.model.sectiongroup.SectionGroupInfo;
import com.gdi.model.sectiongroup.SectionGroupLocation;

import java.util.ArrayList;

public class SectionGroupAdapter extends RecyclerView.Adapter<SectionGroupAdapter.SectionGroupViewHolder> {

    private Context context;
    private ArrayList<SectionGroupLocation> orderData;

    public SectionGroupAdapter(Context context, ArrayList<SectionGroupLocation> orderData) {
        this.context = context;
        this.orderData = orderData;
    }

    @NonNull
    @Override
    public SectionGroupAdapter.SectionGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_group_layout,
                parent, false);

        return new SectionGroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionGroupAdapter.SectionGroupViewHolder holder, int position) {
        final SectionGroupLocation sectionGroupLocation = orderData.get(position);
        holder.hotelName.setText(sectionGroupLocation.getLocation());
        holder.cityName.setText(sectionGroupLocation.getCity());
        holder.countryName.setText(sectionGroupLocation.getCountry());
        holder.viewMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SectionGroupDetailActivity.class);
                intent.putExtra("sectionGroupData", sectionGroupLocation);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderData.size();
    }

    public class SectionGroupViewHolder extends RecyclerView.ViewHolder {

        TextView hotelName;
        TextView cityName;
        TextView countryName;
        TextView viewMoreBtn;

        public SectionGroupViewHolder(View itemView) {
            super(itemView);

            hotelName = (TextView)itemView.findViewById(R.id.tv_hotel_name);
            cityName = (TextView)itemView.findViewById(R.id.tv_city);
            countryName = (TextView)itemView.findViewById(R.id.tv_country);
            viewMoreBtn = (TextView)itemView.findViewById(R.id.tv_view_more_btn);
        }
    }
}
