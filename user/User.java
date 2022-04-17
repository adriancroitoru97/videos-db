package user;

import entertainment.Show;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * User class containing all related information.
 */
public final class User {

    private final String username;
    private final String subscriptionType;
    private final Map<Show, Integer> history;
    private final ArrayList<Show> favoriteMovies;
    private int numberOfRatings;
    private final Map<Show, Integer> ratedShows;

    public User(final String username, final String subscriptionType) {
        this.username = username;
        this.subscriptionType = subscriptionType;
        this.history = new HashMap<>();
        this.favoriteMovies = new ArrayList<>();
        this.numberOfRatings = 0;
        this.ratedShows = new HashMap<>();
    }

    public ArrayList<Show> getFavoriteMovies() {
        return favoriteMovies;
    }

    public Map<Show, Integer> getHistory() {
        return history;
    }

    public String getUsername() {
        return username;
    }

    public int getNumberOfRatings() {
        return numberOfRatings;
    }

    public Map<Show, Integer> getRatedShows() {
        return ratedShows;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public void setNumberOfRatings(final int numberOfRatings) {
        this.numberOfRatings = numberOfRatings;
    }

    @Override
    public String toString() {
        return username;
    }
}
