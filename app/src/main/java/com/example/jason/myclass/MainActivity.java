package com.example.jason.myclass;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jason.myclass.CourseSearch.SearchFragment;
import com.example.jason.myclass.CourseSelect.SubjectsFragment;
import com.example.jason.myclass.Courses.CoursesDBHandler;
import com.example.jason.myclass.Courses.CoursesFragment;
import com.example.jason.myclass.Courses.Schedule;
import com.example.jason.myclass.NavigationBar.NavigationDrawerCallbacks;
import com.example.jason.myclass.NavigationBar.NavigationDrawerFragment;
import com.example.jason.myclass.Reminder.ReminderFragment;


public class MainActivity extends AppCompatActivity
        implements NavigationDrawerCallbacks{

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Toolbar mToolbar;
    private CharSequence mTitle;
    private CharSequence currentTitle;

    private static final String TITLE_KEY = "title key";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        Log.d("MainActivity", "enter onCreate");
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null){
            // get title out
            mTitle = savedInstanceState.getCharSequence(TITLE_KEY);
        }
        // push MyClass into mTitleStack
        //mTitle = getString(R.string.app_name);
        //mTitleStack.push(mTitle);

        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);

        if (savedInstanceState == null) Log.d("MainActivity", "savedInstanceState is null");
        else Log.d("MainActivity", "savedInstanceState is NOT null");

        //if (savedInstanceState == null) {

            mNavigationDrawerFragment = (NavigationDrawerFragment)
                    getFragmentManager().findFragmentById(R.id.fragment_drawer);



            // Set up the drawer.
            mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);
            // populate the navigation drawer
            mNavigationDrawerFragment.setUserData("Guest", "guest@miao.com", BitmapFactory.decodeResource(getResources(), R.drawable.avatar));

        //}

        checkFirstRun();
    }

    @Override
    protected void onStop() {
        Log.d("MainActivity", "enter onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d("MainActivity", "enter onDestroy");
        Toast.makeText(getApplicationContext(), "onDestroy", Toast.LENGTH_LONG)
                .show();
        //getFragmentManager().beginTransaction().remove(mNavigationDrawerFragment).commit();
        super.onDestroy();
        /*getFragmentManager().beginTransaction()
                .remove(mNavigationDrawerFragment)
                .commit();*/

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // convert stack into arraylist
        savedInstanceState.putCharSequence(TITLE_KEY, mTitle);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        if (position == 0) {
            if (mTitle == null || !mTitle.equals(getString(R.string.title_MyClass))) {
                mTitle = getString(R.string.title_MyClass);

                getFragmentManager().beginTransaction()
                        .replace(R.id.container, new CalendarFragment())
                        .addToBackStack("0")
                        .commit();

                if (null != mToolbar) {
                    mToolbar.setTitle(mTitle);
                }
            }
        }
        if (position == 1 && !mTitle.equals(getString(R.string.title_Courses))) {
            mTitle = getString(R.string.title_Courses);
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, new CoursesFragment())
                    .addToBackStack("1")
                    .commit();
            if (null != mToolbar) {
                mToolbar.setTitle(mTitle);
            }
        }
        if (position == 2 && !mTitle.equals(getString(R.string.title_Settings))) {
            mTitle = getString(R.string.title_Course_Select);

            getFragmentManager().beginTransaction()
                    .replace(R.id.container, new SubjectsFragment())
                    .addToBackStack("2")
                    .commit();
            if (null != mToolbar) {
                mToolbar.setTitle(mTitle);
            }
        }
        if (position == 3 && !mTitle.equals(getString(R.string.title_Reminder))) {
            mTitle = getString(R.string.title_Reminder);

            getFragmentManager().beginTransaction()
                    .replace(R.id.container, new ReminderFragment())
                    .addToBackStack("3")
                    .commit();
            if (null != mToolbar) {
                mToolbar.setTitle(mTitle);
            }
        }
        if (position == 4 && !mTitle.equals(getString(R.string.title_Settings))) {
            mTitle = getString(R.string.title_Settings);

            getFragmentManager().beginTransaction()
                    .replace(R.id.container, new SettingFragment())
                    .addToBackStack("4")
                    .commit();
            if (null != mToolbar) {
                mToolbar.setTitle(mTitle);
            }
        }




    }


    @Override
    public void onBackPressed() {
        // pop title and fragment stack

        // there is an ERROR --->
        /*CharSequence currentTitle = mTitleStack.peek();
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
        }*/
        super.onBackPressed();
    }

    public void checkFirstRun() {
        SharedPreferences sp = getSharedPreferences("PREFERENCE", MODE_PRIVATE);
        boolean isFirstRun = sp.getBoolean("isFirstRun", true);
        if (isFirstRun){
            // display the dialog
            showImportDialog();
            sp.edit()
                    .putBoolean("isFirstRun", false)
                    .apply();
        }
    }

    public void showImportDialog() {
        final Dialog d = new Dialog(MainActivity.this);
        d.setTitle(R.string.import_dialog_title);
        d.setContentView(R.layout.dialog_import);
        Button import_cal_btn = (Button) d.findViewById(R.id.import_cal_btn);

        final EditText editText = (EditText) d.findViewById(R.id.editText);



        import_cal_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // set the text display the same as editText
                //  test with whatever value u want
                //  just replace editText.getText() with your charsequence
                Editable QuestData = editText.getText();
                Log.d("try to print this", QuestData.toString());

                /** constructor of Schedule*/
                Schedule schedule = new Schedule(QuestData.toString());
                /** need to check valid of class Schedule then decide to raise exception
                 *  or go on displaying */
                Log.d("after making schedule","");
                // insert into db
                if(schedule.getValidity()) {
                    Log.d("set onlcick", "input is valid");
                    final CoursesDBHandler db = new CoursesDBHandler(getApplicationContext());
                    db.addSchedule(schedule);
                }
                else{
                    // show snackBar
                    Snackbar.make(v, "Invalid input, please follow the instruction and try again", Snackbar.LENGTH_SHORT)
                            .show();
                }

                d.dismiss();
                // show snackBar
                final View CalenderView = findViewById(R.id.course_fragment);
                Snackbar.make(CalenderView, "Courses Updated", Snackbar.LENGTH_SHORT)
                        .show();
            }
        });


        d.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.

            if(mNavigationDrawerFragment.getPositionSelected() == 0) {
                getMenuInflater().inflate(R.menu.calender_action_overflow, menu);
            }
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
        if (id == R.id.Import_Courses_setting) {
            showImportDialog();
            return true;
        }
        // TODO: Need to intergrate with search interface
        if (id == R.id.Search_setting) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, new SearchFragment("CS446"))
                    .addToBackStack("5")
                    .commit();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }



}
