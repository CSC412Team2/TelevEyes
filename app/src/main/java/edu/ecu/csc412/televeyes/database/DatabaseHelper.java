package edu.ecu.csc412.televeyes.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import edu.ecu.csc412.televeyes.ApplicationSingleton;
import edu.ecu.csc412.televeyes.model.Show;

/**
 * Created by joshu on 11/3/2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper sInstance;

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    public static final String DATABASE_NAME = "data.db";

    private static final String TABLE_SHOWS = "shows_table";
    private static final String KEY_ID = "id";

    private DatabaseHelper() {
        super(ApplicationSingleton.getAppContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getInstance() {
        if (sInstance == null) {
            sInstance = new DatabaseHelper();
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CREDENTIALS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_SHOWS + "("
                + KEY_ID + " INTEGER PRIMARY KEY)";
        db.execSQL(CREATE_CREDENTIALS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO Add actual upgrade logic
        onCreate(db);
    }

    public void addShows(List<Show> shows) {
        for (int i = 0; i < shows.size(); i++) {
            addShow(shows.get(i));
        }
    }

    public void addShow(Show show) {
        SQLiteDatabase db = this.getWritableDatabase();
        String insert = "INSERT INTO " + TABLE_SHOWS
                + "(" + KEY_ID + ") VALUES (" + show.getId() + ")";

        try {
            db.execSQL(insert);
        } catch (SQLiteConstraintException e) {

        }
    }

    public void removeShow(Show show) {
        removeShow(show.getId());
    }

    public void removeShow(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = KEY_ID + "=?";
        String[] whereArgs = new String[]{String.valueOf(id)};
        db.delete(TABLE_SHOWS, whereClause, whereArgs);
    }

    public boolean isShowSaved(Show show) {
        return isShowSaved(show.getId());
    }

    public boolean isShowSaved(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = new String[]{KEY_ID};
        String whereClause = KEY_ID + "=?";
        String[] whereArgs = new String[]{String.valueOf(id)};
        Cursor c = db.query(TABLE_SHOWS, columns, whereClause, whereArgs, null, null, null);
        return c.getCount() != 0;
    }

    public List<Integer> getShowIds() {
        SQLiteDatabase db = this.getReadableDatabase();
        final List<Integer> shows = new ArrayList<>();

        Cursor c = db.query(TABLE_SHOWS, null, null, null, null, null, null);

        while (c.moveToNext()) {
            int id = c.getInt(0);
            shows.add(id);
        }

        return shows;
    }
}
