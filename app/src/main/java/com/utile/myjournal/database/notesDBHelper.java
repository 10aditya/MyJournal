package com.utile.myjournal.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by adity on 23/07/2017.
 */

public class notesDBHelper extends SQLiteOpenHelper {

    private static final String CREATE_TABLE = "create table " +
            Constants.NOTES_TABLE_NAME + " (" +
            Constants.KEY_ID + " integer primary key autoincrement, " +
            Constants.TITLE_NAME + " text not null, " +
            Constants.TIME_STAMP + " text not null, " +
            Constants.MOOD_COLOUR + " text not null, " +
            Constants.HIGHLIGHT_NAME + " text not null, " +
            Constants.FAVOURITE + " text not null" + ")";

    notesDBHelper(Context context) {
        super(context, Constants.DATABASE_NAME2, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" drop table if exists " + Constants.NOTES_TABLE_NAME);
        onCreate(db);
    }
}
