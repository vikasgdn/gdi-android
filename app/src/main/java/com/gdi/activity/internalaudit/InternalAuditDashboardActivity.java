package com.gdi.activity.internalaudit;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.gdi.activity.BaseActivity;
import com.gdi.hotel.mystery.audits.R;
import com.gdi.utils.AppPrefs;
import com.gdi.utils.AppUtils;
import com.gdi.utils.Headers;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InternalAuditDashboardActivity extends BaseActivity {

    private Context context;
    String image;
   public static final String TAG = InternalAuditDashboardActivity.class.getSimpleName();
    private AuditActionFragmentPagerAdapter sampleFragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_audit_action_ia);
        context = this;
        initView();
        initVar();
    }

    private void initView() {

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPagerDynamic);

        sampleFragmentPagerAdapter=new AuditActionFragmentPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(sampleFragmentPagerAdapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayoutDynamicViewPager);


        tabLayout.setupWithViewPager(viewPager);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab)
            {

            }
        });

    }

    private void initVar() {

    }


}
