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
            prepStmt = connection.prepareStatement(
                    "SELECT C.commentId, C.commentDate, C.commentMessage, C.accountId, C.storyId, A.accountName, A.accountSurname FROM comments as C \n" +
                    "INNER JOIN accounts as A on A.accountId=C.accountId WHERE C.storyId=? ORDER BY C.commentDate DESC;");
            prepStmt.setInt(1, storyId);
            rs = prepStmt.executeQuery();
            while (rs.next()){
                com = new Comment();
                com.setId(rs.getInt("commentId"));
                com.setDate(rs.getTimestamp("commentDate").toLocalDateTime().toString());
                com.setReaderId(rs.getInt("accountId"));
                com.setStoryId(rs.getInt("storyId"));
                com.setMessage(rs.getString("commentMessage"));
                com.setName(rs.getString("accountName"));
                com.setSurname(rs.getString("accountSurname"));
                comments.add(com);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommentDao_Impl.class.getName()).log(Level.SEVERE, "Failed to get all comments for story", ex);
        }finally{
            closeConnections();
        }
        return comments;
    }

    @Override
    public Comment getCommentById(Integer commentId) {
        Comment comment = null;
        
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT C.commentId, C.commentDate, C.commentMessage, C.accountId, C.storyId, A.accountName, A.accountSurname FROM comments as C \n" +
                    "INNER JOIN accounts as A on A.accountId=C.accountId WHERE C.commentId=?;");
            prepStmt.setInt(1, commentId);
            rs = prepStmt.executeQuery();
            if(rs.next()){
                comment = new Comment();
                comment.setId(rs.getInt("commentId"));
                comment.setDate(rs.getTimestamp("commentDate").toLocalDateTime().toString());
                comment.setReaderId(rs.getInt("accountId"));
                comment.setStoryId(rs.getInt("storyId"));
                comment.setMessage("commentMessage");
                comment.setName(rs.getString("accountName"));
                comment.setSurname(rs.getString("accountSurname"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommentDao_Impl.class.getName()).log(Level.SEVERE, "Failed to get comment by commentId", ex);
            return null;
        }finally{
            closeConnections();
        }
        
        return comment;
    }

    @Override
    public String getCommentMessage(Integer commentId) {
        Comment comment = null;        
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT commentMessage FROM comments WHERE commentId = ?;");
            prepStmt.setInt(1, commentId);
            rs = prepStmt.executeQuery();
            
            if(rs.first()){
                comment = new Comment(rs.getString("commentMessage"));                
            }
            
            return comment.getMessage();
        } catch (SQLException ex) {
            Logger.getLogger(CommentDao_Impl.class.getName()).log(Level.SEVERE, "Failed to get comment message", ex);
            return null;
        }finally{
            closeConnections();
        }
        
    }

    @Override
    public Boolean addComment(Comment comment) {        
        Boolean added = false;        
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("INSERT IGNORE INTO comments (commentMessage, accountId, storyId) VALUES(?,?,?);");
            prepStmt.setString(1, comment.getMessage());
            prepStmt.setInt(2, comment.getReaderId());
            prepStmt.setInt(3, comment.getStoryId());
            prepStmt.executeUpdate();
            added = true;
        } catch (SQLException ex) {
            Logger.getLogger(CommentDao_Impl.class.getName()).log(Level.SEVERE, "Failed to add comment", ex);
            return false;
        }finally{
            closeConnections();
        }
        
        return added;    
    }
    
   private void closeConnections(){
         if(rs!=null){
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EditorDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        if(prepStmt!=null){
                try {
                    prepStmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EditorDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(connection!=null){
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EditorDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

    }
}
    


