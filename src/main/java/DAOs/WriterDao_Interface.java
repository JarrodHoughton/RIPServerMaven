/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import Models.Writer;
import java.util.List;

/**
 *
 * @author jarro
 */
public interface WriterDao_Interface {
    public Writer getWriter(Integer writerId);
    public Writer getWriterByEmail(String email);
    public List<Writer> getAllWriters();
    public Boolean addWriter(Integer readerId);
    public Boolean deleteWriter(Integer writerId);
    public Boolean updateWriter(Writer writer);
}
