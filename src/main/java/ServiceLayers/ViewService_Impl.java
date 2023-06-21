/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServiceLayers;

import DAOs.ViewDao_Impl;
import DAOs.ViewDao_Interface;
import Models.View;
import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author ruthe
 */
public class ViewService_Impl implements ViewService_Interface {
    
    private final ViewDao_Interface viewDao;

    public ViewService_Impl() {
        this.viewDao = new ViewDao_Impl();
    }
    
    

    @Override
    public String addView(View view) {
        if (viewDao.addView(view)) {
            return "View succesfully added";
        } else {
            return "System failed to add view";
        }
        
    }

    @Override
    public List<Integer> getMostViewedStoriesInATimePeriod(Timestamp startDate, Timestamp endDate, Integer numberOfEntries) {
        return viewDao.getMostViewedStoriesInATimePeriod(startDate, endDate, numberOfEntries);
              
    }

    @Override
    public List<View> getTheViewsOnAStoryInATimePeriod(Integer storyId, Timestamp startDate, Timestamp endDate) {
        return viewDao.getTheViewsOnAStoryInATimePeriod(storyId, startDate, endDate);
    }
    
}
