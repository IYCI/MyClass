package com.YC2010.MyClass.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.YC2010.MyClass.model.CourseInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jason on 2015-06-02.
 */
public class CoursesDBHandler extends SQLiteOpenHelper {
    private Context mContext;

    // DB Version
    private static final int DATABASE_VERSION = 5;

    // DB Name
    private static final String DATABASE_NAME = "CoursesManager";

    // Table name
    private static final String TABLE_COURSES = "Courses";

    // TABLE_REMINDERS Columns names                            // columnIndex

    private static final String KEY_NUM = "courseNum";          //      0
    private static final String KEY_NAME = "courseName";        //      1
    private static final String KEY_SECTION = "courseSec";      //      2
    private static final String KEY_TIME = "courseTime";        //      3
    private static final String KEY_LOC = "courseLoc";          //      4
    private static final String KEY_PROF = "courseProf";        //      5
    private static final String KEY_NAME_TUT = "tutName";       //      6
    private static final String KEY_NUM_TUT = "tutNum";         //      7
    private static final String KEY_TIME_TUT = "tutTime";       //      8
    private static final String KEY_LOC_TUT = "tutLoc";         //      9
    private static final String KEY_SEC_TUT = "tutSec";         //      10
    private static final String KEY_ONLINE  = "isOnline";       //      11
    private static final String KEY_TAKING_TERM  = "takingTerm";//      12

    public CoursesDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("CoursesDBHandler", "onCreate");
        String CREATE_TABLE = "CREATE TABLE " + TABLE_COURSES + "("
                + KEY_NUM + " STRING PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_SECTION + " TEXT,"
                + KEY_TIME + " TEXT," + KEY_LOC + " TEXT,"
                + KEY_PROF + " TEXT," + KEY_NAME_TUT + " TEXT,"
                + KEY_NUM_TUT + " TEXT," + KEY_TIME_TUT + " TEXT,"
                + KEY_LOC_TUT + " TEXT," + KEY_SEC_TUT + " TEXT,"
                + KEY_ONLINE + " INTEGER," + KEY_TAKING_TERM +  " INTEGER" + ")";
        db.execSQL(CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("CoursesDBHandler", "onUpgrade");
        switch(oldVersion) {
            case 4:
                try {
                    Log.d("CoursesDBHandler", "onUpgrade case 4");
                    db.execSQL("ALTER TABLE " + TABLE_COURSES + " ADD column " + KEY_TAKING_TERM + " INTEGER NOT NULL DEFAULT(" +
                            mContext.getSharedPreferences("TERMS", mContext.MODE_PRIVATE).getInt("CURRENT_TERM", 0) + ")");
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new course
    public void addCourse(CourseInfo mCourse) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NUM, mCourse.getCourseNum());
        values.put(KEY_NAME, mCourse.getCourseName());
        values.put(KEY_SECTION, mCourse.getCourseSec());
        values.put(KEY_TIME, mCourse.getCourseTime());
        values.put(KEY_LOC, mCourse.getCourseLoc());
        values.put(KEY_PROF, mCourse.getCourseProf());
        values.put(KEY_NAME_TUT, mCourse.getTutName());
        values.put(KEY_NUM_TUT, mCourse.getTutNum());
        values.put(KEY_SEC_TUT, mCourse.getTutSec());
        values.put(KEY_TIME_TUT, mCourse.getTutTime());
        values.put(KEY_LOC_TUT, mCourse.getTutLoc());
        values.put(KEY_ONLINE, mCourse.isOnline());
        values.put(KEY_TAKING_TERM, 0);

        // Inserting Row
        db.insert(TABLE_COURSES, null, values);
        db.close(); // Closing database connection
    }

    // add new course with taking term specify
    public void addCourse(CourseInfo mCourse, int term) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NUM, mCourse.getCourseNum());
        values.put(KEY_NAME, mCourse.getCourseName());
        values.put(KEY_SECTION, mCourse.getCourseSec());
        values.put(KEY_TIME, mCourse.getCourseTime());
        values.put(KEY_LOC, mCourse.getCourseLoc());
        values.put(KEY_PROF, mCourse.getCourseProf());
        values.put(KEY_NAME_TUT, mCourse.getTutName());
        values.put(KEY_NUM_TUT, mCourse.getTutNum());
        values.put(KEY_SEC_TUT, mCourse.getTutSec());
        values.put(KEY_TIME_TUT, mCourse.getTutTime());
        values.put(KEY_LOC_TUT, mCourse.getTutLoc());
        values.put(KEY_ONLINE, mCourse.isOnline());
        values.put(KEY_TAKING_TERM, term);

        // Inserting Row
        db.insert(TABLE_COURSES, null, values);
        db.close(); // Closing database connection
    }

    // remove a course
    public void removeCourse(String courseNum){
        SQLiteDatabase db = this.getWritableDatabase();

        // deleting Row
        int affected_rows = db.delete(TABLE_COURSES, KEY_NUM + " = ?", new String[]{courseNum});
        Log.d("removeReminder", affected_rows + " rows deleted");
        db.close(); // Closing database connection
    }

    public void removeAll(){
        SQLiteDatabase db = this.getWritableDatabase();

        // deleting Row
        int affected_rows = db.delete(TABLE_COURSES, null,null);
        Log.d("removeReminder", affected_rows + " rows deleted");
        db.close(); // Closing database connection
    }

    // get reminder count
    public int CountCourses(){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  COUNT(*) FROM " + TABLE_COURSES;
        SQLiteStatement s = db.compileStatement(selectQuery);
        long count = s.simpleQueryForLong();
        return (int)count;
    }

    // check if course exist
    public boolean IsInDB(String courseID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "Select * from " + TABLE_COURSES + " where " + KEY_NUM + " = " + courseID;
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    // Get All Courses
    public List<CourseInfo> getAllCourses() {
        List<CourseInfo> CoursesList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_COURSES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                CourseInfo course = new CourseInfo(cursor.getString(1));
                course.setNum(cursor.getString(0));
                course.setSec(cursor.getString(2));
                course.setTime(cursor.getString(3));
                course.setLoc(cursor.getString(4));
                course.setProf(cursor.getString(5));
                course.setTutname();
                course.setTutnum(cursor.getString(7));
                course.setTutTime(cursor.getString(8));
                course.setTutLoc(cursor.getString(9));
                course.setTutsec(cursor.getString(10));
                course.setIsOnline(cursor.getInt(11) > 0);

                // Adding course to list
                CoursesList.add(course);
            } while (cursor.moveToNext());
        }

        // return contact list
        return CoursesList;
    }

    // Get All Courses by term
    public List<CourseInfo> getAllCourses(int term) {
        List<CourseInfo> CoursesList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_COURSES + " WHERE " + KEY_TAKING_TERM + " = " + term;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                CourseInfo course = new CourseInfo(cursor.getString(1));
                course.setNum(cursor.getString(0));
                course.setSec(cursor.getString(2));
                course.setTime(cursor.getString(3));
                course.setLoc(cursor.getString(4));
                course.setProf(cursor.getString(5));
                course.setTutname();
                course.setTutnum(cursor.getString(7));
                course.setTutTime(cursor.getString(8));
                course.setTutLoc(cursor.getString(9));
                course.setTutsec(cursor.getString(10));
                course.setIsOnline(cursor.getInt(11) > 0);

                // Adding course to list
                CoursesList.add(course);
            } while (cursor.moveToNext());
        }

        // return contact list
        return CoursesList;
    }
}
