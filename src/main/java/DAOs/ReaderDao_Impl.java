package DAOs;

import Utils.DBManager;
import Models.*;
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

    @Override
    public Reader getReader(String accountEmail) {
        String sql = "SELECT * FROM accounts WHERE accountEmail = ?";
        Reader reader = null;
        try {
            connection = DBManager.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, accountEmail);
            ResultSet rs = ps.executeQuery();
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
                reader.setFavouriteGenreIds(getFavouriteGenresOfUser(rs.getInt("accountId")));
                reader.setFavouriteStoryIds(getFavouriteStoriesOfUser(rs.getInt("accountId")));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            Logger.getLogger(ReaderDao_Impl.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            closeConnection();
        }
        return reader;
    }

    private List<Integer> getFavouriteGenresOfUser(Integer accountId) {
        List<Integer> genreList = new ArrayList<>();
        String sql = "SELECT genreId FROM genres_readers WHERE accountId = ?";
        try {
            connection = DBManager.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                genreList.add(rs.getInt("genreId"));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            Logger.getLogger(ReaderDao_Impl.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            closeConnection();
        }
        return genreList;
    }

    private List<Integer> getFavouriteStoriesOfUser(Integer accountId) {
        List<Integer> storyList = new ArrayList<>();
        String sql = "SELECT storyId FROM likes WHERE accountId = ?";
        try {
            connection = DBManager.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                storyList.add(rs.getInt("storyId"));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            Logger.getLogger(ReaderDao_Impl.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            closeConnection();
        }
        return storyList;
    }

    @Override
    public List<Reader> getAllReaders() {
        List<Reader> readerList = new ArrayList<>();
        String sql = "SELECT accountEmail FROM accounts";
        try {
            connection = DBManager.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Reader reader = getReader(rs.getString("accountEmail"));
                readerList.add(reader);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            Logger.getLogger(ReaderDao_Impl.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            closeConnection();
        }
        return readerList;
    }

    @Override
    public Boolean addReader(Reader reader) {
        String sql = "INSERT INTO accounts (accountName, accountSurname, accountEmail, accountPasswordHash, accountSalt, accountPhoneNumber, accountType) VALUES (?, ?, ?, ?, ?, ?, ?)";
        if (userExists(reader.getEmail())) {
            return false;
        }
        try {
            connection = DBManager.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, reader.getName());
            ps.setString(2, reader.getSurname());
            ps.setString(3, reader.getEmail());
            ps.setString(4, reader.getPasswordHash());
            ps.setString(5, reader.getSalt());
            ps.setString(6, reader.getPhoneNumber());
            ps.setString(7, reader.getUserType());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            Logger.getLogger(ReaderDao_Impl.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            closeConnection();
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
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, reader.getName());
            ps.setString(2, reader.getSurname());
            ps.setString(3, reader.getEmail());
            ps.setString(4, reader.getPasswordHash());
            ps.setString(5, reader.getSalt());
            ps.setString(6, reader.getPhoneNumber());
            ps.setString(7, reader.getUserType());
            ps.setInt(8, reader.getId());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            Logger.getLogger(ReaderDao_Impl.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            closeConnection();
        }
        return true;
    }

    @Override
    public Boolean userExists(String accountEmail) {
        String sql = "SELECT * FROM accounts WHERE accountEmail = ?";
        Boolean exist = false;
        try {
            connection = DBManager.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, accountEmail);
            ResultSet rs = ps.executeQuery();
            exist = rs.next();
            ps.close();
            rs.close();
        } catch (SQLException e) {
            Logger.getLogger(ReaderDao_Impl.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            closeConnection();
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
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setInt(1, listOfGenres.get(i));
                ps.setInt(2, reader.getId());
                ps.executeUpdate();
                ps.close();
            }
            List<Integer> listOfGenresUpdated = getFavouriteGenresOfUser(reader.getId());
            reader.setFavouriteGenreIds(listOfGenresUpdated);
            return true;
        } catch (SQLException e) {
            Logger.getLogger(ReaderDao_Impl.class.getName()).log(Level.SEVERE, null, e);
            return false;
        } finally {
            closeConnection();
        }
    }

    @Override
    public Boolean deleteAFavouriteGenreOfAReader(Reader reader, Integer genreID) {
        String sql = "DELETE FROM genres_readers WHERE genreId = ? AND userID = ?";
        try {
            connection = DBManager.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, genreID);
            ps.setInt(2, reader.getId());
            ps.executeUpdate();
            ps.close();
            List<Integer> listOfGenres = getFavouriteGenresOfUser(reader.getId());
            reader.setFavouriteGenreIds(listOfGenres);
            return true;
        } catch (SQLException e) {
            Logger.getLogger(ReaderDao_Impl.class.getName()).log(Level.SEVERE, null, e);
            return false;
        } finally {
            closeConnection();
        }
    }

    @Override
    public Boolean addAFavouriteGenreOfAReader(Reader reader, Integer genreID) {
        String sql = "INSERT IGNORE INTO genres_readers VALUES(?,?)";
        try {
            connection = DBManager.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, genreID);
            ps.setInt(2, reader.getId());
            ps.executeUpdate();
            ps.close();
            List<Integer> listOfGenres = getFavouriteGenresOfUser(reader.getId());
            reader.setFavouriteGenreIds(listOfGenres);
            return true;
        } catch (SQLException e) {
            Logger.getLogger(ReaderDao_Impl.class.getName()).log(Level.SEVERE, null, e);
            return false;
        } finally {
            closeConnection();
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

    /**
     * Closes the database connection.
     */
    private void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            Logger.getLogger(ReaderDao_Impl.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}

