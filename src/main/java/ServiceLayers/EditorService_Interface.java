/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ServiceLayers;

import Models.Editor;
import java.util.List;

/**
 *
 * @author jarro
 */
public interface EditorService_Interface {
    public Editor getEditor(Integer id);
    public Editor getEditorByEmail(String email);
    public List<Editor> getAllEditors();
    public String updateEditor(Editor editor);
    public String deleteEditor(Integer id);
    public String addEditor(Editor editor);
    public List<Integer> getTopEditors(Integer numberOfEditors);
    public Boolean searchForEditor(String email);
}
