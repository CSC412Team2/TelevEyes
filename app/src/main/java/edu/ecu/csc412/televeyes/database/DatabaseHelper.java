package edu.ecu.csc412.televeyes.database;

import android.app.FragmentManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import edu.ecu.csc412.televeyes.model.Show;
import edu.ecu.csc412.televeyes.tv.TVMaze;

/**
 * Created by joshu on 11/3/2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper sInstance;
    private final Context context;

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    public static final String DATABASE_NAME = "data.db";

    private static final String TABLE_SHOWS = "shows_table";
    private static final String KEY_ID = "id";

    private DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context);
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CREDENTIALS_TABLE = "CREATE TABLE " + TABLE_SHOWS + "("
                + KEY_ID + " INTEGER PRIMARY KEY)";
        db.execSQL(CREATE_CREDENTIALS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO Add actual upgrade logic
        onCreate(db);
    }

    public void addShows(List<Show> shows){
        for(int i = 0; i < shows.size(); i++){
            addShow(shows.get(i));
        }
    }

    public void addShow(Show show){
        SQLiteDatabase db = this.getWritableDatabase();
        String insert = "INSERT INTO " + TABLE_SHOWS
                + "(" + KEY_ID + ") VALUES (" + show.getId() + ")";
        db.execSQL(insert);
    }

    public List<Show> getShows(){
        SQLiteDatabase db = this.getReadableDatabase();
        final List<Show> shows = new ArrayList<>();

        Cursor c = db.query(TABLE_SHOWS, null, null, null, null, null, null);

        while(c.moveToNext()){
            TVMaze.getInstance().getShowFromId(c.getInt(0), new TVMaze.OnShowLookupListener() {
                @Override
                public void onResult(Show show) {
                    shows.add(show);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        return shows;
    }
}
