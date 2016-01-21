package com.YC2010.MyClass.ui.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.YC2010.MyClass.R;
import com.YC2010.MyClass.ui.fragments.SearchExamFragment;
import com.YC2010.MyClass.ui.fragments.SearchInfoFragment;
import com.YC2010.MyClass.ui.fragments.SearchScheduleFragment;

/**
 * Created by Danny on 2015/12/25.
 */
public class CourseDetailPagerAdapter extends FragmentStatePagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitle[];
    private Context mContext;
    private Bundle mFetchedResult;

    public CourseDetailPagerAdapter(FragmentManager fragmentManager, Context context, Bundle bundle) {
        super(fragmentManager);
        this.mContext = context;
        this.mFetchedResult = bundle;
        this.tabTitle = new String[] {
                context.getResources().getString(R.string.course_tab_title1),
                context.getResources().getString(R.string.course_tab_title2),
                context.getResources().getString(R.string.course_tab_title3)
        };
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                SearchScheduleFragment searchScheduleFragment = new SearchScheduleFragment();
                searchScheduleFragment.setArguments(mFetchedResult);
                return searchScheduleFragment;
            case 1:
                SearchExamFragment searchExamFragment = new SearchExamFragment();
                searchExamFragment.setArguments(mFetchedResult);
                return searchExamFragment;
            case 2:
                SearchInfoFragment searchInfoFragment = new SearchInfoFragment();
                searchInfoFragment.setArguments(mFetchedResult);
                return searchInfoFragment;
        }
        return new Fragment();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitle[position];
    }
}
