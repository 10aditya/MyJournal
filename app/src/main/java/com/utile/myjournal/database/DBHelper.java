package com.utile.myjournal.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DBHelper extends SQLiteOpenHelper {
    // create table query - SQLite
    private static final String CREATE_TABLE = "create table " +
            Constants.TABLE_NAME + " (" +
            Constants.KEY_ID + " integer primary key autoincrement, " +
            Constants.TITLE_NAME + " text not null, " +
            Constants.TIME_STAMP + " text not null, " +
            Constants.MOOD + " text not null, " +
            Constants.MOOD_COLOUR + " text not null, " +
            Constants.HIGHLIGHT_NAME + " text not null, " +
            Constants.NO_OF_IMAGES + " text not null, " +
            Constants.IMAGE_URI + " text not null, " +
            Constants.IMAGE2_URI + " text not null, " +
            Constants.IMAGE3_URI + " text not null, " +
            Constants.IMAGE4_URI + " text not null, " +
            Constants.LOCATION_NAME + " text not null, " +
            Constants.LOCATION_COORDINATES + " text not null, " +
            Constants.FAVOURITE + " text not null" + ")";
    //private static final String FILE_DIR = "data";

    DBHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

  /*  public DBHelper(final Context context) {
        super(context, Environment.getExternalStorageDirectory()
                + File.separator + FILE_DIR
                + File.separator + Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }
*/

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" drop table if exists " + Constants.TABLE_NAME);
        onCreate(db);
    }
}
