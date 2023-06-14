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
public class Rating extends StoryStatistic {
    private Integer value;
    
    public Rating() {
    }

    public Rating(Integer id, LocalDateTime date, Integer readerId, Integer storyId, Integer value) {
        super(id, date, readerId, storyId);
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Rating{"+super.toString() + ", value=" + value + '}';
    }

    
    
    
}
