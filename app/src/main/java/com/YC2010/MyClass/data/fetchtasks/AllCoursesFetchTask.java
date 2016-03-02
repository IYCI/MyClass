package com.YC2010.MyClass.data.fetchtasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.YC2010.MyClass.callbacks.AsyncTaskCallbackInterface;
import com.YC2010.MyClass.data.Connections;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Danny on 2015/6/27.
 */
public class AllCoursesFetchTask extends AsyncTask<String, Void, Bundle> {

    private Activity mActivity;
    AsyncTaskCallbackInterface mAsyncTaskCallbackInterface;
    public ArrayList<String> subject_arraylist = new ArrayList<>();
    public ArrayList<String> title_arraylist = new ArrayList<>();

    public AllCoursesFetchTask(AsyncTaskCallbackInterface asyncTaskCallbackInterface) {
        mAsyncTaskCallbackInterface = asyncTaskCallbackInterface;
    }

    @Override
    protected Bundle doInBackground(String...params) {
        Bundle result = new Bundle();

        fetchCourse(result);

        return result;
    }

    private Bundle fetchCourse(Bundle bundle) {
        try{
            String current_term = (String) Connections.getTerms().get(1);
            String url = Connections.getCoursesURL(current_term);

            JSONObject jsonObject = Connections.getJSON_from_url(url);
            JSONArray subject_array = jsonObject.getJSONArray("data");
            Log.d("AllCoursesFetchTask", "subject array is " + subject_array.length());

            for(int i = 0; i < subject_array.length(); i++){
                JSONObject subject = subject_array.getJSONObject(i);
                subject_arraylist.add(i,subject.getString("subject") + subject.getString("catalog_number"));
                title_arraylist.add(i, subject.getString("title"));
                //Log.d("SubjectFetchTask", "miao");
            }

            bundle.putStringArrayList("subject_arraylist", subject_arraylist);

            return bundle;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bundle;
    }

    @Override
    protected void onPostExecute(Bundle bundle) {
        mAsyncTaskCallbackInterface.onOperationComplete(bundle);
    }


}
