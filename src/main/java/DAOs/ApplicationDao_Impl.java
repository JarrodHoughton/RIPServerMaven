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
 * @author Jarrod
 */
public class ApplicationDao_Impl implements ApplicationDao_Interface {

    private Connection connection;
    private PreparedStatement prepStmt;
    private ResultSet rs;

    public ApplicationDao_Impl() {
    }

    @Override
    public List<Application> getApplications() {
        List<Application> applications = null;
        try {
            applications = new ArrayList<>();
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("Select * FROM applications;");
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                applications.add(
                        new Application(
                                rs.getInt("accountId"),
                                rs.getString("motivation")
                        )
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(ApplicationDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            closeConnections();
        }
        
        if (applications == null || applications.isEmpty()) {
            return null;
        }
        
        for (Application app : applications) {
            try {
                connection = DBManager.getConnection();
                prepStmt = connection.prepareStatement("Select accountName, accountSurname FROM accounts WHERE accountId=?;");
                prepStmt.setInt(1, app.getReaderId());
                rs = prepStmt.executeQuery();
                if (rs.next()) {
                    app.setReaderName(rs.getString("accountName"));
                    app.setReaderSurname(rs.getString("accountSurname"));
                }
            } catch (SQLException ex) {
                Logger.getLogger(ApplicationDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            } finally {
                closeConnections();
            }
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
    
    @Override
    public Boolean deleteApplications(List<Integer> accountIds) {
        Boolean deleted = false;
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("DELETE FROM applications WHERE accountId=?;");
            for (Integer accountId : accountIds) {
                prepStmt.setInt(1, accountId);
                prepStmt.addBatch();
            }
            deleted = prepStmt.executeBatch()[0] >= 0;
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
                rs = null;
            } catch (SQLException ex) {
                Logger.getLogger(ApplicationDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (prepStmt != null) {
            try {
                prepStmt.close();
                prepStmt = null;
            } catch (SQLException ex) {
                Logger.getLogger(ApplicationDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException ex) {
                Logger.getLogger(ApplicationDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
