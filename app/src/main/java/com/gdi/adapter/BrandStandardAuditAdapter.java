package com.gdi.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.activity.Audit.BrandStandardAuditActivity;
import com.gdi.activity.MysteryAuditReport.ReportFAQActivity;
import com.gdi.model.audit.AuditInfo;
import com.gdi.model.audit.BrandStandard.BrandStandardInfo;
import com.gdi.model.audit.BrandStandard.BrandStandardQuestion;
import com.gdi.model.audit.BrandStandard.BrandStandardSection;
import com.gdi.model.reportfaq.FAQQuestionsInfo;
import com.gdi.utils.AppUtils;

import java.util.ArrayList;

public class BrandStandardAuditAdapter extends
        RecyclerView.Adapter<BrandStandardAuditAdapter.BrandStandardAuditViewHolder> {

    private Context context;
    private ArrayList<BrandStandardSection> data;
    private int status;

    public BrandStandardAuditAdapter(Context context, ArrayList<BrandStandardSection> data, int status) {
        this.context = context;
        this.data = data;
        this.status = status;
    }

    @Override
    public BrandStandardAuditViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.brand_standard_audit_layout,
                parent, false);

        return new BrandStandardAuditViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BrandStandardAuditViewHolder holder, final int position) {
        //TODO : Static data testing

        final BrandStandardSection brandStandardInfo = data.get(position);
        holder.tvBrandStandardTitle.setText(brandStandardInfo.getSection_title());
        if(!data.get(position).isExpand()){
            holder.brandStandardSubHeadLayout.setVisibility(View.GONE);
            holder.ivExpandIcon.setImageResource(R.drawable.expand_icon);
            data.get(position).setExpand(false);
        }else {
            holder.brandStandardSubHeadLayout.setVisibility(View.VISIBLE);
            holder.ivExpandIcon.setImageResource(R.drawable.compress_icon);
            data.get(position).setExpand(true);
        }

        holder.rlBrandStandardExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(data.get(position).isExpand()){
                    holder.brandStandardSubHeadLayout.setVisibility(View.GONE);
                    holder.ivExpandIcon.setImageResource(R.drawable.expand_icon);
                    data.get(position).setExpand(false);
                }else {
                    holder.brandStandardSubHeadLayout.setVisibility(View.VISIBLE);
                    holder.ivExpandIcon.setImageResource(R.drawable.compress_icon);
                    data.get(position).setExpand(true);
                    addRow(brandStandardInfo.getQuestions(), holder);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class BrandStandardAuditViewHolder extends RecyclerView.ViewHolder {

        TextView tvBrandStandardTitle;
        Button tvBrandStandardQuestionCount;
        Button tvBrandStandardAttachCount;
        RelativeLayout rlBrandStandardExpand;
        ImageView ivExpandIcon;
        LinearLayout brandStandardSubHeadLayout;
        LinearLayout brandStandardAddFile;


        public BrandStandardAuditViewHolder (View itemView) {
            super(itemView);

            tvBrandStandardTitle = itemView.findViewById(R.id.tv_brand_standard_title);
            tvBrandStandardQuestionCount = itemView.findViewById(R.id.brand_standard_question_count);
            tvBrandStandardAttachCount = itemView.findViewById(R.id.brand_standard_attachment_count);
            rlBrandStandardExpand = itemView.findViewById(R.id.rl_brand_standard_expand);
            ivExpandIcon = itemView.findViewById(R.id.iv_expand_icon);
            brandStandardSubHeadLayout = itemView.findViewById(R.id.brand_standard_sub_head_layout);
            brandStandardAddFile = itemView.findViewById(R.id.brand_standard_add_file);

        }
    }

    private void addRow(final ArrayList<BrandStandardQuestion> arrayList, BrandStandardAuditViewHolder holder) {
        holder.brandStandardSubHeadLayout.removeAllViews();
        for (int i = 0; i < arrayList.size(); i++) {
            final BrandStandardQuestion BrandStandardQuestion = arrayList.get(i);
            int count = i + 1;
            View view = ((BrandStandardAuditActivity)context).inflater.inflate(R.layout.brand_standard_audit_layout2, null);
            TextView tvBrandStandardTitle = view.findViewById(R.id.tv_brand_standard_title);
            TextView brandStandardComment = view.findViewById(R.id.tv_brand_standard_comment);
            CheckBox brandStandardNA = view.findViewById(R.id.cb_brand_standard_na);
            Button brandStandardFileBtn = view.findViewById(R.id.brand_standard_file_btn);
            Button brandStandardAttachementCount = view.findViewById(R.id.brand_standard_attachment_count);
            LinearLayout brandStandardAddFile = view.findViewById(R.id.brand_standard_add_file);
            RecyclerView rvBrandStandardAnswer = view.findViewById(R.id.rv_brand_standard_answer);


            tvBrandStandardTitle.setText("" + count + ". " +BrandStandardQuestion.getQuestion_title());

            switch (status){
                case 0:
                    break;
                case 1:
                    brandStandardComment.setEnabled(true);
                    brandStandardNA.setEnabled(true);
                    brandStandardAddFile.setVisibility(View.VISIBLE);
                    brandStandardFileBtn.setVisibility(View.GONE);
                    break;
                case 2:
                    brandStandardComment.setEnabled(true);
                    brandStandardNA.setEnabled(true);
                    brandStandardAddFile.setVisibility(View.VISIBLE);
                    brandStandardFileBtn.setVisibility(View.GONE);
                    break;
                case 3:
                    brandStandardComment.setEnabled(false);
                    brandStandardNA.setEnabled(false);
                    brandStandardAddFile.setVisibility(View.GONE);
                    brandStandardFileBtn.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    brandStandardComment.setEnabled(false);
                    brandStandardNA.setEnabled(false);
                    brandStandardAddFile.setVisibility(View.GONE);
                    brandStandardFileBtn.setVisibility(View.VISIBLE);
                    break;
                case 5:
                    brandStandardComment.setEnabled(false);
                    brandStandardNA.setEnabled(false);
                    brandStandardAddFile.setVisibility(View.GONE);
                    brandStandardFileBtn.setVisibility(View.VISIBLE);
                    break;
            }

            if (brandStandardAttachementCount.getText().toString().equals("0")){

            }else {

            }

            BrandStandardAuditAdapter2 brandStandardAuditAdapter2 = new BrandStandardAuditAdapter2(
                    context, BrandStandardQuestion.getOptions(), BrandStandardQuestion.getQuestion_type());
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context,3
                    , LinearLayoutManager.VERTICAL,false);
            rvBrandStandardAnswer.setLayoutManager(gridLayoutManager);
            rvBrandStandardAnswer.setAdapter(brandStandardAuditAdapter2);

            holder.brandStandardSubHeadLayout.addView(view);
        }

    }

    private void enableView(BrandStandardAuditViewHolder holder){
    }

    private void disableView(BrandStandardAuditViewHolder holder){
    }
}
