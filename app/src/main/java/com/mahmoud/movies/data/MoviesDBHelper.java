package com.mahmoud.movies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.mahmoud.movies.data.MoviesContract.*;

/**
 * Created by mahmoud on 8/8/2016.
 */
public class MoviesDBHelper extends SQLiteOpenHelper {

    private static String DBName = "moviesDB.db";

    private static int version = 6;

    private String CreatePopularTable = "CREATE TABLE " + PopularTable.TABLE_NAME +
            " ( " + PopularTable._ID + " INTEGER PRIMARY KEY, " +
            PopularTable.TITLE + " TEXT NOT NULL, "+
            PopularTable.ID + " INTEGER, " +
            PopularTable.OVERVIEW + " TEXT NOT NULL, " +
            PopularTable.POSTER_PATH + " TEXT NOT NULL, " +
            PopularTable.RELEASE_DATE + " TEXT NOT NULL, " +
            PopularTable.FAV + "  INTEGER, " +
            PopularTable.VOTE + " REAL );";

    private String CreateFAVTable = "CREATE TABLE " + FavouriteTable.TABLE_NAME +
            " ( " + PopularTable._ID + " INTEGER PRIMARY KEY, " +
            PopularTable.TITLE + " TEXT NOT NULL, "+
            PopularTable.ID + " INTEGER UNIQUE, " +
            PopularTable.OVERVIEW + " TEXT NOT NULL, " +
            PopularTable.POSTER_PATH + " TEXT NOT NULL, " +
            PopularTable.RELEASE_DATE + " TEXT NOT NULL, " +
            PopularTable.FAV + "  INTEGER, " +
            PopularTable.VOTE + " REAL );";

    private String CreateRateTable = "CREATE TABLE " + RatedTable.TABLE_NAME +
            " ( " + RatedTable._ID + " INTEGER PRIMARY KEY, " +
            RatedTable.TITLE + " TEXT NOT NULL, "+
            RatedTable.ID + " INTEGER, " +
            RatedTable.OVERVIEW + " TEXT NOT NULL, " +
            RatedTable.POSTER_PATH + " TEXT NOT NULL, " +
            RatedTable.RELEASE_DATE + " TEXT NOT NULL, " +
            PopularTable.FAV + "  INTEGER, " +
            RatedTable.VOTE + " REAL );";

    private String CreateReviewsTable = "CREATE TABLE " + ReviewsTable.TABLE_NAME +
            " ( " + ReviewsTable._ID + " INTEGER PRIMARY KEY, " +
            ReviewsTable.MOVIE_ID + " TEXT NOT NULL, " +
            ReviewsTable.AUTHOR + " TEXT NOT NULL, " +
            ReviewsTable.CONTENT + " TEXT UNIQUE NOT NULL, " +
            ReviewsTable.URI + " TEXT NOT NULL );";



    public MoviesDBHelper(Context context) {
        super(context, DBName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CreatePopularTable);
        db.execSQL(CreateRateTable);
        db.execSQL(CreateReviewsTable);
        db.execSQL(CreateFAVTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RatedTable.TABLE_NAME + " ;");
        db.execSQL("DROP TABLE IF EXISTS " + PopularTable.TABLE_NAME + " ;");
        db.execSQL("DROP TABLE IF EXISTS " + ReviewsTable.TABLE_NAME + " ;");
        db.execSQL("DROP TABLE IF EXISTS " + FavouriteTable.TABLE_NAME + " ;");
        onCreate(db);
    }
}
