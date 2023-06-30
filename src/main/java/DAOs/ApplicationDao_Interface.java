package DAOs;

import Models.Application;
import java.util.List;

/**
 *
 * @author jarro
 */
public interface ApplicationDao_Interface {
    public List<Application> getApplications();
    public Boolean addApplication(Application application);
    public Boolean deleteApplication(Integer accountId);
    public Boolean deleteApplications(List<Integer> accountIds);
}
