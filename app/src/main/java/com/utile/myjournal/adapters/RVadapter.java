package com.utile.myjournal.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.utile.myjournal.R;
import com.utile.myjournal.activities.DetailActivity;
import com.utile.myjournal.activities.MainActivity;
import com.utile.myjournal.activities.NotesActivity;
import com.utile.myjournal.database.Constants;
import com.utile.myjournal.database.Entries;
import com.utile.myjournal.database.Notes;

import java.util.ArrayList;
import java.util.Objects;


/**
 * Created by Aditya on 29/06/2016.
 */

public class RVadapter extends RecyclerView.Adapter<RVadapter.ViewHolder> {
    private SharedPreferences preferences;
    private String flag;
    private ArrayList<Entries> mmEntries;
    public static Context context;
    private View vw;
    private int type;
    private ArrayList<Notes> notes;

    public RVadapter(Context context, ArrayList<Notes> notes, int type) {
        this.notes = notes;
        this.type = type;
        RVadapter.context = context;
    }

    public RVadapter(ArrayList<Entries> mEntries, Context context, int type) {
        this.mmEntries = mEntries;
        RVadapter.context = context;
        this.type = type;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        flag = preferences.getString(Constants.flagForTapTargetViewOnCardView, "false");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        vw = LayoutInflater.from(parent.getContext()).inflate(R.layout.quick_view, parent, false);
        View v;
        if (type == 1)
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_viewer, parent, false);
        else
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notescardview, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (type == 1) {
            Entries movie = mmEntries.get(position);
            String date = movie.getDate();
            holder.title.setText(movie.getTitle());
            holder.date.setText(String.format("%s:%s:%s",
                    date.substring(6, 8),
                    date.substring(4, 6),
                    date.substring(0, 4)
            ));
            holder.cv.setBackgroundColor(Integer.parseInt(movie.getColor()));
            holder.rl.setImageBitmap(BitmapFactory.decodeFile(movie.getImage()));
            if (Objects.equals(movie.getFavourite(), "1")) {
                holder.fav_sign.setVisibility(View.VISIBLE);
            } else {
                holder.fav_sign.setVisibility(View.INVISIBLE);
            }
            holder.time.setText(String.format("%s:%s", date.substring(8, 10), date.substring(10, 12)));
            holder.rel.setBackgroundResource(R.drawable.gradient);

            if (Objects.equals(flag, "false")) {
                TapTargetView.showFor((Activity) context,
                        TapTarget.forView(holder.cv, "Long click for quick view.")
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
                        new TapTargetView.Listener() {
                            @Override
                            public void onTargetClick(TapTargetView view) {
                                super.onTargetClick(view);
                            }
                        });
                SharedPreferences.Editor spe = preferences.edit();
                spe.putString(Constants.flagForTapTargetViewOnCardView, "true");
                spe.apply();
            }
        } else {
            Notes movie = notes.get(position);
            holder.title.setText(movie.getTitle());
            holder.date.setText(movie.getHighlights());
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.curved_corners);
            if (Integer.parseInt(movie.getColor()) != 0)
                drawable.setColorFilter(Integer.parseInt(movie.getColor()), PorterDuff.Mode.MULTIPLY);
            holder.cv.setBackground(drawable);
            holder.ll.setBackground(drawable);

        }
    }

    @Override
    public int getItemCount() {
        return type == 1 ? mmEntries.size() : notes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView date;
        LinearLayout cv, vcv;
        CardView ll;
        RelativeLayout rel;
        ImageView rl;
        ImageView fav_sign;
        TextView time;
        Handler handler;

        ViewHolder(final View itemView) {
            super(itemView);
            if (type == 1) {
                this.title = itemView.findViewById(R.id.card_title);
                this.date = itemView.findViewById(R.id.card_date);
                this.rel = itemView.findViewById(R.id.rlForGradient);
                this.rl = itemView.findViewById(R.id.card_poster);
                this.cv = itemView.findViewById(R.id.back_of_card);
                this.ll = itemView.findViewById(R.id.card_view);
                this.time = itemView.findViewById(R.id.card_time);
                this.fav_sign = itemView.findViewById(R.id.favourite_sign);
            } else {
                this.title = itemView.findViewById(R.id.notestitle);
                this.date = itemView.findViewById(R.id.notesdetail);
                this.cv = itemView.findViewById(R.id.layoutForNotesBackground);
                this.vcv = itemView.findViewById(R.id.main);
                this.ll = itemView.findViewById(R.id.s3);
            }
            itemView.setOnClickListener(this);
            handler = new Handler();

            itemView.setOnLongClickListener(
                    new View.OnLongClickListener() {
                @SuppressLint("DefaultLocale")
                @Override
                public boolean onLongClick(View v) {
                    if (type == 1) {

                        View c = itemView.getRootView();
                        View rv = c.findViewById(android.R.id.content);
                        Drawable window = rv.getBackground();
                        MainActivity.blurView.setVisibility(View.VISIBLE);
                        MainActivity.blurView.setupWith(rv)
                                .windowBackground(window)
                                .blurRadius(8.9f);
                        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                        ImageView imageView = vw.findViewById(R.id.quick_view_image);

                        Entries clickedItem = mmEntries.get(getAdapterPosition());
                        Bitmap bit = BitmapFactory.decodeFile(clickedItem.getImage());
                        imageView.setImageBitmap(bit);
                        TextView textView = vw.findViewById(R.id.quick_view_title);
                        TextView textView2 = vw.findViewById(R.id.quick_view_mood);
                        TextView textView3 = vw.findViewById(R.id.quick_view_highlights);
                        TextView textView4 = vw.findViewById(R.id.no_of_images_Shower);
                        ImageView imageView1 = vw.findViewById(R.id.fav_sign_InQuickView);
                        textView.setText(clickedItem.getTitle());
                        textView2.setText(clickedItem.getMood());
                        textView3.setText(clickedItem.getHighlights());

                        if (Integer.parseInt(clickedItem.getNo_of_images()) > 1) {
                            textView4.setText(String.format(" + %d", Integer.parseInt(clickedItem.getNo_of_images()) - 1));
                        } else {
                            textView4.setVisibility(View.GONE);
                        }

                        if (Objects.equals(clickedItem.getFavourite(), "1"))
                            imageView1.setVisibility(View.VISIBLE);
                        alertDialog.setView(vw);
                        final AlertDialog dialog = alertDialog.create();
                        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                MainActivity.blurView.setBackgroundColor(
                                        ContextCompat.getColor(context, R.color.transparent));
                                MainActivity.blurView.setVisibility(View.INVISIBLE);

                            }
                        });

                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {

                                MainActivity.blurView.setBackgroundColor(
                                        ContextCompat.getColor(context, R.color.transparent));
                                MainActivity.blurView.setVisibility(View.INVISIBLE);
                            }
                        });

                        if (vw.getParent() != null) {
                            ((ViewGroup) vw.getParent()).removeView(vw);
                        }
                        itemView.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                                    dialog.dismiss();
                                    MainActivity.blurView.setBackgroundColor(
                                            ContextCompat.getColor(context, R.color.transparent));
                                    MainActivity.blurView.setVisibility(View.INVISIBLE);
                                }
                                return false;
                            }
                        });

                        dialog.show();
                        vibrator.vibrate(40);

                    }
                    return true;
                }
            });

        }


        @Override
        public void onClick(View v) {

            Intent i;
            Entries clickedItem;
            Notes note;
            Bundle bundle = new Bundle();
            if (type == 1) {
                clickedItem = mmEntries.get(getAdapterPosition());
                bundle.putSerializable("clicked_item", clickedItem);
                i = new Intent(context, DetailActivity.class);
                i.putExtra("type", true);
                i.putExtra("movie_id", clickedItem.getID());
                i.putExtras(bundle);
                ActivityOptions options;
                options = ActivityOptions
                        .makeSceneTransitionAnimation((Activity) context,
                                v.findViewById(R.id.card_poster),
                                "testing");
                context.startActivity(i, options.toBundle());
            } else {
                note = notes.get(getAdapterPosition());
                bundle.putSerializable("clicked_item", note);
                i = new Intent(context, NotesActivity.class);
                i.putExtras(bundle);
                i.putExtra("note_id", note.getId());
                i.putExtra("type", true);
                context.startActivity(i);
            }
            //Log.d("ListAdapter", getAdapterPosition() + " ");
        }

    }
}
