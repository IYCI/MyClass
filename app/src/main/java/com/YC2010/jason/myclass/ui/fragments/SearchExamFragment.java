package com.YC2010.jason.myclass.ui.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.YC2010.jason.myclass.R;
import com.YC2010.jason.myclass.ui.adapters.FinalsListAdapter;

/**
 * Created by Danny on 2015/12/26.
 */
public class SearchExamFragment extends Fragment {
    private Activity mActivity;
    private View mView;
    private Bundle mFetchedResult;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mActivity = getActivity();
        this.mFetchedResult = this.getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_search_exam, container, false);

        if (mFetchedResult.getBoolean("has_finals", false)) {
            TextView finals = (TextView) mView.findViewById(R.id.final_exams);
            if (finals != null) {
                finals.setVisibility(View.VISIBLE);
            }
            updateView();
        }

        return mView;
    }

    public void updateView() {
        // get default divider
        int[] attrs = {android.R.attr.listDivider};
        TypedArray ta = mActivity.obtainStyledAttributes(attrs);
        Drawable divider = ta.getDrawable(0);
        ta.recycle();

        LinearLayout mFinalLinearLayout = (LinearLayout) mView.findViewById(R.id.finals_list);
        FinalsListAdapter FinalsAdapter = new FinalsListAdapter(mActivity, R.layout.section_item, mFetchedResult);
        for (int i = 0; i < FinalsAdapter.getCount(); i++) {
            View view = FinalsAdapter.getView(i, null, mFinalLinearLayout);
            mFinalLinearLayout.addView(view);
            mFinalLinearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE | LinearLayout.SHOW_DIVIDER_BEGINNING);
            mFinalLinearLayout.setDividerDrawable(divider);
        }
    }
}
