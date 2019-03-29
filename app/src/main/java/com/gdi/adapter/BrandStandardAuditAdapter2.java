package com.gdi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.model.audit.BrandStandard.BrandStandardQuestionsOption;
import com.gdi.model.reportfaq.FAQQuestionsOption;

import java.util.ArrayList;

public class BrandStandardAuditAdapter2 extends
        RecyclerView.Adapter<BrandStandardAuditAdapter2.FaqViewHolder2> {

    private Context context;
    private ArrayList<BrandStandardQuestionsOption> data;
    private String questionType;

    public BrandStandardAuditAdapter2(Context context, ArrayList<BrandStandardQuestionsOption> data, String questionType) {
        this.context = context;
        this.data = data;
        this.questionType = questionType;
    }

    @Override
    public FaqViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.brand_standard_audit_layout3,
                parent, false);

        return new FaqViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(final FaqViewHolder2 holder, int position) {
        //TODO : Static data testing

        final BrandStandardQuestionsOption brandStandardQuestionsOption = data.get(position);
        holder.radioText.setText(String.valueOf(brandStandardQuestionsOption.getOption_text()));
        if (questionType.equals("radio")) {
            holder.rbBrandStandardButton.setVisibility(View.VISIBLE);
            holder.cbBrandStandardAnswer.setVisibility(View.GONE);
            /*if (String.valueOf(faqQuestionsOption.getAnswer_option_id()) != null &&
                    faqQuestionsOption.getAnswer_option_id() == faqQuestionsOption.getOption_id()) {
                holder.rbFaqButton.setChecked(true);
            }else {
                holder.rbFaqButton.setChecked(false);
            }*/
        }else {
            holder.rbBrandStandardButton.setVisibility(View.GONE);
            holder.cbBrandStandardAnswer.setVisibility(View.VISIBLE);
            /*if (String.valueOf(faqQuestionsOption.getAnswer_option_id()) != null &&
                    faqQuestionsOption.getAnswer_option_id() == faqQuestionsOption.getOption_id()) {
                holder.cbFaqAnswer.setChecked(true);
            }else {
                holder.cbFaqAnswer.setChecked(false);
            }*/
        }


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class FaqViewHolder2 extends RecyclerView.ViewHolder {

        CheckBox cbBrandStandardAnswer;
        RadioButton rbBrandStandardButton;
        TextView radioText;

        public FaqViewHolder2(View itemView) {
            super(itemView);

            cbBrandStandardAnswer = itemView.findViewById(R.id.cb_brand_standard_answer);
            rbBrandStandardButton = itemView.findViewById(R.id.rb_brand_standard_answer);
            radioText = itemView.findViewById(R.id.radio_text);

        }
    }
}
