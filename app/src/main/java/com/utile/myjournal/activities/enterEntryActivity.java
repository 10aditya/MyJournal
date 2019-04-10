package com.utile.myjournal.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.media.ExifInterface;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.utile.myjournal.R;
import com.utile.myjournal.database.Constants;
import com.utile.myjournal.database.Entries;
import com.utile.myjournal.database.MyDB;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class enterEntryActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    RelativeLayout yy;
    public static EditText xx;
    EditText title;
    EditText highlight;
    public ImageView myFrame;
    MyDB myDB;
    ImageView take_pic;
    ImageView b_save;
    CoordinatorLayout co;
    int ref_value;
    Entries item;
    public String dt;
    public int flagForDateChange = 0;
    private String no_of_images = "0";
    ImageView location;
    private Uri photoURI = null;
    private Uri photoURI2 = null;
    private Uri photoURI3 = null;
    private Uri photoURI4 = null;
    private ImageView take_pic4;
    private ImageView take_pic3;
    private ImageView take_pic2;
    private GoogleApiClient mGoogleApiClient = null;
    boolean checkIfCalenderIsVisible = true;
    private String location_c = "null";
    private String location_n = "null";
    private List<String> place_name = new ArrayList<>();
    private List<String> place_coordinates = new ArrayList<>();
    private LinearLayout bottomLayout;
    private TextView locationName;
    private int darkOf_color;
    private CalendarView calendar;
    private Toolbar toolbar;
    private TextView textViewForDate;
    private RelativeLayout layoutForCalendar;
    private ImageView navArrow;
    private TimePicker timepicker;
    private TextView textViewForTime;
    private Button dropDownForSuggetions;
    private InterstitialAd mInterstitialAd;
    private SharedPreferences preferences;
    private int color;

    public enterEntryActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_entry);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        ref_value = getIntent().getIntExtra("ref_value", 0);

        myDB = new MyDB(this);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-6900171412533642~9756488611");

        initialiseViews();

        navArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    startActivity(new Intent(enterEntryActivity.this, MainActivity.class));
                    finish();
                }
                finish();
            }
        });

        if (Objects.equals(preferences.getString(Constants.flagForTapTargetViewInEnterEntryActivity, "false"), "false")) {
            showIntro();
        }

        Random random = new Random();


        color = ContextCompat.getColor(this, Constants.colors[random.nextInt(Constants.colors.length)]);
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.8f;
        darkOf_color = Color.HSVToColor(hsv);

        layoutForCalendar = findViewById(R.id.layoutForCalendar);
        getWindow().setStatusBarColor(darkOf_color);
        DateFormat dateFormat = new SimpleDateFormat("dd:MM:yyyy", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        textViewForDate.setText(dateFormat.format(cal.getTime()));
        textViewForTime.setText(String.format("@ %s", this.getCurrentTime()));


        if (ref_value == 2) {
            textViewForDate.setText(getIntent().getStringExtra("date"));
        }

        dropDownForSuggetions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(enterEntryActivity.this, Pop.class));
            }
        });


        textViewForDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkIfCalenderIsVisible) {
                    layoutForCalendar.setVisibility(View.VISIBLE);
                    layoutForCalendar.setBackgroundColor(darkOf_color);
                    calendar.setVisibility(View.VISIBLE);
                    timepicker.setVisibility(View.GONE);
                    //ObjectAnimator translationY = ObjectAnimator.ofFloat(menu, View.TRANSLATION_Y, menu.getHeight(), 0);
                    //translationY.setDuration(1000);
                    //translationY.setInterpolator();
                    //translationY.setStartDelay(150);
                    //translationY.start();
                    checkIfCalenderIsVisible = false;

                } else {
                    layoutForCalendar.setVisibility(View.GONE);
                    calendar.setVisibility(View.GONE);
                    checkIfCalenderIsVisible = true;
                }
            }
        });

        textViewForTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkIfCalenderIsVisible) {
                    layoutForCalendar.setVisibility(View.VISIBLE);
                    layoutForCalendar.setBackgroundColor(darkOf_color);
                    timepicker.setVisibility(View.VISIBLE);
                    timepicker.setIs24HourView(true);
                    calendar.setVisibility(View.GONE);
                    checkIfCalenderIsVisible = false;
                } else {
                    layoutForCalendar.setVisibility(View.GONE);
                    timepicker.setVisibility(View.GONE);
                    checkIfCalenderIsVisible = true;
                }
            }
        });

        timepicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                textViewForTime.setText(String.format("@ %02d:%02d", hourOfDay, minute));
            }
        });

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                textViewForDate.setText(String.format("%02d:%02d:%04d", dayOfMonth, month + 1, year));
            }
        });

        toolbar.setBackgroundColor(color);

        myFrame.setBackgroundColor(color);

        if (ref_value == 1) {
            item = (Entries) getIntent().getExtras().getSerializable("entry_to_edit");
            title.setText(item != null ? item.getTitle() : null);
            xx.setText(item.getMood());
            dt = item.getDate();
            textViewForDate.setText(String.format("%s:%s:%s",
                    dt.substring(6, 8),
                    dt.substring(4, 6),
                    dt.substring(0, 4)
            ));
            textViewForTime.setText(String.format("@ %s:%s", dt.substring(8, 10), dt.substring(10, 12)));
            highlight.setText(item.getHighlights());
            if (!Objects.equals(item.getImage(), "null")) {
                photoURI = Uri.parse(item.getImage());
                take_pic.setImageBitmap(BitmapFactory.decodeFile(item.getImage()));
                take_pic2.setEnabled(true);
                take_pic2.setAlpha(1.0f);
            }
            if (!Objects.equals(item.getImage2_uri(), "null")) {
                photoURI2 = Uri.parse(item.getImage2_uri());
                take_pic2.setEnabled(true);
                take_pic2.setAlpha(1.0f);
                take_pic3.setEnabled(true);
                take_pic3.setAlpha(1.0f);
                take_pic2.setImageBitmap(BitmapFactory.decodeFile(item.getImage2_uri()));
            }
            if (!Objects.equals(item.getImage3_uri(), "null")) {
                photoURI3 = Uri.parse(item.getImage3_uri());
                take_pic3.setEnabled(true);
                take_pic3.setAlpha(1.0f);
                take_pic4.setEnabled(true);
                take_pic4.setAlpha(1.0f);
                take_pic3.setImageBitmap(BitmapFactory.decodeFile(item.getImage3_uri()));
            }
            if (!Objects.equals(item.getImage4_uri(), "null")) {
                photoURI4 = Uri.parse(item.getImage4_uri());
                take_pic4.setEnabled(true);
                take_pic4.setAlpha(1.0f);
                take_pic4.setImageBitmap(BitmapFactory.decodeFile(item.getImage4_uri()));
            }

            location_n = item.getLocation_n();
            locationName.setText(location_n);
            location_c = item.getLocation_c();
        }


        getWindow().setStatusBarColor(darkOf_color);
        Drawable drawableForBottomLayout = ContextCompat.getDrawable(enterEntryActivity.this, R.drawable.curved_sides);
        drawableForBottomLayout.setColorFilter(darkOf_color, PorterDuff.Mode.MULTIPLY);
        bottomLayout.setBackground(drawableForBottomLayout);
        take_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(enterEntryActivity.this);
                alertDialog.setTitle(R.string.imageSource);
                alertDialog.setPositiveButton("Take Picture", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        dispatchTakePictureIntent();
                    }
                });
                alertDialog.setNegativeButton("From gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        getImageFromStorage(11);
                    }

                });

                alertDialog.show();
                take_pic2.setVisibility(View.VISIBLE);
            }
        });

        take_pic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(enterEntryActivity.this);
                alertDialog.setTitle(R.string.imageSource);
                alertDialog.setPositiveButton("Take Picture", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        dispatchTakePicture2Intent();
                    }
                });
                alertDialog.setNegativeButton("From gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        getImageFromStorage(22);
                    }

                });

                alertDialog.show();
                take_pic3.setVisibility(View.VISIBLE);
            }
        });

        take_pic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(enterEntryActivity.this);
                alertDialog.setTitle(R.string.imageSource);
                alertDialog.setPositiveButton("Take Picture", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        dispatchTakePicture3Intent();
                    }
                });
                alertDialog.setNegativeButton("From gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        getImageFromStorage(33);
                    }

                });

                alertDialog.show();
                take_pic4.setVisibility(View.VISIBLE);
            }
        });

        take_pic4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(enterEntryActivity.this);
                alertDialog.setTitle(R.string.imageSource);
                alertDialog.setPositiveButton("Take Picture", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        dispatchTakePicture4Intent();
                    }
                });
                alertDialog.setNegativeButton("From gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        getImageFromStorage(44);
                    }

                });

                alertDialog.show();
            }
        });

        mInterstitialAd = new InterstitialAd(this);

        mInterstitialAd.setAdUnitId(getString(R.string.interstitialAd2));

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                startActivity(new Intent(enterEntryActivity.this, MainActivity.class));
                finish();
            }
        });

        requestNewInterstitial();

        b_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToDB();
            }
        });

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(enterEntryActivity.this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .enableAutoManage(this, this)
                    .build();
        }
    }

    private void showIntro() {
        new TapTargetSequence(this)
                .targets(
                        TapTarget.forView(take_pic, "Add image", "Click camera buttons to add images of your moment.")
                                .outerCircleColor(R.color.material_blue)
                                .outerCircleAlpha(0.96f)
                                .targetCircleColor(android.R.color.white)
                                .titleTextSize(25)
                                .titleTextColor(android.R.color.white)
                                .descriptionTextSize(20)
                                .descriptionTextColor(android.R.color.holo_red_light)
                                .textColor(android.R.color.white)
                                .textTypeface(Typeface.SANS_SERIF)
                                .dimColor(android.R.color.black)
                                .drawShadow(true)
                                .cancelable(false)
                                .tintTarget(true)
                                .transparentTarget(false)
                                .targetRadius(60),
                        TapTarget.forView(location, "Add location", "Click here to add location where the moment happened.")
                                .outerCircleColor(R.color.material_blue)
                                .outerCircleAlpha(0.96f)
                                .targetCircleColor(android.R.color.white)
                                .titleTextSize(25)
                                .titleTextColor(android.R.color.white)
                                .descriptionTextSize(20)
                                .descriptionTextColor(android.R.color.holo_red_light)
                                .textColor(android.R.color.white)
                                .textTypeface(Typeface.SANS_SERIF)
                                .dimColor(android.R.color.black)
                                .drawShadow(true)
                                .cancelable(false)
                                .tintTarget(true)
                                .transparentTarget(false)
                                .targetRadius(60),
                        TapTarget.forView(b_save, "Save it", "Click here to save your moment.")
                                .outerCircleColor(R.color.material_blue)
                                .outerCircleAlpha(0.96f)
                                .targetCircleColor(android.R.color.white)
                                .titleTextSize(25)
                                .titleTextColor(android.R.color.white)
                                .descriptionTextSize(20)
                                .descriptionTextColor(android.R.color.holo_red_light)
                                .textColor(android.R.color.white)
                                .textTypeface(Typeface.SANS_SERIF)
                                .dimColor(android.R.color.black)
                                .drawShadow(true)
                                .cancelable(false)
                                .tintTarget(true)
                                .transparentTarget(false)
                                .targetRadius(60),
                        TapTarget.forView(dropDownForSuggetions, "Get Suggetions", "Get suggestions for mood or activity from here.")
                                .outerCircleColor(R.color.material_blue)
                                .outerCircleAlpha(0.96f)
                                .targetCircleColor(android.R.color.white)
                                .titleTextSize(25)
                                .titleTextColor(android.R.color.white)
                                .descriptionTextSize(20)
                                .descriptionTextColor(android.R.color.holo_red_light)
                                .textColor(android.R.color.white)
                                .textTypeface(Typeface.SANS_SERIF)
                                .dimColor(android.R.color.black)
                                .drawShadow(true)
                                .cancelable(false)
                                .tintTarget(false)
                                .transparentTarget(false)
                                .targetRadius(60),
                        TapTarget.forView(textViewForDate, "Set date here")
                                .outerCircleColor(R.color.material_blue)
                                .outerCircleAlpha(0.96f)
                                .targetCircleColor(android.R.color.white)
                                .titleTextSize(25)
                                .titleTextColor(android.R.color.white)
                                .descriptionTextSize(20)
                                .descriptionTextColor(android.R.color.holo_red_light)
                                .textColor(android.R.color.white)
                                .textTypeface(Typeface.SANS_SERIF)
                                .dimColor(android.R.color.black)
                                .drawShadow(true)
                                .cancelable(false)
                                .tintTarget(false)
                                .transparentTarget(false)
                                .targetRadius(60),
                        TapTarget.forView(textViewForTime, "Set time here.")
                                .outerCircleColor(R.color.material_blue)
                                .outerCircleAlpha(0.96f)
                                .targetCircleColor(android.R.color.white)
                                .titleTextSize(25)
                                .titleTextColor(android.R.color.white)
                                .descriptionTextSize(20)
                                .descriptionTextColor(android.R.color.holo_red_light)
                                .textColor(android.R.color.white)
                                .textTypeface(Typeface.SANS_SERIF)
                                .dimColor(android.R.color.black)
                                .drawShadow(true)
                                .cancelable(false)
                                .tintTarget(false)
                                .transparentTarget(false)
                                .targetRadius(60)
                )
                .listener(new TapTargetSequence.Listener() {
                    @Override
                    public void onSequenceFinish() {

                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {


                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {
                        // Boo
                    }
                }).start();

        SharedPreferences.Editor spe = preferences.edit();
        spe.putString(Constants.flagForTapTargetViewInEnterEntryActivity, "true");
        spe.apply();
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
             //   .addTestDevice("4CEA753BAAC3163843B33D35D033FB4A")
              //  .addTestDevice("2AF6453EDCB50B99D08956245E86DB4A")
                .build();
        mInterstitialAd.loadAd(adRequest);
    }

    private void initialiseViews() {
        myFrame = findViewById(R.id.frame);
        yy = findViewById(R.id.relativeLayout);
        title = findViewById(R.id.editText1);
        highlight = findViewById(R.id.editText3);
        b_save = findViewById(R.id.save_entry);
        toolbar = findViewById(R.id.basic_toolbar);
        xx = findViewById(R.id.editText2);
        take_pic = findViewById(R.id.camera4);
        take_pic2 = findViewById(R.id.camera1);
        take_pic2.setAlpha(0.5f);
        take_pic2.setEnabled(false);
        take_pic3 = findViewById(R.id.camera2);
        take_pic3.setAlpha(0.5f);
        take_pic3.setEnabled(false);
        take_pic4 = findViewById(R.id.camera3);
        take_pic4.setAlpha(0.5f);
        take_pic4.setEnabled(false);
        location = findViewById(R.id.location);
        co = findViewById(R.id.coordinator);
        bottomLayout = findViewById(R.id.bottomLayout);
        locationName = findViewById(R.id.locationName);
        textViewForDate = findViewById(R.id.textViewForDate);
        textViewForTime = findViewById(R.id.textViewForTime);
        calendar = findViewById(R.id.calendar);
        navArrow = findViewById(R.id.navArrow);
        timepicker = findViewById(R.id.timepicker);
        dropDownForSuggetions = findViewById(R.id.buttonforsuggetions);
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HH_mm_ss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_TAKE2_PHOTO = 2;
    static final int REQUEST_TAKE3_PHOTO = 3;
    static final int REQUEST_TAKE4_PHOTO = 4;
    File photoFile = null;
    File photoFile2 = null;
    File photoFile3 = null;
    File photoFile4 = null;

    void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go

            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.utile.myjournal.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }

        }
    }

    void dispatchTakePicture2Intent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go

            try {
                photoFile2 = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile2 != null) {
                photoURI2 = FileProvider.getUriForFile(this,
                        "com.utile.myjournal.fileprovider",
                        photoFile2);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI2);
                startActivityForResult(takePictureIntent, REQUEST_TAKE2_PHOTO);
            }

        }
    }

    void dispatchTakePicture3Intent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go

            try {
                photoFile3 = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile3 != null) {
                photoURI3 = FileProvider.getUriForFile(this,
                        "com.utile.myjournal.fileprovider",
                        photoFile3);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI3);
                startActivityForResult(takePictureIntent, REQUEST_TAKE3_PHOTO);
            }

        }
    }

    void dispatchTakePicture4Intent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go

            try {
                photoFile4 = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile4 != null) {
                photoURI4 = FileProvider.getUriForFile(this,
                        "com.utile.myjournal.fileprovider",
                        photoFile4);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI4);
                startActivityForResult(takePictureIntent, REQUEST_TAKE4_PHOTO);
            }

        }
    }

    public void saveToDB() {
        myDB.open();

        String entryTitle = title.getText().toString();
        String entryMood = xx.getText().toString();
        String entryHighlight = highlight.getText().toString();
        String mood_color = String.valueOf(color);
        String date = (String) textViewForDate.getText();
        String time = (String) textViewForTime.getText();
        dt = String.format("%s%s%s%s%s",
                date.substring(6),
                date.substring(3, 5),
                date.substring(0, 2),
                time.substring(2, 4),
                time.substring(5, 7));
        String photoPathForFirstImage;
        if (photoURI == null) {
            photoPathForFirstImage = "null";
        } else {
            photoPathForFirstImage = photoURI.toString();
            no_of_images = "1";
        }
        String photoPathForSecondImage;
        if (photoURI2 == null) {
            photoPathForSecondImage = "null";
        } else {
            photoPathForSecondImage = photoURI2.toString();
            no_of_images = "2";
        }
        String photoPathForThirdImage;
        if (photoURI3 == null) {
            photoPathForThirdImage = "null";
        } else {
            photoPathForThirdImage = photoURI3.toString();
            no_of_images = "3";
        }
        String photoPathForFourthImage;
        if (photoURI4 == null) {
            photoPathForFourthImage = "null";
        } else {
            photoPathForFourthImage = photoURI4.toString();
            no_of_images = "4";
        }

        if (ref_value == 1 && flagForDateChange == 0) {
            myDB.updateEntry(new Entries(
                    item.getID(),
                    entryTitle,
                    dt,
                    entryMood,
                    mood_color,
                    entryHighlight,
                    no_of_images,
                    photoPathForFirstImage,
                    photoPathForSecondImage,
                    photoPathForThirdImage,
                    photoPathForFourthImage,
                    location_n,
                    location_c,
                    "0"));
        } else {
            if (ref_value == 1) {
                myDB.deleteEntry(new Entries(this.item.getID(),
                        this.item.getTitle(),
                        this.item.getDate(),
                        this.item.getMood(),
                        this.item.getColor(),
                        this.item.getHighlights(),
                        this.item.getNo_of_images(),
                        this.item.getImage(),
                        this.item.getImage2_uri(),
                        this.item.getImage3_uri(),
                        this.item.getImage4_uri(),
                        this.item.getLocation_n(),
                        this.item.getLocation_c(),
                        this.item.getFavourite()));
            }
            myDB.insertEntry(new Entries(
                    entryTitle,
                    dt,
                    entryMood,
                    mood_color,
                    entryHighlight,
                    no_of_images,
                    photoPathForFirstImage,
                    photoPathForSecondImage,
                    photoPathForThirdImage,
                    photoPathForFourthImage,
                    location_n,
                    location_c,
                    "0"));

        }

        myDB.close();
        Toast.makeText(enterEntryActivity.this, "Entry saved", Toast.LENGTH_SHORT).show();
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            startActivity(new Intent(enterEntryActivity.this, MainActivity.class));
            finish();
        }
        Log.d("Database Updated", entryTitle + " added to db");
    }

    public String getCurrentTime() {
        //date output format
        DateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        String path = String.format("%s/Android/data/com.utile.myjournal/files/Pictures/",
                Environment.getExternalStorageDirectory());

        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {
            case REQUEST_TAKE_PHOTO:
                String filePathtoDelete;
                if (photoFile.exists()) {
                    filePathtoDelete = path + photoURI.toString().substring(
                            photoURI.toString().indexOf("J"), photoURI.toString().length());
                    photoURI = Uri.parse(compressImage(photoURI.toString(), 0));
                    deleteImage(filePathtoDelete);
                    take_pic.setImageBitmap(BitmapFactory.decodeFile(photoURI.toString()));
                }

                setResult(0);
                take_pic2.setEnabled(true);
                take_pic2.setAlpha(1.0f);
                break;

            case REQUEST_TAKE2_PHOTO:
                if (photoFile2.exists()) {
                    filePathtoDelete = path + photoURI2.toString().substring(
                            photoURI2.toString().indexOf("J"), photoURI2.toString().length());

                    photoURI2 = Uri.parse(compressImage(photoURI2.toString(), 0));
                    deleteImage(filePathtoDelete);
                    take_pic2.setImageBitmap(BitmapFactory.decodeFile(photoURI2.toString()));
                    take_pic3.setVisibility(View.VISIBLE);
                }
                setResult(0);
                take_pic3.setAlpha(1.0f);
                take_pic3.setEnabled(true);
                break;

            case REQUEST_TAKE3_PHOTO:
                if (photoFile3.exists()) {
                    filePathtoDelete = path + photoURI3.toString().substring(
                            photoURI3.toString().indexOf("J"), photoURI3.toString().length());
                    photoURI3 = Uri.parse(compressImage(photoURI3.toString(), 0));
                    deleteImage(filePathtoDelete);
                    take_pic3.setImageBitmap(BitmapFactory.decodeFile(photoURI3.toString()));
                    take_pic4.setVisibility(View.VISIBLE);
                }
                setResult(0);
                take_pic4.setAlpha(1.0f);
                take_pic4.setEnabled(true);
                break;

            case REQUEST_TAKE4_PHOTO:
                if (photoFile4.exists()) {
                    filePathtoDelete = path + photoURI4.toString().substring(
                            photoURI4.toString().indexOf("J"), photoURI4.toString().length());
                    photoURI4 = Uri.parse(compressImage(photoURI4.toString(), 0));
                    deleteImage(filePathtoDelete);
                    take_pic4.setImageBitmap(BitmapFactory.decodeFile(photoURI4.toString()));
                }
                setResult(0);
                break;

            case 11:
                photoURI = Uri.parse(compressImage(data.getData().toString(), 1));
                take_pic.setImageBitmap(BitmapFactory.decodeFile(photoURI.toString()));
                setResult(0);
                take_pic2.setAlpha(1.0f);
                take_pic2.setEnabled(true);
                break;
            case 22:

                photoURI2 = Uri.parse(compressImage(data.getData().toString(), 1));
                take_pic2.setImageBitmap(BitmapFactory.decodeFile(photoURI2.toString()));
                setResult(0);
                take_pic3.setAlpha(1.0f);
                take_pic3.setEnabled(true);
                break;

            case 33:

                photoURI3 = Uri.parse(compressImage(data.getData().toString(), 1));
                take_pic3.setImageBitmap(BitmapFactory.decodeFile(photoURI3.toString()));
                setResult(0);
                take_pic4.setAlpha(1.0f);
                take_pic4.setEnabled(true);
                break;

            case 44:
                photoURI4 = Uri.parse(compressImage(data.getData().toString(), 1));
                take_pic4.setImageBitmap(BitmapFactory.decodeFile(photoURI4.toString()));
                setResult(0);
                break;

            case 100:
                Place place = PlacePicker.getPlace(this, data);
                this.location_n = (String) place.getName();
                this.location_c = String.format("%s,%s", String.valueOf(place.getLatLng().latitude),
                        String.valueOf(place.getLatLng().longitude));
                locationName.setText(location_n);
                setResult(0);
                break;
        }
    }

    void getImageFromStorage(int request_code) {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        switch (request_code) {
            case 11:
                startActivityForResult(i, 11);
                break;
            case 22:
                startActivityForResult(i, 22);
                break;
            case 33:
                startActivityForResult(i, 33);
                break;
            case 44:
                startActivityForResult(i, 44);
                break;

        }
    }

    public String compressImage(String imageUri, int code) {

        String filePath;
        if (code == 0) {
            String path = String.format("%s/Android/data/com.utile.myjournal/files/Pictures/",
                    Environment.getExternalStorageDirectory());
            filePath = path + imageUri.substring(imageUri.indexOf("J"), imageUri.length());
        } else {
            filePath = getRealPathFromURI(imageUri);
        }
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile((new File(filePath)).getAbsolutePath(), options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 956.0f;
        float maxWidth = 652.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = null;
        if (scaledBitmap != null) {
            canvas = new Canvas(scaledBitmap);
        }
        assert canvas != null;
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out;
        String filename;
        filename = getFilename();
        try {
            assert filename != null;
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "My Journal/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        return (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");

    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            String str = cursor.getString(index);
            cursor.close();
            return str;
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(enterEntryActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                .getCurrentPlace(mGoogleApiClient, null);
        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
            @Override
            public void onResult(@NonNull PlaceLikelihoodBuffer likelyPlaces) {
                int i = 0;
                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                    Place place = placeLikelihood.getPlace();
                    String pl = (String) place.getName();
                    String pll = String.format("%s,%s", String.valueOf(place.getLatLng().latitude),
                            String.valueOf(place.getLatLng().longitude));
                    place_name.add(i, pl);
                    place_coordinates.add(i, pll);
                    i++;
                }
                likelyPlaces.release();
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] data = place_name.toArray(new String[place_name.size()]);
                @SuppressLint("RestrictedApi")
                AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(enterEntryActivity.this,
                        R.style.dialogTheme));
                alert.setTitle("Choose Current Location");
                if (data.length == 0)
                    alert.setMessage("Current locations not available, try again!");
                alert.setItems(data, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        location_n = (String) data[i];
                        location_c = place_coordinates.get(i);
                        locationName.setText(data[i]);

                    }
                });

                alert.setPositiveButton("Choose on Map", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                        try {
                            startActivityForResult(builder.build(enterEntryActivity.this), 100);
                        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                            e.printStackTrace();
                        }
                    }
                });
                alert.show();

            }
        });
    }


    @Override
    public void onConnectionSuspended(int i) {
    }

    public void deleteImage(String file_path) {
        File fdelete = new File(file_path);
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                System.out.println("file Deleted :" + file_path);
            } else {
                System.out.println("file not Deleted :" + file_path);
            }
        }
    }
}