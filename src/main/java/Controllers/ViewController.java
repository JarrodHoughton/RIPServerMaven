/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import Models.View;
import ServiceLayers.ViewService_Impl;
import ServiceLayers.ViewService_Interface;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Kylynn van der Merwe
 */
@Path("/view")
public class ViewController {
    private final ViewService_Interface viewService;

    public ViewController() {
        this.viewService = new ViewService_Impl();
    }
    
    @Path("/addView")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addView(View view){
        return Response.ok().entity(viewService.addView(view)).build();
    }
    
    @Path("/isViewAlreadyAdded")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response isViewAlreadyAdded(View view){
        return Response.ok().entity(viewService.isViewAlreadyAdded(view)).build();
    }
    
    @Path("/getMostViewedStories/{numberOfEntries}/{startDate}/{endDate}")
    @GET
    public Response getMostViewedStoriesInATimePeriod(@PathParam("numberOfEntries")Integer numberOfEntries,
                                                      @PathParam("startDate")String startDate,
                                                      @PathParam("endDate")String endDate){
        try{
            List<Integer> storyIds = new ArrayList<>();
            Timestamp start = Timestamp.valueOf(LocalDateTime.parse(startDate));
            Timestamp end = Timestamp.valueOf(LocalDateTime.parse(endDate));
            
            for(Integer storyId : viewService.getMostViewedStoriesInATimePeriod(numberOfEntries, start, end)){
                storyIds.add(storyId);
            }            
            return Response.ok().entity(storyIds).build();
            
        }catch(DateTimeParseException e){            
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid date format. Please provide valid dates in the format yyyy-MM-dd HH:mm:ss")
                    .build();
        }
    }
    
    @Path("/getTheViewOnAStory/{storyId}/{startDate}/{endDate}")
    @GET
    public Response getTheViewsOnAStoryInATimePeriod(@PathParam("storyId")Integer storyId, 
                                                     @PathParam("startDate")String startDate, 
                                                     @PathParam("endDate")String endDate){
        try{
            Timestamp start = Timestamp.valueOf(LocalDateTime.parse(startDate));
            Timestamp end = Timestamp.valueOf(LocalDateTime.parse(endDate));
            return Response.ok().entity(viewService.getTheViewsOnAStoryInATimePeriod(storyId, start, end)).build();
                    
        }catch(DateTimeParseException e){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid date format. Please provide valid dates in the format yyyy-MM-dd HH:mm:ss")
                    .build();
        }
    }
}
