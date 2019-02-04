package com.gdi.fragment;

import android.content.Context;
import android.content.Intent;
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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gdi.R;
import com.gdi.activity.AuditAnalysisActivity;
import com.gdi.activity.BaseActivity;
import com.gdi.activity.MainActivity;
import com.gdi.activity.ReportDashboardActivity;
import com.gdi.activity.SignInActivity;
import com.gdi.api.FilterRequest;
import com.gdi.api.VolleyNetworkRequest;
import com.gdi.model.filter.FilterRootObject;
import com.gdi.utils.ApiResponseKeys;
import com.gdi.utils.AppConstant;
import com.gdi.utils.AppLogger;
import com.gdi.utils.AppPrefs;
import com.gdi.utils.AppUtils;
import com.gdi.utils.CustomDialog;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScoreCardFragment extends Fragment {

    @BindView(R.id.dashboard_layout)
    LinearLayout dashboardLayout;
    @BindView(R.id.mystery_audit_layout)
    LinearLayout mysteryAuditLayout;
    @BindView(R.id.heart_house_layout)
    LinearLayout heartHouseLayout;
    @BindView(R.id.self_assessment_layout)
    LinearLayout selfAssessmentLayout;
    private Context context;
    private CustomDialog customDialog;
    public static final String TAG = ScoreCardFragment.class.getSimpleName();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public void onResume() {
        super.onResume();
        AppUtils.hideKeyboard((MainActivity)context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_score_card, container, false);
        ButterKnife.bind(this, view);
        initViews(view);
        //filterList();
        return view;
    }

    private void initViews(View view) {
        setActionBar();
        dashboardLayout = (LinearLayout) view.findViewById(R.id.dashboard_layout);
        mysteryAuditLayout = (LinearLayout) view.findViewById(R.id.mystery_audit_layout);
        heartHouseLayout = (LinearLayout) view.findViewById(R.id.heart_house_layout);
        selfAssessmentLayout = (LinearLayout) view.findViewById(R.id.self_assessment_layout);
        //filterList();//check login
        dashboardLayout.setLayoutParams(new RelativeLayout.LayoutParams
                (AppConstant.boxSize,AppConstant.boxSize));
        mysteryAuditLayout.setLayoutParams(new RelativeLayout.LayoutParams
                (AppConstant.boxSize,AppConstant.boxSize));
        heartHouseLayout.setLayoutParams(new RelativeLayout.LayoutParams
                (AppConstant.boxSize,AppConstant.boxSize));
        selfAssessmentLayout.setLayoutParams(new RelativeLayout.LayoutParams
                (AppConstant.boxSize,AppConstant.boxSize));

        dashboardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ReportDashboardActivity.class));
            }
        });

        mysteryAuditLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setReportScreen();
            }
        });

        heartHouseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHeartHouseScreen();
            }
        });

        selfAssessmentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //,startActivity(new Intent(context, AuditAnalysisActivity.class));
            }
        });
    }

    private void setDashboardScreen() {
        FragmentManager fragmentManager = ((MainActivity)context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contentFrame, new DashboardFragment());
        fragmentTransaction.addToBackStack(DashboardFragment.TAG);
        fragmentTransaction.commit();
    }

    private void setHeartHouseScreen() {
        FragmentManager fragmentManager = ((MainActivity)context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contentFrame, new HeartOfTheHouseFragment());
        fragmentTransaction.addToBackStack(HeartOfTheHouseFragment.TAG);
        fragmentTransaction.commit();
    }

    private void setReportScreen() {
        FragmentManager fragmentManager = ((MainActivity)context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contentFrame, new ReportFragment());
        fragmentTransaction.addToBackStack(ReportFragment.TAG);
        fragmentTransaction.commit();
    }

    private void setActionBar() {
        ((BaseActivity)context).setTitle("Guest Delight International");
        ActionBar actionBar = ((MainActivity)context).getSupportActionBar();
        if (actionBar != null) {
            // actionBar.setDisplayShowCustomEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
            MainActivity.mDraweToggle.setDrawerIndicatorEnabled(true);
        }
    }
}
