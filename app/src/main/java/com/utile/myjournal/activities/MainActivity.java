package com.utile.myjournal.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.utile.myjournal.NotificationHelper;
import com.utile.myjournal.R;
import com.utile.myjournal.adapters.images_adapter;
import com.utile.myjournal.database.Constants;
import com.utile.myjournal.database.Entries;
import com.utile.myjournal.database.MyDB;
import com.utile.myjournal.database.Notes;
import com.utile.myjournal.database.notesDB;
import com.utile.myjournal.fragments.CalenderFragment;
import com.utile.myjournal.fragments.ChangePINFragment;
import com.utile.myjournal.fragments.EntryAndNotesFragment;
import com.utile.myjournal.fragments.EntryFragment;
import com.utile.myjournal.fragments.FavouritesFragment;
import com.utile.myjournal.fragments.ImpNotesFragment;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import eightbitlab.com.blurview.BlurView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static BlurView blurView;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private RelativeLayout imgNavHeaderBg;
    private CircularImageView imgProfile;
    private TextView goodText, no_of_entries;
    private Toolbar toolbar;
    public static FloatingActionButton fab;
    public static boolean toggle = false;
    public static int navItemIndex = 0;
    private static final String TAG_ENTRY = "entry";
    private static final String TAG_CALENDERVIEW = "calender_view";
    private static final String TAG_FAVOURITES = "favourites";
    private static final String TAG_CHANGEPIN = "change_pin";
    private static final String TAG_IMPORTANT_NOTES = "imps";
    public static String CURRENT_TAG = TAG_ENTRY;
    private Handler handler;
    public static TextView title_image;
    private NavigationView imagesNavigationView;
    public static InterstitialAd mInterstitialAd;
    public static View imageNavigationButton;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-6900171412533642~9756488611");

        mInterstitialAd = new InterstitialAd(this);

        mInterstitialAd.setAdUnitId(getString(R.string.banner_ad_unit_id));

        requestNewInterstitial();

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.statusColor));

        //      getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
        //            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("");

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float drawerWidth = dm.widthPixels;

        handler = new Handler();
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        imagesNavigationView = findViewById(R.id.images_drawer);
        fab = findViewById(R.id.fab);
        navigationView.setNavigationItemSelectedListener(this);
        View navHeader = navigationView.getHeaderView(0);
        goodText = navHeader.findViewById(R.id.good);
        no_of_entries = navHeader.findViewById(R.id.no_of_entries);
        imgNavHeaderBg = navHeader.findViewById(R.id.about);
        imgProfile = navHeader.findViewById(R.id.userDP);

        title_image = toolbar.findViewById(R.id.actionBarTitle);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/lucida_handwriting.ttf");
        title_image.setTypeface(font);
        title_image.setText("Journal");
        imageNavigationButton = findViewById(R.id.imagesNavigationButton);
        imageNavigationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.END);
            }
        });
        DrawerLayout.LayoutParams lp = (DrawerLayout.LayoutParams) navigationView.getLayoutParams();
        lp.width = (int) (drawerWidth * 4 / 5);
        navigationView.setLayoutParams(lp);
        lp = (DrawerLayout.LayoutParams) imagesNavigationView.getLayoutParams();
        lp.width = (int) drawerWidth;
        imagesNavigationView.setLayoutParams(lp);

        sp = PreferenceManager.getDefaultSharedPreferences(this);

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                Intent i;
                if (toggle) {
                    i = new Intent(MainActivity.this, NotesActivity.class);
                    i.putExtra("type", false);
                } else {
                    i = new Intent(MainActivity.this, enterEntryActivity.class);
                    i.putExtra("ref_value", 0);
                }

                startActivity(i);
                finish();
            }
        });
        //intro();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Intent i;
                    if (toggle) {
                        i = new Intent(MainActivity.this, NotesActivity.class);
                        i.putExtra("type", false);
                    } else {
                        i = new Intent(MainActivity.this, enterEntryActivity.class);
                        i.putExtra("ref_value", 0);
                    }
                    startActivity(i);
                    finish();
                }
            }
        });

        blurView = findViewById(R.id.blur);
        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();
        setRemainder();
        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_ENTRY;
            loadHomeFragment();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        toggle = false;

    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                // All emulators
               /// .addTestDevice("2AF6453EDCB50B99D08956245E86DB4A")
               // .addTestDevice("4CEA753BAAC3163843B33D35D033FB4A")
                .build();
        mInterstitialAd.loadAd(adRequest);
    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    private void loadNavHeader() {

        String currentTime = getCurrentTime();

        if (currentTime.compareTo("05:30:00") >= 0 && currentTime.compareTo("12:00:00") <= 0) {
            imgNavHeaderBg.setBackgroundResource(R.drawable.sunrise);
            goodText.setText(R.string.gm);
        } else if (currentTime.compareTo("12:00:00") >= 0 && currentTime.compareTo("16:45:00") <= 0) {
            goodText.setText(R.string.good_afternoon);
            imgNavHeaderBg.setBackgroundResource(R.drawable._afternoon);
        } else if (currentTime.compareTo("16:45:00") >= 0 && currentTime.compareTo("19:30:00") <= 0) {
            imgNavHeaderBg.setBackgroundResource(R.drawable.sunset);
            goodText.setText(R.string.ge);
        } else if (currentTime.compareTo("19:30:00") >= 0 && currentTime.compareTo("20:45:00") <= 0) {
            imgNavHeaderBg.setBackgroundResource(R.drawable.night);
            goodText.setTextColor(ContextCompat.getColor(this, android.R.color.white));
            goodText.setText(R.string.ge);
        } else if (currentTime.compareTo("20:45:00") >= 0 || currentTime.compareTo("05:30:00") <= 0) {
            imgNavHeaderBg.setBackgroundResource(R.drawable.night);
            goodText.setTextColor(ContextCompat.getColor(this, android.R.color.white));
            goodText.setText(R.string.gn);
        }
        no_of_entries.setText("");
        try {
            imgProfile.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(),
                    Uri.parse(sp.getString(Constants.USER_IMAGE_URI, ""))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */

    void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            toggleFab();
            return;
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If runnable is not null, then add to the message queue
        handler.post(runnable);

        // show or hide the fab button
        toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                return new EntryAndNotesFragment();
            case 1:
                // photos
                return new CalenderFragment();
            case 2:
                // movies fragment
                return new FavouritesFragment();
            case 3:
                return new ImpNotesFragment();
            case 4:
                return new ChangePINFragment();
            default:
                return new EntryFragment();
        }
    }

    private void setToolbarTitle() {
        //getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer,
                toolbar, R.string.opendrawer, R.string.closedrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we don't want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we don't want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.addDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setUpNavigationView();
        RecyclerView recyclerView = imagesNavigationView.findViewById(R.id.recycler_images);
        recyclerView.hasFixedSize();
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(MainActivity.this, 3);
        if (new MyDB(MainActivity.this).getEntriesCount() != 0) {
            images_adapter imagesAdapter = new images_adapter(MainActivity.this, get_images_paths());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(imagesAdapter);
        }
    }

    private ArrayList<String> get_images_paths() {
        ArrayList<String> imagesPath = new ArrayList<>();
        File[] fileLists;
        File imagesfile = new File(android.os.Environment.getExternalStorageDirectory().getPath(),
                "My Journal/Images");
        if (imagesfile.isDirectory()) {
            fileLists = imagesfile.listFiles();
            if (fileLists != null) {
                for (int i = fileLists.length - 1; i >= 0; i--) {
                    File fileList = fileLists[i];
                    imagesPath.add(fileList.getAbsolutePath());
                }
            }
        }
        return imagesPath;
    }

    public void setRemainder() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 15);
        calendar.set(Calendar.SECOND, 0);
        Calendar cCalendar = Calendar.getInstance();


        Intent notificationMessage = new Intent(MainActivity.this, NotificationHelper.class);
        PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this, 0, notificationMessage, 0);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (calendar.getTimeInMillis() >= cCalendar.getTimeInMillis()) {
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }
        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        // checking if user is on other navigation menu
        // rather than home
        if (navItemIndex != 0) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_ENTRY;
            loadHomeFragment();
            title_image.setText("Journal");
            return;
        }

        super.onBackPressed();
    }


    private void toggleFab() {
        if (navItemIndex < 2)
            fab.show();
        else
            fab.hide();
    }

    public String getCurrentTime() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }

    public static ArrayList<Entries> get_Entries(Context context) {
        ArrayList<Entries> entries = new ArrayList<>();
        MyDB db = new MyDB(context);
        db.open();
        Cursor cursor = db.getEntries();
        cursor.moveToFirst();
        String id, title, mood, date, highlights;
        while (!cursor.isLast()) {
            id = cursor.getString(0);
            title = cursor.getString(1);
            date = cursor.getString(2);
            mood = cursor.getString(3);
            highlights = cursor.getString(5);
            String no_of_images = cursor.getString(6);
            String image = cursor.getString(7);
            String image2 = cursor.getString(8);
            String image3 = cursor.getString(9);
            String image4 = cursor.getString(10);
            String location = cursor.getString(12);
            String fav = cursor.getString(13);
            entries.add(new Entries(Integer.parseInt(id),
                    title, date, mood, cursor.getString(4), highlights, no_of_images,
                    image, image2, image3, image4, cursor.getString(11), location, fav));
            cursor.moveToNext();
        }
        id = cursor.getString(0);
        title = cursor.getString(1);
        date = cursor.getString(2);
        mood = cursor.getString(3);
        highlights = cursor.getString(5);
        String no_of_images = cursor.getString(6);
        String image = cursor.getString(7);
        String image2 = cursor.getString(8);
        String image3 = cursor.getString(9);
        String image4 = cursor.getString(10);
        String location = cursor.getString(11);
        String fav = cursor.getString(13);
        entries.add(new Entries(Integer.parseInt(id),
                title, date, mood, cursor.getString(4), highlights, no_of_images,
                image, image2, image3, image4, location, cursor.getString(12), fav));
        db.close();
        return entries;
    }

    public static ArrayList<Notes> getNotes(Context context) {
        ArrayList<Notes> notes = new ArrayList<>();
        notesDB notesdb = new notesDB(context);
        notesdb.open();
        Cursor cursor = notesdb.getNotes();
        cursor.moveToFirst();
        String id, title, mood, date, highlights;
        while (!cursor.isLast()) {
            id = cursor.getString(0);
            title = cursor.getString(1);
            date = cursor.getString(2);
            mood = cursor.getString(3);
            highlights = cursor.getString(5);
            notes.add(new Notes(Integer.parseInt(id),
                    title, date, mood, cursor.getString(4), highlights));
            cursor.moveToNext();
        }
        id = cursor.getString(0);
        title = cursor.getString(1);
        date = cursor.getString(2);
        mood = cursor.getString(3);
        highlights = cursor.getString(5);
        notes.add(new Notes(Integer.parseInt(id),
                title, date, mood, cursor.getString(4), highlights));
        notesdb.close();
        return notes;
    }

    public static ArrayList<Entries> get_Entries_of_date(Context context, String calenderDate) {
        ArrayList<Entries> entries = new ArrayList<>();
        MyDB db = new MyDB(context);
        db.open();
        Cursor cursor = db.getEntries();
        cursor.moveToFirst();
        String id, title, mood, date, highlights;
        String calender_date = calenderDate.replace("-", "");
        while (!cursor.isLast()) {
            String Date = cursor.getString(2);
            String c = Date.substring(0, 8);
            if (calender_date.equals(c)) {
                id = cursor.getString(0);
                title = cursor.getString(1);
                date = cursor.getString(2);
                mood = cursor.getString(3);
                highlights = cursor.getString(5);
                String no_of_images = cursor.getString(6);
                String image = cursor.getString(7);
                String image2 = cursor.getString(8);
                String image3 = cursor.getString(9);
                String image4 = cursor.getString(10);
                String rmu = cursor.getString(11);
                String location = cursor.getString(12);
                String fav = cursor.getString(13);
                entries.add(new Entries(Integer.parseInt(id),
                        title, date, mood, cursor.getString(4), highlights, no_of_images,
                        image, image2, image3, image4, rmu, location, fav));

            }
            cursor.moveToNext();
        }

        String c = cursor.getString(2).substring(0, 8);
        if (Objects.equals(calender_date, c)) {
            id = cursor.getString(0);
            title = cursor.getString(1);
            date = cursor.getString(2);
            mood = cursor.getString(3);
            highlights = cursor.getString(5);
            String no_of_images = cursor.getString(6);
            String image = cursor.getString(7);
            String image2 = cursor.getString(8);
            String image3 = cursor.getString(9);
            String image4 = cursor.getString(10);
            String rmu = cursor.getString(11);
            String location = cursor.getString(12);
            String fav = cursor.getString(13);
            entries.add(new Entries(Integer.parseInt(id),
                    title, date, mood, cursor.getString(4), highlights, no_of_images,
                    image, image2, image3, image4, rmu, location, fav));
        }
        db.close();
        return entries;
    }

    public static ArrayList<Entries> get_Fav_Entries(Context context) {
        ArrayList<Entries> entries = new ArrayList<>();
        MyDB db = new MyDB(context);
        db.open();
        Cursor cursor = db.getEntries();
        cursor.moveToFirst();
        String id, title, mood, date, highlights;
        int fav_code = 1;
        while (!cursor.isLast() && cursor != null && cursor.getCount() != 0) {
            if (fav_code == Integer.parseInt(cursor.getString(13))) {
                id = cursor.getString(0);
                title = cursor.getString(1);
                date = cursor.getString(2);
                mood = cursor.getString(3);
                highlights = cursor.getString(5);
                String no_of_images = cursor.getString(6);
                String image = cursor.getString(7);
                String image2 = cursor.getString(8);
                String image3 = cursor.getString(9);
                String image4 = cursor.getString(10);
                String rmu = cursor.getString(11);
                String location = cursor.getString(12);
                String fav = cursor.getString(13);
                entries.add(new Entries(Integer.parseInt(id),
                        title, date, mood, cursor.getString(4), highlights, no_of_images,
                        image, image2, image3, image4, rmu, location, fav));

            }
            cursor.moveToNext();
        }
        if (cursor.getCount() != 0)
            if (fav_code == Integer.parseInt(cursor.getString(13))) {
                id = cursor.getString(0);
                title = cursor.getString(1);
                date = cursor.getString(2);
                mood = cursor.getString(3);
                highlights = cursor.getString(5);
                String no_of_images = cursor.getString(6);
                String image = cursor.getString(7);
                String image2 = cursor.getString(8);
                String image3 = cursor.getString(9);
                String image4 = cursor.getString(10);
                String location = cursor.getString(11);
                String fav = cursor.getString(13);
                entries.add(new Entries(Integer.parseInt(id),
                        title, date, mood, cursor.getString(4), highlights, no_of_images,
                        image, image2, image3, image4, location, cursor.getString(12), fav));
            }
        db.close();
        return entries;
    }

    public static int getEntryIdfromImagePath(Context context, String image_path) {
        Cursor cursor;
        MyDB dtbs = new MyDB(context);
        dtbs.open();
        cursor = (dtbs).getCursorfromImagePath(image_path, Constants.IMAGE_URI);
        if (cursor.getCount() == 0) {
            cursor = (dtbs).getCursorfromImagePath(image_path, Constants.IMAGE2_URI);
        }
        if (cursor.getCount() == 0) {
            cursor = (dtbs).getCursorfromImagePath(image_path, Constants.IMAGE3_URI);
        }
        if (cursor.getCount() == 0) {
            cursor = (dtbs).getCursorfromImagePath(image_path, Constants.IMAGE4_URI);
        }
        int id = -1;
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            id = Integer.parseInt(cursor.getString(0));
        }

        dtbs.close();
        return id;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //Check to see which item was being clicked and perform appropriate action
        switch (item.getItemId()) {
            //Replacing the main content with ContentFragment Which is our Inbox View;
            case R.id.nav_entries:
                navItemIndex = 0;
                CURRENT_TAG = TAG_ENTRY;
                title_image.setText(R.string.journal);
                break;
            case R.id.nav_calender:
                navItemIndex = 1;
                title_image.setText(R.string.calendarviewtext);
                CURRENT_TAG = TAG_CALENDERVIEW;
                break;
            case R.id.nav_favourites:
                navItemIndex = 2;
                CURRENT_TAG = TAG_FAVOURITES;
                title_image.setText(R.string.favoutites);
                break;
            case R.id.nav_imp:
                navItemIndex = 3;
                CURRENT_TAG = TAG_IMPORTANT_NOTES;
                title_image.setText(R.string.impnotes);
                break;
            case R.id.nav_changepin:
                navItemIndex = 4;
                CURRENT_TAG = TAG_CHANGEPIN;
                title_image.setText(R.string.changeppin);
                break;
            case R.id.rate_us:
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                finish();
                break;
            default:
                navItemIndex = 0;
        }

        loadHomeFragment();

        return true;
    }

    public static ArrayList<Notes> getImpNotes(Context context) {
        ArrayList<Notes> notes = new ArrayList<>();
        notesDB db = new notesDB(context);
        db.open();
        Cursor cursor = db.getNotes();
        cursor.moveToFirst();

        while (!cursor.isLast() && cursor != null && cursor.getCount() != 0) {
            if (Objects.equals(cursor.getString(5), "1")) {
                notes.add(new Notes(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)
                ));
            }
            cursor.moveToNext();
        }
        if (cursor.getCount() != 0)
            if (Objects.equals(cursor.getString(5), "1")) {
                notes.add(new Notes(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)
                ));
            }

        return notes;
    }

}