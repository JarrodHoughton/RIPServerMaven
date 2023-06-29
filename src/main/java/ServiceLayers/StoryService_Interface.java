/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ServiceLayers;

import Models.Genre;
import Models.Story;
import java.util.List;

/**
 *
 * @author jarro
 */
public interface StoryService_Interface {
    public Story getStory(Integer storyId);
    public List<Story> getAllStories();
    public List<Story> getSubmittedStories();
    public List<Story> getStoriesInGenre(Integer genreId);
    public List<Story> getTopPicks();
    public List<Story> searchForStories(String searchValue);
    public String updateStory(Story story);
    public String updateStories(List<Story> stories);
    public String deleteStory(Story story);
    public String addStory(Story story);
    public List<Story> getRecommendations(List<Integer> genreIds);
    public List<Story> getWritersSubmittedStories(List<Integer> storyIds, Integer writerId);
    public List<Story> getWritersDraftedStories(List<Integer> storyIds, Integer writerId);
}
