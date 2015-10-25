package com.YC2010.jason.myclass.CourseSearch;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.YC2010.jason.myclass.Constants;
import com.YC2010.jason.myclass.CourseSelect.ShowList.AsyncTaskCallbackInterface;
import com.YC2010.jason.myclass.Courses.CourseInfo;
import com.YC2010.jason.myclass.Courses.CoursesDBHandler;
import com.YC2010.jason.myclass.DataObjects.FinalObject;
import com.YC2010.jason.myclass.DataObjects.LectureSectionObject;
import com.YC2010.jason.myclass.DataObjects.TestObject;
import com.YC2010.jason.myclass.DataObjects.TutorialObject;
import com.YC2010.jason.myclass.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Danny on 2015/6/27.
 */
public class SearchFetchTask extends AsyncTask<String, Void, Bundle> {

    private Activity mActivity;
    private AsyncTaskCallbackInterface mAsyncTaskCallbackInterface;

    public SearchFetchTask(Activity activity, AsyncTaskCallbackInterface asyncTaskCallbackInterface) {
        this.mActivity = activity;
        mAsyncTaskCallbackInterface = asyncTaskCallbackInterface;
    }

    private ArrayList<LectureSectionObject> lectures = new ArrayList<>();
    private ArrayList<TutorialObject> tutorials = new ArrayList<>();
    private ArrayList<TestObject> tests = new ArrayList<>();
    private ArrayList<FinalObject> finals = new ArrayList<>();

    ProgressDialog progDailog;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progDailog = new ProgressDialog(mActivity);
        progDailog.setMessage("Loading...");
        progDailog.setIndeterminate(false);
        progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDailog.setCancelable(true);
        progDailog.show();
    }

    @Override
    protected Bundle doInBackground(String...params) {
        Bundle result = new Bundle();

        fetchCourse(result, params[0]);

        return result;
    }

    private Bundle fetchCourse(Bundle bundle, String input) {

        try{
            bundle.putBoolean("valid_return", false);

            // get schedule JSONBObject
            String schedule_url = Constants.getScheduleURL(input);
            JSONObject schedulesObject = Constants.getJSON_from_url(schedule_url);

            // check valid data return
            if(!schedulesObject.getJSONObject("meta").getString("message").equals("Request successful")) {
                Log.d("SearchFetchTask",schedulesObject.toString());
                return bundle;
            }
            bundle.putBoolean("valid_return", true);
            JSONArray data = schedulesObject.getJSONArray("data");

            for(int i = 0; i < data.length(); i++){
                JSONObject section = data.getJSONObject(i);
                JSONObject first_class = section.getJSONArray("classes").getJSONObject(0);
                //Log.d("SearchFetchTask",first_class.toString());
                JSONObject date = first_class.getJSONObject("date");
                JSONObject loc = first_class.getJSONObject("location");

                String section_str = section.getString("section");
                String section_type = section_str.substring(0, 3);
                String section_num = section_str.substring(section_str.length() - 3);
                if(section_num.equals("081"))
                    bundle.putBoolean("isOnline", true);
                Log.d("SearchFetchTask", "section_type is <" + section_type + ">");
                CoursesDBHandler db = new CoursesDBHandler(mActivity);

                switch (section_type) {
                    case "LEC":
                        String prof = first_class.getJSONArray("instructors").getString(0);

                        // check if is in db
                        if(db.IsInDB(section.getString("class_number"))){
                            bundle.putBoolean("course_taken", true);
                            bundle.putString("course_taken_num",section.getString("class_number"));
                        }

                        LectureSectionObject lecture = new LectureSectionObject();

                        lecture.setNumber(section.getString("class_number"));
                        lecture.setSection(section_str.substring(4));
                        lecture.setTime(date.getString("weekdays") + " " +
                                date.getString("start_time") + "-" + date.getString("end_time"));
                        lecture.setProfessor(prof);
                        lecture.setLocation(loc.getString("building") + " " + loc.getString("room"));
                        lecture.setCapacity(section.getString("enrollment_capacity"));
                        lecture.setTotal(section.getString("enrollment_total"));

                        lectures.add(lecture);
                        break;

                    case "TST":
                        bundle.putBoolean("has_tst", true);

                        TestObject test = new TestObject();

                        test.setSection(section_str.substring(4));
                        test.setTime(date.getString("start_date") + " " +
                                date.getString("start_time") + "-" + date.getString("end_time"));
                        test.setCapacity(section.getString("enrollment_capacity"));
                        test.setTotal(section.getString("enrollment_total"));

                        tests.add(test);
                        break;

                    case "TUT":
                        bundle.putBoolean("has_tut", true);

                        TutorialObject tutorial = new TutorialObject();

                        tutorial.setNumber(section.getString("class_number"));
                        tutorial.setSection(section_str.substring(4));
                        tutorial.setTime(date.getString("weekdays") + " " +
                                date.getString("start_time") + "-" + date.getString("end_time"));
                        tutorial.setLocation(loc.getString("building") + " " + loc.getString("room"));
                        tutorial.setCapacity(section.getString("enrollment_capacity"));
                        tutorial.setTotal(section.getString("enrollment_total"));

                        tutorials.add(tutorial);
                        break;
                    case "LAB":
                        bundle.putBoolean("has_lab", true);
                        break;
                }
            }

            //String title = data.getString("title");
            bundle.putString("title", data.getJSONObject(0).getString("title"));
            bundle.putString("courseName", data.getJSONObject(0).getString("subject") + " " +
                    data.getJSONObject(0).getString("catalog_number"));

            bundle.putParcelableArrayList(Constants.lectureSectionObjectListKey, lectures);
            bundle.putParcelableArrayList(Constants.tutorialObjectListKey, tutorials);
            bundle.putParcelableArrayList(Constants.testObjectListKey, tests);

            String current_term = (String) Constants.getTerms().get(1);

            // get exam JSONBObject
            String exam_url = Constants.getExamsURL(current_term);
            JSONArray examData = Constants.getJSON_from_url(exam_url).getJSONArray("data");
            for(int i = 0; i < examData.length(); i++){
                if(examData.getJSONObject(i).getString("course").equals(bundle.getString("courseName"))){
                    bundle.putBoolean("has_finals", true);
                    JSONArray exam_sections = examData.getJSONObject(i).getJSONArray("sections");
                    for(int j = 0; j < exam_sections.length(); j++){
                        FinalObject finalObject = new FinalObject();

                        finalObject.setSection(exam_sections.getJSONObject(j).getString("section"));
                        finalObject.setTime(exam_sections.getJSONObject(j).getString("start_time") + "-" +
                                        exam_sections.getJSONObject(j).getString("end_time"));
                        finalObject.setLocation(exam_sections.getJSONObject(j).getString("location"));
                        finalObject.setDate(exam_sections.getJSONObject(j).getString("date"));

                        finals.add(finalObject);
                    }
                }
            }
            bundle.putParcelableArrayList(Constants.finalObjectListKey, finals);

            return bundle;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bundle;
    }

    @Override
    protected void onPostExecute(final Bundle bundle) {
        progDailog.dismiss();

        TextView courseName = (TextView) mActivity.findViewById(R.id.course_name);
        if(!bundle.getBoolean("valid_return", true)){
            if(courseName != null) {
                String errorMsg;
                if(!Constants.isNetworkAvailable(mActivity)){
                    errorMsg = "Oops!      (´ﾟдﾟ`)\nNo network connection";
                }
                else
                    errorMsg = "Oops!      (ﾉﾟ0ﾟ)ﾉ~\nCourse is not available this term or it may not exist";
                SpannableString ss = new SpannableString(errorMsg);
                ss.setSpan(new ForegroundColorSpan(mActivity.getResources().getColor(R.color.fab_color_1)), 0, 19, 0);// set color
                courseName.setText(ss);
                return;
            }
        }

        final TextView lec = (TextView) mActivity.findViewById(R.id.lec);
        if(lec != null){
            lec.setVisibility(View.VISIBLE);
            if(bundle.getBoolean("isOnline", false)){
                lec.setText("Online");
            }
        }

        if(bundle.getBoolean("has_tst", false)) {
            TextView tst = (TextView) mActivity.findViewById(R.id.tst);
            if (tst != null)
                tst.setVisibility(View.VISIBLE);
        }

        if(bundle.getBoolean("has_tut", false)) {
            TextView tut = (TextView) mActivity.findViewById(R.id.tut);
            if (tut != null)
                tut.setVisibility(View.VISIBLE);
        }

        if(bundle.getBoolean("has_finals", false)) {
            TextView finals = (TextView) mActivity.findViewById(R.id.final_exams);
            if (finals != null)
                finals.setVisibility(View.VISIBLE);
        }

        Button add_button = (Button) mActivity.findViewById(R.id.add_course_button);

        if (add_button != null) {
            if(bundle.getBoolean("isOnline", false))
                add_button.setVisibility(View.GONE);
            else {
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
                            CharSequence[] mLecSecList = new CharSequence[lectureSections.size()];
                            for (int i = 0; i < lectureSections.size(); i++) {
                                mLecSecList[i] = lectureSections.get(i).getSection();
                            }

                            // show dialog to choose sections
                            AlertDialog ad = new AlertDialog.Builder(mActivity)
                                    .setTitle("Choose a section")
                                    .setSingleChoiceItems(mLecSecList, 0, null)
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
        TextView title = (TextView) mActivity.findViewById(R.id.title);
        if(courseName != null)
            courseName.setText(bundle.getString("courseName"));
        if(title != null)
            title.setText(bundle.getString("title"));

        mAsyncTaskCallbackInterface.onOperationComplete(bundle);

        /*TextView description = (TextView) mActivity.findViewById(R.id.description);
        TextView units = (TextView) mActivity.findViewById(R.id.units);
        TextView preReq = (TextView) mActivity.findViewById(R.id.preReq);
        TextView antiReq = (TextView) mActivity.findViewById(R.id.antiReq);
        TextView termsOffered = (TextView) mActivity.findViewById(R.id.termsOffered);
        TextView online = (TextView) mActivity.findViewById(R.id.online);

        courseName.setText(bundle.getString("courseName"));
        title.setText(bundle.getString("title"));
        description.setText(bundle.getString("description"));
        units.setText(bundle.getString("units"));
        preReq.setText(bundle.getString("preReq"));
        antiReq.setText(bundle.getString("antiReq"));
        termsOffered.setText(bundle.getString("termsOffered"));

        if (bundle.getBoolean("online")) {
            online.setText("Yes");
        } else {
            online.setText("no");
        }*/

    }


}
