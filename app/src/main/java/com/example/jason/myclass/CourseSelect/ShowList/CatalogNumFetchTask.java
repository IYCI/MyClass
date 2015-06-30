package com.example.jason.myclass.CourseSelect.ShowList;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.jason.myclass.Constants;

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
        DefaultHttpClient httpClient = new DefaultHttpClient();


        try{
            String url = Constants.getCatalogNum(mSubject);
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
            JSONArray subject_array = jsonObject.getJSONArray("data");
            Log.d("SubjectFetchTask", "subject array is " + subject_array.length());



            for(int i = 0; i < subject_array.length(); i++){
                JSONObject subject = subject_array.getJSONObject(i);
                mCatalogNum_arraylist.add(i,mSubject + " " +  subject.getString("catalog_number"));
                //Log.d("SubjectFetchTask", "miao");
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
