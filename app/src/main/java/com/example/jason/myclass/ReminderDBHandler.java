package com.example.jason.myclass;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jason on 2015-06-02.
 */
public class ReminderDBHandler extends SQLiteOpenHelper {
    // DB Version
    private static final int DATABASE_VERSION = 3;

    // DB Name
    private static final String DATABASE_NAME = "RemindersManager";

    // Table name
    private static final String TABLE_REMINDERS = "reminders";

    // TABLE_REMINDERS Columns names                            // columnIndex
    private static final String KEY_ID = "id";                  //      0
    private static final String KEY_TITLE = "title";            //      1
    private static final String KEY_LOCATION = "location";      //      2
    private static final String KEY_UNIX_TIME = "Unix_time";    //      3

    public ReminderDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_REMINDERS + "("
                + KEY_ID + " STRING PRIMARY KEY," + KEY_TITLE + " TEXT,"
                + KEY_LOCATION + " TEXT," + KEY_UNIX_TIME + " TEXT" + ")";
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
    void addReminder(Reminder_item reminder) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, reminder.get_id());
        values.put(KEY_TITLE, reminder.get_title());
        values.put(KEY_LOCATION, reminder.get_location());
        values.put(KEY_UNIX_TIME, reminder.get_unix_time());

        // Inserting Row
        db.insert(TABLE_REMINDERS, null, values);
        db.close(); // Closing database connection
    }


    // Get All Reminder
    public List<Reminder_item> getAllReminders() {
        List<Reminder_item> reminderList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_REMINDERS + " ORDER BY " + KEY_UNIX_TIME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Reminder_item reminder = new Reminder_item(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        Long.parseLong(cursor.getString(3)));

                // Adding contact to list
                reminderList.add(reminder);
            } while (cursor.moveToNext());
        }

        // return contact list
        return reminderList;
    }
}
