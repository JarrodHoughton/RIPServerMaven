/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServiceLayers;

import DAOs.WriterDao_Impl;
import DAOs.WriterDao_Interface;
import Models.Writer;
import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author jarro
 */
public class WriterService_Impl implements WriterService_Interface{
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
    public List<Writer> getAllWriters() {
        return writerDao.getAllWriters();
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
    
}
