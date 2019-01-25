package com.gdi.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.activity.ReportAudioImageActivity;
import com.gdi.attachmentactivity.IntegrityAttachmentActivity;
import com.gdi.model.SampleModel;
import com.gdi.model.audioimages.SectionAudioImage;
import com.gdi.model.integrity.IntegrityModel;

import java.util.ArrayList;

public class IntegrityAdapter2 extends
        RecyclerView.Adapter<IntegrityAdapter2.IntegrityViewHolder2> {

    private Context context;
    private ArrayList<IntegrityModel> data;

    public IntegrityAdapter2(Context context, ArrayList<IntegrityModel> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public IntegrityViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.integrity_layout2,
                parent, false);

        return new IntegrityViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(final IntegrityViewHolder2 holder, int position) {
        //TODO : Static data testing

        final IntegrityModel integrityModel = data.get(position);
        holder.tvIntegrityStaffName.setText(integrityModel.getStaff_name());
        holder.tvIntegrityDate.setText(integrityModel.getDate());
        holder.tvIntegrityTime.setText(integrityModel.getTime());
        holder.tvIntegritySummary.setText(integrityModel.getSummary());
        if (integrityModel.getAttachments() != null && integrityModel.getAttachments().size()>0) {
            holder.attachmentLayout.setVisibility(View.VISIBLE);
            holder.tvAttachmentCount.setText("(" + integrityModel.getAttachments().size() + ")");
            holder.attachmentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, IntegrityAttachmentActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("data", integrityModel.getAttachments());
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }else {
            holder.attachmentLayout.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class IntegrityViewHolder2 extends RecyclerView.ViewHolder {

        TextView tvIntegrityStaffName;
        TextView tvIntegrityDate;
        TextView tvIntegrityTime;
        TextView tvIntegritySummary;
        TextView tvAttachmentCount;
        TextView tvIntegrityTitle;
        LinearLayout attachmentLayout;

        public IntegrityViewHolder2(View itemView) {
            super(itemView);

            tvIntegrityStaffName = itemView.findViewById(R.id.tv_integrity_staff_name);
            tvIntegrityDate = itemView.findViewById(R.id.tv_integrity_date);
            tvIntegrityTime = itemView.findViewById(R.id.tv_integrity_time);
            tvIntegritySummary = itemView.findViewById(R.id.tv_integrity_summary);
            tvAttachmentCount = itemView.findViewById(R.id.tv_attachment_count);
            tvIntegrityTitle = itemView.findViewById(R.id.tv_integrity_title);
            attachmentLayout = itemView.findViewById(R.id.attachment_layout);
        }
    }
}