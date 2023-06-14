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
 * @author 27713
 */
public interface LikeDao_Interface {
    
    public boolean addLike(Integer readerId, Integer storyId);
    public boolean deleteLike(Integer likeId);
    public List<Like> getAllLikes();
    public List<Like> getLikesByReaderId(Integer accountId);
    public List<Like> getLikesByStory(Integer storyId);
    public Integer getStoryLikesByDate(Integer storyId, Timestamp startDate, Timestamp endDate);
    
}
