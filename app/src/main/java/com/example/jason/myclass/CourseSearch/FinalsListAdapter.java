package com.example.jason.myclass.CourseSearch;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.jason.myclass.R;

public class FinalsListAdapter extends BaseAdapter {
    Bundle mBundle;
    Context mContext;
    LayoutInflater mLayoutInflater;
    public FinalsListAdapter(Context context, int textViewResourceId, Bundle bundle) {
        mLayoutInflater = LayoutInflater.from(context);
        mBundle = bundle;
        mContext = context;
        Log.d("SectionListAdapter", "Course name is " + bundle.getString("courseName"));
    }


    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public int getCount() {
        if(mBundle.getStringArrayList("FINAL_SEC") == null) return 0;
        return mBundle.getStringArrayList("FINAL_SEC").size();
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

        sec.setText(mBundle.getStringArrayList("FINAL_SEC").get(position));
        time.setText(mBundle.getStringArrayList("FINAL_TIME").get(position));
        loc.setText(mBundle.getStringArrayList("FINAL_LOC").get(position));
        date.setText(mBundle.getStringArrayList("FINAL_DATE").get(position));


        return v;
    }

}