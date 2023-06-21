/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package DAOs;

import Models.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author ruthe
 */
public interface ViewDao_Interface {
    
    public Boolean addView(View view);
    public List<Integer> getMostViewedStoriesInATimePeriod(Timestamp startDate, Timestamp endDate, Integer numberOfEntries);
    public List<View> getTheViewsOnAStoryInATimePeriod(Integer storyId, Timestamp startDate, Timestamp endDate);
    public Boolean isViewAlreadyAdded(View view);
}
