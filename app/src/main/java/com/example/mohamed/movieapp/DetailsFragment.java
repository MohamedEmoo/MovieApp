package com.example.mohamed.movieapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsFragment extends Fragment {
    ListView trailer_list;
    TextView review;
    List<String> list;
    String youtube = "https://www.youtube.com/watch?v=";
    Movie movie;
    Database database;
    public DetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_details, container, false);
         final TextView title=(TextView)view.findViewById(R.id.title);
         final ImageView poster=(ImageView)view.findViewById(R.id.poster);
         final TextView reasledate=(TextView)view.findViewById(R.id.reasledate);
         final TextView average=(TextView)view.findViewById(R.id.average);
         final TextView overview=(TextView)view.findViewById(R.id.overview);
         final CheckBox checkBox=(CheckBox)view.findViewById(R.id.favourite);
         trailer_list=(ListView)view.findViewById(R.id.trailer_list);
         review=(TextView)view.findViewById(R.id.review);
        Bundle bundle = getArguments();
        if (bundle != null) {
            final String Title = bundle.getString("title");
            final String release_date = bundle.getString("release_date");
            final String path=bundle.getString("poster_path");
            final String averag=bundle.getString("vote_average");
            final String over_view = bundle.getString("overview");
            final String id=bundle.getString("id");

            movie=new Movie(id,over_view,path,release_date,Title,averag);
            database=new Database(getContext().getApplicationContext());


            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked())
                    {
                        database.Add_Movie(movie);
                        Toast.makeText(getContext().getApplicationContext(),Title+ " Is Added" , Toast.LENGTH_SHORT).show();
                    }
                    else if(!checkBox.isChecked())
                    {
                        database.deleteMovie(Title);
                    }
                }
            });

            if (!checkBox.isChecked()) {
                if(database.check(Title))
                checkBox.setChecked(true);
            }




            title.setText(Title);
            reasledate.setText(release_date);
            Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185/" + path).into(poster);
            average.setText(averag);
            overview.setText(over_view);
            Trailer fetchTrailer=new Trailer();
            fetchTrailer.execute(id);
            Review fetchReview=new Review();
            fetchReview.execute(id);

        }
            return view;
    }
    public class Trailer extends AsyncTask<String, String[], String[]> {
        public String[] GetTrailerFromJson(String json) throws JSONException {
            String results = "results";
            String id = "key";

            JSONObject moviejson = new JSONObject(json);
            JSONArray moviearray = moviejson.getJSONArray(results);

            String[] result = new String[moviearray.length()];
            for (int i = 0; i < moviearray.length(); i++) {
                JSONObject movie = moviearray.getJSONObject(i);
                result[i] = movie.getString(id);
            }
            return result;
        }

        @Override
        protected String[] doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String JsonStr = null;
            try {
                URL url = new URL("http://api.themoviedb.org/3/movie/" + params[0] + "/videos?api_key=a5b2c50eb0e4db52be89b13b98c0f065");
                Log.d("url", String.valueOf(url));
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                JsonStr = buffer.toString();

            } catch (IOException e) {
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {

                    }
                }
            }

            try {
                return GetTrailerFromJson(JsonStr);
            } catch (JSONException e) {

                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final String[] result) {
            super.onPostExecute(result);
            list = new ArrayList<String>();
            if (result.length > 0 && result != null) {
                for (int i=1; i<= result.length ;i++)
                {
                    list.add("Trailer " + (i));
                }
                final ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_expandable_list_item_1,list);
                trailer_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String Link = youtube + result[position];
                        Log.v("link", Link);
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Link)));
                    }
                });
                trailer_list.setAdapter(arrayAdapter);
              /*  View listitem=null;
                listitem = arrayAdapter.getView(0, listitem, trailer_list);
                listitem.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));;
                Log.d("list", "listitem height = " + listitem.getHeight());
                ViewGroup.LayoutParams params=trailer_list.getLayoutParams();
                params.height=listitem.getHeight()*arrayAdapter.getCount();
                trailer_list.setLayoutParams(params);
*/
                //setListViewHeightBasedOnChildren(trailer_list);
                int h=calculateHeight(trailer_list);
                Log.d("list", "listitem height = " +h);
                ViewGroup.LayoutParams params=trailer_list.getLayoutParams();
                params.height=h*(arrayAdapter.getCount());
                trailer_list.setLayoutParams(params);
            }

        }
        public  void setListViewHeightBasedOnChildren(ListView listView) {
            ListAdapter listAdapter = listView.getAdapter();
            if (listAdapter == null)
                return;

            int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            int totalHeight = 0;
            View view = null;
            for (int i = 0; i < listAdapter.getCount(); i++) {
                view = listAdapter.getView(i, view, listView);
                if (i == 0)
                    view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, RadioGroup.LayoutParams.WRAP_CONTENT));

                view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += view.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount()));
            listView.setLayoutParams(params);
        }

        private int calculateHeight(ListView list) {

            int height = 0;

            for (int i = 0; i < list.getCount()-1; i++) {
                View childView = list.getAdapter().getView(i, null, list);
                childView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                height+= childView.getMeasuredHeight();
               // Log.d("list", "listitem height = " +h);

            }

            //dividers height
            height += list.getDividerHeight() * list.getCount();

            return height;
        }
    }

    
    public class Review extends AsyncTask<String, Void, String[]> {
        
        public String[] GetReviewFromJson(String json) throws JSONException {
            String results = "results";
            String content = "content";

            JSONObject moviejson = new JSONObject(json);
            JSONArray moviearray = moviejson.getJSONArray(results);

            String[] result = new String[moviearray.length()];
            for (int i = 0; i < moviearray.length(); i++) {
                JSONObject movie = moviearray.getJSONObject(i);
                result[i] = movie.getString(content);
            }
            return result;
        }

        @Override
        protected String[] doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String JsonStr = null;
            try {


                URL url = new URL("http://api.themoviedb.org/3/movie/" + params[0] + "/reviews?api_key=a5b2c50eb0e4db52be89b13b98c0f065");
                Log.v("url", String.valueOf(url));
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();


                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                JsonStr = buffer.toString();

            } catch (IOException e) {
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {

                    }
                }
            }

            try {
                return GetReviewFromJson(JsonStr);
            } catch (JSONException e) {

                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);
            String string = "";
            if (result.length > 0 && result != null) {
                for (int i = 0; i < result.length; i++) {
                    string += result[i] + "\n";
                }
                review.setText(string);
            }
        }
    }

}
