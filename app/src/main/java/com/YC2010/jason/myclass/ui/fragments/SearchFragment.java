package com.YC2010.jason.myclass.ui.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.YC2010.jason.myclass.utils.Constants;
import com.YC2010.jason.myclass.data.Connections;
import com.YC2010.jason.myclass.ui.adapters.FinalsListAdapter;
import com.YC2010.jason.myclass.data.FetchTasks.SearchFetchTask;
import com.YC2010.jason.myclass.ui.adapters.SectionListAdapter;
import com.YC2010.jason.myclass.ui.adapters.TstListAdapter;
import com.YC2010.jason.myclass.ui.adapters.TutListAdapter;
import com.YC2010.jason.myclass.callbacks.AsyncTaskCallbackInterface;
import com.YC2010.jason.myclass.model.CourseInfo;
import com.YC2010.jason.myclass.data.CoursesDBHandler;
import com.YC2010.jason.myclass.model.LectureSectionObject;
import com.YC2010.jason.myclass.R;

import java.util.ArrayList;

/**
 * Created by Danny on 2015/6/27.
 */
public class SearchFragment extends Fragment {
    private String mCourse;
    private Activity mActivity;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_COURSE = "COURSE";

    public SearchFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
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
                TextView courseName = (TextView) mActivity.findViewById(R.id.course_name);
                TextView title = (TextView) mActivity.findViewById(R.id.title);
                TextView description = (TextView) mActivity.findViewById(R.id.description);

                TextView prerequisites = (TextView) mActivity.findViewById(R.id.preReq);
                TextView antirequisites = (TextView) mActivity.findViewById(R.id.antiReq);
                TextView prerequisitesLabel = (TextView) mActivity.findViewById(R.id.preReqLabel);
                TextView antirequisitesLabel = (TextView) mActivity.findViewById(R.id.antiReqLabel);

                courseName.setText(bundle.getString("courseName"));
                title.setText(bundle.getString("title"));
                description.setText(bundle.getString("description"));

                prerequisites.setText(bundle.getString("prerequisites"));
                antirequisites.setText(bundle.getString("antirequisites"));

                prerequisitesLabel.setVisibility(View.VISIBLE);
                antirequisitesLabel.setVisibility(View.VISIBLE);

                if (!bundle.getBoolean("valid_return", true)) {
                    String errorMsg;

                    if (!Connections.isNetworkAvailable(mActivity)) {
                        errorMsg = "Oops!      (´ﾟдﾟ`)\nNo network connection";
                    } else {
                        errorMsg = "Oops!      (ﾉﾟ0ﾟ)ﾉ~\nCourse is not available this term or it may not exist";
                    }

                    SpannableString ss = new SpannableString(errorMsg);
                    ss.setSpan(new ForegroundColorSpan(mActivity.getResources().getColor(R.color.fab_color_1)), 0, 19, 0);// set color
                    courseName.setText(ss);
                    return;
                }

                // Display add course button
                setupAddButton(bundle);

                // Display the views for sections, tutorial, and tests related to the course
                setupLists(bundle);
            }
        });
        searchFetchTask.execute(mCourse);
    }

    void setupLists(Bundle bundle) {

        // get default divider
        int[] attrs = { android.R.attr.listDivider };
        TypedArray ta = mActivity.obtainStyledAttributes(attrs);
        Drawable divider = ta.getDrawable(0);
        ta.recycle();

        LinearLayout mLecLinearLayout = (LinearLayout) mActivity.findViewById(R.id.lec_list);
        SectionListAdapter LecAdapter = new SectionListAdapter(mActivity, R.layout.section_item, bundle);
        for (int i = 0; i < LecAdapter.getCount(); i++) {
            View view = LecAdapter.getView(i, null, mLecLinearLayout);
            mLecLinearLayout.addView(view);
            mLecLinearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE | LinearLayout.SHOW_DIVIDER_BEGINNING);
            mLecLinearLayout.setDividerDrawable(divider);
        }

        LinearLayout mTstLinearLayout = (LinearLayout) mActivity.findViewById(R.id.tst_list);
        TstListAdapter TstAdapter = new TstListAdapter(mActivity, R.layout.section_item, bundle);
        for (int i = 0; i < TstAdapter.getCount(); i++) {
            View view = TstAdapter.getView(i, null, mTstLinearLayout);
            mTstLinearLayout.addView(view);
            mTstLinearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE | LinearLayout.SHOW_DIVIDER_BEGINNING);
            mTstLinearLayout.setDividerDrawable(divider);
        }

        LinearLayout mTutLinearLayout = (LinearLayout) mActivity.findViewById(R.id.tut_list);
        TutListAdapter TutAdapter = new TutListAdapter(mActivity, R.layout.section_item, bundle);
        for (int i = 0; i < TutAdapter.getCount(); i++) {
            View view = TutAdapter.getView(i, null, mTutLinearLayout);
            mTutLinearLayout.addView(view);
            mTutLinearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE | LinearLayout.SHOW_DIVIDER_BEGINNING);
            mTutLinearLayout.setDividerDrawable(divider);
        }

        LinearLayout mFinalLinearLayout = (LinearLayout) mActivity.findViewById(R.id.finals_list);
        FinalsListAdapter FinalsAdapter = new FinalsListAdapter(mActivity, R.layout.section_item, bundle);
        for (int i = 0; i < FinalsAdapter.getCount(); i++) {
            View view = FinalsAdapter.getView(i, null, mFinalLinearLayout);
            mFinalLinearLayout.addView(view);
            mFinalLinearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE | LinearLayout.SHOW_DIVIDER_BEGINNING);
            mFinalLinearLayout.setDividerDrawable(divider);
        }

        final TextView lec = (TextView) mActivity.findViewById(R.id.lec);
        if (lec != null){
            lec.setVisibility(View.VISIBLE);
            if(bundle.getBoolean("isOnline", false)){
                lec.setText("Online");
            }
        }

        if (bundle.getBoolean("has_tst", false)) {
            TextView tst = (TextView) mActivity.findViewById(R.id.tst);
            if (tst != null)
                tst.setVisibility(View.VISIBLE);
        }

        if (bundle.getBoolean("has_tut", false)) {
            TextView tut = (TextView) mActivity.findViewById(R.id.tut);
            if (tut != null)
                tut.setVisibility(View.VISIBLE);
        }

        if (bundle.getBoolean("has_finals", false)) {
            TextView finals = (TextView) mActivity.findViewById(R.id.final_exams);
            if (finals != null)
                finals.setVisibility(View.VISIBLE);
        }
    }

    void setupAddButton(final Bundle bundle) {
        Button add_button = (Button) mActivity.findViewById(R.id.add_course_button);

        if (add_button != null) {
            if(bundle.getBoolean("isOnline", false)) {
                add_button.setVisibility(View.GONE);
            } else {
                if (bundle.getBoolean("course_taken")) {
                    add_button.setText("remove");
                    add_button.setBackgroundTintList(ColorStateList.valueOf(mActivity.getResources().getColor(R.color.fab_color_1)));
                }
                add_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        Button add_button = (Button) mActivity.findViewById(R.id.add_course_button);
                        final CoursesDBHandler db = new CoursesDBHandler(mActivity);

                        String course_taken_num = null;
                        boolean courseTaken = false;

                        final ArrayList<LectureSectionObject> lectureSections = bundle.getParcelableArrayList(Constants.lectureSectionObjectListKey);

                        for (int i = 0; i < lectureSections.size(); i++) {
                            if (db.IsInDB(lectureSections.get(i).getNumber())) {
                                courseTaken = true;
                                course_taken_num = lectureSections.get(i).getNumber();
                                break;
                            }
                        }

                        if (!courseTaken) {
                            // put sec info into a list of string
                            CharSequence[] lecSecList = new CharSequence[lectureSections.size()];
                            for (int i = 0; i < lectureSections.size(); i++) {
                                lecSecList[i] = lectureSections.get(i).getSection();
                            }

                            displayDialog(lecSecList, bundle, db, view);
                        } else {
                            // remove from db
                            if (course_taken_num != null)
                                db.removeCourse(course_taken_num);

                            add_button.setText("add");
                            add_button.setBackgroundTintList(ColorStateList.valueOf(mActivity.getResources().getColor(R.color.dialog_text_color)));

                            // show snackBar
                            Snackbar.make(view, "Course Dropped", Snackbar.LENGTH_SHORT)
                                    .show();
                        }
                        db.close();
                    }
                });
                add_button.setVisibility(View.VISIBLE);
            }
        }
    }

    void displayDialog(CharSequence[] sectionList, final Bundle bundle, final CoursesDBHandler db, final View view) {
        // show dialog to choose sections
        AlertDialog ad = new AlertDialog.Builder(mActivity)
                .setTitle("Choose a section")
                .setSingleChoiceItems(sectionList, 0, null)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        int index = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        ArrayList<LectureSectionObject> lectures =
                                bundle.getParcelableArrayList(Constants.lectureSectionObjectListKey);
                        LectureSectionObject selectedSection = lectures.get(index);

                        // add to db
                        CourseInfo mCourse = new CourseInfo(bundle.getString("courseName"));
                        mCourse.setSec(selectedSection.getSection());
                        mCourse.setNum(selectedSection.getNumber());
                        mCourse.setLoc(selectedSection.getLocation());
                        mCourse.setTimeAPM(selectedSection.getTime());
                        mCourse.setProf(selectedSection.getProfessor());

                        db.addCourse(mCourse);

                        // change button
                        Button add_button = (Button) mActivity.findViewById(R.id.add_course_button);
                        add_button.setText("remove");
                        add_button.setBackgroundTintList(ColorStateList.valueOf(mActivity.getResources().getColor(R.color.fab_color_1)));

                        // show snackBar
                        Snackbar.make(view, "Course Added", Snackbar.LENGTH_SHORT)
                                .show();

                        dialog.dismiss();
                    }
                })
                .create();
        ad.show();

        // change color
        ad.getButton(ad.BUTTON_POSITIVE).setTextColor(mActivity.getResources().getColor(R.color.myPrimaryColor));
        ad.getButton(ad.BUTTON_NEGATIVE).setTextColor(mActivity.getResources().getColor(R.color.myPrimaryColor));
    }
}
