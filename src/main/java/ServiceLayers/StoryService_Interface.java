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
    public List<Story> getStoriesInGenre(Integer genreId);
    public String updateStory(Story story);
    public String deleteStory(Integer storyId);
    public String addStory(Story story);
    public List<Story> getRecommendations(List<Integer> genreIds);
}
