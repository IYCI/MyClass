package com.example.jason.myclass;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Jason on 2015-06-05.
 */

public class Reminder_Adapter extends RecyclerView.Adapter<Reminder_Adapter.ViewHolder> {
    private List<Reminder_item> mDataset;
    private Context mContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTitle;
        public TextView mLocation;
        public TextView mdaysLeft;
        public ViewHolder(View v) {
            super(v);
            mTitle = (TextView) v.findViewById(R.id.TitleInCardView);
            mLocation = (TextView) v.findViewById(R.id.LocationInCardView);
            mdaysLeft = (TextView) v.findViewById(R.id.NumberofDaysLeftInCardView);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public Reminder_Adapter(List<Reminder_item> myDataset, Context context) {
        this.mDataset = myDataset;
        this.mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public Reminder_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reminderitem_cardview, parent, false);
        // set the view's size, margins, paddings and layout parameters

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTitle.setText(mDataset.get(position).get_title());
        holder.mLocation.setText(mDataset.get(position).get_location());

        long event_time = mDataset.get(position).get_unix_time();
        long now_time = GregorianCalendar.getInstance().getTime().getTime();
        int days_left = (int) (event_time - now_time)/1000/60/60/24;

        if (days_left < 0){
            // delete it!!!
        }
        else holder.mdaysLeft.setText(Integer.toString(days_left));

        Log.d("onBindViewHolder" ,"event_time is : " + event_time);
        Log.d("onBindViewHolder" ,"now_time is : " +  now_time);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void updateView() {
        ReminderDBHandler db = new ReminderDBHandler(mContext);
        mDataset = db.getAllReminders();
        notifyDataSetChanged();
    }
}