package com.YC2010.MyClass.ui.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.YC2010.MyClass.model.CourseInfo;
import com.YC2010.MyClass.model.LectureSectionObject;
import com.YC2010.MyClass.R;
import com.YC2010.MyClass.data.CoursesDBHandler;
import com.YC2010.MyClass.ui.adapters.SectionListAdapter;
import com.YC2010.MyClass.ui.adapters.TstListAdapter;
import com.YC2010.MyClass.ui.adapters.TutListAdapter;
import com.YC2010.MyClass.utils.Constants;

import java.util.ArrayList;

/**
 * Created by Danny on 2015/12/26.
 */
public class SearchScheduleFragment extends Fragment {
    private Activity mActivity;
    private View mView;
    private Bundle mFetchedResult;
    private boolean mCourseTaken;
    private String mTakenCourseNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mActivity = getActivity();
        this.mFetchedResult = this.getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_search_schedule, container, false);
        TextView courseName = (TextView) mView.findViewById(R.id.course_name);
        courseName.setText(mFetchedResult.getString("courseName"));
        setupLists();
        setupAddButton();

        return mView;
    }

    private void setupLists() {

        // get default divider
        int[] attrs = {android.R.attr.listDivider};
        TypedArray ta = mActivity.obtainStyledAttributes(attrs);
        Drawable divider = ta.getDrawable(0);
        ta.recycle();

        LinearLayout mLecLinearLayout = (LinearLayout) mView.findViewById(R.id.lec_list);
        SectionListAdapter LecAdapter = new SectionListAdapter(mActivity, R.layout.section_item, mFetchedResult);
        for (int i = 0; i < LecAdapter.getCount(); i++) {
            View view = LecAdapter.getView(i, null, mLecLinearLayout);
            mLecLinearLayout.addView(view);
            mLecLinearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE | LinearLayout.SHOW_DIVIDER_BEGINNING);
            mLecLinearLayout.setDividerDrawable(divider);
        }

        TextView lec = (TextView) mView.findViewById(R.id.lec);
        if (lec != null) {
            lec.setVisibility(View.VISIBLE);
        }

        if (mFetchedResult.getBoolean("has_tst", false)) {
            TextView tst = (TextView) mView.findViewById(R.id.tst);
            if (tst != null) {
                tst.setVisibility(View.VISIBLE);
            }

            LinearLayout mTstLinearLayout = (LinearLayout) mView.findViewById(R.id.tst_list);
            TstListAdapter TstAdapter = new TstListAdapter(mActivity, R.layout.section_item, mFetchedResult);
            for (int i = 0; i < TstAdapter.getCount(); i++) {
                View view = TstAdapter.getView(i, null, mTstLinearLayout);
                mTstLinearLayout.addView(view);
                mTstLinearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE | LinearLayout.SHOW_DIVIDER_BEGINNING);
                mTstLinearLayout.setDividerDrawable(divider);
            }
        }

        if (mFetchedResult.getBoolean("has_tut", false)) {
            TextView tut = (TextView) mView.findViewById(R.id.tut);
            if (tut != null) {
                tut.setVisibility(View.VISIBLE);
            }

            LinearLayout mTutLinearLayout = (LinearLayout) mView.findViewById(R.id.tut_list);
            TutListAdapter TutAdapter = new TutListAdapter(mActivity, R.layout.section_item, mFetchedResult);
            for (int i = 0; i < TutAdapter.getCount(); i++) {
                View view = TutAdapter.getView(i, null, mTutLinearLayout);
                mTutLinearLayout.addView(view);
                mTutLinearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE | LinearLayout.SHOW_DIVIDER_BEGINNING);
                mTutLinearLayout.setDividerDrawable(divider);
            }
        }
    }

    void setupAddButton() {
        Button addButton = (Button) mView.findViewById(R.id.add_course_button);

        if (addButton != null) {
            boolean courseTaken = false;

            final ArrayList<LectureSectionObject> lectureSections =
                    mFetchedResult.getParcelableArrayList(Constants.lectureSectionObjectListKey);

            if (lectureSections == null) {
                return;
            }

            final CoursesDBHandler db = new CoursesDBHandler(mActivity);

            for (int i = 0; i < lectureSections.size(); i++) {
                if (db.IsInDB(lectureSections.get(i).getNumber())) {
                    courseTaken = true;
                    mTakenCourseNumber = lectureSections.get(i).getNumber();
                    break;
                }
            }

            setCourseTaken(courseTaken);

            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    if (!mCourseTaken) {
                        // put sec info into a list of string
                        CharSequence[] lecSecList = new CharSequence[lectureSections.size()];
                        for (int i = 0; i < lectureSections.size(); i++) {
                            lecSecList[i] = lectureSections.get(i).getSection();
                        }

                        displayDialog(lecSecList, db, view);
                    } else {
                        setCourseTaken(false);

                        // remove from db
                        if (mTakenCourseNumber != null) {
                            db.removeCourse(mTakenCourseNumber);
                            mTakenCourseNumber = null;
                        }

                        // show snackBar
                        Snackbar.make(view, "Course Dropped", Snackbar.LENGTH_SHORT)
                                .show();
                    }
                    db.close();
                }
            });
            addButton.setVisibility(View.VISIBLE);
        }
    }

    void displayDialog(CharSequence[] sectionList, final CoursesDBHandler db, final View view) {
        // show dialog to choose sections
        AlertDialog ad = new AlertDialog.Builder(mActivity)
                .setTitle("Choose a section")
                .setSingleChoiceItems(sectionList, 0, null)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        int index = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        ArrayList<LectureSectionObject> lectures =
                                mFetchedResult.getParcelableArrayList(Constants.lectureSectionObjectListKey);
                        LectureSectionObject selectedSection = lectures.get(index);

                        // add to db
                        CourseInfo course = new CourseInfo(mFetchedResult.getString("courseName"));
                        course.setSec(selectedSection.getSection());
                        course.setNum(selectedSection.getNumber());
                        course.setLoc(selectedSection.getLocation());
                        course.setIsOnline(selectedSection.isOnline());
                        course.setTimeAPM(selectedSection.getTime());
                        course.setProf(selectedSection.getProfessor());

                        db.addCourse(course, mActivity.getSharedPreferences("TERMS", mActivity.MODE_PRIVATE).getInt("TERM_NUM", 0));
                        mTakenCourseNumber = selectedSection.getNumber();

                        // show snackBar
                        Snackbar.make(view, "Course Added", Snackbar.LENGTH_SHORT)
                                .show();

                        setCourseTaken(true);
                        dialog.dismiss();
                    }
                })
                .create();
        ad.show();

        // change color
        ad.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(mActivity.getResources().getColor(R.color.myPrimaryColor));
        ad.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(mActivity.getResources().getColor(R.color.myPrimaryColor));
    }

    private void setCourseTaken(boolean taken) {
        mCourseTaken = taken;
        Button takeButton = (Button) mView.findViewById(R.id.add_course_button);

        if (takeButton != null) {
            if (taken) {
                takeButton.setText("remove");
                ((AppCompatButton)takeButton).setSupportBackgroundTintList(ColorStateList.valueOf(mActivity.getResources().getColor(R.color.fab_color_1)));
            } else {
                takeButton.setText("Take");
                ((AppCompatButton)takeButton).setSupportBackgroundTintList(ColorStateList.valueOf(mActivity.getResources().getColor(R.color.dialog_text_color)));
            }
        }
    }
}
