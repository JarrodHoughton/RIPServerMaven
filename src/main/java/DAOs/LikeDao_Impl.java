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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import Models.Like;

/**
 *
 * @author 27713
 */
public class LikeDao_Impl implements LikeDao_Interface {
    private Connection connection;
    private PreparedStatement prepStmt;
    private ResultSet rs;

    public LikeDao_Impl() {
    }

    @Override
    public List<Like> getAllLikes() {
        List<Like> likes = new ArrayList<>();
        Like like;
        
        try {           
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT * FROM likes");
            rs = prepStmt.executeQuery();
            if(rs.next()){
                like = new Like(
                        rs.getInt(1),
                        rs.getTimestamp(2).toLocalDateTime(),
                        rs.getInt(3),
                        rs.getInt(4)
                );
                likes.add(like);
            }
        } catch (SQLException ex) {
            Logger.getLogger(LikeDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            
            if(prepStmt!=null){
                try {
                    prepStmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LikeDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(connection!=null){
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LikeDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(rs!=null){
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LikeDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        return likes;
    }

    @Override
    public List<Like> getLikesByReaderId(Integer accountId) {
        List<Like> likes = new ArrayList<>();
        Like like;
        
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT * FROM likes WHERE accountId = ?");
            prepStmt.setInt(1, accountId);
            rs = prepStmt.executeQuery();
            if(rs!=null){
                like = new Like(
                        rs.getInt(1),
                        rs.getTimestamp(2).toLocalDateTime(),
                        rs.getInt(3),
                        rs.getInt(4)
                );
                likes.add(like);
            }
        } catch (SQLException ex) {
            Logger.getLogger(LikeDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            
            if(prepStmt!=null){
                try {
                    prepStmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LikeDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(connection!=null){
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LikeDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(rs!=null){
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LikeDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        return likes;
    }

    @Override
    public List<Like> getLikesByStory(Integer storyId) {
        List<Like> likes = new ArrayList<>();
        Like like;
        
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT * FROM likes WHERE storyId = ?");
            prepStmt.setInt(1, storyId);
            rs = prepStmt.executeQuery();
            if(rs!=null){
                like = new Like(
                        rs.getInt(1),
                        rs.getTimestamp(2).toLocalDateTime(),
                        rs.getInt(3),
                        rs.getInt(4)
                );
                likes.add(like);
            }
        } catch (SQLException ex) {
            Logger.getLogger(LikeDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            
            if(prepStmt!=null){
                try {
                    prepStmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LikeDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(connection!=null){
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LikeDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(rs!=null){
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LikeDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        return likes;
    }

    @Override
    public Integer getStoryLikesByDate(Integer storyId, Timestamp startDate, Timestamp endDate) {
        Integer integer = null;
        
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT COUNT(storyId)  from likes WHERE likeDate between ? AND ? AND storyId = ? ");
            prepStmt.setTimestamp(1, startDate);
            prepStmt.setTimestamp(2, endDate);
            prepStmt.setInt(3, storyId);
            rs = prepStmt.executeQuery();
            integer = rs.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(LikeDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            
            if(prepStmt!=null){
                try {
                    prepStmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LikeDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(connection!=null){
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LikeDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(rs!=null){
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LikeDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        return integer;
    }

    @Override
    public boolean addLike(Integer readerId, Integer storyId) {
        
        
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("INSERT IGNORE INTO likes (accountId, storyId) VALUES (?,?)");
            prepStmt.setInt(1, readerId);
            prepStmt.setInt(2, storyId);
            prepStmt.executeUpdate();
            
            prepStmt = connection.prepareStatement("UPDATE stories set likeCount = likeCount + 1 WHERE storyId = ?");
            prepStmt.setInt(1, storyId);
            prepStmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(LikeDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }finally{
            
            if(prepStmt!=null){
                try {
                    prepStmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LikeDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(connection!=null){
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LikeDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(rs!=null){
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LikeDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        return true;
    }

    @Override
    public boolean deleteLike(Integer likeId) {
        
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("DELETE from likes WHERE likeId = ?");
            prepStmt.setInt(1, likeId);
            prepStmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(LikeDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }finally{
            
            if(prepStmt!=null){
                try {
                    prepStmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LikeDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(connection!=null){
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LikeDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(rs!=null){
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LikeDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        return true;
    }
    
    public boolean searchForLike(Integer readerId, Integer storyId){        
        
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT * from likes where accountId = ? AND storyId = ?");
            prepStmt.setInt(1, readerId);
            prepStmt.setInt(2, storyId);
            rs = prepStmt.executeQuery();
            
            if(rs!=null){
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(LikeDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }finally{
            
            if(prepStmt!=null){
                try {
                    prepStmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LikeDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(connection!=null){
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LikeDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(rs!=null){
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LikeDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        return false;
    }
    
}
