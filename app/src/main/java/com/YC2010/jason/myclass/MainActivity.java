package com.YC2010.jason.myclass;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.YC2010.jason.myclass.CourseSearch.SearchFragment;
import com.YC2010.jason.myclass.CourseSelect.SubjectsFragment;
import com.YC2010.jason.myclass.Courses.CoursesDBHandler;
import com.YC2010.jason.myclass.Courses.CoursesFragment;
import com.YC2010.jason.myclass.Courses.Schedule;
import com.YC2010.jason.myclass.NavigationBar.NavigationDrawerCallbacks;
import com.YC2010.jason.myclass.NavigationBar.NavigationDrawerFragment;
import com.YC2010.jason.myclass.Reminder.ReminderDBHandler;
import com.YC2010.jason.myclass.Reminder.ReminderFragment;
import com.YC2010.jason.myclass.Reminder.Reminder_item;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;


public class MainActivity extends AppCompatActivity
        implements NavigationDrawerCallbacks{

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Toolbar mToolbar;
    private CharSequence mTitle;
    private CharSequence currentTitle;
    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    AccessToken accessToken;
    ProfileTracker profileTracker;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_COURSE = "COURSE";
    private static final String TITLE_KEY = "title key";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        Log.d("MainActivity", "enter onCreate");
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null){
            // get title out
            mTitle = savedInstanceState.getCharSequence(TITLE_KEY);
        }

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

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.
            }
        };
        // If the access token is available already assign it.
        accessToken = AccessToken.getCurrentAccessToken();


        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {
                // App code
            }
        };

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
        super.onDestroy();

        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
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
        if (position == 2 && !mTitle.equals(getString(R.string.title_Course_Select))) {
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

        Log.d("MainActivity", "stack count is " + getFragmentManager().getBackStackEntryCount());
        if (mNavigationDrawerFragment.isDrawerOpen()) {
            mNavigationDrawerFragment.closeDrawer();
        }
        else if (getFragmentManager().getBackStackEntryCount() <= 1) {
            moveTaskToBack(true);
            //onStop();
            // finish();
        }
        else{
            FragmentManager.BackStackEntry backEntry=getFragmentManager().getBackStackEntryAt(getFragmentManager().getBackStackEntryCount()-1);
            String str = backEntry.getName();
            if(str.equals("0") || str.equals("1") || str.equals("2") || str.equals("3") || str.equals("4"))
                moveTaskToBack(true);
            else if(!str.equals("0") && !str.equals("1") && !str.equals("2") && !str.equals("3") && !str.equals("4"))
                getFragmentManager().popBackStackImmediate();
        }
    }

    public void checkFirstRun() {
        SharedPreferences sp = getSharedPreferences("PREFERENCE", MODE_PRIVATE);
        boolean isFirstRun = sp.getBoolean("isFirstRun", true);
        if (isFirstRun){
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

            // search button
            getMenuInflater().inflate(R.menu.main_activity_actions, menu);
            MenuItem searchItem = menu.findItem(R.id.action_search);
            SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.d("MainActivity", "Text Submitted");
                    // pass query into the fragment through a bundle
                    SearchFragment mSearchFragment = new SearchFragment();
                    Bundle args = new Bundle();
                    args.putString(ARG_COURSE, query);
                    mSearchFragment.setArguments(args);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.container, mSearchFragment)
                            .addToBackStack("7")
                            .commit();
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });

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
        else if(id == R.id.Login_FB_setting){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Log in to Facebook");
            builder.setView(R.layout.facebook_login_dialog);

            AlertDialog login_dialog = builder.create();
            login_dialog.show();

            LoginButton loginButton = (LoginButton) login_dialog.findViewById(R.id.login_button);
            List<String> permissions = new ArrayList<String>();
            permissions.add("user_friends");

            LoginManager.getInstance().logInWithReadPermissions(
                    this,
                    Arrays.asList("user_events"));

            loginButton.setReadPermissions(permissions);
            // If using in a fragment
            //loginButton.setFragment(new NativeFragmentWrapper(this));
            // Other app specific specialization

            // Callback registration
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    Log.d("MainActivity", "success");
                    // App code
                    Profile profile = Profile.getCurrentProfile();
                    // change username
                    Log.d("MainActivity", "got id is " + profile.getId() + " " + profile.getFirstName() +
                            " " + profile.getLastName());

                    String userID = profile.getId();

                    new GraphRequest(
                            AccessToken.getCurrentAccessToken(),
                            "/" + userID + "/events",
                            null,
                            HttpMethod.GET,
                            new GraphRequest.Callback() {
                                public void onCompleted(GraphResponse response) {
                            /* handle the result */
                                    try {
                                        Log.d("MainActivity", response.toString());
                                        JSONArray eventsArray = response.getJSONObject().getJSONArray("data");
                                        final ReminderDBHandler reminder_db = new ReminderDBHandler(getApplicationContext());
                                        reminder_db.removeAll("fb");
                                        Bundle parameters = new Bundle();
                                        parameters.putString("fields", "id,name,start_time,place");
                                        for (int i = 0; i < eventsArray.length(); i++) {
                                            JSONObject mEvent = eventsArray.getJSONObject(i);
                                            String eid = mEvent.getString("id");
                                    /* make the API call */
                                            GraphRequest request = new GraphRequest(
                                                    AccessToken.getCurrentAccessToken(),
                                                    "/" + eid,
                                                    null,
                                                    HttpMethod.GET,
                                                    new GraphRequest.Callback() {
                                                        public void onCompleted(GraphResponse response) {
                                                    /* handle the result */
                                                            try {
                                                                //Log.d("MainActivity", response.toString());
                                                                JSONObject event = response.getJSONObject();
                                                                String loc = event.getJSONObject("place").getString("name");
                                                                //String loc = "";
                                                                String title = event.getString("name");
                                                                String start_time = event.getString("start_time");
                                                                DateFormat f1 = new SimpleDateFormat("yyyy-MM-dd'T'h:mm:ssZ", Locale.CANADA);
                                                                Date d = f1.parse(start_time);
                                                                final Reminder_item new_fb_events = new Reminder_item(UUID.randomUUID().toString(), title, loc, d.getTime(), "fb");

                                                                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                                                Boolean exam_warning = sharedPref.getBoolean("pref_key_exam_warning", true);

                                                                if (exam_warning && reminder_db.closoToExam(d)){
                                                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                                                    builder.setMessage(title + " has exams followed by within a week, do you still want to create this event?")
                                                                            .setTitle("Event Warning");
                                                                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                                            reminder_db.addReminder(new_fb_events);

                                                                        }
                                                                    });
                                                                    builder.setNegativeButton("NO", null);
                                                                    AlertDialog dialog = builder.create();
                                                                    dialog.show();


                                                                }
                                                                else
                                                                    reminder_db.addReminder(new_fb_events);

                                                            } catch (org.json.JSONException e) {
                                                                e.printStackTrace();
                                                            } catch (java.text.ParseException p) {
                                                                p.printStackTrace();
                                                            }
                                                        }
                                                    }
                                            );
                                            request.setParameters(parameters);
                                            request.executeAsync();
                                        }
                                        reminder_db.close();
                                    } catch (org.json.JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                    ).executeAsync();
                }

                @Override
                public void onCancel() {
                    Log.d("MainActivity", "cancel");
                    // App code
                }

                @Override
                public void onError(FacebookException exception) {
                    Log.d("MainActivity", "error");
                    // App code
                }
            });

            login_dialog.dismiss();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("MainActivity", "got result");
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
