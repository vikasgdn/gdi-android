package com.gdi.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gdi.R;
import com.gdi.activity.HotelDetailSummaryActivity;
import com.gdi.activity.ImageViewActivity;
import com.gdi.activity.ReportDetailSummaryActivity;
import com.gdi.model.SampleModel;
import com.gdi.model.detailedsummary.AttachmentsInfo;
import com.gdi.model.detailedsummary.SectionsInfo;
import com.gdi.utils.AppPrefs;
import com.gdi.utils.Headers;

import java.util.ArrayList;

public class DetailSummaryAdapter4 extends
        RecyclerView.Adapter<DetailSummaryAdapter4.DetailSummaryViewHolder4> {

    private Context context;
    private ArrayList<AttachmentsInfo> data;
    private ArrayList<SampleModel> sampleOrderData;
    private static final String TAG = DetailSummaryAdapter4.class.getSimpleName();

    public DetailSummaryAdapter4(Context context, ArrayList<AttachmentsInfo> data) {
        this.context = context;
        this.data = data;
    }

    //TODO : Static data testing
    /*public DetailSummaryAdapter3(Context context, ArrayList<SampleModel> sampleOrderData) {
        this.context = context;
        this.sampleOrderData = sampleOrderData;
    }*/

    @Override
    public DetailSummaryViewHolder4 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detailed_summary_layout4, parent, false);

        return new DetailSummaryViewHolder4(view);
    }

    @Override
    public void onBindViewHolder(DetailSummaryViewHolder4 holder, int position) {
        //TODO : Static data testing
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
