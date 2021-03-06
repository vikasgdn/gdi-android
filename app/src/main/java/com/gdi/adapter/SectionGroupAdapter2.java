package com.gdi.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.model.sectiongroup.SectionGroupModel;
import com.gdi.utils.AppUtils;

import java.util.ArrayList;

public class SectionGroupAdapter2 extends RecyclerView.Adapter<SectionGroupAdapter2.SectionGroupViewHolder> {

    private Context context;
    private ArrayList<SectionGroupModel> orderData;

    public SectionGroupAdapter2(Context context, ArrayList<SectionGroupModel> orderData) {
        this.context = context;
        this.orderData = orderData;
    }

    @NonNull
    @Override
    public SectionGroupAdapter2.SectionGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_group_layout2,
                parent, false);

        return new SectionGroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionGroupAdapter2.SectionGroupViewHolder holder, int position) {
        final SectionGroupModel sectionGroupModel = orderData.get(position);
        holder.title.setText(sectionGroupModel.getSection_group_name());
        AppUtils.setScoreColor(sectionGroupModel.getScore(), holder.averageScore, context);
        holder.averageScore.setText(sectionGroupModel.getScore());
    }

    @Override
    public int getItemCount() {
        return orderData.size();
    }

    public class SectionGroupViewHolder extends RecyclerView.ViewHolder {

        TextView averageScore;
        TextView title;

        public SectionGroupViewHolder(View itemView) {
            super(itemView);

            averageScore = itemView.findViewById(R.id.tv_average_score);
            title = itemView.findViewById(R.id.tv_title);
        }
    }
}
