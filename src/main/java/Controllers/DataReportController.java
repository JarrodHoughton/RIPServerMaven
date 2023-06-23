package Controllers;

import Models.Genre;
import Models.Story;
import Models.Writer;
import Models.Editor;
import ServiceLayers.*;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Path("/datareport")
public class DataReportController {
    private final LikeService_Interface likeService;
    private final ViewService_Interface viewService;
    private final RatingService_Interface ratingService;
    private final EditorService_Interface editorService;
    private final WriterService_Interface writerService;
    private final StoryService_Interface storyService;
    private final GenreService_Interface genreService;

    public DataReportController() {
        this.likeService = new LikeService_Impl();
        this.viewService = new ViewService_Impl();
        this.ratingService = new RatingService_Impl();
        this.editorService = new EditorService_Impl();
        this.writerService = new WriterService_Impl();
        this.storyService = new StoryService_Impl();
        this.genreService = new GenreService_Impl();
    }

    @GET
    @Path("/getMostLikedStories/{numberOfStories}/{startDate}/{endDate}")
    public Response getMostLikedStories(
            @PathParam("numberOfStories") String numberOfStories,
            @PathParam("startDate") String startDate,
            @PathParam("endDate") String endDate
    ) {
        try {
            List<Story> listOfStories = new ArrayList<>();
            Timestamp start = getTimestamp(startDate);
            Timestamp end = getTimestamp(endDate);

            if (start == null || end == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Invalid date format. Please provide valid dates in the format 'yyyy-MM-dd HH:mm:ss'.")
                        .build();
            }
            
            Integer numberOfEntries = Integer.valueOf(numberOfStories);

            for (Integer storyId : likeService.getMostLikedBooks(numberOfEntries, start, end)) {
                listOfStories.add(storyService.getStory(storyId));
            }

            return Response.ok().entity(listOfStories).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/getStoryLikesByDate/{storyId}/{startDate}/{endDate}")
    public Response getStoryLikesByDate(
            @PathParam("storyId") String storyId,
            @PathParam("startDate") String startDate,
            @PathParam("endDate") String endDate
    ) {
        try {
            Timestamp start = getTimestamp(startDate);
            Timestamp end = getTimestamp(endDate);

            if (start == null || end == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Invalid date format. Please provide valid dates in the format 'yyyy-MM-dd HH:mm:ss'.")
                        .build();
            }
            
            Integer theId = Integer.valueOf(storyId);

            return Response.ok().entity(likeService.getStoryLikesByDate(theId, start, end)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/getMostViewedStories/{numberOfStories}/{startDate}/{endDate}")
    public Response getMostViewedStories(
            @PathParam("numberOfStories") Integer numberOfStories,
            @PathParam("startDate") String startDate,
            @PathParam("endDate") String endDate
    ) {
        try {
            List<Story> listOfStories = new ArrayList<>();
            Timestamp start = getTimestamp(startDate);
            Timestamp end = getTimestamp(endDate);

            if (start == null || end == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Invalid date format. Please provide valid dates in the format 'yyyy-MM-dd HH:mm:ss'.")
                        .build();
            }

            for (Integer storyId : viewService.getMostViewedStoriesInATimePeriod(numberOfStories, start, end)) {
                listOfStories.add(storyService.getStory(storyId));
            }

            return Response.ok().entity(listOfStories).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/getStoryViewsByDate/{storyId}/{startDate}/{endDate}")
    public Response getStoryViewsByDate(
            @PathParam("storyId") Integer storyId,
            @PathParam("startDate") String startDate,
            @PathParam("endDate") String endDate
    ) {
        try {
            Timestamp start = getTimestamp(startDate);
            Timestamp end = getTimestamp(endDate);

            if (start == null || end == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Invalid date format. Please provide valid dates in the format 'yyyy-MM-dd HH:mm:ss'.")
                        .build();
            }

            return Response.ok().entity(viewService.getTheViewsOnAStoryInATimePeriod(storyId, start, end)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/getTopHighestRatedStories/{startDate}/{endDate}/{numberOfEntries}")
    public Response getTopHighestRatedStoriesInTimePeriod(
            @PathParam("startDate") String startDate,
            @PathParam("endDate") String endDate,
            @PathParam("numberOfEntries") Integer numberOfEntries
    ) {
        try {
            List<Story> listOfStories = new ArrayList<>();
            Timestamp start = getTimestamp(startDate);
            Timestamp end = getTimestamp(endDate);

            if (start == null || end == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Invalid date format. Please provide valid dates in the format 'yyyy-MM-dd HH:mm:ss'.")
                        .build();
            }

            for (Integer storyId : ratingService.getTopHighestRatedStoriesInTimePeriod(start, end, numberOfEntries)) {
                listOfStories.add(storyService.getStory(storyId));
            }

            return Response.ok().entity(listOfStories).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/getTopGenres/{startDate}/{endDate}/{numberOfEntries}")
    public Response getTopGenres(
            @PathParam("startDate") String startDate,
            @PathParam("endDate") String endDate,
            @PathParam("numberOfEntries") Integer numberOfEntries
    ) {
        try {
            List<Genre> topGenres = new ArrayList<>();
            Timestamp start = getTimestamp(startDate);
            Timestamp end = getTimestamp(endDate);

            if (start == null || end == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Invalid date format. Please provide valid dates in the format 'yyyy-MM-dd HH:mm:ss'.")
                        .build();
            }

            for (Genre genre : genreService.getTopGenres(start, end, numberOfEntries)) {
                topGenres.add(genre);
            }

            return Response.ok().entity(topGenres).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/getTopWriters/{numberOfEntries}")
    public Response getTopWriters(
            @PathParam("numberOfEntries") Integer numberOfEntries
    ) {
        try {
            List<Writer> topWriters = new ArrayList<>();

            for (Integer id : writerService.getTopWriters(numberOfEntries)) {
                topWriters.add(writerService.getWriter(id));
            }

            return Response.ok().entity(topWriters).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/getTopWritersByDate/{startDate}/{endDate}/{numberOfEntries}")
    public Response getTopWritersByDate(
            @PathParam("startDate") String startDate,
            @PathParam("endDate") String endDate,
            @PathParam("numberOfEntries") Integer numberOfEntries
    ) {
        try {
            List<Writer> topWriters = new ArrayList<>();
            Timestamp start = getTimestamp(startDate);
            Timestamp end = getTimestamp(endDate);

            if (start == null || end == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Invalid date format. Please provide valid dates in the format 'yyyy-MM-dd HH:mm:ss'.")
                        .build();
            }

            for (Integer id : writerService.getTopWritersByDate(numberOfEntries, start, end)) {
                topWriters.add(writerService.getWriter(id));
            }

            return Response.ok().entity(topWriters).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/getTopEditors/{numberOfEntries}")
    public Response getTopEditors(
            @PathParam("numberOfEntries") Integer numberOfEntries
    ) {
        try {
            List<Editor> topEditors = new ArrayList<>();

            for (Integer id : editorService.getTopEditors(numberOfEntries)) {
                topEditors.add(editorService.getEditor(id));
            }

            return Response.ok().entity(topEditors).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    private Timestamp getTimestamp(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime localDateTime = LocalDateTime.parse(date, formatter);
            return Timestamp.valueOf(localDateTime);
        } catch (DateTimeParseException e) {
            System.err.println("Error converting date string to Timestamp: " + e.getMessage());
            return null;
        }
    }
}
