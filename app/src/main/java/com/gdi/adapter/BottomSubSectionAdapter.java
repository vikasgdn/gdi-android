package com.gdi.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gdi.R;
import com.gdi.activity.Audit.BrandStandardMisteryAuditActivity;
import com.gdi.model.audit.BrandStandard.BrandStandardSectionNew;

import java.util.List;

public class BottomSubSectionAdapter extends RecyclerView.Adapter<BottomSubSectionAdapter.AddAttachmentViewHolder> {

    private Context context;
    private List<BrandStandardSectionNew> mSectionList;


    public BottomSubSectionAdapter(Context context,List<BrandStandardSectionNew> data)
    {
        this.context = context;
        this.mSectionList = data;

    }

    @NonNull
    @Override
    public AddAttachmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custome_bottom_subsection, parent, false);

        return new AddAttachmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddAttachmentViewHolder holder, int position)
    {

        BrandStandardSectionNew sectionNew=mSectionList.get(position);
        holder.mSubSectionNameTV.setText(sectionNew.section_title+" ("+sectionNew.submitted_question_count+"/"+sectionNew.total_question_count+")");
        holder.mSubSectionNameTV.setTag(position);
        holder.mSubSectionNameTV.setOnClickListener((BrandStandardMisteryAuditActivity)context);

    }

    @Override
    public int getItemCount() {
        return mSectionList.size();


    }

    public class AddAttachmentViewHolder extends RecyclerView.ViewHolder {

        TextView mSubSectionNameTV;

        public AddAttachmentViewHolder(View itemView) {
            super(itemView);

            mSubSectionNameTV = itemView.findViewById(R.id.tv_text_center);
            mSubSectionNameTV.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/ProximaNova_Regular.otf"));

        }
    }

}
