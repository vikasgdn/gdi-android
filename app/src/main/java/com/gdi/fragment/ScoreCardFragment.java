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

import com.gdi.activity.internalaudit.InternalAuditDashboardActivity;
import com.gdi.hotel.mystery.audits.R;
import com.gdi.activity.BaseActivity;
import com.gdi.activity.MainActivity;
import com.gdi.utils.AppPrefs;
import com.gdi.utils.AppUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScoreCardFragment extends Fragment {

    @BindView(R.id.dashboard_layout)
    LinearLayout dashboardLayout;
    @BindView(R.id.mystery_audit_layout)
    LinearLayout mysteryAuditLayout;
    @BindView(R.id.internal_layout)
    LinearLayout internalLayout;
    @BindView(R.id.self_assessment_layout)
    LinearLayout selfAssessmentLayout;
    @BindView(R.id.ll_dashboard_container)
    LinearLayout dashboardContainer;
    private Context context;
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
        return view;
    }

    private void initViews(View view) {
        setActionBar();
        dashboardLayout = view.findViewById(R.id.dashboard_layout);
        mysteryAuditLayout = view.findViewById(R.id.mystery_audit_layout);
        internalLayout = view.findViewById(R.id.internal_layout);
        selfAssessmentLayout = view.findViewById(R.id.self_assessment_layout);
        dashboardContainer = view.findViewById(R.id.ll_dashboard_container);




       /* dashboardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ReportDashboardActivity.class));
            }
        });*/

        mysteryAuditLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMisteryScreen();
            }
        });

        internalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getActivity(), InternalAuditDashboardActivity.class);
                startActivity(intent);

            }
        });

        selfAssessmentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // startActivity(new Intent(context, AuditFilterActivity.class));
            }
        });

        if (AppPrefs.getUserRole(context) == 400 ||  AppPrefs.getUserRole(context) == 280){
           // dashboardLayout.setVisibility(View.GONE);
            mysteryAuditLayout.setVisibility(View.GONE);
            internalLayout.setVisibility(View.VISIBLE);
        }else if(AppPrefs.getUserRole(context) == 300)
        {
          //  dashboardLayout.setVisibility(View.GONE);
            mysteryAuditLayout.setVisibility(View.VISIBLE);
            internalLayout.setVisibility(View.GONE);
        }
        else {
           // dashboardLayout.setVisibility(View.VISIBLE);
            mysteryAuditLayout.setVisibility(View.VISIBLE);
            internalLayout.setVisibility(View.VISIBLE);
        }
    }

    private void setDashboardScreen() {
        FragmentManager fragmentManager = ((MainActivity)context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contentFrame, new DashboardFragment());
        fragmentTransaction.addToBackStack(DashboardFragment.TAG);
        fragmentTransaction.commit();
    }


    private void setInternalAuditScreen() {
       /* FragmentManager fragmentManager = ((MainActivity)context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contentFrame, new InternalAuditFragment());
        fragmentTransaction.addToBackStack(InternalAuditFragment.TAG);
        fragmentTransaction.commit();
*/
      //Intent intent=new Intent(getActivity(), ExoPlayer.class);
     // startActivity(intent);
    }

    private void setMisteryScreen()
    {

        if(AppPrefs.getUserRole(context) == 300)
        {
         /*   Intent intent = new Intent(context, AssignmentActivityForMistery.class);
            intent.putExtra("brandId", "");
            intent.putExtra("locationId", "");
            intent.putExtra("typeId", "");
            intent.putExtra("type", "Mystery");
            startActivity(intent);*/
        }
        else
        {

            FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.contentFrame, new MisteryAuditFragment());
            fragmentTransaction.addToBackStack(MisteryAuditFragment.TAG);
            fragmentTransaction.commit();
        }
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
