package com.YC2010.MyClass.ui.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.YC2010.MyClass.data.Connections;
import com.YC2010.MyClass.ui.adapters.CourseDetailPagerAdapter;
import com.YC2010.MyClass.data.fetchtasks.SearchFetchTask;
import com.YC2010.MyClass.callbacks.AsyncTaskCallbackInterface;
import com.YC2010.MyClass.R;


/**
 * Created by Danny on 2015/6/27.
 */
public class SearchFragment extends Fragment {
    private String mCourse;
    private Activity mActivity;
    private View mView;
    private float defaultElevation;
    private ProgressDialog mProgDialog;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_COURSE = "COURSE";

    public SearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_search, container, false);
            generateView();
        }

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

    private void showErrorMessage(ErrorState errorState) {
        final LinearLayout errorView = (LinearLayout) mView.findViewById(R.id.error_view_layout);
        TextView errorMessage = (TextView) mView.findViewById(R.id.error_view_message);
        ImageView errorIcon = (ImageView) mView.findViewById(R.id.error_view_icon);
        Button errorButton = (Button) mView.findViewById(R.id.error_view_button);

        switch (errorState) {
            case Network:
                errorMessage.setText(mActivity.getString(R.string.error_message_network));
                errorIcon.setImageResource(R.drawable.ic_cloud_off_black_36dp);
                errorButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        errorView.setVisibility(View.GONE);
                        generateView();
                    }
                });
                break;
            case CourseNotFound:
                errorMessage.setText(mActivity.getString(R.string.error_message_course));
                errorIcon.setImageResource(R.drawable.ic_report_black_36dp);
                errorButton.setVisibility(View.GONE);
                break;
        }

        errorView.setVisibility(View.VISIBLE);
    }

    private void generateView() {
        showProgressDialog();

        if (!Connections.isNetworkAvailable(mActivity)) {
            mProgDialog.dismiss();
            showErrorMessage(ErrorState.Network);
        } else {
            SearchFetchTask searchFetchTask = new SearchFetchTask(mActivity, new AsyncTaskCallbackInterface() {
                @Override
                public void onOperationComplete(Bundle bundle) {
                    if (bundle != null && bundle.getBoolean("valid_return")) {
                        setupTabs(bundle);
                        LinearLayout searchLayout = (LinearLayout) mView.findViewById(R.id.search_detail_layout);
                        searchLayout.setVisibility(View.VISIBLE);
                    } else {
                        showErrorMessage(ErrorState.CourseNotFound);
                    }
                    mProgDialog.dismiss();
                }
            });
            searchFetchTask.execute(mCourse);
        }
    }

    private void showProgressDialog() {
        if (mProgDialog == null) {
            mProgDialog = new ProgressDialog(mActivity);
            mProgDialog.setMessage("Loading...");
            mProgDialog.setCanceledOnTouchOutside(false);
            mProgDialog.setIndeterminate(false);
            mProgDialog.setCancelable(false);
            mProgDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }

        mProgDialog.show();
    }
}

enum ErrorState {
    Network, CourseNotFound
}