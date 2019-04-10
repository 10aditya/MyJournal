package com.utile.myjournal.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by adity on 23/07/2017.
 */

public class notesDB {
    private SQLiteDatabase sqLiteDatabase;
    private notesDBHelper notesDBHelper;

    public notesDB(Context context) {
        notesDBHelper = new notesDBHelper(context);
    }

    public void open() {
        sqLiteDatabase = notesDBHelper.getWritableDatabase();
    }

    public void close() {
        sqLiteDatabase.close();
    }

    public long insertNote(Notes notes) {
        ContentValues cv = new ContentValues();
        cv.put(Constants.TITLE_NAME, notes.getTitle());
        cv.put(Constants.TIME_STAMP, notes.getTimestamp());
        cv.put(Constants.MOOD_COLOUR, notes.getColor());
        cv.put(Constants.HIGHLIGHT_NAME, notes.getHighlights());
        cv.put(Constants.FAVOURITE, notes.getFavourite());
        return (sqLiteDatabase.insert(Constants.NOTES_TABLE_NAME, null, cv));
    }

    public Notes getNote(int id) {
        SQLiteDatabase db = notesDBHelper.getReadableDatabase();
        String searchQuery = "SELECT * FROM "
                + Constants.NOTES_TABLE_NAME
                + " WHERE "
                + Constants.KEY_ID
                + "="
                + id;
        Cursor cursor = db.rawQuery(searchQuery, null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
        }
        // return Entry
        Notes notes = new Notes(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5)
        );
        cursor.close();
        return notes;
    }

    public Cursor getNotes() {
        return notesDBHelper.getReadableDatabase().query(Constants.NOTES_TABLE_NAME, null, null, null, null,
                null, Constants.TIME_STAMP + " DESC");
    }

    public void deleteEntry(Notes notes) {
        SQLiteDatabase db = notesDBHelper.getWritableDatabase();
        db.delete(Constants.NOTES_TABLE_NAME, Constants.KEY_ID + " = ?",
                new String[]{String.valueOf(notes.getId())});

    }

    public int getNotesCount() {
        SQLiteDatabase db = notesDBHelper.getReadableDatabase();
        String countQuery = "SELECT  * FROM " + Constants.NOTES_TABLE_NAME;
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;

    }

    public int updateNote(Notes note) {
        SQLiteDatabase db = notesDBHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(Constants.TITLE_NAME, note.getTitle());
        cv.put(Constants.TIME_STAMP, note.getTimestamp());
        cv.put(Constants.MOOD_COLOUR, note.getColor());
        cv.put(Constants.HIGHLIGHT_NAME, note.getHighlights());
        cv.put(Constants.FAVOURITE, note.getFavourite());
        // updating row
        return db.update(Constants.NOTES_TABLE_NAME, cv, Constants.KEY_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
    }

}
