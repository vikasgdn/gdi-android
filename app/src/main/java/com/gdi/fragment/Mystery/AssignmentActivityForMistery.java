package com.gdi.fragment.Mystery;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.gdi.hotel.mystery.audits.R;
import com.gdi.activity.BaseActivity;
import com.gdi.fragment.Audit.AssignedFragmentTab;
import com.gdi.fragment.Audit.RejectedFragmentTab;
import com.gdi.fragment.Audit.ResumeFragmentTab;
import com.gdi.fragment.Audit.SubmittedFragmentTab;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AssignmentActivityForMistery extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tab_layout_listing_filter)
    TabLayout filter_tab;
    @BindView(R.id.vp_audit_listing)
    ViewPager audit_listing_viewpager;
    Context context;
    String brandFilter = "";
    String locationFilter = "";
    String typeId = "";
    String type = "";
    public static final String TAG = AssignmentActivityForMistery.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);
        ButterKnife.bind(this);
        context = this;
        brandFilter = getIntent().getStringExtra("brandId");
        locationFilter = getIntent().getStringExtra("locationId");
        typeId = getIntent().getStringExtra("typeId");
        type = getIntent().getStringExtra("type");
        initView();
    }
    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        filter_tab = findViewById(R.id.tab_layout_listing_filter);
        audit_listing_viewpager = findViewById(R.id.vp_audit_listing);
        setActionBar();


        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        audit_listing_viewpager.setAdapter(viewPagerAdapter);
        filter_tab.setupWithViewPager(audit_listing_viewpager);
        setTab();
    }

    private void setActionBar() {
        initToolbar(toolbar);
        setTitle(getResources().getString(R.string.mystery_audit));
        enableBack(true);
        enableBackPressed();
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            Bundle arg = new Bundle();
            arg.putString("brandId", brandFilter);
            arg.putString("locationId", locationFilter);
            arg.putString("typeId", typeId);
            switch (position){
                case 0:
                    fragment = new AssignedFragmentTabMistery();
                    fragment.setArguments(arg);
                    break;
                case 1:
                    fragment = new ResumeFragmentTabMistery();
                    fragment.setArguments(arg);
                    break;
                case 2:
                    fragment = new SubmittedFragmentTabMistery();
                    fragment.setArguments(arg);
                    break;
                case 3:
                    fragment = new RejectedFragmentTabMistery();
                    fragment.setArguments(arg);
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = null;
            switch (position){
                case 0:
                    title = "Assigned";
                    break;
                case 1:
                    title = "Resume";
                    break;
                case 2:
                    title = "Submitted";
                    break;
                case 3:
                    title = "Rejected";
                    break;
            }
            return title;
        }
    }

    private void setTab(){
        filter_tab.getTabAt(0).setCustomView(R.layout.tab_view);
        TextView assigned = filter_tab.getTabAt(0).getCustomView().findViewById(R.id.tab_text);
        assigned.setText(getResources().getString(R.string.assigned));
        filter_tab.getTabAt(1).setCustomView(R.layout.tab_view);
        TextView resume = filter_tab.getTabAt(1).getCustomView().findViewById(R.id.tab_text);
        resume.setText(getResources().getString(R.string.resume));
        filter_tab.getTabAt(2).setCustomView(R.layout.tab_view);
        TextView submitted = filter_tab.getTabAt(2).getCustomView().findViewById(R.id.tab_text);
        submitted.setText(getResources().getString(R.string.submitted));
        filter_tab.getTabAt(3).setCustomView(R.layout.tab_view);
        TextView rejected = filter_tab.getTabAt(3).getCustomView().findViewById(R.id.tab_text);
        rejected.setText(getResources().getString(R.string.rejected));

    }
}
