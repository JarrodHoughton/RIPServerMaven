package Controllers;

import Models.Genre;
import Models.Story;
import Models.Writer;
import Models.Editor;
import ServiceLayers.*;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

import java.sql.Timestamp;
import java.time.LocalDate;
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
    @Path("/getMostLikedStories")
    public Response getMostLikedStories(
            @QueryParam("numberOfStories") Integer numberOfStories,
            @QueryParam("startDate") String startDate,
            @QueryParam("endDate") String endDate
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

            for (Integer storyId : likeService.getMostLikedBooks(numberOfStories, start, end)) {
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
    @Path("/getStoryLikesByDate")
    public Response getStoryLikesByDate(
            @QueryParam("storyId") Integer storyId,
            @QueryParam("startDate") String startDate,
            @QueryParam("endDate") String endDate
    ) {
        try {
            Timestamp start = getTimestamp(startDate);
            Timestamp end = getTimestamp(endDate);

            if (start == null || end == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Invalid date format. Please provide valid dates in the format 'yyyy-MM-dd HH:mm:ss'.")
                        .build();
            }

            return Response.ok().entity(likeService.getStoryLikesByDate(storyId, start, end)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/getMostViewedStories")
    public Response getMostViewedStories(
            @QueryParam("numberOfEntries") Integer numberOfEntries,
            @QueryParam("startDate") String startDate,
            @QueryParam("endDate") String endDate
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

            List<Integer> storyIds = viewService.getMostViewedStoriesInATimePeriod(numberOfEntries, start, end);
            for (Integer storyId : storyIds) {
                Story story = storyService.getStory(storyId);
                if (story != null) {
                    listOfStories.add(story);
                }
            }

            return Response.ok().entity(listOfStories).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/getStoryViewsByDate")
    public Response getStoryViewsByDate(
            @QueryParam("storyId") Integer storyId,
            @QueryParam("startDate") String startDate,
            @QueryParam("endDate") String endDate
    ) {
        try {
            Timestamp start = getTimestamp(startDate);
            Timestamp end = getTimestamp(endDate);

            if (start == null || end == null) {
                System.out.println("____________INVALID DATE FORMAT RECEIEVED______________");
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Invalid date format. Please provide valid dates in the format 'yyyy-MM-dd HH:mm:ss'.")
                        .build();
            }

            Integer views = viewService.getTheViewsOnAStoryInATimePeriod(storyId, start, end);
            if (views==null) {
                views = 0;
            }
            return Response.ok().entity(views).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/getTopHighestRatedStories")
    public Response getTopHighestRatedStoriesInTimePeriod(
            @QueryParam("startDate") String startDate,
            @QueryParam("endDate") String endDate,
            @QueryParam("numberOfEntries") Integer numberOfEntries
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

            List<Integer> topRatedStoryIds = ratingService.getTopHighestRatedStoriesInTimePeriod(start, end, numberOfEntries);
            if (topRatedStoryIds != null) {
                for (Integer storyId : topRatedStoryIds) {
                    listOfStories.add(storyService.getStory(storyId));
                }
            }

            return Response.ok().entity(listOfStories).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/getStoryRatingByDate")
    public Response getAverageRatingOfAStoryInATimePeriod(
            @QueryParam("startDate") String startDate,
            @QueryParam("endDate") String endDate,
            @QueryParam("storyId") Integer storyId
    ) {
        try {
            Timestamp start = getTimestamp(startDate);
            Timestamp end = getTimestamp(endDate);

            if (start == null || end == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Invalid date format. Please provide valid dates in the format 'yyyy-MM-dd HH:mm:ss'.")
                        .build();
            }

            return Response.ok().entity(ratingService.getAverageRatingOfAStoryInATimePeriod(storyId, start, end)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/getTopGenres")
    public Response getTopGenres(
            @QueryParam("startDate") String startDate,
            @QueryParam("endDate") String endDate,
            @QueryParam("numberOfEntries") Integer numberOfEntries
    ) {
        try {
            List<Genre> topGenres;
            Timestamp start = getTimestamp(startDate);
            Timestamp end = getTimestamp(endDate);

            if (start == null || end == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Invalid date format. Please provide valid dates in the format 'yyyy-MM-dd HH:mm:ss'.")
                        .build();
            }

            topGenres = genreService.getTopGenres(start, end, numberOfEntries);
            
            if (topGenres == null) {
                topGenres = new ArrayList<>();
            }
            return Response.ok().entity(topGenres).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/getGenreViewsByDate")
    public Response getGenreViewsByDate(
            @QueryParam("startDate") String startDate,
            @QueryParam("endDate") String endDate,
            @QueryParam("genreId") Integer genreId
    ) {
        try {
            Timestamp start = getTimestamp(startDate);
            Timestamp end = getTimestamp(endDate);

            if (start == null || end == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Invalid date format. Please provide valid dates in the format 'yyyy-MM-dd HH:mm:ss'.")
                        .build();
            }

            return Response.ok().entity(genreService.getTotalViewsByGenreWithinTimePeriod(genreId, start, end)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/getTopWriters/{numberOfWriters}")
    public Response getTopWriters(
            @PathParam("numberOfWriters") Integer numberOfWriters
    ) {
        try {
            List<Writer> topWriters = new ArrayList<>();

            for (Integer id : writerService.getTopWriters(numberOfWriters)) {
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
    @Path("/getTopWritersByDate")
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

    @GET
    @Path("/getTotalViewsByWriterId/{writerId}")
    public Response getTotalViewsByWriterId(@PathParam("writerId") Integer writerId) {
        try {
            Integer totalViews = writerService.getTotalViewsByWriterId(writerId);
            if (totalViews == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Writer with ID " + writerId + " not found.")
                        .build();
            }
            return Response.ok().entity(totalViews).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    private Timestamp getTimestamp(String date) {
        try {
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime localDateTime = LocalDateTime.parse(date, outputFormatter);
            return Timestamp.valueOf(localDateTime);
        } catch (DateTimeParseException e) {
            System.err.println("Error converting date string to Timestamp: " + e.getMessage());
            return null;
        }
    }
}
