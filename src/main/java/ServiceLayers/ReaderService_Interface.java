/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ServiceLayers;

import Models.Reader;
import java.util.List;

/**
 *
 * @author jarro
 */
public interface ReaderService_Interface {
    public Reader getReader(String accountEmail);
    public List<Reader> getAllReaders(); 
    public String addReader(Reader reader);
    public String updateReaderDetails(Reader reader);
    public Boolean userExists(String accountEmail);
    public String updateFavouriteGenresOfAReader(Reader reader);
    public String deleteAFavouriteGenreOfAReader(Reader reader, Integer genreID);
    public String addAFavouriteGenreOfAReader(Reader reader, Integer genreID);
    public String deleteReader(Reader reader);
    public void updateFavouriteStoriesOfAReader(Reader reader);
    public String setVerified(Integer readerId);
    public Boolean isVerified(Integer readerId);
    public String getVerifyToken(Integer readerId);
}
