package com.YC2010.jason.myclass.data.fetchtasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.YC2010.jason.myclass.data.ReminderDBHandler;
import com.YC2010.jason.myclass.data.Connections;
import com.YC2010.jason.myclass.callbacks.AsyncTaskCallbackInterface;
import com.YC2010.jason.myclass.model.Reminder_item;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Jason on 2015/6/27.
 */
public class FinalsFetchTask extends AsyncTask<List<String>, Void, Bundle> {

    private Activity mActivity;
    private AsyncTaskCallbackInterface mAsyncTaskCallbackInterface;

    public FinalsFetchTask(Activity activity, AsyncTaskCallbackInterface asyncTaskCallbackInterface) {
        this.mActivity = activity;
        mAsyncTaskCallbackInterface = asyncTaskCallbackInterface;
    }
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
    protected Bundle doInBackground(List<String>...params) {
        Bundle result = new Bundle();

        fetchExam(result, params[0], params[1]);

        return result;
    }

    private Bundle fetchExam(Bundle bundle, List<String> course_names, List<String> course_secs) {
        try{
            bundle.putBoolean("valid_return", false);
            // get schedule JSONBObject
            // TODO: what if no course exist???
            String schedule_url = Connections.getScheduleURL(course_names.get(0));
            JSONObject schedulesObject = Connections.getJSON_from_url(schedule_url);

            // check valid data return
            if(!schedulesObject.getJSONObject("meta").getString("message").equals("Request successful")) {
                Log.d("FinalsFetchTask",schedulesObject.toString());
                return bundle;
            }
            bundle.putBoolean("valid_return", true);

            String current_term = (String) Connections.getTerms().get(1);
            // get exam JSONBObject
            String exam_url = Connections.getExamsURL(current_term);
            JSONArray examData = Connections.getJSON_from_url(exam_url).getJSONArray("data");

            // get final info from courses
            ReminderDBHandler reminder_db = new ReminderDBHandler(mActivity);
            reminder_db.removeAll("e");
            for (int i = 0; i < course_names.size(); i++) {
                String course_name = course_names.get(i);
                String course_sec = course_secs.get(i);

                Log.d("FinalsFetchTask",course_name + course_sec);

                // check if has final and get index for finals
                boolean has_final = false;
                int exam_index = 0;
                int section_index = 0;
                for (; exam_index < examData.length(); exam_index++) {
                    if (examData.getJSONObject(exam_index).getString("course").equals(course_name)) {
                        JSONArray section_array = examData.getJSONObject(exam_index).getJSONArray("sections");
                        int length = section_array.length();
                        for(; section_index < section_array.length(); section_index++){
                            Log.d("FinalsFetchTask",section_array.getJSONObject(section_index).getString("section"));
                            if(section_array.getJSONObject(section_index).getString("section").equals(course_sec)){
                                Log.d("FinalsFetchTask","section_index is " + section_index);
                                has_final = true;
                                break;
                            }
                        }
                        break;
                    }
                }
                if(has_final){
                    JSONObject final_schedule = examData.getJSONObject(exam_index).getJSONArray("sections").getJSONObject(section_index);
                    String loc = "SEC " + course_sec + "    " + final_schedule.getString("location");
                    String title = course_name + "  Final exam";
                    String start_time = final_schedule.getString("date") + " " + final_schedule.getString("start_time");
                    DateFormat f1 = new SimpleDateFormat("yyyy-MM-dd h:mm a");
                    Date d = f1.parse(start_time);

                    Reminder_item new_exam = new Reminder_item(UUID.randomUUID().toString(), title, loc, d.getTime(), "e");

                    reminder_db.addReminder(new_exam);
                    reminder_db.close();
                }
            }
            //MainActivity mainActivity = (MainActivity) getActivity();


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


    }


}
