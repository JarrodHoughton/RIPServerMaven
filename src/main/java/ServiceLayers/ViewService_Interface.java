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
    public List<Integer> getMostViewedStoriesInATimePeriod(Timestamp startDate, Timestamp endDate, Integer numberOfEntries);
    public List<View> getTheViewsOnAStoryInATimePeriod(Integer storyId, Timestamp startDate, Timestamp endDate);

}
