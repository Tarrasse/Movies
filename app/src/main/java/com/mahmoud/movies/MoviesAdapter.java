package com.mahmoud.movies;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by mahmoud on 8/10/2016.
 */
public class MoviesAdapter extends CursorAdapter {


    public MoviesAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.movie_grid_item, parent, false);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //TextView tv = (TextView) view.findViewById(R.id.list_item_movie_name);
        ImageView iv = (ImageView) view.findViewById(R.id.list_item_movie_image);

        //tv.setText(cursor.getString(MoviesFragment.COL_TITLE));
        Picasso.with(context).load("http://image.tmdb.org/t/p/w185/" + cursor.getString(MoviesFragment.COL_POSTER_PATH)).into(iv);

    }


}

















