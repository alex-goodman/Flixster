package codepath.com.flixster.models;

import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import codepath.com.flixster.R;
import cz.msebera.android.httpclient.Header;

import static codepath.com.flixster.MainActivity.API_BASE;
import static codepath.com.flixster.MainActivity.API_KEY_PARAM;

public class MovieDetailsActivity extends YouTubeBaseActivity {

    Movie movie;
    AsyncHttpClient client;
    String videoId;

    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvOverview) TextView tvOverview;
    @BindView(R.id.rbVoteAverage) RatingBar rbVoteAverage;
    @BindView(R.id.player) YouTubePlayerView playerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        // initialize client instance
        client = new AsyncHttpClient();

        ButterKnife.bind(this);

        // get the movie passed in from intent
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));

        Log.d("MovieDetailsActivity", String.format("Showing details for %s", movie.getTitle()));

        // set the info on the page from the movie passed in
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());

        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage > 0 ? voteAverage / 2.0f : 0);

        //// Start request for video key, then if successful, load the video
        // API request string
        String url = API_BASE + String.format("/movie/%s/videos", movie.getId());
        // set request params
        RequestParams params = new RequestParams();
        // add API key
        params.put(API_KEY_PARAM, getString(R.string.api_key));
        // send the request
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray results = response.getJSONArray("results");
                    videoId = (results.length() > 0) ? results.getJSONObject(0).getString("key") : null;
                    Log.i("MovieDetailsActivity", "Successfully retrieved YouTube id");

                    playerView.initialize(getString(R.string.youtube_key), new YouTubePlayer.OnInitializedListener() {
                        @Override
                        public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean b) {
                            // here is where we cue the video and all that fun
                            player.cueVideo(videoId);
                        }

                        @Override
                        public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult result) {
                            // log error
                            Log.e("MovieTrailerActivity", "Error initiaiizing YouTube player");
                            Toast.makeText(getApplicationContext(),"Error initializing YouTube player", Toast.LENGTH_SHORT);
                        }
                    });
                } catch (JSONException e) {
                    Log.e("MovieDetailsActivity", "Error getting video ID");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("MovieDetailsActivity", "Failed to retrieve data from videos endpoint");
            }
        });

    }
}
