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
public interface GenreDao_Interface {
    public Genre getGenre(Integer id);
    public List<Genre> getAllGenresForStory(Integer id);
    public List<Genre> getAllGenres();
//    public Boolean deleteGenre(Integer id);
//    public Boolean addGenre(Genre genre);
}
