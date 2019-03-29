package com.gdi.activity.Audit;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.gdi.R;
import com.gdi.activity.BaseActivity;
import com.gdi.fragment.Audit.AssignedFragmentTab;
import com.gdi.fragment.Audit.RejectedFragmentTab;
import com.gdi.fragment.Audit.ResumeFragmentTab;
import com.gdi.fragment.Audit.SubmittedFragmentTab;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AssignmentActivity extends BaseActivity {

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
    public static final String TAG = AssignmentActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);
        ButterKnife.bind(this);
        //setSupportActionBar(toolbar);
        // setActionBar();
        context = this;
        brandFilter = getIntent().getStringExtra("brandId");
        locationFilter = getIntent().getStringExtra("locationId");
        typeId = getIntent().getStringExtra("typeId");
        initView();
    }
    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        filter_tab = findViewById(R.id.tab_layout_listing_filter);
        audit_listing_viewpager = findViewById(R.id.vp_audit_listing);
        setActionBar();


        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        audit_listing_viewpager.setAdapter(viewPagerAdapter);
        filter_tab.setupWithViewPager(audit_listing_viewpager);
    }

    private void setActionBar() {
        initToolbar(toolbar);
        setTitle("My Assignments");
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
                    fragment = new AssignedFragmentTab();
                    fragment.setArguments(arg);
                    break;
                case 1:
                    fragment = new ResumeFragmentTab();
                    fragment.setArguments(arg);
                    break;
                case 2:
                    fragment = new SubmittedFragmentTab();
                    fragment.setArguments(arg);
                    break;
                case 3:
                    fragment = new RejectedFragmentTab();
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
}
