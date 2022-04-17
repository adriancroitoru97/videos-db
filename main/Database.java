package main;

import fileio.UserInputData;
import fileio.ActorInputData;
import fileio.Input;
import fileio.MovieInputData;
import fileio.SerialInputData;
import user.User;
import actor.Actor;
import entertainment.Movie;
import entertainment.Serial;
import entertainment.Show;

import utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The databased used for storing the data from input.
 */
public final class Database {

    private final List<Actor> actorsData = new ArrayList<>();
    private final List<User> usersData = new ArrayList<>();
    private final List<Show> showsData = new ArrayList<>();

    /**
     * Initializer of the database.
     *
     * @param input input read from JSON files
     */
    public Database(final Input input) {

        /* Add the movies into the show field of the database. */
        for (MovieInputData currentMovie : input.getMovies()) {
            Movie newMovie = new Movie(currentMovie.getTitle(),
                    Utils.stringListToGenreList(currentMovie.getGenres()),
                    currentMovie.getYear(), currentMovie.getDuration());
            showsData.add(newMovie);
        }

        /* Add the serials into the show field of the database. */
        for (SerialInputData currentSerial : input.getSerials()) {
            Serial newSerial = new Serial(currentSerial.getTitle(),
                    Utils.stringListToGenreList(currentSerial.getGenres()),
                    currentSerial.getNumberSeason(), currentSerial.getSeasons(),
                    currentSerial.getYear());
            showsData.add(newSerial);
        }

        /* Add the actors into the actor field of the database and
         * links every new actor with its filmography (Show objects) */
        for (ActorInputData currentActor : input.getActors()) {
            Actor newActor = new Actor(currentActor.getName(),
                    currentActor.getCareerDescription(), currentActor.getAwards());

            for (String showName : currentActor.getFilmography()) {
                Show show = Utils.stringToShow((ArrayList<Show>) showsData, showName);
                if (show != null) {
                    newActor.getFilmography().add(show);
                    show.getCast().add(newActor);
                }
            }

            actorsData.add(newActor);
        }

        /* Add the users into the user field of the database. It also
         * initializes the user's history and favorites fields (list and map of Shows)
         * and updates the total number of views and adds at favorites for each show. */
        for (UserInputData currentUser : input.getUsers()) {
            User newUser = new User(currentUser.getUsername(), currentUser.getSubscriptionType());

            for (String showName : currentUser.getFavoriteMovies()) {
                Show show = Utils.stringToShow((ArrayList<Show>) showsData, showName);
                if (show != null) {
                    newUser.getFavoriteMovies().add(show);
                    show.setNrOfAddsAtFavorites(show.getNrOfAddsAtFavorites() + 1);
                }
            }

            for (Map.Entry<String, Integer> entry : currentUser.getHistory().entrySet()) {
                Show currentShow =
                        Utils.stringToShow((ArrayList<Show>) showsData, entry.getKey());
                if (currentShow != null) {
                    currentShow.setNrOfViews(currentShow.getNrOfViews() + entry.getValue());
                }
                newUser.getHistory().put(currentShow, entry.getValue());
            }

            usersData.add(newUser);
        }
    }


    public List<Show> getShowsData() {
        return showsData;
    }

    public List<Actor> getActorsData() {
        return actorsData;
    }

    public List<User> getUsersData() {
        return usersData;
    }
}
