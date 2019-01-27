package com.gdi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.model.dashboard.HighestDeparmentInfo;

import java.util.ArrayList;

public class DashBoardHighestDeptAdapter extends
        RecyclerView.Adapter<DashBoardHighestDeptAdapter.DashBoardHighestDeptViewHolder3> {

    private Context context;
    private ArrayList<HighestDeparmentInfo> orderData;
    private boolean expand = false;
    private static final String TAG = DashBoardHighestDeptAdapter.class.getSimpleName();

    public DashBoardHighestDeptAdapter(Context context, ArrayList<HighestDeparmentInfo> orderData) {
        this.context = context;
        this.orderData = orderData;
    }

    @Override
    public DashBoardHighestDeptViewHolder3 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_highest_dept_layout,
                parent, false);

        return new DashBoardHighestDeptViewHolder3(view);
    }

    @Override
    public void onBindViewHolder(final DashBoardHighestDeptViewHolder3 holder, final int position) {
        final HighestDeparmentInfo highestDeparmentInfo = orderData.get(position);
        holder.txt1.setText(highestDeparmentInfo.getSection_group_name());
        holder.txt2.setText(highestDeparmentInfo.getSection_name());
        holder.score.setText(highestDeparmentInfo.getScore());

    }

    @Override
    public int getItemCount() {
        return orderData.size();
    }

    public class DashBoardHighestDeptViewHolder3 extends RecyclerView.ViewHolder {

        TextView txt1;
        TextView txt2;
        TextView score;

        public DashBoardHighestDeptViewHolder3(View itemView) {
            super(itemView);

            txt1 = itemView.findViewById(R.id.highest1);
            txt2 = itemView.findViewById(R.id.dashboard_highest1);
            score = itemView.findViewById(R.id.dashboard_highest_score1);

        }
    }
}
