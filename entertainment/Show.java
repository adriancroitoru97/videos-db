package entertainment;

import actor.Actor;

import java.util.ArrayList;

/**
 * Show abstract class - a show must be either be a movie or a serial.
 * Each show is linked directly with its actors (the cast field is
 * a list of Actor objects).
 */
public abstract class Show {

    private final String title;
    private final int year;
    private final ArrayList<Actor> cast;
    private final ArrayList<Genre> genres;
    private int nrOfAddsAtFavorites;
    private int nrOfViews;

    public Show(final String title, final int year,
                final ArrayList<Genre> genres) {
        this.title = title;
        this.year = year;
        this.cast = new ArrayList<>();
        this.genres = genres;
        this.nrOfAddsAtFavorites = 0;
        this.nrOfViews = 0;
    }

    /**
     * Calculates the rating average of a show using different formulas
     * depending on the type of the show (movie/serial).
     *
     * @return the rating average of a show
     */
    public abstract double getRatingAverage();

    /**
     * Calculates the total duration of a show using different formulas
     * depending on the type of the show (movie/serial).
     *
     * @return the duration of a show
     */
    public abstract int getDuration();

    public final String getTitle() {
        return title;
    }

    public final ArrayList<Actor> getCast() {
        return cast;
    }

    public final int getYear() {
        return year;
    }

    public final ArrayList<Genre> getGenres() {
        return genres;
    }

    public final int getNrOfAddsAtFavorites() {
        return nrOfAddsAtFavorites;
    }

    public final void setNrOfAddsAtFavorites(final int nrOfAddsAtFavorites) {
        this.nrOfAddsAtFavorites = nrOfAddsAtFavorites;
    }

    public final int getNrOfViews() {
        return nrOfViews;
    }

    public final void setNrOfViews(final int nrOfViews) {
        this.nrOfViews = nrOfViews;
    }

    @Override
    public final String toString() {
        return title;
    }
}
