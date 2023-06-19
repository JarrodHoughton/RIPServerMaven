/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import Models.Genre;
import Models.Story;
import Utils.DBManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jarro
 */
public class StoryDao_Impl implements StoryDao_Interface{
    private Connection connection;
    private PreparedStatement prepStmt;
    private ResultSet rs;

    public StoryDao_Impl() {}
    
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
                story.setIsApproved(rs.getString("approved").charAt(0)=='T');
                story.setIsSubmitted(rs.getString("submitted").charAt(0)=='T');
                story.setCommentsEnabled(rs.getString("commentsEnabled").charAt(0)=='T');
                story.setImage(getImageById(storyId));
                story.setImageName(getImageNameById(storyId));
            }
            
            List<Integer> genreIds = new ArrayList<>();
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
    
    private byte[] getImageById(int storyId) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        byte[] data = null;
        try {
            String query = "SELECT * FROM images WHERE id = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, storyId);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                data = resultSet.getBytes("image");
                return data;
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
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String imageName = null;
        try {
            String query = "SELECT * FROM images WHERE id = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, storyId);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                imageName = resultSet.getString("imageName");
                return imageName;
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
        List<Story> stories = new ArrayList<>();
        try {
            Story story = new Story();
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT * FROM stories;");
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                story.setId(rs.getInt("storyId"));
                story.setTitle(rs.getString("title"));
                story.setBlurb(rs.getString("blurb"));
                story.setContent(rs.getString("content"));
                story.setAuthorId(rs.getInt("accountId"));
                story.setLikeCount(rs.getInt("likeCount"));
                story.setViewCount(rs.getInt("viewCount"));
                story.setRating(rs.getDouble("rating"));
                story.setIsApproved(rs.getString("approved").charAt(0)=='T');
                story.setIsSubmitted(rs.getString("submitted").charAt(0)=='T');
                story.setCommentsEnabled(rs.getString("commentsEnabled").charAt(0)=='T');
                story.setImage(getImageById(rs.getInt("storyId")));
                story.setImageName(getImageNameById(rs.getInt("storyId")));
                stories.add(story);
            }
            story.setGenreIds(getStoryGenres(story.getId()));
        } catch (SQLException ex) {
            Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            closeConnections();
        }
        return stories;
    }

    @Override
    public List<Story> getStoriesInGenre(Integer genreId) {
        List<Story> stories = new ArrayList<>();
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT storyId FROM stories_genres WHERE genreId=?;");
            prepStmt.setInt(1, genreId);
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                stories.add(getStory(rs.getInt(1)));
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
            prepStmt = connection.prepareStatement("UPDATE `ripdb`.`accounts` SET `title`=?, `blurb`=?, `approved`=?, `submitted`=?, `commentsEnabled`=?, `content`=? `accountId`=? `viewCount`=? `rating`=? `likeCount`=? storyId`=?;");
            prepStmt.setString(1, story.getTitle());
            prepStmt.setString(2, story.getBlurb());
            prepStmt.setString(3, story.getIsApproved() ? "T" : "F");
            prepStmt.setString(4, story.getIsSubmitted()? "T" : "F");
            prepStmt.setString(5, story.getCommentsEnabled()? "T" : "F");
            prepStmt.setString(6, story.getContent());
            prepStmt.setInt(7, story.getAuthorId());
            prepStmt.setInt(8, story.getViewCount());
            prepStmt.setInt(9, story.getLikeCount());
            prepStmt.setDouble(10, story.getRating());
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
                    "INSERT IGNORE INTO `ripdb`.`stories` (`title`, `blurb`, `approved`, `submitted`, `commentsEnabled`, `content`, `accountId`, `viewCount`, `likeCount`, `rating`) "
                            + "VALUES (?,?,?,?,?,?,?,?,?,?);"
            );
            prepStmt.setString(1, story.getTitle());
            prepStmt.setString(2, story.getBlurb());
            prepStmt.setString(3, story.getIsApproved() ? "T" : "F");
            prepStmt.setString(4, story.getIsSubmitted()? "T" : "F");
            prepStmt.setString(5, story.getCommentsEnabled()? "T" : "F");
            prepStmt.setString(6, story.getContent());
            prepStmt.setInt(7, story.getAuthorId());
            prepStmt.setInt(8, story.getViewCount());
            prepStmt.setInt(9, story.getLikeCount());
            prepStmt.setDouble(10, story.getRating());
            prepStmt.executeUpdate();
            addImageData(story.getImage(),story.getImageName());
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
            prepStmt = connection.prepareStatement("INSERT INTO stories_genres(storyId, genreId) VALUES(?, ?)");
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
        for (Integer genreId:story.getGenreIds()) {
            if (!addStoryGenre(story.getId(), genreId)) {
                added = false;
            }
        }
        return added;
    }
    
    private Boolean deleteStoryGenres(Integer storyId) {
        Boolean deleted = false;
        try {
            prepStmt = connection.prepareStatement("DELETE FROM stories_genres WHERE storyId=?");
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
    
    private void addImageData(byte[] image, String imageName) {
        String sql = "INSERT INTO images (image, imageName) VALUES (?, ?)";
        try {
            prepStmt = connection.prepareStatement(sql);
            prepStmt.setBytes(1, image);
            prepStmt.setString(2, imageName);
            prepStmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnections();
        }
    }
    
    
    
    
    
    
    private void updateImageAndImageName(Integer storyId, byte[] image, String imageName) {
        try {
            String sql ="UPDATE `ripdb`.`images` SET `image` = ?, `imageName` = ? WHERE `storyId` = ?";
            prepStmt = connection.prepareStatement(sql);
            prepStmt.setBytes(1, image);
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
    
    private void closeConnections() {
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
            
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
    }
}
