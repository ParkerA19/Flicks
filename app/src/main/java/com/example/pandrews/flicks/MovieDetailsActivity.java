package com.example.pandrews.flicks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pandrews.flicks.models.Config;
import com.example.pandrews.flicks.models.Movie;
import com.loopj.android.http.AsyncHttpClient;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieDetailsActivity extends AppCompatActivity implements View.OnClickListener{

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
    // config for image urls
    Config config;

    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvOverview) TextView tvOverview;
    @BindView(R.id.rbVoteAverage) RatingBar rbVoteAverage;
    @BindView(R.id.ivTrailer) ImageView ivTrailer;
    @BindView(R.id.tvReleaseDate) TextView tvReleaseDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        // initialize the client
        client = new AsyncHttpClient();

        // unwrap the movie passed in vie intent, using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format( "Showing details for %s", movie.getTitle()));

        // now unwrap the config
        config = (Config) Parcels.unwrap(getIntent().getParcelableExtra(Config.class.getSimpleName()));

        // set the title and overview
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        tvReleaseDate.setText(movie.getReleaseDate());

       // ivTrailer -- set the backdrop image
        String imageUrl = config.getImageUrl(config.getDetailsSize(), movie.getBackdropPath());



        Glide.with(getApplicationContext())
                .load(imageUrl)
                .bitmapTransform(new RoundedCornersTransformation(getApplicationContext(), 25, 0))
          //      .placeholder(placeholderId)
          //      .error(placeholderId)
                .into(ivTrailer);





        // vote average is 0..10, convert to 0..5 by dividing by 2
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f: voteAverage);
    }



    @Override
    public void onClick(View v) {
        // create intent for the new activity
        Intent intent = new Intent(this, MovieTrailerActivity.class);
        // serialize the movie using parceler, use its short name as a key
        intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
        // show the activity
        startActivity(intent);
    }
}

