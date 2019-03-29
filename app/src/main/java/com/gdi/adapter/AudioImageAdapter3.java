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
import com.gdi.activity.ImageViewActivity;
import com.gdi.activity.PlayAudioActivity;
import com.gdi.model.SampleModel;
import com.gdi.model.reportaudioimages.AttachmentAudioImages;
import com.gdi.utils.AppPrefs;
import com.gdi.utils.AppUtils;
import com.gdi.utils.Headers;

import java.util.ArrayList;

public class AudioImageAdapter3 extends
        RecyclerView.Adapter<AudioImageAdapter3.AudioImageViewHolder3> {

    private Context context;
    private ArrayList<AttachmentAudioImages> data;

    public AudioImageAdapter3(Context context, ArrayList<AttachmentAudioImages> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public AudioImageViewHolder3 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.audio_image_layout3,
                parent, false);

        return new AudioImageViewHolder3(view);
    }

    @Override
    public void onBindViewHolder(final AudioImageViewHolder3 holder, int position) {
        //TODO : Static data testing
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

    public class AudioImageViewHolder3 extends RecyclerView.ViewHolder {

        RelativeLayout audioPlayLayout;
        RelativeLayout imageLayout;
        ImageView ivImage;
        ImageView ivAudioPlayBtn;
        TextView tvImageAudioDescription;

        public AudioImageViewHolder3(View itemView) {
            super(itemView);

            tvImageAudioDescription = itemView.findViewById(R.id.tv_image_audio_description);
            audioPlayLayout = itemView.findViewById(R.id.audio_play_layout);
            imageLayout = itemView.findViewById(R.id.image_layout);
            ivImage = itemView.findViewById(R.id.iv_image);
            ivAudioPlayBtn = itemView.findViewById(R.id.iv_audio_play_btn);
        }
    }


}
