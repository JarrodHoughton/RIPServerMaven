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
 * @author Jarrod
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
    public List<Story> getStoriesInGenre(Integer genreId, Integer numberOfStories, Integer currentId, Boolean next) {
        return storyDao.getStoriesInGenre(genreId, numberOfStories, currentId, next);
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
    public String deleteStory(Story story) {
        if (storyDao.deleteStory(story)) {
            return "Story was successfully deleted from the system.";
        } else {
            return "System failed to delete the story from the system.";
        }
    }
    
    @Override
    public String addStory(Story story) { 
        if (storyDao.searchForTitle(story.getTitle())) {
            return "Story with this title already exists.";
        }
        
        if (storyDao.addStory(story)) {
            return "Your story has been saved!";
        } else {
            return "System failed to save your story. Please try again...";
        }
    }

    @Override
    public List<Story> getRecommendations(List<Integer> genreIds) {
        if (genreIds == null) {
            return null;
        } else if (genreIds.isEmpty()) {
            return null;
        }
        List<Story> recommendedStories = storyDao.getRecommendations(genreIds);
        if (recommendedStories != null && !recommendedStories.isEmpty()) {
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
    public List<Story> getSubmittedStories(Integer numberOfStories, Integer offset) {
        return storyDao.getSubmittedStories(numberOfStories, offset);
    }

    @Override
    public List<Story> searchForStories(String searchValue, Integer numberOfStories, Integer currentId, Boolean next) {
        return storyDao.searchForStories(searchValue, numberOfStories, currentId, next);
    }

    @Override
    public List<Story> getWritersSubmittedStories(List<Integer> storyIds, Integer writerId) {
        return storyDao.getWritersSubmittedStories(storyIds, writerId);
    }

    @Override
    public List<Story> getWritersDraftedStories(List<Integer> storyIds, Integer writerId) {
        return storyDao.getWritersDraftedStories(storyIds, writerId);
    }

    @Override
    public String updateStories(List<Story> stories) {
        String message = "Stories were updated successfully.";
        for (Story story : stories) {
            if (!storyDao.updateStory(story)) {
                message = "System failed to updat stories.";
            }
        }
        return message;
    }
    
}
