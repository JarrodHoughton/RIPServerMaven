/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import Models.Editor;
import Utils.DBManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jarrod
 */
public class EditorDao_Impl implements EditorDao_Interface {

    private Connection connection;
    private PreparedStatement prepStmt;
    private ResultSet rs;
    
    public EditorDao_Impl() {
    }

    @Override
    public Editor getEditor(Integer id) {
        Editor editor = null;
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement(
                    "SELECT A.accountId, A.accountName, A.accountSurname, A.accountEmail, A.accountPasswordHash, A.accountSalt, A.accountPhoneNumber, A.accountType, E.approvalCount\n"
                    + "FROM accounts as A \n"
                    + "INNER JOIN editorsapprovals as E\n"
                    + "ON A.accountId=E.accountId"
                            + "WHERE A.accountId = ?;");
            prepStmt.setInt(1, id);
            rs = prepStmt.executeQuery();
            if (rs.next()) {
                editor = new Editor();
                editor.setId(rs.getInt("accountId"));
                editor.setName(rs.getString("accountName"));
                editor.setSurname(rs.getString("accountSurname"));
                editor.setEmail(rs.getString("accountEmail"));
                editor.setSalt(rs.getString("accountSalt"));
                editor.setPasswordHash(rs.getString("accountPasswordHash"));
                editor.setUserType(rs.getString("accountType"));
                editor.setPhoneNumber(rs.getString("accountPhoneNumber"));
                editor.setApprovalCount(rs.getInt("approvalCount"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(EditorDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            closeConnections();
        }
        return editor;
    }
    
    @Override
    public Editor getEditorByEmail(String email) {
        Editor editor = null;
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement(
                    "SELECT A.accountId, A.accountName, A.accountSurname, A.accountEmail, A.accountPasswordHash, A.accountSalt, A.accountPhoneNumber, A.accountType, E.approvalCount\n"
                    + "FROM accounts as A \n"
                    + "INNER JOIN editorsapprovals as E\n"
                    + "ON A.accountId=E.accountId\n"
                            + "WHERE A.accountEmail= ?;");
            prepStmt.setString(1, email);
            rs = prepStmt.executeQuery();
            if (rs.next()) {
                editor = new Editor();
                editor.setId(rs.getInt("accountId"));
                editor.setName(rs.getString("accountName"));
                editor.setSurname(rs.getString("accountSurname"));
                editor.setEmail(rs.getString("accountEmail"));
                editor.setSalt(rs.getString("accountSalt"));
                editor.setPasswordHash(rs.getString("accountPasswordHash"));
                editor.setUserType(rs.getString("accountType"));
                editor.setPhoneNumber(rs.getString("accountPhoneNumber"));
                editor.setApprovalCount(rs.getInt("approvalCount"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(EditorDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            closeConnections();
        }
        return editor;
    }

    @Override
    public List<Editor> getAllEditors() {
        List<Editor> editors = new ArrayList<>();
        Editor editor;
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement(
                    "SELECT A.accountId, A.accountName, A.accountSurname, A.accountEmail, A.accountPasswordHash, A.accountSalt, A.accountPhoneNumber, A.accountType, E.approvalCount\n"
                    + "FROM accounts as A \n"
                    + "INNER JOIN editorsapprovals as E\n"
                    + "ON A.accountId=E.accountId;");
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                editor = new Editor();
                editor.setId(rs.getInt("accountId"));
                editor.setName(rs.getString("accountName"));
                editor.setSurname(rs.getString("accountSurname"));
                editor.setEmail(rs.getString("accountEmail"));
                editor.setSalt(rs.getString("accountSalt"));
                editor.setPasswordHash(rs.getString("accountPasswordHash"));
                editor.setPhoneNumber(rs.getString("accountPhoneNumber"));
                editor.setApprovalCount(rs.getInt("approvalCount"));
                editors.add(editor);
            }
        } catch (SQLException ex) {
            Logger.getLogger(EditorDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            closeConnections();
        }
        return editors;
    }

    @Override
    public Boolean updateEditor(Editor editor) {
        Boolean updated = false;
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("UPDATE `ripdb`.`accounts` SET `accountName`=?, `accountSurname`=?, `accountEmail`=?, `accountPasswordHash`=?, `accountPhoneNumber`=?, `accountType`=? WHERE `accountId`=?;");
            prepStmt.setString(1, editor.getName());
            prepStmt.setString(2, editor.getSurname());
            prepStmt.setString(3, editor.getEmail());
            prepStmt.setString(4, editor.getPasswordHash());
            prepStmt.setString(5, editor.getSalt());
            prepStmt.setString(6, editor.getUserType());
            prepStmt.setInt(7, editor.getId());
            prepStmt.executeUpdate();
            prepStmt = connection.prepareStatement("UPDATE `ripdb`.`editorsapprovals` SET `approvalCount`=? WHERE `accountId`=?;");
            prepStmt.setInt(1, editor.getApprovalCount());
            prepStmt.setInt(2, editor.getId());
            prepStmt.executeUpdate();
            updated = true;
        } catch (SQLException ex) {
            Logger.getLogger(EditorDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            closeConnections();
        }
        return updated;
    }

    @Override
    public Boolean deleteEditor(Integer id) {
        Boolean deleted = false;
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("DELETE FROM `ripdb`.`editorsapprovals` WHERE `accountId`=?;");
            prepStmt.setInt(1, id);
            prepStmt.executeUpdate();
            prepStmt = connection.prepareStatement("DELETE FROM `ripdb`.`accounts` WHERE `accountId`=?;");
            prepStmt.setInt(1, id);
            prepStmt.executeUpdate();
            deleted = true;
        } catch (SQLException ex) {
            Logger.getLogger(EditorDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            closeConnections();
        }
        return deleted;
    }

    @Override
    public Boolean addEditor(Editor editor) {
        Boolean added = false;
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("INSERT INTO accounts (accountName, accountSurname, accountEmail, accountPasswordHash, accountSalt, accountPhoneNumber, accountType, verifyToken) VALUES (?, ?, ?, ?, ?, ?, ?, ?);");
            prepStmt.setString(1, editor.getName());
            prepStmt.setString(2, editor.getSurname());
            prepStmt.setString(3, editor.getEmail());
            prepStmt.setString(4, editor.getPasswordHash());
            prepStmt.setString(5, editor.getSalt());
            prepStmt.setString(6, editor.getPhoneNumber());
            prepStmt.setString(7, editor.getUserType());
            prepStmt.setString(8, "");
            prepStmt.executeUpdate();
            prepStmt = connection.prepareStatement("SELECT accountId FROM accounts WHERE accountEmail=?;");
            rs = prepStmt.executeQuery();
            if (rs.next()) {
                prepStmt = connection.prepareStatement("INSERT INTO `ripdb`.`editorsapprovals` (`accountId`, `approvalCount`) VALUES (?, ?);");
                prepStmt.setInt(1, rs.getInt(1));
                prepStmt.setInt(2, editor.getApprovalCount());
                prepStmt.executeUpdate();
            }
            added = true;
        } catch (SQLException ex) {
            Logger.getLogger(EditorDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
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

    @Override
    public List<Integer> getTopEditors(Integer numberOfEditors) {
        List<Integer> topEditors = new ArrayList<>();        
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT accountId, approvalCount FROM editorsapprovals ORDER BY approvalCount DESC LIMIT ?;");
            prepStmt.setInt(1, numberOfEditors);
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                topEditors.add(rs.getInt("accountId")); 
            }
        } catch (SQLException ex) {
            Logger.getLogger(WriterDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            closeConnections();
        }
        return topEditors;
    }

    @Override
    public Boolean searchForEditor(String accountEmail) {
        Boolean editorFound = false;
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT COUNT(1) FROM accounts WHERE accountEmail=?");
            prepStmt.setString(1, accountEmail);
            rs = prepStmt.executeQuery();
            if (rs.next()) {
                editorFound = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(EditorDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            closeConnections();
        }
        return editorFound;
    }


}
