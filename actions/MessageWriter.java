package actions;

import entertainment.Show;
import fileio.ActionInputData;
import fileio.Writer;
import main.Database;
import user.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")

public abstract class MessageWriter extends ActionsHandler {

    public MessageWriter(final ArrayList<ActionInputData> actions,
                         final Writer fileWriter,
                         final JSONArray jsonArray, final Database database) {
        super(actions, fileWriter, jsonArray, database);
    }

    /**
     * Generic method which writes the output for every type of query action.
     *
     * @param action the query action
     * @param result the result of the action
     * @param <T> used for the genericity of the method
     */
    public static <T> void queryResult(final ActionInputData action,
                                       final Stream<T> result) throws IOException {
        JSONObject jsonObject = getFileWriter().writeFile(action.getActionId(), "",
                "Query result: " + result.toList());
        getJsonArray().add(jsonObject);
    }

    /**
     * Generic method which writes the output for every type of recommendation action.
     *
     * @param action the recommendation action
     * @param recommendation "XRecommendation" formatted string,
     *                       where X is the name of the recommendation
     * @param result the result of the recommendation action
     * @param <T> used for the genericity of the method
     */
    public static <T> void recommendationResult(final ActionInputData action,
                                                final String recommendation,
                                                final T result) throws IOException {
        JSONObject jsonObject = getFileWriter().writeFile(action.getActionId(), "",
                recommendation + " result: " + result);
        getJsonArray().add(jsonObject);
    }

    /**
     * Error message writer - the view command output
     *
     * @param action the current view action
     * @param user the user who viewed the show
     */
    public static void wasViewed(final ActionInputData action,
                                 final User user) throws IOException {
        JSONObject jsonObject = getFileWriter().writeFile(action.getActionId(), "",
                "success -> " + action.getTitle() + " was viewed with total views of " + user
                        .getHistory()
                        .get(Utils.stringToShow((ArrayList<Show>) getDatabase()
                                .getShowsData(), action.getTitle()))
        );
        getJsonArray().add(jsonObject);
    }

    /**
     * Error message writer - the show is not seen
     *
     * @param action the current action
     */
    public static void isNotSeen(final ActionInputData action) throws IOException {
        JSONObject jsonObject = getFileWriter().writeFile(action.getActionId(), "",
                "error -> " + action.getTitle() + " is not seen");
        getJsonArray().add(jsonObject);
    }

    /**
     * Error message writer - the show is already in the favourite list of the user
     *
     * @param action the current action
     */
    public static void alreadyFavorite(final ActionInputData action) throws IOException {
        JSONObject jsonObject = getFileWriter().writeFile(action.getActionId(), "",
                "error -> " + action.getTitle() + " is already in favourite list");
        getJsonArray().add(jsonObject);
    }

    /**
     * Error message writer - the show has been already rated by the user
     *
     * @param action the current action
     */
    public static void alreadyRated(final ActionInputData action) throws IOException {
        JSONObject jsonObject = getFileWriter().writeFile(action.getActionId(), "",
                "error -> " + action.getTitle() + " has been already rated");
        getJsonArray().add(jsonObject);
    }

    /**
     * Success message writer - the show was added in the favorites list of the user
     *
     * @param action the current action - favorite command
     */
    public static void wasAddedAtFavorites(final ActionInputData action) throws IOException {
        JSONObject jsonObject = getFileWriter().writeFile(action.getActionId(), "",
                "success -> " + action.getTitle() + " was added as favourite");
        getJsonArray().add(jsonObject);
    }

    /**
     * Success message writer - the show was rated by the user
     *
     * @param action the current action - rating command
     */
    public static void wasRated(final ActionInputData action) throws IOException {
        JSONObject jsonObject = getFileWriter().writeFile(action.getActionId(), "",
                "success -> " + action.getTitle() + " was rated with "
                        + action.getGrade() + " by " + action.getUsername());
        getJsonArray().add(jsonObject);
    }

    /**
     * Error message - the current recommendation cannot be applied
     *
     * @param action the current recommendation action
     * @param recommendation "XRecommendation" formatted string,
     *                       where X is the name of the recommendation
     */
    public static void cannotBeApplied(final ActionInputData action,
                                       final String recommendation) throws IOException {
        JSONObject jsonObject = getFileWriter().writeFile(action.getActionId(), "",
                recommendation + " cannot be applied!");
        getJsonArray().add(jsonObject);
    }
}
