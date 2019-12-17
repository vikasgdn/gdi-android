package com.gdi.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.gdi.R;
import com.gdi.activity.BaseActivity;
import com.gdi.activity.MainActivity;
import com.gdi.activity.MysteryAuditReport.ReportDashboardActivity;
import com.gdi.model.filter.FilterInfo;
import com.gdi.utils.AppConstant;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompetetionBenchmarkingFragment extends Fragment {

    @BindView(R.id.mystery_audit_layout)
    LinearLayout mysteryAuditLayout;
    @BindView(R.id.report_dashboard_layout)
    LinearLayout reportDashboardLayout;
    private FilterInfo filterInfo;
    private Context context;
    public static final String TAG = CompetetionBenchmarkingFragment.class.getSimpleName();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this, view);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        setActionBar();
        mysteryAuditLayout = view.findViewById(R.id.mystery_audit_layout);
        reportDashboardLayout = view.findViewById(R.id.report_dashboard_layout);

        //set screen tabs layout
        mysteryAuditLayout.setLayoutParams(new RelativeLayout.LayoutParams
                (AppConstant.boxSize,AppConstant.boxSize));
        reportDashboardLayout.setLayoutParams(new RelativeLayout.LayoutParams
                (AppConstant.boxSize,AppConstant.boxSize));

        mysteryAuditLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ReportDashboardActivity.class));
            }
        });

        reportDashboardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }



    private void setActionBar() {
        ((BaseActivity)context).setTitle("DashBoard");
        ActionBar actionBar = ((MainActivity)context).getSupportActionBar();
        if (actionBar != null) {
            // actionBar.setDisplayShowCustomEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
            MainActivity.mDraweToggle.setDrawerIndicatorEnabled(false);
        }
    }
}
