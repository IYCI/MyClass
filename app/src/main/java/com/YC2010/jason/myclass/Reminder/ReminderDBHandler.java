package com.YC2010.jason.myclass.Reminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Jason on 2015-06-02.
 */
public class ReminderDBHandler extends SQLiteOpenHelper {
    // DB Version
    private static final int DATABASE_VERSION = 3;

    // DB Name
    private static final String DATABASE_NAME = "ReminderManager";

    // Table name
    private static final String TABLE_REMINDERS = "reminders";

    // TABLE_REMINDERS Columns names                            // columnIndex
    private static final String KEY_ID = "id";                  //      0
    private static final String KEY_TITLE = "title";            //      1
    private static final String KEY_LOCATION = "location";      //      2
    private static final String KEY_UNIX_TIME = "Unix_time";    //      3
    private static final String KEY_TYPE = "type";              //      4

    public ReminderDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("ReminderDBHandler", "onCreate");
        String CREATE_TABLE = "CREATE TABLE " + TABLE_REMINDERS + "("
                + KEY_ID + " STRING PRIMARY KEY," + KEY_TITLE + " TEXT,"
                + KEY_LOCATION + " TEXT," + KEY_UNIX_TIME + " TEXT,"
                + KEY_TYPE + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDERS);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new Reminder
    public void addReminder(Reminder_item reminder) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, reminder.get_id());
        values.put(KEY_TITLE, reminder.get_title());
        values.put(KEY_LOCATION, reminder.get_location());
        values.put(KEY_UNIX_TIME, reminder.get_unix_time());
        values.put(KEY_TYPE, reminder.get_type());

        // Inserting Row
        db.insert(TABLE_REMINDERS, null, values);
        db.close(); // Closing database connection
    }

    // remove a reminder
    public void removeReminder(String uuid_string){
        SQLiteDatabase db = this.getWritableDatabase();

        // deleting Row
        int affected_rows = db.delete(TABLE_REMINDERS, KEY_ID + " = ?", new String[]{uuid_string});
        Log.d("uuid", "uuid is " + uuid_string);
        Log.d("removeReminder", affected_rows + " rows deleted");
        db.close(); // Closing database connection
    }

    // remove a reminder
    public void removeAll(String type){
        SQLiteDatabase db = this.getWritableDatabase();

        // deleting Row
        int affected_rows = db.delete(TABLE_REMINDERS, KEY_TYPE + " = ?", new String[]{type});
        Log.d("removeReminder", affected_rows + " rows deleted");
        db.close(); // Closing database connection
    }

    public void removeAll(){
        SQLiteDatabase db = this.getWritableDatabase();

        // deleting Row
        int affected_rows = db.delete(TABLE_REMINDERS, null,null);
        Log.d("removeReminder", affected_rows + " rows deleted");
        db.close(); // Closing database connection
    }

    // get reminder count
    public int CountReminders(){
        SQLiteDatabase db = this.getReadableDatabase();

        long now_time = GregorianCalendar.getInstance().getTime().getTime();
        String selectQuery = "SELECT  COUNT(*) FROM " + TABLE_REMINDERS + " WHERE " +
                KEY_UNIX_TIME + " > " + now_time;
        SQLiteStatement s = db.compileStatement(selectQuery);
        long count = s.simpleQueryForLong();
        return (int)count;
    }

    // see if exam in 7 days
    public boolean closoToExam(Date mdate){
        SQLiteDatabase db = this.getReadableDatabase();
        long event_time = mdate.getTime();
        long weeks_time = 604800000;
        long time_in_a_week = event_time + weeks_time;
        String selectQuery = "SELECT  COUNT(*) FROM " + TABLE_REMINDERS + " WHERE " +
                KEY_UNIX_TIME + " > " + event_time + " and " +  KEY_UNIX_TIME + " < " +
                time_in_a_week + " and " + KEY_TYPE + " = 'e'";
        SQLiteStatement s = db.compileStatement(selectQuery);
        long count = s.simpleQueryForLong();
        //Log.d("getAllReminders", "long is " + count);
        return count > 0;
    }

    // Get All Reminder
    public List<Reminder_item> getAllReminders() {
        List<Reminder_item> reminderList = new ArrayList<>();

        // Select All Query
        long now_time = GregorianCalendar.getInstance().getTime().getTime();
        String selectQuery = "SELECT  * FROM " + TABLE_REMINDERS + " WHERE " +
                KEY_UNIX_TIME + " > " + now_time + " ORDER BY " + KEY_UNIX_TIME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                //Log.d("getAllReminders", "uuid is " + cursor.getString(0));
                Reminder_item reminder = new Reminder_item(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        Long.parseLong(cursor.getString(3)),
                        cursor.getString(4));

                // Adding reminder to list
                reminderList.add(reminder);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return contact list
        return reminderList;
    }
}
