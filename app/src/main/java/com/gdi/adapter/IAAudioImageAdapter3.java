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
import com.gdi.activity.IAReportAudioImageActivity;
import com.gdi.activity.ImageViewActivity;
import com.gdi.activity.ReportAudioImageActivity;
import com.gdi.model.SampleModel;
import com.gdi.model.audioimages.AttachmentAudioImages;
import com.gdi.model.audioimages.IAAudioImages;
import com.gdi.utils.AppPrefs;
import com.gdi.utils.DownloadAudioTask;
import com.gdi.utils.Headers;

import java.util.ArrayList;

public class IAAudioImageAdapter3 extends
        RecyclerView.Adapter<IAAudioImageAdapter3.IAAudioImageViewHolder3> {

    private Context context;
    private ArrayList<IAAudioImages> data;
    private ArrayList<SampleModel> sampleOrderData;
    private boolean expand = false;
    private static final String TAG = AuditAdapter.class.getSimpleName();
    private DownloadAudioTask.AudioDownloadFinishedListner audioDownloadFinishedListner;

    public IAAudioImageAdapter3(Context context, ArrayList<IAAudioImages> data,
                                DownloadAudioTask.AudioDownloadFinishedListner audioDownloadFinishedListner) {
        this.context = context;
        this.data = data;
        this.audioDownloadFinishedListner = audioDownloadFinishedListner;
    }

    @Override
    public IAAudioImageViewHolder3 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.audio_image_layout3,
                parent, false);

        return new IAAudioImageViewHolder3(view);
    }

    @Override
    public void onBindViewHolder(final IAAudioImageViewHolder3 holder, int position) {
        //TODO : Static data testing
        final IAAudioImages attachmentAudioImages = data.get(position);
        String fileType = attachmentAudioImages.getFile_type();
        if (fileType.equals("image/jpeg")){
            holder.ivImage.setVisibility(View.VISIBLE);
            holder.audioPlayLayout.setVisibility(View.GONE);
            Glide.with(context)
                    .load(Headers.getUrlWithHeaders(attachmentAudioImages.getThumb_url(), AppPrefs.getAccessToken(context)))
                    .into(holder.ivImage);
        }else if (fileType.equals("audio/mpeg")){
            holder.audioPlayLayout.setVisibility(View.VISIBLE);
            holder.ivImage.setVisibility(View.GONE);
        }

        holder.audioPlayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((IAReportAudioImageActivity)context).downloadAudio(attachmentAudioImages.getFile_url());
            }
        });
        holder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ImageViewActivity.class);
                intent.putExtra("fileUrl", attachmentAudioImages.getFile_url());
                context.startActivity(intent);
            }
        });
        holder.tvImageAudioDescription.setText(attachmentAudioImages.getDescription());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class IAAudioImageViewHolder3 extends RecyclerView.ViewHolder {

        RelativeLayout audioPlayLayout;
        ImageView ivImage;
        ImageView ivAudioPlayBtn;
        TextView tvImageAudioDescription;

        public IAAudioImageViewHolder3(View itemView) {
            super(itemView);

            tvImageAudioDescription = itemView.findViewById(R.id.tv_image_audio_description);
            audioPlayLayout = itemView.findViewById(R.id.audio_play_layout);
            ivImage = itemView.findViewById(R.id.iv_image);
            ivAudioPlayBtn = itemView.findViewById(R.id.iv_audio_play_btn);
        }
    }
}
