package com.gdi.adapter;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.gdi.R;
import com.gdi.activity.MysteryAuditReport.ReportFAQActivity;
import com.gdi.model.audit.BrandStandard.BrandStandardQuestion;
import com.gdi.model.audit.BrandStandard.BrandStandardSection;
import com.gdi.model.audit.DetailedSummary.DetailedSummaryInfo;
import com.gdi.utils.AppConstant;
import com.gdi.utils.AppUtils;

import java.util.ArrayList;
import java.util.Calendar;

public class DetailedSummaryAuditAdapter extends
        RecyclerView.Adapter<DetailedSummaryAuditAdapter.DetailedSummaryAuditViewHolder> {

    private Context context;
    private ArrayList<DetailedSummaryInfo> data;

    public DetailedSummaryAuditAdapter(Context context, ArrayList<DetailedSummaryInfo> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public DetailedSummaryAuditViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detailed_summary_audit_layout,
                parent, false);

        return new DetailedSummaryAuditViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DetailedSummaryAuditViewHolder holder, final int position) {
        final DetailedSummaryInfo detailedSummaryInfo = data.get(position);
        holder.tvDetailedSummaryTitle.setText(detailedSummaryInfo.getSection_title());
        showViewAccStatus(holder, detailedSummaryInfo);
        if(!data.get(position).isExpand() && detailedSummaryInfo.getIs_na() == 1){
            holder.detailedSummarySubLayout.setVisibility(View.GONE);
            holder.ivExpandIcon.setImageResource(R.drawable.expand_icon);
            data.get(position).setExpand(false);
        }else {
            holder.detailedSummarySubLayout.setVisibility(View.VISIBLE);
            holder.ivExpandIcon.setImageResource(R.drawable.compress_icon);
            data.get(position).setExpand(true);
        }
        holder.rlDetailedSummaryExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(data.get(position).isExpand() && detailedSummaryInfo.getIs_na() == 1){
                    holder.detailedSummarySubLayout.setVisibility(View.GONE);
                    holder.ivExpandIcon.setImageResource(R.drawable.expand_icon);
                    data.get(position).setExpand(false);
                }else {
                    holder.detailedSummarySubLayout.setVisibility(View.VISIBLE);
                    holder.ivExpandIcon.setImageResource(R.drawable.compress_icon);
                    data.get(position).setExpand(true);
                }
            }
        });

        holder.detailedSummaryNA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    holder.detailedSummarySubLayout.setVisibility(View.GONE);
                    holder.ivExpandIcon.setImageResource(R.drawable.expand_icon);
                    detailedSummaryInfo.setIs_na(0);
                } else {
                    detailedSummaryInfo.setIs_na(1);
                    if(data.get(position).isExpand()){
                        holder.detailedSummarySubLayout.setVisibility(View.GONE);
                        holder.ivExpandIcon.setImageResource(R.drawable.expand_icon);
                        data.get(position).setExpand(false);
                    }else {
                        holder.detailedSummarySubLayout.setVisibility(View.VISIBLE);
                        holder.ivExpandIcon.setImageResource(R.drawable.compress_icon);
                        data.get(position).setExpand(true);
                    }
                }

            }
        });

        holder.tvDetailedSummaryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar auditMonthCal = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                Calendar cal = Calendar.getInstance();
                                cal.set(year, monthOfYear, dayOfMonth);
                                holder.tvDetailedSummaryDate.setText(AppUtils.getDSAuditDate(cal.getTime()));

                            }
                        }, auditMonthCal.get(Calendar.YEAR), auditMonthCal.get(Calendar.MONTH),
                        auditMonthCal.get(Calendar.YEAR));
                datePickerDialog.show();
            }
        });

        holder.tvDetailedSummaryTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Time
                final Calendar calendar = Calendar.getInstance();
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                String AM_PM ;
                                if(hourOfDay < 12) {
                                    AM_PM = " AM";
                                } else {
                                    AM_PM = " PM";
                                }

                                calendar.set(Calendar.YEAR, Calendar.MONTH, Calendar.DATE, hourOfDay, minute);
                                String time = AppUtils.getDSAuditTime(calendar.getTime());

                                holder.tvDetailedSummaryTime.setText(time);

                            }
                        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
                timePickerDialog.show();
            }
        });

        if (!AppUtils.isStringEmpty(detailedSummaryInfo.getStaff_name())){
            holder.etDetailedSummaryStaffName.setText(detailedSummaryInfo.getStaff_name());
        }
        if (!AppUtils.isStringEmpty(detailedSummaryInfo.getDate_time())){
            String date = AppUtils.getDSAuditDate(detailedSummaryInfo.getDate_time());
            holder.tvDetailedSummaryDate.setText(date);
        }
        if (!AppUtils.isStringEmpty(detailedSummaryInfo.getDate_time())){
            String time = AppUtils.getDSAuditTime(detailedSummaryInfo.getDate_time());
            holder.tvDetailedSummaryTime.setText(time);
        }
        if (!AppUtils.isStringEmpty(detailedSummaryInfo.getSummary())){
            holder.etDetailedSummarySummary.setText(detailedSummaryInfo.getSummary());
        }
        if (!AppUtils.isStringEmpty(detailedSummaryInfo.getKey_positive())){
            holder.etDetailedSummaryKeyPositive.setText(detailedSummaryInfo.getKey_positive());
        }
        if (!AppUtils.isStringEmpty(detailedSummaryInfo.getKey_negative())){
            holder.etDetailedSummaryKeyNegative.setText(detailedSummaryInfo.getKey_negative());
        }
        if (!AppUtils.isStringEmpty(detailedSummaryInfo.getRecommendation())){
            holder.etDetailedSummaryRecommendation.setText(detailedSummaryInfo.getRecommendation());
        }
        if (!AppUtils.isStringEmpty("" + detailedSummaryInfo.getFile_count())){
            holder.detailedSummaryAttachCount.setText("" + detailedSummaryInfo.getFile_count());
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class DetailedSummaryAuditViewHolder extends RecyclerView.ViewHolder {

        TextView tvDetailedSummaryTitle;
        RelativeLayout rlDetailedSummaryExpand;
        ImageView ivExpandIcon;
        LinearLayout detailedSummarySubLayout;
        EditText etDetailedSummaryStaffName;
        EditText etDetailedSummarySummary;
        EditText etDetailedSummaryKeyNegative;
        EditText etDetailedSummaryKeyPositive;
        EditText etDetailedSummaryRecommendation;
        TextView tvDetailedSummaryTime;
        TextView tvDetailedSummaryDate;
        Button detailedSummaryFileBtn;
        Button detailedSummaryAttachCount;
        LinearLayout detailedSummaryAddFile;
        CheckBox detailedSummaryNA;


        public DetailedSummaryAuditViewHolder (View itemView) {
            super(itemView);

            tvDetailedSummaryTitle = itemView.findViewById(R.id.tv_detailed_summary_title);
            rlDetailedSummaryExpand = itemView.findViewById(R.id.rl_detailed_summary_expand);
            ivExpandIcon = itemView.findViewById(R.id.iv_expand_icon);
            detailedSummarySubLayout = itemView.findViewById(R.id.detailed_summary_sub_layout);
            tvDetailedSummaryTime = itemView.findViewById(R.id.tv_detailed_summary_time);
            tvDetailedSummaryDate = itemView.findViewById(R.id.tv_detailed_summary_date);
            etDetailedSummaryStaffName = itemView.findViewById(R.id.et_detailed_summary_staff_name);
            etDetailedSummarySummary = itemView.findViewById(R.id.et_detailed_summary_summary);
            etDetailedSummaryKeyNegative = itemView.findViewById(R.id.et_detailed_summary_key_negative);
            etDetailedSummaryKeyPositive = itemView.findViewById(R.id.et_detailed_summary_key_positive);
            etDetailedSummaryRecommendation = itemView.findViewById(R.id.et_detailed_summary_recommendation);
            detailedSummaryFileBtn = itemView.findViewById(R.id.detailed_summary_file_btn);
            detailedSummaryAttachCount = itemView.findViewById(R.id.detailed_summary_attachment_count);
            detailedSummaryAddFile = itemView.findViewById(R.id.detailed_summary_add_file);
            detailedSummaryNA = itemView.findViewById(R.id.cb_detailed_summary_na);

        }
    }

    private void showViewAccStatus(DetailedSummaryAuditViewHolder holder, DetailedSummaryInfo detailedSummaryInfo){
        switch (detailedSummaryInfo.getDetailed_sum_status()){
            case 0:
                break;
            case 1:
                enableView(holder);
                holder.detailedSummaryFileBtn.setVisibility(View.GONE);
                holder.detailedSummaryAddFile.setVisibility(View.VISIBLE);
                break;
            case 2:
                enableView(holder);
                holder.detailedSummaryFileBtn.setVisibility(View.GONE);
                holder.detailedSummaryAddFile.setVisibility(View.VISIBLE);
                break;
            case 3:
                disableView(holder);
                holder.detailedSummaryFileBtn.setVisibility(View.VISIBLE);
                holder.detailedSummaryAddFile.setVisibility(View.GONE);
                break;
            case 4:
                disableView(holder);
                holder.detailedSummaryFileBtn.setVisibility(View.VISIBLE);
                holder.detailedSummaryAddFile.setVisibility(View.GONE);
                break;
            case 5:
                disableView(holder);
                holder.detailedSummaryFileBtn.setVisibility(View.VISIBLE);
                holder.detailedSummaryAddFile.setVisibility(View.GONE);
                break;
        }
    }

    private void enableView(DetailedSummaryAuditViewHolder holder){
        holder.tvDetailedSummaryTime.setEnabled(true);
        holder.tvDetailedSummaryDate.setEnabled(true);
        holder.etDetailedSummaryStaffName.setEnabled(true);
        holder.etDetailedSummarySummary.setEnabled(true);
        holder.etDetailedSummaryKeyNegative.setEnabled(true);
        holder.etDetailedSummaryKeyPositive.setEnabled(true);
        holder.etDetailedSummaryRecommendation.setEnabled(true);
    }

    private void disableView(DetailedSummaryAuditViewHolder holder){
        holder.tvDetailedSummaryTime.setEnabled(false);
        holder.tvDetailedSummaryDate.setEnabled(false);
        holder.etDetailedSummaryStaffName.setEnabled(false);
        holder.etDetailedSummarySummary.setEnabled(false);
        holder.etDetailedSummaryKeyNegative.setEnabled(false);
        holder.etDetailedSummaryKeyPositive.setEnabled(false);
        holder.etDetailedSummaryRecommendation.setEnabled(false);
    }
}
