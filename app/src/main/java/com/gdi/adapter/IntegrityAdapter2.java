package com.gdi.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.gdi.hotel.mystery.audits.R;
import com.gdi.attachmentactivity.IntegrityAttachmentActivity;
import com.gdi.model.reportintegrity.IntegrityModel;

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
        final IntegrityModel integrityModel = data.get(position);
        holder.tvIntegrityTitle.setText(integrityModel.getName());
        holder.tvIntegrityStaffName.setText(integrityModel.getStaff_name());
        holder.tvIntegrityDate.setText(integrityModel.getDate());
        holder.tvIntegrityTime.setText(integrityModel.getTime());
        String summary_text = Html.fromHtml(integrityModel.getSummary()).toString();
        holder.tvIntegritySummary.setText(summary_text);
        if (integrityModel.getAttachments() != null && integrityModel.getAttachments().size()>0) {
            holder.attachmentLayout.setVisibility(View.VISIBLE);
            if (integrityModel.getAttachments().size() == 1) {
                holder.tvAttachmentCount.setText("" + integrityModel.getAttachments().size() + " Attachment");
            }else {
                holder.tvAttachmentCount.setText("" + integrityModel.getAttachments().size() + " Attachments");
            }
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
        TextView tvIntegrityTitle;
        TextView tvAttachmentCount;
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
