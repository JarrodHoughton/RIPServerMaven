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
    private String name;
    private String surname;

    public Comment() {
    }
    
    public Comment(String message) {
        this.message = message;
    }
    
    public Comment(Integer id, LocalDateTime date, Integer readerId, Integer storyId, String message, String name, String surname) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
    
}
