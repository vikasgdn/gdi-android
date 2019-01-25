package com.gdi.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.model.SampleModel;
import com.gdi.model.highlights.QuestionsInfo;

import java.util.ArrayList;

public class HighlightAdapter2 extends RecyclerView.Adapter<HighlightAdapter2.HighlightViewHolder2> {

    private Context context;
    private ArrayList<QuestionsInfo> orderData;
    private ArrayList<SampleModel> sampleOrderData;
    private static final String TAG = AuditAdapter.class.getSimpleName();

    public HighlightAdapter2(Context context, ArrayList<QuestionsInfo> orderData) {
        this.context = context;
        this.orderData = orderData;
    }

    //TODO : Static data testing
    /*public HighlightAdapter2(Context context, ArrayList<SampleModel> sampleOrderData) {
        this.context = context;
        this.sampleOrderData = sampleOrderData;
    }*/

    @Override
    public HighlightViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.highlight_layout2,
                parent, false);

        return new HighlightViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(HighlightViewHolder2 holder, int position) {
        QuestionsInfo questionsInfo = orderData.get(position);
        holder.highlightDetailText.setText(questionsInfo.getQuestion());
        holder.highlightDetailTextSummary.setText(questionsInfo.getAnswer());
    }

    @Override
    public int getItemCount() {
        return orderData.size();
    }

    public class HighlightViewHolder2 extends RecyclerView.ViewHolder {

        TextView highlightDetailText;
        RelativeLayout expand;
        TextView highlightDetailTextSummary;

        public HighlightViewHolder2(View itemView) {
            super(itemView);

            highlightDetailText = itemView.findViewById(R.id.highlightDetailText);
            expand = itemView.findViewById(R.id.expandLayout);
            highlightDetailTextSummary = itemView.findViewById(R.id.highlightDetailTextSummary);

        }
    }
}