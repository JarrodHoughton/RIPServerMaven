package ServiceLayers;

import DAOs.RatingDao_Impl;
import DAOs.RatingDao_Interface;
import DAOs.ViewDao_Impl;
import Models.Rating;
import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author Kylynn van der Merwe
 */
public class RatingService_Impl implements RatingService_Interface {
    private final RatingDao_Interface ratingDao;
    
    public RatingService_Impl(){
        this.ratingDao = new RatingDao_Impl(); 
    }

    @Override
    public List<Rating> getAllRatings() {
        return ratingDao.getAllRatings();
    }

    @Override
    public List<Rating> getRatingsByReaderId(Integer accountId) {
        return ratingDao.getRatingsByReaderId(accountId);
    }

    @Override
    public List<Rating> getRatingsByStory(Integer storyId) {
        return ratingDao.getRatingsByStory(storyId);
    }

    @Override
    public String addRating(Rating rating) {
        
        if (ratingDao.checkRatingExists(rating)) {
            return "Rating already exists";
        }
        
        if(ratingDao.addRating(rating)){
            return "Rating has been added";
        }else{
            return "System failed to add rating";
        }
    }

    @Override
    public Integer getRatingValue(Integer storyId) {
        return ratingDao.getRatingValue(storyId);
    }

    @Override
    public List<Integer> getTopHighestRatedStoriesInTimePeriod(Timestamp startDate, Timestamp endDate, Integer numberOfEntries) {
        return ratingDao.getTopHighestRatedStoriesInTimePeriod(startDate, endDate, numberOfEntries);
    }

    @Override
    public Boolean checkRatingExists(Rating rating) {
        return ratingDao.checkRatingExists(rating);
    }

    @Override
    public String updateRatingValue(Rating rating) {
        if (ratingDao.updateRatingValue(rating)) {
            return "rating successfully updated";
        } else {
          return "rating not updated";  
        }
    }

    @Override
    public Rating getRating(Integer accountId, Integer storyId) {
        return ratingDao.getRating(accountId, storyId);
    }

    @Override
    public Double getAverageRatingOfAStoryInATimePeriod(Integer storyId, Timestamp startDate, Timestamp endDate) {
        return ratingDao.getAverageRatingOfAStoryInATimePeriod(storyId, startDate, endDate);
    }
    
    
    
}
