package com.gdi.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.activity.MysteryAuditReport.ReportFAQActivity;
import com.gdi.attachmentactivity.FaqAttachmentActivity;
import com.gdi.model.reportfaq.FAQAttachment;
import com.gdi.model.reportfaq.FAQInfo;
import com.gdi.model.reportfaq.FAQQuestionsInfo;
import com.gdi.model.reportfaq.FAQSectionInfo;
import com.gdi.utils.AppUtils;

import java.util.ArrayList;

public class FAQAdapter extends
        RecyclerView.Adapter<FAQAdapter.FAQViewHolder> {

    private Context context;
    private ArrayList<FAQInfo> data;

    public FAQAdapter(Context context, ArrayList<FAQInfo> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public FAQViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.faq_layout,
                parent, false);

        return new FAQViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FAQViewHolder holder, final int position) {
        final FAQInfo faqInfo = data.get(position);
        holder.tvFaqTitle.setText(faqInfo.getLocation_title() + " | " + faqInfo.getCity_name());
        holder.tvFaqScore.setText(" (" + faqInfo.getScore_text() + ")");
        holder.tvFaqMarks.setText("" + faqInfo.getFaq_total_obtained_mark()+ "/" + faqInfo.getFaq_total_max_mark());
        if(!data.get(position).isExpand()){
            holder.faqSubHeadLayout.setVisibility(View.GONE);
            holder.ivExpandIcon.setImageResource(R.drawable.expand_icon);
            data.get(position).setExpand(false);
        }else {
            holder.faqSubHeadLayout.setVisibility(View.VISIBLE);
            holder.ivExpandIcon.setImageResource(R.drawable.compress_icon);
            data.get(position).setExpand(true);
        }

        holder.rlBackHouseExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(data.get(position).isExpand()){
                    holder.faqSubHeadLayout.setVisibility(View.GONE);
                    holder.ivExpandIcon.setImageResource(R.drawable.expand_icon);
                    data.get(position).setExpand(false);
                }else {
                    holder.faqSubHeadLayout.setVisibility(View.VISIBLE);
                    holder.ivExpandIcon.setImageResource(R.drawable.compress_icon);
                    data.get(position).setExpand(true);
                    addRow(faqInfo.getSections(), holder.faqSubHeadLayout);
                }
            }
        });

        holder.pdfIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ReportFAQActivity)context).downloadPdf(faqInfo.getReport_urls().getPdf());
            }
        });

        holder.mailIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ReportFAQActivity)context).emailAttachment(faqInfo.getReport_urls().getEmail());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class FAQViewHolder extends RecyclerView.ViewHolder {


        TextView tvFaqTitle;
        TextView tvFaqScore;
        TextView tvFaqMarks;
        RelativeLayout rlBackHouseExpand;
        ImageView pdfIcon;
        ImageView mailIcon;
        ImageView ivExpandIcon;
        LinearLayout faqSubHeadLayout;

        public FAQViewHolder(View itemView) {
            super(itemView);

            tvFaqTitle = itemView.findViewById(R.id.tv_faq_title);
            tvFaqScore = itemView.findViewById(R.id.faq_score_text);
            tvFaqMarks = itemView.findViewById(R.id.faq_marks_text);
            rlBackHouseExpand = itemView.findViewById(R.id.rl_faq_expand);
            ivExpandIcon = itemView.findViewById(R.id.iv_expand_icon);
            faqSubHeadLayout = itemView.findViewById(R.id.faq_sub_head_layout);
            pdfIcon = itemView.findViewById(R.id.pdf_icon);
            mailIcon = itemView.findViewById(R.id.mail_icon);

        }
    }

    private void addRow(final ArrayList<FAQSectionInfo> arrayList, LinearLayout subHead) {
        subHead.removeAllViews();
        for (int i = 0; i < arrayList.size(); i++) {
            final FAQSectionInfo faqSectionInfo = arrayList.get(i);
            View view = ((ReportFAQActivity)context).inflater.inflate(R.layout.faq_layout2, null);
            TextView tvFaqTitle = view.findViewById(R.id.tv_faq_sub_head);
            TextView faqScore = view.findViewById(R.id.tv_faq_sub_head_score);
            LinearLayout faqDetail = view.findViewById(R.id.faq_detail_layout);
            LinearLayout attachmentLayout = view.findViewById(R.id.faq_attachment_layout);
            TextView attachmentCount = view.findViewById(R.id.tv_faq_attachment_count);

            tvFaqTitle.setText(faqSectionInfo.getSection_title());
            if (AppUtils.isStringEmpty(faqSectionInfo.getScore_text())){
                faqScore.setVisibility(View.GONE);
            }else {
                faqScore.setVisibility(View.VISIBLE);
                faqScore.setText("" + faqSectionInfo.getTotal_obtained_mark()+ "/" + faqSectionInfo.getTotal_max_mark() +" (" + faqSectionInfo.getScore_text() + ")");
            }

            if (faqSectionInfo.getAttachments() !=null && faqSectionInfo.getAttachments().size() > 0){
                attachmentLayout.setVisibility(View.VISIBLE);
                if (faqSectionInfo.getAttachments().size() == 1) {
                    attachmentCount.setText("" + faqSectionInfo.getAttachments().size() + " Attachment");
                }else {
                    attachmentCount.setText("" + faqSectionInfo.getAttachments().size() + " Attachments");
                }
                attachmentLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context,FaqAttachmentActivity.class);
                        Bundle bundle = new Bundle();
                        ArrayList<FAQAttachment> faqAttachments = new ArrayList<>();
                        faqAttachments = faqSectionInfo.getAttachments();
                        bundle.putParcelableArrayList("data", faqAttachments);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                });
            }else {
                attachmentLayout.setVisibility(View.GONE);
            }

            addSubRow(faqSectionInfo.getQuestions(), faqDetail);

            subHead.addView(view);
        }

    }

    private void addSubRow(final ArrayList<FAQQuestionsInfo> arrayList, LinearLayout subHead) {
        subHead.removeAllViews();
        for (int i = 0; i < arrayList.size(); i++) {
            final FAQQuestionsInfo faqQuestionsInfo = arrayList.get(i);
            int count = i + 1;
            View view = ((ReportFAQActivity)context).inflater.inflate(R.layout.faq_layout3, null);
            TextView tvFaqTitle = view.findViewById(R.id.tv_faq_title);
            TextView faqComment = view.findViewById(R.id.tv_faq_comment);
            RadioButton yesRadioBtn = view.findViewById(R.id.radio_btn_yes);
            RadioButton noRadioBtn = view.findViewById(R.id.radio_btn_no);
            RecyclerView rvFaqAnswer = view.findViewById(R.id.rv_faq_answer);


            tvFaqTitle.setText("" + count + ". " +faqQuestionsInfo.getQuestion_title());

            if (AppUtils.isStringEmpty(faqQuestionsInfo.getAnswer_comment())){
                faqComment.setVisibility(View.GONE);
            }else {
                faqComment.setVisibility(View.VISIBLE);
                faqComment.setText(faqQuestionsInfo.getAnswer_comment());
            }

            FaqAdapter2 faqAdapter2 = new FaqAdapter2(context, faqQuestionsInfo.getOptions(), faqQuestionsInfo.getQuestion_type());
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context,3
                    , LinearLayoutManager.VERTICAL,false);
            rvFaqAnswer.setLayoutManager(gridLayoutManager);
            rvFaqAnswer.setAdapter(faqAdapter2);

            subHead.addView(view);
        }

    }
}
