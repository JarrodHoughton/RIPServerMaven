package ServiceLayers;

import DAOs.LikeDao_Impl;
import DAOs.LikeDao_Interface;
import Models.Like;
import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author 27713
 */
public class LikeService_Impl implements LikeService_Interface {
    private final LikeDao_Interface likeDao;

    public LikeService_Impl() {
        this.likeDao = new LikeDao_Impl();
    }

    @Override
    public String addLike(Like like) {
        
        if (likeDao.checkIfLikeExists(like)) {
            return "You have already liked this story.";
        }
        
        if(likeDao.addLike(like)){
            return "Like added";
        }else{
            return "System failed to add like";
        }
    }

    @Override
    public String deleteLike(Like like) {
        if(likeDao.deleteLike(like)){
            return "Like removed.";
        }else{
            return "System failed to remove like";
        }
    }

    @Override
    public List<Like> getAllLikes() {
        return likeDao.getAllLikes();
    }

    @Override
    public List<Like> getLikesByReaderId(Integer accountId) {
        return likeDao.getLikesByReaderId(accountId);
    }

    @Override
    public List<Like> getLikesByStory(Integer storyId) {
        return likeDao.getLikesByStory(storyId);
    }

    @Override
    public Integer getStoryLikesByDate(Integer storyId, Timestamp startDate, Timestamp endDate) {
        return likeDao.getStoryLikesByDate(storyId, startDate, endDate);
    }

    @Override
    public List<Integer> getMostLikedBooks(Integer numberOfBooks, Timestamp startDate, Timestamp endDate) {
        return likeDao.getMostLikedBooks(numberOfBooks, startDate, endDate);
    }

    @Override
    public Boolean checkIfLikeExists(Like like) {
        return likeDao.checkIfLikeExists(like);
    }
    
    
    
}
