package com.utile.myjournal.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.utile.myjournal.R;
import com.utile.myjournal.database.Entries;
import com.utile.myjournal.database.MyDB;

import java.util.Objects;

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    Toolbar toolbar;
    Entries item;
    TextView tv1, tv2, tv3;
    ImageView entry_image;
    CollapsingToolbarLayout collapseToolbar;
    MyDB myDB;
    private LinearLayout images_scroll_view;
    private ImageView secondImage;
    private ImageView thirdImage;
    private ImageView fourthImage;
    private TextView locationName;
    private Intent intent;
    private enterEntryActivity delete_images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        item = (Entries) getIntent().getExtras().getSerializable("clicked_item");
        myDB = new MyDB(DetailActivity.this);
        initializeViews();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(String.format("%s:%s:%s @%s:%s",
                item.getDate().substring(6, 8),
                item.getDate().substring(4, 6),
                item.getDate().substring(0, 4),
                item.getDate().substring(8, 10),
                item.getDate().substring(10, 12)));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailActivity.this, MainActivity.class));
                finish();
            }
        });

        if (Integer.parseInt(item.getNo_of_images()) >= 2) {
            images_scroll_view.setVisibility(View.VISIBLE);
            BitmapLoaderClass bitmapLoaderClass = new BitmapLoaderClass(item, new ImageView[]{secondImage,
                    thirdImage, fourthImage});
            bitmapLoaderClass.execute();
        }

        tv1.setText(item.getTitle());
        tv2.setText(item.getMood());
        tv3.setText(item.getHighlights());
        tv3.setTextColor(ContextCompat.getColor(this, R.color.fontc));
        tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        locationName.setText(item.getLocation_n());

        entry_image.setImageBitmap(BitmapFactory.decodeFile(item.getImage()));


        entry_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Objects.equals(item.getImage(), "null")) {
                    intent = new Intent(DetailActivity.this, ImageViewerActivity.class);
                    intent.putExtra("image_uri", item.getImage());
                    intent.putExtra("entry_Id", item.getID());
                    ActivityOptions options = ActivityOptions.makeCustomAnimation(DetailActivity.this,
                            android.R.anim.fade_in, android.R.anim.fade_out);
                    startActivity(intent, options.toBundle());
                }
            }
        });

        secondImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Objects.equals(item.getImage2_uri(), "null")) {
                    intent = new Intent(DetailActivity.this, ImageViewerActivity.class);
                    intent.putExtra("image_uri", item.getImage2_uri());
                    intent.putExtra("entry_Id", item.getID());
                    ActivityOptions options = ActivityOptions.makeCustomAnimation(DetailActivity.this,
                            android.R.anim.fade_in, android.R.anim.fade_out);
                    startActivity(intent, options.toBundle());
                }
            }
        });
        thirdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Objects.equals(item.getImage3_uri(), "null")) {
                    intent = new Intent(DetailActivity.this, ImageViewerActivity.class);
                    intent.putExtra("image_uri", item.getImage3_uri());
                    intent.putExtra("entry_Id", item.getID());
                    ActivityOptions options = ActivityOptions.makeCustomAnimation(DetailActivity.this,
                            android.R.anim.fade_in, android.R.anim.fade_out);
                    startActivity(intent, options.toBundle());
                }
            }
        });
        fourthImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Objects.equals(item.getImage4_uri(), "null")) {
                    intent = new Intent(DetailActivity.this, ImageViewerActivity.class);
                    intent.putExtra("image_uri", item.getImage4_uri());
                    intent.putExtra("entry_Id", item.getID());
                    ActivityOptions options = ActivityOptions.makeCustomAnimation(DetailActivity.this,
                            android.R.anim.fade_in, android.R.anim.fade_out);
                    startActivity(intent, options.toBundle());
                }
            }
        });
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(DetailActivity.this);

    }

    private void initializeViews() {
        tv1 = findViewById(R.id.entrytitle);
        tv2 = findViewById(R.id.entrymood);
        tv3 = findViewById(R.id.highlights_container);
        toolbar = findViewById(R.id.tool_bar);
        entry_image = findViewById(R.id.card_poster);
        collapseToolbar = findViewById(R.id.collapse_toolbar);
        images_scroll_view = findViewById(R.id.images_scroll_view);
        secondImage = findViewById(R.id.SecondImage);
        thirdImage = findViewById(R.id.ThirdImage);
        fourthImage = findViewById(R.id.FourthImage);
        locationName = findViewById(R.id.location_name);
        delete_images = new enterEntryActivity();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (!Objects.equals(item.getLocation_c(), "null")) {
            Double lat = Double.parseDouble(item.getLocation_c().substring(0, item.getLocation_c().indexOf(",") - 1));
            Double lang = Double.parseDouble(item.getLocation_c().substring(item.getLocation_c().indexOf(",") + 1, item.getLocation_c().length()));
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lat, lang)));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lang), 16.0f));
        }
    }

    private class BitmapLoaderClass extends AsyncTask<Integer, Void, Bitmap[]> {

        private final Entries item;
        private final ImageView[] imageViews;

        BitmapLoaderClass(Entries item, ImageView[] imageViews) {
            this.item = item;
            this.imageViews = imageViews;
        }

        @Override
        protected Bitmap[] doInBackground(Integer... integers) {
            Bitmap[] bitmaps = new Bitmap[Integer.parseInt(item.getNo_of_images()) - 1];
            switch (Integer.parseInt(item.getNo_of_images())) {
                case 2:
                    bitmaps[0] = BitmapFactory.decodeFile(item.getImage2_uri());
                    break;

                case 3:
                    bitmaps[0] = BitmapFactory.decodeFile(item.getImage2_uri());
                    bitmaps[1] = BitmapFactory.decodeFile(item.getImage3_uri());
                    break;

                case 4:
                    bitmaps[0] = BitmapFactory.decodeFile(item.getImage2_uri());
                    bitmaps[1] = BitmapFactory.decodeFile(item.getImage3_uri());
                    bitmaps[2] = BitmapFactory.decodeFile(item.getImage4_uri());
                    break;

                default:
                    break;
            }


            return bitmaps;
        }

        @Override
        protected void onPostExecute(Bitmap[] bitmaps) {
            super.onPostExecute(bitmaps);
            switch (Integer.parseInt(item.getNo_of_images())) {
                case 2:
                    this.imageViews[0].setImageBitmap(bitmaps[0]);
                    break;
                case 3:
                    this.imageViews[0].setImageBitmap(bitmaps[0]);
                    this.imageViews[1].setImageBitmap(bitmaps[1]);
                    break;
                case 4:
                    this.imageViews[0].setImageBitmap(bitmaps[0]);
                    this.imageViews[1].setImageBitmap(bitmaps[1]);
                    this.imageViews[2].setImageBitmap(bitmaps[2]);
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem id = menu.findItem(R.id.action_favourite);
        if (Objects.equals(item.getFavourite(), "1")) {
            id.setTitle("Delete Favourite");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.action_share:
                intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT,
                        "-> Few words which describes my whole day : \n\n"
                                + this.item.getTitle() + "\n\n-> Mood : " + this.item.getMood()
                                + "\n\n-> Date & time : " + String.format("%s:%s:%s @%s:%s",
                                this.item.getDate().substring(6, 8),
                                this.item.getDate().substring(4, 6),
                                this.item.getDate().substring(0, 4),
                                this.item.getDate().substring(8, 10),
                                this.item.getDate().substring(10, 12))
                                + "\n\n-> Location : " + this.item.getLocation_n()
                                + "\n\n-> Some of highlights of the day : \n\n"
                                + this.item.getHighlights());
                intent.setType("text/plain");

                if (!Objects.equals(this.item.getImage(), "null")) {
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + this.item.getImage()));
                    intent.setType("image/jpeg");
                }

                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(intent, "Share Entry"));
                return true;

            case R.id.action_delete:
                intent = new Intent();
                intent.setAction(Intent.ACTION_DELETE);
                myDB.open();
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
                myDB.close();
                if (!Objects.equals(this.item.getImage(), "null")) {
                    delete_images.deleteImage(this.item.getImage());
                }
                if (!Objects.equals(this.item.getImage2_uri(), "null")) {
                    delete_images.deleteImage(this.item.getImage2_uri());
                }
                if (!Objects.equals(this.item.getImage3_uri(), "null")) {
                    delete_images.deleteImage(this.item.getImage3_uri());
                }
                if (!Objects.equals(this.item.getImage4_uri(), "null")) {
                    delete_images.deleteImage(this.item.getImage4_uri());
                }
                Toast.makeText(DetailActivity.this, "Entry Deleted...", Toast.LENGTH_LONG).show();
                startActivity(new Intent(DetailActivity.this, MainActivity.class));
                finish();
                break;

            case R.id.action_edit:
                intent = new Intent(DetailActivity.this, enterEntryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("entry_to_edit", this.item);
                intent.putExtras(bundle);
                intent.putExtra("ref_value", 1);
                startActivity(intent);
                finish();
                break;

            case R.id.action_favourite:
                String fav;
                if (item.getTitle() == getResources().getString(R.string.make_favourite)) {
                    fav = "1";
                } else {
                    fav = "0";
                }
                myDB.open();
                myDB.updateEntry(new Entries(this.item.getID(),
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
                        fav));
                myDB.close();
                if (Objects.equals(fav, "1")) {
                    Toast.makeText(this, "Marked as Favourite", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DetailActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "Deleted from Favourites", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DetailActivity.this, MainActivity.class));
                    finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
