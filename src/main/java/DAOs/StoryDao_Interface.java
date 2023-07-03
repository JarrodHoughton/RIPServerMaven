/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import Models.*;
import java.util.List;

/**
 *
 * @author Jarrod
 */
public interface StoryDao_Interface {
    public Story getStory(Integer storyId);
    public List<Story> getAllStories();
    public List<Story> getSubmittedStories(Integer numberOfStories, Integer offset);
    public List<Story> getApprovedStories(Integer numberOfStories);
    public List<Story> getStoriesInGenre(Integer genreId, Integer numberOfStories, Integer currentId, Boolean next);
    public List<Story> getRecommendations(List<Integer>  genreIds);
    public List<Story> getWritersSubmittedStories(List<Integer> storyIds, Integer writerId);
    public List<Story> getWritersDraftedStories(List<Integer> storyIds, Integer writerId);
    public List<Story> searchForStories(String searchValue, Integer numberOfStories, Integer currentId, Boolean next);
    public List<Story> getTopPicks();
    public Boolean updateStory(Story story);
    public Boolean deleteStory(Story story);
    public Boolean addStory(Story story);
    public Boolean searchForTitle(String title);
}
