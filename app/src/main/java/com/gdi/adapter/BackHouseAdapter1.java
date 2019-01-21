package com.gdi.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.activity.ReportAudioImageActivity;
import com.gdi.model.SampleModel;
import com.gdi.model.audioimages.AudioImageInfo;

import java.util.ArrayList;

public class BackHouseAdapter1 extends
        RecyclerView.Adapter<BackHouseAdapter1.AudioImageViewHolder> {

    private Context context;
    private ArrayList<AudioImageInfo> data;
    private ArrayList<SampleModel> sampleOrderData;
    private boolean expand = false;
    private static final String TAG = AuditAdapter.class.getSimpleName();

    public BackHouseAdapter1(Context context, ArrayList<AudioImageInfo> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public AudioImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.back_house_layout,
                parent, false);

        return new AudioImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AudioImageViewHolder holder, int position) {
        //TODO : Static data testing

        final AudioImageInfo audioImageInfo = data.get(position);
        holder.tvAudioImageTitle.setText(audioImageInfo.getLocation_name() + " | " + audioImageInfo.getCity_name());

        holder.rlAudioImageExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.recyclerViewAudioImage.setVisibility(View.VISIBLE);
                if (!expand){
                    expand = true;
                    holder.recyclerViewAudioImage.setVisibility(View.VISIBLE);
                    holder.ivExpandIcon.setImageResource(R.drawable.compress_icon);
                    /*AudioImageAdapter2 audioImageAdapter2 = new AudioImageAdapter2(context, audioImageInfo.getSections());
                    holder.recyclerViewAudioImage.setLayoutManager(new LinearLayoutManager(context));
                    holder.recyclerViewAudioImage.setAdapter(audioImageAdapter2);*/

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
                ((ReportAudioImageActivity)context).downloadPdf(audioImageInfo.getReport_urls().getPdf());
            }
        });

        holder.mailIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ReportAudioImageActivity)context).emailAttachment(audioImageInfo.getReport_urls().getEmail());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class AudioImageViewHolder extends RecyclerView.ViewHolder {


        TextView tvAudioImageTitle;
        RelativeLayout rlAudioImageExpand;
        RecyclerView recyclerViewAudioImage;
        ImageView pdfIcon;
        ImageView mailIcon;
        ImageView ivExpandIcon;

        public AudioImageViewHolder(View itemView) {
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
