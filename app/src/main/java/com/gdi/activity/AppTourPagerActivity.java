package com.gdi.activity;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.gdi.R;
import com.gdi.fragment.Tour.FragmentTab;
import com.gdi.fragment.Tour.FragmentTab2;
import com.gdi.fragment.Tour.FragmentTab3;
import com.gdi.fragment.Tour.FragmentTab4;
import com.gdi.utils.AppLogger;
import com.rd.PageIndicatorView;

import butterknife.BindView;

public class AppTourPagerActivity extends BaseActivity {

    @BindView(R.id.view_pager_slides)
    ViewPager pagerSlider;
    @BindView(R.id.slides_indicator)
    public PageIndicatorView slidesIndicator;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_tour_pager);
        context = this;
        //AppPrefs.setInstalled(context, false);
        pagerSlider = findViewById(R.id.view_pager_slides);
        slidesIndicator = findViewById(R.id.slides_indicator);
        pagerSlider.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        AppLogger.e("PagerActivity", "pagecurrentitem" + pagerSlider.getCurrentItem());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
                default:
                    return new FragmentTab();
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}
