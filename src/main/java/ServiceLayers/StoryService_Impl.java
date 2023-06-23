/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServiceLayers;

import DAOs.StoryDao_Impl;
import DAOs.StoryDao_Interface;
import Models.Genre;
import Models.Story;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jarro
 */
public class StoryService_Impl implements StoryService_Interface{
    private StoryDao_Interface storyDao;

    public StoryService_Impl() {
        storyDao = new StoryDao_Impl();
    }
    
    @Override
    public Story getStory(Integer storyId) {
        return storyDao.getStory(storyId);
    }

    @Override
    public List<Story> getAllStories() {
        return storyDao.getAllStories();
    }

    @Override
    public List<Story> getStoriesInGenre(Integer genreId) {
        return storyDao.getStoriesInGenre(genreId);
    }

    @Override
    public String updateStory(Story story) {
        if (storyDao.updateStory(story)) {
            return "Story was successfully updated on the system.";
        } else {
            return "System failed to update the story.";
        }
    }

    @Override
    public String deleteStory(Integer storyId) {
        if (storyDao.deleteStory(storyId)) {
            return "Story was successfully deleted from the system.";
        } else {
            return "System failed to delete the story from the system.";
        }
    }
    
    @Override
    public String addStory(Story story) { 
        if (storyDao.addStory(story)) {
            return "Story was successfully added to the system.";
        } else {
            return "System failed to add the story.";
        }
    }

    @Override
    public List<Story> getRecommendations(List<Integer> genreIds) {
        List<Story> recommendedStories = storyDao.getRecommendations(genreIds);
        if (recommendedStories.isEmpty()) {
            return recommendedStories;
        } else {
            return storyDao.getApprovedStories(10);
        }
    }

    @Override
    public List<Story> getTopPicks() {
        return storyDao.getTopPicks();
    }

    @Override
    public List<Story> getSubmittedStories() {
        return storyDao.getSubmittedStories();
    }

    @Override
    public List<Story> searchForStories(String searchValue) {
        return storyDao.searchForStories(searchValue);
    }

    @Override
    public List<Story> getWritersSubmittedStories(List<Integer> storyIds, Integer writerId) {
        return storyDao.getWritersSubmittedStories(storyIds, writerId);
    }

    @Override
    public List<Story> getWritersDraftedStories(List<Integer> storyIds, Integer writerId) {
        return storyDao.getWritersDraftedStories(storyIds, writerId);
    }
    
}
