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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.activity.ReportAudioImageActivity;
import com.gdi.attachmentactivity.BackHouseAttachmentActivity;
import com.gdi.model.SampleModel;
import com.gdi.model.audioimages.AudioImageInfo;
import com.gdi.model.backhouse.BackHouseAttachment;
import com.gdi.model.backhouse.BackHouseQuestion;
import com.gdi.utils.AppLogger;
import com.gdi.utils.AppUtils;

import java.util.ArrayList;

public class BackHouseAdapter2 extends
        RecyclerView.Adapter<BackHouseAdapter2.BackHouseViewHolder2> {

    private Context context;
    private ArrayList<BackHouseQuestion> data;

    public BackHouseAdapter2(Context context, ArrayList<BackHouseQuestion> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public BackHouseViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.back_house_layout2,
                parent, false);

        return new BackHouseViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(final BackHouseViewHolder2 holder, int position) {
        //TODO : Static data testing

        final BackHouseQuestion backHouseQuestion = data.get(position);
        int count = position + 1;
        AppLogger.e("count", "" + count);
        //holder.tvBackHouseTitle.setText("" + count);
        holder.tvBackHouseTitle.setText("" + count + ". " + backHouseQuestion.getQuestion_name());

        if (AppUtils.isStringEmpty(backHouseQuestion.getComment())){
            holder.tvBackHouseComment.setVisibility(View.GONE);
        }else {
            holder.tvBackHouseComment.setVisibility(View.VISIBLE);
            holder.tvBackHouseComment.setText(backHouseQuestion.getComment());
        }
        /*if (!AppUtils.isStringEmpty(backHouseQuestion.getComment())) {
            holder.tvBackHouseTitle.setText(backHouseQuestion.getQuestion_name());
        }*/
        BackHouseAdapter3 backHouseAdapter3 = new BackHouseAdapter3(context, backHouseQuestion.getOptions());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,3
                , LinearLayoutManager.VERTICAL,false);
        holder.recyclerViewRadioBtn.setLayoutManager(gridLayoutManager);
        holder.recyclerViewRadioBtn.setAdapter(backHouseAdapter3);

        if (backHouseQuestion.getAttachments() !=null && backHouseQuestion.getAttachments().size() > 0){
            holder.attachmentLayout.setVisibility(View.VISIBLE);
            if (backHouseQuestion.getAttachments().size() == 1) {
                holder.attachmentCount.setText("" + backHouseQuestion.getAttachments().size() + " Attachment");
            }else {
                holder.attachmentCount.setText("" + backHouseQuestion.getAttachments().size() + " Attachments");
            }
            holder.attachmentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,BackHouseAttachmentActivity.class);
                    Bundle bundle = new Bundle();
                    ArrayList<BackHouseAttachment> backHouseAttachments = new ArrayList<>();
                    backHouseAttachments = backHouseQuestion.getAttachments();
                    bundle.putParcelableArrayList("data", backHouseAttachments);
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

    public class BackHouseViewHolder2 extends RecyclerView.ViewHolder {


        TextView tvBackHouseTitle;
        TextView tvBackHouseComment;
        LinearLayout attachmentLayout;
        TextView attachmentCount;
        RecyclerView recyclerViewRadioBtn;

        public BackHouseViewHolder2(View itemView) {
            super(itemView);

            tvBackHouseTitle = itemView.findViewById(R.id.tv_back_house_title);
            tvBackHouseComment = itemView.findViewById(R.id.tv_back_house_comment);
            attachmentLayout = itemView.findViewById(R.id.attachment_layout);
            attachmentCount = itemView.findViewById(R.id.tv_attachment_count);
            recyclerViewRadioBtn = itemView.findViewById(R.id.recycler_view_radio_button);

        }
    }
}
