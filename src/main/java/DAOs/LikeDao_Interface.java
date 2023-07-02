/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import java.sql.Timestamp;
import java.util.List;
import Models.Like;

/**
 *
 * @author Kylynn van der Merwe
 */
public interface LikeDao_Interface {
    
    public Boolean addLike(Like like);
    public Boolean deleteLike(Like like);
    public List<Like> getAllLikes();
    public List<Like> getLikesByReaderId(Integer accountId);
    public List<Like> getLikesByStory(Integer storyId);
    public Integer getStoryLikesByDate(Integer storyId, Timestamp startDate, Timestamp endDate);
    public List<Integer> getMostLikedBooks(Integer numberOfBooks, Timestamp startDate, Timestamp endDate);
    public List<Integer> getMostLikedStoriesByGenreAndTime(Integer genreId, Integer numberOfStories, String startDate, String endDate);
    public Boolean searchForLike(Like like);
}
