package DAOs;

import Models.Application;
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
public class ApplicationDao_Impl implements ApplicationDao_Interface {
    private Connection connection;
    private PreparedStatement prepStmt;
    private ResultSet rs;

    public ApplicationDao_Impl() {}

    @Override
    public List<Application> getApplications() {
        List<Application> applications = new ArrayList<>();
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("Select accountId, motivation FROM `ripdb`.`applications`;");
            prepStmt.executeQuery();
            while (rs.next()) {
                applications.add(
                        new Application(
                                rs.getInt(1),
                                rs.getString(2)
                        )
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(ApplicationDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnections();
        }
        return applications;
    }

    @Override
    public Boolean addApplication(Application application) {
        Boolean added = false;
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("INSERT INTO applications(accountId, motivation) VALUES(?, ?);");
            prepStmt.setInt(1, application.getReaderId());
            prepStmt.setString(2, application.getMotivation());
            prepStmt.executeUpdate();
            added = true;
        } catch (SQLException ex) {
            Logger.getLogger(ApplicationDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            closeConnections();
        }
        return added;
    }

    @Override
    public Boolean deleteApplication(Integer accountId) {
        Boolean deleted = false;
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("DELETE FROM applications WHERE accountId=?;");
            prepStmt.setInt(1, accountId);
            prepStmt.executeUpdate();
            deleted = true;
        } catch (SQLException ex) {
            Logger.getLogger(ApplicationDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            closeConnections();
        }
        return deleted;
    }
    
    private void closeConnections() {
        
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(ApplicationDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if (prepStmt != null) {
            try {
                prepStmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(ApplicationDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(ApplicationDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        

    }
}

