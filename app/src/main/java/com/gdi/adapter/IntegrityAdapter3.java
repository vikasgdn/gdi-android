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

import com.bumptech.glide.Glide;
import com.gdi.R;
import com.gdi.model.SampleModel;
import com.gdi.model.audioimages.AttachmentAudioImages;
import com.gdi.model.integrity.IntegrityAttachment;
import com.gdi.utils.AppPrefs;
import com.gdi.utils.Headers;

import java.util.ArrayList;

public class IntegrityAdapter3 extends
        RecyclerView.Adapter<IntegrityAdapter3.AudioImageViewHolder3> {

    private Context context;
    private ArrayList<IntegrityAttachment> data;

    public IntegrityAdapter3(Context context, ArrayList<IntegrityAttachment> data) {
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
        IntegrityAttachment integrityAttachment = data.get(position);
        Glide.with(context)
                .load(Headers.getUrlWithHeaders(integrityAttachment.getThumb_url(), AppPrefs.getAccessToken(context)))
                .into(holder.attachmentImage);
        holder.attachmentDescription.setText(integrityAttachment.getDescription());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class AudioImageViewHolder3 extends RecyclerView.ViewHolder {

        ImageView attachmentImage;
        TextView attachmentDescription;

        public AudioImageViewHolder3(View itemView) {
            super(itemView);

            attachmentImage = itemView.findViewById(R.id.iv_attachment_image);
            attachmentDescription = itemView.findViewById(R.id.tv_attachment_description);
        }
    }
}
