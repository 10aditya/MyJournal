package com.utile.myjournal.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.app.OnNavigationBlockedListener;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;
import com.heinrichreimersoftware.materialintro.slide.Slide;
import com.utile.myjournal.R;
import com.utile.myjournal.database.Constants;
import com.utile.myjournal.fragments.ChangePINFragment;
import com.utile.myjournal.fragments.LoginFragment;

public class introActivity extends IntroActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor spe = sp.edit();
        spe.putString(Constants.introSlideFlag, "11");
        spe.apply();
        addSlide(new SimpleSlide.Builder()
                .title("Welcome to Memorandum")
                .description("You can add description of your whole day with suitable title, couple of pictures, mood and location. Also you can note anything here.")
                .background(R.color.material_blue)
                .backgroundDark(R.color.colorPrimaryDark)
                .build());


        final Slide permissionsSlide;
        permissionsSlide = new SimpleSlide.Builder()
                .title(R.string.title_permissions)
                .description(R.string.description_permissions)
                .background(R.color.color_permissions)
                .backgroundDark(R.color.color_dark_permissions)
                .permissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE})
                .build();
        addSlide(permissionsSlide);

        final Slide loginSlide;
        loginSlide = new FragmentSlide.Builder()
                .background(R.color.color_custom_fragment_1)
                .backgroundDark(R.color.color_dark_custom_fragment_1)
                .fragment(LoginFragment.newInstance())
                .build();
        addSlide(loginSlide);

        final Slide pinSlide = new FragmentSlide.Builder()
                .background(R.color.material_green)
                .backgroundDark(R.color.material_darkgreen)
                .fragment(ChangePINFragment.newInstance())
                .build();
        addSlide(pinSlide);

        addOnNavigationBlockedListener(new OnNavigationBlockedListener() {
            @Override
            public void onNavigationBlocked(int position, int direction) {
                View contentView = findViewById(android.R.id.content);
                if (contentView != null) {
                    Slide slide = getSlide(position);

                    if (slide == permissionsSlide) {
                        Snackbar.make(contentView, R.string.label_grant_permissions, Snackbar.LENGTH_LONG).show();
                    } else if (slide == loginSlide) {
                        Snackbar.make(contentView, R.string.label_fill_out_form, Snackbar.LENGTH_LONG).show();
                    }else if(slide == pinSlide){
                        Snackbar.make(contentView, R.string.enter_pin, Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        startActivity(new Intent(this,LauncherActivity.class));
    }
}
