/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
    private PreparedStatement insertStatement;
    private PreparedStatement updateStatement;
    private PreparedStatement queryStatement;
    private ResultSet resultSet;

    @Override
    public Boolean addView(View view) {
        String insertSql = "INSERT INTO views (accountId, storyId) VALUES (?, ?)";
        String updateSql = "UPDATE stories SET viewCount = viewCount + 1 WHERE storyId = ?";

        try {
            connection = DBManager.getConnection();
            
            // Insert a new view
            insertStatement = connection.prepareStatement(insertSql);
            insertStatement.setInt(1, view.getReaderId());
            insertStatement.setInt(2, view.getStoryId());
            insertStatement.executeUpdate();
            insertStatement.close();

            // Update the view count of the story
            updateStatement = connection.prepareStatement(updateSql);
            updateStatement.setInt(1, view.getStoryId());
            updateStatement.executeUpdate();
            updateStatement.close();

            return true;
        } catch (SQLException ex) {
            try {
                if (insertStatement != null && !insertStatement.isClosed()) {
                    logger.log(Level.SEVERE, "Failed to insert a new view", ex);
                } else if (updateStatement != null && !updateStatement.isClosed()) {
                    logger.log(Level.SEVERE, "Failed to update the view count", ex);
                } else {
                    logger.log(Level.SEVERE, "Unknown error occurred", ex);
                }
            } catch (SQLException ex1) {
                Logger.getLogger(ViewDao_Impl.class.getName()).log(Level.SEVERE, null, ex1);
            }
            return false;
        }  finally {
            closeStatements();
        }
    }

    @Override
    public List<Integer> getMostViewedStoriesInATimePeriod(Timestamp startDate, Timestamp endDate, Integer numberOfEntries) {
        List<Integer> storyIds = new ArrayList<>();
        String sql = "SELECT storyId, COUNT(*) AS view_count FROM views WHERE viewDate BETWEEN ? AND ? GROUP BY storyId ORDER BY view_count DESC LIMIT ?";

        try {
            connection = DBManager.getConnection();
            queryStatement = connection.prepareStatement(sql);
            queryStatement.setTimestamp(1, startDate);
            queryStatement.setTimestamp(2, endDate);
            queryStatement.setInt(3, numberOfEntries);

            resultSet = queryStatement.executeQuery();

            while (resultSet.next()) {
                int storyId = resultSet.getInt("storyId");
                storyIds.add(storyId);
            }

            return storyIds;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error occurred while retrieving most viewed stories", ex);
            return storyIds;
        } finally {
            closeStatements();
        }
    }
    
     @Override
    public List<View> getTheViewsOnAStoryInATimePeriod(Integer storyId, Timestamp startDate, Timestamp endDate) {
        List<View> views = new ArrayList<>();
        
         try {
              String query = "SELECT viewId, viewDate, accountId, storyId FROM views WHERE storyId = ? AND viewDate BETWEEN ? AND ?";
              connection = DBManager.getConnection();
              queryStatement = connection.prepareStatement(query);
              queryStatement.setInt(1, storyId);
              queryStatement.setTimestamp(2, startDate);
              queryStatement.setTimestamp(3, endDate);
              
              resultSet = queryStatement.executeQuery();
              while (resultSet.next()) {                 
                  Integer viewId = resultSet.getInt("viewId");
                  Timestamp viewDate = resultSet.getTimestamp("viewDate");
                  Integer accountId = resultSet.getInt("accountId");
                  Integer theStoryId = resultSet.getInt("storyId");
                  View view = new View(viewId, viewDate.toLocalDateTime(), accountId, theStoryId);
                  views.add(view);
             }
              
              return views;
              
         } catch (SQLException ex) {
             logger.log(Level.SEVERE, "Error occurred while retrieving views", ex);
             return views;
         } finally {
             closeStatements();
         }
        
    }
    
    private void closeStatements() {
        try {
            if (insertStatement != null && !insertStatement.isClosed()) {
                insertStatement.close();
            }
            if (updateStatement != null && !updateStatement.isClosed()) {
                updateStatement.close();
            }
            if (queryStatement != null && !queryStatement.isClosed()) {
                queryStatement.close();
            }
            if (resultSet != null && !resultSet.isClosed()) {
                resultSet.close();
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to close statements", e);
        }
    }

   
}






    

    
    
    

    

