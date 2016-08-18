package com.mahmoud.movies.data;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mahmoud.movies.modules.Movies;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MoviesService extends IntentService {

    public MoviesService() {
        super("MoviesService");
    }

    public static final String QUERY_PARAM = "qp";


    @Override
    protected void onHandleIntent(Intent intent) {
        String LOG_TAG = MoviesService.class.getSimpleName();

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
            if(intent.getIntExtra(QUERY_PARAM, 1) == 1 ){
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


            Movies movies ;
            Gson gson =  new Gson();
            movies =  gson.fromJson(resultStr, Movies.class);
            Log.i(LOG_TAG, resultStr);
            insertDataToDataBase(movies, intent.getIntExtra(QUERY_PARAM, 1));

        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            Toast.makeText(this, "CONNECTION ERROR", Toast.LENGTH_LONG).show();
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

    }
    public void insertDataToDataBase (Movies movies, int x ){
//        SQLiteDatabase db = new MoviesDBHelper(this).getReadableDatabase();
        ArrayList<Movies.ResultsBean> moviesList = movies.getResults();
        ContentValues values = new ContentValues();
        if (x == 1){
            getContentResolver().delete(MoviesContract.PopularTable.CONTENT_URI, null, null);
        }else{
            getContentResolver().delete(MoviesContract.RatedTable.CONTENT_URI, null, null);
        }

        for (int i = 0; i < moviesList.size() ; i++) {
            Log.i("inserting data : " , moviesList.get(i).getTitle());
            values.put(MoviesContract.RatedTable.TITLE, moviesList.get(i).getTitle() );
            values.put(MoviesContract.RatedTable.POSTER_PATH, moviesList.get(i).getPoster_path() );
            values.put(MoviesContract.RatedTable.RELEASE_DATE, moviesList.get(i).getRelease_date() );
            values.put(MoviesContract.RatedTable.OVERVIEW, moviesList.get(i).getOverview());
            values.put(MoviesContract.RatedTable.ID, moviesList.get(i).getId());
            values.put(MoviesContract.RatedTable.VOTE, moviesList.get(i).getVote_average());
            if(x == 1){
                getContentResolver().insert(
                        MoviesContract.PopularTable.CONTENT_URI,
                        values
                );
            }else{
                getContentResolver().insert(
                        MoviesContract.RatedTable.CONTENT_URI,
                        values
                );
            }

        }

    }

    public static class AlarmReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Intent popIntent = new Intent(context, MoviesService.class);
            popIntent.putExtra(MoviesService.QUERY_PARAM, 1);
            context.startService(popIntent);
            Intent ratedIntent = new Intent(context, MoviesService.class);
            ratedIntent.putExtra(MoviesService.QUERY_PARAM, 2);
            context.startService(ratedIntent);
        }
    }


}
