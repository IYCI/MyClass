package com.YC2010.MyClass.data;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import com.YC2010.MyClass.utils.Constants;
import com.YC2010.MyClass.utils.Tools;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
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

        return Constants.UWAPIROOT + "courses/" + subject + "/" + cataNum + ".json" + URLEnding();
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
        return Constants.UWAPIROOT + "courses/" + subject + "/" + cataNum + "/schedule.json" + URLEnding();
    }

    // /terms/{term}/{subject}/{catalog_number}/schedule
    public static String getScheduleURL(String input, Integer term) {
        String subject = "";
        String cataNum = "";
        boolean isCoursePrefix = true;
        input = input.replaceAll("\\s+","");

        for (int i = 0; i < input.length(); i++) {
            if (isCoursePrefix && Character.isDigit(input.charAt(i)))
                isCoursePrefix = false;
            if (isCoursePrefix){
                subject += input.charAt(i);
            }
            else {
                cataNum += input.charAt(i);
            }
        }
        return Constants.UWAPIROOT + "terms/" + term + "/" + subject + "/" + cataNum + "/schedule.json" + URLEnding();
    }

    public static Integer getCurrentTerm() throws Exception {
        String term;
        String termList_url = Constants.UWAPIROOT + "terms/list.json" + URLEnding();
        JSONObject termList = getJSON_from_url(termList_url);
        try {
            term = termList.getJSONObject("data").getString("current_term");
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return Integer.parseInt(term);
    }


    /*      return a list of term num and a list of term name     */
    /* index 0: previous term */
    /* index 1: current term  */
    /* index 2: next term     */
    public static Bundle getTerms() {
        Bundle mbundle = new Bundle();
        ArrayList<Integer> termNumList = new ArrayList<>();
        ArrayList<String> termNameList = new ArrayList<>();
        String termList_url = Constants.UWAPIROOT + "terms/list.json" + URLEnding();
        try {
            JSONObject termData = getJSON_from_url(termList_url).getJSONObject("data");
            termNumList.add(0, Integer.parseInt(termData.getString("previous_term")));
            termNumList.add(1, Integer.parseInt(termData.getString("current_term")));
            termNumList.add(2,Integer.parseInt(termData.getString("next_term")));

            JSONObject termYear = termData.getJSONObject("listings");
            Iterator<?> years = termYear.keys();
            JSONArray termTermList;
            HashMap<Integer, String> termMap = new HashMap<>();
            while (years.hasNext()){
                String key = (String) years.next();
                termTermList = termYear.getJSONArray(key);
                for (int j = 0; j < termTermList.length(); j++){
                    termMap.put(termTermList.getJSONObject(j).getInt("id"), termTermList.getJSONObject(j).getString("name"));
                }
            }

            for (int i = 0; i < termNumList.size(); i++){
                termNameList.add(i, termMap.get(termNumList.get(i)));
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }

        mbundle.putIntegerArrayList("KEY_TERM_NUM", termNumList);
        mbundle.putStringArrayList("KEY_TERM_NAME", termNameList);
        return mbundle;
    }

    public static String getTermName(String term){
        String termList_url = Constants.UWAPIROOT + "terms/list.json" + URLEnding();
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

    public static String getExamsURL(int term) {
        return Constants.UWAPIROOT + "terms/" + term + "/examschedule" + ".json" + URLEnding();
    }

    public static String getSubjectsOfferingURL() {
        return Constants.UWAPIROOT + "codes/subjects" + ".json" + URLEnding();
    }

    public static String getCatalogNumURL(String subject) {
        return Constants.UWAPIROOT + "courses/" + subject + ".json" + URLEnding();
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

    public static String URLEnding() {
        return "?key=" + Tools.YOUNEEDTHIS;
    }
}
