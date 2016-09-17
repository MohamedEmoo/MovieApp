package com.example.mohamed.movieapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mohamed on 8/27/2016.
 */
public class Database extends SQLiteOpenHelper {

    private static String databaseName="favorite";
    private SQLiteDatabase sql_database;


    public Database(Context context) {
        super(context, databaseName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Movie(MovieID integer primary key,title text not null,release_date text not null,poster_path text not null,vote_average text not null,overview text not null,id text not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Movie");
        onCreate(db);
    }

    public void Add_Movie(Movie movie )
    {
        sql_database = getWritableDatabase();
        ContentValues row = new ContentValues();
        row.put("title", movie.getTitle());
        row.put("release_date", movie.getRelease_date());
        row.put("poster_path", movie.getPoster_path());
        row.put("vote_average", movie.getVote_average());
        row.put("overview", movie.getOverview());
        row.put("id", movie.getId());
        sql_database.insert("Movie", null, row);
        sql_database.close();
    }

    public Cursor fetchall_Movie()
    {
        sql_database = getReadableDatabase();
        String[] rowdetails = {"title","release_date","poster_path","vote_average","overview","id"};
        Cursor cursor = sql_database.query("Movie", rowdetails, null, null, null, null, null);
        if(cursor != null)
        {
            cursor.moveToFirst();
        }
        sql_database.close();
        return cursor;
    }
    public void deleteMovie(String title)
    {
        sql_database=getWritableDatabase();
        sql_database.delete("Movie", "title='" + title + "'", null);
        sql_database.close();
    }

    public Boolean check(String title) {
        sql_database=getReadableDatabase();
        String n;
        Cursor cursor=sql_database.rawQuery("Select title from Movie", null);
        if(cursor.moveToFirst())
        {
            do {
                n=cursor.getString(0);
                if (n.equals(title)) {
                    return true;
                }
            } while (cursor.moveToNext());
        }
        cursor.moveToFirst();
        return false;
    }
}
