package com.YC2010.jason.myclass.data.fetchtasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.YC2010.jason.myclass.callbacks.AsyncTaskCallbackInterface;
import com.YC2010.jason.myclass.data.Connections;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by Danny on 2015/6/27.
 */
public class CatalogNumFetchTask extends AsyncTask<String, Void, Bundle> {

    private Activity mActivity;
    AsyncTaskCallbackInterface mAsyncTaskCallbackInterface;
    private ArrayList<String> mCatalogNum_arraylist = new ArrayList<>();
    private String mSubject;
    public CatalogNumFetchTask(String subject, AsyncTaskCallbackInterface asyncTaskCallbackInterface) {
        mAsyncTaskCallbackInterface = asyncTaskCallbackInterface;
        mSubject = subject;
    }

    @Override
    protected Bundle doInBackground(String...params) {
        Bundle result = new Bundle();
        fetchCourse(result);

        return result;
    }

    private Bundle fetchCourse(Bundle bundle) {
        try{
            String url = Connections.getCatalogNumURL(mSubject);

            JSONObject jsonObject = Connections.getJSON_from_url(url);
            JSONArray subject_array = jsonObject.getJSONArray("data");
            Log.d("SubjectFetchTask", "subject array is " + subject_array.length());

            for(int i = 0; i < subject_array.length(); i++){
                JSONObject subject = subject_array.getJSONObject(i);
                mCatalogNum_arraylist.add(i,mSubject + " " +  subject.getString("catalog_number"));
            }

            bundle.putStringArrayList("CatalogNum_arraylist", mCatalogNum_arraylist);

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
