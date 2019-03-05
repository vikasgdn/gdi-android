package com.gdi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.model.backhouse.BackHouseOption;
import com.gdi.model.faq.FAQQuestionsOption;

import java.util.ArrayList;

public class FaqAdapter2 extends
        RecyclerView.Adapter<FaqAdapter2.FaqViewHolder2> {

    private Context context;
    private ArrayList<FAQQuestionsOption> data;
    private String questionType;

    public FaqAdapter2(Context context, ArrayList<FAQQuestionsOption> data, String questionType) {
        this.context = context;
        this.data = data;
        this.questionType = questionType;
    }

    @Override
    public FaqViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.faq_adapter_layout2,
                parent, false);

        return new FaqViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(final FaqViewHolder2 holder, int position) {
        //TODO : Static data testing

        final FAQQuestionsOption faqQuestionsOption = data.get(position);
        //holder.radioText.setText(faqQuestionsOption.getOption_text() + "(" + faqQuestionsOption.getOption_mark() + ")");
        holder.radioText.setText(String.valueOf(faqQuestionsOption.getOption_text()));
        if (questionType.equals("radio")) {
            holder.rbFaqButton.setVisibility(View.VISIBLE);
            holder.cbFaqAnswer.setVisibility(View.GONE);
            if (String.valueOf(faqQuestionsOption.getAnswer_option_id()) != null &&
                    faqQuestionsOption.getAnswer_option_id() == faqQuestionsOption.getOption_id()) {
                //holder.radioText.setTextColor(context.getResources().getColor(R.color.colorPink));
                //holder.radioImage.setImageResource(R.drawable.radio_checked);
                holder.rbFaqButton.setChecked(true);
            }else {
                //holder.radioText.setTextColor(context.getResources().getColor(R.color.colorBlack));
                //holder.radioImage.setImageResource(R.drawable.radio_unchecked);
                holder.rbFaqButton.setChecked(false);
            }
        }else {
            holder.rbFaqButton.setVisibility(View.GONE);
            holder.cbFaqAnswer.setVisibility(View.VISIBLE);
            if (String.valueOf(faqQuestionsOption.getAnswer_option_id()) != null &&
                    faqQuestionsOption.getAnswer_option_id() == faqQuestionsOption.getOption_id()) {
                //holder.radioText.setTextColor(context.getResources().getColor(R.color.colorPink));
                //holder.radioImage.setImageResource(R.drawable.radio_checked);
                holder.cbFaqAnswer.setChecked(true);
            }else {
                //holder.radioText.setTextColor(context.getResources().getColor(R.color.colorBlack));
                //holder.radioImage.setImageResource(R.drawable.radio_unchecked);
                holder.cbFaqAnswer.setChecked(false);
            }
        }


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class FaqViewHolder2 extends RecyclerView.ViewHolder {

        //ImageView radioImage;
        CheckBox cbFaqAnswer;
        RadioButton rbFaqButton;
        TextView radioText;

        public FaqViewHolder2(View itemView) {
            super(itemView);

            cbFaqAnswer = itemView.findViewById(R.id.cb_faq_answer);
            rbFaqButton = itemView.findViewById(R.id.rb_faq_answer);
            radioText = itemView.findViewById(R.id.radio_text);

        }
    }
}
