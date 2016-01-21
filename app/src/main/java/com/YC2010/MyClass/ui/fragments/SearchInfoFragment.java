package com.YC2010.MyClass.ui.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.YC2010.MyClass.R;

/**
 * Created by Danny on 2015/12/25.
 */
public class SearchInfoFragment extends Fragment {
    private Activity mActivity;
    private View mView;
    private Bundle mFetchedResult;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mActivity = getActivity();
        this.mFetchedResult = this.getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_search_info, container, false);
        updateView();

        return mView;
    }

    public void updateView() {
        TextView title = (TextView) mView.findViewById(R.id.title);
        TextView description = (TextView) mView.findViewById(R.id.description);

        TextView prerequisites = (TextView) mView.findViewById(R.id.preReq);
        TextView antirequisites = (TextView) mView.findViewById(R.id.antiReq);
        TextView prerequisitesLabel = (TextView) mView.findViewById(R.id.preReqLabel);
        TextView antirequisitesLabel = (TextView) mView.findViewById(R.id.antiReqLabel);

        title.setText(mFetchedResult.getString("title"));
        description.setText(mFetchedResult.getString("description"));

        String prerequisitesText = mFetchedResult.getString("prerequisites");
        String antirequisitesText = mFetchedResult.getString("antirequisites");

        if (prerequisitesText != null && prerequisitesText.length() > 0) {
            prerequisites.setText(prerequisitesText);
            prerequisites.setVisibility(View.VISIBLE);
            prerequisitesLabel.setVisibility(View.VISIBLE);
        }

        if (antirequisitesText != null && antirequisitesText.length() > 0) {
            antirequisites.setText(antirequisitesText);
            antirequisites.setVisibility(View.VISIBLE);
            antirequisitesLabel.setVisibility(View.VISIBLE);
        }
    }
}
