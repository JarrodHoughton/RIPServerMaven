/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import Models.Story;
import ServiceLayers.StoryService_Impl;
import ServiceLayers.StoryService_Interface;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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
    @POST
    public Response getStory(@PathParam("storyId") Integer storyId) {
        return Response.ok().entity(storyService.getStory(storyId)).build();
    }
    
    @Path("/getAllStories")
    @POST
    public Response getStory() {
        return Response.ok().entity(storyService.getAllStories()).build();
    }
    
    @Path("/getStoriesInGenre/{genreId}")
    @POST
    public Response getStoriesInGenre(@PathParam("genreId") Integer genreId) {
        return Response.ok().entity(storyService.getStoriesInGenre(genreId)).build();
    }
    
    @Path("/addStory")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addStory(Story story) {
        return Response.ok().entity(storyService.addStory(story)).build();
    }
}
