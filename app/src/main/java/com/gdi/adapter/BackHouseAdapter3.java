package com.gdi.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.model.reportbackhouse.BackHouseOption;

import java.util.ArrayList;

public class BackHouseAdapter3 extends
        RecyclerView.Adapter<BackHouseAdapter3.BackHouseViewHolder3> {

    private Context context;
    private ArrayList<BackHouseOption> data;

    public BackHouseAdapter3(Context context, ArrayList<BackHouseOption> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public BackHouseViewHolder3 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.back_house_layout3,
                parent, false);

        return new BackHouseViewHolder3(view);
    }

    @Override
    public void onBindViewHolder(final BackHouseViewHolder3 holder, int position) {

        final BackHouseOption backHouseOption = data.get(position);
        holder.radioText.setText(String.valueOf(backHouseOption.getOption_text()));
        if (backHouseOption.getOption_mark() == 0) {

            if (backHouseOption.getSelected() == 1) {
                holder.radioText.setTextColor(context.getResources().getColor(R.color.colorPink));
                holder.radioImage.setImageResource(R.drawable.radio_checked);
            }else {
                holder.radioText.setTextColor(context.getResources().getColor(R.color.colorBlack));
                holder.radioImage.setImageResource(R.drawable.radio_unchecked);
            }
        }else {
            holder.radioText.setTextColor(context.getResources().getColor(R.color.colorBlack));
            if (backHouseOption.getSelected() == 1) {
                holder.radioImage.setImageResource(R.drawable.radio_checked);
            }else {
                holder.radioImage.setImageResource(R.drawable.radio_unchecked);
            }
        }


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class BackHouseViewHolder3 extends RecyclerView.ViewHolder {

        ImageView radioImage;
        TextView radioText;

        public BackHouseViewHolder3(View itemView) {
            super(itemView);

            radioImage = itemView.findViewById(R.id.radio_image);
            radioText = itemView.findViewById(R.id.radio_text);

        }
    }
}
