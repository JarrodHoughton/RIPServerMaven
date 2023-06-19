/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
    public Boolean addRating(Integer accountId, Integer storyId, Integer ratingValue);
    public Integer getRatingValue(Integer storyId);
    public List<Integer> getTopHighestRatedStoriesInTimePeriod(Timestamp startDate, Timestamp endDate, Integer numberOfEntries);
    
}
