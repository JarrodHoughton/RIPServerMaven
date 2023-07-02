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
 * @author Kylynn van der Merwe
 */
public class RatingDao_Impl implements RatingDao_Interface {

    private Connection connection;
    private PreparedStatement prepStmt;
    private ResultSet rs;

    public RatingDao_Impl() {
    }

    @Override
    public List<Rating> getAllRatings() {
        List<Rating> ratings = null;
        Rating rating;

        try {
            ratings = new ArrayList<>();
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT * FROM ratings;");
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                rating = new Rating();
                rating.setId(rs.getInt("ratingId"));
                rating.setDate(rs.getTimestamp("ratingDate").toLocalDateTime());
                rating.setValue(rs.getInt("ratingValue"));
                rating.setReaderId(rs.getInt("accountId"));
                rating.setStoryId(rs.getInt("storyId"));
                ratings.add(rating);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RatingDao_Impl.class.getName()).log(Level.SEVERE, "Failed to get all ratings", ex);
        } finally {
            closeConnections();
        }

        if (ratings != null && ratings.isEmpty()) {
            return null;
        }

        return ratings;
    }

    @Override
    public List<Rating> getRatingsByReaderId(Integer accountId) {
        List<Rating> ratings = null;
        Rating rating;

        try {
            ratings = new ArrayList<>();
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT * FROM ratings WHERE accountId = ?;");
            prepStmt.setInt(1, accountId);
            rs = prepStmt.executeQuery();
            while (rs != null) {
                rating = new Rating();
                rating.setId(rs.getInt("ratingId"));
                rating.setDate(rs.getTimestamp("ratingDate").toLocalDateTime());
                rating.setValue(rs.getInt("ratingValue"));
                rating.setReaderId(rs.getInt("accountId"));
                rating.setStoryId(rs.getInt("storyId"));
                ratings.add(rating);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RatingDao_Impl.class.getName()).log(Level.SEVERE, "Failed to get ratings by readerId", ex);
            return null;
        } finally {
            closeConnections();
        }

        if (ratings != null && ratings.isEmpty()) {
            return null;
        }

        return ratings;
    }

    @Override
    public List<Rating> getRatingsByStory(Integer storyId) {
        List<Rating> ratings = null;
        Rating rating;

        try {
            ratings = new ArrayList<>();
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT * FROM ratings WHERE storyId = ?;");
            prepStmt.setInt(1, storyId);
            rs = prepStmt.executeQuery();
            while (rs != null) {
                rating = new Rating();
                rating.setId(rs.getInt("ratingId"));
                rating.setDate(rs.getTimestamp("ratingDate").toLocalDateTime());
                rating.setValue(rs.getInt("ratingValue"));
                rating.setReaderId(rs.getInt("accountId"));
                rating.setStoryId(rs.getInt("storyId"));
                ratings.add(rating);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RatingDao_Impl.class.getName()).log(Level.SEVERE, "Failed to get ratings by storyId", ex);
            return null;
        } finally {
            closeConnections();
        }

        if (ratings != null && ratings.isEmpty()) {
            return null;
        }

        return ratings;
    }

    @Override
    public Boolean addRating(Rating rating) {
        Boolean added = false;
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("INSERT into ratings (accountId, storyId, ratingValue) values (?,?,?);");
            prepStmt.setInt(1, rating.getReaderId());
            prepStmt.setInt(2, rating.getStoryId());
            prepStmt.setInt(3, rating.getValue());
            added = prepStmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(RatingDao_Impl.class.getName()).log(Level.SEVERE, "Failed to add rating", ex);
            return false;
        } finally {
            closeConnections();
        }

        if (added) {
            added = added && updateRatingValueForStory(rating.getStoryId());
        }

        return added;
    }

    @Override
    public Integer getRatingValue(Integer storyId) {
        Integer integer = null;
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT AVG(ratingValue) as ratingAverage FROM ratings WHERE storyId = ?;");
            prepStmt.setInt(1, storyId);
            rs = prepStmt.executeQuery();

            if (rs.next()) {
                integer = rs.getInt(1);
            }

        } catch (SQLException ex) {
            Logger.getLogger(RatingDao_Impl.class.getName()).log(Level.SEVERE, "Failed to get rating value", ex);
            return null;
        } finally {
            closeConnections();
        }

        return integer;
    }

    private Boolean updateRatingValueForStory(Integer storyId) {
        String sql = "UPDATE stories SET rating = (SELECT AVG(ratingValue) FROM ratings WHERE storyId = ?) WHERE storyId = ?;";
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement(sql);
            prepStmt.setInt(1, storyId);
            prepStmt.setInt(2, storyId);
            return prepStmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(RatingDao_Impl.class.getName()).log(Level.SEVERE, "Failed to update average rating", ex);
            return false;
        } finally {
            closeConnections();
        }
    }

    @Override
    public List<Integer> getTopHighestRatedStoriesInTimePeriod(Timestamp startDate, Timestamp endDate, Integer numberOfEntries) {
        ArrayList<Integer> storyIds = null;
        try {
            storyIds = new ArrayList<>();
            String sql = "SELECT storyId, AVG(ratingValue) AS average_rating FROM ratings WHERE ratingDate BETWEEN ? AND ? GROUP BY storyId ORDER BY average_rating DESC LIMIT ?;";
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement(sql);
            prepStmt.setTimestamp(1, startDate);
            prepStmt.setTimestamp(2, endDate);
            prepStmt.setInt(3, numberOfEntries);
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                storyIds.add(rs.getInt("storyId"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RatingDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            closeConnections();
        }

        if (storyIds != null && storyIds.isEmpty()) {
            return null;
        }
        
        return storyIds;
    }

    @Override
    public Boolean checkRatingExists(Rating rating) {
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT COUNT(ratingId) FROM ratings WHERE accountId = ? AND storyId = ?;");
            prepStmt.setInt(1, rating.getReaderId());
            prepStmt.setInt(2, rating.getStoryId());
            rs = prepStmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            Logger.getLogger(RatingDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnections();
        }

        return false;
    }

    @Override
    public Boolean updateRatingValue(Rating rating) {
        Boolean updated = false;
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("UPDATE ratings SET ratingValue = ?, ratingDate = CURRENT_TIMESTAMP WHERE accountId = ? AND storyId = ?;");
            prepStmt.setInt(1, rating.getValue());
            prepStmt.setInt(2, rating.getReaderId());
            prepStmt.setInt(3, rating.getStoryId());
            updated = prepStmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(RatingDao_Impl.class.getName()).log(Level.SEVERE, "Failed to edit rating", ex);
            return false;
        } finally {
            closeConnections();
        }
        
        if (updated) {
            updated = updated && updateRatingValueForStory(rating.getStoryId());
        }
        
        return updated;
    }

    @Override
    public Rating getRating(Integer accountId, Integer storyId) {
        Rating rating = null;

        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT * FROM ratings WHERE storyId = ? AND accountId = ?;");
            prepStmt.setInt(1, storyId);
            prepStmt.setInt(2, accountId);
            rs = prepStmt.executeQuery();
            if (rs.next()) {
                rating = new Rating();
                rating.setId(rs.getInt("ratingId"));
                rating.setDate(rs.getTimestamp("ratingDate").toLocalDateTime());
                rating.setReaderId(rs.getInt("accountId"));
                rating.setStoryId(rs.getInt("storyId"));
                rating.setValue(rs.getInt("ratingValue"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RatingDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            closeConnections();
        }

        return rating;
    }

    @Override
    public Double getAverageRatingOfAStoryInATimePeriod(Integer storyId, Timestamp startDate, Timestamp endDate) {
        Double averageRating = 0.0;

        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT AVG(ratingValue) AS average_rating FROM ratings WHERE storyId = ? AND ratingDate BETWEEN ? AND ?;");
            prepStmt.setInt(1, storyId);
            prepStmt.setTimestamp(2, startDate);
            prepStmt.setTimestamp(3, endDate);
            rs = prepStmt.executeQuery();

            if (rs.next()) {
                averageRating = rs.getDouble("average_rating");
            }
        } catch (SQLException ex) {
            Logger.getLogger(RatingDao_Impl.class.getName()).log(Level.SEVERE, "Failed to get average rating within time period", ex);
            return 0.0;
        } finally {
            closeConnections();
        }

        return averageRating;
    }

    private void closeConnections() {
        if (rs != null) {
            try {
                rs.close();
                rs = null;
            } catch (SQLException ex) {
                Logger.getLogger(RatingDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (prepStmt != null) {
            try {
                prepStmt.close();
                prepStmt = null;
            } catch (SQLException ex) {
                Logger.getLogger(RatingDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException ex) {
                Logger.getLogger(RatingDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
