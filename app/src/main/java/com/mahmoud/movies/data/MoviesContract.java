package com.mahmoud.movies.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by mahmoud on 8/8/2016.
 */
public class MoviesContract {

    public static final String CONTENT_AUTHORITY = "com.mahmoud.movies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_POPULAR = "popular";
    public static final String PATH_RATED = "rated";
    public static final String PATH_REVIEWS = "reviews";
    public static final String PATH_FAV = "favourite";





    public static class PopularTable implements BaseColumns{
        public static final String TABLE_NAME = "popular";
        public static final String TITLE = "title";
        public static final String RELEASE_DATE = "date";
        public static final String OVERVIEW = "overview";
        public static final String ADULTS = "adults";
        public static final String VOTE = "vote";
        public static final String POSTER_PATH = "poster";
        public static final String ID = "movieid";
        public static final String FAV = "favourit";


        public static final Uri CONTENT_URI =  BASE_CONTENT_URI.buildUpon().appendPath(PATH_POPULAR).build();

        public static Uri buildPopularUri(long id) {
            //  ContentUris contains convenience methods for appending id values to a URI
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


    }

    public static class RatedTable implements BaseColumns{
        public static final String TABLE_NAME = "rated";
        public static final String TITLE = "title";
        public static final String RELEASE_DATE = "date";
        public static final String OVERVIEW = "overview";
        public static final String ADULTS = "adults";
        public static final String VOTE = "vote";
        public static final String POSTER_PATH = "poster";
        public static final String ID = "movieid";
        public static final String FAV = "favourit";

        public static final Uri CONTENT_URI =  BASE_CONTENT_URI.buildUpon().appendPath(PATH_RATED).build();

        public static Uri buildRatedUri(long id) {
            //  ContentUris contains convenience methods for appending id values to a URI
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }public static class FavouriteTable implements BaseColumns{
        public static final String TABLE_NAME = "favourite";
        public static final String TITLE = "title";
        public static final String RELEASE_DATE = "date";
        public static final String OVERVIEW = "overview";
        public static final String ADULTS = "adults";
        public static final String VOTE = "vote";
        public static final String POSTER_PATH = "poster";
        public static final String ID = "movieid";


        public static final Uri CONTENT_URI =  BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAV).build();

        public static Uri buildRatedUri(long id) {
            //  ContentUris contains convenience methods for appending id values to a URI
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static class ReviewsTable implements BaseColumns{

        public static final String TABLE_NAME = "reviews";
        public static final String MOVIE_ID = "movie_id";
        public static final String AUTHOR = "author";
        public static final String CONTENT = "content";
        public static final String URI = "uri";


        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEWS).build();

        public static Uri buildReviewUri(long id) {
            //  ContentUris contains convenience methods for appending id values to a URI
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

}
