package ServiceLayers;
import Models.*;
import java.sql.Timestamp;
import java.util.List;
/**
 *
 * @author ruthe
 */
public interface ViewService_Interface {
    
    public String addView(View view);
    public List<Integer> getMostViewedStoriesInATimePeriod(Integer numberOfEntries, Timestamp startDate, Timestamp endDate);
    public Integer getTheViewsOnAStoryInATimePeriod(Integer storyId, Timestamp startDate, Timestamp endDate);
    public Boolean isViewAlreadyAdded(View view);
}
