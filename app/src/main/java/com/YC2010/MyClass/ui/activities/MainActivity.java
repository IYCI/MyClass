package com.YC2010.MyClass.ui.activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.YC2010.MyClass.R;
import com.YC2010.MyClass.data.Connections;
import com.YC2010.MyClass.ui.fragments.CalendarFragment;
import com.YC2010.MyClass.ui.fragments.CoursesFragment;
import com.YC2010.MyClass.ui.fragments.ReminderFragment;
import com.YC2010.MyClass.ui.fragments.SearchFragment;
import com.YC2010.MyClass.ui.fragments.SettingFragment;
import com.YC2010.MyClass.ui.fragments.SubjectsFragment;
import com.YC2010.MyClass.utils.Constants;

import java.util.ArrayList;
//import com.facebook.AccessToken;
//import com.facebook.AccessTokenTracker;
//import com.facebook.CallbackManager;
//import com.facebook.FacebookCallback;
//import com.facebook.FacebookException;
//import com.facebook.FacebookSdk;
//import com.facebook.GraphRequest;
//import com.facebook.GraphResponse;
//import com.facebook.HttpMethod;
//import com.facebook.Profile;
//import com.facebook.ProfileTracker;
//import com.facebook.appevents.AppEventsLogger;
//import com.facebook.login.LoginManager;
//import com.facebook.login.LoginResult;
//import com.facebook.login.widget.LoginButton;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        AdapterView.OnItemSelectedListener{

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer_menu.
     */
    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;

    private CharSequence mTitle;
    private CharSequence currentTitle;
//    CallbackManager callbackManager;
//    AccessTokenTracker accessTokenTracker;
//    AccessToken accessToken;
//    ProfileTracker profileTracker;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_COURSE = "COURSE";
    private static final String TITLE_KEY = "title key";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        Log.d("MainActivity", "enter onCreate");
        super.onCreate(savedInstanceState);

        // update ALL terms in sharedPreference
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    ArrayList<Integer> termNumList = Connections.getTerms().getIntegerArrayList("KEY_TERM_NUM");
                    ArrayList<String> termNameList = Connections.getTerms().getStringArrayList("KEY_TERM_NAME");

                    if (termNumList != null) {
                        getSharedPreferences("TERMS", MODE_PRIVATE)
                                .edit()
                                .putInt("CURRENT_TERM", termNumList.get(1))
                                .putInt("NEXT_TERM", termNumList.get(2))
                                .apply();
                    }
                    if (termNameList != null) {
                        getSharedPreferences("TERMS", MODE_PRIVATE)
                                .edit()
                                .putString("CURRENT_TERM_NAME", termNameList.get(1) + " (Current)")
                                .putString("NEXT_TERM_NAME", termNameList.get(2) + " (Next)")
                                .apply();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

        }.execute();

        if (savedInstanceState != null){
            // get title out
            mTitle = savedInstanceState.getCharSequence(TITLE_KEY);
        }
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);

        if (savedInstanceState == null) Log.d("MainActivity", "savedInstanceState is null");
        else Log.d("MainActivity", "savedInstanceState is NOT null");

        //Initializing NavigationView
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        mNavigationView.setNavigationItemSelectedListener(this);

        // make the profile image as a circle
        ImageView profileImageContainer = (ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.profile_image);
        profileImageContainer.setImageDrawable((new RoundImage(BitmapFactory.decodeResource(getResources(), R.drawable.avatar))));

        // show calendar initially, before any drawer_menu item select
        if (savedInstanceState == null) {
            mNavigationView.getMenu().getItem(0).setChecked(true);
            mTitle = getString(R.string.title_MyClass);
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, new CalendarFragment())
                    .addToBackStack("0")
                    .commit();
            if (mToolbar != null) {
                mToolbar.setTitle(mTitle);
            }
        }

        // Initializing Drawer Layout and ActionBarToggle
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,mToolbar,R.string.drawer_openDrawer, R.string.drawer_closeDrawer){
            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer_menu closes as we don't want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer_menu open as we don't want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        // set up the term spinner
        Spinner termSpinner = (Spinner) mNavigationView.getHeaderView(0).findViewById(R.id.term_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayList<String> termStrings = new ArrayList<>();
        termStrings.add(0, getSharedPreferences("TERMS", MODE_PRIVATE).getString("CURRENT_TERM_NAME", Constants.defaultCurTermName));
        termStrings.add(1, getSharedPreferences("TERMS", MODE_PRIVATE).getString("NEXT_TERM_NAME", Constants.defaultNextTermName));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, termStrings);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        termSpinner.setAdapter(adapter);
        ArrayList<Integer> termInts = new ArrayList<>();
        termInts.add(0, getSharedPreferences("TERMS", MODE_PRIVATE).getInt("CURRENT_TERM", Constants.defaultCurTermNum));
        termInts.add(1, getSharedPreferences("TERMS", MODE_PRIVATE).getInt("NEXT_TERM", Constants.defaultNextTermNum));
        termSpinner.setSelection(termInts.indexOf(getSharedPreferences("TERMS", MODE_PRIVATE).getInt("TERM_NUM", termInts.get(0))));
        termSpinner.setOnItemSelectedListener(this);

        //Setting the actionbarToggle to drawer_menu layout
        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

        checkFirstRun();


//        /* Facebook stuff starts */
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        callbackManager = CallbackManager.Factory.create();
//
//        accessTokenTracker = new AccessTokenTracker() {
//            @Override
//            protected void onCurrentAccessTokenChanged(
//                    AccessToken oldAccessToken,
//                    AccessToken currentAccessToken) {
//                // Set the access token using
//                // currentAccessToken when it's loaded or set.
//            }
//        };
//        // If the access token is available already assign it.
//        accessToken = AccessToken.getCurrentAccessToken();
//        profileTracker = new ProfileTracker() {
//            @Override
//            protected void onCurrentProfileChanged(
//                    Profile oldProfile,
//                    Profile currentProfile) {
//                // App code
//            }
//        };
//        /* Facebook stuff ends */

    }

    @Override
    protected void onStop() {
        Log.d("MainActivity", "enter onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d("MainActivity", "enter onDestroy");
        //Toast.makeText(getApplicationContext(), "onDestroy", Toast.LENGTH_LONG).show();
        super.onDestroy();

        // Facebook Related Code
//        accessTokenTracker.stopTracking();
//        profileTracker.stopTracking();

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
//        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
//        AppEventsLogger.deactivateApp(this);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // convert stack into arraylist
        savedInstanceState.putCharSequence(TITLE_KEY, mTitle);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        // pop title and fragment stack
        Log.d("MainActivity", "stack count is " + getFragmentManager().getBackStackEntryCount());
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        else if (getFragmentManager().getBackStackEntryCount() <= 1) {
            moveTaskToBack(true);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // search button
        getMenuInflater().inflate(R.menu.main_activity_actions, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("MainActivity", "Text Submitted");
                MenuItemCompat.collapseActionView(searchItem);

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

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean queryTextFocused) {
                if (!queryTextFocused) {
                    MenuItemCompat.collapseActionView(searchItem);
                }
            }
        });

        // if calendar is shown, show calendar action overflow
        if(mNavigationView.getMenu().getItem(0).isChecked()) {
            getMenuInflater().inflate(R.menu.calender_action_overflow, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        if (id == R.id.Login_FB_setting){
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//            builder.setTitle("Log in to Facebook");
//            builder.setView(R.layout.facebook_login_dialog);
//
//            AlertDialog login_dialog = builder.create();
//            login_dialog.show();
//
//            LoginButton loginButton = (LoginButton) login_dialog.findViewById(R.id.login_button);
//            List<String> permissions = new ArrayList<String>();
//            permissions.add("user_friends");
//
//            LoginManager.getInstance().logInWithReadPermissions(
//                    this,
//                    Arrays.asList("user_events"));
//
//            loginButton.setReadPermissions(permissions);
//            // If using in a fragment
//            //loginButton.setFragment(new NativeFragmentWrapper(this));
//            // Other app specific specialization
//
//            // Callback registration
//            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//                @Override
//                public void onSuccess(LoginResult loginResult) {
//                    Log.d("MainActivity", "success");
//                    // App code
//                    Profile profile = Profile.getCurrentProfile();
//                    // change username
//                    Log.d("MainActivity", "got id is " + profile.getId() + " " + profile.getFirstName() +
//                            " " + profile.getLastName());
//
//                    String userID = profile.getId();
//
//                    new GraphRequest(
//                            AccessToken.getCurrentAccessToken(),
//                            "/" + userID + "/events",
//                            null,
//                            HttpMethod.GET,
//                            new GraphRequest.Callback() {
//                                public void onCompleted(GraphResponse response) {
//                            /* handle the result */
//                                    try {
//                                        Log.d("MainActivity", response.toString());
//                                        JSONArray eventsArray = response.getJSONObject().getJSONArray("data");
//                                        final ReminderDBHandler reminder_db = new ReminderDBHandler(getApplicationContext());
//                                        reminder_db.removeAll("fb");
//                                        Bundle parameters = new Bundle();
//                                        parameters.putString("fields", "id,name,start_time,place");
//                                        for (int i = 0; i < eventsArray.length(); i++) {
//                                            JSONObject mEvent = eventsArray.getJSONObject(i);
//                                            String eid = mEvent.getString("id");
//                                    /* make the API call */
//                                            GraphRequest request = new GraphRequest(
//                                                    AccessToken.getCurrentAccessToken(),
//                                                    "/" + eid,
//                                                    null,
//                                                    HttpMethod.GET,
//                                                    new GraphRequest.Callback() {
//                                                        public void onCompleted(GraphResponse response) {
//                                                    /* handle the result */
//                                                            try {
//                                                                //Log.d("MainActivity", response.toString());
//                                                                JSONObject event = response.getJSONObject();
//                                                                String loc = event.getJSONObject("place").getString("name");
//                                                                //String loc = "";
//                                                                String title = event.getString("name");
//                                                                String start_time = event.getString("start_time");
//                                                                DateFormat f1 = new SimpleDateFormat("yyyy-MM-dd'T'h:mm:ssZ", Locale.CANADA);
//                                                                Date d = f1.parse(start_time);
//                                                                final Reminder_item new_fb_events = new Reminder_item(UUID.randomUUID().toString(), title, loc, d.getTime(), "fb");
//
//                                                                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                                                                Boolean exam_warning = sharedPref.getBoolean("pref_key_exam_warning", true);
//
//                                                                if (exam_warning && reminder_db.closoToExam(d)){
//                                                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                                                                    builder.setMessage(title + " has exams followed by within a week, do you still want to create this event?")
//                                                                            .setTitle("Event Warning");
//                                                                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                                                                        @Override
//                                                                        public void onClick(DialogInterface dialogInterface, int i) {
//                                                                            reminder_db.addReminder(new_fb_events);
//
//                                                                        }
//                                                                    });
//                                                                    builder.setNegativeButton("NO", null);
//                                                                    AlertDialog dialog = builder.create();
//                                                                    dialog.show();
//
//
//                                                                }
//                                                                else
//                                                                    reminder_db.addReminder(new_fb_events);
//
//                                                            } catch (org.json.JSONException e) {
//                                                                e.printStackTrace();
//                                                            } catch (java.text.ParseException p) {
//                                                                p.printStackTrace();
//                                                            }
//                                                        }
//                                                    }
//                                            );
//                                            request.setParameters(parameters);
//                                            request.executeAsync();
//                                        }
//                                        reminder_db.close();
//                                    } catch (org.json.JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }
//                    ).executeAsync();
//                }
//
//                @Override
//                public void onCancel() {
//                    Log.d("MainActivity", "cancel");
//                    // App code
//                }
//
//                @Override
//                public void onError(FacebookException exception) {
//                    Log.d("MainActivity", "error");
//                    // App code
//                }
//            });
//
//            login_dialog.dismiss();
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("MainActivity", "got result");
        super.onActivityResult(requestCode, resultCode, data);
//        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public static class RoundImage extends Drawable {
        private final Bitmap mBitmap;
        private final Paint mPaint;
        private final RectF mRectF;
        private final int mBitmapWidth;
        private final int mBitmapHeight;

        public RoundImage(Bitmap bitmap) {
            mBitmap = bitmap;
            mRectF = new RectF();
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setDither(true);
            final BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            mPaint.setShader(shader);

            mBitmapWidth = mBitmap.getWidth();
            mBitmapHeight = mBitmap.getHeight();
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.drawOval(mRectF, mPaint);
        }

        @Override
        protected void onBoundsChange(Rect bounds) {
            super.onBoundsChange(bounds);
            mRectF.set(bounds);
        }

        @Override
        public void setAlpha(int alpha) {
            if (mPaint.getAlpha() != alpha) {
                mPaint.setAlpha(alpha);
                invalidateSelf();
            }
        }

        @Override
        public void setColorFilter(ColorFilter cf) {
            mPaint.setColorFilter(cf);
        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSLUCENT;
        }

        @Override
        public int getIntrinsicWidth() {
            return mBitmapWidth;
        }

        @Override
        public int getIntrinsicHeight() {
            return mBitmapHeight;
        }

        public void setAntiAlias(boolean aa) {
            mPaint.setAntiAlias(aa);
            invalidateSelf();
        }

        @Override
        public void setFilterBitmap(boolean filter) {
            mPaint.setFilterBitmap(filter);
            invalidateSelf();
        }

        @Override
        public void setDither(boolean dither) {
            mPaint.setDither(dither);
            invalidateSelf();
        }

        public Bitmap getBitmap() {
            return mBitmap;
        }
    }



    /*** Here begins all listeners ***/

    /**************** implements NavigationView.OnNavigationItemSelectedListener ****************/
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        Log.d("MainActivity", "onNavigationItemSelected");
        mDrawerLayout.closeDrawers();

        //Checking if the item is in checked state or not, if not make it in checked state
        if (menuItem.isChecked()){
            //Closing drawer_menu on item click
            return true;
        }

        menuItem.setChecked(true);

        //Check to see which item was being clicked and perform appropriate action
        switch (menuItem.getItemId()) {
            case R.id.drawer_Calender:
                mTitle = getString(R.string.title_MyClass);
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, new CalendarFragment())
                        .addToBackStack("0")
                        .commit();
                break;
            case R.id.drawer_Courses:
                mTitle = getString(R.string.title_Courses);
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, new CoursesFragment())
                        .addToBackStack("1")
                        .commit();
                break;
            case R.id.drawer_Course_Select:
                mTitle = getString(R.string.title_Course_Select);
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, new SubjectsFragment())
                        .addToBackStack("2")
                        .commit();
                break;
            case R.id.drawer_Reminder:
                mTitle = getString(R.string.title_Reminder);
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, new ReminderFragment())
                        .addToBackStack("3")
                        .commit();
                break;
            case R.id.drawer_Settings:
                mTitle = getString(R.string.title_Settings);
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, new SettingFragment())
                        .addToBackStack("4")
                        .commit();
                break;
            default:
                Toast.makeText(getApplicationContext(), "undefine drawer item selected, please report to the developer", Toast.LENGTH_LONG).show();
                break;
        }

        if (mToolbar != null) {
            mToolbar.setTitle(mTitle);
        }
        return true;
    }


    /**************** AdapterView.OnItemSelectedListener(Term Selection) ****************/
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        SharedPreferences termSharedPreferences = getSharedPreferences("TERMS", MODE_PRIVATE);
        // search for term id and add it into shared preference
        int selectedTermID = 200;
        Log.d("MainActivity", "selected item is " + parent.getItemAtPosition(position) + ", " + termSharedPreferences.getString("CURRENT_TERM_NAME", Constants.defaultCurTermName));
        if (parent.getItemAtPosition(position).equals(termSharedPreferences.getString("CURRENT_TERM_NAME", Constants.defaultCurTermName))){
            selectedTermID = termSharedPreferences.getInt("CURRENT_TERM", Constants.defaultCurTermNum);
        }
        if (parent.getItemAtPosition(position).equals(termSharedPreferences.getString("NEXT_TERM_NAME", Constants.defaultNextTermName))){
            selectedTermID = termSharedPreferences.getInt("NEXT_TERM", Constants.defaultNextTermNum);
        }
        Log.d("MainActivity", "selectedTermID is " + selectedTermID);

        if (termSharedPreferences.getInt("TERM_NUM", -1) ==  -1){
            termSharedPreferences.edit().putInt("TERM_NUM", selectedTermID).apply();
        }
        else if (termSharedPreferences.getInt("TERM_NUM", -1) != selectedTermID) {
            termSharedPreferences.edit().putInt("TERM_NUM", selectedTermID).apply();

            // get back to the calender view
            mDrawerLayout.closeDrawers();
            mNavigationView.getMenu().getItem(0).setChecked(true);
            mTitle = getString(R.string.title_MyClass);
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, new CalendarFragment())
                    .addToBackStack("0")
                    .commit();
            if (mToolbar != null) {
                mToolbar.setTitle(mTitle);
            }

            Toast.makeText(getApplicationContext(), "Change term to " + parent.getItemAtPosition(position),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
