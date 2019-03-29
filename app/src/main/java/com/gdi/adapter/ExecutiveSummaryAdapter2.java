package com.gdi.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gdi.R;
import com.gdi.activity.ImageViewActivity;
import com.gdi.model.SampleModel;
import com.gdi.model.reportexecutivesummary.ExecutiveAttachmentsInfo;
import com.gdi.utils.AppPrefs;
import com.gdi.utils.Headers;

import java.util.ArrayList;

public class ExecutiveSummaryAdapter2 extends
        RecyclerView.Adapter<ExecutiveSummaryAdapter2.ExecutiveSummaryViewHolder2> {

    private Context context;
    private ArrayList<ExecutiveAttachmentsInfo> data;

    public ExecutiveSummaryAdapter2(Context context, ArrayList<ExecutiveAttachmentsInfo> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public ExecutiveSummaryViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detailed_summary_layout4, parent, false);

        return new ExecutiveSummaryViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(ExecutiveSummaryViewHolder2 holder, int position) {
        //TODO : Static data testing
        final ExecutiveAttachmentsInfo executiveAttachmentsInfo = data.get(position);
        Glide.with(context)
                .load(Headers.getUrlWithHeaders(executiveAttachmentsInfo.getThumb_url(), AppPrefs.getAccessToken(context)))
                .into(holder.ivImage);
        holder.tvImageDescription.setText(executiveAttachmentsInfo.getDescription());
        holder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ImageViewActivity.class);
                intent.putExtra("fileUrl", executiveAttachmentsInfo.getFile_url());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ExecutiveSummaryViewHolder2 extends RecyclerView.ViewHolder {

        ImageView ivImage;
        TextView tvImageDescription;

        public ExecutiveSummaryViewHolder2(View itemView) {
            super(itemView);

            ivImage = itemView.findViewById(R.id.iv_image_detailed_summary);
            tvImageDescription = itemView.findViewById(R.id.tv_image_description);

        }
    }
}
