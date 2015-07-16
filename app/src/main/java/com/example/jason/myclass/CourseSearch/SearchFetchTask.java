package com.example.jason.myclass.CourseSearch;

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

import com.example.jason.myclass.Constants;
import com.example.jason.myclass.CourseSelect.ShowList.AsyncTaskCallbackInterface;
import com.example.jason.myclass.Courses.CourseInfo;
import com.example.jason.myclass.Courses.CoursesDBHandler;
import com.example.jason.myclass.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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

    // for LEC
    private ArrayList<String> LEC_NUM = new ArrayList<>();
    private ArrayList<String> LEC_SEC = new ArrayList<>();
    private ArrayList<String> LEC_TIME = new ArrayList<>();
    private ArrayList<String> LEC_PROF = new ArrayList<>();
    private ArrayList<String> LEC_LOC = new ArrayList<>();
    private ArrayList<String> LEC_CAPACITY = new ArrayList<>();
    private ArrayList<String> LEC_TOTAL = new ArrayList<>();

    // for TUT
    private ArrayList<String> TUT_NUM = new ArrayList<>();
    private ArrayList<String> TUT_SEC = new ArrayList<>();
    private ArrayList<String> TUT_TIME = new ArrayList<>();
    private ArrayList<String> TUT_LOC = new ArrayList<>();
    private ArrayList<String> TUT_CAPACITY = new ArrayList<>();
    private ArrayList<String> TUT_TOTAL = new ArrayList<>();

    // for TST
    private ArrayList<String> TST_SEC = new ArrayList<>();
    private ArrayList<String> TST_TIME = new ArrayList<>();
    private ArrayList<String> TST_CAPACITY = new ArrayList<>();
    private ArrayList<String> TST_TOTAL = new ArrayList<>();

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
        DefaultHttpClient httpClient = new DefaultHttpClient();


        try{
            bundle.putBoolean("valid_return", false);
            String url = Constants.getScheduleURL(input);
            Log.d("SearchFetchTask","URL is " + url);
            HttpGet httpGet = new HttpGet(url);

            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();

            if (null == httpEntity) return null;

            if (httpResponse.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed: HTTP error code: " + httpResponse.getStatusLine().getStatusCode());
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(httpEntity.getContent(), "UTF-8"));
    
            String inputLine;
            StringBuilder entityStringBuilder = new StringBuilder();

            while (null != (inputLine = in.readLine())) {
                entityStringBuilder.append(inputLine).append("\n");
            }
            in.close();

            JSONObject jsonObject = new JSONObject(entityStringBuilder.toString());

            // check valid data return
            if(!jsonObject.getJSONObject("meta").getString("message").equals("Request successful")) {
                Log.d("SearchFetchTask",jsonObject.toString());
                return bundle;
            }
            bundle.putBoolean("valid_return", true);
            JSONArray data = jsonObject.getJSONArray("data");
            //Log.d("SearchFetchTask",sections_array.toString());
            for(int i = 0; i < data.length(); i++){
                JSONObject section = data.getJSONObject(i);
                JSONObject first_class = section.getJSONArray("classes").getJSONObject(0);
                //Log.d("SearchFetchTask",first_class.toString());
                JSONObject date = first_class.getJSONObject("date");
                JSONObject loc = first_class.getJSONObject("location");


                String section_str = section.getString("section");
                String section_type = section_str.substring(0,3);
                Log.d("SearchFetchTask", "section_type is <" + section_type + ">");
                CoursesDBHandler db = new CoursesDBHandler(mActivity);
                if(section_type.equals("LEC")){
                    String prof = first_class.getJSONArray("instructors").getString(0);

                    // check if is in db
                    if(db.IsInDB(section.getString("class_number"))){
                        bundle.putBoolean("course_taken", true);
                        bundle.putString("course_taken_num",section.getString("class_number"));
                    }
                    LEC_NUM.add(section.getString("class_number"));
                    LEC_SEC.add(section_str.substring(4));
                    LEC_TIME.add(date.getString("weekdays") + " " +
                            date.getString("start_time") + "-" + date.getString("end_time"));
                    LEC_PROF.add(prof);
                    LEC_LOC.add(loc.getString("building") + " " + loc.getString("room"));
                    LEC_CAPACITY.add(section.getString("enrollment_capacity"));
                    LEC_TOTAL.add(section.getString("enrollment_total"));
                }
                else if(section_type.equals("TST")){
                    bundle.putBoolean("has_tst", true);
                    TST_SEC.add(section_str.substring(4));
                    TST_TIME.add(date.getString("start_date") + " " +
                            date.getString("start_time") + "-" + date.getString("end_time"));
                    TST_CAPACITY.add(section.getString("enrollment_capacity"));
                    TST_TOTAL.add(section.getString("enrollment_total"));
                }
                else if(section_type.equals("TUT")){
                    bundle.putBoolean("has_tut", true);
                    TUT_NUM.add(section.getString("class_number"));
                    TUT_SEC.add(section_str.substring(4));
                    TUT_TIME.add(date.getString("weekdays") + " " +
                                    date.getString("start_time") + "-" + date.getString("end_time"));
                    TUT_LOC.add(loc.getString("building") + " " + loc.getString("room"));
                    TUT_CAPACITY.add(section.getString("enrollment_capacity"));
                    TUT_TOTAL.add(section.getString("enrollment_total"));
                }
                else if(section_type.equals("LAB")){
                    bundle.putBoolean("has_lab", true);
                }
                //Log.d("SubjectFetchTask", "miao");
            }


            //String title = data.getString("title");
            bundle.putString("title", data.getJSONObject(0).getString("title"));
            bundle.putString("courseName", data.getJSONObject(0).getString("subject") + " " +
                                             data.getJSONObject(0).getString("catalog_number"));
            bundle.putStringArrayList("LEC_NUM", LEC_NUM);
            bundle.putStringArrayList("LEC_SEC", LEC_SEC);
            bundle.putStringArrayList("LEC_TIME", LEC_TIME);
            bundle.putStringArrayList("LEC_PROF", LEC_PROF);
            bundle.putStringArrayList("LEC_LOC", LEC_LOC);
            bundle.putStringArrayList("LEC_CAPACITY", LEC_CAPACITY);
            bundle.putStringArrayList("LEC_TOTAL", LEC_TOTAL);

            bundle.putStringArrayList("TUT_NUM", TUT_NUM);
            bundle.putStringArrayList("TUT_SEC", TUT_SEC);
            bundle.putStringArrayList("TUT_TIME", TUT_TIME);
            bundle.putStringArrayList("TUT_LOC", TUT_LOC);
            bundle.putStringArrayList("TUT_CAPACITY", TUT_CAPACITY);
            bundle.putStringArrayList("TUT_TOTAL", TUT_TOTAL);

            bundle.putStringArrayList("TST_SEC", TST_SEC);
            bundle.putStringArrayList("TST_TIME", TST_TIME);
            bundle.putStringArrayList("TST_CAPACITY", TST_CAPACITY);
            bundle.putStringArrayList("TST_TOTAL", TST_TOTAL);


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

        TextView lec = (TextView) mActivity.findViewById(R.id.lec);
        if(lec != null)
            lec.setVisibility(View.VISIBLE);

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

        Button add_button = (Button) mActivity.findViewById(R.id.add_course_button);

        if (add_button != null) {
            if(bundle.getBoolean("course_taken")) {
                add_button.setText("drop");
                add_button.setBackgroundTintList(ColorStateList.valueOf(mActivity.getResources().getColor(R.color.fab_color_1)));
            }
            add_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    Button add_button = (Button) mActivity.findViewById(R.id.add_course_button);
                    final CoursesDBHandler db = new CoursesDBHandler(mActivity);

                    String course_taken_num = null;
                    boolean courseTaken = false;
                    for(int i = 0; i < bundle.getStringArrayList("LEC_NUM").size(); i++){
                        if(db.IsInDB(bundle.getStringArrayList("LEC_NUM").get(i))){
                            courseTaken = true;
                            course_taken_num = bundle.getStringArrayList("LEC_NUM").get(i);
                            break;
                        }
                    }

                    if(!courseTaken){
                       // put sec info into a list of string
                        CharSequence[] mLecSecList = new CharSequence[bundle.getStringArrayList("LEC_SEC").size()];
                        for(int i = 0; i < bundle.getStringArrayList("LEC_SEC").size(); i++){
                            mLecSecList[i] = bundle.getStringArrayList("LEC_SEC").get(i);
                        }

                        // show dialog to choose sections
                        AlertDialog ad = new AlertDialog.Builder(mActivity)
                                .setTitle("Choose a section")
                                .setSingleChoiceItems(mLecSecList, 0, null)
                                .setNegativeButton(R.string.cancel, null)
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        ListView lw = ((AlertDialog) dialog).getListView();
                                        Object checkedItem = lw.getAdapter().getItem(lw.getCheckedItemPosition());
                                        int index = bundle.getStringArrayList("LEC_SEC").indexOf(checkedItem.toString());
                                        Log.d("SearchFetchTask", "choose " + checkedItem.toString());
                                        // add to db
                                        CourseInfo mCourse = new CourseInfo(bundle.getString("courseName"));
                                        mCourse.setSec(checkedItem.toString());
                                        mCourse.setNum(bundle.getStringArrayList("LEC_NUM").get(index));
                                        mCourse.setLoc(bundle.getStringArrayList("LEC_LOC").get(index));
                                        mCourse.setTimeAPM(bundle.getStringArrayList("LEC_TIME").get(index));
                                        mCourse.setProf(bundle.getStringArrayList("LEC_PROF").get(index));


                                        db.addCourse(mCourse);


                                        // change button
                                        Button add_button = (Button) mActivity.findViewById(R.id.add_course_button);
                                        add_button.setText("drop");
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
                    else{
                        // remove from db
                        if(course_taken_num != null)
                            db.removeCourse(course_taken_num);

                        add_button.setText("take");
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
