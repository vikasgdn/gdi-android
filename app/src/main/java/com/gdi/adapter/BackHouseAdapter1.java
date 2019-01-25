package com.gdi.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.activity.ReportAudioImageActivity;
import com.gdi.activity.ReportBackHouseActivity;
import com.gdi.model.SampleModel;
import com.gdi.model.audioimages.AudioImageInfo;
import com.gdi.model.backhouse.BackHouseInfo;

import java.util.ArrayList;

public class BackHouseAdapter1 extends
        RecyclerView.Adapter<BackHouseAdapter1.BackHouseViewHolder1> {

    private Context context;
    private ArrayList<BackHouseInfo> data;
    private boolean expand = false;

    public BackHouseAdapter1(Context context, ArrayList<BackHouseInfo> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public BackHouseViewHolder1 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.back_house_layout,
                parent, false);

        return new BackHouseViewHolder1(view);
    }

    @Override
    public void onBindViewHolder(final BackHouseViewHolder1 holder, int position) {
        //TODO : Static data testing

        final BackHouseInfo backHouseInfo = data.get(position);
        holder.tvBackHouseTitle.setText(backHouseInfo.getLocation_name() + " | " + backHouseInfo.getCity_name());
        BackHouseAdapter2 backHouseAdapter2 = new BackHouseAdapter2(context, backHouseInfo.getQuestions());
        holder.recyclerViewBackHouse.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerViewBackHouse.setAdapter(backHouseAdapter2);
        holder.rlBackHouseExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.recyclerViewBackHouse.setVisibility(View.VISIBLE);
                if (!expand){
                    expand = true;
                    holder.recyclerViewBackHouse.setVisibility(View.VISIBLE);
                    holder.ivExpandIcon.setImageResource(R.drawable.compress_icon);

                }else if(expand){
                    expand = false;
                    holder.recyclerViewBackHouse.setVisibility(View.GONE);
                    holder.ivExpandIcon.setImageResource(R.drawable.expand_icon);
                }
            }
        });

        holder.pdfIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ReportBackHouseActivity)context).downloadPdf(backHouseInfo.getReport_urls().getPdf());
            }
        });

        holder.mailIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ReportBackHouseActivity)context).emailAttachment(backHouseInfo.getReport_urls().getEmail());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class BackHouseViewHolder1 extends RecyclerView.ViewHolder {


        TextView tvBackHouseTitle;
        RelativeLayout rlBackHouseExpand;
        RecyclerView recyclerViewBackHouse;
        ImageView pdfIcon;
        ImageView mailIcon;
        ImageView ivExpandIcon;

        public BackHouseViewHolder1(View itemView) {
            super(itemView);

            tvBackHouseTitle = itemView.findViewById(R.id.tv_back_house_title);
            rlBackHouseExpand = itemView.findViewById(R.id.rl_back_house_expand);
            ivExpandIcon = itemView.findViewById(R.id.iv_expand_icon);
            recyclerViewBackHouse = itemView.findViewById(R.id.recycler_view_back_house);
            pdfIcon = itemView.findViewById(R.id.pdf_icon);
            mailIcon = itemView.findViewById(R.id.mail_icon);

        }
    }
}
