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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import Models.Rating;

/**
 *
 * @author 27713
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
            if(rs.next()){
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
        }finally{
            
            if(prepStmt!=null){
                try {
                    prepStmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RatingDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(connection!=null){
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RatingDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(rs!=null){
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RatingDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
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
            if(rs!=null){
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
        }finally{
            
            if(prepStmt!=null){
                try {
                    prepStmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RatingDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(connection!=null){
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RatingDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(rs!=null){
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RatingDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
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
            if(rs!=null){
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
        }finally{
            
            if(prepStmt!=null){
                try {
                    prepStmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RatingDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(connection!=null){
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RatingDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(rs!=null){
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RatingDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        return ratings;
    }

    
    
}
