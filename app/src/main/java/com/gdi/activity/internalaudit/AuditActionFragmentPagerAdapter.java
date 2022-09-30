package com.gdi.activity.internalaudit;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.gdi.activity.internalaudit.fragment.AuditFragment;
import com.gdi.activity.internalaudit.fragment.actionplan.ActionFragment;
import com.gdi.hotel.mystery.audits.R;

public class AuditActionFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Inspection", "Action"};
    private Context context;

    public AuditActionFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
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
                fragment= AuditFragment.newInstance("mAuditType");
                break;
            case 1:
                fragment= ActionFragment.newInstance(position);
                break;
            default:
                fragment= AuditFragment.newInstance("mAuditType");
                break;

        }
        return  fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        String title=context.getString(R.string.text_action);
        if (position==0)
            title=context.getString(R.string.text_inspection);

        return title;
    }
}
