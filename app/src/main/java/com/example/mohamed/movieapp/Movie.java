package com.example.mohamed.movieapp;

/**
 * Created by Mohamed on 8/13/2016.
 */
public class Movie {
    private String title;
    private String release_date;
    private String poster_path;
    private String vote_average;
    private String overview;
    private String id;

    public Movie(String id, String overview, String poster_path, String release_date, String title, String vote_average) {
        this.id = id;
        this.overview = overview;
        this.poster_path = poster_path;
        this.release_date = release_date;
        this.title = title;
        this.vote_average = vote_average;
    }

    public String getId() {
        return id;
    }

    public String getOverview() {
        return overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getTitle() {
        return title;
    }

    public String getVote_average() {
        return vote_average;
    }
}
