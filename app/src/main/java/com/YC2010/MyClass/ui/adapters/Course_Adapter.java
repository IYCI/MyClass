package com.YC2010.MyClass.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.YC2010.MyClass.model.CourseInfo;
import com.YC2010.MyClass.data.CoursesDBHandler;
import com.YC2010.MyClass.R;

import java.util.List;

/**
 * Created by Jason on 2015-06-05.
 */

public class Course_Adapter extends RecyclerView.Adapter<Course_Adapter.ViewHolder> {
    private List<CourseInfo> mDataset;
    private Context mContext;
    private CoursesDBHandler db;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder{
        // each data item is just a string in this case
        public TextView mTitle;
        public TextView mLocation;
        public TextView mSection;
        public TextView mTime;
        public ViewHolder(View v) {
            super(v);
            mTitle = (TextView) v.findViewById(R.id.TitleInCardView);
            mLocation = (TextView) v.findViewById(R.id.LocationInCardView);
            mSection = (TextView) v.findViewById(R.id.SecInCardView);
            mTime = (TextView) v.findViewById(R.id.TimeInCardView);
            //mdaysLeft = (TextView) v.findViewById(R.id.NumberofDaysLeftInCardView);
            //mdaysLeft_Symbol = (TextView) v.findViewById(R.id.DaysLeftInCardView);
        }


    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public Course_Adapter(List<CourseInfo> myDataset, Context context) {
        this.mDataset = myDataset;
        this.mContext = context;
        this.db = new CoursesDBHandler(mContext);
    }


    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_cardview, parent, false);

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
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTitle.setText(mDataset.get(position).getCourseName());
        holder.mLocation.setText(mDataset.get(position).getCourseLoc());
        holder.mSection.setText(mDataset.get(position).getCourseSec());
        holder.mTime.setText(mDataset.get(position).getCourseTime());
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
        CoursesDBHandler db = new CoursesDBHandler(mContext);
        mDataset = db.getAllCourses(mContext.getSharedPreferences("TERMS", mContext.MODE_PRIVATE).getInt("TERM_NUM", 0));
        notifyDataSetChanged();
    }

    public void removeAt(int position) {
        dbItemRemove(position);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    public void dbItemRemove(int position){
        String CourseNum = mDataset.get(position).getCourseNum();
        Log.d("dbItemRemove", "position is " + position);
        db.removeCourse(CourseNum);
    }
}