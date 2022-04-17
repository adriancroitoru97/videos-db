package actions;

import entertainment.Movie;
import entertainment.Season;
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
import java.util.Map;

public abstract class Commands extends ActionsHandler {

    public Commands(final ArrayList<ActionInputData> actions,
                    final Writer fileWriter, final JSONArray jsonArray,
                    final Database database) {
        super(actions, fileWriter, jsonArray, database);
    }

    /**
     * Checks if a show exists and adds it to the user's history
     * (or increments the number of views if already seen)
     * for that show. It also increases the total number of views
     * of that show.
     *
     * @param currentAction the current view action and its details
     */
    public static void view(final ActionInputData currentAction) throws IOException {
        User user = Utils.stringToUser((ArrayList<User>) getDatabase().getUsersData(),
                currentAction.getUsername());

        if (user != null) {
            boolean check = false;
            for (Map.Entry<Show, Integer> show : user.getHistory().entrySet()) {
                if (show.getKey().getTitle().equals(currentAction.getTitle())) {
                    user.getHistory().replace(show.getKey(), show.getValue() + 1);
                    show.getKey().setNrOfViews(show.getKey().getNrOfViews() + 1);

                    check = true;
                    break;
                }
            }

            if (!check) {
                Show show = Utils.stringToShow((ArrayList<Show>) getDatabase().getShowsData(),
                        currentAction.getTitle());
                user.getHistory().put(show, 1);
            }

            MessageWriter.wasViewed(currentAction, user);
        }
    }

    /**
     * Checks if the show is seen by the user and if it's not already
     * added at favorites. It adds the show to the user's favorites list,
     * and it increases the number of adds at favorites of that show.
     *
     * @param currentAction the current favorite action and its details
     */
    public static void favorite(final ActionInputData currentAction) throws IOException {
        User user = Utils.stringToUser((ArrayList<User>) getDatabase().getUsersData(),
                currentAction.getUsername());

        if (user != null) {
            if (showIsNotSeen(user, currentAction)) {
                MessageWriter.isNotSeen(currentAction);
                return;
            }

            if (showAlreadyFavorite(user, currentAction)) {
                MessageWriter.alreadyFavorite(currentAction);
                return;
            }

            for (Map.Entry<Show, Integer> show : user.getHistory().entrySet()) {
                if (show.getKey().getTitle().equals(currentAction.getTitle())) {
                    user.getFavoriteMovies().add(show.getKey());
                    show.getKey().setNrOfAddsAtFavorites(
                            show.getKey().getNrOfAddsAtFavorites() + 1);

                    MessageWriter.wasAddedAtFavorites(currentAction);
                    return;
                }
            }
        }
    }

    /**
     * Checks if the show is seen by the user and if it's not already
     * rated by that user. It then adds the grade to the show's list of grades.
     * Also, it increases the total number of ratings given by that user, and it adds
     * the show to the rated shows structure of the user.
     *
     * @param currentAction the current rating action and its details
     */
    public static void rating(final ActionInputData currentAction) throws IOException {
        Show show = Utils.stringToShow((ArrayList<Show>) getDatabase().getShowsData(),
                currentAction.getTitle());
        User user = Utils.stringToUser((ArrayList<User>) getDatabase().getUsersData(),
                currentAction.getUsername());

        if (user != null && show != null) {
            if (showIsNotSeen(user, currentAction)) {
                MessageWriter.isNotSeen(currentAction);
                return;
            }

            if (showAlreadyRated(user, currentAction)) {
                MessageWriter.alreadyRated(currentAction);
                return;
            }

            if (currentAction.getSeasonNumber() == 0) {
                /* it's a movie */
                ((Movie) show).getRatings().add(currentAction.getGrade());
            } else {
                /* it's a serial */
                Season season = ((Serial) show).getSeasons()
                        .get(currentAction.getSeasonNumber() - 1);
                season.getRatings().add(currentAction.getGrade());
            }

            user.getRatedShows().put(show, currentAction.getSeasonNumber());
            user.setNumberOfRatings(user.getNumberOfRatings() + 1);

            MessageWriter.wasRated(currentAction);
        }
    }

    private static boolean showIsNotSeen(final User user,
                                         final ActionInputData currentAction) {
        for (Map.Entry<Show, Integer> entry : user.getHistory().entrySet()) {
            if (entry.getKey().getTitle().equals(currentAction.getTitle())) {
                return false;
            }
        }

        return true;
    }

    private static boolean showAlreadyFavorite(final User user,
                                               final ActionInputData currentAction) {
        for (Show show : user.getFavoriteMovies()) {
            if (show.getTitle().equals(currentAction.getTitle())) {
                return true;
            }
        }

        return false;
    }

    private static boolean showAlreadyRated(final User user,
                                            final ActionInputData currentAction) {
        for (Map.Entry<Show, Integer> entry : user.getRatedShows().entrySet()) {
            if (entry.getKey().getTitle().equals(currentAction.getTitle())
                    && entry.getValue() == currentAction.getSeasonNumber()) {
                return true;
            }
        }

        return false;
    }
}
