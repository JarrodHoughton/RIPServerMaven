/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import Models.Reader;
import java.util.List;

/**
 *
 * @author jarro
 */
public interface ReaderDao_Interface {
    public Reader getReader(String accountEmail);
    public Reader getReader(Integer readerId);
    public List<Reader> getAllReaders(); 
    public Boolean addReader(Reader reader);
    public Boolean updateReader(Reader reader);
    public Boolean setVerified(Integer readerId);
    public Boolean isVerified(Integer readerId);
    public String getVerifyToken(Integer readerId);
    public Boolean userExists(String accountEmail);
    public Boolean addFavouriteGenres(Reader reader);
    public Boolean deleteFavouriteGenres(Reader reader);
    public Boolean deleteAFavouriteGenreOfAReader(Reader reader, Integer genreID);
    public Boolean addAFavouriteGenreOfAReader(Reader reader, Integer genreID);
    public Boolean deleteReader(Reader reader);
    public void updateFavouriteStoriesOfAReader(Reader reader);
}
