package DAOs;

import java.util.List;
import Models.Rating;
import java.sql.Timestamp;

/**
 *
 * @author 27713
 */
public interface RatingDao_Interface {
    
    public List<Rating> getAllRatings();
    public List<Rating> getRatingsByReaderId(Integer accountId);
    public List<Rating> getRatingsByStory(Integer storyId);
    public Boolean addRating(Rating rating);
    public Integer getRatingValue(Integer storyId);
    public List<Integer> getTopHighestRatedStoriesInTimePeriod(Timestamp startDate, Timestamp endDate, Integer numberOfEntries);
    public Boolean checkRatingExists(int accountId, int storyId);
    public Boolean editRatingValue(Integer ratingId, Integer newValue);
}
