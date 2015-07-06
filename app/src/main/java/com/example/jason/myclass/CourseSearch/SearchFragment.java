package com.example.jason.myclass.CourseSearch;

import android.app.Fragment;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.jason.myclass.CourseSelect.ShowList.AsyncTaskCallbackInterface;
import com.example.jason.myclass.R;

/**
 * Created by Danny on 2015/6/27.
 */
public class SearchFragment extends Fragment {
    private String mCourse;

    public SearchFragment(){}

    public SearchFragment(String course){
        mCourse = course;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        return rootView;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SearchFetchTask searchFetchTask = new SearchFetchTask(getActivity(), new AsyncTaskCallbackInterface() {
            @Override
            public void onOperationComplete(Bundle bundle) {
                // get default devider
                int[] attrs = { android.R.attr.listDivider };
                TypedArray ta = getActivity().obtainStyledAttributes(attrs);
                Drawable divider = ta.getDrawable(0);
                ta.recycle();

                LinearLayout mLecLinearLayout = (LinearLayout) getActivity().findViewById(R.id.lec_list);
                SectionListAdapter LecAdapter = new SectionListAdapter(getActivity(), R.layout.section_item, bundle);
                for (int i = 0; i < LecAdapter.getCount(); i++) {
                    View view = LecAdapter.getView(i, null, mLecLinearLayout);
                    mLecLinearLayout.addView(view);
                    mLecLinearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE | LinearLayout.SHOW_DIVIDER_BEGINNING);
                    mLecLinearLayout.setDividerDrawable(divider);
                }

                LinearLayout mTstLinearLayout = (LinearLayout) getActivity().findViewById(R.id.tst_list);
                TstListAdapter TstAdapter = new TstListAdapter(getActivity(), R.layout.section_item, bundle);
                for (int i = 0; i < TstAdapter.getCount(); i++) {
                    View view = TstAdapter.getView(i, null, mTstLinearLayout);
                    mTstLinearLayout.addView(view);
                    mTstLinearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE | LinearLayout.SHOW_DIVIDER_BEGINNING);
                    mTstLinearLayout.setDividerDrawable(divider);
                }

                LinearLayout mTutLinearLayout = (LinearLayout) getActivity().findViewById(R.id.tut_list);
                TutListAdapter TutAdapter = new TutListAdapter(getActivity(), R.layout.section_item, bundle);
                for (int i = 0; i < TutAdapter.getCount(); i++) {
                    View view = TutAdapter.getView(i, null, mTutLinearLayout);
                    mTutLinearLayout.addView(view);
                    mTutLinearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE | LinearLayout.SHOW_DIVIDER_BEGINNING);
                    mTutLinearLayout.setDividerDrawable(divider);
                }
            }
        });
        searchFetchTask.execute(mCourse);

    }

}
