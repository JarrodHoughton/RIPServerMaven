/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ServiceLayers;

import Models.Writer;
import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author Jarrod
 */
public interface WriterService_Interface {
    public Writer getWriter(Integer writerId);
    public Writer getWriterByEmail(String email);
    public List<Writer> getWriters(Integer numberOfWriters, Integer pageNumber);
    public List<Writer> searchForWriters(String searchValue, Integer numberOfWriters, Integer currentId);
    public String addWriter(Integer readerId);
    public String addWriters(List<Integer> writerIds);
    public String deleteWriter(Integer writerId);
    public String updateWriter(Writer writer);
    public String blockWriters(List<Integer> writerId);
    public List<Integer> getTopWriters(Integer numberOfWriters);
    public List<Integer> getTopWritersByDate(Integer numberOfWriters, Timestamp startDate, Timestamp endDate);
    public Integer getTotalViewsByWriterId(Integer writerId);
}
