package entertainment;

import java.util.ArrayList;

/**
 * Serial class containing all generic show information and
 * specialized information for a serial.
 */
public final class Serial extends Show {

    private final int numberOfSeasons;
    private final ArrayList<Season> seasons;

    public Serial(final String title, final ArrayList<Genre> genres,
                  final int numberOfSeasons, final ArrayList<Season> seasons,
                  final int year) {
        super(title, year, genres);
        this.numberOfSeasons = numberOfSeasons;
        this.seasons = seasons;
    }

    public ArrayList<Season> getSeasons() {
        return seasons;
    }

    /**
     * Seeks the list of ratings of each season of the serial and calculates the average.
     *
     * @return the rating average of the serial
     */
    public double getRatingAverage() {
        double ratingsSum = 0;
        for (Season currentSeason : seasons) {
            double currentSeasonRatingsSum = 0;
            for (double currentSeasonCurrentRating : currentSeason.getRatings()) {
                currentSeasonRatingsSum += currentSeasonCurrentRating;
            }

            if (currentSeason.getRatings().size() != 0) {
                ratingsSum += (currentSeasonRatingsSum / currentSeason.getRatings().size());
            }
        }

        if (numberOfSeasons != 0) {
            return ratingsSum / numberOfSeasons;
        }

        return 0;
    }

    /**
     * Calculates the total duration of a serial, seeking each season
     * and adding the durations.
     *
     * @return the duration of the serial
     */
    public int getDuration() {
        int totalDuration = 0;
        for (Season currentSeason : seasons) {
            totalDuration += currentSeason.getDuration();
        }

        return totalDuration;
    }
}
