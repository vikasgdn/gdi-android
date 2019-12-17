package com.gdi.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.model.dashboard.IADeparmentInfo;
import com.gdi.utils.AppUtils;

import java.util.ArrayList;

public class IADashBoardHighestDeptAdapter extends
        RecyclerView.Adapter<IADashBoardHighestDeptAdapter.DashBoardHighestDeptViewHolder3> {

    private Context context;
    private ArrayList<IADeparmentInfo> orderData;

    public IADashBoardHighestDeptAdapter(Context context, ArrayList<IADeparmentInfo> orderData) {
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
        final IADeparmentInfo highestDeparmentInfo = orderData.get(position);
        if (position == 0){
            holder.lineView.setVisibility(View.GONE);
        }else {
            holder.lineView.setVisibility(View.VISIBLE);
        }
        holder.txt1.setText(highestDeparmentInfo.getSection_group_title());
        holder.txt2.setText(highestDeparmentInfo.getSection_title());
        AppUtils.setScoreColor(highestDeparmentInfo.getScore(), holder.score, context);
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
        View lineView;

        public DashBoardHighestDeptViewHolder3(View itemView) {
            super(itemView);

            txt1 = itemView.findViewById(R.id.highest1);
            txt2 = itemView.findViewById(R.id.dashboard_highest1);
            score = itemView.findViewById(R.id.dashboard_highest_score1);
            lineView = itemView.findViewById(R.id.line_view);

        }
    }
}
