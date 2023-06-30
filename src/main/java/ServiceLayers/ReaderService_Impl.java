/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServiceLayers;

import DAOs.ReaderDao_Impl;
import DAOs.ReaderDao_Interface;
import Models.Reader;
import java.util.List;

/**
 *
 * @author jarro
 */
public class ReaderService_Impl implements ReaderService_Interface{
    private ReaderDao_Interface readerDao;

    public ReaderService_Impl() {
        this.readerDao = new ReaderDao_Impl();
    }
    
    @Override
    public Reader getReader(String accountEmail) {
        return readerDao.getReader(accountEmail);
    }

    @Override
    public void updateFavouriteStoriesOfAReader(Reader reader) {
        readerDao.updateFavouriteStoriesOfAReader(reader);
    }

    @Override
    public List<Reader> getAllReaders() {
        return readerDao.getAllReaders();
    }

    @Override
    public String addReader(Reader reader) {
        if (readerDao.userExists(reader.getEmail())) {
            return "This email already exists";
        }
        
        if (readerDao.addReader(reader)) {
            return "Reader account has been successfully created.";
        } else {
            return "System failed to add Reader account to the system.";
        }
    }

    @Override
    public String updateReader(Reader reader) {
        if (readerDao.updateReader(reader)) {
            return "Reader details were updated successfully.";
        } else {
            return "System failed to update the reader's details.";
        }
    }

    @Override
    public Boolean userExists(String accountEmail) {
        return readerDao.userExists(accountEmail);
    }

    @Override
    public String addFavouriteGenres(Reader reader) {
        if (readerDao.addFavouriteGenres(reader)) {
            return "Reader's favourite genres were successfully updated.";
        } else {
            return "System failed to update the reader's favourite genres.";
        }
    }

    @Override
    public String deleteAFavouriteGenreOfAReader(Reader reader, Integer genreID) {
        if (readerDao.deleteAFavouriteGenreOfAReader(reader, genreID)) {
            return "One of the Reader's favourite genre was successfully deleted.";
        } else {
            return "System failed to delete one of the reader's favourite genre.";
        }
    }

    @Override
    public String addAFavouriteGenreOfAReader(Reader reader, Integer genreID) {
        if (readerDao.addAFavouriteGenreOfAReader(reader, genreID)) {
            return "One of the Reader's favourite genre was successfully added.";
        } else {
            return "System failed to add one of the reader's favourite genre.";
        }
    }

    @Override
    public String deleteReader(Reader reader) {
        if (readerDao.deleteReader(reader)) {
            return "The Reader's account was successfully deleted.";
        } else {
            return "System failed to deleted the reader's account.";
        }
    }

    @Override
    public String setVerified(Integer readerId) {
        if (readerDao.setVerified(readerId)) {
            return "You have been verified!";
        } else {
            return "Something went wrong verifying your account.";
        }
    }

    @Override
    public String getVerifyToken(Integer readerId) {
        return readerDao.getVerifyToken(readerId);
    }

    @Override
    public Boolean isVerified(Integer readerId) {
        return readerDao.isVerified(readerId);
    }

    @Override
    public Reader getReader(Integer readerId) {
        return readerDao.getReader(readerId);
    }
    
}
