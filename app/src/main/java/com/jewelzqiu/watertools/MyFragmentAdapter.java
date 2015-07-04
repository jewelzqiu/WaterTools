package com.jewelzqiu.watertools;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Jewelz on 2015-7-4.
 */
public class MyFragmentAdapter extends FragmentPagerAdapter {

    private HydraulicFragment mHydraulicFragment;
    private FullPipeFlowFragment mFullPipeFlowFragment;

    private String[] tabTitles;
    private Context mContext;
    private int pageCount;

    public MyFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
        tabTitles = context.getResources().getStringArray(R.array.tab_titles);
        pageCount = tabTitles.length;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        if (position == 0) {
            if (mHydraulicFragment == null) {
                mHydraulicFragment = new HydraulicFragment();
            }
            fragment = mHydraulicFragment;
        } else {
            if (mFullPipeFlowFragment == null) {
                mFullPipeFlowFragment = new FullPipeFlowFragment();
            }
            fragment = mFullPipeFlowFragment;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return pageCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
