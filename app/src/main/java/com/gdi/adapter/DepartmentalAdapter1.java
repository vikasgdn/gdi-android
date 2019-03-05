package com.gdi.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.model.SampleModel;
import com.gdi.model.overallbrand.DepartmentOverallInfo;
import com.gdi.utils.AppUtils;

import java.util.ArrayList;

public class DepartmentalAdapter1 extends
        RecyclerView.Adapter<DepartmentalAdapter1.DepartmentalViewHolder1> {

    private Context context;
    private ArrayList<DepartmentOverallInfo> orderData;
    private ArrayList<SampleModel> sampleOrderData;
    private boolean expand = false;
    private static final String TAG = DepartmentalAdapter1.class.getSimpleName();

    public DepartmentalAdapter1(Context context, ArrayList<DepartmentOverallInfo> orderData) {
        this.context = context;
        this.orderData = orderData;
    }

    //TODO : Static data testing
    /*public DepartmentalAdapter1(Context context, ArrayList<SampleModel> sampleOrderData) {
        this.context = context;
        this.sampleOrderData = sampleOrderData;
    }*/

    @Override
    public DepartmentalViewHolder1 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.departmental_layout,
                parent, false);

        return new DepartmentalViewHolder1(view);
    }

    @Override
    public void onBindViewHolder(final DepartmentalViewHolder1 holder, final int position) {
        //TODO : Static data testing
        final DepartmentOverallInfo departmentOverallInfo = orderData.get(position);
        holder.departmentalDetailText.setText(departmentOverallInfo.getSection_group_name());
        AppUtils.setScoreColor(departmentOverallInfo.getScore(), holder.score, context);
        holder.score.setText("Avg. Score : " + departmentOverallInfo.getScore());
        DepartmentalAdapter2 departmentalAdapter2 = new DepartmentalAdapter2(context, departmentOverallInfo.getSections());
        holder.departmentalList.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false));
        holder.departmentalList.setAdapter(departmentalAdapter2);

        if(!orderData.get(position).isExpand()){
            holder.departmentalList.setVisibility(View.GONE);
            holder.expandIcon.setImageResource(R.drawable.expand_icon);
            orderData.get(position).setExpand(false);
        }else {
            holder.departmentalList.setVisibility(View.VISIBLE);
            holder.expandIcon.setImageResource(R.drawable.compress_icon);
            orderData.get(position).setExpand(true);
        }

        holder.departmentExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(orderData.get(position).isExpand()){
                    holder.departmentalList.setVisibility(View.GONE);
                    holder.expandIcon.setImageResource(R.drawable.expand_icon);
                    orderData.get(position).setExpand(false);
                }else {
                    holder.departmentalList.setVisibility(View.VISIBLE);
                    holder.expandIcon.setImageResource(R.drawable.compress_icon);
                    orderData.get(position).setExpand(true);
                }

                /*if (!expand){
                    expand = true;
                    holder.departmentalList.setVisibility(View.VISIBLE);
                    holder.expandIcon.setImageResource(R.drawable.compress_icon);

                }else if (expand){
                    expand = false;
                    holder.departmentalList.setVisibility(View.GONE);
                    holder.expandIcon.setImageResource(R.drawable.expand_icon);
                }*/
            }
        });

    }

    @Override
    public int getItemCount() {
        return orderData.size();
    }

    public class DepartmentalViewHolder1 extends RecyclerView.ViewHolder {

        TextView departmentalDetailText;
        RelativeLayout departmentExpand;
        RecyclerView departmentalList;
        TextView score;
        ImageView expandIcon;

        public DepartmentalViewHolder1(View itemView) {
            super(itemView);

            departmentalDetailText = itemView.findViewById(R.id.depatmental_detail);
            departmentExpand = itemView.findViewById(R.id.expand_layout);
            departmentalList = itemView.findViewById(R.id.departmental_recycler);
            score = itemView.findViewById(R.id.score_text);
            expandIcon = itemView.findViewById(R.id.expand_icon);
        }
    }
}
