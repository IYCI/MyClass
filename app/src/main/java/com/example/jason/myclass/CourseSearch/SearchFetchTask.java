package com.example.jason.myclass.CourseSearch;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.jason.myclass.Constants;
import com.example.jason.myclass.CourseSelect.ShowList.AsyncTaskCallbackInterface;
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
    private ArrayList<String> LEC_SEC = new ArrayList<>();
    private ArrayList<String> LEC_TIME = new ArrayList<>();
    private ArrayList<String> LEC_PROF = new ArrayList<>();
    private ArrayList<String> LEC_LOC = new ArrayList<>();
    private ArrayList<String> LEC_CAPACITY = new ArrayList<>();
    private ArrayList<String> LEC_TOTAL = new ArrayList<>();

    // for TUT
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

    @Override
    protected Bundle doInBackground(String...params) {
        Bundle result = new Bundle();

        fetchCourse(result, params[0]);

        return result;
    }

    private Bundle fetchCourse(Bundle bundle, String input) {
        DefaultHttpClient httpClient = new DefaultHttpClient();


        try{
            String url = Constants.getScheduleURL(input);
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
                bundle.putBoolean("valid_return", false);
                return bundle;
            }
            JSONArray sections_array = jsonObject.getJSONArray("data");
            //Log.d("SearchFetchTask",sections_array.toString());
            for(int i = 0; i < sections_array.length(); i++){
                JSONObject section = sections_array.getJSONObject(i);
                JSONObject first_class = section.getJSONArray("classes").getJSONObject(0);
                //Log.d("SearchFetchTask",first_class.toString());
                JSONObject date = first_class.getJSONObject("date");
                JSONObject loc = first_class.getJSONObject("location");


                String section_str = section.getString("section");
                String section_type = section_str.substring(0,3);
                Log.d("SearchFetchTask", "section_type is <" + section_type + ">");
                if(section_type.equals("LEC")){
                    String prof = first_class.getJSONArray("instructors").getString(0);
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
            bundle.putString("title", sections_array.getJSONObject(0).getString("title"));
            bundle.putString("courseName", input);
            bundle.putStringArrayList("LEC_SEC", LEC_SEC);
            bundle.putStringArrayList("LEC_TIME", LEC_TIME);
            bundle.putStringArrayList("LEC_PROF", LEC_PROF);
            bundle.putStringArrayList("LEC_LOC", LEC_LOC);
            bundle.putStringArrayList("LEC_CAPACITY", LEC_CAPACITY);
            bundle.putStringArrayList("LEC_TOTAL", LEC_TOTAL);

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
    protected void onPostExecute(Bundle bundle) {
        TextView courseName = (TextView) mActivity.findViewById(R.id.course_name);
        if(!bundle.getBoolean("valid_return", true)){
            courseName.setText("Not available this term");
            return;
        }

        TextView lec = (TextView) mActivity.findViewById(R.id.lec);
        lec.setVisibility(View.VISIBLE);

        if(bundle.getBoolean("has_tst", false)) {
            TextView tst = (TextView) mActivity.findViewById(R.id.tst);
            tst.setVisibility(View.VISIBLE);
        }

        if(bundle.getBoolean("has_tut", false)) {
            TextView tut = (TextView) mActivity.findViewById(R.id.tut);
            tut.setVisibility(View.VISIBLE);
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
