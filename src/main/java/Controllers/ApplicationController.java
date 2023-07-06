/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import Models.Application;
import ServiceLayers.ApplicationService_Impl;
import ServiceLayers.ApplicationService_Interface;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

/**
 *
 * @author Jarrod
 */
@Path("/applications")
public class ApplicationController {
    private ApplicationService_Interface applicationService;

    public ApplicationController() {
        this.applicationService = new ApplicationService_Impl();
    }
    
    @Path("/getApplications")
    @GET
    public Response getApplications() {
        return Response.ok().entity(applicationService.getApplications()).build();
    }
    
    @Path("/addApplication")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addApplication(Application application) {
        return Response.ok().entity(applicationService.addApplication(application)).build();
    }
    
    @Path("/deleteApplication/{readerId}")
    @GET
    public Response addApplication(@PathParam("readerId") Integer readerId) {
        return Response.ok().entity(applicationService.deleteApplication(readerId)).build();
    }
    
    @Path("/deleteApplications")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addApplication(List<Integer> accountIds) {
        return Response.ok().entity(applicationService.deleteApplications(accountIds)).build();
    }
}
