package com.YC2010.jason.myclass.CourseSearch;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.YC2010.jason.myclass.Constants;
import com.YC2010.jason.myclass.DataObjects.FinalObject;
import com.YC2010.jason.myclass.R;

import java.util.ArrayList;

public class FinalsListAdapter extends BaseAdapter {
    ArrayList<FinalObject> mArrayList;
    Context mContext;
    LayoutInflater mLayoutInflater;

    public FinalsListAdapter(Context context, int textViewResourceId, Bundle bundle) {
        mLayoutInflater = LayoutInflater.from(context);
        mArrayList = bundle.getParcelableArrayList(Constants.finalObjectListKey);
        mContext = context;
        Log.d("SectionListAdapter", "Course name is " + bundle.getString("courseName"));
    }

    @Override
    public FinalObject getItem(int i) {
        return mArrayList.get(i);
    }

    @Override
    public int getCount() {
        return mArrayList != null ? mArrayList.size() : 0;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Log.d("SectionListAdapter", "enter getView");
        View v = convertView;

        v = mLayoutInflater.inflate(R.layout.section_item, null);


        Log.d("SectionListAdapter", "position is " + position);

        TextView date = (TextView) v.findViewById(R.id.section_item_capacity);
        TextView sec = (TextView) v.findViewById(R.id.section_item_lec);
        TextView loc = (TextView) v.findViewById(R.id.section_item_loc);
        TextView time = (TextView) v.findViewById(R.id.section_item_time);

        FinalObject finalInfo = getItem(position);

        sec.setText(finalInfo.getSection());
        time.setText(finalInfo.getTime());
        loc.setText(finalInfo.getLocation());
        date.setText(finalInfo.getDate());

        return v;
    }

}