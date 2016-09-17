package com.example.mohamed.movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements On_Get_Data {
    boolean flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FrameLayout movie_detail =(FrameLayout)findViewById(R.id.movie_detail);
        if (null==movie_detail)
        {
            flag=false;
        }
        else
        {
            flag=true;
        }
        if (savedInstanceState==null) {
            MainActivityFragment mainActivityFragment=new MainActivityFragment();
            mainActivityFragment.setnamelistenr(this);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, mainActivityFragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void get_data(ArrayList<Movie> movies) {

    }

    @Override
    public void setselectedmovie(Bundle movie) {
        if(flag==false) {
            Intent intent = new Intent(getApplicationContext(), Details.class);
            intent.putExtras(movie);
            startActivity(intent);
        }
        else
        {
             DetailsFragment detailsfragment=new DetailsFragment();
            detailsfragment.setArguments(movie);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail,detailsfragment).commit();
        }
    }
}
