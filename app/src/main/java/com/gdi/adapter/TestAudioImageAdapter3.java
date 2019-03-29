package com.gdi.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gdi.R;
import com.gdi.activity.ImageViewActivity;
import com.gdi.activity.PlayAudioActivity;
import com.gdi.model.reportaudioimages.AttachmentAudioImages;
import com.gdi.utils.AppPrefs;
import com.gdi.utils.AppUtils;
import com.gdi.utils.Headers;

import java.util.ArrayList;

public class TestAudioImageAdapter3 extends
        RecyclerView.Adapter<TestAudioImageAdapter3.TestAudioImageViewHolder3> {

    private Context context;
    private ArrayList<AttachmentAudioImages> data;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;

    public TestAudioImageAdapter3(Context context, ArrayList<AttachmentAudioImages> data, RecyclerView recyclerView) {
        this.context = context;
        this.data = data;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    @Override
    public TestAudioImageViewHolder3 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.audio_image_layout3,
                parent, false);

        return new TestAudioImageViewHolder3(view);
    }

    @Override
    public void onBindViewHolder(final TestAudioImageViewHolder3 holder, int position) {
        final AttachmentAudioImages attachmentAudioImages = data.get(position);
        String fileType = attachmentAudioImages.getFile_type();
        if (fileType.contains("image/")){
            holder.imageLayout.setVisibility(View.VISIBLE);
            holder.audioPlayLayout.setVisibility(View.GONE);
            if (!AppUtils.isStringEmpty(attachmentAudioImages.getThumb_url())) {
                Glide.with(context)
                        .load(Headers.getUrlWithHeaders(attachmentAudioImages.getThumb_url(),
                                AppPrefs.getAccessToken(context)))
                        .into(holder.ivImage);
            }
            if (!AppUtils.isStringEmpty(attachmentAudioImages.getFile_url())){
                holder.ivImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, ImageViewActivity.class);
                        intent.putExtra("fileUrl", attachmentAudioImages.getFile_url());
                        context.startActivity(intent);
                    }
                });
            }
        }else {
            holder.audioPlayLayout.setVisibility(View.VISIBLE);
            holder.imageLayout.setVisibility(View.GONE);

            if (!AppUtils.isStringEmpty(attachmentAudioImages.getFile_url())){
                holder.audioPlayLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, PlayAudioActivity.class);
                        intent.putExtra("audioUrl", attachmentAudioImages.getFile_url());
                        context.startActivity(intent);
                    }
                });
                /*holder.audioPlayLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((ReportAudioImageActivity)context).downloadAudio(attachmentAudioImages.getFile_url());
                    }
                });*/
            }
        }

        holder.tvImageAudioDescription.setText(String.valueOf(attachmentAudioImages.getDescription()));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class TestAudioImageViewHolder3 extends RecyclerView.ViewHolder {

        RelativeLayout audioPlayLayout;
        RelativeLayout imageLayout;
        ImageView ivImage;
        ImageView ivAudioPlayBtn;
        TextView tvImageAudioDescription;

        public TestAudioImageViewHolder3(View itemView) {
            super(itemView);

            tvImageAudioDescription = itemView.findViewById(R.id.tv_image_audio_description);
            audioPlayLayout = itemView.findViewById(R.id.audio_play_layout);
            imageLayout = itemView.findViewById(R.id.image_layout);
            ivImage = itemView.findViewById(R.id.iv_image);
            ivAudioPlayBtn = itemView.findViewById(R.id.iv_audio_play_btn);
        }
    }

    public void setLoaded() {
        isLoading = false;
    }


    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}
