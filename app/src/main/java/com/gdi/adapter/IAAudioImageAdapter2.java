package com.gdi.adapter;

import android.content.Context;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.activity.InternalAuditReport.IAReportAudioImageActivity;
import com.gdi.model.reportaudioimages.IASectionAudioImage;
import com.gdi.utils.DownloadAudioTask;

import java.util.ArrayList;

public class IAAudioImageAdapter2 extends
        RecyclerView.Adapter<IAAudioImageAdapter2.IAAudioImageViewHolder2> {

    private Context context;
    private ArrayList<IASectionAudioImage> data;
    private DownloadAudioTask.AudioDownloadFinishedListner audioDownloadFinishedListner;

    public IAAudioImageAdapter2(Context context, ArrayList<IASectionAudioImage> data,
                                DownloadAudioTask.AudioDownloadFinishedListner audioDownloadFinishedListner) {
        this.context = context;
        this.data = data;
        this.audioDownloadFinishedListner = audioDownloadFinishedListner;
    }

    @Override
    public IAAudioImageViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.audio_image_layout2,
                parent, false);

        return new IAAudioImageViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(final IAAudioImageViewHolder2 holder, int position) {
        final IASectionAudioImage sectionAudioImage = data.get(position);
        holder.tvAudioImageTitle.setText(sectionAudioImage.getSection_name());
        IAAudioImageAdapter3 audioImageAdapter3 = new IAAudioImageAdapter3(context, sectionAudioImage.getImages_audio(), audioDownloadFinishedListner);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,2
                , LinearLayoutManager.VERTICAL,false);
        holder.recyclerViewAudioImage.setLayoutManager(gridLayoutManager);
        holder.recyclerViewAudioImage.setAdapter(audioImageAdapter3);

        holder.pdfIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((IAReportAudioImageActivity)context).downloadPdf(sectionAudioImage.getReport_urls().getPdf());
            }
        });

        holder.mailIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((IAReportAudioImageActivity)context).emailAttachment(sectionAudioImage.getReport_urls().getEmail());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class IAAudioImageViewHolder2 extends RecyclerView.ViewHolder {

        TextView tvAudioImageTitle;
        RelativeLayout rlAudioImageExpand;
        RecyclerView recyclerViewAudioImage;
        ImageView pdfIcon;
        ImageView mailIcon;

        public IAAudioImageViewHolder2(View itemView) {
            super(itemView);

            tvAudioImageTitle = itemView.findViewById(R.id.tv_audio_image_title);
            rlAudioImageExpand = itemView.findViewById(R.id.rl_audio_image_expand);
            recyclerViewAudioImage = itemView.findViewById(R.id.recycler_view_audio_image);
            pdfIcon = itemView.findViewById(R.id.pdf_icon);
            mailIcon = itemView.findViewById(R.id.mail_icon);
        }
    }
}
