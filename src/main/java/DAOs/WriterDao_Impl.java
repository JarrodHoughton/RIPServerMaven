/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import Models.Writer;
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
public class WriterDao_Impl implements WriterDao_Interface {
    private Connection connection;
    private PreparedStatement prepStmt;
    private ResultSet rs;
    
    public WriterDao_Impl() {}
    
    @Override
    public Writer getWriter(Integer writerId) {
        Writer writer = null;
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement(
                    "SELECT accountId, accountName, accountSurname, accountEmail, accountPasswordHash, accountSalt, accountPhoneNumber, accountType FROM accounts WHERE accountId = ?;");
            prepStmt.setInt(1, writerId);
            rs = prepStmt.executeQuery();
            if (rs.next()) {
                writer = new Writer();
                writer.setId(writerId);
                writer.setName(rs.getString(2));
                writer.setEmail(rs.getString(3));
                writer.setPasswordHash(rs.getString(4));
                writer.setSalt(rs.getString(5));
                writer.setPhoneNumber(rs.getString(6));
                writer.setUserType(rs.getString(7));
            }
            
            //getting all the writer's favourite story Ids
            List<Integer> favouriteStoryIds = new ArrayList<>();
            prepStmt = connection.prepareStatement(
                    "SELECT storyId FROM likes WHERE accountId = ?;");
            prepStmt.setInt(1, writerId);
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                favouriteStoryIds.add(rs.getInt(1));
            }
            writer.setFavouriteStoryIds(favouriteStoryIds);
            
            //getting all the writer's favourite genre Ids
            List<Integer> favouriteGenreIds = new ArrayList<>();
            prepStmt = connection.prepareStatement(
                    "SELECT genreId FROM genres_readers WHERE accountId = ?;");
            prepStmt.setInt(1, writerId);
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                favouriteGenreIds.add(rs.getInt(1));
            }
            writer.setFavouriteGenreIds(favouriteGenreIds);
            
            //getting all the writer's submitted and drafted story Ids
            List<Integer> submittedStoryIds = new ArrayList<>();
            List<Integer> draftedStoryIds = new ArrayList<>();
            prepStmt = connection.prepareStatement(
                    "SELECT storyId, submitted FROM stories WHERE accountId = ?;");
            prepStmt.setInt(1, writerId);
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                Integer storyId = rs.getInt(1);
                if (rs.getString(2).charAt(0)=='T') {
                    submittedStoryIds.add(storyId);
                } else {
                    draftedStoryIds.add(storyId);
                }
            }
            writer.setDraftedStoryIds(draftedStoryIds);
            writer.setSubmittedStoryIds(submittedStoryIds);
        } catch (SQLException ex) {
            Logger.getLogger(WriterDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (prepStmt != null) {
                try {
                    prepStmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(WriterDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(WriterDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(WriterDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return writer;
    }

    @Override
    public Writer getWriterByEmail(String email) {
        Writer writer = null;
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement(
                    "SELECT accountId, accountName, accountSurname, accountEmail, accountPasswordHash, accountSalt, accountPhoneNumber, accountType FROM accounts WHERE accountEmail = ?;");
            prepStmt.setString(1, email);
            rs = prepStmt.executeQuery();
            if (rs.next()) {
                writer = new Writer();
                writer.setId(rs.getInt(1));
                writer.setName(rs.getString(2));
                writer.setEmail(rs.getString(3));
                writer.setPasswordHash(rs.getString(4));
                writer.setSalt(rs.getString(5));
                writer.setPhoneNumber(rs.getString(6));
                writer.setUserType(rs.getString(7));
            }
            
            //getting all the writer's favourite story Ids
            List<Integer> favouriteStoryIds = new ArrayList<>();
            prepStmt = connection.prepareStatement(
                    "SELECT storyId FROM likes WHERE accountEmail = ?;");
            prepStmt.setString(1, email);
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                favouriteStoryIds.add(rs.getInt(1));
            }
            writer.setFavouriteStoryIds(favouriteStoryIds);
            
            //getting all the writer's favourite genre Ids
            List<Integer> favouriteGenreIds = new ArrayList<>();
            prepStmt = connection.prepareStatement(
                    "SELECT genreId FROM genres_readers WHERE accountEmail = ?;");
            prepStmt.setString(1, email);
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                favouriteGenreIds.add(rs.getInt(1));
            }
            writer.setFavouriteGenreIds(favouriteGenreIds);
            
            //getting all the writer's submitted and drafted story Ids
            List<Integer> submittedStoryIds = new ArrayList<>();
            List<Integer> draftedStoryIds = new ArrayList<>();
            prepStmt = connection.prepareStatement(
                    "SELECT storyId, submitted FROM stories WHERE accountEmail = ?;");
            prepStmt.setString(1, email);
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                Integer storyId = rs.getInt(1);
                if (rs.getString(2).charAt(0)=='T') {
                    submittedStoryIds.add(storyId);
                } else {
                    draftedStoryIds.add(storyId);
                }
            }
            writer.setDraftedStoryIds(draftedStoryIds);
            writer.setSubmittedStoryIds(submittedStoryIds);
        } catch (SQLException ex) {
            Logger.getLogger(WriterDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (prepStmt != null) {
                try {
                    prepStmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(WriterDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(WriterDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(WriterDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return writer;
    }
    
    @Override
    public List<Writer> getAllWriters() {
        List<Writer> writers = new ArrayList<>();
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement(
                    "SELECT accountId FROM accounts;");
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                writers.add(getWriter(rs.getInt(1)));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(WriterDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (prepStmt != null) {
                try {
                    prepStmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(WriterDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(WriterDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(WriterDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return writers;
    }

    @Override
    public Boolean addWriter(Integer readerId) {
        Boolean added = false;
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("UPDATE accounts SET accountType='W' WHERE accountId=?");
            prepStmt.setInt(1, readerId);
            prepStmt.executeUpdate(); 
        } catch (SQLException ex) {
            Logger.getLogger(WriterDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (prepStmt != null) {
                try {
                    prepStmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(WriterDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(WriterDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(WriterDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return added;
    }

    @Override
    public Boolean deleteWriter(Integer writerId) {
        Boolean deleted = false;
        
        return deleted;    
    }

    @Override
    public Boolean updateWriter(Writer writer) {
        Boolean updated = false;
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement(
                    "UPDATE accounts SET accountName=? accountSurname=? accountEmail=? accountPasswordHash=? accountSalt=? accountPhoneNumber=? accountType=?) " +
                    "WHERE accountId=?;"
            );
            prepStmt.setString(1, writer.getName());
            prepStmt.setString(2, writer.getSurname());
            prepStmt.setString(3, writer.getEmail());
            prepStmt.setString(4, writer.getPasswordHash());
            prepStmt.setString(5, writer.getSalt());
            prepStmt.setString(6, writer.getPhoneNumber());
            prepStmt.setString(7, writer.getUserType());
            prepStmt.setInt(8, writer.getId());
            prepStmt.executeUpdate();
            updated = true;
        } catch (SQLException ex) {
            Logger.getLogger(WriterDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (prepStmt != null) {
                try {
                    prepStmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(WriterDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(WriterDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(WriterDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return updated;
    }
}
