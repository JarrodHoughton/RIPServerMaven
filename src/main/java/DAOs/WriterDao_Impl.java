package DAOs;

import Models.Writer;
import Utils.DBManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WriterDao_Impl implements WriterDao_Interface {

    private Connection connection;
    private PreparedStatement prepStmt;
    private ResultSet rs;

    public WriterDao_Impl() {
    }

    @Override
    public Boolean blockWriters(List<Integer> writerIds) {
        Boolean writersBlocked = false;
        for (Integer writerId : writerIds) {
            String sql = "UPDATE accounts SET accountType='R' WHERE accountId=?;";
            try {
                connection = DBManager.getConnection();
                prepStmt = connection.prepareStatement(sql);
                prepStmt.setInt(1, writerId);
                writersBlocked = prepStmt.executeUpdate() > 0;
            } catch (SQLException ex) {
                Logger.getLogger(WriterDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        return writersBlocked;
    }

    @Override
    public Writer getWriter(Integer writerId) {
        Writer writer = null;
        String writerDetailsQuery = "SELECT * FROM accounts WHERE accountId = ?;";
        try {
            writer = new Writer();
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement(writerDetailsQuery);
            prepStmt.setInt(1, writerId);
            rs = prepStmt.executeQuery();
            if (rs.next()) {
                writer.setId(writerId);
                writer.setName(rs.getString("accountName"));
                writer.setSurname(rs.getString("accountSurname"));
                writer.setEmail(rs.getString("accountEmail"));
                writer.setPasswordHash(rs.getString("accountPasswordHash"));
                writer.setSalt(rs.getString("accountSalt"));
                writer.setPhoneNumber(rs.getString("accountPhoneNumber"));
                writer.setUserType(rs.getString("accountType"));
                writer.setVerified(rs.getString("verified").equals("F") ? Boolean.FALSE : Boolean.TRUE);
            }
        } catch (SQLException ex) {
            Logger.getLogger(WriterDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            closeConnections();
        }

        if (writer != null) {
            //getting all the writer's favourite story Ids
            writer.setFavouriteStoryIds(getWritersFavouriteStoryIds(writer.getId()));

            //getting all the writer's favourite genre Ids
            writer.setFavouriteGenreIds(getWritersFavouriteGenreIds(writer.getId()));

            //getting all the writer's submitted and drafted story Ids
            writer = setCreatedStories(writer);
        }

        return writer;
    }

    private List<Integer> getWritersFavouriteStoryIds(Integer writerId) {
        List<Integer> favouriteStoryIds = null;
        String favouriteStoriesQuery = "SELECT storyId FROM likes WHERE accountId = ?;";
        try {
            favouriteStoryIds = new ArrayList<>();
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement(favouriteStoriesQuery);
            prepStmt.setInt(1, writerId);
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                favouriteStoryIds.add(rs.getInt("storyId"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(WriterDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnections();
        }

        if (favouriteStoryIds != null && favouriteStoryIds.isEmpty()) {
            return null;
        }

        return favouriteStoryIds;
    }

    private List<Integer> getWritersFavouriteGenreIds(Integer writerId) {
        List<Integer> favouriteGenreIds = null;
        String favouriteGenresQuery = "SELECT genreId FROM genres_readers WHERE accountId = ?;";
        try {
            favouriteGenreIds = new ArrayList<>();
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement(favouriteGenresQuery);
            prepStmt.setInt(1, writerId);
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                favouriteGenreIds.add(rs.getInt("genreId"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(WriterDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnections();
        }

        if (favouriteGenreIds != null && favouriteGenreIds.isEmpty()) {
            return null;
        }

        return favouriteGenreIds;
    }

    private Writer setCreatedStories(Writer writer) {
        try {
            String createdStoriesQuery = "SELECT storyId, submitted FROM stories WHERE accountId = ?;";
            List<Integer> submittedStoryIds = new ArrayList<>();
            List<Integer> draftedStoryIds = new ArrayList<>();
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement(createdStoriesQuery);
            prepStmt.setInt(1, writer.getId());
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                Integer storyId = rs.getInt("storyId");
                if (rs.getBoolean("submitted")) {
                    submittedStoryIds.add(storyId);
                } else {
                    draftedStoryIds.add(storyId);
                }
            }
            writer.setDraftedStoryIds(draftedStoryIds);
            writer.setSubmittedStoryIds(submittedStoryIds);
        } catch (SQLException ex) {
            Logger.getLogger(WriterDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnections();
        }
        return writer;
    }

    @Override
    public List<Writer> getWriters(Integer numberOfWriters, Integer pageNumber) {
        List<Writer> writers = null;
        List<Integer> writerIds = null;

        try {
            Integer currentOffSet = pageNumber * numberOfWriters;
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement(
                    "SELECT accountId FROM accounts WHERE accountType='W' ORDER BY accountId  LIMIT ?,?;");
            prepStmt.setInt(1, currentOffSet);
            prepStmt.setInt(2, numberOfWriters);
            rs = prepStmt.executeQuery();
            writerIds = new ArrayList<>();
            while (rs.next()) {
                writerIds.add(rs.getInt("accountId"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(WriterDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            closeConnections();
        }

        if (writerIds != null && !writerIds.isEmpty()) {
            writers = new ArrayList<>();
            for (Integer writerId : writerIds) {
                writers.add(getWriter(writerId));
            }
        }

        return writers;
    }

    @Override
    public List<Integer> getTopWritersByDate(Integer numberOfWriters, Timestamp startDate, Timestamp endDate) {
        List<Integer> topWriters = null;
        try {
            topWriters = new ArrayList<>();
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT v.accountId, COUNT(viewId) AS viewCount "
                    + "FROM views AS v INNER JOIN stories AS s ON v.storyId = s.storyId "
                    + "WHERE v.viewDate >= ? AND v.viewDate <= ? "
                    + "GROUP BY v.accountId ORDER BY viewCount DESC LIMIT ?;");
            prepStmt.setTimestamp(1, startDate);
            prepStmt.setTimestamp(2, endDate);
            prepStmt.setInt(3, numberOfWriters);
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                topWriters.add(rs.getInt("accountId"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(WriterDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            closeConnections();
        }
        return topWriters;
    }

    @Override
    public Writer getWriterByEmail(String email) {
        Writer writer = null;
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT * FROM accounts WHERE accountEmail = ?;");
            prepStmt.setString(1, email);
            rs = prepStmt.executeQuery();
            if (rs.next()) {
                writer = new Writer();
                writer.setId(rs.getInt("accountId"));
                writer.setName(rs.getString("accountName"));
                writer.setSurname(rs.getString("accountSurname"));
                writer.setEmail(rs.getString("accountEmail"));
                writer.setPasswordHash(rs.getString("accountPasswordHash"));
                writer.setSalt(rs.getString("accountSalt"));
                writer.setPhoneNumber(rs.getString("accountPhoneNumber"));
                writer.setUserType(rs.getString("accountType"));
                writer.setVerified(rs.getString("verified").equals("F") ? Boolean.FALSE : Boolean.TRUE);
            }
        } catch (SQLException ex) {
            Logger.getLogger(WriterDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            closeConnections();
        }

        if (writer != null) {
            //getting all the writer's favourite story Ids
            writer.setFavouriteStoryIds(getWritersFavouriteStoryIds(writer.getId()));

            //getting all the writer's favourite genre Ids
            writer.setFavouriteGenreIds(getWritersFavouriteGenreIds(writer.getId()));

            //getting all the writer's submitted and drafted story Ids
            writer = setCreatedStories(writer);
        }

        return writer;
    }

    @Override
    public Boolean addWriter(Integer readerId) {
        Boolean added = false;
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("UPDATE accounts SET accountType='W' WHERE accountId=?");
            prepStmt.setInt(1, readerId);
            prepStmt.executeUpdate();
            added = true;
        } catch (SQLException ex) {
            Logger.getLogger(WriterDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            closeConnections();
        }
        return added;
    }

    @Override
    public Boolean addWriters(List<Integer> writerIds) {
        Boolean added = false;
        try {
            connection = DBManager.getConnection();
            for (Integer writerId : writerIds) {
                prepStmt = connection.prepareStatement("UPDATE accounts SET accountType='W' WHERE accountId=?");
                prepStmt.setInt(1, writerId);
                prepStmt.addBatch();
            }
            added = prepStmt.executeBatch()[0] > 0;
        } catch (SQLException ex) {
            Logger.getLogger(WriterDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            closeConnections();
        }
        return added;
    }

    @Override
    public Boolean deleteWriter(Integer writerId) {
        Boolean deleted = false;
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("DELETE FROM accounts WHERE accountId=?");
            prepStmt.setInt(1, writerId);
            prepStmt.executeUpdate();
            deleted = true;
        } catch (SQLException ex) {
            Logger.getLogger(WriterDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            closeConnections();
        }
        return deleted;
    }

    @Override
    public Boolean updateWriter(Writer writer) {
        Boolean updated = false;
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement(
                    "UPDATE accounts SET accountName=?, accountSurname=?, accountEmail=?, accountPasswordHash=?, accountSalt=?, accountPhoneNumber=?, accountType=? "
                    + "WHERE accountId=?;"
            );
            prepStmt.setString(1, writer.getName());
            prepStmt.setString(2, writer.getSurname());
            prepStmt.setString(3, writer.getEmail());
            prepStmt.setString(4, writer.getPasswordHash());
            prepStmt.setString(5, writer.getSalt());
            prepStmt.setString(6, writer.getPhoneNumber());
            prepStmt.setString(7, writer.getUserType());
            prepStmt.setInt(8, writer.getId());
            prepStmt.executeUpdate();
            updated = true;
        } catch (SQLException ex) {
            Logger.getLogger(WriterDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            closeConnections();
        }
        return updated;
    }

    @Override
    public List<Integer> getTopWriters(Integer numberOfWriters) {
        List<Integer> topWriters = new ArrayList<>();
        try {
            connection = DBManager.getConnection();
            updateViewCount(connection);
            prepStmt = connection.prepareStatement("SELECT accountId, SUM(viewCount) AS totalViews "
                    + "FROM stories GROUP BY accountId ORDER BY totalViews DESC LIMIT ?;");
            prepStmt.setInt(1, numberOfWriters);
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                topWriters.add(rs.getInt("accountId"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(WriterDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            closeConnections();
        }
        return topWriters;
    }

    @Override
    public Integer getTotalViewsByWriterId(Integer writerId) {
        Integer totalViews = null;
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT COUNT(viewId) AS totalViews FROM views "
                    + "INNER JOIN stories ON views.storyId = stories.storyId "
                    + "WHERE stories.accountId = ?");
            prepStmt.setInt(1, writerId);
            rs = prepStmt.executeQuery();

            if (rs.next()) {
                totalViews = rs.getInt("totalViews");
            }
        } catch (SQLException ex) {
            Logger.getLogger(RatingDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            closeConnections();
        }

        return totalViews;
    }

    private void closeConnections() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (prepStmt != null) {
                prepStmt.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(WriterDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void updateViewCount(Connection conn) {
        PreparedStatement statement = null;
        try {
            String query = "UPDATE stories s "
                    + "SET s.viewCount = (SELECT COUNT(*) FROM views v WHERE v.storyId = s.storyId)";
            statement = conn.prepareStatement(query);
            statement.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(ViewDao_Impl.class.getName()).log(Level.SEVERE, null, ex);

        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ViewDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    public List<Writer> searchForWriters(String searchValue, Integer numberOfWriters, Integer pageNumber) {
        List<Writer> writers = null;
        List<Integer> writerIds = null;

        try {
            writerIds = new ArrayList<>();
            for (String value : searchValue.split(" ")) {
                System.out.println(searchValue + " " + numberOfWriters + " " + pageNumber + " ");
                Integer startingOffset = pageNumber * numberOfWriters;
                connection = DBManager.getConnection();
                prepStmt = connection.prepareStatement(
                        "SELECT accountId FROM accounts WHERE (accountName LIKE ? OR accountSurname LIKE ? OR accountEmail LIKE ?) and accountType='W' GROUP BY accountId ORDER BY accountId  LIMIT ?,?;");
                prepStmt.setString(1, "%" + value + "%");
                prepStmt.setString(2, "%" + value + "%");
                prepStmt.setString(3, "%" + value + "%");
                prepStmt.setInt(4, startingOffset);
                prepStmt.setInt(5, numberOfWriters);
                rs = prepStmt.executeQuery();
                while (rs.next()) {
                    if (!writerIds.contains(rs.getInt("accountId"))) {
                        writerIds.add(rs.getInt("accountId"));
                    }
                }
                closeConnections();
            }
        } catch (SQLException ex) {
            Logger.getLogger(WriterDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            closeConnections();
        }

        if (writerIds != null && !writerIds.isEmpty()) {
            writers = new ArrayList<>();
            for (Integer writerId : writerIds) {
                writers.add(getWriter(writerId));
            }
        }

        return writers;
    }
}
