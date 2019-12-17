package com.gdi.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.gdi.R;
import com.gdi.activity.BaseActivity;
import com.gdi.activity.InternalAuditDashboardActivity;
import com.gdi.activity.MainActivity;
import com.gdi.model.filter.FilterInfo;
import com.gdi.utils.AppConstant;
import com.gdi.utils.AppPrefs;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InternalAuditFragment extends Fragment {

    @BindView(R.id.audit_dashboard_layout)
    LinearLayout auditDashboardLayout;
    @BindView(R.id.report_layout)
    LinearLayout reportLayout;
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
        reportLayout = view.findViewById(R.id.report_layout);
        auditDashboardLayout = view.findViewById(R.id.audit_dashboard_layout);
        auditLayout = view.findViewById(R.id.audit_layout);

        /*if (AppPrefs.getClientRoleName(context).equals("OYO Auditor") ){
            reportLayout.setVisibility(View.GONE);
            auditDashboardLayout.setVisibility(View.GONE);
            auditLayout.setVisibility(View.VISIBLE);
        }else {
            reportLayout.setVisibility(View.VISIBLE);
            auditDashboardLayout.setVisibility(View.VISIBLE);
            auditLayout.setVisibility(View.VISIBLE);
        }*/
        if (AppPrefs.getUserRole(context) == 280){
            reportLayout.setVisibility(View.GONE);
            auditDashboardLayout.setVisibility(View.GONE);
            auditLayout.setVisibility(View.VISIBLE);
        }else {
            reportLayout.setVisibility(View.VISIBLE);
            auditDashboardLayout.setVisibility(View.VISIBLE);
            auditLayout.setVisibility(View.VISIBLE);
        }

        //set screen tabs layout
        reportLayout.setLayoutParams(new RelativeLayout.LayoutParams
                (AppConstant.boxSize,AppConstant.boxSize));
        auditDashboardLayout.setLayoutParams(new RelativeLayout.LayoutParams
                (AppConstant.boxSize,AppConstant.boxSize));
        auditLayout.setLayoutParams(new RelativeLayout.LayoutParams
                (AppConstant.boxSize,AppConstant.boxSize));

        reportLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setInternalAuditScreen();
            }
        });

        auditDashboardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, InternalAuditDashboardActivity.class));
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
