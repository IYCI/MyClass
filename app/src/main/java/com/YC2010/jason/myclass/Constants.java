package com.YC2010.jason.myclass;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.YC2010.jason.myclass.Reminder.Reminder_item;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by Danny on 2015/6/27.
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class Constants {
    public static String UWAPIRoot = "https://api.uwaterloo.ca/v2/";
    public static String key = "93071c27c200c7b41e22f12902dabdef";
    public static List<Reminder_item> holiday_2015;
    public static List<Reminder_item> sample_reminder;

    public static String lectureSectionObjectListKey = "LEC_OBJECTS";
    public static String testObjectListKey = "TST_OBJECTS";
    public static String tutorialObjectListKey = "TUT_OBJECTS";
    public static String finalObjectListKey = "FINAL_OBJECTS";


    // /courses/{subject}/{catalog_number}
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


    // /courses/{subject}/{catalog_number}/schedule
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

    public static String getCurrentTerm() {
        String term;
        String termList_url = UWAPIRoot + "terms/list.json?key=" + key;
        JSONObject termList = getJSON_from_url(termList_url);
        try {
            term = termList.getJSONObject("data").getString("current_term");
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return term;
    }


    /*      return a list     */
    /* index 0: previous term */
    /* index 1: current term  */
    /* index 2: next term     */
    public static ArrayList getTerms() {
        ArrayList<String> term_list = new ArrayList<>();
        String termList_url = UWAPIRoot + "terms/list.json?key=" + key;
        try {
            JSONObject termData = getJSON_from_url(termList_url).getJSONObject("data");
            term_list.add(0, termData.getString("previous_term"));
            term_list.add(1, termData.getString("current_term"));
            term_list.add(2,termData.getString("next_term"));
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return term_list;
    }

    public static String getTermName(String term){
        String termList_url = UWAPIRoot + "terms/list.json?key=" + key;
        try {
            JSONObject terms_listing = getJSON_from_url(termList_url).getJSONObject("data").getJSONObject("listings");
            Iterator i = terms_listing.keys();
            while(i.hasNext()){
                String key = (String) i.next();
                JSONArray year = (JSONArray) terms_listing.get(key);
                for(int j = 0; j < year.length(); j++){
                    if (year.getJSONObject(j).getString("id").equals(term)) {
                        Log.d("Constants", year.getJSONObject(j).getString("name"));
                        return year.getJSONObject(j).getString("name");
                    }
                }
            }
            return null;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
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

    public static JSONObject getJSON_from_url(String url){
        DefaultHttpClient httpClient = new DefaultHttpClient();
        JSONObject jsonObject;

        try {
            Log.d("getJSONObject", "URL is " + url);
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

    public static List<Reminder_item> get_sample_reminder(){
        sample_reminder = new ArrayList<>();
        Reminder_item tmp = new Reminder_item(UUID.randomUUID().toString(),
                "Final end","", 2015, 7, 15, "d");
        sample_reminder.add(tmp);
        tmp = new Reminder_item(UUID.randomUUID().toString(),
                "Next term start","", 2015, 8, 14, "d");
        sample_reminder.add(tmp);
        tmp = new Reminder_item(UUID.randomUUID().toString(),
                "First penalty end","", 2015, 9, 3, "d");
        sample_reminder.add(tmp);
        tmp = new Reminder_item(UUID.randomUUID().toString(),
                "Late fee for fall","", 2015, 7, 28, "d");
        sample_reminder.add(tmp);

        return sample_reminder;
    }

    public static List<Reminder_item> get_holidays(){
        holiday_2015 = new ArrayList<>();
        Reminder_item tmp = new Reminder_item(UUID.randomUUID().toString(),
                "Civic Holiday","", 2015, 7, 3, "d");
        holiday_2015.add(tmp);
        tmp = new Reminder_item(UUID.randomUUID().toString(),
                "Labour Day","", 2015, 8, 7, "d");
        holiday_2015.add(tmp);
        tmp = new Reminder_item(UUID.randomUUID().toString(),
                "Thanksgiving","", 2015, 9, 12, "d");
        holiday_2015.add(tmp);
        tmp = new Reminder_item(UUID.randomUUID().toString(),
                "Christmas","", 2015, 11, 25, "d");
        holiday_2015.add(tmp);

        tmp = new Reminder_item(UUID.randomUUID().toString(),
                "Boxing Day","", 2015, 11, 28, "d");
        holiday_2015.add(tmp);

        return holiday_2015;


    }
}
