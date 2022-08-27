package com.gdi.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.gdi.hotel.mystery.audits.R;
import com.gdi.model.audit.BrandStandard.BrandStandardQuestionsOption;

import java.util.ArrayList;

public class BrandStandardAuditAdapter2 extends
        RecyclerView.Adapter<BrandStandardAuditAdapter2.BrandStandardAuditViewHolder2> {

    private Context context;
    private ArrayList<BrandStandardQuestionsOption> data;
    private ArrayList<Integer> answerOptionId;
    private String questionType;
    private String editable;

    public BrandStandardAuditAdapter2(Context context, ArrayList<BrandStandardQuestionsOption> data,
                                      ArrayList<Integer> answerOptionId, String questionType, String editable) {
        this.context = context;
        this.data = data;
        this.answerOptionId = answerOptionId;
        this.questionType = questionType;
        this.editable = editable;
    }

    @Override
    public BrandStandardAuditViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.brand_standard_audit_layout3,
                parent, false);

        return new BrandStandardAuditViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(final BrandStandardAuditViewHolder2 holder, int position) {
        //TODO : Static data testing

        final BrandStandardQuestionsOption brandStandardQuestionsOption = data.get(position);
        holder.radioText.setText(String.valueOf(brandStandardQuestionsOption.getOption_text()));
        if (editable.equals("0")){
            enableView(holder);
        }else {
            disableView(holder);
        }
        if (questionType.equals("radio")) {
            holder.rbBrandStandardButton.setVisibility(View.VISIBLE);
            holder.cbBrandStandardAnswer.setVisibility(View.GONE);
            if (answerOptionId.size() != 0 && answerOptionId.get(0) == brandStandardQuestionsOption.getOption_id()) {
                holder.rbBrandStandardButton.setChecked(true);
            }
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

    public class BrandStandardAuditViewHolder2 extends RecyclerView.ViewHolder {

        CheckBox cbBrandStandardAnswer;
        RadioButton rbBrandStandardButton;
        TextView radioText;

        public BrandStandardAuditViewHolder2(View itemView) {
            super(itemView);

            /*cbBrandStandardAnswer = itemView.findViewById(R.id.cb_brand_standard_answer);
            rbBrandStandardButton = itemView.findViewById(R.id.rb_brand_standard_answer);*/
            radioText = itemView.findViewById(R.id.radio_text);

        }
    }

    private void enableView(BrandStandardAuditViewHolder2 holder){
        holder.radioText.setEnabled(true);
        holder.cbBrandStandardAnswer.setEnabled(true);
    }

    private void disableView(BrandStandardAuditViewHolder2 holder){
        holder.radioText.setEnabled(false);
        holder.cbBrandStandardAnswer.setEnabled(false);
    }
}
