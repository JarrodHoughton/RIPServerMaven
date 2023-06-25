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
 * @author jarro
 */
public interface WriterService_Interface {
    public Writer getWriter(Integer writerId);
    public Writer getWriterByEmail(String email);
    public List<Writer> getAllWriters();
    public String addWriter(Integer readerId);
    public String deleteWriter(Integer writerId);
    public String updateWriter(Writer writer);
    public List<Integer> getTopWriters(Integer numberOfWriters);
    public List<Integer> getTopWritersByDate(Integer numberOfWriters, Timestamp startDate, Timestamp endDate);
    public Integer getTotalViewsByWriterId(Integer writerId);
}
