package com.YC2010.jason.myclass.Courses;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.YC2010.jason.myclass.CourseSearch.SearchFragment;
import com.YC2010.jason.myclass.R;

import java.util.List;

/**
 * Created by Jason on 2015-05-25.
 */
public class CoursesFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_courses, container, false);

        // RecyclerView:
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.courses_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // get Courses from db
        CoursesDBHandler db = new CoursesDBHandler(getActivity());
        final List<CourseInfo> myDataset = db.getAllCourses();


        // specify an adapter (see also next example)
        mAdapter = new Course_Adapter(myDataset, getActivity());

        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                Log.d("CoursesFragment", "Data set changed");
            }
        });

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                SearchFragment mSearchFragment = new SearchFragment();
                mSearchFragment.setCourse(myDataset.get(position).getCourseName());
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, mSearchFragment)
                        .addToBackStack("7")
                        .commit();
            }

            @Override
            public void onLongClick(final View view, final int position) {
                Log.d("mRecyclerView", "enter onlongclick");
                // vibrate
                Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(10);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Delete this Course?");

                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.courses_recycler_view);
                        Course_Adapter adapter = (Course_Adapter) recyclerView.getAdapter();
                        adapter.removeAt(position);

                        // show snackBar
                        Snackbar.make(view, "Course Removed", Snackbar.LENGTH_SHORT)
                                .show();

                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // nothing
                    }
                });
                AlertDialog confirmation = builder.create();

                confirmation.show();
                confirmation.getButton(confirmation.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.myPrimaryColor));
                confirmation.getButton(confirmation.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.myPrimaryColor));
            }
        }));

        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildLayoutPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(final RecyclerView rv, MotionEvent e) {

            final View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                //Log.d("onInterceptTouchEvent", "before delay");
                // delay 100ms for displaying ripple effect
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        //write the code here which you want to run after 500 milliseconds
                        //Log.d("onInterceptTouchEvent", "delayed");
                        clickListener.onClick(child, rv.getChildLayoutPosition(child));
                    }
                }, 100);
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean bool) {
        }
    }
}
