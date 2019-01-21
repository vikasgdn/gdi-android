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
import com.gdi.activity.ReportHighlightActivity;
import com.gdi.model.SampleModel;
import com.gdi.model.highlights.LocationsInfo;

import java.util.ArrayList;

public class HighlightAdapter1 extends RecyclerView.Adapter<HighlightAdapter1.HighlightViewHolder> {

    private Context context;
    private ArrayList<LocationsInfo> orderData;
    private ArrayList<SampleModel> sampleOrderData;
    private boolean expand = false;
    private static final String TAG = AuditAdapter.class.getSimpleName();

    public HighlightAdapter1(Context context, ArrayList<LocationsInfo> orderData) {
        this.context = context;
        this.orderData = orderData;
    }

    //TODO : Static data testing
    /*public HighlightAdapter1(Context context, ArrayList<SampleModel> sampleOrderData) {
        this.context = context;
        this.sampleOrderData = sampleOrderData;
    }*/

    @Override
    public HighlightViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.highlight_layout,
                parent, false);

        return new HighlightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final HighlightViewHolder holder, int position) {
        //TODO : Static data testing
        /*holder.expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!expand){
                    expand = true;
                    holder.highlightList.setVisibility(View.VISIBLE);
                    holder.expandIcon.setImageResource(R.drawable.compress_icon);
                    ArrayList<SampleModel> sampleModels = SampleModel.createList(5);
                    HighlightAdapter2 highlightAdapter2 = new HighlightAdapter2(context, sampleModels);
                    holder.highlightList.setLayoutManager(new LinearLayoutManager(context,
                            LinearLayoutManager.VERTICAL, false));
                    holder.highlightList.setAdapter(highlightAdapter2);
                }else if (expand){
                    expand = false;
                    holder.highlightList.setVisibility(View.GONE);
                    holder.expandIcon.setImageResource(R.drawable.expand_icon);
                }

            }
        });*/
        final LocationsInfo locationsInfo = orderData.get(position);
        holder.highlightDetailText.setText(locationsInfo.getLocation_name());

        holder.expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!expand){
                    expand = true;
                    holder.highlightList.setVisibility(View.VISIBLE);
                    holder.expandIcon.setImageResource(R.drawable.compress_icon);
                    HighlightAdapter2 highlightAdapter2 = new HighlightAdapter2(context, locationsInfo.getQuestions());
                    holder.highlightList.setLayoutManager(new LinearLayoutManager(context,
                            LinearLayoutManager.VERTICAL, false));
                    holder.highlightList.setAdapter(highlightAdapter2);
                }else if (expand){
                    expand = false;
                    holder.highlightList.setVisibility(View.GONE);
                    holder.expandIcon.setImageResource(R.drawable.expand_icon);
                }

            }
        });

        holder.pdfIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ReportHighlightActivity)context).downloadPdf(locationsInfo.getReport_urls().getPdf());
            }
        });

        holder.mailIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ReportHighlightActivity)context).emailAttachment(locationsInfo.getReport_urls().getEmail());
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderData.size();
    }

    public class HighlightViewHolder extends RecyclerView.ViewHolder {

        TextView highlightDetailText;
        RelativeLayout expand;
        ImageView expandIcon;
        RecyclerView highlightList;
        ImageView pdfIcon;
        ImageView mailIcon;

        public HighlightViewHolder(View itemView) {
            super(itemView);

            highlightDetailText = itemView.findViewById(R.id.highlightDetailText);
            expand = itemView.findViewById(R.id.expandLayout);
            expandIcon = itemView.findViewById(R.id.expand_icon);
            highlightList = itemView.findViewById(R.id.highlight_recycler);
            pdfIcon = itemView.findViewById(R.id.pdf_icon);
            mailIcon = itemView.findViewById(R.id.mail_icon);

        }
    }
}
