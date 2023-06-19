/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
    public String addLike(Integer readerId, Integer storyId) {
        if(likeDao.addLike(readerId, storyId)){
            return "Like added";
        }else{
            return "System failed to add like";
        }
    }

    @Override
    public String deleteLike(Integer likeId) {
        if(likeDao.deleteLike(likeId)){
            return "Like added";
        }else{
            return "System failed to delete like";
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
}
