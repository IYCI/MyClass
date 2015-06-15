package com.example.jason.myclass.Reminder;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.jason.myclass.MainActivity;
import com.example.jason.myclass.R;

import java.util.List;
import java.util.UUID;

/**
 * Created by Jason on 2015-05-25.
 */
public class ReminderFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //return inflater.inflate(R.layout.fragment_reminder, container, false);

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_reminder, container, false);

        // Make this {@link Fragment} listen for changes in both FABs.
        //FloatingActionButton fab1 = (FloatingActionButton) rootView.findViewById(R.id.fab_1);
        //fab1.setOnCheckedChangeListener(this);



        // RecyclerView:
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.reminder_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // get reminder from db
        ReminderDBHandler db = new ReminderDBHandler(getActivity());
        List<Reminder_item> myDataset = db.getAllReminders();

        //String[] myDataset = {"123\nsdfsdf","345\n123","456\n123"};

        // specify an adapter (see also next example)
        mAdapter = new Reminder_Adapter(myDataset, getActivity());

        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                Log.d("ReminderFragment", "Data set changed");
            }
        });

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Log.d("mRecyclerView", "enter onclick");
                // 1. Instantiate an AlertDialog.Builder with its constructor
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                // 2. Chain together various setter methods to set the dialog characteristics
                builder.setTitle(R.string.edit_Event_dialog_title);
                builder.setView(R.layout.dialog_add_event);



                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked confirm button
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked cancel button

                    }
                });
                builder.setNeutralButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked delete button

                    }
                });
                // 3. Get the AlertDialog from create()
                final AlertDialog dialog = builder.create();
                dialog.show();


                // button for picking date
                // create date picker dialog
                final Button pick_date = (Button) dialog.findViewById(R.id.dateBtn);

                // fetch data from reminder db
                final ReminderDBHandler db = new ReminderDBHandler(getActivity());

                //int year, month, day, hour, minute;

                pick_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        AlertDialog.Builder datePicker_builder = new AlertDialog.Builder(v.getContext());
                        datePicker_builder.setView(R.layout.dialog_add_event_date_picker);


                        datePicker_builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked cancel button

                            }
                        });
                        final AlertDialog DatePicker_Dialog;
                        DatePicker_Dialog = datePicker_builder.create();
                        DatePicker_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


                        DatePicker_Dialog.setButton(DatePicker_Dialog.BUTTON_POSITIVE, v.getContext().getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                final DatePicker mDatePicker = (DatePicker) DatePicker_Dialog.findViewById(R.id.datePicker);

                                // saving into to SharedPreferences
                                SharedPreferences new_event_dialog_buffer = v.getContext().getSharedPreferences("new_event_dialog_buffer", 0);
                                SharedPreferences.Editor buffer_editor = new_event_dialog_buffer.edit();
                                buffer_editor.putInt("year", mDatePicker.getYear());
                                buffer_editor.putInt("month", mDatePicker.getMonth());
                                // Counting of months in the Calendar class is zero based,
                                // so when we display, we add by one
                                Log.d("buffer_editor", "Month is : " + mDatePicker.getMonth() + 1);
                                buffer_editor.putInt("day", mDatePicker.getDayOfMonth());
                                buffer_editor.apply();

                                int month = mDatePicker.getMonth() + 1;
                                String date = mDatePicker.getYear() + "-" + month + "-" + mDatePicker.getDayOfMonth();
                                //editor.putString("new_event_dialog_buffer", date);

                                //editor.apply();
                                pick_date.setText(date);
                                pick_date.setTextSize(22);
                            }
                        });

                        DatePicker_Dialog.show();
                        DatePicker_Dialog.getButton(DatePicker_Dialog.BUTTON_NEGATIVE).setTextColor(v.getContext().getResources().getColor(R.color.myPrimaryColor));
                        DatePicker_Dialog.getButton(DatePicker_Dialog.BUTTON_POSITIVE).setTextColor(v.getContext().getResources().getColor(R.color.myPrimaryColor));

                    }
                });



                // button for picking time
                // create date picker dialog
                final Button pick_time = (Button) dialog.findViewById(R.id.TimeBtn);
                pick_time.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        AlertDialog.Builder timePicker_builder = new AlertDialog.Builder(v.getContext());
                        timePicker_builder.setView(R.layout.dialog_add_event_time_picker);
                        timePicker_builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked cancel button

                            }
                        });
                        final AlertDialog TimePicker_Dialog = timePicker_builder.create();
                        TimePicker_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                        TimePicker_Dialog.setButton(TimePicker_Dialog.BUTTON_POSITIVE, v.getContext().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                final TimePicker mTimePicker = (TimePicker) TimePicker_Dialog.findViewById(R.id.timePicker);

                                // saving info to SharedPreferences
                                SharedPreferences new_event_dialog_buffer = v.getContext().getSharedPreferences("new_event_dialog_buffer", 0);
                                SharedPreferences.Editor buffer_editor = new_event_dialog_buffer.edit();
                                buffer_editor.putInt("hour", mTimePicker.getCurrentHour());
                                buffer_editor.putInt("minute", mTimePicker.getCurrentMinute());
                                buffer_editor.apply();
                                //hour = mTimePicker.getCurrentHour();
                                //minute = mTimePicker.getCurrentMinute();
                                String minute = "" + mTimePicker.getCurrentMinute();
                                if (mTimePicker.getCurrentMinute() < 10) minute = "0" + minute;
                                String time = mTimePicker.getCurrentHour() + " : " + minute;

                                //editor.putString("new_event_dialog_buffer", time);
                                //editor.apply();
                                pick_time.setText(time);
                                pick_time.setTextSize(32);
                            }
                        });

                        TimePicker_Dialog.show();
                        TimePicker_Dialog.getButton(TimePicker_Dialog.BUTTON_NEGATIVE).setTextColor(v.getContext().getResources().getColor(R.color.myPrimaryColor));
                        TimePicker_Dialog.getButton(TimePicker_Dialog.BUTTON_POSITIVE).setTextColor(v.getContext().getResources().getColor(R.color.myPrimaryColor));
                    }
                });
                dialog.getButton(dialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.myPrimaryColor));
                dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.myPrimaryColor));

                // insert into database

                final EditText event_name= (EditText) dialog.findViewById(R.id.event_name);
                final EditText event_location= (EditText) dialog.findViewById(R.id.event_location);

                dialog.getButton(dialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        // User clicked confirm button
                        SharedPreferences new_event_dialog_buffer = view.getContext().getSharedPreferences("new_event_dialog_buffer", 0);
                        SharedPreferences.Editor buffer_editor = new_event_dialog_buffer.edit();
                        Log.d("FAB", "Month is : " + new_event_dialog_buffer.getInt("month", 0));
                        Log.d("FAB", "Day is : " + new_event_dialog_buffer.getInt("day", 0));
                        db.addReminder(new Reminder_item(
                                UUID.randomUUID().toString(),
                                event_name.getText().toString(),
                                event_location.getText().toString(),
                                new_event_dialog_buffer.getInt("year", 0),
                                new_event_dialog_buffer.getInt("month", 0),
                                new_event_dialog_buffer.getInt("day", 0),
                                new_event_dialog_buffer.getInt("hour", 0),
                                new_event_dialog_buffer.getInt("minute", 0)
                        ));
                        buffer_editor.clear().apply();

                        MainActivity mainActivity = (MainActivity) getActivity();
                        RecyclerView recyclerView = (RecyclerView) mainActivity.findViewById(R.id.reminder_recycler_view);

                        if (recyclerView != null) {
                            Reminder_Adapter adapter = (Reminder_Adapter) recyclerView.getAdapter();
                            adapter.updateView();
                        }

                        dialog.dismiss();
                    }
                });
            }

            @Override
            public void onLongClick(View view, final int position) {
                Log.d("mRecyclerView", "enter onlongclick");
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Delete this reminder?");

                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.reminder_recycler_view);
                        Reminder_Adapter adapter = (Reminder_Adapter) recyclerView.getAdapter();
                        adapter.removeAt(position);
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // nothing
                    }
                });
                AlertDialog confirmation = builder.create();

                confirmation.show();
                //confirmation.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                confirmation.getButton(confirmation.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.myPrimaryColor));
                confirmation.getButton(confirmation.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.myPrimaryColor));
            }
        }));

        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildLayoutPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(final RecyclerView rv, MotionEvent e) {

            final View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                //Log.d("onInterceptTouchEvent", "before delay");
                // delay 100ms for displaying ripple effect
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        //write the code here which you want to run after 500 milliseconds
                        //Log.d("onInterceptTouchEvent", "delayed");
                        clickListener.onClick(child, rv.getChildLayoutPosition(child));
                    }
                }, 100);
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }
    }
}
