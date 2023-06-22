package Controllers;

import Models.Genre;
import Models.Story;
import Models.Writer;
import Models.Editor;
import ServiceLayers.EditorService_Impl;
import ServiceLayers.EditorService_Interface;
import ServiceLayers.GenreService_Impl;
import ServiceLayers.GenreService_Interface;
import ServiceLayers.LikeService_Impl;
import ServiceLayers.LikeService_Interface;
import ServiceLayers.RatingService_Impl;
import ServiceLayers.RatingService_Interface;
import ServiceLayers.StoryService_Impl;
import ServiceLayers.StoryService_Interface;
import ServiceLayers.ViewService_Impl;
import ServiceLayers.ViewService_Interface;
import ServiceLayers.WriterService_Impl;
import ServiceLayers.WriterService_Interface;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Path("/datareport")
public class DataReportController {

    private LikeService_Interface likeService;
    private ViewService_Interface viewService;
    private RatingService_Interface ratingService;
    private EditorService_Interface editorService;
    private WriterService_Interface writerService;
    private StoryService_Interface storyService;
    private GenreService_Interface genreService;

    public DataReportController() {
        this.likeService = new LikeService_Impl();
        this.viewService = new ViewService_Impl();
        this.ratingService = new RatingService_Impl();
        this.editorService = new EditorService_Impl();
        this.writerService = new WriterService_Impl();
        this.storyService = new StoryService_Impl();
        this.genreService = new GenreService_Impl();
    }

    @Path("/getMostLikedStories/{numberOfStories}/{startDate}/{endDate}")
    @GET
    public Response getMostLikedStories(@PathParam("numberOfStories") Integer numberOfStories,
                                      @PathParam("startDate") String startDate,
                                      @PathParam("endDate") String endDate) {
        List<Story> listOfStories = new ArrayList<>();
        Timestamp start = getTimestamp(startDate);
        Timestamp end = getTimestamp(endDate);
        for (Integer storyId : likeService.getMostLikedBooks(numberOfStories, start, end)) {
            listOfStories.add(storyService.getStory(storyId));
        }
        return Response.ok().entity(listOfStories).build();
    }

    // To get the number of likes on a story within a date range
    @Path("/getStoryLikesByDate/{storyId}/{startDate}/{endDate}")
    @GET
    public Response getStoryLikesByDate(@PathParam("storyId") Integer storyId,
                                        @PathParam("startDate") String startDate,
                                        @PathParam("endDate") String endDate) {
        Timestamp start = getTimestamp(startDate);
        Timestamp end = getTimestamp(endDate);
        return Response.ok().entity(likeService.getStoryLikesByDate(storyId, start, end)).build();
    }

    @Path("/getmostviewedstories/{numberOfStories}/{startDate}/{endDate}")
    @GET
    public Response getMostViewedStories(@PathParam("numberOfStories") Integer numberOfStories,
                                         @PathParam("startDate") String startDate,
                                         @PathParam("endDate") String endDate) {
        List<Story> listOfStories = new ArrayList<>();
        Timestamp start = getTimestamp(startDate);
        Timestamp end = getTimestamp(endDate);
        for (Integer storyId : viewService.getMostViewedStoriesInATimePeriod(numberOfStories, start, end)){
            listOfStories.add(storyService.getStory(storyId));
        }
        return Response.ok().entity(listOfStories).build();
        
    }

    // To get the number of views on a story within a date range
    @Path("/getstoryviewsbydate/{storyId}/{startDate}/{endDate}")
    @GET
    public Response getStoryViewsByDate(@PathParam("storyId") Integer storyId,
                                        @PathParam("startDate") String startDate,
                                        @PathParam("endDate") String endDate) {
        Timestamp start = getTimestamp(startDate);
        Timestamp end = getTimestamp(endDate);
        return Response.ok().entity(viewService.getTheViewsOnAStoryInATimePeriod(storyId, start, end)).build();
    }

    @Path("/getTopHighestRatedStories/{startDate}/{endDate}/{numberOfEntries}")
    @GET
    public Response getTopHighestRatedStoriesInTimePeriod(@PathParam("startDate") String startDate,
                                                          @PathParam("endDate") String endDate,
                                                          @PathParam("numberOfEntries") Integer numberOfEntries) {
        List<Story> listOfStories = new ArrayList<>();
        Timestamp start = getTimestamp(startDate);
        Timestamp end = getTimestamp(endDate);
        for (Integer storyId : ratingService.getTopHighestRatedStoriesInTimePeriod(start, end, numberOfEntries)) {
            listOfStories.add(storyService.getStory(storyId));
        }
        return Response.ok().entity(listOfStories).build();
    }
    
    @Path("/getTopGenres/{startDate}/{endDate}/{numberOfEntries}")
    @GET
    public Response getTopGenres(@PathParam("startDate") String startDate,
            @PathParam("endDate") String endDate,
            @PathParam("numberOfEntries") Integer numberOfEntries) {
        
        List<Genre> topGenres = new ArrayList<>();
        Timestamp start = getTimestamp(startDate);
        Timestamp end = getTimestamp(endDate);
        
        for (Genre genre : genreService.getTopGenres(start, end, numberOfEntries)) {
            topGenres.add(genre);
        }

        return Response.ok().entity(topGenres).build();
    }
    
    @Path("/getTopWriters/{numberOfEntries}")
    @GET
    public Response getTopWriters(@PathParam("numberOfEntries") Integer numberOfEntries) {
        
        List<Writer> topWriters = new ArrayList<>();
        
        for (Integer id : writerService.getTopWriters(numberOfEntries)) {
            topWriters.add(writerService.getWriter(id));
        }

        return Response.ok().entity(topWriters).build();
    }
    
    @Path("/getTopWritersByDate/{startDate}/{endDate}/{numberOfEntries}")
    @GET
    public Response getTopWritersByDate(@PathParam("startDate") String startDate,
            @PathParam("endDate") String endDate,
            @PathParam("numberOfEntries") Integer numberOfEntries) {
        
        List<Writer> topWriters = new ArrayList<>();
        Timestamp start = getTimestamp(startDate);
        Timestamp end = getTimestamp(endDate);
        
        for (Integer id : writerService.getTopWritersByDate(numberOfEntries, start, end)) {
            topWriters.add(writerService.getWriter(id));
        }

        return Response.ok().entity(topWriters).build();
    }
    
    @Path("/getTopEditors/{numberOfEntries}")
    @GET
    public Response getTopEditors(@PathParam("numberOfEntries") Integer numberOfEntries) {
        
        List<Editor> topEditors = new ArrayList<>();
        
        for (Integer id : editorService.getTopEditors(numberOfEntries)) {
            topEditors.add(editorService.getEditor(id));
        }

        return Response.ok().entity(topEditors).build();
    }
    
    
    
    
    
    
    private Timestamp getTimestamp(String date) {
        return Timestamp.valueOf(LocalDateTime.parse(date));
    }
}

