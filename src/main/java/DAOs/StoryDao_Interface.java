/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import Models.*;
import java.util.List;

/**
 *
 * @author jarro
 */
public interface StoryDao_Interface {
    public Story getStory(Integer storyId);
    public List<Story> getAllStories();
    public List<Story> getStoriesInGenre(Integer genreId);
    public List<Story> getRecommendations(List<Integer>  genreIds);
    public Boolean updateStory(Story story);
    public Boolean deleteStory(Integer storyId);
    public Boolean addStory(Story story);
}
