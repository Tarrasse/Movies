package com.mahmoud.movies.data;

import android.content.ContentValues;
import android.content.Context;
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

/**
 * Created by mahmoud on 8/11/2016.
 */
public class ReviewsTask extends AsyncTask <String, Void, Void>{

    private Context mContext;

    public ReviewsTask(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected Void doInBackground(String... params) {

        String LOG_TAG = ReviewsTask.class.getSimpleName();


        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultStr = null;
        try {

            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast

            String uri = "http://api.themoviedb.org/3/movie/"+ params[0] +"/reviews?api_key=7332c3e1c7f02f72c3edbc3d8f982342";

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


            Reviews reviews = new Reviews();
            Gson gson =  new Gson();
            reviews =  gson.fromJson(resultStr, Reviews.class);
            insertDataToDataBase(reviews);

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

    private void insertDataToDataBase(Reviews reviews) {
        ArrayList<Reviews.ResultsBean> results = reviews.getResults();
        for (int i = 0; i < results.size() ; i++) {
            System.out.println("inserting " + results.get(i).getContent() + " reiew in datebse");
            ContentValues values = new ContentValues();
            values.put(MoviesContract.ReviewsTable.MOVIE_ID,
                    reviews.getMovie_id());
            values.put(MoviesContract.ReviewsTable.CONTENT,
                    results.get(i).getContent());
            values.put(MoviesContract.ReviewsTable.URI,
                    results.get(i).getUrl());
            values.put(MoviesContract.ReviewsTable.AUTHOR,
                    results.get(i).getAuthor());

            mContext.getContentResolver().insert(
                    MoviesContract.ReviewsTable.buildReviewUri(1),
                    values
            );
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
