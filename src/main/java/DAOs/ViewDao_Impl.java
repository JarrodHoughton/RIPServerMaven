package DAOs;

import Utils.DBManager;
import Models.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ViewDao_Impl implements ViewDao_Interface {

    private Connection connection;
    private Logger logger;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @Override
    public Boolean addView(View view) {
        Boolean added = false;
        String insertSql = "INSERT INTO views (accountId, storyId) VALUES (?, ?)";

        try {
            connection = DBManager.getConnection();
            preparedStatement = connection.prepareStatement(insertSql);
            preparedStatement.setInt(1, view.getReaderId());
            preparedStatement.setInt(2, view.getStoryId());
            added = preparedStatement.executeUpdate() >= 0;
        } catch (SQLException ex) {
            Logger.getLogger(ViewDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            closeStatements();
        }
        
        added = added && updateStoryViewCount(view.getStoryId());
        
        return added;
    }
    
    private Boolean updateStoryViewCount(Integer storyId) {
        Boolean added = false;
        String updateSql = "UPDATE stories SET viewCount = viewCount + 1 WHERE storyId = ?";
        try {
            connection = DBManager.getConnection();
            preparedStatement = connection.prepareStatement(updateSql);
            preparedStatement.setInt(1, storyId);
            added = preparedStatement.executeUpdate() >= 0;
        } catch (SQLException ex) {
            Logger.getLogger(ViewDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            closeStatements();
        }
        return added;
    }

    @Override
    public List<Integer> getMostViewedStoriesInATimePeriod(Timestamp startDate, Timestamp endDate, Integer numberOfEntries) {
        List<Integer> storyIds = null;
        String sql = "SELECT storyId, COUNT(viewId) AS view_count FROM views WHERE viewDate BETWEEN ? AND ? GROUP BY storyId ORDER BY view_count DESC LIMIT ?";

        try {
            storyIds = new ArrayList<>();
            connection = DBManager.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setTimestamp(1, startDate);
            preparedStatement.setTimestamp(2, endDate);
            preparedStatement.setInt(3, numberOfEntries);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int storyId = resultSet.getInt("storyId");
                storyIds.add(storyId);
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error occurred while retrieving most viewed stories", ex);
            return null;
        } finally {
            closeStatements();
        }

        if (storyIds != null && storyIds.isEmpty()) {
            return null;
        }

        return storyIds;
    }

    @Override
    public List<View> getTheViewsOnAStoryInATimePeriod(Integer storyId, Timestamp startDate, Timestamp endDate) {
        List<View> views = new ArrayList<>();

        try {
            String query = "SELECT viewId, viewDate, accountId, storyId FROM views WHERE storyId = ? AND viewDate BETWEEN ? AND ?";
            connection = DBManager.getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, storyId);
            preparedStatement.setTimestamp(2, startDate);
            preparedStatement.setTimestamp(3, endDate);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Integer viewId = resultSet.getInt("viewId");
                Timestamp viewDate = resultSet.getTimestamp("viewDate");
                Integer accountId = resultSet.getInt("accountId");
                Integer theStoryId = resultSet.getInt("storyId");
                View view = new View(viewId, viewDate.toLocalDateTime(), accountId, theStoryId);
                views.add(view);
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error occurred while retrieving views", ex);
            return null;
        } finally {
            closeStatements();
        }

        if (views != null && views.isEmpty()) {
            return null;
        }

        return views;
    }

    @Override
    public Boolean isViewAlreadyAdded(View view) {
        try {
            String query = "SELECT COUNT(viewId) AS count FROM views WHERE accountId = ? AND storyId = ?";
            connection = DBManager.getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, view.getReaderId());
            preparedStatement.setInt(2, view.getStoryId());

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("count") > 0;
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error occurred while checking if the view is already added", ex);
            return false;
        } finally {
            closeStatements();
        }
        return false;
    }

    private void closeStatements() {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException ex) {
                Logger.getLogger(ViewDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException ex) {
                Logger.getLogger(ViewDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(ViewDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
