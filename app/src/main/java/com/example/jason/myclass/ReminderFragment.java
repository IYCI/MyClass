package com.example.jason.myclass;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Jason on 2015-05-25.
 */
public class ReminderFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //return inflater.inflate(R.layout.fragment_reminder, container, false);

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_reminder, container, false);

        // Make this {@link Fragment} listen for changes in both FABs.
        //FloatingActionButton fab1 = (FloatingActionButton) rootView.findViewById(R.id.fab_1);
        //fab1.setOnCheckedChangeListener(this);



        // RecyclerView:
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.reminder_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // get reminder from db
        ReminderDBHandler db = new ReminderDBHandler(getActivity());
        List<Reminder_item> myDataset = db.getAllReminders();

        //String[] myDataset = {"123\nsdfsdf","345\n123","456\n123"};

        // specify an adapter (see also next example)
        mAdapter = new Reminder_Adapter(myDataset, getActivity());

        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                Log.d("ReminderFragment", "Data set changed");
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }
}
