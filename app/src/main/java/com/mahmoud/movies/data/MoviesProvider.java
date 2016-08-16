package com.mahmoud.movies.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;

public class MoviesProvider extends ContentProvider {

    private static MoviesDBHelper mOpenHelper ;

    public static final int LIST_POPULAR = 100;
    public static final int LIST_RATED = 200;
    public static final int MOVIE_POPULAR = 101;
    public static final int MOVIE_RATED = 201;
    public static final int REVIEWS = 300;
    public static final int FAVOURIT = 400;



    public static final UriMatcher matcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        final UriMatcher match = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MoviesContract.CONTENT_AUTHORITY;

        match.addURI(authority, MoviesContract.PATH_POPULAR ,LIST_POPULAR);
        match.addURI(authority, MoviesContract.PATH_RATED ,LIST_RATED);

        match.addURI(authority, MoviesContract.PATH_POPULAR + "/#" , MOVIE_POPULAR );
        match.addURI(authority, MoviesContract.PATH_RATED + "/#" , MOVIE_RATED );

        match.addURI(authority, MoviesContract.PATH_REVIEWS + "/#", REVIEWS);
        match.addURI(authority, MoviesContract.PATH_FAV + "/#", FAVOURIT);

        return match;
    }

    public MoviesProvider() {
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rows = 0;
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        switch (matcher.match(uri)){
            case LIST_POPULAR:
                rows = db.delete(MoviesContract.PopularTable.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case LIST_RATED:
                rows =  db.delete(MoviesContract.RatedTable.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case REVIEWS :
                rows =  db.delete(MoviesContract.ReviewsTable.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case FAVOURIT :
                rows =  db.delete(MoviesContract.FavouriteTable.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("unknewn uri :" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rows;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        SQLiteDatabase db =  mOpenHelper.getWritableDatabase();

        switch (matcher.match(uri)){
            case LIST_POPULAR:
               db.insert(MoviesContract.PopularTable.TABLE_NAME,
                        null,
                        values);
                break;
            case LIST_RATED:
                db.insert(MoviesContract.RatedTable.TABLE_NAME,
                        null,
                        values);
                break;
            case REVIEWS :
                db.insert(MoviesContract.ReviewsTable.TABLE_NAME,
                        null,
                        values);
                break;
            case FAVOURIT :
                db.insert(MoviesContract.FavouriteTable.TABLE_NAME,
                        null,
                        values);
                break;
            default:
                throw new UnsupportedOperationException("unknewn uri :" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return uri;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper =  new MoviesDBHelper(getContext());
        return false;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor = null;
        switch (matcher.match(uri)){
            case LIST_POPULAR:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MoviesContract.PopularTable.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case LIST_RATED:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MoviesContract.RatedTable.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case MOVIE_POPULAR:
                System.out.println("provider popular");
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MoviesContract.PopularTable.TABLE_NAME,
                        projection,
                        MoviesContract.PopularTable._ID + " = '" + ContentUris.parseId(uri) + "'",
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case MOVIE_RATED:
                System.out.println("provider rated");
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MoviesContract.RatedTable.TABLE_NAME,
                        projection,
                        MoviesContract.PopularTable._ID + " = '" + ContentUris.parseId(uri) + "'",
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case REVIEWS :
                System.out.println("reviews scoop provider ");
//                retCursor = mOpenHelper.getReadableDatabase().rawQuery("SELECT * FROM " +
//                        MoviesContract.ReviewsTable.TABLE_NAME + " WHERE " +
//                        MoviesContract.ReviewsTable.MOVIE_ID + " = " +
//                        ContentUris.parseId(uri),
//                        null,
//                        null
//                );
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MoviesContract.ReviewsTable.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                if (retCursor == null){
                    System.out.println("query broblem");
                }else {
                    System.out.println("no query broblem");
                }
                break;
            case FAVOURIT:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MoviesContract.FavouriteTable.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            default:
                System.out.println(uri);
                throw new UnsupportedOperationException("Unknewn uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
