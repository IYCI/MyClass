package com.YC2010.jason.myclass.Courses;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Lincoln on 2015-06-02.
 */
public class CourseInfo {
    protected String courseName;
    protected String courseNum;
    protected String courseSec;
    protected String courseTime;
    protected String courseLoc;
    protected String courseProf;
    protected String tutName;
    protected String tutNum;
    protected String tutTime;
    protected String tutLoc;
    protected String tutSec;
    protected boolean isOnline;
    protected String labName;
    protected String labNum;
    protected String labTime;
    protected String labSec;
    protected String labLoc;

    public CourseInfo(String name){
        courseName = name;
    }

    public void setNum(String number){
        courseNum = number;
    }

    public void setLoc(String loc){
        courseLoc = loc;
    }

    public void setTime(String time){
        courseTime = time;
    }

    public void setTimeAPM(String time){
        int i = 0;
        for(; time.charAt(i) < 48 || time.charAt(i) > 57; i++);
        String days = time.substring(0,i);
        String realtime = time.substring(i);
        String starttime = realtime.substring(0,5);
        String endtime = realtime.substring(6);
        try{
            DateFormat f1 = new SimpleDateFormat("HH:mm"); //HH for hour of the day (0 - 23)
            Date d = f1.parse(starttime);
            DateFormat f2 = new SimpleDateFormat("h:mma");
            starttime = f2.format(d); // "12:18am"

            DateFormat f3 = new SimpleDateFormat("HH:mm"); //HH for hour of the day (0 - 23)
            Date d2 = f3.parse(endtime);
            DateFormat f4 = new SimpleDateFormat("h:mma");
            endtime = f4.format(d2); // "12:18am"


        } catch (ParseException e){
            e.printStackTrace();
        }

        courseTime = days + starttime + "-" + endtime;
    }
    public void setSec(String sec){
        courseSec = sec;
        isOnline = sec.equals("081");
    }

    public void setProf(String prof){
        courseProf = prof;
    }

    public void setTutname(){
        tutName = courseName;
    }

    public void setTutnum(String num){
        tutNum = num;
    }

    public void setTutTime(String time){
        tutTime = time;
    }

    public void setTutLoc(String loc) {
        tutLoc = loc;
    }

    public void setTutsec(String sec){
        tutSec = sec;
    }

    public void setlabName(){
        tutSec = courseName;
    }

    public void setlabNum(String num){ labNum = num;}

    public void setlabTime(String time){ labTime = time;}

    public void setlabLoc(String loc){ labLoc = loc;}

    public void setlabSec(String sec){ labSec = sec;}

    public String getCourseTime() {
        return courseTime;
    }

    public String getCourseName() { return courseName; }

    public String getCourseSec(){return courseSec;}
}
