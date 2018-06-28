package codepath.com.flixster;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.parceler.Parcels;

import java.util.ArrayList;

import codepath.com.flixster.models.Config;
import codepath.com.flixster.models.GlideApp;
import codepath.com.flixster.models.Movie;
import codepath.com.flixster.models.MovieDetailsActivity;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    // list of movies
    ArrayList<Movie> movies;
    // image config needed for images
    Config imConfig;
    // context from parent
    Context context;

    public void setImConfig(Config imConfig) {
        this.imConfig = imConfig;
    }

    // initialize with list
    public MovieAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    // creates and inflates a new view
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // get the context and create the inflater
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // create the view using the item_movie layout
        View movieView = inflater.inflate(R.layout.item_movie, viewGroup, false);

        return new ViewHolder(movieView);
    }

    // binds an inflated view to a new item (just sets up the data in the layout/UI)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        // get the movie data at a specified position
        Movie movie = movies.get(i);
        // populate the view with the movie data
        viewHolder.tvTitle.setText(movie.getTitle());
        viewHolder.tvOverview.setText(movie.getOverview());

        // determine device orientation
        boolean isPortrait = (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);

        // set image url based on orientation
        String imageUrl = isPortrait ? imConfig.getImageUrl(imConfig.getPosterSize(), movie.getPosterPath()) :
                imConfig.getImageUrl(imConfig.getBackdropSize(), movie.getBackdropPath());

        // determine which placeholder and imageView to use from orientation
        int placeholderId = isPortrait ? R.drawable.flicks_movie_placeholder : R.drawable.flicks_backdrop_placeholder;
        ImageView imageView = isPortrait ? viewHolder.ivPoster : viewHolder.ivBackdrop;

        // load image using glide
        GlideApp.with(context)
                .load(imageUrl)
                .transform(new RoundedCornersTransformation(15, 0))
                .placeholder(placeholderId)
                .error(placeholderId)
                .into(imageView);

    }

    // returns the number of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    // create ViewHolder as a static inner class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // track view objects
        ImageView ivPoster;
        ImageView ivBackdrop;
        TextView tvTitle;
        TextView tvOverview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // lookup view objects by id
            ivPoster = (ImageView) itemView.findViewById(R.id.ivPoster);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvOverview = (TextView) itemView.findViewById(R.id.tvOverview);
            ivBackdrop = (ImageView) itemView.findViewById(R.id.ivBackdrop);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // get position of item in list
            int position = getAdapterPosition();

            // get movie at that position
            Movie movie = movies.get(position);

            // Intent to display details page
            Intent intent = new Intent(context, MovieDetailsActivity.class);

            // package up the movie to send to the details page
            intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));

            // change activities
            context.startActivity(intent);
        }
    }
}
