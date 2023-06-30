/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ServiceLayers;

import Models.Application;
import java.util.List;

/**
 *
 * @author jarro
 */
public interface ApplicationService_Interface {
    public List<Application> getApplications();
    public String addApplication(Application application);
    public String deleteApplication(Integer accountId);
    public String deleteApplications(List<Integer> accountIds);
}
