/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServiceLayers;

import DAOs.RatingDao_Impl;
import DAOs.RatingDao_Interface;
import Models.Rating;
import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author 27713
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
    
}
