package com.gdi.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdi.hotel.mystery.audits.R;
import com.gdi.activity.MysteryAuditReport.ReportAuditActivity;
import com.gdi.model.SampleModel;
import com.gdi.model.reportaudit.SectionInfo;
import com.gdi.utils.AppUtils;

import java.util.ArrayList;

public class AuditAdapter2 extends RecyclerView.Adapter<AuditAdapter2.AuditViewHolder4> {

    private Context context;
    private ArrayList<SectionInfo> orderData;
    private ArrayList<SampleModel> sampleOrderData;
    private static final String TAG = AuditAdapter.class.getSimpleName();

    public AuditAdapter2(Context context, ArrayList<SectionInfo> orderData) {
        this.context = context;
        this.orderData = orderData;
    }

    @Override
    public AuditViewHolder4 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.audit_layout2,
                parent, false);

        return new AuditViewHolder4(view);
    }

    @Override
    public void onBindViewHolder(AuditViewHolder4 holder, int position) {
        final SectionInfo sectionInfo = orderData.get(position);
        AppUtils.setScoreColor(sectionInfo.getScore(), holder.score, context);
        holder.section.setText(sectionInfo.getSection_name());
        holder.score.setText(sectionInfo.getScore());
        holder.pdfIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ReportAuditActivity)context).downloadPdf(sectionInfo.getReport_urls().getPdf());
            }
        });
        holder.mailIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ReportAuditActivity)context).sentEmail(sectionInfo.getReport_urls().getEmail());
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderData.size();
    }

    public class AuditViewHolder4 extends RecyclerView.ViewHolder {

        TextView section;
        TextView score;
        ImageView pdfIcon;
        ImageView excelIcon;
        ImageView mailIcon;

        public AuditViewHolder4(View itemView) {
            super(itemView);

            section = itemView.findViewById(R.id.section_txt);
            score = itemView.findViewById(R.id.score_txt);
            pdfIcon = itemView.findViewById(R.id.pdf_icon);
            excelIcon = itemView.findViewById(R.id.excel_icon);
            mailIcon = itemView.findViewById(R.id.mail_icon);

        }
    }
}
