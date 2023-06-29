/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import Models.Genre;
import Models.Story;
import ServiceLayers.LikeService_Impl;
import Utils.DBManager;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.ArrayUtils;

/**
 *
 * @author jarro
 */
public class StoryDao_Impl implements StoryDao_Interface {

    private Connection connection;
    private PreparedStatement prepStmt;
    private ResultSet rs;
    private LikeDao_Interface likeDao;
    private final String ROOT_DIR;
    private final String IMAGES_FILE_PATH;

    public StoryDao_Impl() {
        this.likeDao = new LikeDao_Impl();
        this.ROOT_DIR = System.getProperty("user.dir");
        this.IMAGES_FILE_PATH = ROOT_DIR.substring(0, ROOT_DIR.indexOf("\\")) + "\\RIPServerImages";
        File imageFile = new File(IMAGES_FILE_PATH);
        if (!imageFile.exists()) {
            System.out.println("Directory created: " + imageFile.mkdir() + " " + IMAGES_FILE_PATH);
        } else {
            System.out.println("Directory exists: " + IMAGES_FILE_PATH);
        }
    }

    @Override
    public Story getStory(Integer storyId) {
        Story story = null;
        try {
            story = new Story();
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT * FROM stories WHERE storyId=?;");
            prepStmt.setInt(1, storyId);
            rs = prepStmt.executeQuery();
            if (rs.next()) {
                story.setId(rs.getInt("storyId"));
                story.setTitle(rs.getString("title"));
                story.setBlurb(rs.getString("blurb"));
                story.setContent(rs.getString("content"));
                story.setAuthorId(rs.getInt("accountId"));
                story.setLikeCount(rs.getInt("likeCount"));
                story.setViewCount(rs.getInt("viewCount"));
                story.setRating(rs.getDouble("rating"));
                story.setApproved(rs.getString("approved").charAt(0) == 'T');
                story.setSubmitted(rs.getString("submitted").charAt(0) == 'T');
                story.setRejected(rs.getString("rejected").charAt(0) == 'T');
                story.setCommentsEnabled(rs.getString("commentsEnabled").charAt(0) == 'T');
                story.setImageName(rs.getString("imageName"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            closeConnections();
        }

        if (story != null) {
            story.setImage(getImageById(story));
            story.setGenreIds(getStoryGenres(story.getId()));
        }

        return story;
    }

    @Override
    public List<Story> getAllStories() {
        List<Story> stories = null;
        try {
            stories = new ArrayList<>();
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT * FROM stories;");
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                Story story = new Story();
                story.setId(rs.getInt("storyId"));
                story.setTitle(rs.getString("title"));
                story.setBlurb(rs.getString("blurb"));
                story.setContent(rs.getString("content"));
                story.setAuthorId(rs.getInt("accountId"));
                story.setLikeCount(rs.getInt("likeCount"));
                story.setViewCount(rs.getInt("viewCount"));
                story.setRating(rs.getDouble("rating"));
                story.setApproved(rs.getString("approved").charAt(0) == 'T');
                story.setSubmitted(rs.getString("submitted").charAt(0) == 'T');
                story.setRejected(rs.getString("rejected").charAt(0) == 'T');
                story.setCommentsEnabled(rs.getString("commentsEnabled").charAt(0) == 'T');
                story.setImageName(rs.getString("imageName"));
                stories.add(story);
            }
        } catch (SQLException ex) {
            Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            closeConnections();
        }

        if (stories != null && !stories.isEmpty()) {
            for (Story story : stories) {
                story.setImage(getImageById(story));
                story.setGenreIds(getStoryGenres(story.getId()));
            }
        } else {
            return null;
        }

        return stories;
    }

    @Override
    public List<Story> getRecommendations(List<Integer> genreIds) {
        List<Story> recommendations = null;
        List<Integer> storyIds = null;
        try {
            String genres = "";
            for (int i = 0; i < genreIds.size() - 1; i++) {
                genres += "'" + genreIds.get(i) + "',";
            }
            genres += "'" + genreIds.get(genreIds.size() - 1) + "'";
            storyIds = new ArrayList<>();

            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT SG.storyId\n"
                    + "FROM stories_genres AS SG\n"
                    + "JOIN stories AS S ON SG.storyId = S.storyId\n"
                    + "WHERE SG.genreId IN (" + genres + ") AND S.approved='T' AND S.submitted='T'\n"
                    + "GROUP BY S.storyId\n"
                    + "ORDER BY SUM(S.likeCount) DESC\n"
                    + "LIMIT 10;");
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                storyIds.add(rs.getInt("storyId"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            closeConnections();
        }

        if (storyIds != null && !storyIds.isEmpty()) {
            recommendations = new ArrayList<>();
            for (Integer storyId : storyIds) {
                recommendations.add(getStory(storyId));
            }
        }

        return recommendations;
    }

    @Override
    public List<Story> getTopPicks() {
        List<Story> topStories = new ArrayList<>();
        List<Integer> storyIds = likeDao.getMostLikedBooks(10, Timestamp.valueOf(LocalDateTime.now().minusWeeks(1)), Timestamp.valueOf(LocalDateTime.now()));
        if (storyIds != null && storyIds.isEmpty()) {
            return getApprovedStories(10);
        }

        if (storyIds != null) {
            for (Integer storyId : storyIds) {
                Story story = getStory(storyId);
                if (story.getSubmitted() && story.getApproved()) {
                    topStories.add(story);
                }
            }
        } else {
            return null;
        }
        return topStories;
    }

    @Override
    public List<Story> getApprovedStories(Integer numberOfStories) {
        List<Story> stories = null;
        try {
            stories = new ArrayList<>();
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT * FROM stories WHERE approved='T' AND submitted='T' LIMIT ?;");
            prepStmt.setInt(1, numberOfStories);
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                Story story = new Story();
                story.setId(rs.getInt("storyId"));
                story.setTitle(rs.getString("title"));
                story.setBlurb(rs.getString("blurb"));
                story.setContent(rs.getString("content"));
                story.setAuthorId(rs.getInt("accountId"));
                story.setLikeCount(rs.getInt("likeCount"));
                story.setViewCount(rs.getInt("viewCount"));
                story.setRating(rs.getDouble("rating"));
                story.setApproved(rs.getString("approved").charAt(0) == 'T');
                story.setSubmitted(rs.getString("submitted").charAt(0) == 'T');
                story.setRejected(rs.getString("rejected").charAt(0) == 'T');
                story.setCommentsEnabled(rs.getString("commentsEnabled").charAt(0) == 'T');
                story.setImageName(rs.getString("imageName"));
                stories.add(story);
            }
        } catch (SQLException ex) {
            Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            closeConnections();
        }

        if (stories != null && !stories.isEmpty()) {
            for (Story story : stories) {
                story.setImage(getImageById(story));
                story.setGenreIds(getStoryGenres(story.getId()));
            }
        } else {
            return null;
        }

        return stories;
    }

    @Override
    public List<Story> getSubmittedStories() {
        List<Story> submittedStories = null;
        try {
            submittedStories = new ArrayList<>();
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT * FROM stories WHERE approved='F' AND submitted='T' AND rejected='F';");
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                Story story = new Story();
                story.setId(rs.getInt("storyId"));
                story.setTitle(rs.getString("title"));
                story.setBlurb(rs.getString("blurb"));
                story.setContent(rs.getString("content"));
                story.setAuthorId(rs.getInt("accountId"));
                story.setLikeCount(rs.getInt("likeCount"));
                story.setViewCount(rs.getInt("viewCount"));
                story.setRating(rs.getDouble("rating"));
                story.setApproved(rs.getString("approved").charAt(0) == 'T');
                story.setSubmitted(rs.getString("submitted").charAt(0) == 'T');
                story.setRejected(rs.getString("rejected").charAt(0) == 'T');
                story.setCommentsEnabled(rs.getString("commentsEnabled").charAt(0) == 'T');
                story.setImageName(rs.getString("imageName"));
                submittedStories.add(story);
            }
        } catch (SQLException ex) {
            Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            closeConnections();
        }

        if (submittedStories != null && !submittedStories.isEmpty()) {
            for (Story story : submittedStories) {
                story.setImage(getImageById(story));
                story.setGenreIds(getStoryGenres(story.getId()));
            }
        } else {
            return null;
        }

        return submittedStories;
    }

    @Override
    public List<Story> getStoriesInGenre(Integer genreId) {
        List<Story> stories = null;
        List<Integer> storyIds = null;
        try {
            storyIds = new ArrayList<>();
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT S.storyId FROM stories_genres AS SG INNER JOIN stories AS S ON S.storyId=SG.storyId WHERE SG.genreId=? AND S.submitted='T' AND S.approved='T';");
            prepStmt.setInt(1, genreId);
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                storyIds.add(rs.getInt(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            closeConnections();
        }

        if (storyIds != null && !storyIds.isEmpty()) {
            stories = new ArrayList<>();
            for (Integer storyId : storyIds) {
                stories.add(getStory(storyId));
            }
        } else {
            return null;
        }

        return stories;
    }

    @Override
    public Boolean updateStory(Story story) {
        System.out.println(story);
        Boolean updated;
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("UPDATE stories SET title=?, blurb=?, approved=?, submitted=?, rejected=?, commentsEnabled=?, content=?, viewCount=?, rating=?, likeCount=?, imageName=? WHERE storyId=?;");
            prepStmt.setString(1, story.getTitle());
            prepStmt.setString(2, story.getBlurb());
            prepStmt.setString(3, story.getApproved() ? "T" : "F");
            prepStmt.setString(4, story.getSubmitted() ? "T" : "F");
            prepStmt.setString(5, story.getRejected() ? "T" : "F");
            prepStmt.setString(6, story.getCommentsEnabled() ? "T" : "F");
            prepStmt.setString(7, story.getContent());
            prepStmt.setInt(8, story.getViewCount());
            prepStmt.setDouble(9, story.getRating());
            prepStmt.setInt(10, story.getLikeCount());
            prepStmt.setString(11, story.getImageName());
            prepStmt.setInt(12, story.getId());
            prepStmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            closeConnections();
        }

        if (story.getImage() != null) {
            updated = updateImage(story);
        } else {
            updated = addImageData(story);
        }
        updated = updated && deleteStoryGenres(story.getId()) && addStoryGenres(story);

        return updated;
    }

    @Override
    public Boolean deleteStory(Story story) {
        Boolean deleted = false;
        try {
            deleted = deleteImage(story) && deleteStoryGenres(story.getId()) && deleteStoryLikes(story.getId()) && deleteStoryRatings(story.getId()) && deleteStoryComments(story.getId()) && deleteStoryViews(story.getId());
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("DELETE FROM stories WHERE storyId=?;");
            prepStmt.setInt(1, story.getId());
            prepStmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            closeConnections();
        }
        return deleted;
    }

    private Boolean deleteStoryGenres(Integer storyId) {
        Boolean deleted = false;
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("DELETE FROM stories_genres WHERE storyId=?;");
            prepStmt.setInt(1, storyId);
            prepStmt.executeUpdate();
            deleted = true;
        } catch (SQLException ex) {
            Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            closeConnections();
        }
        return deleted;
    }

    private Boolean deleteStoryLikes(Integer storyId) {
        Boolean deleted = false;
        try {
            deleted = deleteStoryGenres(storyId);
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("DELETE FROM likes WHERE storyId=?;");
            prepStmt.setInt(1, storyId);
            prepStmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            closeConnections();
        }
        return deleted;
    }

    private Boolean deleteStoryRatings(Integer storyId) {
        Boolean deleted = false;
        try {
            deleted = deleteStoryGenres(storyId);
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("DELETE FROM ratings WHERE storyId=?;");
            prepStmt.setInt(1, storyId);
            prepStmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            closeConnections();
        }
        return deleted;
    }

    private Boolean deleteStoryComments(Integer storyId) {
        Boolean deleted = false;
        try {
            deleted = deleteStoryGenres(storyId);
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("DELETE FROM comments WHERE storyId=?;");
            prepStmt.setInt(1, storyId);
            prepStmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            closeConnections();
        }
        return deleted;
    }

    private Boolean deleteStoryViews(Integer storyId) {
        Boolean deleted = false;
        try {
            deleted = deleteStoryGenres(storyId);
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("DELETE FROM views WHERE storyId=?;");
            prepStmt.setInt(1, storyId);
            prepStmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            closeConnections();
        }
        return deleted;
    }

    @Override
    public Boolean searchForTitle(String title) {
        Boolean titleExists = false;
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT COUNT(*) AS titleCount FROM stories WHERE title = ?;"
            );
            prepStmt.setString(1, title);
            rs = prepStmt.executeQuery();
            if (rs.next()) {
                titleExists = rs.getInt("titleCount") > 0;
            }
        } catch (SQLException ex) {
            Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            closeConnections();
        }
        return titleExists;
    }

    @Override
    public Boolean addStory(Story story) {
        if (searchForTitle(story.getTitle())) {
            return false;
        }
        Boolean added = false;
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement(
                    "INSERT IGNORE INTO `ripdb`.`stories` (`title`, `blurb`, `approved`, `rejected`, `submitted`, `commentsEnabled`, `content`, `accountId`, `viewCount`, `likeCount`, `rating`, imageName) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?);"
            );
            prepStmt.setString(1, story.getTitle());
            prepStmt.setString(2, story.getBlurb());
            prepStmt.setString(3, story.getApproved() ? "T" : "F");
            prepStmt.setString(4, story.getRejected() ? "T" : "F");
            prepStmt.setString(5, story.getSubmitted() ? "T" : "F");
            prepStmt.setString(6, story.getCommentsEnabled() ? "T" : "F");
            prepStmt.setString(7, story.getContent());
            prepStmt.setInt(8, story.getAuthorId());
            prepStmt.setInt(9, story.getViewCount());
            prepStmt.setInt(10, story.getLikeCount());
            prepStmt.setDouble(11, story.getRating());
            prepStmt.setString(12, story.getImageName());
            prepStmt.executeUpdate();

            prepStmt = connection.prepareStatement("select storyId from stories where title = ?;");
            prepStmt.setString(1, story.getTitle());
            rs = prepStmt.executeQuery();
            if (rs.next()) {
                story.setId(rs.getInt(1));
            }

            if (story.getId() != null && story.getImage() != null) {
                added = addImageData(story);
            }
            added = added && addStoryGenres(story);
        } catch (SQLException ex) {
            Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            closeConnections();
        }
        return added;
    }

    private Boolean addStoryGenre(Integer storyId, Integer genreId) {
        Boolean added = false;
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("INSERT INTO stories_genres(storyId, genreId) VALUES(?, ?);");
            prepStmt.setInt(1, storyId);
            prepStmt.setInt(2, genreId);
            prepStmt.executeUpdate();
            added = true;
        } catch (SQLException ex) {
            Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            closeConnections();
        }
        return added;
    }

    private Boolean addStoryGenres(Story story) {
        Boolean added = true;
        for (Integer genreId : story.getGenreIds()) {
            if (!addStoryGenre(story.getId(), genreId)) {
                added = false;
            }
        }
        return added;
    }

    private List<Integer> getStoryGenres(Integer storyId) {
        List<Integer> genreIds = new ArrayList<>();
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT genreId FROM stories_genres WHERE storyId=?;");
            prepStmt.setInt(1, storyId);
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                genreIds.add(rs.getInt(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            closeConnections();
        }
        return genreIds;
    }

    private Byte[] getImageById(Story story) {
        String relativePath = IMAGES_FILE_PATH + "\\" + story.getId() + story.getImageName();
        Byte[] imageData = null;
        InputStream imageInput = null;
        try {
            File imageFile = new File(relativePath);
            if (imageFile.exists()) {
                imageInput = new FileInputStream(imageFile);
                imageData = ArrayUtils.toObject(imageInput.readAllBytes());
                if (imageData.length == 0) {
                    return null;
                }
            } else {
                System.out.println("Image not found at: " + relativePath);
            }
        } catch (IOException ex) {
            Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            if (imageInput != null) {
                try {
                    imageInput.close();
                } catch (IOException ex) {
                    Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return imageData;
    }

    private Boolean addImageData(Story story) {
        String relativePath = IMAGES_FILE_PATH + "\\" + story.getId() + story.getImageName();
        Boolean imageAdded = false;
        FileOutputStream output = null;
        try {
            File imageFile = new File(relativePath);
            if (imageFile.createNewFile() && story.getImage() != null) {
                output = new FileOutputStream(imageFile);
                byte[] imageData = ArrayUtils.toPrimitive(story.getImage());
                output.write(imageData);
            } else {
                System.out.println("Could not create new file at: " + relativePath);
            }
            imageAdded = true;
        } catch (IOException ex) {
            Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Could not create new file at: " + relativePath);
            return false;
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException ex) {
                    Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return imageAdded;
    }

    private Boolean updateImage(Story story) {
        return deleteImage(story) && addImageData(story);
    }

    private Boolean deleteImage(Story story) {
        String relativePath = IMAGES_FILE_PATH + "\\" + story.getId() + story.getImageName();
        Boolean imageDeleted = false;
        File imageFile = new File(relativePath);
        if (imageFile.exists()) {
            imageDeleted = imageFile.delete();
        } else {
            Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.FINE, "Image not found at: {0}", relativePath);
            System.out.println("Image not found at: " + relativePath);
            return true;
        }
        return imageDeleted;
    }

    @Override
    public List<Story> searchForStories(String searchValue) {
        List<Story> storySearchResults = null;
        List<Integer> storyIds = null;
        try {
            storyIds = new ArrayList<>();
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT storyId FROM stories WHERE title LIKE ? AND submitted = 'T' AND approved = 'T';");
            prepStmt.setString(1, "%" + searchValue + "%");
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                storyIds.add(rs.getInt("storyId"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            closeConnections();
        }

        if (storyIds != null && !storyIds.isEmpty()) {
            storySearchResults = new ArrayList<>();
            for (Integer storyId : storyIds) {
                storySearchResults.add(getStory(storyId));
            }
        }
        return storySearchResults;
    }

    @Override
    public List<Story> getWritersSubmittedStories(List<Integer> storyIds, Integer writerId) {
        List<Story> submittedStories = null;
        try {
            submittedStories = new ArrayList<>();
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT * FROM stories WHERE submitted='T' AND accountId=?;");
            prepStmt.setInt(1, writerId);
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                Story story = new Story();
                story.setId(rs.getInt("storyId"));
                story.setTitle(rs.getString("title"));
                story.setBlurb(rs.getString("blurb"));
                story.setContent(rs.getString("content"));
                story.setAuthorId(rs.getInt("accountId"));
                story.setLikeCount(rs.getInt("likeCount"));
                story.setViewCount(rs.getInt("viewCount"));
                story.setRating(rs.getDouble("rating"));
                story.setApproved(rs.getString("approved").charAt(0) == 'T');
                story.setSubmitted(rs.getString("submitted").charAt(0) == 'T');
                story.setRejected(rs.getString("rejected").charAt(0) == 'T');
                story.setCommentsEnabled(rs.getString("commentsEnabled").charAt(0) == 'T');
                story.setImageName(rs.getString("imageName"));
                submittedStories.add(story);
            }
        } catch (SQLException ex) {
            Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            closeConnections();
        }

        if (submittedStories != null && !submittedStories.isEmpty()) {
            for (Story story : submittedStories) {
                story.setImage(getImageById(story));
                story.setGenreIds(getStoryGenres(story.getId()));
            }
        } else {
            return null;
        }
        return submittedStories;
    }

    @Override
    public List<Story> getWritersDraftedStories(List<Integer> storyIds, Integer writerId) {
        List<Story> draftedStories = null;
        try {
            draftedStories = new ArrayList<>();
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT * FROM stories WHERE submitted='F' AND accountId=?;");
            prepStmt.setInt(1, writerId);
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                Story story = new Story();
                story.setId(rs.getInt("storyId"));
                story.setTitle(rs.getString("title"));
                story.setBlurb(rs.getString("blurb"));
                story.setContent(rs.getString("content"));
                story.setAuthorId(rs.getInt("accountId"));
                story.setLikeCount(rs.getInt("likeCount"));
                story.setViewCount(rs.getInt("viewCount"));
                story.setRating(rs.getDouble("rating"));
                story.setApproved(rs.getString("approved").charAt(0) == 'T');
                story.setSubmitted(rs.getString("submitted").charAt(0) == 'T');
                story.setRejected(rs.getString("rejected").charAt(0) == 'T');
                story.setCommentsEnabled(rs.getString("commentsEnabled").charAt(0) == 'T');
                story.setImageName(rs.getString("imageName"));
                draftedStories.add(story);
            }
        } catch (SQLException ex) {
            Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            closeConnections();
        }

        if (draftedStories != null && !draftedStories.isEmpty()) {
            for (Story story : draftedStories) {
                story.setImage(getImageById(story));
                story.setGenreIds(getStoryGenres(story.getId()));
            }
        } else {
            return null;
        }
        return draftedStories;
    }

    private void closeConnections() {
        if (rs != null) {
            try {
                rs.close();
                rs = null;
            } catch (SQLException ex) {
                Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (prepStmt != null) {
            try {
                prepStmt.close();
                prepStmt = null;
            } catch (SQLException ex) {
                Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException ex) {
                Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
