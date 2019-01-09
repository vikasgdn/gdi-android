package com.gdi.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.model.SampleModel;
import com.gdi.model.overallbrand.SectionsInfo;

import java.util.ArrayList;

public class DepartmentalAdapter2 extends
        RecyclerView.Adapter<DepartmentalAdapter2.DepartmentalViewHolder2> {

    private Context context;
    private ArrayList<SectionsInfo> orderData;
    private ArrayList<SampleModel> sampleOrderData;
    private static final String TAG = DepartmentalAdapter2.class.getSimpleName();

    public DepartmentalAdapter2(Context context, ArrayList<SectionsInfo> orderData) {
        this.context = context;
        this.orderData = orderData;
    }

    //TODO : Static data testing
    /*public DepartmentalAdapter2(Context context, ArrayList<SampleModel> sampleOrderData) {
        this.context = context;
        this.sampleOrderData = sampleOrderData;
    }*/

    @Override
    public DepartmentalViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.departmental_layout2,
                parent, false);

        return new DepartmentalViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(DepartmentalViewHolder2 holder, int position) {
        SectionsInfo sectionsInfo = orderData.get(position);
        holder.departmentalDetailText.setText(sectionsInfo.getSection_name());
        holder.score.setText("Avg. score : " + sectionsInfo.getScore());
        //TODO : Static data testing
        ArrayList<SampleModel> sampleModels = SampleModel.createList(5);
        DepartmentalAdapter3 departmentalAdapter3 = new DepartmentalAdapter3(context, sectionsInfo.getLocations());
        holder.departmentalList.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false));
        holder.departmentalList.setAdapter(departmentalAdapter3);
    }

    @Override
    public int getItemCount() {
        return orderData.size();
    }

    public class DepartmentalViewHolder2 extends RecyclerView.ViewHolder {

        TextView departmentalDetailText;
        RecyclerView departmentalList;
        TextView score;

        public DepartmentalViewHolder2(View itemView) {
            super(itemView);

            departmentalDetailText = itemView.findViewById(R.id.depatmental_detail);
            departmentalList = itemView.findViewById(R.id.departmental_recycler);
            score = itemView.findViewById(R.id.score_text);
        }
    }
}
