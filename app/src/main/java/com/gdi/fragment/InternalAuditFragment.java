package com.gdi.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.gdi.R;
import com.gdi.activity.BaseActivity;
import com.gdi.activity.MainActivity;
import com.gdi.model.filter.FilterInfo;
import com.gdi.utils.AppConstant;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InternalAuditFragment extends Fragment {

    @BindView(R.id.report_layout)
    LinearLayout reportLayout;
    @BindView(R.id.analysis_layout)
    LinearLayout analysisLayout;
    @BindView(R.id.audit_layout)
    LinearLayout auditLayout;
    private FilterInfo filterInfo;
    private Context context;
    public static final String TAG = InternalAuditFragment.class.getSimpleName();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_heart_of_the_house, container, false);
        ButterKnife.bind(this, view);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        setActionBar();
        reportLayout = (LinearLayout) view.findViewById(R.id.report_layout);
        analysisLayout = (LinearLayout) view.findViewById(R.id.analysis_layout);
        auditLayout = (LinearLayout) view.findViewById(R.id.audit_layout);

        //set screen tabs layout
        reportLayout.setLayoutParams(new RelativeLayout.LayoutParams
                (AppConstant.boxSize,AppConstant.boxSize));
        analysisLayout.setLayoutParams(new RelativeLayout.LayoutParams
                (AppConstant.boxSize,AppConstant.boxSize));
        auditLayout.setLayoutParams(new RelativeLayout.LayoutParams
                (AppConstant.boxSize,AppConstant.boxSize));

        reportLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setInternalAuditScreen();
            }
        });

        analysisLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        auditLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAuditScreen();
            }
        });
    }

    private void setInternalAuditScreen() {
        FragmentManager fragmentManager = ((MainActivity)context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contentFrame, new AuditReportFragment());
        fragmentTransaction.addToBackStack(AuditReportFragment.TAG);
        fragmentTransaction.commit();
    }

    private void setAuditScreen() {
        FragmentManager fragmentManager = ((MainActivity)context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contentFrame, new AuditFragment());
        fragmentTransaction.addToBackStack(AuditFragment.TAG);
        fragmentTransaction.commit();
    }

    private void setActionBar() {
        ((BaseActivity)context).setTitle("Internal Audit");
        ActionBar actionBar = ((MainActivity)context).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
            MainActivity.mDraweToggle.setDrawerIndicatorEnabled(false);
        }
    }
}
