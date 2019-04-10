package com.utile.myjournal.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.utile.myjournal.R;
import com.utile.myjournal.database.Constants;

import java.util.Objects;

public class LauncherActivity extends AppCompatActivity implements View.OnKeyListener {

    public ImageView imageView;
    float XofIcon, YofIcon;
    public static EditText passwordField;
    private SharedPreferences sp;
    private int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = PreferenceManager.getDefaultSharedPreferences(this);

        if (!Objects.equals(sp.getString(Constants.introSlideFlag, "0"), "11")) {
            Intent i = new Intent(LauncherActivity.this, introActivity.class);
            startActivity(i);
            finish();
        } else if (!Objects.equals(sp.getString(Constants.SIGNEDORNOT, "false"), "true")) {
            Intent i = new Intent(LauncherActivity.this, introActivity.class);
            startActivity(i);
            finish();
        }
        setContentView(R.layout.activity_launcher);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        height = dm.heightPixels;

        passwordField = findViewById(R.id.password_field);
        imageView = findViewById(R.id.appIcon);
        animate();
    }


    private void animate() {
        passwordField.setOnKeyListener(this);
        XofIcon = imageView.getX();
        YofIcon = imageView.getY();
        TranslateAnimation animation = new TranslateAnimation(
                XofIcon,
                XofIcon,
                (YofIcon + 250 * height / 1920),
                YofIcon
        );
        animation.setDuration(600);
        animation.setFillAfter(true);
        imageView.startAnimation(animation);
        Animation fadeAnimation = AnimationUtils.loadAnimation(this, R.anim.text_view_animation);
        passwordField.startAnimation(fadeAnimation);
    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_ENTER:
                    if (Objects.equals(passwordField.getText().toString(), sp.getString(Constants.USER_KEY, ""))) {
                        startActivity(new Intent(LauncherActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LauncherActivity.this, "Wrong PIN!", Toast.LENGTH_SHORT).show();
                    }
            }
        }
        return false;
    }
}
