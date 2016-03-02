package com.YC2010.MyClass.utils;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.SearchRecentSuggestionsProvider;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.YC2010.MyClass.R;
import com.YC2010.MyClass.callbacks.AsyncTaskCallbackInterface;
import com.YC2010.MyClass.data.fetchtasks.AllCoursesFetchTask;
import com.YC2010.MyClass.data.fetchtasks.SearchFetchTask;

import java.util.ArrayList;

/**
 * Created by Yusong on 2016-02-09.
 */
public class SampleRecentSuggestionsProvider extends SearchRecentSuggestionsProvider {

    public static final String AUTHORITY = "com.YC2010.MyClass.utils.SampleRecentSuggestionsProvider";
//            SampleRecentSuggestionsProvider.class.getName();
    private ProgressDialog mProgDialog;

    public ArrayList<String> subject_arraylist = new ArrayList<>();
    public ArrayList<String> titles = new ArrayList<>();

    public static final int MODE = DATABASE_MODE_QUERIES | DATABASE_MODE_2LINES;;

    public SampleRecentSuggestionsProvider() {
        setupSuggestions(AUTHORITY, MODE);

        AllCoursesFetchTask coursesFetchTask = new AllCoursesFetchTask(new AsyncTaskCallbackInterface() {
            @Override
            public void onOperationComplete(Bundle bundle) {

                //mProgDialog.dismiss();

            }
        });
        coursesFetchTask.execute("w");

        subject_arraylist = coursesFetchTask.subject_arraylist;
        titles = coursesFetchTask.title_arraylist;
    }
    private static final String[] COLUMNS = {
      "_id",
      SearchManager.SUGGEST_COLUMN_TEXT_1,
            SearchManager.SUGGEST_COLUMN_TEXT_2,
            SearchManager.SUGGEST_COLUMN_INTENT_DATA
    /*SearchManager.SUGGEST_COLUMN_FORMAT,
    SearchManager.SUGGEST_COLUMN_INTENT_DATA,
    SearchManager.SUGGEST_COLUMN_INTENT_ACTION,
    SearchManager.SUGGEST_COLUMN_SHORTCUT_ID*/
    };
    @Override
    public Cursor query(Uri uri,
                        String[] projection,
                        String selection,
                        String[] selectionArgs,
                        String sortOrder) {
        Cursor recentCursor = super.query(uri, projection, selection, selectionArgs,
                sortOrder);

        String query = selectionArgs[0];

        if (query == null || query.length() == 0) {
            return null;
        }

        MatrixCursor mc = new MatrixCursor(COLUMNS);

        try {
            for (int i = 0; i < subject_arraylist.size(); ++i) {
                String course = subject_arraylist.get(i).toLowerCase();
                String title = titles.get(i);
                if (course.contains(query)) {
                    mc.addRow(createRow(i, course, title, "lebron"));
                }
            }
            return mc;
        } catch (Exception e) {
            e.printStackTrace();
        }

        Cursor[] cursors = new Cursor[] { recentCursor, mc};

        return new MergeCursor(cursors);
    }

    private Object[] createRow(Integer id, String text1, String text2,
                               String name) {
        return new Object[] { id, // _id
                text1,
                text2,
                text1
        };
    }
}
