package com.example.jason.myclass;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

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
public class Constants {
    public static String UWAPIRoot = "https://api.uwaterloo.ca/v2/";
    public static String key = "93071c27c200c7b41e22f12902dabdef";

    public static String getCourseInfoURL(String input) {
        String subject = "";
        String cataNum = "";
        boolean isCoursePrefix = true;

        input = input.replaceAll("\\s+","");

        for (int i = 0; i < input.length(); i++) {
            /*if (Character.isDigit(input.charAt(i))) {
                cataNum += input.charAt(i);
            } else {
                subject += input.charAt(i);
            }*/
            if(isCoursePrefix && Character.isDigit(input.charAt(i)))
                isCoursePrefix = false;
            if(isCoursePrefix){
                subject += input.charAt(i);
            }
            else{
                cataNum += input.charAt(i);
            }
        }

        return UWAPIRoot + "courses/" + subject + "/" + cataNum + ".json?key=" + key;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String getScheduleURL(String input) {
        String subject = "";
        String cataNum = "";
        boolean isCoursePrefix = true;
        input = input.replaceAll("\\s+","");

        for (int i = 0; i < input.length(); i++) {
            if(isCoursePrefix && Character.isDigit(input.charAt(i)))
                isCoursePrefix = false;
            if(isCoursePrefix){
                subject += input.charAt(i);
            }
            else{
                cataNum += input.charAt(i);
            }
        }


        return UWAPIRoot + "courses/" + subject + "/" + cataNum + "/schedule.json?key=" + key;
    }

    public static String getExamsURL(String term) {
        return UWAPIRoot + "terms/" + term + "/examschedule" + ".json?key=" + key;
    }

    public static String getSubjectsOfferingURL() {
        return UWAPIRoot + "codes/subjects" + ".json?key=" + key;
    }

    public static String getCatalogNumURL(String subject) {
        return UWAPIRoot + "courses/" + subject + ".json?key=" + key;
    }

    public static JSONObject getJSONObject(String url){
        DefaultHttpClient httpClient = new DefaultHttpClient();
        JSONObject jsonObject;

        try {
            Log.d("SearchFetchTask", "URL is " + url);
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

            jsonObject = new JSONObject(entityStringBuilder.toString());


        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return jsonObject;
    }
}
