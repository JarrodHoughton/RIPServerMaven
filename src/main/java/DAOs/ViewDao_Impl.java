/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import Utils.DBManager;
import Models.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ViewDao_Impl implements ViewDao_Interface {

    private Connection connection;
    private Logger logger;

    @Override
    public Boolean addView(View view) {
        String sql = "INSERT INTO views (accountId, storyId) VALUES (?, ?)";
        String updateSql = "UPDATE stories SET viewCount = viewCount + 1 WHERE storyId = ?";
        PreparedStatement insertStatement = null;
        PreparedStatement updateStatement = null;

        try {
            connection = DBManager.getConnection();
            // Insert a new view
            insertStatement = connection.prepareStatement(sql);
            insertStatement.setInt(1, view.getReaderId());
            insertStatement.setInt(2, view.getStoryId());
            insertStatement.executeUpdate();
            insertStatement.close(); // Close the insertStatement after successful execution

            // Update the view count of the story
            updateStatement = connection.prepareStatement(updateSql);
            updateStatement.setInt(1, view.getStoryId());
            updateStatement.executeUpdate();
            updateStatement.close(); // Close the updateStatement after successful execution

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
        } finally {
            // Close the statements in the finally block to ensure they are always closed
            try {
                if (insertStatement != null && !insertStatement.isClosed()) {
                    insertStatement.close();
                }
                if (updateStatement != null && !updateStatement.isClosed()) {
                    updateStatement.close();
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Failed to close PreparedStatement", e);
            }
        }
    }

    @Override
    public List<Integer> get10MostViewed(LocalDateTime startDate, LocalDateTime endDate) {
        List<Integer> books = new ArrayList<>();
        
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    
}






    

    
    
    

    

