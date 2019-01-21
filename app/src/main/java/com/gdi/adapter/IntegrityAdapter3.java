package com.gdi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.model.SampleModel;
import com.gdi.model.audioimages.AttachmentAudioImages;

import java.util.ArrayList;

public class IntegrityAdapter3 extends
        RecyclerView.Adapter<IntegrityAdapter3.AudioImageViewHolder3> {

    private Context context;
    private ArrayList<AttachmentAudioImages> data;
    private ArrayList<SampleModel> sampleOrderData;
    private boolean expand = false;
    private static final String TAG = AuditAdapter.class.getSimpleName();

    public IntegrityAdapter3(Context context, ArrayList<AttachmentAudioImages> data) {
        this.context = context;
        this.data = data;
    }
    //TODO : Static data testing
    /*public DetailSummaryAdapter1(Context context, ArrayList<SampleModel> sampleOrderData) {
        this.context = context;
        this.sampleOrderData = sampleOrderData;
    }*/

    @Override
    public AudioImageViewHolder3 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.integrity_layout3,
                parent, false);

        return new AudioImageViewHolder3(view);
    }

    @Override
    public void onBindViewHolder(final AudioImageViewHolder3 holder, int position) {
        //TODO : Static data testing
        AttachmentAudioImages attachmentAudioImages = data.get(position);
        holder.tvImageAudioDescription.setText(attachmentAudioImages.getDescription());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class AudioImageViewHolder3 extends RecyclerView.ViewHolder {

        RelativeLayout audioPlayLayout;
        SeekBar seekBarTestPlay;
        ImageView ivImage;
        ImageView ivAudioPlayBtn;
        TextView tvImageAudioDescription;

        public AudioImageViewHolder3(View itemView) {
            super(itemView);

            tvImageAudioDescription = itemView.findViewById(R.id.tv_image_audio_description);
            audioPlayLayout = itemView.findViewById(R.id.audio_play_layout);
            seekBarTestPlay = itemView.findViewById(R.id.seek_bar_test_play);
            ivImage = itemView.findViewById(R.id.iv_image);
            ivAudioPlayBtn = itemView.findViewById(R.id.iv_audio_play_btn);
        }
    }
}
