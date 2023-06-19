/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import Utils.DBManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import Models.Rating;
import java.sql.Timestamp;

/**
 *
 * author 27713
 */
public class RatingDao_Impl implements RatingDao_Interface {
    private Connection connection;
    private PreparedStatement prepStmt;
    private ResultSet rs;

    public RatingDao_Impl() {
    }

    @Override
    public List<Rating> getAllRatings() {
        List<Rating> ratings = new ArrayList<>();
        Rating rating;
        
        try {           
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT ratingId, ratingDate, accountId, storyId, ratingValue FROM ratings");
            rs = prepStmt.executeQuery();
            if (rs.next()) {
                rating = new Rating(
                        rs.getInt(1),
                        rs.getTimestamp(2).toLocalDateTime(),
                        rs.getInt(3),
                        rs.getInt(4),
                        rs.getInt(5)
                );
                ratings.add(rating);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RatingDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnections();
        }
        
        return ratings;
    }

    @Override
    public List<Rating> getRatingsByReaderId(Integer accountId) {
        List<Rating> ratings = new ArrayList<>();
        Rating rating;
        
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT ratingId, ratingDate, accountId, storyId, ratingValue FROM ratings WHERE accountId = ?");
            prepStmt.setInt(1, accountId);
            rs = prepStmt.executeQuery();
            if (rs != null) {
                rating = new Rating(
                        rs.getInt(1),
                        rs.getTimestamp(2).toLocalDateTime(),
                        rs.getInt(3),
                        rs.getInt(4),
                        rs.getInt(5)
                );
                ratings.add(rating);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RatingDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnections();
        }
        
        return ratings;
    }

    @Override
    public List<Rating> getRatingsByStory(Integer storyId) {
        List<Rating> ratings = new ArrayList<>();
        Rating rating;
        
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT ratingId, ratingDate, accountId, storyId, ratingValue FROM ratings WHERE storyId = ?");
            prepStmt.setInt(1, storyId);
            rs = prepStmt.executeQuery();
            if (rs != null) {
                rating = new Rating(
                        rs.getInt(1),
                        rs.getTimestamp(2).toLocalDateTime(),
                        rs.getInt(3),
                        rs.getInt(4),
                        rs.getInt(5)
                );
                ratings.add(rating);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RatingDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnections();
        }
        
        return ratings;
    }

    @Override
    public boolean addRating(Integer accountId, Integer storyId, Integer ratingValue) {
        
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("INSERT into ratings (accountId, storyId, ratingValue) values (?,?,?)");
            prepStmt.setInt(1, accountId);
            prepStmt.setInt(2, storyId);
            prepStmt.setInt(3, ratingValue);
            prepStmt.executeUpdate();
            updateRatingValueForStories();
        } catch (SQLException ex) {
            Logger.getLogger(RatingDao_Impl.class.getName()).log(Level.SEVERE, "Failed to add rating", ex);
            return false;
        } finally {
            closeConnections();
        }
        return true;
    }

    @Override
    public Integer getRatingValue(Integer storyId) {
        Integer integer = null;
        
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT AVG(ratingValue) as ratingAverage FROM ratings WHERE storyId = ?");
            prepStmt.setInt(1, storyId);
            rs = prepStmt.executeQuery();
            
            if (rs != null) {
                integer = rs.getInt(1);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(RatingDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnections();
        }
        
        return integer;
    }

    private void updateRatingValueForStories() {
        String sql = "UPDATE stories SET rating = (SELECT AVG(ratingValue) FROM ratings WHERE storyId = stories.storyId)";
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement(sql);
            prepStmt.executeUpdate();
           
        } catch (SQLException ex) {
           Logger.getLogger(RatingDao_Impl.class.getName()).log(Level.SEVERE, "Failed to update average rating", ex);
        } finally {
            closeConnections();
        }
    }
    
    private void closeConnections() {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(RatingDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (prepStmt != null) {
            try {
                prepStmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(RatingDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(RatingDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    } 

    @Override
    public List<Integer> getTopHighestRatedStoriesInTimePeriod(Timestamp startDate, Timestamp endDate, Integer numberOfEntries) {
        ArrayList<Integer> storyIds = new ArrayList<>();
        try {
            String sql = "SELECT stroyId, AVG(ratingValue) AS average_rating FROM ratings WHERE ratingDate BETWEEN ? AND ? GROUP BY storyId ORDER BY average_rating DESC LIMIT ?";
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement(sql);             
            prepStmt.setTimestamp(1, startDate);
            prepStmt.setTimestamp(2, endDate);
            prepStmt.setInt(3, numberOfEntries);
            rs = prepStmt.executeQuery();
            while (rs.next()) {                
                Integer storyId = rs.getInt("storyId");
                storyIds.add(storyId);
            }
            return storyIds;
        } catch (SQLException ex) {
            Logger.getLogger(RatingDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return storyIds;
        } finally {
            closeConnections();
        }
    }
}

