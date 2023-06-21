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
    PreparedStatement ps;
    ResultSet rs;

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
        List<Integer> genreList = new ArrayList<>();
        String sql = "SELECT genreId FROM genres_readers WHERE accountId = ?";
        try {
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
        return genreList;
    }

    private List<Integer> getFavouriteStoriesOfUser(Integer accountId) {
        List<Integer> storyList = new ArrayList<>();
        String sql = "SELECT storyId FROM likes WHERE accountId = ?";
        try {
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
        return storyList;
    }

    @Override
    public List<Reader> getAllReaders() {
        List<Reader> readerList = new ArrayList<>();
        String sql = "SELECT accountEmail FROM accounts";
        try {
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
        return readerList;
    }

    @Override
    public Boolean addReader(Reader reader) {
        System.out.println(reader);
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
            ps.executeUpdate();
            reader.setId(getReader(reader.getEmail()).getId());
            for (Integer genreId : reader.getFavouriteGenreIds()) {
                addAFavouriteGenreOfAReader(reader, genreId);
            }
        } catch (SQLException e) {
            Logger.getLogger(ReaderDao_Impl.class.getName()).log(Level.SEVERE, null, e);
            return false;
        } finally {
            closeConnections();
        }
        return true;
    }

    @Override
    public Boolean updateReaderDetails(Reader reader) {
        String sql = "UPDATE accounts SET accountName = ?, accountSurname = ?, accountEmail = ?, accountPasswordHash = ?, accountSalt = ?, accountPhoneNumber = ?, accountType = ? WHERE accountId = ?";
        if (!userExists(reader.getEmail())) {
            return false;
        }
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
            ps.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(ReaderDao_Impl.class.getName()).log(Level.SEVERE, null, e);
            return false;
        } finally {
            closeConnections();
        }
        return true;
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
    public Boolean updateFavouriteGenresOfAReader(Reader reader) {
        List<Integer> listOfGenres = reader.getFavouriteGenreIds();
        String sql = "INSERT IGNORE INTO genres_readers VALUES(?,?)";
        try {
            connection = DBManager.getConnection();
            for (int i = 0; i < listOfGenres.size(); i++) {
                ps = connection.prepareStatement(sql);
                ps.setInt(1, listOfGenres.get(i));
                ps.setInt(2, reader.getId());
                ps.executeUpdate();
            }
            List<Integer> listOfGenresUpdated = getFavouriteGenresOfUser(reader.getId());
            reader.setFavouriteGenreIds(listOfGenresUpdated);
            return true;
        } catch (SQLException e) {
            Logger.getLogger(ReaderDao_Impl.class.getName()).log(Level.SEVERE, null, e);
            return false;
        } finally {
            closeConnections();
        }
    }

    @Override
    public Boolean deleteAFavouriteGenreOfAReader(Reader reader, Integer genreID) {
        String sql = "DELETE FROM genres_readers WHERE genreId = ? AND userID = ?";
        try {
            connection = DBManager.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1, genreID);
            ps.setInt(2, reader.getId());
            ps.executeUpdate();
            List<Integer> listOfGenres = getFavouriteGenresOfUser(reader.getId());
            reader.setFavouriteGenreIds(listOfGenres);
            return true;
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
            ps.executeUpdate();
            List<Integer> listOfGenres = getFavouriteGenresOfUser(reader.getId());
            reader.setFavouriteGenreIds(listOfGenres);
            return true;
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
            ps.executeUpdate();
            verified = true;
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


