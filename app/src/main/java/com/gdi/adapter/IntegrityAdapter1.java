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
import com.gdi.activity.MysteryAuditReport.ReportIntegrityActivity;
import com.gdi.model.reportintegrity.IntegrityInfo;

import java.util.ArrayList;

public class IntegrityAdapter1 extends
        RecyclerView.Adapter<IntegrityAdapter1.IntegrityViewHolder1> {

    private Context context;
    private ArrayList<IntegrityInfo> data;
    private boolean expand = false;

    public IntegrityAdapter1(Context context, ArrayList<IntegrityInfo> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public IntegrityViewHolder1 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.integrity_layout,
                parent, false);

        return new IntegrityViewHolder1(view);
    }

    @Override
    public void onBindViewHolder(final IntegrityViewHolder1 holder, final int position) {
        final IntegrityInfo integrityInfo = data.get(position);
        holder.tvIntegrityTitle.setText(integrityInfo.getLocation_name() + " | " + integrityInfo.getCity_name());
        IntegrityAdapter2 audioImageAdapter2 = new IntegrityAdapter2(context, integrityInfo.getIntegrity());
        holder.recyclerViewIntegrity.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerViewIntegrity.setAdapter(audioImageAdapter2);

        if(!data.get(position).isExpand()){
            holder.recyclerViewIntegrity.setVisibility(View.GONE);
            holder.ivExpandIcon.setImageResource(R.drawable.expand_icon);
            data.get(position).setExpand(false);
        }else {
            holder.recyclerViewIntegrity.setVisibility(View.VISIBLE);
            holder.ivExpandIcon.setImageResource(R.drawable.compress_icon);
            data.get(position).setExpand(true);
        }

        holder.rlIntegrityExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(data.get(position).isExpand()){
                    holder.recyclerViewIntegrity.setVisibility(View.GONE);
                    holder.ivExpandIcon.setImageResource(R.drawable.expand_icon);
                    data.get(position).setExpand(false);
                }else {
                    holder.recyclerViewIntegrity.setVisibility(View.VISIBLE);
                    holder.ivExpandIcon.setImageResource(R.drawable.compress_icon);
                    data.get(position).setExpand(true);
                }

                /*if (!expand){
                    expand = true;
                    holder.recyclerViewIntegrity.setVisibility(View.VISIBLE);
                    holder.ivExpandIcon.setImageResource(R.drawable.compress_icon);

                }else if(expand){
                    expand = false;
                    holder.recyclerViewIntegrity.setVisibility(View.GONE);
                    holder.ivExpandIcon.setImageResource(R.drawable.expand_icon);
                }*/
            }
        });

        holder.pdfIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ReportIntegrityActivity)context).downloadPdf(integrityInfo.getReport_urls().getPdf());
            }
        });

        holder.mailIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ReportIntegrityActivity)context).emailAttachment(integrityInfo.getReport_urls().getEmail());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class IntegrityViewHolder1 extends RecyclerView.ViewHolder {


        TextView tvIntegrityTitle;
        RelativeLayout rlIntegrityExpand;
        RecyclerView recyclerViewIntegrity;
        ImageView pdfIcon;
        ImageView mailIcon;
        ImageView ivExpandIcon;

        public IntegrityViewHolder1(View itemView) {
            super(itemView);

            tvIntegrityTitle = itemView.findViewById(R.id.tv_integrity_title);
            rlIntegrityExpand = itemView.findViewById(R.id.rl_integrity_expand);
            ivExpandIcon = itemView.findViewById(R.id.iv_expand_icon);
            recyclerViewIntegrity = itemView.findViewById(R.id.recycler_view_integrity);
            pdfIcon = itemView.findViewById(R.id.pdf_icon);
            mailIcon = itemView.findViewById(R.id.mail_icon);

        }
    }
}
