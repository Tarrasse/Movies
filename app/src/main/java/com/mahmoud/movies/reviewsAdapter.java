package com.mahmoud.movies;

import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.mahmoud.movies.data.Movies;
import com.mahmoud.movies.data.MoviesContract;
import com.mahmoud.movies.data.Reviews;

import java.util.List;

/**
 * Created by mahmoud on 8/11/2016.
 */
public class reviewsAdapter extends ArrayAdapter {


    public reviewsAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Reviews.ResultsBean resultsBean = (Reviews.ResultsBean) getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.review_list_item, parent, false);
        }
        // Lookup view for data population
        TextView title = (TextView) convertView.findViewById(R.id.review_author_name);
        TextView body = (TextView) convertView.findViewById(R.id.review_body);
        // Populate the data into the template view using the data object
        title.setText(resultsBean.getAuthor());
        body.setText(resultsBean.getContent());
        // Return the completed view to render on screen
        return convertView;
    }
}
