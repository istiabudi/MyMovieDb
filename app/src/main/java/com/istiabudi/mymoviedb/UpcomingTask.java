package com.istiabudi.mymoviedb;

import android.os.AsyncTask;
import android.util.Log;

import com.istiabudi.mymoviedb.Utility.DCM;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class UpcomingTask extends AsyncTask<Void, Void, ArrayList<Movie>> {

    private IUpcoming searchListener;

    public UpcomingTask(IUpcoming searchListener) {
        this.searchListener = searchListener;
    }

    @Override
    protected ArrayList<Movie> doInBackground(Void... voids) {
        ArrayList<Movie> searchResult = new ArrayList<>();

        try {

            String urlString = "https://api.themoviedb.org/3/movie/upcoming?api_key="
                    + DCM.TMDB_API_KEY
                    + "&language=en-US";

            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder contents = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                contents.append(line).append('\n');
            }
            reader.close();
            connection.disconnect();

            try {
                JSONObject resultObject = new JSONObject(contents.toString());
                JSONArray moviesArray = resultObject.getJSONArray("results");
                for (int i = 0; i < moviesArray.length(); i++) {
                    JSONObject movieObject = moviesArray.getJSONObject(i);
                    Movie movie = new Movie(
                            movieObject.getInt("id"),
                            movieObject.getString("title"),
                            movieObject.getString("original_title"),
                            movieObject.getString("overview"),
                            movieObject.getString("release_date"),
                            movieObject.getString("poster_path")
                    );
                    searchResult.add(movie);
                }
            } catch (Exception ex) {
                Log.e(DCM.AppTag, "Unable to parse JSON: " + ex.getMessage());
            }

        } catch (IOException e) {
            Log.e( DCM.AppTag, "Unable to get search result");
            return null;
        }

        return searchResult;
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> movies) {
        if(this.searchListener != null)
            this.searchListener.onUpcomingResult(movies);
    }

    public interface IUpcoming {
        void onUpcomingResult(ArrayList<Movie> movies);
    }
}
