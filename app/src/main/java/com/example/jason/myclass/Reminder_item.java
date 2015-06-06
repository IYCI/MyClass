package com.example.jason.myclass;

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

    // constructor
    public Reminder_item(){
        // empty
    }

    // Ctor for all
    public Reminder_item(String id, String title, String location, int year, int month, int day, int hour, int minute){
        this.title = title;
        this.location = location;
        this.mClaendar = new GregorianCalendar(year,month,day,hour,minute);

    }

    // ctor for no hout and minute
    public Reminder_item(String id, String title, String location, int year, int month, int day){
        this.title = title;
        this.location = location;
        this.mClaendar = new GregorianCalendar(year,month,day);
    }

    //Ctor for unix time
    public Reminder_item(String id, String title, String location, long unix_time){
        this.title = title;
        this.location = location;
        this.mClaendar = new GregorianCalendar();
        this.mClaendar.setTimeInMillis(unix_time);
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
        return this.mClaendar.getTime().getTime();
    }
}
