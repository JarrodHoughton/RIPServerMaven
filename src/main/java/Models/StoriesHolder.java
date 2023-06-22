/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.util.List;

/**
 *
 * @author jarro
 */
public class StoriesHolder {
    private List<Story> stories;
    private List<Integer> storyIds;
    private Integer id; //used as a id attribute for methods

    public StoriesHolder() {}

    public StoriesHolder(List<Story> stories, List<Integer> storyIds, Integer id) {
        this.stories = stories;
        this.storyIds = storyIds;
        this.id = id;
    }

    public List<Story> getStories() {
        return stories;
    }

    public void setStories(List<Story> stories) {
        this.stories = stories;
    }

    public List<Integer> getStoryIds() {
        return storyIds;
    }

    public void setStoryIds(List<Integer> storyIds) {
        this.storyIds = storyIds;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    
}
