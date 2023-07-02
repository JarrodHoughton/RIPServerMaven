/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import Models.Like;
import ServiceLayers.LikeService_Impl;
import ServiceLayers.LikeService_Interface;
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
@Path("/like")
public class LikeController {
    private final LikeService_Interface likeService;

    public LikeController() {
        this.likeService = new LikeService_Impl();
    }
    
    @Path("/addLike")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addLike(Like like){
        return Response.ok().entity(likeService.addLike(like)).build();
    }
    
    @Path("/deleteLike")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteLike(Like like){
        return Response.ok().entity(likeService.deleteLike(like)).build();
    }
    
    @Path("/getLikesByReaderId/{accountId}")
    @GET
    public Response getLikesByReaderId(@PathParam("accountId")Integer accountId){
        List<Like> readerLikes = new ArrayList<>();        
        for(Like like : likeService.getLikesByReaderId(accountId)){
            readerLikes.add(like);
        }
        return Response.ok().entity(readerLikes).build();
    }
    
    @Path("/getLikesByStory/{storyId}")
    @GET    
    public Response getLikesByStory(@PathParam("storyId")Integer storyId){
        List<Like> storyLikes = new ArrayList<>();
        for(Like like : likeService.getLikesByStory(storyId)){
            storyLikes.add(like);
        }
        return Response.ok().entity(storyLikes).build();
    }
    
    @Path("/getStoryLikesByDate/{storyId}/{startDate}/{endDate}")
    @GET
    public Response getStoryLikesByDate(@PathParam("storyId")Integer storyId,
                                        @PathParam("startDate")String startDate,
                                        @PathParam("endDate")String endDate){
        try{
            Timestamp start = Timestamp.valueOf(LocalDateTime.parse(startDate));
            Timestamp end = Timestamp.valueOf(LocalDateTime.parse(endDate));
            return Response.ok().entity(likeService.getStoryLikesByDate(storyId, start, end)).build();
        
        }catch(DateTimeParseException e){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid date format. Please provide valid dates in the format yyyy-MM-dd HH:mm:ss")
                    .build();
        }
        
    }
    
    @Path("/getMostLikedBooks/{numberOfBooks}/{startDate}/{endDate}")
    @GET
    public Response getMostLikedBooks(@PathParam("numberOfBooks")Integer numberOFBooks,
                                      @PathParam("startDate")String startDate,
                                      @PathParam("endDate")String endDate){
        try{
            List<Integer> bookIds = new ArrayList<>();
            Timestamp start = Timestamp.valueOf(LocalDateTime.parse(startDate));
            Timestamp end = Timestamp.valueOf(LocalDateTime.parse(endDate));
            
            for(Integer storyId : likeService.getMostLikedBooks(numberOFBooks, start, end)){
                bookIds.add(storyId);
            }            
            return Response.ok().entity(bookIds).build();
            
        }catch(DateTimeParseException e){            
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid date format. Please provide valid dates in the format yyyy-MM-dd HH:mm:ss")
                    .build();
        }       
    }
    
    @Path("/checkIfLikeExists")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response checkIfLikeExists(Like like){
        return Response.ok().entity(likeService.searchForLike(like)).build();
    }
}
