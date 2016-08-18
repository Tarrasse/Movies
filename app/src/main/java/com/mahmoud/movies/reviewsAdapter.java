package com.mahmoud.movies;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mahmoud.movies.modules.Reviews;

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
        String content =resultsBean.getContent();
//        if(content.length() >= 250){
//            body.setText(content.substring(0,250) + " ...");
//        }else
            body.setText(content);
        // Return the completed view to render on screen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            convertView.setNestedScrollingEnabled(true);
        }
        return convertView;
    }
}
