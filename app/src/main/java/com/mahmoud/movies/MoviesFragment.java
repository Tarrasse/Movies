package com.mahmoud.movies;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.provider.Telephony;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.RadioButton;


import com.mahmoud.movies.data.MoviesContract;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private MoviesAdapter mAdapter ;

    public static int sort;

    private GridView gridView = null;
    private RadioButton popularRadiobutton = null;
    private RadioButton RatedRadioButton = null;
    private RadioButton favdRadioButton = null;

    private Button popularButton;
    private Button RatedButton;
    private Button FaveButton;

    private View mNoContent;

    public static String[] MOVIES_COLUMNS = {
            MoviesContract.RatedTable.POSTER_PATH,
            MoviesContract.RatedTable.TITLE,
            MoviesContract.RatedTable._ID
    };

    public static final int COL_POSTER_PATH = 0;
    public static final int COL_TITLE = 1;

    public static final int LOADR_FLAG = 0;


    public MoviesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null)
            sort = 1;
        getLoaderManager().initLoader(LOADR_FLAG, null, this);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        if (savedInstanceState == null)
            sort = 1;

        mAdapter = new MoviesAdapter(getActivity(), null, 1);

        // Inflate the layout for this fragment

        gridView = (GridView) rootView.findViewById(R.id.movies_gridView);
        gridView.setAdapter(mAdapter);
//        popularRadiobutton = (RadioButton) rootView.findViewById(R.id.popular_radio_button);
//        RatedRadioButton = (RadioButton) rootView.findViewById(R.id.top_rated_radio_button);
//        favdRadioButton = (RadioButton) rootView.findViewById(R.id.fav_radio_button);

        RatedButton = (Button) rootView.findViewById(R.id.top_rated_button);
        popularButton = (Button) rootView.findViewById(R.id.popular_button);
        FaveButton = (Button) rootView.findViewById(R.id.fav_button);

        mNoContent = rootView.findViewById(R.id.no_content_palce_holder);
        RatedButton.setBackgroundColor(Color.parseColor("#cccccc"));
        popularButton.setBackgroundColor(Color.parseColor("#cccccc"));
        FaveButton.setBackgroundColor(Color.parseColor("#cccccc"));


        if (sort == 1){
//            popularRadiobutton.setChecked(true);
            popularButton.setBackgroundColor(Color.parseColor("#e94d4d"));

        }else if (sort == 2){
//            RatedRadioButton.setChecked(true);
            RatedButton.setBackgroundColor(Color.parseColor("#e94d4d"));
        }else {
//            favdRadioButton.setChecked(true);
            FaveButton.setBackgroundColor(Color.parseColor("#e94d4d"));
        }


        popularButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sort = 1;
                getLoaderManager().restartLoader(LOADR_FLAG, null, MoviesFragment.this);
                RatedButton.setBackgroundColor(Color.parseColor("#cccccc"));
                FaveButton.setBackgroundColor(Color.parseColor("#cccccc"));
                popularButton.setBackgroundColor(Color.parseColor("#e94d4d"));

            }
        });

        RatedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sort = 2;
                getLoaderManager().restartLoader(LOADR_FLAG, null, MoviesFragment.this);
                popularButton.setBackgroundColor(Color.parseColor("#cccccc"));
                FaveButton.setBackgroundColor(Color.parseColor("#cccccc"));
                RatedButton.setBackgroundColor(Color.parseColor("#e94d4d"));

            }
        });

        FaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sort = 3;
                getLoaderManager().restartLoader(LOADR_FLAG, null, MoviesFragment.this);
                RatedButton.setBackgroundColor(Color.parseColor("#cccccc"));
                popularButton.setBackgroundColor(Color.parseColor("#cccccc"));
                FaveButton.setBackgroundColor(Color.parseColor("#e94d4d"));

            }
        });

//        popularRadiobutton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    RatedRadioButton.setChecked(false);
//                    favdRadioButton.setChecked(false);
//                    sort = 1;
//                    getLoaderManager().restartLoader(LOADR_FLAG, null, MoviesFragment.this);
//                }
//            }
//        });
//
//        RatedRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    popularRadiobutton.setChecked(false);
//                    favdRadioButton.setChecked(false);
//                    sort = 2;
//                    getLoaderManager().restartLoader(LOADR_FLAG, null, MoviesFragment.this);
//                }
//            }
//        });
//
//        favdRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    sort = 3;
//                    popularRadiobutton.setChecked(false);
//
//                    RatedRadioButton.setChecked(false);
//                    getLoaderManager().restartLoader(LOADR_FLAG, null, MoviesFragment.this);
//                }
//            }
//        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!MainActivity.towPane){
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    intent.putExtra(DetailFragment.POSITION, position);
                    if (sort == 1){
                        intent.setData(MoviesContract.PopularTable.CONTENT_URI);
                    }else if(sort == 2){
                        intent.setData(MoviesContract.RatedTable.CONTENT_URI);
                    }else{
                        intent.setData(MoviesContract.FavouriteTable.buildRatedUri(1));
                    }
                    startActivity(intent);
                }else{
                    DetailFragment df = new DetailFragment();
                    Bundle args = new Bundle();
                    if (MoviesFragment.sort == 1){
                        args.putParcelable(
                                DetailFragment.URI,
                                MoviesContract.PopularTable.CONTENT_URI
                        );
                    }else if(sort == 2){
                        args.putParcelable(
                                DetailFragment.URI,
                                MoviesContract.RatedTable.CONTENT_URI
                        );
                    }else{
                        args.putParcelable(
                                DetailFragment.URI,
                                MoviesContract.FavouriteTable.buildRatedUri(1)
                        );
                    }
                    args.putInt(DetailFragment.POSITION, position);
                    df.setArguments(args);
                    getFragmentManager().beginTransaction().replace(R.id.detail_place_holder, df).commit();
                }
            }
        });

        return rootView;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri;;
        if (sort == 1){
            uri = MoviesContract.PopularTable.CONTENT_URI;
        }else if(sort == 2) {
            uri = MoviesContract.RatedTable.CONTENT_URI;
        }else{
            uri = MoviesContract.FavouriteTable.buildRatedUri(1);
        }
        System.out.println("loader created");
        return new CursorLoader(
                getActivity(),
                uri,
                MOVIES_COLUMNS,
                null,
                null,
                null
        );


    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        System.out.println("load finshed");
        if(data.moveToFirst()) {
            mNoContent.setVisibility(View.GONE);
            System.out.println(data.getString(COL_TITLE));
            mAdapter.swapCursor(data);
        }
        else {
            mNoContent.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader loader) {
        System.out.println("load reset");
        mNoContent.setVisibility(View.VISIBLE);
        mAdapter.swapCursor(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        RatedButton.setBackgroundColor(Color.parseColor("#cccccc"));
        popularButton.setBackgroundColor(Color.parseColor("#cccccc"));
        FaveButton.setBackgroundColor(Color.parseColor("#cccccc"));


        if (sort == 1){
//            popularRadiobutton.setChecked(true);
            popularButton.setBackgroundColor(Color.parseColor("#e94d4d"));

        }else if (sort == 2){
//            RatedRadioButton.setChecked(true);
            RatedButton.setBackgroundColor(Color.parseColor("#e94d4d"));
        }else {
//            favdRadioButton.setChecked(true);
            FaveButton.setBackgroundColor(Color.parseColor("#e94d4d"));
        }


    }
}
