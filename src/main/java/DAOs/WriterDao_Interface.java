/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import Models.Writer;
import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author jarro
 */
public interface WriterDao_Interface {
    public Writer getWriter(Integer writerId);
    public Writer getWriterByEmail(String email);
    public List<Writer> getWriters(Integer numberOfWriters, Integer pageNumber);
    public List<Writer> searchForWriters(String searchValue, Integer numberOfWriters, Integer pageNumber);
    public Boolean addWriter(Integer readerId);
    public Boolean addWriters(List<Integer> writerIds);
    public Boolean deleteWriter(Integer writerId);
    public Boolean updateWriter(Writer writer);
    public Boolean blockWriters(List<Integer> writerIds);
    public List<Integer> getTopWriters(Integer numberOfWriters);
    public List<Integer> getTopWritersByDate(Integer numberOfWriters, Timestamp startDate, Timestamp endDate);
    public Integer getTotalViewsByWriterId(Integer writerId);
}
