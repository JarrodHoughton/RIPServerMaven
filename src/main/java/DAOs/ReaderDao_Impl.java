package DAOs;

import Utils.DBManager;
import Utils.PasswordEncryptor;
import Models.*;
import Utils.VerificationTokenGenerator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReaderDao_Impl implements ReaderDao_Interface {

    private Connection connection;
    private PreparedStatement ps;
    private ResultSet rs;

    public ReaderDao_Impl() {
    }

    @Override
    public Reader getReader(String accountEmail) {
        String sql = "SELECT * FROM accounts WHERE accountEmail = ?";
        Reader reader = null;
        try {
            connection = DBManager.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setString(1, accountEmail);
            rs = ps.executeQuery();
            if (rs.next()) {
                reader = new Reader();
                reader.setId(rs.getInt("accountId"));
                reader.setName(rs.getString("accountName"));
                reader.setSurname(rs.getString("accountSurname"));
                reader.setEmail(accountEmail);
                reader.setPasswordHash(rs.getString("accountPasswordHash"));
                reader.setSalt(rs.getString("accountSalt"));
                reader.setPhoneNumber(rs.getString("accountPhoneNumber"));
                reader.setUserType(rs.getString("accountType"));
                reader.setVerified(rs.getString("verified").equals("F") ? Boolean.FALSE : Boolean.TRUE);
            }
        } catch (SQLException e) {
            Logger.getLogger(ReaderDao_Impl.class.getName()).log(Level.SEVERE, null, e);
            return null;
        } finally {
            closeConnections();
        }
        
        if (reader != null) {
            reader.setFavouriteGenreIds(getFavouriteGenresOfUser(reader.getId()));
            reader.setFavouriteStoryIds(getFavouriteStoriesOfUser(reader.getId()));
        }
        
        return reader;
    }

    @Override
    public Reader getReader(Integer readerId) {
        String sql = "SELECT * FROM accounts WHERE accountId = ?";
        Reader reader = null;
        try {
            connection = DBManager.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1, readerId);
            rs = ps.executeQuery();
            if (rs.next()) {
                reader = new Reader();
                reader.setId(rs.getInt("accountId"));
                reader.setName(rs.getString("accountName"));
                reader.setSurname(rs.getString("accountSurname"));
                reader.setEmail(rs.getString("accountEmail"));
                reader.setPasswordHash(rs.getString("accountPasswordHash"));
                reader.setSalt(rs.getString("accountSalt"));
                reader.setPhoneNumber(rs.getString("accountPhoneNumber"));
                reader.setUserType(rs.getString("accountType"));
                reader.setVerified(rs.getString("verified").equals("F") ? Boolean.FALSE : Boolean.TRUE);
                reader.setFavouriteGenreIds(getFavouriteGenresOfUser(reader.getId()));
                reader.setFavouriteStoryIds(getFavouriteStoriesOfUser(reader.getId()));
            }
        } catch (SQLException e) {
            Logger.getLogger(ReaderDao_Impl.class.getName()).log(Level.SEVERE, null, e);
            return null;
        } finally {
            closeConnections();
        }
        return reader;
    }

    private List<Integer> getFavouriteGenresOfUser(Integer accountId) {
        List<Integer> genreList = null;
        String sql = "SELECT genreId FROM genres_readers WHERE accountId = ?";
        try {
            genreList = new ArrayList<>();
            connection = DBManager.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1, accountId);
            rs = ps.executeQuery();
            while (rs.next()) {
                genreList.add(rs.getInt("genreId"));
            }
        } catch (SQLException e) {
            Logger.getLogger(ReaderDao_Impl.class.getName()).log(Level.SEVERE, null, e);
            return null;
        } finally {
            closeConnections();
        }

        if (genreList != null && genreList.isEmpty()) {
            return null;
        }

        return genreList;
    }

    private List<Integer> getFavouriteStoriesOfUser(Integer accountId) {
        List<Integer> storyList = null;
        String sql = "SELECT storyId FROM likes WHERE accountId = ?";
        try {
            storyList = new ArrayList<>();
            connection = DBManager.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1, accountId);
            rs = ps.executeQuery();
            while (rs.next()) {
                storyList.add(rs.getInt("storyId"));
            }
        } catch (SQLException e) {
            Logger.getLogger(ReaderDao_Impl.class.getName()).log(Level.SEVERE, null, e);
            return null;
        } finally {
            closeConnections();
        }

        if (storyList != null && storyList.isEmpty()) {
            return null;
        }

        return storyList;
    }

    @Override
    public List<Reader> getAllReaders() {
        List<Reader> readerList = null;
        String sql = "SELECT accountEmail FROM accounts";
        try {
            readerList = new ArrayList<>();
            connection = DBManager.getConnection();
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Reader reader = getReader(rs.getString("accountEmail"));
                readerList.add(reader);
            }
        } catch (SQLException e) {
            Logger.getLogger(ReaderDao_Impl.class.getName()).log(Level.SEVERE, null, e);
            return null;
        } finally {
            closeConnections();
        }

        if (readerList != null && readerList.isEmpty()) {
            return null;
        }

        return readerList;
    }

    @Override
    public Boolean addReader(Reader reader) {
        Boolean added = false;
        String sql = "INSERT INTO accounts (accountName, accountSurname, accountEmail, accountPasswordHash, accountSalt, accountPhoneNumber, accountType, verifyToken) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            connection = DBManager.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setString(1, reader.getName());
            ps.setString(2, reader.getSurname());
            ps.setString(3, reader.getEmail());
            ps.setString(4, reader.getPasswordHash());
            ps.setString(5, reader.getSalt());
            ps.setString(6, reader.getPhoneNumber());
            ps.setString(7, reader.getUserType());
            ps.setString(8, VerificationTokenGenerator.generateToken());
            added = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(ReaderDao_Impl.class.getName()).log(Level.SEVERE, null, e);
            return false;
        } finally {
            closeConnections();
        }
        if (added) {
            reader.setId(getReaderId(reader.getEmail()));
        }

        if (reader.getId() != null) {
            for (Integer genreId : reader.getFavouriteGenreIds()) {
                added = added && addAFavouriteGenreOfAReader(reader, genreId);
            }
        }
        return added;
    }

    private Integer getReaderId(String accountEmail) {
        Integer readerId = null;
        String sql = "SELECT accountId FROM accounts WHERE accountEmail=?;";
        try {
            connection = DBManager.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setString(1, accountEmail);
            rs = ps.executeQuery();
            if (rs.next()) {
                readerId = rs.getInt("accountId");
            }
        } catch (SQLException e) {
            Logger.getLogger(ReaderDao_Impl.class.getName()).log(Level.SEVERE, null, e);
            return null;
        } finally {
            closeConnections();
        }
        return readerId;
    }

    @Override
    public Boolean updateReader(Reader reader) {
        Boolean updated = false;
        String sql = "UPDATE accounts SET accountName = ?, accountSurname = ?, accountEmail = ?, accountPasswordHash = ?, accountSalt = ?, accountPhoneNumber = ?, accountType = ? WHERE accountId = ?";
        try {
            connection = DBManager.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setString(1, reader.getName());
            ps.setString(2, reader.getSurname());
            ps.setString(3, reader.getEmail());
            ps.setString(4, reader.getPasswordHash());
            ps.setString(5, reader.getSalt());
            ps.setString(6, reader.getPhoneNumber());
            ps.setString(7, reader.getUserType());
            ps.setInt(8, reader.getId());
            updated = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(ReaderDao_Impl.class.getName()).log(Level.SEVERE, null, e);
            return false;
        } finally {
            closeConnections();
        }
        
        updated = updated && deleteFavouriteGenres(reader) && addFavouriteGenres(reader);
        return updated;
    }

    @Override
    public Boolean userExists(String accountEmail) {
        String sql = "SELECT * FROM accounts WHERE accountEmail = ?";
        Boolean exist = false;
        try {
            connection = DBManager.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setString(1, accountEmail);
            rs = ps.executeQuery();
            exist = rs.next();
        } catch (SQLException e) {
            Logger.getLogger(ReaderDao_Impl.class.getName()).log(Level.SEVERE, null, e);
            return false;
        } finally {
            closeConnections();
        }
        return exist;
    }

    @Override
    public Boolean addFavouriteGenres(Reader reader) {
        Boolean updated = true;
        String addFavouriteGenresSql = "INSERT INTO genres_readers (genreId, accountId) VALUES(?,?)";
        try {
            connection = DBManager.getConnection();
            ps = connection.prepareStatement(addFavouriteGenresSql);
            for (Integer genreId : reader.getFavouriteGenreIds()) {
                ps.setInt(1, genreId);
                ps.setInt(2, reader.getId());
                ps.addBatch();
            }
            updated = ps.executeBatch()[0] >= 0;
        } catch (SQLException e) {
            Logger.getLogger(ReaderDao_Impl.class.getName()).log(Level.SEVERE, null, e);
            return false;
        } finally {
            closeConnections();
        }
        return updated;
    }

    @Override
    public Boolean deleteAFavouriteGenreOfAReader(Reader reader, Integer genreID) {
        String sql = "DELETE FROM genres_readers WHERE genreId = ? AND userID = ?";
        try {
            connection = DBManager.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1, genreID);
            ps.setInt(2, reader.getId());
            return ps.executeUpdate() >= 0;
        } catch (SQLException e) {
            Logger.getLogger(ReaderDao_Impl.class.getName()).log(Level.SEVERE, null, e);
            return false;
        } finally {
            closeConnections();
        }
    }
    
    @Override
    public Boolean deleteFavouriteGenres(Reader reader) {
        String sql = "DELETE FROM genres_readers WHERE accountId = ?";
        try {
            connection = DBManager.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1, reader.getId());
            return ps.executeUpdate() >= 0;
        } catch (SQLException e) {
            Logger.getLogger(ReaderDao_Impl.class.getName()).log(Level.SEVERE, null, e);
            return false;
        } finally {
            closeConnections();
        }
    }

    @Override
    public Boolean addAFavouriteGenreOfAReader(Reader reader, Integer genreID) {
        String sql = "INSERT INTO genres_readers VALUES(?,?)";
        try {
            connection = DBManager.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1, genreID);
            ps.setInt(2, reader.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(ReaderDao_Impl.class.getName()).log(Level.SEVERE, null, e);
            return false;
        } finally {
            closeConnections();
        }
    }

    @Override
    public void updateFavouriteStoriesOfAReader(Reader reader) {
        List<Integer> favouriteStories = getFavouriteStoriesOfUser(reader.getId());
        reader.setFavouriteStoryIds(favouriteStories);
    }

    @Override
    public Boolean deleteReader(Reader reader) {
        return false;
    }

    @Override
    public Boolean setVerified(Integer readerId) {
        Boolean verified = false;
        String sql = "UPDATE accounts SET verified = 'T' WHERE accountId = ?";
        try {
            connection = DBManager.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1, readerId);
            verified = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(ReaderDao_Impl.class.getName()).log(Level.SEVERE, null, e);
            return false;
        } finally {
            closeConnections();
        }
        return verified;
    }

    @Override
    public Boolean isVerified(Integer readerId) {
        Boolean verified = false;
        String sql = "SELECT verified FROM accounts WHERE accountId = ?";
        try {
            connection = DBManager.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1, readerId);
            rs = ps.executeQuery();
            if (rs.next()) {
                verified = rs.getString("verified").equals("T") ? Boolean.TRUE : Boolean.FALSE;
            }
        } catch (SQLException e) {
            Logger.getLogger(ReaderDao_Impl.class.getName()).log(Level.SEVERE, null, e);
            return false;
        } finally {
            closeConnections();
        }
        return verified;

    }

    @Override
    public String getVerifyToken(Integer readerId) {
        String verifyToken = null;
        String sql = "SELECT verifyToken FROM accounts WHERE accountId = ?";
        try {
            connection = DBManager.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1, readerId);
            rs = ps.executeQuery();
            if (rs.next()) {
                verifyToken = rs.getString("verifyToken");
            }
        } catch (SQLException e) {
            Logger.getLogger(ReaderDao_Impl.class.getName()).log(Level.SEVERE, null, e);
            return null;
        } finally {
            closeConnections();
        }
        return verifyToken;
    }

    /**
     * Closes the database connection, prepared statement, and result set.
     */
    private void closeConnections() {
        try {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
            if (ps != null && !ps.isClosed()) {
                ps.close();
            }
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            Logger.getLogger(ReaderDao_Impl.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
