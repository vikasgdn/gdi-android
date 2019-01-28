package com.gdi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.model.dashboard.LowestDepartmentInfo;
import com.gdi.utils.AppUtils;

import java.util.ArrayList;

public class DashBoardLowestDeptAdapter extends
        RecyclerView.Adapter<DashBoardLowestDeptAdapter.DashBoardLowestDeptViewHolder3> {

    private Context context;
    private ArrayList<LowestDepartmentInfo> orderData;
    private boolean expand = false;
    private static final String TAG = DashBoardHighestDeptAdapter.class.getSimpleName();

    public DashBoardLowestDeptAdapter(Context context, ArrayList<LowestDepartmentInfo> orderData) {
        this.context = context;
        this.orderData = orderData;
    }

    @Override
    public DashBoardLowestDeptViewHolder3 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_lowest_dept_layout,
                parent, false);

        return new DashBoardLowestDeptViewHolder3(view);
    }

    @Override
    public void onBindViewHolder(final DashBoardLowestDeptViewHolder3 holder, final int position) {
        final LowestDepartmentInfo lowestDepartmentInfo = orderData.get(position);
        holder.txt1.setText(lowestDepartmentInfo.getSection_group_name());
        holder.txt2.setText(lowestDepartmentInfo.getSection_name());
        AppUtils.setScoreColor(lowestDepartmentInfo.getScore(), holder.score, context);
        holder.score.setText(lowestDepartmentInfo.getScore());

    }

    @Override
    public int getItemCount() {
        return orderData.size();
    }

    public class DashBoardLowestDeptViewHolder3 extends RecyclerView.ViewHolder {

        TextView txt1;
        TextView txt2;
        TextView score;

        public DashBoardLowestDeptViewHolder3(View itemView) {
            super(itemView);

            txt1 = itemView.findViewById(R.id.lowest1);
            txt2 = itemView.findViewById(R.id.dashboard_lowest1);
            score = itemView.findViewById(R.id.dashboard_lowest_score1);

        }
    }
}