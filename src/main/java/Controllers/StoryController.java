/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import Models.StoriesHolder;
import Models.Story;
import ServiceLayers.StoryService_Impl;
import ServiceLayers.StoryService_Interface;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
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
        return Response.ok().entity(storyService.deleteStory(storyService.getStory(storyId))).build();
    }
    
    @Path("/getTopPicks")
    @GET
    public Response getTopPicks() {
        return Response.ok().entity(storyService.getTopPicks()).build();
    }
    
    @Path("/getSubmittedStories")
    @GET
    public Response getSubmittedStories() {
        return Response.ok().entity(storyService.getSubmittedStories()).build();
    }
    
    @Path("/getRecommendations")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getRecommendations(List<Integer> genreIds) {
        StoriesHolder storiesHolder = new StoriesHolder();
        storiesHolder.setStories(storyService.getRecommendations(genreIds));
        if (storiesHolder.getStories()== null) {
            storiesHolder.setStoryIds(new ArrayList<>());
        }
        return Response.ok().entity(storiesHolder).build();
    }
    
    @Path("/searchForStories/{searchValue}")
    @GET
    public Response searchForStories(@PathParam("searchValue") String searchValue) {
        return Response.ok().entity(storyService.searchForStories(searchValue)).build();
    }
    
    @Path("/getWritersSubmittedStories")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getWritersSubmittedStories(StoriesHolder storiesHolder) {
        storiesHolder.setStories(storyService.getWritersSubmittedStories(storiesHolder.getStoryIds(), storiesHolder.getId()));
        return Response.ok().entity(storiesHolder).build();
    }

    @Path("/getWritersDraftedStories")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getWritersDraftedStories(StoriesHolder storiesHolder) {
        storiesHolder.setStories(storyService.getWritersDraftedStories(storiesHolder.getStoryIds(), storiesHolder.getId()));
        return Response.ok().entity(storiesHolder).build();
    }
    
    @Path("/updateStories")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateStories(List<Story> stories) {
        return Response.ok().entity(storyService.updateStories(stories)).build();
    }
}
