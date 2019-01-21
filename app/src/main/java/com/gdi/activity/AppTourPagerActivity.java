package com.gdi.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.fragment.FragmentTab;
import com.gdi.fragment.FragmentTab2;
import com.gdi.fragment.FragmentTab3;
import com.gdi.fragment.FragmentTab4;
import com.gdi.fragment.FragmentTab5;
import com.gdi.utils.AppLogger;
import com.rd.PageIndicatorView;

import butterknife.BindView;

public class AppTourPagerActivity extends BaseActivity {

    @BindView(R.id.view_pager_slides)
    ViewPager pagerSlider;
    @BindView(R.id.slides_indicator)
    PageIndicatorView slidesIndicator;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_tour_pager);
        context = this;
        //AppPrefs.setInstalled(context, false);
        pagerSlider = (ViewPager) findViewById(R.id.view_pager_slides);
        pagerSlider.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        AppLogger.e("PagerActivity", "pagecurrentitem" + pagerSlider.getCurrentItem());
    }

    public void jumpToNextPage() {
        if (pagerSlider.getCurrentItem() == 8){
            Intent intent = new Intent(context, SignInActivity.class);
            startActivity(intent);
            finish();
        }else {
            pagerSlider.setCurrentItem(pagerSlider.getCurrentItem() + 1, true);
            AppLogger.e("PagerActivity", "pagecurrentitem" + pagerSlider.getCurrentItem());
        }
    }

    public void jumpToPreviousPage() {
        pagerSlider.setCurrentItem(pagerSlider.getCurrentItem() - 1, true);
        AppLogger.e("PagerActivity", "pagecurrentitem" + pagerSlider.getCurrentItem());
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch(pos) {

                case 0:
                    return new FragmentTab();
                case 1:
                    return new FragmentTab2();
                case 2:
                    return new FragmentTab3();
                case 3:
                    return new FragmentTab4();
                case 4:
                    return new FragmentTab5();
                default:
                    return new FragmentTab();
            }
        }

        @Override
        public int getCount() {
            return 5;
        }
    }
}
