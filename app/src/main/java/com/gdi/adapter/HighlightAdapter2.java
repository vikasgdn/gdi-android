package com.gdi.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.model.SampleModel;
import com.gdi.model.reporthighlights.QuestionsInfo;

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
        String answer_text = Html.fromHtml(questionsInfo.getAnswer()).toString();
        holder.highlightDetailTextSummary.setText(answer_text);
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
