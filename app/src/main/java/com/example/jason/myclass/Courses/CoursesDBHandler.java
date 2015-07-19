package com.example.jason.myclass.Courses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jason on 2015-06-02.
 */
public class CoursesDBHandler extends SQLiteOpenHelper {
    // DB Version
    private static final int DATABASE_VERSION = 3;

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

    public CoursesDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
                + KEY_ONLINE + " INTEGER"+ ")";
        db.execSQL(CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new course
    public void addSchedule(Schedule schedule) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);

        // Create tables again
        onCreate(db);

        for(int i = 0; i < schedule.NumofCourse; i++){
            ContentValues values = new ContentValues();
            values.put(KEY_NUM, schedule.courses[i].courseNum);
            values.put(KEY_NAME, schedule.courses[i].courseName);
            values.put(KEY_SECTION, schedule.courses[i].courseSec);
            values.put(KEY_TIME, schedule.courses[i].courseTime);
            values.put(KEY_LOC, schedule.courses[i].courseLoc);
            values.put(KEY_PROF, schedule.courses[i].courseProf);
            values.put(KEY_NAME_TUT, schedule.courses[i].tutName);
            values.put(KEY_NUM_TUT, schedule.courses[i].tutNum);
            values.put(KEY_SEC_TUT, schedule.courses[i].tutSec);
            values.put(KEY_TIME_TUT, schedule.courses[i].tutTime);
            values.put(KEY_LOC_TUT, schedule.courses[i].tutLoc);

            // Inserting Row
            db.insert(TABLE_COURSES, null, values);
        }

        db.close(); // Closing database connection
    }

    // Adding new course
    public void addCourse(CourseInfo mCourse) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NUM, mCourse.courseNum);
        values.put(KEY_NAME, mCourse.courseName);
        values.put(KEY_SECTION, mCourse.courseSec);
        values.put(KEY_TIME, mCourse.courseTime);
        values.put(KEY_LOC, mCourse.courseLoc);
        values.put(KEY_PROF, mCourse.courseProf);
        values.put(KEY_NAME_TUT, mCourse.tutName);
        values.put(KEY_NUM_TUT, mCourse.tutNum);
        values.put(KEY_SEC_TUT, mCourse.tutSec);
        values.put(KEY_TIME_TUT, mCourse.tutTime);
        values.put(KEY_LOC_TUT, mCourse.tutLoc);

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

                // Adding course to list
                CoursesList.add(course);
            } while (cursor.moveToNext());
        }

        // return contact list
        return CoursesList;
    }
}
