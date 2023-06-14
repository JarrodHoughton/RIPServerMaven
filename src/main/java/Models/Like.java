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
public class Like extends StoryStatistic {

    public Like() {
    }

    public Like(Integer id, LocalDateTime date, Integer readerId, Integer storyId) {
        super(id, date, readerId, storyId);
    }

    @Override
    public String toString() {
        return "Like{" +super.toString()+ '}';
    }
    
}
