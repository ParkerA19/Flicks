package com.example.pandrews.flicks;

import android.os.Bundle;
import android.util.Log;

import com.example.pandrews.flicks.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.parceler.Parcels;

public class MovieTrailerActivity extends YouTubeBaseActivity {

    Movie movie;
    Integer id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_trailer);

        // TODO: open the intent
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format( "Showing details for %s", movie.getTitle()));

        // TODO: pull out the movieId
        id = movie.getId();
        // TODO: use the movieId to obtain the videoId (using the getVideos endpoint in the TMDB API)

        // temporary test video id --  TODO replace with movie trailer video id
        final String videoId = "tkodtNFpzBA";

        // resolve the player view from the layout
        YouTubePlayerView playerView = (YouTubePlayerView) findViewById(R.id.player);

        // initialize with API key stored in secrets.xml
        playerView.initialize(getString(R.string.youtube_api_key), new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                // do any work here to cue video, play video, etc/.
                youTubePlayer.cueVideo(videoId);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                // log the error
                Log.e("MovieTrailerActivity", "Error initializing YouTube player");
            }
        });

    }
}
