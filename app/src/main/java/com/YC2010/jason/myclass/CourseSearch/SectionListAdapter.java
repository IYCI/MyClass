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

import com.YC2010.jason.myclass.Constants;
import com.YC2010.jason.myclass.DataObjects.LectureSectionObject;
import com.YC2010.jason.myclass.R;

import java.util.ArrayList;

public class SectionListAdapter extends BaseAdapter {
    ArrayList<LectureSectionObject> mArrayList;
    Context mContext;
    LayoutInflater mLayoutInflater;

    public SectionListAdapter(Context context, int textViewResourceId, Bundle bundle) {
        mLayoutInflater = LayoutInflater.from(context);
        mArrayList = bundle.getParcelableArrayList(Constants.lectureSectionObjectListKey);
        mContext = context;
        Log.d("SectionListAdapter", "Course name is " + bundle.getString("courseName"));
    }

    @Override
    public LectureSectionObject getItem(int i) {
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

        TextView capacity = (TextView) v.findViewById(R.id.section_item_capacity);
        TextView lec = (TextView) v.findViewById(R.id.section_item_lec);
        TextView loc = (TextView) v.findViewById(R.id.section_item_loc);
        TextView time = (TextView) v.findViewById(R.id.section_item_time);
        TextView prof = (TextView) v.findViewById(R.id.section_item_prof);
        RelativeLayout section_item_prof_layout = (RelativeLayout) v.findViewById((R.id.section_item_prof_layout));
        section_item_prof_layout.setVisibility(View.VISIBLE);

        LectureSectionObject sectionInfo = getItem(position);

        lec.setText(sectionInfo.getSection());
        time.setText(sectionInfo.getTime());
        prof.setText(sectionInfo.getProfessor());
        loc.setText(sectionInfo.getLocation());

        int enroll_total = Integer.parseInt(sectionInfo.getTotal());
        int enroll_cap = Integer.parseInt(sectionInfo.getCapacity());

        if(enroll_total/enroll_cap >= 1)
            capacity.setTextColor(mContext.getResources().getColor(R.color.fab_color_1));
        else
            capacity.setTextColor(mContext.getResources().getColor(R.color.light_green));

        Log.d("SectionListAdapter", "rate is " + enroll_total/enroll_cap);

        capacity.setText(enroll_total + "/" + enroll_cap);

        return v;
    }

}