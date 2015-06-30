package com.example.jason.myclass.Reminder;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jason.myclass.R;
import com.example.jason.myclass.Reminder.helpers.ItemTouchHelperAdapter;
import com.example.jason.myclass.Reminder.helpers.ItemTouchHelperViewHolder;

import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Jason on 2015-06-05.
 */

public class Reminder_Adapter extends RecyclerView.Adapter<Reminder_Adapter.ViewHolder> implements ItemTouchHelperAdapter {
    private List<Reminder_item> mDataset;
    private Context mContext;
    private ReminderDBHandler db;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        // each data item is just a string in this case
        public TextView mTitle;
        public TextView mLocation;
        public TextView mdaysLeft;
        public TextView mdaysLeft_Symbol;
        public CardView mcard_view;
        public RelativeLayout mleft_relative_Layout;
        public ViewHolder(View v) {
            super(v);
            mTitle = (TextView) v.findViewById(R.id.TitleInCardView);
            mLocation = (TextView) v.findViewById(R.id.LocationInCardView);
            mdaysLeft = (TextView) v.findViewById(R.id.NumberofDaysLeftInCardView);
            mdaysLeft_Symbol = (TextView) v.findViewById(R.id.DaysLeftInCardView);
            mcard_view = (CardView) v.findViewById(R.id.card_view);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public Reminder_Adapter(List<Reminder_item> myDataset, Context context) {
        this.mDataset = myDataset;
        this.mContext = context;
        this.db = new ReminderDBHandler(mContext);
    }


    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reminderitem_cardview, parent, false);
        // set the view's size, margins, paddings and layout parameters
        final ViewHolder mViewHolder = new ViewHolder(v);
        mViewHolder.itemView.setClickable(true);
        mViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Log.d("mViewHolder", "enter onclick");
                                                    }
                                                }
        );
        return mViewHolder;
    }



    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTitle.setText(mDataset.get(position).get_title());
        holder.mLocation.setText(mDataset.get(position).get_location());
        long event_time = mDataset.get(position).get_unix_time();
        long now_time = GregorianCalendar.getInstance().getTime().getTime();
        long days_left = (event_time - now_time)/1000/60/60/24;

        if(days_left < 2){
                holder.mdaysLeft_Symbol.setText("Day Left");
                holder.mdaysLeft.setTextColor(Color.parseColor("#ff5252"));
        }
        holder.mdaysLeft.setText(Long.toString(days_left));



        //Log.d("onBindViewHolder", "event_time is : " + event_time);
        //Log.d("onBindViewHolder", "now_time is : " + now_time);
        //Log.d("onBindViewHolder", "now_time is : " + now_time);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void updateView() {
        Log.d("Reminder adapter ", "updating view");
        ReminderDBHandler db = new ReminderDBHandler(mContext);
        mDataset = db.getAllReminders();
        notifyDataSetChanged();
    }

    public void removeAt(int position) {
        dbItemRemove(position);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    public void dbItemRemove(int position){
        String mUUID_string = mDataset.get(position).get_id();
        Log.d("dbItemRemove", "position is " + position);
        db.removeReminder(mUUID_string);
    }


    @Override
    public void onItemDismiss(int position) {
        dbItemRemove(position);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }
}