/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServiceLayers;

import DAOs.ApplicationDao_Impl;
import DAOs.ApplicationDao_Interface;
import Models.Application;
import java.util.List;

/**
 *
 * @author jarro
 */
public class ApplicationService_Impl implements ApplicationService_Interface {

    private ApplicationDao_Interface applicationDao;

    public ApplicationService_Impl() {
        this.applicationDao = new ApplicationDao_Impl();
    }

    @Override
    public List<Application> getApplications() {
        return applicationDao.getApplications();
    }

    @Override
    public String addApplication(Application application) {
        if (applicationDao.addApplication(application)) {
            return "Application added to the system successfully.";
        } else {
            return "System failed to add application.";
        }
    }

    @Override
    public String deleteApplication(Integer accountId) {
        if (applicationDao.deleteApplication(accountId)) {
            return "Application deleted from the system successfully.";
        } else {
            return "System failed to delete application.";
        }
    }

    @Override
    public String deleteApplications(List<Integer> accountIds) {
        if (applicationDao.deleteApplications(accountIds)) {
            return "Applications deleted from the system successfully.";
        } else {
            return "System failed to delete applications.";
        }
    }

}
