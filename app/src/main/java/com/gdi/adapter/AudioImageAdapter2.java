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

import com.gdi.hotel.mystery.audits.R;
import com.gdi.activity.MysteryAuditReport.ReportAudioImageActivity;
import com.gdi.model.reportaudioimages.SectionAudioImage;
import com.gdi.utils.AppUtils;

import java.util.ArrayList;

public class AudioImageAdapter2 extends
        RecyclerView.Adapter<AudioImageAdapter2.AudioImageViewHolder2> {

    private Context context;
    private ArrayList<SectionAudioImage> data;
    AudioImageAdapter3 audioImageAdapter3;

    public AudioImageAdapter2(Context context, ArrayList<SectionAudioImage> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public AudioImageViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.audio_image_layout2,
                parent, false);

        return new AudioImageViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(final AudioImageViewHolder2 holder, int position) {
        //TODO : Static data testing

        final SectionAudioImage sectionAudioImage = data.get(position);
        holder.tvAudioImageTitle.setText(sectionAudioImage.getSection_name());
        if (AppUtils.isStringEmpty(sectionAudioImage.getScore())){
            holder.score.setVisibility(View.GONE);
        }else {
            holder.score.setVisibility(View.VISIBLE);
            AppUtils.setScoreColor(sectionAudioImage.getScore(), holder.score, context);
            holder.score.setText("Score: " + sectionAudioImage.getScore());
        }
         audioImageAdapter3 = new AudioImageAdapter3(context, sectionAudioImage.getAttachments());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,2
                , LinearLayoutManager.VERTICAL,false);
        gridLayoutManager.setAutoMeasureEnabled(false);

        holder.recyclerViewAudioImage.setLayoutManager(gridLayoutManager);
        holder.recyclerViewAudioImage.setNestedScrollingEnabled(false);
        holder.recyclerViewAudioImage.setHasFixedSize(false);
        holder.recyclerViewAudioImage.setAdapter(audioImageAdapter3);

        holder.pdfIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ReportAudioImageActivity)context).downloadPdf(sectionAudioImage.getReport_urls().getPdf());
            }
        });

        holder.mailIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ReportAudioImageActivity)context).emailAttachment(sectionAudioImage.getReport_urls().getEmail());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class AudioImageViewHolder2 extends RecyclerView.ViewHolder {

        TextView tvAudioImageTitle;
        TextView score;
        RelativeLayout rlAudioImageExpand;
        RecyclerView recyclerViewAudioImage;
        ImageView pdfIcon;
        ImageView mailIcon;

        public AudioImageViewHolder2(View itemView) {
            super(itemView);

            tvAudioImageTitle = itemView.findViewById(R.id.tv_audio_image_title);
            rlAudioImageExpand = itemView.findViewById(R.id.rl_audio_image_expand);
            recyclerViewAudioImage = itemView.findViewById(R.id.recycler_view_audio_image);
            pdfIcon = itemView.findViewById(R.id.pdf_icon);
            mailIcon = itemView.findViewById(R.id.mail_icon);
            score = itemView.findViewById(R.id.score_text);

        }
    }
}
