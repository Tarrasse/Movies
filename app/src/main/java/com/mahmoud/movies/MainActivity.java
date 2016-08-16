package com.mahmoud.movies;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.mahmoud.movies.data.DataTask;
import com.mahmoud.movies.data.MoviesContract;

public class MainActivity extends AppCompatActivity {

    public static boolean towPane = false ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int pos ;
        int sort;
        if (savedInstanceState == null){
            DataTask task = new DataTask(this);
            task.execute(1);
            DataTask task1 = new DataTask(this);
            task1.execute(2);
        }else {
            pos = savedInstanceState.getInt(DetailFragment.POSITION);
            sort = savedInstanceState.getInt("sort");
        }


        FragmentTransaction moviesFT = getFragmentManager().beginTransaction();

        MoviesFragment moviesFragment = new MoviesFragment();
        moviesFT.replace(R.id.movies_place_holder, moviesFragment);
        moviesFT.commit();

        if(findViewById(R.id.detail_place_holder) != null){
            towPane = true;
            DetailFragment df = new DetailFragment();
            Bundle args = new Bundle();
            if (MoviesFragment.sort == 3){
                args.putParcelable(
                        DetailFragment.URI,
                        MoviesContract.FavouriteTable.buildRatedUri(1)
                );
            }else if(MoviesFragment.sort == 2) {
                args.putParcelable(
                        DetailFragment.URI,
                        MoviesContract.RatedTable.CONTENT_URI
                );
            }else {
                args.putParcelable(
                        DetailFragment.URI,
                        MoviesContract.PopularTable.CONTENT_URI
                );
            }
            args.putInt(DetailFragment.POSITION, 0);
            df.setArguments(args);
            getFragmentManager().beginTransaction().replace(R.id.detail_place_holder, df).commit();

        }else{
            towPane = false;
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refresh, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh  ) {
            DataTask task = new DataTask(this);
            task.execute(1);
            DataTask task1 = new DataTask(this);
            task1.execute(2);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(DetailFragment.POSITION, DetailFragment.mposition);
        outState.putInt("sort", DetailFragment.mSort);

    }


}
