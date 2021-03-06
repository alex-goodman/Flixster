package codepath.com.flixster.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel // makes class parcelable
public class Movie {

    // values from API (fields have to be public for Parceler)
    String title;
    String overview;
    String posterPath; //not the actual image
    String backdropPath; // again, not the actual image
    Double voteAverage; // average rating
    Integer id; // movie id

    // no-arg constructor
    public Movie() {}

    // initialize the movie from the API JSON data
    public Movie(JSONObject object) throws JSONException {
        title = object.getString("title");
        overview = object.getString("overview");
        posterPath = object.getString("poster_path");
        backdropPath = object.getString("backdrop_path");
        voteAverage = object.getDouble("vote_average");
        id = object.getInt("id");
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public Integer getId() {
        return id;
    }
}
