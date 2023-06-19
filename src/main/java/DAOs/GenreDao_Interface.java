/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import Models.Genre;
import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author 27713
 */
public interface GenreDao_Interface {
    public Genre getGenre(Integer id);
    public List<Genre> getAllGenres();
    public Boolean deleteGenre(Integer id);
    public Boolean addGenre(Genre genre);
    public List<Genre> getTopGenres(Timestamp startDate, Timestamp endDate);
}