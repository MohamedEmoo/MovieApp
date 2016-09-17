package com.example.mohamed.movieapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements On_Get_Data {
    @Override
    public void onResume() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("move_sort_chosen", Activity.MODE_PRIVATE);
        String sortPref = sharedPreferences.getString(getString(R.string.pref_movies_key), getString(R.string.pref_popular));

        if(sortPref.equals(getString(R.string.pref_popular)))
        {
            FetchMovieTask task = new FetchMovieTask();
            task.setListener(this);
            task.execute(getString(R.string.pref_popular));
        }
        else if(sortPref.equals(getString(R.string.pref_top_rated)))
        {
            FetchMovieTask task = new FetchMovieTask();
            task.setListener(this);
            task.execute(getString(R.string.pref_top_rated));
        }
        else if(sortPref.equals(getString(R.string.pref_Favorite)))
        {
            getMoviesFromDatabase();
        }
        super.onResume();
    }

    String order = "popular";
    On_Get_Data Selectedmovie;

    public MainActivityFragment() {
    }

    GridView gridview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_activity_fragment, container, false);
        gridview = (GridView) view.findViewById(R.id.gridView);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("move_sort_chosen", Activity.MODE_PRIVATE);
        String sortPref = sharedPreferences.getString(getString(R.string.pref_movies_key), getString(R.string.pref_popular));

        if(sortPref.equals(getString(R.string.pref_popular)))
        {
            FetchMovieTask task = new FetchMovieTask();
            task.setListener(this);
            task.execute(getString(R.string.pref_popular));
        }
        else if(sortPref.equals(getString(R.string.pref_top_rated)))
        {
            FetchMovieTask task = new FetchMovieTask();
            task.setListener(this);
            task.execute(getString(R.string.pref_top_rated));
        }
        else if(sortPref.equals(getString(R.string.pref_Favorite)))
        {
            getMoviesFromDatabase();
        }

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = (Movie) parent.getItemAtPosition(position);
                Bundle bundle = new Bundle();
                bundle.putString("title", movie.getTitle());
                bundle.putString("release_date", movie.getRelease_date());
                bundle.putString("poster_path", movie.getPoster_path());
                bundle.putString("vote_average", movie.getVote_average());
                bundle.putString("overview", movie.getOverview());
                bundle.putString("id", movie.getId());
                Selectedmovie.setselectedmovie(bundle);
            }
        });
        return view;
    }

    private void getMoviesFromDatabase()
    {
        Database database;
        Movie movie;
        ArrayList<Movie>movies=new ArrayList<>();
        database=new Database(getContext().getApplicationContext());
        Cursor cursor=database.fetchall_Movie();
        while(!cursor.isAfterLast())
        {
            movie=new Movie(cursor.getString(5),cursor.getString(4),cursor.getString(2),cursor.getString(1),cursor.getString(0),cursor.getString(3));
            movies.add(movie);
            cursor.moveToNext();
        }
        gridview.setAdapter(new CustomAdapter(getActivity().getApplicationContext(),movies));
    }

    @Override
    public void get_data(ArrayList<Movie> movies) {
      gridview.setAdapter(new CustomAdapter(getActivity().getApplicationContext(),movies));
    }

    @Override
    public void setselectedmovie(Bundle movie) {
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        SharedPreferences settings = getActivity().getSharedPreferences("move_sort_chosen", Activity.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = settings.edit();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_popular)
        {
            FetchMovieTask task = new FetchMovieTask();
            task.setListener(this);
            task.execute(getString(R.string.pref_popular));
            prefEditor.putString(getString(R.string.pref_movies_key), getString(R.string.pref_popular));
            prefEditor.commit();
        }
        if(id== R.id.action_top_rated)
        {
            FetchMovieTask task = new FetchMovieTask();
            task.setListener(this);
            task.execute(getString(R.string.pref_top_rated));
            prefEditor.putString(getString(R.string.pref_movies_key), getString(R.string.pref_top_rated));
            prefEditor.commit();
        }
        if (id==R.id.action_Favorite)
        {
            getMoviesFromDatabase();
            prefEditor.putString(getString(R.string.pref_movies_key), getString(R.string.pref_Favorite));
            prefEditor.commit();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_favorite, menu);
    }
        public void setnamelistenr(On_Get_Data namelistener) {
            Selectedmovie=namelistener;
        }
}