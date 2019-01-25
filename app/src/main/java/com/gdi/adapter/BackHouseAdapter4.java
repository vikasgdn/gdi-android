package com.gdi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gdi.R;
import com.gdi.model.backhouse.BackHouseAttachment;
import com.gdi.model.backhouse.BackHouseOption;
import com.gdi.model.integrity.IntegrityAttachment;
import com.gdi.utils.AppPrefs;
import com.gdi.utils.Headers;

import java.util.ArrayList;

public class BackHouseAdapter4 extends
        RecyclerView.Adapter<BackHouseAdapter4.BackHouseViewHolder4> {

    private Context context;
    private ArrayList<BackHouseAttachment> data;

    public BackHouseAdapter4(Context context, ArrayList<BackHouseAttachment> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public BackHouseViewHolder4 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.back_house_layout4,
                parent, false);

        return new BackHouseViewHolder4(view);
    }

    @Override
    public void onBindViewHolder(final BackHouseViewHolder4 holder, int position) {
        //TODO : Static data testing

        BackHouseAttachment backHouseAttachment = data.get(position);
        Glide.with(context)
                .load(Headers.getUrlWithHeaders(backHouseAttachment.getThumb_url(), AppPrefs.getAccessToken(context)))
                .into(holder.attachmentImage);
        holder.attachmentDescription.setText(backHouseAttachment.getDescription());


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class BackHouseViewHolder4 extends RecyclerView.ViewHolder {

        ImageView attachmentImage;
        TextView attachmentDescription;

        public BackHouseViewHolder4(View itemView) {
            super(itemView);

            attachmentImage = itemView.findViewById(R.id.iv_attachment_image);
            attachmentDescription = itemView.findViewById(R.id.tv_attachment_description);

        }
    }
}
