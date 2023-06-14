/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package DAOs;

import Models.Application;
import java.util.List;

/**
 *
 * @author jarro
 */
public interface ApplicationDao_Interface {
    public List<Application> getApplications();
    public boolean addApplication(Application application);
    public boolean deleteApplication(Integer accountId);
}
