package com.utile.myjournal.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.utile.myjournal.R;
import com.utile.myjournal.database.Notes;
import com.utile.myjournal.database.notesDB;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NotesActivity extends Activity implements View.OnClickListener {

    private ImageView red, orange, green, pink, yellow, lemonYellow, skyBlue, purple, gray, blue;
    private RelativeLayout actionBar, colorsLayout, noteDetails;
    private DisplayMetrics dm;
    private Notes item = null;
    private EditText title;
    private EditText highlights;
    private int isImp;
    private ImageView imp;
    private int backColor = 0;
    private int color;
    private notesDB notesDB;
    private boolean src;
    private boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        setColors();
        ImageView nav = findViewById(R.id.back);
        notesDB = new notesDB(NotesActivity.this);
        title = findViewById(R.id.noteTitle);
        TextView date = findViewById(R.id.noteDateAndTime);
        highlights = findViewById(R.id.noteHighlights);
        imp = findViewById(R.id.important);
        final ImageView editorsave = findViewById(R.id.saveOrEdit);
        ImageView delete = findViewById(R.id.delete);
        noteDetails = findViewById(R.id.noteDetails);
        actionBar = findViewById(R.id.actionBar);
        src = getIntent().getBooleanExtra("type", false);
        colorsLayout = findViewById(R.id.colorsLayout);
        flag = src;
        if (src) {
            title.setEnabled(false);
            highlights.setEnabled(false);
            colorsLayout.setVisibility(View.GONE);
            item = (Notes) getIntent().getExtras().getSerializable("clicked_item");
            title.setText(item.getTitle());
            highlights.setText(item.getHighlights());
            date.setVisibility(View.VISIBLE);
            date.setText(String.format("edited on %s/%s/%s @%s:%s",
                    item.getTimestamp().substring(6, 8),
                    item.getTimestamp().substring(4, 6),
                    item.getTimestamp().substring(0, 4),
                    item.getTimestamp().substring(8, 10),
                    item.getTimestamp().substring(10, 12)
            ));
            float[] hsv = new float[3];
            Color.colorToHSV(Integer.parseInt(item.getColor()), hsv);
            hsv[2] *= 0.8f;
            color = Color.HSVToColor(hsv);
            getWindow().setStatusBarColor(color);
            backColor = Integer.parseInt(item.getColor());
            noteDetails.setBackgroundColor(backColor);
            actionBar.setBackgroundColor(backColor);

            isImp = Integer.parseInt(item.getFavourite());
            if (isImp == 1) {
                imp.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star_black_24dp));
            }
        } else {
            date.setVisibility(View.GONE);
            delete.setVisibility(View.INVISIBLE);
            editorsave.setImageResource(R.drawable.ic_done_black_24dp);
            colorsLayout.setVisibility(View.VISIBLE);
            colorsLayoutAnimation();
        }

        imp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isImp == 1) {
                    isImp = 0;
                    imp.setImageDrawable(ContextCompat.getDrawable(NotesActivity.this, R.drawable.ic_star_border_black_24dp));
                    saveImp();
                } else {
                    isImp = 1;
                    imp.setImageDrawable(ContextCompat.getDrawable(NotesActivity.this, R.drawable.ic_star_black_24dp));
                    saveImp();
                }
            }
        });

        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NotesActivity.this, MainActivity.class));
                finish();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notesDB.open();
                notesDB.deleteEntry(item);
                notesDB.close();
                startActivity(new Intent(NotesActivity.this, MainActivity.class));
                finish();
            }
        });


        editorsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag) {
                    title.setEnabled(true);
                    highlights.setEnabled(true);
                    colorsLayout.setVisibility(View.VISIBLE);
                    colorsLayoutAnimation();
                    editorsave.setImageResource(R.drawable.ic_done_black_24dp);
                    flag = false;
                } else {
                    saveNoteToDB();
                    startActivity(new Intent(NotesActivity.this, MainActivity.class));
                    finish();
                }
            }
        });

        red.setOnClickListener(this);
        orange.setOnClickListener(this);
        green.setOnClickListener(this);
        yellow.setOnClickListener(this);
        lemonYellow.setOnClickListener(this);
        skyBlue.setOnClickListener(this);
        blue.setOnClickListener(this);
        pink.setOnClickListener(this);
        purple.setOnClickListener(this);
        gray.setOnClickListener(this);

    }

    private void saveImp() {
        if (item != null) {
            notesDB.open();
            notesDB.updateNote(new Notes(
                    item.getId(),
                    item.getTitle(),
                    item.getTimestamp(),
                    item.getColor(),
                    item.getHighlights(),
                    String.valueOf(isImp)));
        }
    }

    @SuppressLint("DefaultLocale")
    private void saveNoteToDB() {
        notesDB.open();
        DateFormat dateFormat = new SimpleDateFormat("dd:MM:yyyy", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());
        dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String time = dateFormat.format(cal.getTime());

        if (src) {
            notesDB.updateNote(new Notes(
                    item.getId(),
                    title.getText().toString(),
                    String.format("%s%s%s%s%s",
                            date.substring(6),
                            date.substring(3, 5),
                            date.substring(0, 2),
                            time.substring(0, 2),
                            time.substring(3, 5)),
                    String.format("%d", backColor),
                    highlights.getText().toString(),
                    String.valueOf(isImp)
            ));
        } else {
            notesDB.insertNote(new Notes(
                    title.getText().toString(),
                    String.format("%s%s%s%s%s",
                            date.substring(6),
                            date.substring(3, 5),
                            date.substring(0, 2),
                            time.substring(0, 2),
                            time.substring(3, 5)),
                    String.format("%d", backColor),
                    highlights.getText().toString(),
                    String.valueOf(isImp)
            ));
        }
        notesDB.close();
    }

    private void colorsLayoutAnimation() {

        TranslateAnimation animation = new TranslateAnimation(
                colorsLayout.getX(),
                colorsLayout.getX(),
                dm.heightPixels,
                colorsLayout.getY()
        );
        animation.setDuration(400);
        animation.setFillAfter(true);
        colorsLayout.startAnimation(animation);
    }

    private void setColors() {
        Drawable drawable = ContextCompat.getDrawable(NotesActivity.this, R.drawable.circle);
        drawable.setColorFilter(ContextCompat.getColor(this, android.R.color.holo_red_light), PorterDuff.Mode.MULTIPLY);
        red = findViewById(R.id.red);
        red.setImageDrawable(drawable);

        drawable = ContextCompat.getDrawable(NotesActivity.this, R.drawable.circle);
        drawable.setColorFilter(ContextCompat.getColor(this, android.R.color.holo_orange_dark), PorterDuff.Mode.MULTIPLY);
        orange = findViewById(R.id.orange);
        orange.setImageDrawable(drawable);

        drawable = ContextCompat.getDrawable(NotesActivity.this, R.drawable.circle);
        drawable.setColorFilter(ContextCompat.getColor(this, android.R.color.holo_green_dark), PorterDuff.Mode.MULTIPLY);
        green = findViewById(R.id.green);
        green.setImageDrawable(drawable);

        drawable = ContextCompat.getDrawable(NotesActivity.this, R.drawable.circle);
        drawable.setColorFilter(ContextCompat.getColor(this, R.color.color_permissions), PorterDuff.Mode.MULTIPLY);
        yellow = findViewById(R.id.yellow);
        yellow.setImageDrawable(drawable);

        drawable = ContextCompat.getDrawable(NotesActivity.this, R.drawable.circle);
        drawable.setColorFilter(ContextCompat.getColor(this, R.color.yellowLemon), PorterDuff.Mode.MULTIPLY);
        lemonYellow = findViewById(R.id.lemonYellow);
        lemonYellow.setImageDrawable(drawable);

        drawable = ContextCompat.getDrawable(NotesActivity.this, R.drawable.circle);
        drawable.setColorFilter(ContextCompat.getColor(this, android.R.color.holo_blue_light), PorterDuff.Mode.MULTIPLY);
        skyBlue = findViewById(R.id.skyBlue);
        skyBlue.setImageDrawable(drawable);

        drawable = ContextCompat.getDrawable(NotesActivity.this, R.drawable.circle);
        drawable.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimaryDark), PorterDuff.Mode.MULTIPLY);
        blue = findViewById(R.id.blue);
        blue.setImageDrawable(drawable);

        drawable = ContextCompat.getDrawable(NotesActivity.this, R.drawable.circle);
        drawable.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
        pink = findViewById(R.id.pink);
        pink.setImageDrawable(drawable);

        drawable = ContextCompat.getDrawable(NotesActivity.this, R.drawable.circle);
        drawable.setColorFilter(ContextCompat.getColor(this, android.R.color.holo_purple), PorterDuff.Mode.MULTIPLY);
        purple = findViewById(R.id.purple);
        purple.setImageDrawable(drawable);

        drawable = ContextCompat.getDrawable(NotesActivity.this, R.drawable.circle);
        drawable.setColorFilter(ContextCompat.getColor(this, android.R.color.darker_gray), PorterDuff.Mode.MULTIPLY);
        gray = findViewById(R.id.gray);
        gray.setImageDrawable(drawable);
    }

    @Override
    public void onClick(View v) {
        float[] hsv = new float[3];
        if (v.getId() == red.getId()) {
            backColor = ContextCompat.getColor(NotesActivity.this, android.R.color.holo_red_light);
            Color.colorToHSV(backColor, hsv);
            hsv[2] *= 0.8f;
            color = Color.HSVToColor(hsv);
        } else if (v.getId() == orange.getId()) {
            backColor = ContextCompat.getColor(NotesActivity.this, android.R.color.holo_orange_dark);
            Color.colorToHSV(backColor, hsv);
            hsv[2] *= 0.8f;
            color = Color.HSVToColor(hsv);
        } else if (v.getId() == green.getId()) {
            backColor = ContextCompat.getColor(NotesActivity.this, android.R.color.holo_green_dark);
            Color.colorToHSV(backColor, hsv);
            hsv[2] *= 0.8f;
            color = Color.HSVToColor(hsv);
        } else if (v.getId() == yellow.getId()) {
            backColor = ContextCompat.getColor(NotesActivity.this, R.color.color_permissions);
            Color.colorToHSV(backColor, hsv);
            hsv[2] *= 0.8f;
            color = Color.HSVToColor(hsv);
        } else if (v.getId() == lemonYellow.getId()) {
            backColor = ContextCompat.getColor(NotesActivity.this, R.color.yellowLemon);
            Color.colorToHSV(backColor, hsv);
            hsv[2] *= 0.8f;
            color = Color.HSVToColor(hsv);
        } else if (v.getId() == skyBlue.getId()) {
            backColor = ContextCompat.getColor(NotesActivity.this, android.R.color.holo_blue_light);
            Color.colorToHSV(backColor, hsv);
            hsv[2] *= 0.8f;
            color = Color.HSVToColor(hsv);
        } else if (v.getId() == blue.getId()) {
            backColor = ContextCompat.getColor(NotesActivity.this, R.color.colorPrimaryDark);
            Color.colorToHSV(backColor, hsv);
            hsv[2] *= 0.8f;
            color = Color.HSVToColor(hsv);
        } else if (v.getId() == pink.getId()) {
            backColor = ContextCompat.getColor(NotesActivity.this, R.color.colorAccent);
            Color.colorToHSV(backColor, hsv);
            hsv[2] *= 0.8f;
            color = Color.HSVToColor(hsv);
        } else if (v.getId() == purple.getId()) {
            backColor = ContextCompat.getColor(NotesActivity.this, android.R.color.holo_purple);
            Color.colorToHSV(backColor, hsv);
            hsv[2] *= 0.8f;
            color = Color.HSVToColor(hsv);
        } else {
            backColor = ContextCompat.getColor(NotesActivity.this, android.R.color.darker_gray);
            Color.colorToHSV(backColor, hsv);
            hsv[2] *= 0.8f;
            color = Color.HSVToColor(hsv);
        }
        actionBar.setBackgroundColor(backColor);
        noteDetails.setBackgroundColor(backColor);
        getWindow().setStatusBarColor(color);
    }
}
