package com.gdi.activity.internalaudit.adapter;
import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.gdi.hotel.mystery.audits.R;


public class ActionPlanPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    //private String tabTitles[] = new String[] { "Details","Update", "Complete"};
    private String[] tabTitles =null;
    private Context context;

    public ActionPlanPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        //tabTitles=context.getResources().getStringArray(R.array.arr_action_title);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position)
    {
        Fragment fragment=null;
        switch (position) {
            case 0:
              //  fragment= ActionDetailsFragment.newInstance(position);
                break;
            case 1:
               // fragment= ActionUpdateFragment.newInstance(position);
                break;
            case 2:
               // fragment= ActionCompleteFragment.newInstance(position);
                break;
            default:
                //fragment= ActionDetailsFragment.newInstance(position);
                break;

        }
        return  fragment;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];

    }
}
