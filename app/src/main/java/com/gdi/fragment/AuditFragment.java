package com.gdi.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.gdi.R;
import com.gdi.activity.Audit.AuditFilterActivity;
import com.gdi.activity.BaseActivity;
import com.gdi.activity.MainActivity;
import com.gdi.model.filter.FilterInfo;
import com.gdi.utils.AppConstant;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AuditFragment extends Fragment {

    @BindView(R.id.product_layout)
    LinearLayout productLayout;
    @BindView(R.id.self_assessment_layout)
    LinearLayout selfAssessmentLayout;
    @BindView(R.id.heart_house_layout)
    LinearLayout heartHouseLayout;
    private FilterInfo filterInfo;
    private Context context;
    public static final String TAG = AuditFragment.class.getSimpleName();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_standard_report, container, false);
        ButterKnife.bind(this, view);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        setActionBar();
        productLayout = (LinearLayout) view.findViewById(R.id.product_layout);
        selfAssessmentLayout = (LinearLayout) view.findViewById(R.id.self_assessment_layout);
        heartHouseLayout = (LinearLayout) view.findViewById(R.id.heart_house_layout);

        //set screen tabs layout
        productLayout.setLayoutParams(new RelativeLayout.LayoutParams
                (AppConstant.boxSize,AppConstant.boxSize));
        selfAssessmentLayout.setLayoutParams(new RelativeLayout.LayoutParams
                (AppConstant.boxSize,AppConstant.boxSize));
        heartHouseLayout.setLayoutParams(new RelativeLayout.LayoutParams
                (AppConstant.boxSize,AppConstant.boxSize));


        selfAssessmentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AuditFilterActivity.class);
                intent.putExtra("type_id", "1");
                intent.putExtra("type", "Self Assessment");
                startActivity(intent);
            }
        });
        heartHouseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AuditFilterActivity.class);
                intent.putExtra("type_id", "2");
                intent.putExtra("type", "Heart of the House");
                startActivity(intent);
            }
        });
        productLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AuditFilterActivity.class);
                intent.putExtra("type_id", "3");
                intent.putExtra("type", "Inspection");
                startActivity(intent);
            }
        });
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
