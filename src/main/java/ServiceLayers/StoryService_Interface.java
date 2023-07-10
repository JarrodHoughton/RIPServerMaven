/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ServiceLayers;

import Models.Story;
import java.util.List;

/**
 *
 * @author Jarrod
 */
public interface StoryService_Interface {
    public Story getStory(Integer storyId);
    public List<Story> getAllStories();
    public List<Story> getSubmittedStories(Integer numberOfStories, Integer offset);
    public List<Story> getStoriesInGenre(Integer genreId, Integer numberOfStories, Integer currentId, Boolean next);
    public List<Story> getTopPicks();
    public List<Story> searchForStories(String searchValue, Integer numberOfStories, Integer currentId, Boolean next);
    public String updateStory(Story story);
    public String updateStories(List<Story> stories);
    public String deleteStory(Story story);
    public String addStory(Story story);
    public List<Story> getRecommendations(List<Integer> genreIds);
    public List<Story> getWritersSubmittedStories(List<Integer> storyIds, Integer writerId);
    public List<Story> getWritersDraftedStories(List<Integer> storyIds, Integer writerId);
}
