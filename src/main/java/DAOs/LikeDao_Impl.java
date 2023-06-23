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
        Like like = new Like();
        
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT * FROM likes;");
            rs = prepStmt.executeQuery();
            
            while (rs.next()) {                
                like.setId(rs.getInt("likeId"));
                like.setDate(rs.getTimestamp("likeDate").toLocalDateTime());
                like.setReaderId(rs.getInt("accountId"));
                like.setStoryId(rs.getInt("storyId"));
                likes.add(like);
            }
        } catch (SQLException ex) {
            Logger.getLogger(LikeDao_Impl.class.getName()).log(Level.SEVERE, "Failed to get all likes", ex);
            return null;
        } finally {
            closeConnections();
        }

        return likes;
    }

    @Override
    public List<Like> getLikesByReaderId(Integer accountId) {
        List<Like> likes = new ArrayList<>();
        Like like = new Like();
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT * FROM likes WHERE accountId = ?;");
            prepStmt.setInt(1, accountId);
            rs = prepStmt.executeQuery();

            while (rs.next()) {
                like.setId(rs.getInt("likeId"));
                like.setDate(rs.getTimestamp("likeDate").toLocalDateTime());
                like.setReaderId(rs.getInt("accountId"));
                like.setStoryId(rs.getInt("storyId"));
                likes.add(like);
            }
        } catch (SQLException ex) {
            Logger.getLogger(LikeDao_Impl.class.getName()).log(Level.SEVERE, "Failed to get likes by readerId", ex);
            return null;
        } finally {
            closeConnections();
        }

        return likes;
    }

    @Override
    public List<Like> getLikesByStory(Integer storyId) {
        List<Like> likes = new ArrayList<>();
        Like like = new Like();
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT * FROM likes WHERE storyId = ?;");
            prepStmt.setInt(1, storyId);
            rs = prepStmt.executeQuery();

            while (rs.next()) {
                like.setId(rs.getInt("likeId"));
                like.setDate(rs.getTimestamp("likeDate").toLocalDateTime());
                like.setReaderId(rs.getInt("accountId"));
                like.setStoryId(rs.getInt("storyId"));
                likes.add(like);
            }
        } catch (SQLException ex) {
            Logger.getLogger(LikeDao_Impl.class.getName()).log(Level.SEVERE, "Failed to get likes by storyId", ex);
            return null;
        } finally {
            closeConnections();
        }

        return likes;
    }

    @Override
    public Integer getStoryLikesByDate(Integer storyId, Timestamp startDate, Timestamp endDate) {
        Integer count = null;

        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT COUNT(storyId) AS likeCount FROM likes WHERE likeDate BETWEEN ? AND ? AND storyId = ?;");
            prepStmt.setTimestamp(1, startDate);
            prepStmt.setTimestamp(2, endDate);
            prepStmt.setInt(3, storyId);
            rs = prepStmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt("likeCount");
            }
        } catch (SQLException ex) {
            Logger.getLogger(LikeDao_Impl.class.getName()).log(Level.SEVERE, "Failed to get story likes by date", ex);
            return 0;
        } finally {
            closeConnections();
        }

        return count;
    }

    @Override
    public Boolean addLike(Like like) {
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("INSERT INTO likes (accountId, storyId) VALUES (?, ?);");
            prepStmt.setInt(1, like.getReaderId());
            prepStmt.setInt(2, like.getStoryId());
            prepStmt.executeUpdate();
            
            prepStmt = connection.prepareStatement("UPDATE stories SET likeCount = likeCount + 1 WHERE storyId = ?;");
            prepStmt.setInt(1, like.getStoryId());
            prepStmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(LikeDao_Impl.class.getName()).log(Level.SEVERE, "Failed to add like", ex);
            return false;
        } finally {
            closeConnections();
        }
        
        return true;
    }

    @Override
    public Boolean deleteLike(Like like) {
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("DELETE FROM likes WHERE accountId = ? AND storyId = ?;");
            prepStmt.setInt(1, like.getReaderId());
            prepStmt.setInt(2, like.getStoryId());
            prepStmt.executeUpdate();
            
            prepStmt = connection.prepareStatement("UPDATE stories SET likeCount = likeCount - 1 WHERE storyId = ?;");
            prepStmt.setInt(1, like.getStoryId());
            prepStmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(LikeDao_Impl.class.getName()).log(Level.SEVERE, "Failed to delete like", ex);
            return false;
        } finally {
            closeConnections();
        }

        return true;
    }

    private Boolean searchForLike(Integer readerId, Integer storyId) {
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT * FROM likes WHERE accountId = ? AND storyId = ?;");
            prepStmt.setInt(1, readerId);
            prepStmt.setInt(2, storyId);
            rs = prepStmt.executeQuery();

            return rs.next();
        } catch (SQLException ex) {
            Logger.getLogger(LikeDao_Impl.class.getName()).log(Level.SEVERE, "Failed to search for like", ex);
            return false;
        } finally {
            closeConnections();
        }
    }

    @Override
    public List<Integer> getMostLikedBooks(Integer numberOfBooks, Timestamp startDate, Timestamp endDate) {
        List<Integer> mostLikedBooks = new ArrayList<>();

        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT storyId, COUNT(*) AS likeCount FROM likes WHERE likeDate >= ? AND likeDate <= ? GROUP BY storyId HAVING COUNT(*) >= 1 ORDER BY likeCount DESC LIMIT ?;");
            prepStmt.setTimestamp(1, startDate);
            prepStmt.setTimestamp(2, endDate);
            prepStmt.setInt(3, numberOfBooks);
            rs = prepStmt.executeQuery();

            while (rs.next()) {
                mostLikedBooks.add(rs.getInt("storyId"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(LikeDao_Impl.class.getName()).log(Level.SEVERE, "Failed to get most liked books", ex);
            return null;
        } finally {
            closeConnections();
        }

        return mostLikedBooks;
    }

    private void closeConnections() {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(LikeDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (prepStmt != null) {
            try {
                prepStmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(LikeDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(LikeDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @Override
    public List<Integer> getMostLikedStoriesByGenreAndTime(Integer genreId, Integer numberOfStories, String startDate, String endDate) {
        List<Integer> storyIds = new ArrayList<>();

        try {
            String query = "SELECT s.storyId FROM ripdb.stories s JOIN ripdb.stories_genres sg ON s.storyId = sg.storyId WHERE sg.genreId = ? AND s.likeDate >= ? AND s.likeDate <= ? ORDER BY s.likeCount DESC LIMIT ?;";
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement(query);
            prepStmt.setInt(1, genreId);
            prepStmt.setString(2, startDate);
            prepStmt.setString(3, endDate);
            prepStmt.setInt(4, numberOfStories);
            rs = prepStmt.executeQuery();

            while (rs.next()) {
                int storyId = rs.getInt("storyId");
                storyIds.add(storyId);
            }
        } catch (SQLException ex) {
            Logger.getLogger(LikeDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            closeConnections();
        }

        return storyIds;
    }
    
    @Override
    public Boolean checkIfLikeExists(Like like) {
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT COUNT(*) AS likeCount FROM likes WHERE accountId = ? AND storyId = ?;");
            prepStmt.setInt(1, like.getReaderId());
            prepStmt.setInt(2, like.getStoryId());
            rs = prepStmt.executeQuery();

            if (rs.next()) {
                int likeCount = rs.getInt("likeCount");
                return likeCount > 0;
            }
        } catch (SQLException ex) {
            Logger.getLogger(LikeDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnections();
        }

        return false;
    }
}

