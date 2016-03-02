package com.YC2010.MyClass.ui.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.YC2010.MyClass.R;
import com.YC2010.MyClass.ui.fragments.SearchFragment;
import com.YC2010.MyClass.utils.SampleRecentSuggestionsProvider;

public class SearchableActivity extends AppCompatActivity {
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        if (mToolbar != null) {
            mToolbar.setTitle("Course Search Result");
        }

        Intent intent = getIntent();
        Log.d("SearchableActivity", "Query");
        handleIntent(intent);
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            Log.d("SearchableActivity", "Query is" + query);
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    SampleRecentSuggestionsProvider.AUTHORITY,
                    SampleRecentSuggestionsProvider.MODE);
            suggestions.saveRecentQuery(query, null);

            doQuery(query);
        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Log.d("SearchableActivity", "Query is action");
            Uri detailUri = intent.getData();
            String query = detailUri.getLastPathSegment();

            doQuery(query);
        }
    }


    public void doQuery(String query) {
        SearchFragment mSearchFragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString("COURSE", query);

        mSearchFragment.setArguments(args);
        getFragmentManager().beginTransaction()
                .replace(R.id.container, mSearchFragment)
                .addToBackStack("7")
                .commit();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        /*getMenuInflater().inflate(R.menu.main_activity_actions, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);*/

        return true;
    }
}
