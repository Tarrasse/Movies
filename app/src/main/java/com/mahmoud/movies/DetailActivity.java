package com.mahmoud.movies;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mahmoud.movies.data.MoviesContract;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Uri uri = getIntent().getData();

        DetailFragment df = new DetailFragment();
        Bundle args = new Bundle();
        if (MoviesFragment.sort == 1){
            args.putParcelable(
                    DetailFragment.URI,
                    MoviesContract.PopularTable.CONTENT_URI
            );
        }else if(MoviesFragment.sort == 2) {
            args.putParcelable(
                    DetailFragment.URI,
                    MoviesContract.RatedTable.CONTENT_URI
            );
        }else {
            args.putParcelable(
                    DetailFragment.URI,
                    MoviesContract.FavouriteTable.buildRatedUri(1)
            );
        }
        args.putInt(DetailFragment.POSITION, getIntent().getIntExtra(DetailFragment.POSITION, 0));
        df.setArguments(args);
        getFragmentManager().beginTransaction().replace(R.id.detail_place_holder, df).commit();


    }
}
