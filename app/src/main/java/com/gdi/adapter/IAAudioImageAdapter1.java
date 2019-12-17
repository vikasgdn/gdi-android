package com.gdi.adapter;

import android.content.Context;
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
import com.gdi.model.SampleModel;
import com.gdi.model.reportaudioimages.IAAudioImageInfo;
import com.gdi.utils.DownloadAudioTask;

import java.util.ArrayList;

public class IAAudioImageAdapter1 extends
        RecyclerView.Adapter<IAAudioImageAdapter1.IAAudioImageViewHolder> {

    private Context context;
    private ArrayList<IAAudioImageInfo> data;
    private ArrayList<SampleModel> sampleOrderData;
    private boolean expand = false;
    private static final String TAG = AuditAdapter.class.getSimpleName();
    private DownloadAudioTask.AudioDownloadFinishedListner audioDownloadFinishedListner;

    public IAAudioImageAdapter1(Context context, ArrayList<IAAudioImageInfo> data,
                                DownloadAudioTask.AudioDownloadFinishedListner audioDownloadFinishedListner) {
        this.context = context;
        this.data = data;
        this.audioDownloadFinishedListner = audioDownloadFinishedListner;
    }

    @Override
    public IAAudioImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.audio_image_layout,
                parent, false);

        return new IAAudioImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final IAAudioImageViewHolder holder, int position) {
        final IAAudioImageInfo audioImageInfo = data.get(position);
        holder.tvAudioImageTitle.setText(audioImageInfo.getLocation_name() + " | " + audioImageInfo.getCity());

        holder.rlAudioImageExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.recyclerViewAudioImage.setVisibility(View.VISIBLE);
                if (!expand){
                    expand = true;
                    holder.recyclerViewAudioImage.setVisibility(View.VISIBLE);
                    holder.ivExpandIcon.setImageResource(R.drawable.compress_icon);
                    IAAudioImageAdapter2 audioImageAdapter2 = new IAAudioImageAdapter2(context, audioImageInfo.getSections(), audioDownloadFinishedListner);
                    holder.recyclerViewAudioImage.setLayoutManager(new LinearLayoutManager(context));
                    holder.recyclerViewAudioImage.setAdapter(audioImageAdapter2);

                }else if(expand){
                    expand = false;
                    holder.recyclerViewAudioImage.setVisibility(View.GONE);
                    holder.ivExpandIcon.setImageResource(R.drawable.expand_icon);
                }
            }
        });

        holder.pdfIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((IAReportAudioImageActivity)context).downloadPdf(audioImageInfo.getReport_urls().getPdf());
            }
        });

        holder.mailIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((IAReportAudioImageActivity)context).emailAttachment(audioImageInfo.getReport_urls().getEmail());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class IAAudioImageViewHolder extends RecyclerView.ViewHolder {


        TextView tvAudioImageTitle;
        RelativeLayout rlAudioImageExpand;
        RecyclerView recyclerViewAudioImage;
        ImageView pdfIcon;
        ImageView mailIcon;
        ImageView ivExpandIcon;

        public IAAudioImageViewHolder(View itemView) {
            super(itemView);

            tvAudioImageTitle = itemView.findViewById(R.id.tv_audio_image_title);
            rlAudioImageExpand = itemView.findViewById(R.id.rl_audio_image_expand);
            ivExpandIcon = itemView.findViewById(R.id.iv_expand_icon);
            recyclerViewAudioImage = itemView.findViewById(R.id.recycler_view_audio_image);
            pdfIcon = itemView.findViewById(R.id.pdf_icon);
            mailIcon = itemView.findViewById(R.id.mail_icon);

        }
    }
}
