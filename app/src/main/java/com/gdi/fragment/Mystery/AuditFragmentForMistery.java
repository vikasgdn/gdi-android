package com.gdi.fragment.Mystery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import com.gdi.R;
import com.gdi.activity.Audit.AuditFilterActivity;
import com.gdi.activity.BaseActivity;
import com.gdi.activity.MainActivity;
import com.gdi.model.filter.FilterInfo;
import com.gdi.utils.AppConstant;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AuditFragmentForMistery extends Fragment {

    @BindView(R.id.pre_opening_layout)
    LinearLayout preOpeningLayout;
    @BindView(R.id.product_layout)
    LinearLayout productLayout;
    @BindView(R.id.self_assessment_layout)
    LinearLayout selfAssessmentLayout;
    @BindView(R.id.heart_house_layout)
    LinearLayout heartHouseLayout;
    private FilterInfo filterInfo;
    private Context context;
    public static final String TAG = AuditFragmentForMistery.class.getSimpleName();

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
        productLayout = view.findViewById(R.id.product_layout);
        selfAssessmentLayout = view.findViewById(R.id.self_assessment_layout);
        heartHouseLayout = view.findViewById(R.id.heart_house_layout);
        preOpeningLayout = view.findViewById(R.id.pre_opening_layout);

        //set screen tabs layout
        productLayout.setLayoutParams(new RelativeLayout.LayoutParams(AppConstant.boxSize,AppConstant.boxSize));
        preOpeningLayout.setLayoutParams(new RelativeLayout.LayoutParams(AppConstant.boxSize,AppConstant.boxSize));
        selfAssessmentLayout.setLayoutParams(new RelativeLayout.LayoutParams(AppConstant.boxSize,AppConstant.boxSize));
        heartHouseLayout.setLayoutParams(new RelativeLayout.LayoutParams(AppConstant.boxSize,AppConstant.boxSize));


        selfAssessmentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MisteryAuditFilterActivity.class);
                intent.putExtra("type_id", "1");
                intent.putExtra("type", "Self Assessment");
                startActivity(intent);
            }
        });
        heartHouseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MisteryAuditFilterActivity.class);
                intent.putExtra("type_id", "2");
                intent.putExtra("type", "Product Audit");  // Heart of House Change to Product Audit VIKAS
                startActivity(intent);
            }
        });
        productLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MisteryAuditFilterActivity.class);
                intent.putExtra("type_id", "3");
                intent.putExtra("type", "Inspection");
                startActivity(intent);
            }
        });
        preOpeningLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MisteryAuditFilterActivity.class);
                intent.putExtra("type_id", "4");
                intent.putExtra("type", "Pre Opening");
                startActivity(intent);
            }
        });
    }



    private void setActionBar() {
        ((BaseActivity)context).setTitle("Mystery Audit");
        ActionBar actionBar = ((MainActivity)context).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
            MainActivity.mDraweToggle.setDrawerIndicatorEnabled(false);
        }
    }
}
