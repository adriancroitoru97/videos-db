package actions;

import actor.Actor;
import common.Constants;
import entertainment.Genre;
import entertainment.Movie;
import entertainment.Serial;
import entertainment.Show;
import fileio.ActionInputData;
import fileio.Writer;
import main.Database;
import user.User;
import org.json.simple.JSONArray;
import utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

import java.util.stream.Stream;

public abstract class Queries extends ActionsHandler {

    public Queries(final ArrayList<ActionInputData> actions, final Writer fileWriter,
                   final JSONArray jsonArray, final Database database) {
        super(actions, fileWriter, jsonArray, database);
    }

    /**
     * Generic method which takes any type of show-related query
     * and filters and sorts the database accordingly.
     *
     * @param currentAction the current show query
     */
    public static void show(final ActionInputData currentAction) throws IOException {
        Stream<Show> sortedShows = getDatabase().getShowsData().stream();

        if (currentAction.getObjectType().equals("movies")) {
            sortedShows = sortedShows.filter(show -> show instanceof Movie);
        } else {
            sortedShows = sortedShows.filter(show -> show instanceof Serial);
        }

        if (currentAction.getFilters().get(Constants.YEAR_FILTER).get(0) != null) {
            String filterYear = currentAction.getFilters().get(Constants.YEAR_FILTER).get(0);
            if (filterYear != null) {
                sortedShows = sortedShows
                        .filter(show -> show.getYear() == Integer.parseInt(filterYear));
            }
        }

        if (currentAction.getFilters().get(Constants.GENRE_FILTER).get(0) != null) {
            Genre filterGenre = Utils
                    .stringToGenre(currentAction.getFilters().get(Constants.GENRE_FILTER).get(0));
            if (filterGenre != null) {
                sortedShows = sortedShows.filter(show -> show.getGenres().contains(filterGenre));
            } else {
                sortedShows = Stream.empty();
            }
        }

        Comparator<Show> comparator = ((o1, o2) -> 0);
        switch (currentAction.getCriteria()) {
            case "ratings" -> {
                comparator = Comparator.comparing(Show::getRatingAverage)
                        .thenComparing(Show::toString);
                sortedShows = sortedShows.filter(show -> show.getRatingAverage() > 0);
            }
            case "favorite" -> {
                comparator = Comparator.comparing(Show::getNrOfAddsAtFavorites)
                        .thenComparing(Show::toString);
                sortedShows = sortedShows.filter(show -> show.getNrOfAddsAtFavorites() > 0);
            }
            case "longest" -> {
                comparator = Comparator.comparing(Show::getDuration)
                        .thenComparing(Show::toString);
                sortedShows = sortedShows.filter(show -> show.getDuration() > 0);
            }
            case "most_viewed" -> {
                comparator = Comparator.comparing(Show::getNrOfViews)
                        .thenComparing(Show::toString);
                sortedShows = sortedShows.filter(show -> show.getNrOfViews() > 0);
            }
            default -> { }
        }

        if (currentAction.getSortType().equals("asc")) {
            sortedShows = sortedShows.sorted(comparator);
        } else {
            sortedShows = sortedShows.sorted(comparator.reversed());
        }

        sortedShows = sortedShows.limit(currentAction.getNumber());

        MessageWriter.queryResult(currentAction, sortedShows);
    }

    /**
     * Method which takes the user-related queries
     * and sorts the database (the users field) accordingly.
     *
     * @param currentAction the current user query
     */
    public static void user(final ActionInputData currentAction) throws IOException {
        Stream<User> sortedUsers = getDatabase().getUsersData().stream()
                .filter(user -> user.getNumberOfRatings() > 0);

        if (currentAction.getSortType().equals("asc")) {
            sortedUsers = sortedUsers.sorted(Comparator
                    .comparing(User::getNumberOfRatings)
                    .thenComparing(User::toString));
        } else {
            sortedUsers = sortedUsers.sorted(Comparator
                    .comparing(User::getNumberOfRatings)
                    .thenComparing(User::toString).reversed());
        }

        sortedUsers = sortedUsers.limit(currentAction.getNumber());

        MessageWriter.queryResult(currentAction, sortedUsers);
    }

    /**
     * Generic method which takes any type of actor-related query
     * and filters and sorts the database accordingly.
     *
     * @param currentAction the current show query
     */
    public static void actor(final ActionInputData currentAction) throws IOException {
        Stream<Actor> sortedActors = getDatabase().getActorsData().stream();

        Comparator<Actor> comparator = ((o1, o2) -> 0);
        switch (currentAction.getCriteria()) {
            case "average" -> {
                comparator = Comparator.comparing(Actor::getRatingAverage)
                        .thenComparing(Actor::toString);
                sortedActors = sortedActors.filter(actor -> actor.getRatingAverage() > 0);
            }
            case "awards" -> {
                comparator = Comparator.comparing(Actor::getNrOfAwards)
                        .thenComparing(Actor::toString);
                sortedActors = sortedActors.filter(actor -> actor.getNrOfAwards() > 0);

                for (String currentAward : currentAction
                        .getFilters().get(Constants.AWARDS_FILTER)) {
                    sortedActors = sortedActors
                            .filter(actor -> actor.getAwards()
                                    .containsKey(Utils.stringToAwards(currentAward)));
                }
            }
            case "filter_description" -> {
                comparator = Comparator.comparing(Actor::toString);
                for (String string : currentAction.getFilters().get(Constants.WORDS_FILTER)) {
                    String currentWord = " " + string + " ";
                    sortedActors = sortedActors
                            .filter(actor -> actor.getCareerDescription().toLowerCase()
                                    .replaceAll("[^a-zA-Z0-9]", " ")
                                    .contains(currentWord.toLowerCase()));
                }
            }
            default -> { }
        }

        if (currentAction.getSortType().equals("asc")) {
            sortedActors = sortedActors.sorted(comparator);
        } else {
            sortedActors = sortedActors.sorted(comparator.reversed());
        }

        sortedActors = sortedActors.limit(currentAction.getNumber());

        MessageWriter.queryResult(currentAction, sortedActors);
    }
}
