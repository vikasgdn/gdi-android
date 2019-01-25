package com.gdi.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.model.sectiongroup.SectionGroupModel;

import java.util.ArrayList;

public class SectionGroupAdapter3 extends RecyclerView.Adapter<SectionGroupAdapter3.SectionGroupViewHolder3> {

    private Context context;
    private ArrayList<SectionGroupModel> orderData;

    public SectionGroupAdapter3(Context context, ArrayList<SectionGroupModel> orderData) {
        this.context = context;
        this.orderData = orderData;
    }

    @NonNull
    @Override
    public SectionGroupAdapter3.SectionGroupViewHolder3 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_group_layout3,
                parent, false);

        return new SectionGroupViewHolder3(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionGroupAdapter3.SectionGroupViewHolder3 holder, int position) {
        final SectionGroupModel sectionGroupModel = orderData.get(position);
        holder.title.setText(sectionGroupModel.getSection_group_name());
        holder.averageScore.setText(sectionGroupModel.getScore());
    }

    @Override
    public int getItemCount() {
        return orderData.size();
    }

    public class SectionGroupViewHolder3 extends RecyclerView.ViewHolder {

        TextView averageScore;
        TextView title;

        public SectionGroupViewHolder3(View itemView) {
            super(itemView);

            averageScore = (TextView)itemView.findViewById(R.id.tv_average_score);
            title = (TextView)itemView.findViewById(R.id.tv_title);
        }
    }
}
