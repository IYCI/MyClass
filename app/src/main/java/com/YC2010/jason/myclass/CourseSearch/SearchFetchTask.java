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
            String schedule_url = Connections.getScheduleURL(input);
            JSONObject schedulesObject = Connections.getJSON_from_url(schedule_url);

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

            String current_term = (String) Connections.getTerms().get(1);

            // get exam JSONBObject
            String exam_url = Connections.getExamsURL(current_term);
            JSONArray examData = Connections.getJSON_from_url(exam_url).getJSONArray("data");
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
