package com.example.mohamed.movieapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Mohamed on 8/12/2016.
 */
public class CustomAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Movie> movies;
    public CustomAdapter(Context mContext, ArrayList<Movie> movies) {
        this.mContext = mContext;
        this.movies = movies;

    }

    CustomAdapter(Context c) {
        mContext = c;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Movie getItem(int position) {
        return movies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        // if it's not recycled, initialize some attributes
        // imageView = new ImageView(mContext);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_fragment, null);
        }
        imageView = (ImageView) convertView.findViewById(R.id.imageView);
        String path="http://image.tmdb.org/t/p/w185/"+movies.get(position).getPoster_path();
        Picasso.with(mContext).load(path).into(imageView);
        return convertView;
    }


}
