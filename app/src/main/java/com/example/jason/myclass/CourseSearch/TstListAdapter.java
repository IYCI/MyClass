package com.example.jason.myclass.CourseSearch;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jason.myclass.R;

public class TstListAdapter extends BaseAdapter {
    Bundle mBundle;
    Context mContext;
    LayoutInflater mLayoutInflater;
    public TstListAdapter(Context context, int textViewResourceId, Bundle bundle) {
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
        if(mBundle.getStringArrayList("TST_SEC") == null) return 0;
        return mBundle.getStringArrayList("TST_SEC").size();
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
        ImageView star = (ImageView) v.findViewById(R.id.section_item_add);

        loc.setVisibility(View.GONE);
        prof.setVisibility(View.GONE);
        star.setVisibility(View.GONE);

        lec.setText(mBundle.getStringArrayList("TST_SEC").get(position));
        time.setText(mBundle.getStringArrayList("TST_TIME").get(position));
        //prof.setText(mBundle.getStringArrayList("LEC_PROF").get(position));
        //loc.setText(mBundle.getStringArrayList("LEC_LOC").get(position));

        capacity.setText(mBundle.getStringArrayList("TST_TOTAL").get(position) + "/" +
                         mBundle.getStringArrayList("TST_CAPACITY").get(position));


        return v;
    }

}