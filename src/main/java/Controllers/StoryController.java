/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import Models.Story;
import ServiceLayers.LikeService_Impl;
import ServiceLayers.StoryService_Impl;
import ServiceLayers.StoryService_Interface;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jarro
 */
@Path("/stories")
public class StoryController {
    private StoryService_Interface storyService;

    public StoryController() {
        this.storyService = new StoryService_Impl();
    }
    
    @Path("/getStory/{storyId}")
    @GET
    public Response getStory(@PathParam("storyId") Integer storyId) {
        return Response.ok().entity(storyService.getStory(storyId)).build();
    }
    
    @Path("/getAllStories")
    @GET
    public Response getStory() {
        return Response.ok().entity(storyService.getAllStories()).build();
    }
    
    @Path("/getStoriesInGenre/{genreId}")
    @GET
    public Response getStoriesInGenre(@PathParam("genreId") Integer genreId) {
        return Response.ok().entity(storyService.getStoriesInGenre(genreId)).build();
    }
    
    @Path("/addStory")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addStory(Story story) {
        return Response.ok().entity(storyService.addStory(story)).build();
    }
    
    @Path("/updateStory")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateStory(Story story) {
        return Response.ok().entity(storyService.updateStory(story)).build();
    }
    
    @Path("/deleteStory/{storyId}")
    @GET
    public Response deleteStory(@PathParam("storyId") Integer storyId) {
        return Response.ok().entity(storyService.deleteStory(storyId)).build();
    }
    
    @Path("/getTopPicks")
    @GET
    public Response getTopPicks() {
        List<Story> stories = new ArrayList<>();
        List<Integer> storyIds = new LikeService_Impl().getMostLikedBooks(10, Timestamp.valueOf(LocalDateTime.now().minusWeeks(1)), Timestamp.valueOf(LocalDateTime.now()));
        for (Integer storyId:storyIds) {
            stories.add(storyService.getStory(storyId));
        }
        return Response.ok().entity(stories).build();
    }
}
