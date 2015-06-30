package com.example.jason.myclass.CourseSelect;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.jason.myclass.CourseSelect.ShowList.AsyncTaskCallbackInterface;
import com.example.jason.myclass.CourseSelect.ShowList.CatalogNumFetchTask;
import com.example.jason.myclass.R;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class CatalogNumFragment extends ListFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mSubject;

    ArrayList<String> mCatalogNum_arraylist;

    private OnFragmentInteractionListener mListener;

    // TODO: Rename and change types of parameters
    /*public static CatalogNumFragment newInstance(String param1, String param2) {
        CatalogNumFragment fragment = new CatalogNumFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CatalogNumFragment(String subject) {
        mSubject = subject;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        final CatalogNumFetchTask catalogNumFetchTask = new CatalogNumFetchTask(mSubject, new AsyncTaskCallbackInterface() {
            @Override
            public void onOperationComplete(Bundle bundle) {
                mCatalogNum_arraylist = bundle.getStringArrayList("CatalogNum_arraylist");
                setListAdapter(new ArrayAdapter<>(getActivity(),
                        R.layout.fragment_select_list, R.id.select_list_text, mCatalogNum_arraylist));
            }
        });
        catalogNumFetchTask.execute();


        //ArrayList<String> Subject_arraylist = subjectFetchTask.getSubject_arraylist();
        //ArrayList<String> Description_arraylist = subjectFetchTask.getDescription_arraylist();
        //Log.d("SubjectFragment", "Subject_arraylist 0 is " + Subject_arraylist.get(0));
    }


    /*@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Log.d("CatalogNumFragment", "item click: " + position);

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

}
