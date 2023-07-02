/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.time.LocalDateTime;

/**
 *
 * @author Kylynn van der Merwe
 */
public abstract class StoryStatistic {
    private Integer id;
    private LocalDateTime date;
    private Integer readerId;
    private Integer storyId;

    public StoryStatistic() {
    }

    public StoryStatistic(Integer id, LocalDateTime date, Integer readerId, Integer storyId) {
        this.id = id;
        this.date = date;
        this.readerId = readerId;
        this.storyId = storyId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(String date) {
        if (date != null) {
            this.date = LocalDateTime.parse(date);
        }
    }
    
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    
    public Integer getReaderId() {
        return readerId;
    }

    public void setReaderId(Integer readerId) {
        this.readerId = readerId;
    }

    public Integer getStoryId() {
        return storyId;
    }

    public void setStoryId(Integer storyId) {
        this.storyId = storyId;
    }

    @Override
    public String toString() {
        return  "id=" + id + ", date=" + date + ", readerId=" + readerId + ", storyId=" + storyId;
    }
    
    
}
