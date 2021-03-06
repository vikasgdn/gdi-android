package com.gdi.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gdi.R;
import com.gdi.activity.ImageViewActivity;
import com.gdi.model.reportdetailedsummary.AttachmentsInfo;
import com.gdi.utils.AppPrefs;
import com.gdi.utils.Headers;

import java.util.ArrayList;

public class DetailSummaryAdapter4 extends
        RecyclerView.Adapter<DetailSummaryAdapter4.DetailSummaryViewHolder4> {

    private Context context;
    private ArrayList<AttachmentsInfo> data;

    public DetailSummaryAdapter4(Context context, ArrayList<AttachmentsInfo> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public DetailSummaryViewHolder4 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detailed_summary_layout4, parent, false);

        return new DetailSummaryViewHolder4(view);
    }

    @Override
    public void onBindViewHolder(DetailSummaryViewHolder4 holder, int position) {
        final AttachmentsInfo attachmentsInfo = data.get(position);
        Glide.with(context)
                .load(Headers.getUrlWithHeaders(attachmentsInfo.getThumb_url(), AppPrefs.getAccessToken(context)))
                .into(holder.ivImage);
        holder.tvImageDescription.setText(attachmentsInfo.getDescription());
        holder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ImageViewActivity.class);
                intent.putExtra("fileUrl", attachmentsInfo.getFile_url());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class DetailSummaryViewHolder4 extends RecyclerView.ViewHolder {

        ImageView ivImage;
        TextView tvImageDescription;

        public DetailSummaryViewHolder4(View itemView) {
            super(itemView);

            ivImage = itemView.findViewById(R.id.iv_image_detailed_summary);
            tvImageDescription = itemView.findViewById(R.id.tv_image_description);

        }
    }
}
