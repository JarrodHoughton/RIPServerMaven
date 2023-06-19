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
import Models.Comment;

/**
 *
 * @author 27713
 */
public class CommentDao_Impl implements CommentDao_Interface{
    private Connection connection;
    private PreparedStatement prepStmt;
    private ResultSet rs;

    public CommentDao_Impl() {
    }
    
    @Override
    public List<Comment> getAllCommentForStory(Integer storyId) {
        List<Comment> comments = new ArrayList<>();
        Comment com;
        
        try { 
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT * FROM comments WHERE storyId = ?");
            prepStmt.setInt(1, storyId);
            rs = prepStmt.executeQuery();
            if(rs.next()){
                com = new Comment(
                        rs.getInt(1),
                        rs.getTimestamp(2).toLocalDateTime(),
                        rs.getInt(3),
                        rs.getInt(4),
                        rs.getString(5)
                );
                comments.add(com);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommentDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            closeConnections();
        }
        
        return comments;
    }
    
    @Override
    public Boolean addComment(String message, Integer accountId, Integer storyId) {
        
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("INSERT INTO comments(commentMessage, accountId, storyId) VALUES(?,?,?);");
            prepStmt.setString(1, message);
            prepStmt.setInt(2, accountId);
            prepStmt.setInt(3, storyId);
            prepStmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CommentDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }finally{
            closeConnections();
        }
        
        return true;
    }
    
    private void closeConnections(){
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

    
    
}
