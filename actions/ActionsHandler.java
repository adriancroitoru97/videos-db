package actions;

import fileio.ActionInputData;
import fileio.Writer;
import main.Database;
import org.json.simple.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Takes every action from input and calls the according method.
 */
public class ActionsHandler {

    private static List<ActionInputData> actions;
    private static Writer fileWriter;
    private static JSONArray jsonArray;
    private static Database database;

    public ActionsHandler(final ArrayList<ActionInputData> actions,
                          final Writer fileWriter, final JSONArray jsonArray,
                          final Database database) {
        ActionsHandler.actions = actions;
        ActionsHandler.fileWriter = fileWriter;
        ActionsHandler.jsonArray = jsonArray;
        ActionsHandler.database = database;
    }

    /**
     * Matches each action with its type and calls the proper method.
     */
    public final void executeActions() throws IOException {
        for (ActionInputData currentAction : actions) {
            switch (currentAction.getActionType()) {
                case "command":
                    switch (currentAction.getType()) {
                        case "view" -> Commands.view(currentAction);
                        case "favorite" -> Commands.favorite(currentAction);
                        case "rating" -> Commands.rating(currentAction);
                        default -> { }
                    }
                    break;
                case "query":
                    switch (currentAction.getObjectType()) {
                        case "users" -> Queries.user(currentAction);
                        case "shows", "movies" -> Queries.show(currentAction);
                        case "actors" -> Queries.actor(currentAction);
                        default -> { }
                    }
                    break;
                case "recommendation":
                    switch (currentAction.getType()) {
                        case "standard" -> Recommendations.standard(currentAction);
                        case "best_unseen" -> Recommendations.bestUnseen(currentAction);
                        case "popular" -> Recommendations.popular(currentAction);
                        case "favorite" -> Recommendations.favorite(currentAction);
                        case "search" -> Recommendations.search(currentAction);
                        default -> { }
                    }
                    break;
                default:
            }
        }
    }

    public static List<ActionInputData> getActions() {
        return actions;
    }

    public static Writer getFileWriter() {
        return fileWriter;
    }

    public static JSONArray getJsonArray() {
        return jsonArray;
    }

    public static Database getDatabase() {
        return database;
    }
}
