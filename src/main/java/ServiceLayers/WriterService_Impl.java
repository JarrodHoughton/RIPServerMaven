/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServiceLayers;

import DAOs.WriterDao_Impl;
import DAOs.WriterDao_Interface;
import Models.Writer;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jarrod
 */
public class WriterService_Impl implements WriterService_Interface {
    private final WriterDao_Interface writerDao;

    public WriterService_Impl() {
        writerDao = new WriterDao_Impl();
    }

    @Override
    public Writer getWriter(Integer writerId) {
        return writerDao.getWriter(writerId);
    }

    @Override
    public Writer getWriterByEmail(String email) {
        return writerDao.getWriterByEmail(email);
    }

    @Override
    public List<Writer> getWriters(Integer numberOfWriters, Integer currentId) {
        return writerDao.getWriters(numberOfWriters, currentId);
    }

    @Override
    public String addWriter(Integer readerId) {
        if (writerDao.addWriter(readerId)) {
            return "Account has been created for Writer.";
        } else {
            return "System failed to add an account for the Writer.";
        }
    }

    @Override
    public String deleteWriter(Integer writerId) {
        if (writerDao.deleteWriter(writerId)) {
            return "Writer account has been deleted from the system.";
        } else {
            return "System failed to delete Writer's account.";
        }
    }

    @Override
    public String updateWriter(Writer writer) {
        if (writerDao.updateWriter(writer)) {
            return "Writer account has been updated on the system.";
        } else {
            return "System failed to update Writer's account.";
        }
    }

    @Override
    public List<Integer> getTopWriters(Integer numberOfWriters) {
        return writerDao.getTopWriters(numberOfWriters);
    }

    @Override
    public List<Integer> getTopWritersByDate(Integer numberOfWriters, Timestamp startDate, Timestamp endDate) {
        return writerDao.getTopWritersByDate(numberOfWriters, startDate, endDate);
    }

    @Override
    public Integer getTotalViewsByWriterId(Integer writerId) {
        return writerDao.getTotalViewsByWriterId(writerId);
    }

    @Override
    public String blockWriters(List<Integer> writerId) {
        if (writerDao.blockWriters(writerId)) {
            return "Writers selected have been blocked successfully.";
        }
        return "System failed to block writers selected.";
    }

    @Override
    public String addWriters(List<Integer> writerIds) {
        if (writerDao.addWriters(writerIds)) {
            return "Writers selected have been added successfully.";
        }
        return "System failed to add writers selected.";
    }

    @Override
    public List<Writer> searchForWriters(String searchValue, Integer numberOfWriters, Integer pageNumber) {
        return writerDao.searchForWriters(searchValue, numberOfWriters, pageNumber);
    }

}
