/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.time.LocalDateTime;

/**
 *
 * @author 27713
 */
public class Comment extends StoryStatistic {
    private String message;

    public Comment(String message) {
        this.message = message;
    }

    public Comment(Integer id, LocalDateTime date, Integer readerId, Integer storyId, String message) {
        super(id, date, readerId, storyId);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Comment{"+super.toString() + ", message=" + message + '}';
    }
    
}
