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
        if (viewDao.isViewAlreadyAdded(view)) {
            return "View already exists";
        }

        if (viewDao.addView(view)) {
            return "View successfully added";
        }

        return "System failed to add view";
    }


    @Override
    public List<Integer> getMostViewedStoriesInATimePeriod(Integer numberOfEntries, Timestamp startDate, Timestamp endDate) {
        return viewDao.getMostViewedStoriesInATimePeriod(startDate, endDate, numberOfEntries);
              
    }

    @Override
    public Integer getTheViewsOnAStoryInATimePeriod(Integer storyId, Timestamp startDate, Timestamp endDate) {
        if (viewDao.getTheViewsOnAStoryInATimePeriod(storyId, startDate, endDate)!=null) {
            return viewDao.getTheViewsOnAStoryInATimePeriod(storyId, startDate, endDate).size();
        } else {
            return 0;
        }
    }

    @Override
    public Boolean isViewAlreadyAdded(View view) {
        return viewDao.isViewAlreadyAdded(view);
    }
    
}
