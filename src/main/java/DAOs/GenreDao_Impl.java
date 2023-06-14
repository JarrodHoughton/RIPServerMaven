/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import Models.Genre;
import java.util.List;

/**
 *
 * @author jarro
 */
public class GenreDao_Impl implements GenreDao_Interface{

    @Override
    public Genre getGenre(Integer id) {
        Genre genre = null;
        
        return genre;
    }

    @Override
    public List<Genre> getAllGenres() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Genre> getAllGenresForStory(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
