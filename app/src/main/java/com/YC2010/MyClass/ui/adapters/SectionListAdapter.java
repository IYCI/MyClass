package com.YC2010.MyClass.ui.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.YC2010.MyClass.model.LectureSectionObject;
import com.YC2010.MyClass.utils.Constants;
import com.YC2010.MyClass.R;

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

        View v = mLayoutInflater.inflate(R.layout.section_item, null);

        TextView capacity = (TextView) v.findViewById(R.id.section_item_capacity);
        TextView lec = (TextView) v.findViewById(R.id.section_item_lec);
        TextView loc = (TextView) v.findViewById(R.id.section_item_loc);
        TextView time = (TextView) v.findViewById(R.id.section_item_time);
        TextView prof = (TextView) v.findViewById(R.id.section_item_prof);
        RelativeLayout section_item_prof_layout = (RelativeLayout) v.findViewById((R.id.section_item_prof_layout));

        LectureSectionObject sectionInfo = getItem(position);
        String professor = sectionInfo.getProfessor();

        if (professor != null && professor.length() > 0) {
            prof.setText(professor);
            prof.setVisibility(View.VISIBLE);
            section_item_prof_layout.setVisibility(View.VISIBLE);
        } else {
            prof.setVisibility(View.GONE);
        }

        lec.setText(sectionInfo.getSection());
        time.setText(sectionInfo.getTime());
        loc.setText(sectionInfo.getLocation());

        int enroll_total = Integer.parseInt(sectionInfo.getTotal());
        int enroll_cap = Integer.parseInt(sectionInfo.getCapacity());

        if (enroll_total/enroll_cap >= 1) {
            capacity.setTextColor(mContext.getResources().getColor(R.color.fab_color_1));
        } else {
            capacity.setTextColor(mContext.getResources().getColor(R.color.light_green));
        }

        Log.d("SectionListAdapter", "rate is " + enroll_total/enroll_cap);

        capacity.setText(enroll_total + "/" + enroll_cap);

        return v;
    }

}