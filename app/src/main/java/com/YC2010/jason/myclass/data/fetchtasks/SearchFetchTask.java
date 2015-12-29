package com.YC2010.jason.myclass.data.fetchtasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.YC2010.jason.myclass.R;
import com.YC2010.jason.myclass.data.Connections;
import com.YC2010.jason.myclass.data.CoursesDBHandler;
import com.YC2010.jason.myclass.utils.Constants;
import com.YC2010.jason.myclass.callbacks.AsyncTaskCallbackInterface;
import com.YC2010.jason.myclass.model.FinalObject;
import com.YC2010.jason.myclass.model.LectureSectionObject;
import com.YC2010.jason.myclass.model.TestObject;
import com.YC2010.jason.myclass.model.TutorialObject;

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
            if (schedulesObject == null) {
                Log.d("SearchFetchTask", "WARNING - schedulesObject is NULL");
                return null;
            }

            if (!schedulesObject.getJSONObject("meta").getString("message").equals("Request successful")) {
                Log.d("SearchFetchTask", schedulesObject.toString());
                return null;
            }

            bundle.putBoolean("valid_return", true);
            JSONArray data = schedulesObject.getJSONArray("data");

            for(int i = 0; i < data.length(); i++){
                JSONObject section = data.getJSONObject(i);
                JSONObject first_class = section.getJSONArray("classes").getJSONObject(0);
                JSONObject date = first_class.getJSONObject("date");
                JSONObject loc = first_class.getJSONObject("location");

                String section_str = section.getString("section");
                String section_type = section_str.substring(0, 3);
                String sectionNumber = section_str.substring(section_str.length() - 3);

                boolean isOnline = sectionNumber.equals("081");

                Log.d("SearchFetchTask", "section_type is <" + section_type + ">");

                CoursesDBHandler db = new CoursesDBHandler(mActivity);

                switch (section_type) {
                    case "LEC":
                    case "STU":
                        JSONArray professors = first_class.getJSONArray("instructors");
                        String prof = "";

                        if (professors != null && professors.length() > 0) {
                            prof = professors.getString(0);
                        }

                        // check if is in db
                        if(db.IsInDB(section.getString("class_number"))){
                            bundle.putBoolean("course_taken", true);
                            bundle.putString("course_taken_num",section.getString("class_number"));
                        }

                        LectureSectionObject lecture = new LectureSectionObject();
                        lecture.setNumber(section.getString("class_number"));
                        lecture.setIsOnline(isOnline);

                        if (!isOnline) {
                            String weekdays = date.getString("weekdays");
                            String startTime = date.getString("start_time");
                            String endTime = date.getString("end_time");

                            if (!weekdays.equals("null") && !startTime.equals("null") && !endTime.equals("null")) {
                                lecture.setTime(date.getString("weekdays") + " " +
                                        date.getString("start_time") + "-" + date.getString("end_time"));
                            }

                            String building = loc.getString("building");
                            String room = loc.getString("room");

                            if (!building.equals("null") && !room.equals("null")) {
                                lecture.setLocation(building + " " + room);
                            } else {
                                lecture.setLocation("");
                            }
                        } else {
                            lecture.setTime(mActivity.getResources().getString(R.string.not_available));
                            lecture.setLocation("Online");
                        }

                        lecture.setProfessor(prof);
                        lecture.setSection(sectionNumber);
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

            bundle.putString("title", data.getJSONObject(0).getString("title"));
            bundle.putString("courseName", data.getJSONObject(0).getString("subject") + " " +
                    data.getJSONObject(0).getString("catalog_number"));
            bundle.putParcelableArrayList(Constants.lectureSectionObjectListKey, lectures);
            bundle.putParcelableArrayList(Constants.tutorialObjectListKey, tutorials);
            bundle.putParcelableArrayList(Constants.testObjectListKey, tests);

            String current_term = (String) Connections.getTerms().get(1);

            // get exam JSONBObject
            String exam_url = Connections.getExamsURL(current_term);
            JSONObject examObject = Connections.getJSON_from_url(exam_url);

            if (examObject != null) {
                JSONArray examData = examObject.getJSONArray("data");

                for (int i = 0; i < examData.length(); i++) {
                    if (examData.getJSONObject(i).getString("course").equals(bundle.getString("courseName"))) {
                        bundle.putBoolean("has_finals", true);
                        JSONArray exam_sections = examData.getJSONObject(i).getJSONArray("sections");
                        for (int j = 0; j < exam_sections.length(); j++) {
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
            }

            // Fetch course information (description, etc)
            JSONObject courseInfoObject = Connections.getJSON_from_url(Connections.getCourseInfoURL(input));
            JSONObject courseObject = courseInfoObject.getJSONObject("data");

            bundle.putString("description", courseObject.getString("description"));
            String prerequisites = courseObject.getString("prerequisites");
            String antirequisites = courseObject.getString("antirequisites");

            if (prerequisites != null && !prerequisites.equals("null")) {
                bundle.putString("prerequisites", prerequisites);
            }

            if (antirequisites != null && !antirequisites.equals("null")) {
                bundle.putString("antirequisites", antirequisites);
            }

            return bundle;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(final Bundle bundle) {
        progDailog.dismiss();

        mAsyncTaskCallbackInterface.onOperationComplete(bundle);
    }


}
