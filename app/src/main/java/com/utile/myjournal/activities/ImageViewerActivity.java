package com.utile.myjournal.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.utile.myjournal.R;
import com.utile.myjournal.database.Entries;
import com.utile.myjournal.database.MyDB;

public class ImageViewerActivity extends AppCompatActivity {

    boolean isDetailPresent = true;
    private LinearLayout linearLayout;
    private AppBarLayout appbarlayout;
    private Animation FadeInAnimation;
    private Animation FadeOutAnimation;
    private String uri;
    private ImageView imageView;
    private TextView dateandtime;
    private TextView locationText;
    private Toolbar toolbar;
    private Entries item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
        toolbar = findViewById(R.id.actionbar);
        int id = getIntent().getIntExtra("entry_Id", 0);
        uri = getIntent().getStringExtra("image_uri");
        appbarlayout = findViewById(R.id.appbarlayout);
        imageView = findViewById(R.id.fullscreenimageviewer);
        linearLayout = findViewById(R.id.detailaboutimage);
        dateandtime = findViewById(R.id.DateAndTime);
        locationText = findViewById(R.id.locationinimageviewer);
        FadeInAnimation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        FadeOutAnimation = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
        if (id == -1) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("This image is not more part of any entry saved!\nDo you want to delete it?");
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    (new enterEntryActivity()).deleteImage(uri);
                    startActivity(new Intent(ImageViewerActivity.this, MainActivity.class));
                }
            });
            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(ImageViewerActivity.this, MainActivity.class));
                }
            });
            alert.show();
        } else {
            MyDB db = new MyDB(this);
            db.open();
            item = db.getEntry(id);
            db.close();
            loadImage();

        }


        FadeInAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                linearLayout.setVisibility(View.VISIBLE);
                appbarlayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        FadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                linearLayout.setVisibility(View.GONE);
                appbarlayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void loadImage() {
        toolbar.setTitle(item.getTitle());
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitleMarginStart(8);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        imageView.setImageBitmap(BitmapFactory.decodeFile(uri, options));

        dateandtime.setText(String.format("%s:%s:%s @%s:%s",
                item.getDate().substring(6, 8),
                item.getDate().substring(4, 6),
                item.getDate().substring(0, 4),
                item.getDate().substring(8, 10),
                item.getDate().substring(10, 12)));

        locationText.setText(item.getLocation_n());

        appbarlayout.startAnimation(FadeInAnimation);
        linearLayout.startAnimation(FadeInAnimation);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isDetailPresent) {
                    appbarlayout.startAnimation(FadeOutAnimation);
                    linearLayout.startAnimation(FadeOutAnimation);
                    isDetailPresent = false;
                } else {
                    appbarlayout.startAnimation(FadeInAnimation);
                    linearLayout.startAnimation(FadeInAnimation);
                    isDetailPresent = true;
                }
            }
        });

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isDetailPresent) {
                    appbarlayout.startAnimation(FadeOutAnimation);
                    linearLayout.startAnimation(FadeOutAnimation);
                    isDetailPresent = false;
                } else {
                    appbarlayout.startAnimation(FadeInAnimation);
                    linearLayout.startAnimation(FadeInAnimation);
                    isDetailPresent = true;
                }
            }

        });
        linearLayout.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                if (isDetailPresent) {
                                                    appbarlayout.startAnimation(FadeOutAnimation);
                                                    linearLayout.startAnimation(FadeOutAnimation);
                                                    isDetailPresent = false;
                                                } else {
                                                    appbarlayout.startAnimation(FadeInAnimation);
                                                    linearLayout.startAnimation(FadeInAnimation);
                                                    isDetailPresent = true;
                                                }
                                            }
                                        }

        );
    }
}
