package com.utile.myjournal.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


/**
 * Created by Aditya on 16/06/2016.
 */

public class MyDB {

    private SQLiteDatabase db;
    private DBHelper dbhelper;

    public MyDB(Context context) {
        dbhelper = new DBHelper(context);
    }

    public void open() {
        db = dbhelper.getWritableDatabase();

    }

    public void close() {
        db.close();
    }

    //inserting new entry

    public long insertEntry(Entries entries) {
        ContentValues cv = new ContentValues();
        cv.put(Constants.TITLE_NAME, entries.getTitle());
        cv.put(Constants.TIME_STAMP, entries.getDate());
        cv.put(Constants.MOOD, entries.getMood());
        cv.put(Constants.MOOD_COLOUR, entries.getColor());
        cv.put(Constants.HIGHLIGHT_NAME, entries.getHighlights());
        cv.put(Constants.NO_OF_IMAGES, entries.getNo_of_images());
        cv.put(Constants.IMAGE_URI, entries.getImage());
        cv.put(Constants.IMAGE2_URI, entries.getImage2_uri());
        cv.put(Constants.IMAGE3_URI, entries.getImage3_uri());
        cv.put(Constants.IMAGE4_URI, entries.getImage4_uri());
        cv.put(Constants.LOCATION_NAME, entries.getLocation_n());
        cv.put(Constants.LOCATION_COORDINATES, entries.getLocation_c());
        cv.put(Constants.FAVOURITE, entries.getFavourite());
        return (db.insert(Constants.TABLE_NAME, null, cv));
    }

    // Getting single Entries
    public Entries getEntry(int id) {
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String searchQuery = "SELECT * FROM "
                + Constants.TABLE_NAME
                + " WHERE "
                + Constants.KEY_ID
                + "="
                + id;
        Cursor cursor = db.rawQuery(searchQuery, null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
        }
        // return Entry
        Entries entry = new Entries(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6),
                cursor.getString(7),
                cursor.getString(8),
                cursor.getString(9),
                cursor.getString(10),
                cursor.getString(11),
                cursor.getString(12),
                cursor.getString(13)
        );
        cursor.close();
        return entry;
    }


    //getting all entries
    public Cursor getEntries() {
        return dbhelper.getReadableDatabase().query(Constants.TABLE_NAME, null, null, null, null,
                null, Constants.TIME_STAMP + " DESC");
        //return dbhelper.getReadableDatabase().query(Constants.TABLE_NAME, null, null, null, null, null, null);
    }

 /*   // Getting All Entries
    public List<Entries> getAllEntries() {
        List<Entries> entriesList = new ArrayList<Entries>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + Constants.TABLE_NAME;

        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Entries entry = new Entries();
                entry.setID(Integer.parseInt(cursor.getString(0)));
                entry.setTitle(cursor.getString(1));
                entry.setMood(cursor.getString(2));
                entry.setDate(cursor.getString(3));
                entry.setHighlights(cursor.getString(4));
                // Adding contact to list
                entriesList.add(entry);
            } while (cursor.moveToNext());
        }

        // return contact list
        return entriesList;
    }
*/

    // Deleting single entry
    public void deleteEntry(Entries entry) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        db.delete(Constants.TABLE_NAME, Constants.KEY_ID + " = ?",
                new String[]{String.valueOf(entry.getID())});

    }

    // Updating single entry
    public int updateEntry(Entries entries) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(Constants.TITLE_NAME, entries.getTitle());
        cv.put(Constants.TIME_STAMP, entries.getDate());
        cv.put(Constants.MOOD, entries.getMood());
        cv.put(Constants.MOOD_COLOUR, entries.getColor());
        cv.put(Constants.HIGHLIGHT_NAME, entries.getHighlights());
        cv.put(Constants.NO_OF_IMAGES, entries.getNo_of_images());
        cv.put(Constants.IMAGE_URI, entries.getImage());
        cv.put(Constants.IMAGE2_URI, entries.getImage2_uri());
        cv.put(Constants.IMAGE3_URI, entries.getImage3_uri());
        cv.put(Constants.IMAGE4_URI, entries.getImage4_uri());
        cv.put(Constants.LOCATION_NAME, entries.getLocation_n());
        cv.put(Constants.LOCATION_COORDINATES, entries.getLocation_c());
        cv.put(Constants.FAVOURITE, entries.getFavourite());
        // updating row
        return db.update(Constants.TABLE_NAME, cv, Constants.KEY_ID + " = ?",
                new String[]{String.valueOf(entries.getID())});
    }

    // Getting entries Count
    public int getEntriesCount() {
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String countQuery = "SELECT  * FROM " + Constants.TABLE_NAME;
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
    }

    public Cursor getCursorfromImagePath(String image_path, String columnName) {

        SQLiteDatabase db = dbhelper.getReadableDatabase();

        String searchQuery = "SELECT * FROM "
                + Constants.TABLE_NAME
                + " WHERE "
                + columnName
                + "=\'"
                + image_path
                + "\'";

        return db.rawQuery(searchQuery, null);

    }
}
