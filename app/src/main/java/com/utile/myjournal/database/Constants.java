package com.utile.myjournal.database;

import com.utile.myjournal.R;

/**
 * Created by adity on 03/01/2017.
 */

public class Constants {
    static final String NOTES_TABLE_NAME = "notes";
    public static String introSlideFlag = "flagforintroductionslide";
    public static String HelpSlideFlag = "flagforhelpslide";
    static final String DATABASE_NAME = "mydiaries";
    static final String DATABASE_NAME2 = "mynotes";
    static final String TABLE_NAME = "entries";
    static final String TITLE_NAME = "title";
    static final String HIGHLIGHT_NAME = "highlight";
    static final String TIME_STAMP = "date";
    static final String MOOD = "mood";
    static final String MOOD_COLOUR = "color";
    static final String NO_OF_IMAGES = "noofimages";
    public static final String IMAGE_URI = "imageuri";
    public static final String IMAGE2_URI = "image_two_uri";
    public static final String IMAGE3_URI = "image_three_uri";
    public static final String IMAGE4_URI = "image_four_uri";
    static final String LOCATION_NAME = "location_name";
    static final String LOCATION_COORDINATES = "location";
    static final String FAVOURITE = "favourite";
    static final String KEY_ID = "_id";

    static final int DATABASE_VERSION = 1;

    public static final String SIGNEDORNOT = "signed_or_not";
    public static final String USER_PIN = "pin_key";

    public static final String USER_NAME = "user_name";

    public static final String USER_EMAIL = "user_email";


    public static final String USER_KEY = "user_key";
    public static final String USER_IMAGE_URI = "image_uri";
    public static final String flagForTapTargetView = "flag_ForTapTargetView";
    public static final String flagForTapTargetViewOnCardView = "flag_ForTapTargetViewOnCardView";
    public static final String flagForTapTargetViewInEnterEntryActivity = "flag_forTapTagetViewInEnterEntryActivity";

    public static final int[] colors = {android.R.color.darker_gray,
            R.color.material_purple,
            android.R.color.holo_blue_dark,
            R.color.colorPrimary,
            android.R.color.holo_blue_bright,
            R.color.colorPrimaryDark,
            R.color.material_darkred,
            android.R.color.holo_red_dark,
            R.color.material_green,
            R.color.material_red,
            R.color.colorAccent,
            R.color.colorPrimary,
            android.R.color.holo_green_dark,
            android.R.color.holo_green_light,
            R.color.material_darkgreen,
            android.R.color.holo_orange_dark,
            android.R.color.holo_purple,
            android.R.color.holo_red_light,
            R.color.color_custom_fragment_1,
            R.color.material_blue,
            android.R.color.holo_orange_light,
            R.color.color_dark_permissions,
            R.color.color_permissions,
            android.R.color.holo_blue_light,
            R.color.actual_greean};
}