package actor;

import entertainment.Show;

import java.util.ArrayList;
import java.util.Map;

/**
 * Actor class containing all relevant information for an actor.
 * An actor is linked with its shows directly (Filmography is
 * a list of Show objects).
 */
public final class Actor {
    private final String name;
    private final String careerDescription;
    private final ArrayList<Show> filmography;
    private final Map<ActorsAwards, Integer> awards;

    public Actor(final String name, final String careerDescription,
                 final Map<ActorsAwards, Integer> awards) {
        this.name = name;
        this.careerDescription = careerDescription;
        this.filmography = new ArrayList<>();
        this.awards = awards;
    }

    /**
     * Calculates the average rating of all shows in which the actor plays.
     *
     * @return the rating average of an actor
     */
    public double getRatingAverage() {
        double sum = 0;
        double nrOfShows = 0;
        for (Show show : filmography) {
            if (show.getRatingAverage() != 0) {
                sum += show.getRatingAverage();
                nrOfShows++;
            }
        }

        if (nrOfShows != 0) {
            return sum / nrOfShows;
        }

        return 0;
    }

    /**
     * Seek the awards map of the actor and calculates
     * the total number of awards.
     *
     * @return the total number of awards of an actor
     */
    public int getNrOfAwards() {
        int nrOfAwards = 0;
        for (Map.Entry<ActorsAwards, Integer> entry : awards.entrySet()) {
            nrOfAwards += entry.getValue();
        }

        return nrOfAwards;
    }

    public ArrayList<Show> getFilmography() {
        return filmography;
    }

    public Map<ActorsAwards, Integer> getAwards() {
        return awards;
    }

    public String getCareerDescription() {
        return careerDescription;
    }

    @Override
    public String toString() {
        return name;
    }
}
