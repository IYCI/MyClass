package com.YC2010.MyClass.ui.fragments;

import android.app.Fragment;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.YC2010.MyClass.R;
import com.YC2010.MyClass.data.CoursesDBHandler;
import com.YC2010.MyClass.model.CourseInfo;
import com.YC2010.MyClass.utils.Constants;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Jason on 2015-05-25.
 */

public class CalendarFragment extends Fragment implements WeekView.MonthChangeListener,
        WeekView.EventClickListener, WeekView.EventLongPressListener {
    private WeekView mWeekView;
    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_FIVE_DAY_VIEW = 2;
    private static final int TYPE_WEEK_VIEW = 3;
    private int mWeekViewType = TYPE_FIVE_DAY_VIEW;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_COURSE = "COURSE";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mWeekView = (WeekView) view.findViewById(R.id.weekView);

        // Show a toast message about the touched event.
        mWeekView.setOnEventClickListener(this);

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(this);

        // Set long press listener for events.
        mWeekView.setEventLongPressListener(this);

        // Go to correct position in the page
        mWeekView.goToHour(8);

        // Go to next Term's date if select next term
        if (getActivity().getSharedPreferences("TERMS", getActivity().MODE_PRIVATE).getInt("CURRENT_TERM", 0) !=
                getActivity().getSharedPreferences("TERMS", getActivity().MODE_PRIVATE).getInt("TERM_NUM", -1)) {
            mWeekView.goToDate(Constants.nextTermStartDate);
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.calendar_menu, menu);
            super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_day_view:
                if (mWeekViewType != TYPE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(1);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.goToHour(8);
                }
                return true;
            case R.id.action_five_day_view:
                if (mWeekViewType != TYPE_FIVE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_FIVE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(5);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.goToHour(8);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        List<WeekViewEvent> events = parseEvent(newMonth);
        List<WeekViewEvent> newEvents= new ArrayList<>();

        // Display courses for every week
        for (WeekViewEvent event : events) {
            Calendar dateTime = event .getStartTime();
            Calendar dateEndTime = event .getEndTime();
            Calendar monCal = getFirstDay(newMonth - 1, newYear, dateTime.get(Calendar.DAY_OF_WEEK));
            int hday = dateTime.get(Calendar.HOUR_OF_DAY);
            int mday = dateTime.get(Calendar.MINUTE);
            int ehday = dateEndTime.get(Calendar.HOUR_OF_DAY);
            int emday = dateEndTime.get(Calendar.MINUTE);

            for (int k = monCal.get(Calendar.DAY_OF_MONTH); k <= monCal.getActualMaximum(Calendar.DAY_OF_MONTH); k += 7) {
                Calendar startTime = Calendar.getInstance();
                startTime.set(Calendar.MONTH, newMonth - 1);
                startTime.set(Calendar.DAY_OF_MONTH, k);
                startTime.set(Calendar.YEAR, newYear);
                startTime.set(Calendar.HOUR_OF_DAY, hday);
                startTime.set(Calendar.MINUTE, mday);

                Calendar endTime = (Calendar) startTime.clone();
                endTime.set(Calendar.HOUR_OF_DAY, ehday);
                endTime.set(Calendar.MINUTE, emday - 1);
                endTime.set(Calendar.MONTH, newMonth - 1);


                WeekViewEvent newEvent = new WeekViewEvent(1, event .getName(), startTime, endTime);
                newEvent.setColor(event .getColor());
                newEvents.add(newEvent);
            }
        }

        return newEvents;
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        //Toast.makeText(getActivity(), "Clicked " + event.getName(), Toast.LENGTH_SHORT).show();
        SearchFragment mSearchFragment = new SearchFragment();
        Bundle args = new Bundle();
        String course_name = event.getName().split(System.getProperty("line.separator"))[0];
        args.putString(ARG_COURSE, course_name);
        mSearchFragment.setArguments(args);
        getFragmentManager().beginTransaction()
                .replace(R.id.container, mSearchFragment)
                .addToBackStack("7")
                .commit();
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
        Toast.makeText(getActivity(), "Long pressed event: " + event.getName(), Toast.LENGTH_SHORT).show();
    }

    private List<WeekViewEvent> parseEvent(int newMonth) {
        List<WeekViewEvent> events = new ArrayList<>();

        CoursesDBHandler db = new CoursesDBHandler(getActivity());
        List<CourseInfo> myDataset = db.getAllCourses(getActivity().getSharedPreferences("TERMS", getActivity().MODE_PRIVATE).getInt("TERM_NUM", 0));
        db.close();

        int eventCount = 1;

        if (myDataset == null) return events;

        for (int k = 0; k < myDataset.size(); k++) {
            // Won't display online courses
            CourseInfo course = myDataset.get(k);

            if (course.isOnline()) {
                continue;
            }

            String time = course.getCourseTime();
            String name = course.getCourseName();

            if (time == null) continue;

            String[] splited = time.split("\\s+");
            String days = splited[0];

            for (int i = 0; i < days.length(); i++) {
                char cur = days.charAt(i);
                Calendar startTime = Calendar.getInstance();
                Calendar tempTime = Calendar.getInstance();

                try {

                    switch (cur) {
                        case 'T':
                            if (i + 1 < days.length() && days.charAt(i + 1) == 'h') {
                                startTime.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                                i++;
                            } else {
                                startTime.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                            }
                            break;
                        case 'M':
                            startTime.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                            break;
                        case 'W':
                            startTime.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                            break;
                        case 'F':
                            startTime.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                            break;
                    }

                    String[] times = splited[1].split("-");

                    SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mma", Locale.CANADA);
                    Date startClock = parseFormat.parse(times[0]);
                    tempTime.setTime(startClock);
                    startTime.set(Calendar.HOUR_OF_DAY, tempTime.get(Calendar.HOUR_OF_DAY));
                    startTime.set(Calendar.MINUTE, tempTime.get(Calendar.MINUTE));
                    startTime.set(Calendar.MONTH, newMonth - 1);

                    Calendar endTime = (Calendar) startTime.clone();
                    Date endClock = parseFormat.parse(times[1]);
                    tempTime.setTime(endClock);
                    endTime.set(Calendar.MONTH, newMonth - 1);
                    endTime.set(Calendar.HOUR_OF_DAY, tempTime.get(Calendar.HOUR_OF_DAY));
                    endTime.set(Calendar.MINUTE, tempTime.get(Calendar.MINUTE));

                    String formattedTime = times[0] + "-\n" + times[1];

                    WeekViewEvent event = new WeekViewEvent(eventCount++, name + "\n" + formattedTime, startTime, endTime);

                    // Change display color of courses
                    // TODO: what if exceed 5 courses?
                    switch (k % 5) {
                        case 0:
                            event.setColor(getResources().getColor(R.color.event_color_01));
                            break;
                        case 1:
                            event.setColor(getResources().getColor(R.color.event_color_02));
                            break;
                        case 2:
                            event.setColor(getResources().getColor(R.color.event_color_03));
                            break;
                        case 3:
                            event.setColor(getResources().getColor(R.color.event_color_04));
                            break;
                        case 4:
                            event.setColor(getResources().getColor(R.color.event_color_05));
                            break;
                    }

                    events.add(event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        return events;
    }

    private Calendar getFirstDay(int i2, int i, int weekday) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, i2);
        c.set(Calendar.YEAR, i);
        c.set(Calendar.DAY_OF_MONTH, 1);
        int day = c.get(Calendar.DAY_OF_WEEK);
        while (day != weekday) {
            c.add(Calendar.DAY_OF_MONTH, 1);
            day = c.get(Calendar.DAY_OF_WEEK);
        }
        return c;
    }
}
