package com.example.jason.myclass;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Stack;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Toolbar mToolbar;
    private CharSequence mTitle;
    private Stack<CharSequence> mTitleStack;
    private CharSequence currentTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTitleStack = new Stack<>();
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);

        if (savedInstanceState == null) {
            mNavigationDrawerFragment = (NavigationDrawerFragment)
                    getFragmentManager().findFragmentById(R.id.fragment_drawer);

            // push MyClass into mTitleStack
            mTitle = getString(R.string.app_name);
            mTitleStack.push(mTitle);

            // Set up the drawer.
            mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);
            // populate the navigation drawer
            mNavigationDrawerFragment.setUserData("Guest", "guest@miao.com", BitmapFactory.decodeResource(getResources(), R.drawable.avatar));

        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        if (position == 0) {
            if (mTitleStack.size() == 0 || !mTitleStack.peek().toString().equals(getString(R.string.title_MyClass))) {
                mTitle = getString(R.string.title_MyClass);

                getFragmentManager().beginTransaction()
                        .replace(R.id.container, new CalendarFragment())
                        .addToBackStack("0")
                        .commit();

                mTitleStack.push(mTitle);
                if (null != mToolbar) {
                    mToolbar.setTitle(mTitle);
                }
            }

        }

        if (position == 1 && !mTitleStack.peek().toString().equals(getString(R.string.title_Reminder))) {
            mTitle = getString(R.string.title_Reminder);

            getFragmentManager().beginTransaction()
                    .replace(R.id.container, new ReminderFragment())
                    .addToBackStack("1")
                    .commit();

            mTitleStack.push(mTitle);
            mToolbar.setTitle(mTitle);
        }
        if (position == 2 && !mTitleStack.peek().toString().equals(getString(R.string.title_Settings))){
            mTitle = getString(R.string.title_Settings);

            getFragmentManager().beginTransaction()
                    .replace(R.id.container, new SettingFragment())
                    .addToBackStack("2")
                    .commit();

            mTitleStack.push(mTitle);
            mToolbar.setTitle(mTitle);
        }




    }


    @Override
    public void onBackPressed() {
        // pop title and fragment stack
        CharSequence currentTitle = mTitleStack.peek();
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else if(mTitleStack.peek().toString().equals(getString(R.string.title_MyClass)))
            super.onBackPressed();
        else if (getFragmentManager().getBackStackEntryCount() > 1 ){
            getFragmentManager().popBackStack();
            mTitleStack.pop();

            mToolbar.setTitle(currentTitle);
        } else {
            super.onBackPressed();
        }

        // change navigation drawer selected
        if (currentTitle.toString().equals(getString(R.string.title_MyClass))) {
            onNavigationDrawerItemSelected(0);
        } else if (currentTitle.toString().equals(getString(R.string.title_Reminder))) {
            onNavigationDrawerItemSelected(1);
        } else {
            onNavigationDrawerItemSelected(2);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
