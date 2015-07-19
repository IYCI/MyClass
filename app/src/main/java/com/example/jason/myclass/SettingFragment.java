package com.example.jason.myclass;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.design.widget.Snackbar;

import com.example.jason.myclass.Courses.CoursesDBHandler;
import com.example.jason.myclass.Reminder.ReminderDBHandler;
import com.example.jason.myclass.Reminder.Reminder_item;

import java.util.List;

/**
 * Created by Jason on 2015-05-26.
 */

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class SettingFragment extends PreferenceFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

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

    }

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }*/

    // TODO: Rename method, update argument and hook method into UI event
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
