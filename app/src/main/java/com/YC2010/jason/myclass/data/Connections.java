package com.YC2010.jason.myclass.data;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.YC2010.jason.myclass.utils.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Danny on 2015/10/25.
 */
public class Connections {

    // /courses/{subject}/{catalog_number}

    public static String getCourseInfoURL(String input) {
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

        return Constants.UWAPIROOT + "courses/" + subject + "/" + cataNum + ".json?key=" + Constants.APIKEY;
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


        return Constants.UWAPIROOT + "courses/" + subject + "/" + cataNum + "/schedule.json?key=" + Constants.APIKEY;
    }

    public static String getCurrentTerm() throws Exception {
        String term;
        String termList_url = Constants.UWAPIROOT + "terms/list.json?key=" + Constants.APIKEY;
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
        String termList_url = Constants.UWAPIROOT + "terms/list.json?key=" + Constants.APIKEY;
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
        String termList_url = Constants.UWAPIROOT + "terms/list.json?key=" + Constants.APIKEY;
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
        return Constants.UWAPIROOT + "terms/" + term + "/examschedule" + ".json?key=" + Constants.APIKEY;
    }

    public static String getSubjectsOfferingURL() {
        return Constants.UWAPIROOT + "codes/subjects" + ".json?key=" + Constants.APIKEY;
    }

    public static String getCatalogNumURL(String subject) {
        return Constants.UWAPIROOT + "courses/" + subject + ".json?key=" + Constants.APIKEY;
    }

    public static JSONObject getJSON_from_url(String url) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        JSONObject jsonObject;

        try {
            Log.d("getJSONObject", "URL is " + url);

            int responseCode = connection.getResponseCode();

            if (responseCode != 200) {
                throw new RuntimeException("Failed: HTTP error code: " + responseCode);
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));

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
