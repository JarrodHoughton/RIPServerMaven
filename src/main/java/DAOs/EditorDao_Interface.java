/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import Models.Editor;
import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author jarro
 */
public interface EditorDao_Interface {
    public Editor getEditor(Integer id);
    public Editor getEditorByEmail(String email);
    public List<Editor> getAllEditors();
    public Boolean updateEditor(Editor editor);
    public Boolean deleteEditor(Integer id);
    public Boolean addEditor(Editor editor);
    public List<Integer> getTopEditors(Integer numberOfEditors);
    public Boolean searchForEditor(String accountEmail);
    
}
