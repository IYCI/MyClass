package com.YC2010.jason.myclass.Reminder;

import java.util.GregorianCalendar;

/**
 * Created by Jason on 2015-06-02.
 */
public class Reminder_item {
    // variables
    String id;
    String title;
    String location;
    GregorianCalendar mClaendar;
    String type;

    // type:
    // "c"      customized
    // "e"      exams
    // "fb"     facebook events
    // "d"      important dates

    // constructor
    public Reminder_item(){
        // empty
    }

    // Ctor for all
    public Reminder_item(String id, String title, String location, int year, int month, int day, int hour, int minute, String type){
        this.id = id;
        this.title = title;
        this.location = location;
        this.mClaendar = new GregorianCalendar(year,month,day,hour,minute);
        this.type = type;
    }

    // ctor for no hout and minute
    public Reminder_item(String id, String title, String location, int year, int month, int day, String type){
        this.id = id;
        this.title = title;
        this.location = location;
        this.mClaendar = new GregorianCalendar(year,month,day);
        this.type = type;
    }

    //Ctor for unix time
    public Reminder_item(String id, String title, String location, long unix_time, String type){
        this.id = id;
        this.title = title;
        this.location = location;
        this.mClaendar = new GregorianCalendar();
        this.mClaendar.setTimeInMillis(unix_time);
        this.type = type;
    }

    public String get_id(){
        return this.id;
    }

    public String get_title(){
        return this.title;
    }

    public String get_location(){
        return this.location;
    }

    public long get_unix_time(){
        return this.mClaendar.getTimeInMillis();
    }

    public String get_type(){ return this.type;}
}


