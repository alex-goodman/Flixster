package codepath.com.flixster.models;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import codepath.com.flixster.MovieTrailerActivity;
import codepath.com.flixster.R;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static codepath.com.flixster.MainActivity.API_BASE;
import static codepath.com.flixster.MainActivity.API_KEY_PARAM;

public class MovieDetailsActivity extends AppCompatActivity {

    Movie movie;
    AsyncHttpClient client;
    String videoId;
    Config imConfig;
    String imageUrl;

    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvOverview) TextView tvOverview;
    @BindView(R.id.rbVoteAverage) RatingBar rbVoteAverage;
    @BindView(R.id.ivBackdrop) ImageView ivBackdrop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        // initialize client instance
        client = new AsyncHttpClient();

        ButterKnife.bind(this);

        // get the movie passed in from intent
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        imConfig = (Config) Parcels.unwrap(getIntent().getParcelableExtra("config"));

        Log.d("MovieDetailsActivity", String.format("Showing details for %s", movie.getTitle()));

        // set the info on the page from the movie passed in
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());

        imageUrl = imConfig.getImageUrl(imConfig.getBackdropSize(), movie.getBackdropPath());

        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage > 0 ? voteAverage / 2.0f : 0);

        // load image using glide
        GlideApp.with(MovieDetailsActivity.this)
                .load(imageUrl)
                .transform(new RoundedCornersTransformation(15, 0))
                .placeholder(R.drawable.flicks_backdrop_placeholder)
                .error(R.drawable.flicks_backdrop_placeholder)
                .into(ivBackdrop);
    }


    @OnClick(R.id.ivBackdrop)
    void onClickBackdrop() {
        // API request string
        String url = API_BASE + String.format("/movie/%s/videos", movie.getId());
        // set request params
        RequestParams params = new RequestParams();
        // add API key
        params.put(API_KEY_PARAM, getString(R.string.youtube_key));
        // send the request
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray results = response.getJSONArray("results");
                    videoId = (results.length() > 0) ? results.getJSONObject(0).getString("key") : null;
                    Log.i("MovieDetailsActivity", "Successfully retrieved YouTube id");

                    // send intent to start video
                    Intent intent  = new Intent(MovieDetailsActivity.this, MovieTrailerActivity.class);
                    intent.putExtra("id", videoId);
                    startActivity(intent);
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
