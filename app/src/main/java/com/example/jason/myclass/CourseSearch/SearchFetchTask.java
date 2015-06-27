package com.example.jason.myclass.CourseSearch;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.example.jason.myclass.Constants;
import com.example.jason.myclass.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by Danny on 2015/6/27.
 */
public class SearchFetchTask extends AsyncTask<String, Void, Bundle> {

    private Activity mActivity;

    public SearchFetchTask(Activity activity) {
        this.mActivity = activity;
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
            JSONObject data = jsonObject.getJSONObject("data");

            String title = data.getString("title");
            String description = data.getString("description");
            String units = data.getString("units");
            String preReq = data.getString("prerequisites");
            String antiReq = data.getString("antirequisites");
            String termsOffered = data.getJSONArray("terms_offered").toString().replaceAll("\\p{P}", "");
            boolean online = data.getJSONObject("offerings").getBoolean("online");

            bundle.putString("title", title);
            bundle.putString("description", description);
            bundle.putString("units", units);
            bundle.putString("preReq", preReq);
            bundle.putString("antiReq", antiReq);
            bundle.putString("termsOffered", termsOffered);
            bundle.putBoolean("online", online);

            return bundle;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bundle;
    }

    @Override
    protected void onPostExecute(Bundle bundle) {
        TextView title = (TextView) mActivity.findViewById(R.id.title);
        TextView description = (TextView) mActivity.findViewById(R.id.description);
        TextView units = (TextView) mActivity.findViewById(R.id.units);
        TextView preReq = (TextView) mActivity.findViewById(R.id.preReq);
        TextView antiReq = (TextView) mActivity.findViewById(R.id.antiReq);
        TextView termsOffered = (TextView) mActivity.findViewById(R.id.termsOffered);
        TextView online = (TextView) mActivity.findViewById(R.id.online);

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
        }
    }
}
