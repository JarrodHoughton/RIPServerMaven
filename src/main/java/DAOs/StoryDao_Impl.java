/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import Models.Genre;
import Models.Story;
import ServiceLayers.LikeService_Impl;
import Utils.DBManager;
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

    public StoryDao_Impl() {
        this.likeDao = new LikeDao_Impl();
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
            }
            story.setImage(getImageById(storyId));
            story.setImageName(getImageNameById(storyId));

            List<Integer> genreIds = new ArrayList<>();
            
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT genreId FROM stories_genres WHERE storyId=?;");
            prepStmt.setInt(1, storyId);
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                genreIds.add(rs.getInt(1));
            }
            story.setGenreIds(genreIds);
        } catch (SQLException ex) {
            Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            closeConnections();
        }
        return story;
    }

    private Byte[] getImageById(int storyId) {
        Byte[] data = null;
        try {
            String query = "SELECT * FROM images WHERE storyId = ?";
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement(query);
            prepStmt.setInt(1, storyId);
            rs = prepStmt.executeQuery();
            if (rs.next()) {
                data = ArrayUtils.toObject(rs.getBytes("imageData"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return data;
        } finally {
            closeConnections();
        }
        return data;
    }

    private String getImageNameById(int storyId) {
        String imageName = null;
        try {
            String query = "SELECT * FROM images WHERE storyId = ?";
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement(query);
            prepStmt.setInt(1, storyId);
            rs = prepStmt.executeQuery();
            if (rs.next()) {
                imageName = rs.getString("imageName");
            }
        } catch (SQLException ex) {
            Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return imageName;
        } finally {
            closeConnections();
        }
        return imageName;
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
                stories.add(story);
            }

            for (Story story : stories) {
                story.setImage(getImageById(story.getId()));
                story.setImageName(getImageNameById(story.getId()));
                story.setGenreIds(getStoryGenres(story.getId()));
            }
        } catch (SQLException ex) {
            Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            closeConnections();
        }
        return stories;
    }
    
    @Override
    public List<Story> getRecommendations(List<Integer> genreIds) {
        if (genreIds!=null) {
            return null;
        } else if (genreIds.isEmpty()) {
            return null;
        }
        List<Story> recommendations = null;
        try {
            String genres = "";
            for (int i = 0; i < genreIds.size()-1; i++) {
                genres += "'"+genreIds.get(i)+"',";
            }
            genres += "'"+genreIds.get(genreIds.size()-1)+"'";
            recommendations = new ArrayList<>();
            List<Integer> storyIds = new ArrayList<>();
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT SG.storyId\n"
                    + "FROM stories_genres AS SG\n"
                    + "JOIN stories AS S ON SG.storyId = S.storyId\n"
                    + "WHERE SG.genreId IN ("+genres+") AND S.approved='T' AND S.submitted='T'\n"
                    + "GROUP BY S.storyId\n"
                    + "ORDER BY SUM(S.likeCount) DESC\n"
                    + "LIMIT 10;");
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                storyIds.add(rs.getInt("storyId"));
            }
            
            for (Integer storyId : storyIds) {
                recommendations.add(getStory(storyId));
            }
        } catch (SQLException ex) {
            Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            closeConnections();
        }
        return recommendations;
    }

    @Override
    public List<Story> getTopPicks() {
        List<Story> topStories = new ArrayList<>();
        List<Integer> storyIds = likeDao.getMostLikedBooks(10, Timestamp.valueOf(LocalDateTime.now().minusWeeks(1)), Timestamp.valueOf(LocalDateTime.now()));
        if (storyIds.isEmpty()) {
            return getApprovedStories(10);
        }
        for (Integer storyId:storyIds) {
            Story story = getStory(storyId);
            if (story.getSubmitted()&&story.getApproved()) {
                topStories.add(story);
            }
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
                stories.add(story);
            }

            for (Story story : stories) {
                story.setImage(getImageById(story.getId()));
                story.setImageName(getImageNameById(story.getId()));
                story.setGenreIds(getStoryGenres(story.getId()));
            }
        } catch (SQLException ex) {
            Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            closeConnections();
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
                submittedStories.add(story);
            }

            for (Story story : submittedStories) {
                story.setImage(getImageById(story.getId()));
                story.setImageName(getImageNameById(story.getId()));
                story.setGenreIds(getStoryGenres(story.getId()));
            }
            
            if (submittedStories.isEmpty()) {
                return null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            closeConnections();
        }
        return submittedStories;
    }

    @Override
    public List<Story> getStoriesInGenre(Integer genreId) {
        List<Story> stories = null;
        try {
            stories = new ArrayList<>();
            List<Integer> storyIds = new ArrayList<>();
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT S.storyId FROM stories_genres AS SG INNER JOIN stories AS S ON S.storyId=SG.storyId WHERE SG.genreId=? AND S.submitted='T' AND S.approved='T';");
            prepStmt.setInt(1, genreId);
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                storyIds.add(rs.getInt(1));
            }
            
            for (Integer storyId : storyIds) {
                stories.add(getStory(storyId));
            }
            
            if (stories.isEmpty()) {
                return null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            closeConnections();
        }
        return stories;
    }

    @Override
    public Boolean updateStory(Story story) {
        Boolean updated = false;
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("UPDATE stories SET title=?, blurb=?, approved=?, submitted=?, rejected=?,commentsEnabled=?, content=?, viewCount=?, rating=?, likeCount=? WHERE storyId=?;");
            prepStmt.setString(1, story.getTitle());
            prepStmt.setString(2, story.getBlurb());
            prepStmt.setString(3, story.getApproved() ? "T" : "F");
            prepStmt.setString(4, story.getSubmitted() ? "T" : "F");
            prepStmt.setString(5, story.getRejected()? "T" : "F");
            prepStmt.setString(6, story.getCommentsEnabled() ? "T" : "F");
            prepStmt.setString(7, story.getContent());
            prepStmt.setInt(8, story.getViewCount());
            prepStmt.setDouble(9, story.getRating());
            prepStmt.setInt(10, story.getLikeCount());
            prepStmt.setInt(11, story.getId());
            prepStmt.executeUpdate();
            updateImageAndImageName(story.getId(), story.getImage(), story.getImageName());
            updated = deleteStoryGenres(story.getId()) && addStoryGenres(story);
        } catch (SQLException ex) {
            Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            closeConnections();
        }
        return updated;
    }

    @Override
    public Boolean deleteStory(Integer storyId) {
        Boolean deleted = false;
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("DELETE FROM `ripdb`.`stories` WHERE `accountId`=?;");
            prepStmt.setInt(1, storyId);
            prepStmt.executeUpdate();
            deleteImage(storyId);
            deleted = deleteStoryGenres(storyId);
        } catch (SQLException ex) {
            Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            closeConnections();
        }
        return deleted;
    }

    @Override
    public Boolean addStory(Story story) {
        Boolean added = false;
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement(
                    "INSERT IGNORE INTO `ripdb`.`stories` (`title`, `blurb`, `approved`, `rejected`, `submitted`, `commentsEnabled`, `content`, `accountId`, `viewCount`, `likeCount`, `rating`) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?);"
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
            prepStmt.executeUpdate();

            prepStmt = connection.prepareStatement("select storyId from stories where title = ? and blurb = ?;");
            prepStmt.setString(1, story.getTitle());
            prepStmt.setString(2, story.getBlurb());
            rs = prepStmt.executeQuery();
            if (rs.next()) {
                story.setId(rs.getInt(1));
            }
            addImageData(story);
            added = addStoryGenres(story);
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

    private void addImageData(Story story) {
        String sql = "INSERT INTO images (storyId, imageData, imageName) VALUES (?, ?, ?);";
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement(sql);
            prepStmt.setInt(1, story.getId());
            prepStmt.setBytes(2, ArrayUtils.toPrimitive(story.getImage()));
            prepStmt.setString(3, story.getImageName());
            prepStmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnections();
        }
    }

    private void updateImageAndImageName(Integer storyId, Byte[] image, String imageName) {
        try {
            connection = DBManager.getConnection();
            String sql = "UPDATE `ripdb`.`images` SET `imageData` = ?, `imageName` = ? WHERE `storyId` = ?;";
            prepStmt = connection.prepareStatement(sql);
            prepStmt.setBytes(1, ArrayUtils.toPrimitive(image));
            prepStmt.setString(2, imageName);
            prepStmt.setInt(3, storyId);
            prepStmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnections();
        }
    }

    private void deleteImage(Integer storyId) {
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("DELETE FROM `ripdb`.`images` WHERE `storyId`=?;");
            prepStmt.setInt(1, storyId);
            prepStmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnections();
        }
    }

    @Override
    public List<Story> searchForStories(String searchValue) {
        List<Story> storySearchResults = null;
        try {
            storySearchResults = new ArrayList<>();
            List<Integer> storyIds = new ArrayList<>();
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT storyId FROM stories WHERE (title LIKE ? OR blurb LIKE ?) AND submitted = 'T' AND approved = 'T';");
            prepStmt.setString(1, "%"+searchValue+"%");
            prepStmt.setString(2, "%"+searchValue+"%");
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                storyIds.add(rs.getInt("storyId"));
            }
            
            for (Integer storyId : storyIds) {
                storySearchResults.add(getStory(storyId));
            }
            
            if (storySearchResults.isEmpty()) {
                return null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            closeConnections();
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
                submittedStories.add(story);
            }

            for (Story story : submittedStories) {
                story.setImage(getImageById(story.getId()));
                story.setImageName(getImageNameById(story.getId()));
                story.setGenreIds(getStoryGenres(story.getId()));
            }
            
            if (submittedStories.isEmpty()) {
                return null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            closeConnections();
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
                draftedStories.add(story);
            }

            for (Story story : draftedStories) {
                story.setImage(getImageById(story.getId()));
                story.setImageName(getImageNameById(story.getId()));
                story.setGenreIds(getStoryGenres(story.getId()));
            }
            
            if (draftedStories.isEmpty()) {
                return null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            closeConnections();
        }
        return draftedStories;
    }
    
    private void closeConnections() {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (prepStmt != null) {
            try {
                prepStmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
