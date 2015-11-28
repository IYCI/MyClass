package com.YC2010.jason.myclass.data.FetchTasks;

import android.os.AsyncTask;
import android.util.Log;

import com.YC2010.jason.myclass.data.Connections;
import com.YC2010.jason.myclass.utils.Constants;

import org.json.JSONObject;

/**
 * Created by Danny on 2015/11/27.
 */
public class APIKeyFetchTask extends AsyncTask<Void, Void, String> {

    @Override
    protected String doInBackground(Void... unused) {
        JSONObject resultObject = Connections.getJSON_from_url(Constants.APIKEY_SERVER);
        String API_KEY = null;

        if (resultObject != null) {
            try {
                int status = resultObject.getInt("status");
                if (status == 200) {
                    API_KEY = resultObject.getString("key");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return API_KEY;
    }

    @Override
    protected void onPostExecute(String key) {
        if (key == null) {
            Log.d("API Key", "WARNING: API key is NULL");
        }

        Connections.initialize(key);
    }
}
