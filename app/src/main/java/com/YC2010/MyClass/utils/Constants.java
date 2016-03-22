package com.YC2010.MyClass.utils;

import com.YC2010.MyClass.model.Reminder_item;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

/**
 * Created by Danny on 2015/6/27.
 */

public class Constants {
    public static String UWAPIROOT = "https://api.uwaterloo.ca/v2/";
    public static List<Reminder_item> holiday_2015;
    public static List<Reminder_item> sample_reminder;

    public static String lectureSectionObjectListKey = "LEC_OBJECTS";
    public static String testObjectListKey = "TST_OBJECTS";
    public static String tutorialObjectListKey = "TUT_OBJECTS";
    public static String finalObjectListKey = "FINAL_OBJECTS";

    // default term value
    public static Integer defaultCurTermNum = 1161;
    public static String defaultCurTermName = "Winter 2016 (Current)";
    public static Integer defaultNextTermNum = 1165;
    public static String defaultNextTermName = "Spring 2016 (Next)";

    // temporarily hardcoded
    // Note: month off by one because of unix time standard
    public static GregorianCalendar nextTermStartDate = new GregorianCalendar(2016, 4, 2);



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
