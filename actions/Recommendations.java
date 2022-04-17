package actions;

import entertainment.Genre;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Collections;
import java.util.LinkedHashMap;

public abstract class Recommendations extends ActionsHandler {

    public Recommendations(final ArrayList<ActionInputData> actions, final Writer fileWriter,
                           final JSONArray jsonArray, final Database database) {
        super(actions, fileWriter, jsonArray, database);
    }

    /**
     * Seeks each show and print the first one not to be seen by user.
     *
     * @param currentAction the current standard recommendation action
     */
    public static void standard(final ActionInputData currentAction) throws IOException {
        User user = Utils.stringToUser(
                (ArrayList<User>) getDatabase().getUsersData(), currentAction.getUsername());

        if (user != null) {
            for (Show show : getDatabase().getShowsData()) {
                if (!user.getHistory().containsKey(show)) {
                    MessageWriter.recommendationResult(
                            currentAction, "StandardRecommendation", show);
                    return;
                }
            }
        }

        MessageWriter.cannotBeApplied(currentAction, "StandardRecommendation");
    }

    /**
     * Creates a list of unseen shows and sort it descending by the rating. The first
     * show on that list is the best unseen show, which is printed.
     *
     * @param currentAction the current bestUnseen recommendation action
     */
    public static void bestUnseen(final ActionInputData currentAction) throws IOException {
        User user = Utils.stringToUser(
                (ArrayList<User>) getDatabase().getUsersData(), currentAction.getUsername());

        List<Show> unseenShows = new ArrayList<>();

        if (user != null) {
            for (Show show : getDatabase().getShowsData()) {
                if (!user.getHistory().containsKey(show)) {
                    unseenShows.add(show);
                }
            }
        }

        List<Show> unseenShowsSorted = unseenShows.stream()
                .sorted(Comparator.comparing(Show::getRatingAverage).reversed()).toList();

        String bestUnseenShow;
        if (unseenShowsSorted.size() != 0) {
            bestUnseenShow = unseenShowsSorted.get(0).getTitle();
            MessageWriter.recommendationResult(
                    currentAction, "BestRatedUnseenRecommendation", bestUnseenShow);
            return;
        }

        MessageWriter.cannotBeApplied(currentAction, "BestRatedUnseenRecommendation");
    }

    /**
     * Checks if the user has a premium account. Creates a map of genres and the views
     * for each genre and sorts it descending. The LinkedHashMap created before is traversed
     * and the first unseen show with the most possible popular genre is printed.
     *
     * @param currentAction the current popular recommendation action
     */
    public static void popular(final ActionInputData currentAction) throws IOException {
        User user = Utils.stringToUser(
                (ArrayList<User>) getDatabase().getUsersData(), currentAction.getUsername());

        if (user != null && !user.getSubscriptionType().equals("PREMIUM")) {
            MessageWriter.cannotBeApplied(currentAction, "PopularRecommendation");
            return;
        }

        Map<Genre, Integer> genresMap = new HashMap<>();
        for (Show show : getDatabase().getShowsData()) {
            for (Genre showGenre : show.getGenres()) {
                if (genresMap.containsKey(showGenre)) {
                    genresMap.put(showGenre, genresMap.get(showGenre) + show.getNrOfViews());
                } else {
                    genresMap.put(showGenre, show.getNrOfViews());
                }
            }
        }

        Map<Genre, Integer> sortedGenresMap = genresMap.entrySet()
                .stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        for (Map.Entry<Genre, Integer> entry : sortedGenresMap.entrySet()) {
            for (Show show : getDatabase().getShowsData()) {
                if (user != null
                        && showIsNotSeen(user, show)
                        && showContainsGenre(show, entry.getKey())) {
                    MessageWriter.recommendationResult(
                            currentAction, "PopularRecommendation", show);
                    return;
                }
            }
        }

        MessageWriter.cannotBeApplied(currentAction, "PopularRecommendation");
    }

    /**
     * Checks if the user has a premium account. If yes, it sorts all the show descending
     * by the number of adds at favorites. It prints the first unseen show of this list.
     *
     * @param currentAction the current favorite recommendation action
     */
    public static void favorite(final ActionInputData currentAction) throws IOException {
        User user = Utils.stringToUser(
                (ArrayList<User>) getDatabase().getUsersData(), currentAction.getUsername());

        if (user != null && !user.getSubscriptionType().equals("PREMIUM")) {
            MessageWriter.cannotBeApplied(currentAction, "FavoriteRecommendation");
            return;
        }

        List<Show> sortedShows = getDatabase().getShowsData().stream()
                .filter(show -> show.getNrOfAddsAtFavorites() > 0)
                .sorted(Comparator.comparing(Show::getNrOfAddsAtFavorites).reversed())
                .toList();

        for (Show show : sortedShows) {
            if (user != null && showIsNotSeen(user, show)) {
                MessageWriter.recommendationResult(
                        currentAction, "FavoriteRecommendation", show);
                return;
            }
        }

        MessageWriter.cannotBeApplied(currentAction, "FavoriteRecommendation");
    }

    /**
     * Checks if the user has a premium account. Creates a list of unseen shows and
     * filters it by the given genre. It prints the resulted list.
     *
     * @param currentAction the current search recommendation action
     */
    public static void search(final ActionInputData currentAction) throws IOException {
        User user = Utils.stringToUser(
                (ArrayList<User>) getDatabase().getUsersData(), currentAction.getUsername());

        if (user != null && !user.getSubscriptionType().equals("PREMIUM")) {
            MessageWriter.cannotBeApplied(currentAction, "SearchRecommendation");
            return;
        }

        List<Show> unseenShows = new ArrayList<>();
        for (Show show : getDatabase().getShowsData()) {
            if (user != null && showIsNotSeen(user, show)) {
                unseenShows.add(show);
            }
        }

        unseenShows = unseenShows.stream()
                .filter(show -> show.getGenres().contains(
                        Utils.stringToGenre(currentAction.getGenre())))
                .sorted(Comparator.comparing(Show::getRatingAverage).thenComparing(Show::toString))
                .toList();

        if (unseenShows.size() == 0) {
            MessageWriter.cannotBeApplied(currentAction, "SearchRecommendation");
        } else {
            MessageWriter.recommendationResult(
                    currentAction, "SearchRecommendation", unseenShows);
        }
    }

    private static boolean showIsNotSeen(final User user, final Show show) {
        for (Map.Entry<Show, Integer> entry : user.getHistory().entrySet()) {
            if (entry.getKey().getTitle().equals(show.getTitle())) {
                return false;
            }
        }

        return true;
    }

    private static boolean showContainsGenre(final Show show, final Genre genre) {
        for (Genre currentGenre : show.getGenres()) {
            if (currentGenre == genre) {
                return true;
            }
        }

        return false;
    }
}
