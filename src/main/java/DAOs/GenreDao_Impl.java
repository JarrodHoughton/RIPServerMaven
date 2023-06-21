package DAOs;

import Models.Genre;
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

public class GenreDao_Impl implements GenreDao_Interface {
    private Connection connection;
    private PreparedStatement prepStmt;
    private ResultSet rs;

    public GenreDao_Impl() {
    }

    @Override
    public Genre getGenre(Integer id) {
        Genre genre = null;

        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT * FROM genres WHERE genreId=?");
            prepStmt.setInt(1, id);
            rs = prepStmt.executeQuery();

            if (rs.next()) {
                genre = new Genre(
                        rs.getInt(1),
                        rs.getString(2)
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(GenreDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            closeConnections();
        }

        return genre;
    }

    @Override
    public List<Genre> getAllGenres() {
        List<Genre> genres = new ArrayList<>();
        Genre genre;

        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT * FROM genres");
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                genre = new Genre(
                        rs.getInt(1),
                        rs.getString(2)
                );
                genres.add(genre);
            }
        } catch (SQLException ex) {
            Logger.getLogger(GenreDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            closeConnections();
        }

        return genres;
    }

    @Override
    public Boolean deleteGenre(Integer id) {

        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("DELETE FROM genres WHERE genreId = ?");
            prepStmt.setInt(1, id);
            prepStmt.executeUpdate();

            // Check to see if the genre was deleted
            prepStmt = connection.prepareStatement("SELECT * FROM genres WHERE genreId = ?");
            prepStmt.setInt(1, id);
            rs = prepStmt.executeQuery();
            if (rs != null) {
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(GenreDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            closeConnections();
        }

        return true;
    }

    @Override
    public Boolean addGenre(Genre genre) {

        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("INSERT INTO genres (genreName) VALUES (?)");
            prepStmt.setString(1, genre.getName());
            prepStmt.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(GenreDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            closeConnections();
        }

        return true;
    }

    private void closeConnections() {
        
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(GenreDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if (prepStmt != null) {
            try {
                prepStmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(GenreDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(GenreDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }

    @Override
    public List<Genre> getTopGenres(Timestamp startDate, Timestamp endDate, Integer numberOfEntries) {

        List<Genre> genres = new ArrayList<>();

        try {
            connection = DBManager.getConnection();

            String query = "SELECT g.genreId, g.genreName, COUNT(*) AS viewCount " +
                    "FROM ripdb.views v " +
                    "JOIN ripdb.stories s ON v.storyId = s.storyId " +
                    "JOIN ripdb.stories_genres sg ON s.storyId = sg.storyId " +
                    "JOIN ripdb.genres g ON sg.genreId = g.genreId " +
                    "WHERE v.viewDate >= ? AND v.viewDate <= ? " +
                    "GROUP BY g.genreId, g.genreName " +
                    "ORDER BY viewCount DESC " +
                    "LIMIT ?";

            prepStmt = connection.prepareStatement(query);
            prepStmt.setTimestamp(1, startDate);
            prepStmt.setTimestamp(2, endDate);
            prepStmt.setInt(3, numberOfEntries);

            rs = prepStmt.executeQuery();

            while (rs.next()) {
                int genreId = rs.getInt("genreId");
                String genreName = rs.getString("genreName");

                Genre genre = new Genre(genreId, genreName);
                genres.add(genre);
            }

        } catch (SQLException ex) {
            Logger.getLogger(GenreDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            closeConnections();
        }

        return genres;
    }

    @Override
    public List<Genre> searchForGenres(String searchValue) {
        List<Genre> genres = new ArrayList<>();
        Genre genre;
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT * FROM genres WHERE genreName LIKE ?");
            prepStmt.setString(1, "%"+searchValue+"%");
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                genre = new Genre(
                        rs.getInt(1),
                        rs.getString(2)
                );
                genres.add(genre);
            }
        } catch (SQLException ex) {
            Logger.getLogger(GenreDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            closeConnections();
        }

        return genres;
    }

}


