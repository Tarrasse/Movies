package com.mahmoud.movies;


import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.gson.Gson;
import com.mahmoud.movies.data.MoviesContract;

import com.mahmoud.movies.data.MoviesDBHelper;
import com.mahmoud.movies.modules.Reviews;
import com.mahmoud.movies.modules.Videos;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private TextView mNameTextView;
    private TextView mDateTextView;
    private TextView mRateTextView;
    private TextView mOverViewTextView;
    private ImageView mPosterImageView;
    private VideoView mVideoView;
    public static int mposition ;
    private Button mFAVButton;
    private ListView vediosListVoew ;
    public static int mSort;
    private TextView mReviewTV;
    private TextView mVedioTV;
    private ScrollView mScroll;
    private ListView lv;
    private LinearLayout mList;

    private reviewsAdapter adapter ;

    private Uri mUri = null;

    public static String URI = "uri";
    public static String POSITION = "position";

    public DetailFragment() {
        // Required empty public constructor
    }

    public static String[] MOViE_COLUMNS = {
            MoviesContract.PopularTable._ID,
            MoviesContract.PopularTable.TITLE,
            MoviesContract.PopularTable.POSTER_PATH,
            MoviesContract.PopularTable.VOTE,
            MoviesContract.PopularTable.OVERVIEW,
            MoviesContract.PopularTable.RELEASE_DATE,
            MoviesContract.PopularTable.ID,
            MoviesContract.PopularTable.FAV
    };

    public static final int COL_TITLE = 1;
    public static final int COL_POSTER_PATH = 2;
    public static final int COL_VOTE = 3;
    public static final int COL_OVERVIEW = 4;
    public static final int COL_DATE = 5;
    public static final int COL_ID = 6;
    public static final int COL_FAV = 7;

    private String mOverView ;
    private String mTitle ;
    private String mPosrPath ;
    private String mVote ;
    private String mMovieID ;

    private ArrayAdapter<String> videos_Adapter ;

    private boolean fav = false;

    private String mDate ;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView =  inflater.inflate(R.layout.fragment_detail, container, false);

//        adapter = new reviewsAdapter(getActivity(), R.id.reviews_list_view, new ArrayList());
//
//        lv = (ListView) rootView.findViewById(R.id.reviews_list_view);
//        lv.setAdapter(adapter);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            lv.setNestedScrollingEnabled(true);
//        }
        mList = (LinearLayout) rootView.findViewById(R.id.list_view_replacement);
        mFAVButton = (Button) rootView.findViewById(R.id.fav_button);
        mNameTextView = (TextView) rootView.findViewById(R.id.movie_name_text_View);
        mDateTextView = (TextView) rootView.findViewById(R.id.movie_date_textView);
        mRateTextView = (TextView) rootView.findViewById(R.id.movie_rate_textView);
        mOverViewTextView = (TextView) rootView.findViewById(R.id.movie_overView_textView);
        mPosterImageView = (ImageView) rootView.findViewById(R.id.Movie_logo_imageView);
        vediosListVoew = (ListView)rootView.findViewById(R.id.trailers_list_view);
        videos_Adapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.vedio_list_item,
                new ArrayList<String>()
        );
        vediosListVoew.setAdapter(videos_Adapter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            vediosListVoew.setNestedScrollingEnabled(true);
        }

        mVedioTV = (TextView) rootView.findViewById(R.id.detail_video_tv);
        mReviewTV = (TextView) rootView.findViewById(R.id.detail_reviews_tv);

        mFAVButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(fav);
                if (!fav) {

                    ContentValues values = new ContentValues();
                    values.put(MoviesContract.PopularTable.OVERVIEW, mOverView);
                    values.put(MoviesContract.PopularTable.ID, mMovieID);
                    values.put(MoviesContract.PopularTable.FAV, 1);
                    values.put(MoviesContract.PopularTable.VOTE, mVote);
                    values.put(MoviesContract.PopularTable.POSTER_PATH, mPosrPath);
                    values.put(MoviesContract.PopularTable.RELEASE_DATE, mDate);
                    values.put(MoviesContract.PopularTable.TITLE, mTitle);


                    getActivity().getContentResolver().insert(
                            MoviesContract.FavouriteTable.buildRatedUri(1),
                            values);
                    Toast.makeText(getActivity(), "INSERTED", Toast.LENGTH_LONG).show();
                    fav = true;
                }else{
                    getActivity().getContentResolver().delete(
                            MoviesContract.FavouriteTable.buildRatedUri(1),
                            MoviesContract.PopularTable.ID +"= ?",
                            new String[]{
                                    String.valueOf(mMovieID)
                            });
                    if(mposition >= 1 && MoviesFragment.sort == 3) {
                        mposition--;
                    }
                    fav = false;
                    Toast.makeText(getActivity(), "DELETED", Toast.LENGTH_LONG).show();

                }

                getLoaderManager().restartLoader(0, null, DetailFragment.this);
            }
        });

        Bundle bundle = getArguments();
        mUri = bundle.getParcelable(URI);
        mposition = bundle.getInt(POSITION);
        System.out.println(mUri);
        mScroll = (ScrollView) rootView.findViewById(R.id.scrollView);
        getLoaderManager().initLoader(0, null, this);

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),
                mUri,
                MOViE_COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(! data.moveToFirst()){
            System.out.println("no data");

            if(fav){
                mFAVButton.setBackgroundColor(Color.parseColor("#e94d4d"));


            }else{
                mFAVButton.setBackgroundColor(Color.parseColor("#cccccc"));

            }

            return;
        }
        try{
            data.move(mposition);
            mPosrPath = data.getString(COL_POSTER_PATH);
            Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185/" + data.getString(DetailFragment.COL_POSTER_PATH)).into(mPosterImageView);
            mOverView = data.getString(COL_OVERVIEW);
            mTitle = data.getString(COL_TITLE);
            mDate = data.getString(COL_DATE);
            mVote = data.getString(COL_VOTE);
            mNameTextView.setText(mTitle);
            mOverViewTextView.setText(mOverView);
            mDateTextView.setText(mDate);
            mRateTextView.setText(mVote + " / 10");
            mMovieID = data.getString(COL_ID);

        }catch (Exception e){
            System.out.println(e);
        }


        SQLiteDatabase db;
        try {

            db =  new MoviesDBHelper(getActivity()).getWritableDatabase();
            Cursor cursor =  db.query(MoviesContract.FavouriteTable.TABLE_NAME,
                    MOViE_COLUMNS,
                    MoviesContract.FavouriteTable.ID + "= ?" ,
                    new String[]{
                            mMovieID
                    },
                    null,
                    null,
                    null
            );

            if(cursor.moveToFirst()){
                mFAVButton.setBackgroundColor(Color.parseColor("#e94d4d"));
                System.out.println(1);
                fav =true;
            }else{
                mFAVButton.setBackgroundColor(Color.parseColor("#cccccc"));
                System.out.println(0);
                fav = false;
            }
            db.close();
        }catch (Exception e){
            System.out.println(e);
        }

        getActivity().getContentResolver().delete(MoviesContract.ReviewsTable.buildReviewUri(1),null,null);
        ReviewsTask task = new ReviewsTask(getActivity());
        task.execute(String.valueOf(mMovieID));
        VideosTask tsk = new VideosTask(getActivity());
        tsk.execute(String.valueOf(mMovieID));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public class ReviewsTask extends AsyncTask<String, Void, Reviews> {

        private Context mContext;

        public ReviewsTask(Context mContext) {
            this.mContext = mContext;
        }

        Reviews ret = null;

        @Override
        protected Reviews doInBackground(String... params) {

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
                ret = reviews;
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


            return ret;
        }


        @Override
        protected void onPostExecute(final Reviews reviews) {
            super.onPostExecute(reviews);
            if (reviews != null){
                if((reviews.getResults().size() > 0)){

                    mReviewTV.setVisibility(View.VISIBLE);

                    reviewsAdapter ad = new reviewsAdapter(getActivity(),R.layout.review_list_item, reviews.getResults());

                    for (int i = 0; i < ad.getCount() ; i++) {
                        View v = ad.getView(i, null, mList);
                        mList.addView(v);

                    }

//                    adapter.addAll(reviews.getResults());
//
//                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            Log.i("vedios task" , reviews.getResults().get(position).getUrl());
//                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(reviews.getResults().get(position).getUrl())));
//                        }
//                    });

                    mScroll.scrollTo(0,0);


                }
            }
        }
    }

    public class VideosTask extends AsyncTask<String, Void, Videos> {

        private Context mContext;

        public VideosTask(Context mContext) {
            this.mContext = mContext;
        }

        Videos ret = null;

        @Override
        protected Videos doInBackground(String... params) {

            String LOG_TAG = ReviewsTask.class.getSimpleName();


            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String resultStr = null;
            try {

                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast

                String uri = "http://api.themoviedb.org/3/movie/"+ params[0] +"/videos?api_key=7332c3e1c7f02f72c3edbc3d8f982342";

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


                Videos videos ;
                Gson gson =  new Gson();
                videos =  gson.fromJson(resultStr, Videos.class);
                ret = videos;
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


            return ret;
        }


        @Override
        protected void onPostExecute(final Videos videos) {
            super.onPostExecute(videos);
            if (videos != null){

                mVedioTV.setVisibility(View.VISIBLE);

                final List<Videos.ResultsBean> res = videos.getResults();

                ArrayList<String> vediosNames = new ArrayList<>();

                vediosNames.clear();
                for (int i = 0; i <res.size() ; i++) {
                    vediosNames.add(res.get(i).getName());
                }

                videos_Adapter.clear();
                videos_Adapter.addAll(vediosNames);

                vediosListVoew.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" +
                                res.get(position).getKey())));
                    }
                });
            }
        }
    }

}
