/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServiceLayers;

import Models.Rating;
import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author Kylynn van der Merwe
 */
public interface RatingService_Interface {
    
    public List<Rating> getAllRatings();
    public List<Rating> getRatingsByReaderId(Integer accountId);
    public List<Rating> getRatingsByStory(Integer storyId);
    public String addRating(Rating rating);
    public Integer getRatingValue(Integer storyId);
    public List<Integer> getTopHighestRatedStoriesInTimePeriod(Timestamp startDate, Timestamp endDate, Integer numberOfEntries);
    public Boolean checkRatingExists(Rating rating);
    public String updateRatingValue(Rating rating);
    public Rating getRating(Integer accountId, Integer storyId);
    public Double getAverageRatingOfAStoryInATimePeriod(Integer storyId, Timestamp startDate, Timestamp endDate);
}
