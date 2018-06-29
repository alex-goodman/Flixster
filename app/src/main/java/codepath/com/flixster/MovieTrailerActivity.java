package codepath.com.flixster;

import android.os.Bundle;
import android.util.Log;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class MovieTrailerActivity extends YouTubeBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_trailer);

        // test video ID
        final String videoId = "go4dmVyfkL8";

        // get the PlayerView
        YouTubePlayerView playerView = (YouTubePlayerView) findViewById(R.id.player);

        // initialize the PlayerView
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
            }
        });

    }
}
