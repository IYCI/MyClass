package com.example.jason.myclass.Courses;

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

    public CourseInfo(String name){
        courseName = name;
        courseTime = null;
        courseLoc = null;
        tutName = null;
        tutNum = null;
        tutTime = null;
        tutLoc = null;
        tutSec = null;
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
    public void setSec(String sec){
        courseSec = sec;
        isOnline = sec.equals("Section081");
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
}
