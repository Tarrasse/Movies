package com.mahmoud.movies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.mahmoud.movies.data.MoviesContract.*;

/**
 * Created by mahmoud on 8/8/2016.
 */
public class DataTask extends AsyncTask <Integer, Void, Void>  {

    private Context mContext;

    public DataTask(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(Integer... params) {

        String LOG_TAG = DataTask.class.getSimpleName();

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultStr = null;
        try {

            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast

            String popular = "http://api.themoviedb.org/3/movie/popular?api_key=7332c3e1c7f02f72c3edbc3d8f982342";
            String top_rated = "http://api.themoviedb.org/3/movie/top_rated?api_key=7332c3e1c7f02f72c3edbc3d8f982342";
            String uri = null;
            if(params[0] == 1 ){
                uri = popular;
            }else{
                uri = top_rated;
            }
            URL url = new URL(uri);


            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.

            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.

            }

            resultStr = buffer.toString();
            Log.d(LOG_TAG,"Json input : " + resultStr);


            Movies movies = new Movies();
            Gson gson =  new Gson();
            movies =  gson.fromJson(resultStr, Movies.class);
            insertDataToDataBase(movies, params[0]);

        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }



        return null;
    }

    public void insertDataToDataBase (Movies movies, int x ){
        SQLiteDatabase db = new MoviesDBHelper(mContext).getReadableDatabase();
        ArrayList<Movies.ResultsBean> moviesList = movies.getResults();
        ContentValues values = new ContentValues();
        if (x == 1){
            mContext.getContentResolver().delete(PopularTable.CONTENT_URI, null, null);
        }else{
            mContext.getContentResolver().delete(RatedTable.CONTENT_URI, null, null);
        }

        for (int i = 0; i < moviesList.size() ; i++) {
            Log.i("inserting data : " , moviesList.get(i).getTitle());
            values.put(RatedTable.TITLE, moviesList.get(i).getTitle() );
            values.put(RatedTable.POSTER_PATH, moviesList.get(i).getPoster_path() );
            values.put(RatedTable.RELEASE_DATE, moviesList.get(i).getRelease_date() );
            values.put(RatedTable.OVERVIEW, moviesList.get(i).getOverview());
            values.put(RatedTable.ID, moviesList.get(i).getId());
            values.put(RatedTable.VOTE, moviesList.get(i).getVote_average());
            if(x == 1){
                mContext.getContentResolver().insert(
                        PopularTable.CONTENT_URI,
                        values
                );
            }else{
                mContext.getContentResolver().insert(
                        RatedTable.CONTENT_URI,
                        values
                );
            }

        }

    }

}
