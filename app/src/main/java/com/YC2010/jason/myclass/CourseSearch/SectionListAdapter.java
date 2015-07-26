package com.YC2010.jason.myclass.CourseSearch;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.YC2010.jason.myclass.R;

public class SectionListAdapter extends BaseAdapter {
    Bundle mBundle;
    Context mContext;
    LayoutInflater mLayoutInflater;
    public SectionListAdapter(Context context, int textViewResourceId, Bundle bundle) {
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
        if(mBundle.getStringArrayList("LEC_SEC") == null) return 0;
        return mBundle.getStringArrayList("LEC_SEC").size();
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

        TextView capacity = (TextView) v.findViewById(R.id.section_item_capacity);
        TextView lec = (TextView) v.findViewById(R.id.section_item_lec);
        TextView loc = (TextView) v.findViewById(R.id.section_item_loc);
        TextView time = (TextView) v.findViewById(R.id.section_item_time);
        TextView prof = (TextView) v.findViewById(R.id.section_item_prof);
        RelativeLayout section_item_prof_layout = (RelativeLayout) v.findViewById((R.id.section_item_prof_layout));
        section_item_prof_layout.setVisibility(View.VISIBLE);

        lec.setText(mBundle.getStringArrayList("LEC_SEC").get(position));
        time.setText(mBundle.getStringArrayList("LEC_TIME").get(position));
        prof.setText(mBundle.getStringArrayList("LEC_PROF").get(position));
        loc.setText(mBundle.getStringArrayList("LEC_LOC").get(position));

        int enroll_total = Integer.parseInt(mBundle.getStringArrayList("LEC_TOTAL").get(position));
        int enroll_cap = Integer.parseInt(mBundle.getStringArrayList("LEC_CAPACITY").get(position));
        if(enroll_total/enroll_cap >= 1)
            capacity.setTextColor(mContext.getResources().getColor(R.color.fab_color_1));
        else
            capacity.setTextColor(mContext.getResources().getColor(R.color.light_green));
        Log.d("SectionListAdapter", "rate is " + enroll_total/enroll_cap);
        capacity.setText(mBundle.getStringArrayList("LEC_TOTAL").get(position) + "/" +
        mBundle.getStringArrayList("LEC_CAPACITY").get(position));


        return v;
    }

}