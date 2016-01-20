package com.YC2010.jason.myclass.ui.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;

import com.YC2010.jason.myclass.utils.Constants;
import com.YC2010.jason.myclass.data.CoursesDBHandler;
import com.YC2010.jason.myclass.R;
import com.YC2010.jason.myclass.data.ReminderDBHandler;
import com.YC2010.jason.myclass.model.Reminder_item;

import java.util.List;

/**
 * Created by Jason on 2015-05-26.
 */

public class SettingFragment extends PreferenceFragment {
    private OnFragmentInteractionListener mListener;

    public SettingFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        Preference clean_reminder_pref = (Preference) findPreference("pref_key_clean_reminder_db");
        clean_reminder_pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                //open browser or intent here
                // should show an alert dialog
                ReminderDBHandler reminder_db = new ReminderDBHandler(getActivity());
                reminder_db.removeAll();
                reminder_db.close();

                // show snackBar
                Snackbar.make(getView(), "All reminders removed", Snackbar.LENGTH_SHORT)
                        .show();
                return true;
            }
        });

        Preference import_sample_pref = (Preference) findPreference("pref_key_import_reminders");
        import_sample_pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                //open browser or intent here
                ReminderDBHandler reminder_db = new ReminderDBHandler(getActivity());
                reminder_db.removeAll("d");
                List<Reminder_item> holiday_2015 = Constants.get_holidays();
                List<Reminder_item> sample_reminder = Constants.get_sample_reminder();
                for(int i = 0; i < holiday_2015.size(); i++){
                    reminder_db.addReminder(holiday_2015.get(i));
                }
                for(int i = 0; i < sample_reminder.size(); i++){
                    reminder_db.addReminder(sample_reminder.get(i));
                }

                // show snackBar
                Snackbar.make(getView(), "Sample reminders added", Snackbar.LENGTH_SHORT)
                        .show();
                return true;
            }
        });

        Preference clean_course_pref = (Preference) findPreference("pref_key_clean_course_db");
        clean_course_pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                //open browser or intent here
                // should show an alert dialog
                CoursesDBHandler courses_db = new CoursesDBHandler(getActivity());
                courses_db.removeAll();
                courses_db.close();

                // show snackBar
                Snackbar.make(getView(), "All courses removed", Snackbar.LENGTH_SHORT)
                        .show();
                return true;
            }
        });

        Preference credit = findPreference("pref_key_Credit");
        credit.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder
                        .setTitle(R.string.credit_title)
                        .setMessage(R.string.credit_content)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {}
                        });
                builder.show();
                return true;
            }
        });

    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }

}
