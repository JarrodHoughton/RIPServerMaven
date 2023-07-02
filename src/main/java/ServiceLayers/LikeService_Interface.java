/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServiceLayers;

import Models.Like;
import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author Kylynn van der Merwe
 */
public interface LikeService_Interface {
    
    public String addLike(Like like);
    public String deleteLike(Like like);
    public List<Like> getAllLikes();
    public List<Like> getLikesByReaderId(Integer accountId);
    public List<Like> getLikesByStory(Integer storyId);
    public Integer getStoryLikesByDate(Integer storyId, Timestamp startDate, Timestamp endDate);    
    public List<Integer> getMostLikedBooks(Integer numberOfBooks, Timestamp startDate, Timestamp endDate);
    public Boolean searchForLike(Like like);
}
