package com.example.jason.myclass;

/**
 * Created by Danny on 2015/6/27.
 */
public class Constants {
    public static String UWAPIRoot = "https://api.uwaterloo.ca/v2/";
    public static String key = "93071c27c200c7b41e22f12902dabdef";

    public static String getScheduleURL(String input) {
        String subject = "";
        String cataNum = "";

        input = input.replaceAll("\\s+","");

        for (int i = 0; i < input.length(); i++) {
            if (Character.isDigit(input.charAt(i))) {
                cataNum += input.charAt(i);
            } else {
                subject += input.charAt(i);
            }
        }

        return UWAPIRoot + "courses/" + subject + "/" + cataNum + ".json?key=" + key;
    }

    public static String getSubjectsOffering() {
        return UWAPIRoot + "codes/subjects" + ".json?key=" + key;
    }


}
