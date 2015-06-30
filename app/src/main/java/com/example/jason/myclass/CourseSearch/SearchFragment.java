package com.example.jason.myclass.CourseSearch;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.jason.myclass.R;

/**
 * Created by Danny on 2015/6/27.
 */
public class SearchFragment extends Fragment {
    private String mCourse;

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
    public void onStart() {
        super.onStart();

        SearchFetchTask searchFetchTask = new SearchFetchTask(getActivity());
        searchFetchTask.execute(mCourse);

    }

}
