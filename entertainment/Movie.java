package entertainment;

import java.util.ArrayList;
import java.util.List;

/**
 * Movie class containing all generic show information and
 * specialized information for a movie.
 */
public final class Movie extends Show {

    private final int duration;
    private final List<Double> ratings;

    public Movie(final String title, final ArrayList<Genre> genres,
                 final int year, final int duration) {
        super(title, year, genres);
        this.duration = duration;
        this.ratings = new ArrayList<>();
    }

    public List<Double> getRatings() {
        return ratings;
    }

    /**
     * Seeks the list of ratings of a movie and calculates the average.
     *
     * @return the rating average of the movie
     */
    public double getRatingAverage() {
        double ratingsSum = 0;
        for (double currentRating : ratings) {
            ratingsSum += currentRating;
        }

        if (ratings.size() != 0) {
            return ratingsSum / ratings.size();
        }

        return 0;
    }

    public int getDuration() {
        return duration;
    }
}
