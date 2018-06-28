package codepath.com.flixster;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import codepath.com.flixster.models.Config;
import codepath.com.flixster.models.Movie;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    // base movie db URL
    public static final String API_BASE = "https://api.themoviedb.org/3";
    // the request parameter name for the API key
    public static final String API_KEY_PARAM = "api_key";
    // logging tag for this activity
    public static final String LOG_TAG = "MainActivity";

    // instance of Async Http Client
    AsyncHttpClient client;
    // base URL for loading images
    String imageBaseUrl;
    // poster size for getting images, gotten from configuration
    String posterSize;
    // list of currently playing movies
    ArrayList<Movie> movies;
    // RecyclerView for list
    RecyclerView rvMovies;
    // adapter going to the RecyclerView
    MovieAdapter adapter;
    // image config object
    Config imConfig;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize client instance
        client = new AsyncHttpClient();

        // initialize the list of movies
        movies = new ArrayList<>();

        // initialize MovieAdapter after the movies list (b/c it gets passed in), but cannot reinitialize
        adapter = new MovieAdapter(movies);

        // resolve the RecyclerView, connect a LayoutManager, add adapter
        rvMovies = findViewById(R.id.rvMovies);
        rvMovies.setLayoutManager(new LinearLayoutManager (this));
        rvMovies.setAdapter(adapter);



        // get the configuration on app creation
        getConfig();
    }

    // get the list of currently playing movies from the API
    private void getNowPlaying() {
        // start to make the URL
        String url = API_BASE + "/movie/now_playing";
        // set the request params
        RequestParams params = new RequestParams();
        // add API key
        params.put(API_KEY_PARAM, getString(R.string.api_key));
        // send the API request
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // load the results into the movies list
                try {
                    JSONArray results = response.getJSONArray("results");
                    // construct Movie objects and add to movies list
                    for (int i = 0; i < results.length(); i++) {
                        Movie movie = new Movie(results.getJSONObject(i));
                        movies.add(movie);
                        adapter.notifyItemInserted(movies.size() - 1);
                    }
                    Log.i(LOG_TAG, String.format("Loaded %s movies", results.length()));
                } catch (JSONException e) {
                    logError("Failed to parse now_playing JSON data", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed to get data from now_playing endpoint", throwable, true);
            }
        });
    }

    // get configuration from the API
    private void getConfig() {
        // start to make the URL
        String url = API_BASE + "/configuration";
        // set the request params
        RequestParams params = new RequestParams();
        // add API key
        params.put(API_KEY_PARAM, getString(R.string.api_key));
        // send the API request
        client.get(url, params, new JsonHttpResponseHandler() {
            // write our own onSuccess method
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    imConfig = new Config(response);
                    Log.i(LOG_TAG, String.format("Loaded configuration with imageBaseURL %s and posterSize %s",
                            imConfig.getImageBaseUrl(), imConfig.getPosterSize()));
                    // pass config to adapter
                    adapter.setImConfig(imConfig);
                    // get the list of movies playing now
                    getNowPlaying();
                } catch (JSONException e) {
                    logError("Failure parsing configuration", e, true);
                }
            }

            // and our own onFailure method

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failure getting configuration", throwable, true);
            }
        });
    }

    private void logError(String message, Throwable error, boolean alertUser) {
        // log the error regardless of if we show user
        Log.e(LOG_TAG, message, error);

        // alert the user of the error
        if (alertUser) {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }
}
