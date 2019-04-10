package com.utile.myjournal.fragments;


import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.utile.myjournal.R;
import com.utile.myjournal.activities.MainActivity;
import com.utile.myjournal.database.Constants;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class EntryAndNotesFragment extends Fragment {


    private View V;
    private SharedPreferences preferences;

    public EntryAndNotesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        V = inflater.inflate(R.layout.fragment_entry_and_notes, container, false);
        Fragment fragment = new EntryFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.EntryAndNotesFrame, fragment);
        fragmentTransaction.commitAllowingStateLoss();

        ImageButton entryButton = V.findViewById(R.id.EntryButton);
        ImageButton notesButton = V.findViewById(R.id.NotesButton);

        final Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.custom_fadeout);
        //animation.setFillAfter(true);
        final Animation animation2 = AnimationUtils.loadAnimation(getContext(), R.anim.custom_fadein);
        //animation2.setFillAfter(true);

        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String flag = preferences.getString(Constants.flagForTapTargetView, "false");
        if (Objects.equals(flag, "false")) {
            giveIntro();
        }

        entryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.toggle = false;
                MainActivity.title_image.setText("Journal");
                Fragment fragment = new EntryFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.EntryAndNotesFrame, fragment);
                fragmentTransaction.commitAllowingStateLoss();

                MainActivity.fab.startAnimation(animation);
                MainActivity.fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark)));
                MainActivity.fab.startAnimation(animation2);
            }
        });
        notesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.toggle = true;
                MainActivity.title_image.setText("Notes");
                Fragment fragment = new NotesFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.EntryAndNotesFrame, fragment);
                fragmentTransaction.commitAllowingStateLoss();
                MainActivity.fab.startAnimation(animation);
                MainActivity.fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.colorAccent)));
                MainActivity.fab.startAnimation(animation2);
            }
        });
        return V;
    }

    private void giveIntro() {
        new TapTargetSequence(this.getActivity())
                .targets(
                        TapTarget.forView(V.findViewById(R.id.EntryButton), "This is journal section", "Here you'll see all journal entries you have saved.")
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
                        TapTarget.forView(V.findViewById(R.id.NotesButton), "This is notes section", "Here you'll see all notes you have saved.")
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
                        TapTarget.forView(MainActivity.imageNavigationButton, "Gallery", "Here you'll see all images you have saved in your journal.")
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
                        TapTarget.forView(MainActivity.fab, "Blue Button", "If you want to add new Journal entry, click blue button in journal section.")
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
                                .targetRadius(60))
                .listener(new TapTargetSequence.Listener() {
                    // This listener will tell us when interesting(tm) events happen in regards
                    // to the sequence
                    @Override
                    public void onSequenceFinish() {
                        MainActivity.toggle = true;
                        MainActivity.title_image.setText("Notes");
                        Fragment fragment = new NotesFragment();
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                                android.R.anim.fade_out);
                        fragmentTransaction.replace(R.id.EntryAndNotesFrame, fragment);
                        fragmentTransaction.commitAllowingStateLoss();
                        //MainActivity.fab.startAnimation(animation);
                        MainActivity.fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.colorAccent)));
                        //MainActivity.fab.startAnimation(animation2);

                        TapTargetView.showFor(getActivity(),
                                TapTarget.forView(MainActivity.fab, "Pink Button", "If you want to add new Note, click pink button in notes section.")
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
        spe.putString(Constants.flagForTapTargetView, "true");
        spe.apply();
    }

}
