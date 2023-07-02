/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import Models.Rating;
import ServiceLayers.RatingService_Impl;
import ServiceLayers.RatingService_Interface;
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
@Path("/rating")
public class RatingController {
    private final RatingService_Interface ratingService;
    
    public RatingController(){
        ratingService = new RatingService_Impl();
    }
    
    @Path("/addRating")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addRating(Rating rating){
        return Response.ok().entity(ratingService.addRating(rating)).build();
    }
    
    @Path("/updateRatingValue")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateRatingValue(Rating rating) {
        return Response.ok().entity(ratingService.updateRatingValue(rating)).build();
    }
    
    @Path("/checkRatingExists")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response checkRatingExists(Rating rating) {
        return Response.ok().entity(ratingService.checkRatingExists(rating)).build();
    }
    
    @Path("/getRating/{accountId}/{storyId}")
    @GET
    public Response getRating(@PathParam("accountId")Integer accountId,
                                   @PathParam("storyId")Integer storyId){
        return Response.ok().entity(ratingService.getRating(accountId, storyId)).build();
    }
    
    @Path("/getRatingValue/{storyId}")
    @GET
    public Response getRatingValue(@PathParam("storyId")Integer storyId){
        return Response.ok().entity(ratingService.getRatingValue(storyId)).build();
    }
    
    @Path("/getRatingsByStory/{storyId}")
    @GET
    public Response getRatingsByStory(@PathParam("storyId")Integer storyId){
        List<Rating> storyRatings = new ArrayList<>();
        for(Rating ratings : ratingService.getRatingsByStory(storyId)){
            storyRatings.add(ratings);
        }
        return Response.ok().entity(storyRatings).build();
    }
    
    @Path("/getRatingByReaderId/{accountId}")
    @GET
    public Response getRatingsByReaderId(@PathParam("accountId")Integer accountId){
        List<Rating> readerRatings = new ArrayList<>();
        for(Rating ratings : ratingService.getRatingsByReaderId(accountId)){
            readerRatings.add(ratings);
        }
        return Response.ok().entity(readerRatings).build();
    }
    
    @Path("/getAllRatings")
    @GET
    public Response getAllRatings(){
        List<Rating> allRatings = new ArrayList<>();
        for(Rating ratings : ratingService.getAllRatings()){
            allRatings.add(ratings);
        }
        return Response.ok().entity(allRatings).build();
    }
    
    @Path("getTopHighestRatedStories/{startDate}/{endDate}/{numberOfEntries}")
    @GET
    public Response getTopHighestRatedStoriesInTimePeriod(@PathParam("startDate")String startDate, 
                                                          @PathParam("endDate")String endDate,
                                                          @PathParam("numberOfEntries")Integer numberOfEntries){
        try{
        List<Integer> bookIds = new ArrayList<>();
        Timestamp start = Timestamp.valueOf(LocalDateTime.parse(startDate));
        Timestamp end = Timestamp.valueOf(LocalDateTime.parse(endDate));
        for(Integer storyId : ratingService.getTopHighestRatedStoriesInTimePeriod(start, end, numberOfEntries)){
            bookIds.add(storyId);
        }
        return Response.ok().entity(bookIds).build();
        
        }catch(DateTimeParseException e){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid date format. Please provide valid dates in the format yyyy-MM-dd HH:mm:ss")
                    .build();
        }
    }
}
