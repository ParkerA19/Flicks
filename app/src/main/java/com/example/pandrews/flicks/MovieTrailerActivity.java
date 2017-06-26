package com.example.pandrews.flicks;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.pandrews.flicks.models.Movie;
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
import cz.msebera.android.httpclient.Header;

public class MovieTrailerActivity extends YouTubeBaseActivity {

    //constants
    // the base URL for the API
    public final static String API_BASE_URL = "https://api.themoviedb.org/3";
    // the parameter name for the API key
    public final static String API_KEY_PARAM = "api_key";
    // tag for logging from this activity
    public final static String TAG = "MovieListActivity";

    // instance fields
    AsyncHttpClient client;
    // the movie to display
    Movie movie;
    // the movie id
    Integer id;
    // the movie id
 //   String mid;

    // bind the view and the video together with ButterKnife
    @BindView(R.id.player) YouTubePlayerView playerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_trailer);
        ButterKnife.bind(this);

        // initialize the client
        client = new AsyncHttpClient();

        // TODO: open the intent
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format( "Showing details for %s", movie.getTitle()));

        // TODO: pull out the movieId
        id = movie.getId();
     //   mid = movie.getMid();

        // TODO: use the movieId to obtain the videoId (using the getVideos endpoint in the TMDB API)
        String url = API_BASE_URL + "/movie/" + id + "/videos";

        // make the parameters to get the JSON data and get the video id
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key));

        // execute a GET request expecting a JSON object response
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray results = null;

                // load the results into movies list
                try {
                    results = response.getJSONArray("results");
                    JSONObject object = results.getJSONObject(0);
                    final String videoId = object.getString("key");

                    // log results
                    Log.i(TAG, String.format("Loaded %s Trailer", movie.getTitle()));

                    // / initialize with API key stored in secrets.xml
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




                } catch (JSONException e) {
                    logError("Failed to parse Trailer", e, true);
                    e.printStackTrace();
                }

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed getting Trailer", throwable, true);
            }
        });

    }

        // handle errors, log and alert user
        private void logError(String message, Throwable error, boolean alertUser) {
            // always log the error
            Log.e(TAG, message, error);
            // alert the user to avoid silent errors
            if (alertUser) {
                // show a long toast with the error message
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        }
}
