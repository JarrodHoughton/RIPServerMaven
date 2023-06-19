/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import Models.Genre;
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
 * @author 27713
 */
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
            
            if(rs.next()){
                genre = new Genre(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getInt(3)
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(GenreDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
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
            while (rs.next()){
                genre = new Genre(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getInt(3)
                );
                genres.add(genre);
            }
        } catch (SQLException ex) {
            Logger.getLogger(GenreDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
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
            
            //check to see if the genre was deleted
            prepStmt = connection.prepareStatement("SELECT * FROM genres WHERE genreId = ?");
            prepStmt.setInt(1, id);
            rs = prepStmt.executeQuery();
            if(rs!=null){
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(GenreDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }finally{
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
        }finally{
            closeConnections();
        }        
        
        return true;
    }
    
    private void closeConnections(){
        if(prepStmt!=null){
                try {
                    prepStmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(GenreDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(connection!=null){
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(GenreDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(rs!=null){
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(GenreDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
    }
    
}
