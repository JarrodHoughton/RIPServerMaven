/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServiceLayers;

import DAOs.GenreDao_Impl;
import DAOs.GenreDao_Interface;
import Models.Genre;
import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author jarro
 */
public class GenreService_Impl implements GenreService_Interface {
    private GenreDao_Interface genreDao;

    public GenreService_Impl() {
        genreDao = new GenreDao_Impl();
    }
    
    @Override
    public Genre getGenre(Integer id) {
        return genreDao.getGenre(id);
    }

    @Override
    public List<Genre> getAllGenres() {
        return genreDao.getAllGenres();
    }

    @Override
    public String deleteGenre(Integer id) {
        if (genreDao.deleteGenre(id)) {
            return "Genre has been deleted.";
        } else {
            return "System failed to delete genre.";
        }
    }

    @Override
    public String addGenre(Genre genre) {
        if (genreDao.addGenre(genre)) {
            return "Genre has been added.";
        } else {
            return "System failed to add genre.";
        }
    }

    @Override
    public List<Genre> searchForGenres(String searchValue) {
        return genreDao.searchForGenres(searchValue);
    }

    @Override
    public List<Genre> getTopGenres(Timestamp startDate, Timestamp endDate, Integer numberOfEntries) {
        return genreDao.getTopGenres(startDate, endDate, numberOfEntries);
    }

    @Override
    public Integer getTotalViewsByGenreWithinTimePeriod(Integer genreId, Timestamp startDate, Timestamp endDate) {
        return genreDao.getTotalViewsByGenreWithinTimePeriod(genreId, startDate, endDate);
    }
    
    
}
