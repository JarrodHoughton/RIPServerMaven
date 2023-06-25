package DAOs;

import Models.Writer;
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
                    "SELECT * FROM accounts WHERE accountId = ?;");
            prepStmt.setInt(1, writerId);
            rs = prepStmt.executeQuery();
            if (rs.next()) {
                writer = new Writer();
                writer.setId(writerId);
                writer.setName(rs.getString("accountName"));  
                writer.setEmail(rs.getString("accountEmail")); 
                writer.setPasswordHash(rs.getString("accountPasswordHash")); 
                writer.setSalt(rs.getString("accountSalt")); 
                writer.setPhoneNumber(rs.getString("accountPhoneNumber"));
                writer.setUserType(rs.getString("accountType")); 
            }
            
            //getting all the writer's favourite story Ids
            List<Integer> favouriteStoryIds = new ArrayList<>();
            prepStmt = connection.prepareStatement(
                    "SELECT storyId FROM likes WHERE accountId = ?;");
            prepStmt.setInt(1, writerId);
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                favouriteStoryIds.add(rs.getInt("storyId")); 
            }
            writer.setFavouriteStoryIds(favouriteStoryIds);
            
            //getting all the writer's favourite genre Ids
            List<Integer> favouriteGenreIds = new ArrayList<>();
            prepStmt = connection.prepareStatement(
                    "SELECT genreId FROM genres_readers WHERE accountId = ?;");
            prepStmt.setInt(1, writerId);
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                favouriteGenreIds.add(rs.getInt("genreId")); 
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
                Integer storyId = rs.getInt("storyId"); 
                if (rs.getBoolean("submitted")) { 
                    submittedStoryIds.add(storyId);
                } else {
                    draftedStoryIds.add(storyId);
                }
            }
            writer.setDraftedStoryIds(draftedStoryIds);
            writer.setSubmittedStoryIds(submittedStoryIds);
        } catch (SQLException ex) {
            Logger.getLogger(WriterDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            closeConnections();
        }
        return writer;
    }

    @Override
    public Writer getWriterByEmail(String email) {
        Writer writer = null;
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement(
                    "SELECT * FROM accounts WHERE accountEmail = ?;");
            prepStmt.setString(1, email);
            rs = prepStmt.executeQuery();
            if (rs.next()) {
                writer = new Writer();
                writer.setId(rs.getInt("accountId")); 
                writer.setName(rs.getString("accountName")); 
                writer.setSurname(rs.getString("accountSurname")); 
                writer.setEmail(rs.getString("accountEmail")); 
                writer.setPasswordHash(rs.getString("accountPasswordHash")); 
                writer.setSalt(rs.getString("accountSalt"));
                writer.setPhoneNumber(rs.getString("accountPhoneNumber")); 
                writer.setUserType(rs.getString("accountType"));
                writer.setVerified(rs.getString("verified").equals("F") ? Boolean.FALSE : Boolean.TRUE);
            }
            
            //getting all the writer's favourite story Ids
            List<Integer> favouriteStoryIds = new ArrayList<>();
            prepStmt = connection.prepareStatement("SELECT storyId FROM likes WHERE accountId = ?;");
            prepStmt.setInt(1, writer.getId());
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                favouriteStoryIds.add(rs.getInt("storyId")); 
            }
            writer.setFavouriteStoryIds(favouriteStoryIds);
            
            //getting all the writer's favourite genre Ids
            List<Integer> favouriteGenreIds = new ArrayList<>();
            prepStmt = connection.prepareStatement("SELECT genreId FROM genres_readers WHERE accountId = ?;");
            prepStmt.setInt(1, writer.getId());
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                favouriteGenreIds.add(rs.getInt("genreId")); 
            }
            writer.setFavouriteGenreIds(favouriteGenreIds);
            
            //getting all the writer's submitted and drafted story Ids
            List<Integer> submittedStoryIds = new ArrayList<>();
            List<Integer> draftedStoryIds = new ArrayList<>();
            prepStmt = connection.prepareStatement(
                    "SELECT storyId, submitted FROM stories WHERE accountId = ?;");
            prepStmt.setInt(1, writer.getId());
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                Integer storyId = rs.getInt("storyId"); 
                if (rs.getBoolean("submitted")) {
                    submittedStoryIds.add(storyId);
                } else {
                    draftedStoryIds.add(storyId);
                }
            }
            writer.setDraftedStoryIds(draftedStoryIds);
            writer.setSubmittedStoryIds(submittedStoryIds);
        } catch (SQLException ex) {
            Logger.getLogger(WriterDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            closeConnections();
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
                writers.add(getWriter(rs.getInt("accountId")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(WriterDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            closeConnections();
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
            added = true; 
        } catch (SQLException ex) {
            Logger.getLogger(WriterDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            closeConnections();
        }
        return added;
    }

    @Override
    public Boolean deleteWriter(Integer writerId) {
        Boolean deleted = false;
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("DELETE FROM accounts WHERE accountId=?");
            prepStmt.setInt(1, writerId);
            prepStmt.executeUpdate();
            deleted = true; 
        } catch (SQLException ex) {
            Logger.getLogger(WriterDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            closeConnections();
        }
        return deleted;
    }

    @Override
    public Boolean updateWriter(Writer writer) {
        Boolean updated = false;
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement(
                    "UPDATE accounts SET accountName=?, accountSurname=?, accountEmail=?, accountPasswordHash=?, accountSalt=?, accountPhoneNumber=?, accountType=? " +
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
            return false;
        } finally {
            closeConnections();
        }
        return updated;
    }
    
    @Override
    public List<Integer> getTopWriters(Integer numberOfWriters) {
        List<Integer> topWriters = new ArrayList<>();        
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT accountId, SUM(viewCount) AS totalViews " +
                    "FROM stories GROUP BY accountId ORDER BY totalViews DESC LIMIT ?;");
            prepStmt.setInt(1, numberOfWriters);
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                topWriters.add(rs.getInt("accountId")); 
            }
        } catch (SQLException ex) {
            Logger.getLogger(WriterDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            closeConnections();
        }
        return topWriters;
    }
    
    @Override
    public Integer getTotalViewsByWriterId(Integer writerId) {
    Integer totalViews = null;
    try {
        connection = DBManager.getConnection();
        prepStmt = connection.prepareStatement("SELECT COUNT(*) AS totalViews FROM views "
                + "INNER JOIN stories ON views.storyId = stories.storyId "
                + "WHERE stories.writerId = ?");
        prepStmt.setInt(1, writerId);
        rs = prepStmt.executeQuery();

        if (rs.next()) {
            totalViews = rs.getInt("totalViews");
        }
    } catch (SQLException ex) {
        Logger.getLogger(RatingDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
        return null;
    } finally {
        closeConnections();
    }

    return totalViews;
}


    @Override
    public List<Integer> getTopWritersByDate(Integer numberOfWriters, Timestamp startDate, Timestamp endDate) {
        List<Integer> topWriters = new ArrayList<>();        
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT v.accountId, COUNT(*) AS viewCount " +
                    "FROM views AS v INNER JOIN stories AS s ON v.storyId = s.storyId " +
                    "WHERE v.viewDate >= ? AND v.viewDate <= ? " +
                    "GROUP BY v.accountId ORDER BY viewCount DESC LIMIT ?;" );
            prepStmt.setTimestamp(1, startDate);
            prepStmt.setTimestamp(2, endDate);
            prepStmt.setInt(3, numberOfWriters);
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                topWriters.add(rs.getInt("accountId")); 
            }
        } catch (SQLException ex) {
            Logger.getLogger(WriterDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            closeConnections();
        }
        return topWriters;
    }

    private void closeConnections() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (prepStmt != null) {
                prepStmt.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(WriterDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

