package com.gdi.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.model.actionplan.ActionPlanModel;
import com.gdi.model.sectiongroup.SectionGroupInfo;

import java.util.ArrayList;

public class SectionGroupAdapter extends RecyclerView.Adapter<SectionGroupAdapter.SectionGroupViewHolder> {

    private Context context;
    private ArrayList<SectionGroupInfo> orderData;

    public SectionGroupAdapter(Context context, ArrayList<SectionGroupInfo> orderData) {
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
        SectionGroupInfo sectionGroupInfo = orderData.get(position);
        holder.hotelName.setText(sectionGroupInfo.getLocation());
        holder.cityName.setText(sectionGroupInfo.getCity());
        holder.countryName.setText(sectionGroupInfo.getCountry());
        holder.generalManager.setText(sectionGroupInfo.getGeneral_manager());
        holder.overallScore.setText(sectionGroupInfo.getOverall_score());
        holder.frontOfficeScore.setText(sectionGroupInfo.getFront_office());
        holder.guestServicesTelephoneScore.setText(sectionGroupInfo.getGuest_services_telephone());
        holder.conciergeScore.setText(sectionGroupInfo.getConcierge());
        holder.housekeepingScore.setText(sectionGroupInfo.getHousekeeping());
        holder.productScore.setText(sectionGroupInfo.getProduct());
        holder.foodBeverageScore.setText(sectionGroupInfo.getFood_beverage());
        holder.recreationScore.setText(sectionGroupInfo.getRecreation());
    }

    @Override
    public int getItemCount() {
        return orderData.size();
    }

    public class SectionGroupViewHolder extends RecyclerView.ViewHolder {

        TextView hotelName;
        TextView cityName;
        TextView countryName;
        TextView generalManager;
        TextView overallScore;
        TextView frontOfficeScore;
        TextView guestServicesTelephoneScore;
        TextView conciergeScore;
        TextView housekeepingScore;
        TextView productScore;
        TextView foodBeverageScore;
        TextView recreationScore;

        public SectionGroupViewHolder(View itemView) {
            super(itemView);

            hotelName = (TextView)itemView.findViewById(R.id.hotel_name);
            cityName = (TextView)itemView.findViewById(R.id.city_name);
            countryName = (TextView)itemView.findViewById(R.id.country_name);
            generalManager = (TextView)itemView.findViewById(R.id.general_manager);
            overallScore = (TextView)itemView.findViewById(R.id.overall_score);
            frontOfficeScore = (TextView)itemView.findViewById(R.id.front_office_score);
            guestServicesTelephoneScore = (TextView)itemView.findViewById(R.id.guest_services_telephone_score);
            conciergeScore = (TextView)itemView.findViewById(R.id.concierge_score);
            housekeepingScore = (TextView)itemView.findViewById(R.id.housekeeping_score);
            productScore = (TextView)itemView.findViewById(R.id.product_score);
            foodBeverageScore = (TextView)itemView.findViewById(R.id.food_beverage_score);
            recreationScore = (TextView)itemView.findViewById(R.id.recreation_score);
        }
    }
}
