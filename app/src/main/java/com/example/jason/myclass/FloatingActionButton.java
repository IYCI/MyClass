/*
 * Copyright 2014, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.jason.myclass;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Outline;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.Window;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TimePicker;

import java.util.UUID;


/**
 * A Floating Action Button is a {@link Checkable} view distinguished by a circled
 * icon floating above the UI, with special motion behaviors.
 */
public class FloatingActionButton extends FrameLayout implements Checkable {

    /**
     * Interface definition for a callback to be invoked when the checked state
     * of a compound button changes.
     */
    public static interface OnCheckedChangeListener {

        /**
         * Called when the checked state of a FAB has changed.
         *
         * @param fabView   The FAB view whose state has changed.
         * @param isChecked The new checked state of buttonView.
         */
        void onCheckedChanged(FloatingActionButton fabView, boolean isChecked);
    }

    /**
     * An array of states.
     */
    private static final int[] CHECKED_STATE_SET = {
            android.R.attr.state_checked
    };

    private static final String TAG = "FloatingActionButton";

    // A boolean that tells if the FAB is checked or not.
    private boolean mChecked;

    // A listener to communicate that the FAB has changed it's state
    private OnCheckedChangeListener mOnCheckedChangeListener;

    public FloatingActionButton(Context context) {
        this(context, null, 0, 0);
    }

    public FloatingActionButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0, 0);
    }

    public FloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public FloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr);

        setClickable(true);

        // Set the outline provider for this view. The provider is given the outline which it can
        // then modify as needed. In this case we set the outline to be an oval fitting the height
        // and width.
        setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setOval(0, 0, getWidth(), getHeight());
            }
        });

        // Finally, enable clipping to the outline, using the provider we set above
        setClipToOutline(true);
    }

    /**
     * Sets the checked/unchecked state of the FAB.
     * @param checked
     */
    public void setChecked(boolean checked) {

    }

    /**
     * Register a callback to be invoked when the checked state of this button
     * changes.
     *
     * @param listener the callback to call on checked state change
     */
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        //mOnCheckedChangeListener = listener;
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        //setChecked(!mChecked);
    }




    /**
     * Override performClick() so that we can toggle the checked state when the view is clicked
     */
    @Override
    public boolean performClick() {

        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // 2. Chain together various setter methods to set the dialog characteristics
                builder.setTitle(R.string.newEvent_dialog_title);
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
        // 3. Get the AlertDialog from create()
        final AlertDialog dialog = builder.create();
        dialog.show();


        // button for picking date
        // create date picker dialog
        final Button pick_date = (Button) dialog.findViewById(R.id.dateBtn);

        //int year, month, day, hour, minute;

        pick_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder datePicker_builder = new AlertDialog.Builder(getContext());
                datePicker_builder.setView(R.layout.dialog_add_event_date_picker);


                datePicker_builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked cancel button

                    }
                });
                final AlertDialog DatePicker_Dialog;
                DatePicker_Dialog = datePicker_builder.create();
                DatePicker_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


                DatePicker_Dialog.setButton(DatePicker_Dialog.BUTTON_POSITIVE, getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        final DatePicker mDatePicker = (DatePicker) DatePicker_Dialog.findViewById(R.id.datePicker);

                        // saving info to SharedPreferences
                        SharedPreferences new_event_dialog_buffer = getContext().getSharedPreferences("new_event_dialog_buffer", 0);
                        SharedPreferences.Editor buffer_editor = new_event_dialog_buffer.edit();
                        buffer_editor.putInt("year", mDatePicker.getYear());
                        buffer_editor.putInt("month", mDatePicker.getMonth());
                        // Counting of months in the Calendar class is zero based,
                        // so when we display, we add by one
                        Log.d("buffer_editor", "Month is : " + mDatePicker.getMonth() + 1);
                        buffer_editor.putInt("day", mDatePicker.getDayOfMonth());
                        buffer_editor.commit();

                        int month = mDatePicker.getMonth() + 1;
                        String date = mDatePicker.getYear() + "/" + month + "/" + mDatePicker.getDayOfMonth();
                        //editor.putString("new_event_dialog_buffer", date);

                        //editor.apply();
                        pick_date.setText(date);
                        pick_date.setTextSize(22);
                    }
                });

                DatePicker_Dialog.show();
                DatePicker_Dialog.getButton(DatePicker_Dialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.myPrimaryColor));
                DatePicker_Dialog.getButton(DatePicker_Dialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.myPrimaryColor));

            }
        });



        // button for picking time
        // create date picker dialog
        final Button pick_time = (Button) dialog.findViewById(R.id.TimeBtn);
        pick_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder timePicker_builder = new AlertDialog.Builder(getContext());
                timePicker_builder.setView(R.layout.dialog_add_event_time_picker);
                timePicker_builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked cancel button

                    }
                });
                final AlertDialog TimePicker_Dialog = timePicker_builder.create();
                TimePicker_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                TimePicker_Dialog.setButton(TimePicker_Dialog.BUTTON_POSITIVE, getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        final TimePicker mTimePicker = (TimePicker) TimePicker_Dialog.findViewById(R.id.timePicker);

                        // saving info to SharedPreferences
                        SharedPreferences new_event_dialog_buffer = getContext().getSharedPreferences("new_event_dialog_buffer", 0);
                        SharedPreferences.Editor buffer_editor = new_event_dialog_buffer.edit();
                        buffer_editor.putInt("hour", mTimePicker.getCurrentHour());
                        buffer_editor.putInt("minute", mTimePicker.getCurrentMinute());
                        buffer_editor.commit();
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
                TimePicker_Dialog.getButton(TimePicker_Dialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.myPrimaryColor));
                TimePicker_Dialog.getButton(TimePicker_Dialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.myPrimaryColor));
            }
        });
        dialog.getButton(dialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.myPrimaryColor));
        dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.myPrimaryColor));

        // insert into database
        final ReminderDBHandler db = new ReminderDBHandler(getContext());
        final EditText event_name= (EditText) dialog.findViewById(R.id.event_name);
        final EditText event_location= (EditText) dialog.findViewById(R.id.event_location);

        dialog.getButton(dialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // User clicked confirm button
                SharedPreferences new_event_dialog_buffer = getContext().getSharedPreferences("new_event_dialog_buffer", 0);

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

                dialog.dismiss();
            }
        });



        return super.performClick();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // As we have changed size, we should invalidate the outline so that is the the
        // correct size
        invalidateOutline();
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }
}
