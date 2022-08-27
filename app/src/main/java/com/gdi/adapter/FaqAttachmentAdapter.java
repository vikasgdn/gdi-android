package com.gdi.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gdi.hotel.mystery.audits.R;
import com.gdi.activity.ImageViewActivity;
import com.gdi.activity.PlayAudioActivity;
import com.gdi.model.reportfaq.FAQAttachment;
import com.gdi.utils.AppPrefs;
import com.gdi.utils.AppUtils;
import com.gdi.utils.Headers;

import java.util.ArrayList;

public class FaqAttachmentAdapter extends RecyclerView.Adapter<FaqAttachmentAdapter.FaqAttachmentViewHolder> {

    private Context context;
    private ArrayList<FAQAttachment> data;

    public FaqAttachmentAdapter(Context context, ArrayList<FAQAttachment> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public FaqAttachmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.integrity_layout3,
                parent, false);

        return new FaqAttachmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FaqAttachmentViewHolder holder, int position) {
        final FAQAttachment faqAttachment = data.get(position);
        String fileType = faqAttachment.getFile_type();
        if (fileType.contains("image/")){
            holder.imageLayout.setVisibility(View.VISIBLE);
            holder.audioPlayLayout.setVisibility(View.GONE);
            if (!AppUtils.isStringEmpty(faqAttachment.getThumb_url())) {
                Glide.with(context)
                        .load(Headers.getUrlWithHeaders(faqAttachment.getThumb_url(), AppPrefs.getAccessToken(context)))
                        .into(holder.attachmentImage);
            }
            if (!AppUtils.isStringEmpty(faqAttachment.getFile_url())){
                holder.attachmentImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, ImageViewActivity.class);
                        intent.putExtra("fileUrl", faqAttachment.getFile_url());
                        context.startActivity(intent);
                    }
                });
            }
        }else {
            holder.audioPlayLayout.setVisibility(View.VISIBLE);
            holder.imageLayout.setVisibility(View.GONE);

            if (!AppUtils.isStringEmpty(faqAttachment.getFile_url())){
                holder.audioPlayLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, PlayAudioActivity.class);
                        intent.putExtra("audioUrl", faqAttachment.getFile_url());
                        context.startActivity(intent);
                    }
                });
            }
        }
        holder.attachmentDescription.setText(faqAttachment.getDescription());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class FaqAttachmentViewHolder extends RecyclerView.ViewHolder {

        ImageView attachmentImage;
        TextView attachmentDescription;
        RelativeLayout audioPlayLayout;
        RelativeLayout imageLayout;
        ImageView ivAudioPlayBtn;

        public FaqAttachmentViewHolder(View itemView) {
            super(itemView);

            attachmentImage = itemView.findViewById(R.id.iv_attachment_image);
            attachmentDescription = itemView.findViewById(R.id.tv_attachment_description);
            audioPlayLayout = itemView.findViewById(R.id.audio_play_layout);
            imageLayout = itemView.findViewById(R.id.image_layout);
            ivAudioPlayBtn = itemView.findViewById(R.id.iv_audio_play_btn);
        }
    }
}
