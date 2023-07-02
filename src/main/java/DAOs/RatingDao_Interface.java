package DAOs;

import java.util.List;
import Models.Rating;
import java.sql.Timestamp;

/**
 *
 * @author Kylynn van der Merwe
 */
public interface RatingDao_Interface {
    
    public List<Rating> getAllRatings();
    public List<Rating> getRatingsByReaderId(Integer accountId);
    public List<Rating> getRatingsByStory(Integer storyId);
    public Boolean addRating(Rating rating);
    public Integer getRatingValue(Integer storyId);
    public Rating getRating(Integer accountId, Integer storyId);
    public List<Integer> getTopHighestRatedStoriesInTimePeriod(Timestamp startDate, Timestamp endDate, Integer numberOfEntries);
    public Boolean checkRatingExists(Rating rating);
    public Boolean updateRatingValue(Rating rating);
    public Double getAverageRatingOfAStoryInATimePeriod(Integer storyId, Timestamp startDate, Timestamp endDate);
}
