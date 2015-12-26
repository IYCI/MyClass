package com.YC2010.jason.myclass.ui.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.YC2010.jason.myclass.ui.adapters.CourseDetailPagerAdapter;
import com.YC2010.jason.myclass.data.fetchtasks.SearchFetchTask;
import com.YC2010.jason.myclass.callbacks.AsyncTaskCallbackInterface;
import com.YC2010.jason.myclass.R;


/**
 * Created by Danny on 2015/6/27.
 */
public class SearchFragment extends Fragment {
    private String mCourse;
    private Activity mActivity;
    private View mView;
    private float defaultElevation;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_COURSE = "COURSE";

    public SearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_search, container, false);

        return mView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String course = getArguments().getString(ARG_COURSE);
        mCourse = course;
        mActivity = getActivity();


        if (course == null) {
            Toast.makeText(mActivity, "course string not found", Toast.LENGTH_SHORT).show();
        }

        SearchFetchTask searchFetchTask = new SearchFetchTask(mActivity, new AsyncTaskCallbackInterface() {
            @Override
            public void onOperationComplete(Bundle bundle) {
                setupTabs(bundle);
            }
        });
        searchFetchTask.execute(mCourse);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Remove the shadow between the action bar and the tabs
        ActionBar actionBar = ((AppCompatActivity) mActivity).getSupportActionBar();
        if (actionBar != null) {
            defaultElevation = actionBar.getElevation();
            actionBar.setElevation(0);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        // Restore the shadow in the action bar
        ActionBar actionBar = ((AppCompatActivity) mActivity).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setElevation(defaultElevation);
        }
    }

    private void setupTabs(Bundle bundle) {
        ViewPager viewPager = (ViewPager) mView.findViewById(R.id.viewpager);
        viewPager.setAdapter(new CourseDetailPagerAdapter(mActivity.getFragmentManager(), mActivity, bundle));

        TabLayout tabLayout = (TabLayout) mView.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setVisibility(View.VISIBLE);
    }
}
