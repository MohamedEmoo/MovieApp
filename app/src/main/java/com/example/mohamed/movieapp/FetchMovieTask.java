package com.example.mohamed.movieapp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Mohamed on 8/13/2016.
 */
public class FetchMovieTask extends AsyncTask<String, Void, ArrayList<Movie>> {
    private final String LOG_TAG = FetchMovieTask.class.getSimpleName();
    On_Get_Data listener;

    public void setListener(On_Get_Data listener) {
        this.listener = listener;
    }

    private  ArrayList<Movie> getMovieFromJson(String movieJsonStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String results_list = "results";
        final String title = "title";
        final String release_date = "release_date";
        final String poster_path  = "poster_path";
        final String vote_average = "vote_average";
        final String overview = "overview";
        final String id = "id";


        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray movieArray = movieJson.getJSONArray(results_list);

        ArrayList<Movie>result=new ArrayList<Movie>();
        for(int i = 0; i < movieArray.length(); i++) {
            JSONObject object = movieArray.getJSONObject(i);
            String Title = object.getString(title);
            String releaseDate = object.getString(release_date);
            String posterPath = object.getString(poster_path);
            String voteAverage = object.getString(vote_average);
            String overView = object.getString(overview);
            String iD = object.getString(id);
            Movie movie= new Movie(iD,overView,posterPath,releaseDate,Title,voteAverage);
            result.add(movie);
        }

       /* for (String s : resultStrs) {
            Log.v(LOG_TAG, "Forecast entry: " + s);
        }*/
        return result;

    }

    @Override
    protected  ArrayList<Movie> doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String mJsonStr = null;
        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            String baseUrl = "http://api.themoviedb.org/3/movie/"+params[0]+"?";
            String apiKey = "api_key=" + BuildConfig.OPEN_WEATHER_MAP_API_KEY;
            URL url = new URL(baseUrl.concat(apiKey));

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            mJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        try {
            return getMovieFromJson(mJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> movies) {

        super.onPostExecute(movies);

        if(listener != null)
            listener.get_data(movies);

    }
}
