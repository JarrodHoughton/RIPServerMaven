/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServiceLayers;

import DAOs.EditorDao_Impl;
import DAOs.EditorDao_Interface;
import Models.Editor;
import java.util.List;

/**
 *
 * @author jarro
 */
public class EditorService_Impl implements EditorService_Interface{
    private EditorDao_Interface editorDao;

    public EditorService_Impl() {
        editorDao = new EditorDao_Impl();
    }

    @Override
    public Editor getEditor(Integer id) {
        return editorDao.getEditor(id);
    }

    @Override
    public Editor getEditorByEmail(String email) {
        return editorDao.getEditorByEmail(email);
    }

    @Override
    public List<Editor> getAllEditors() {
        return editorDao.getAllEditors();
    }

    @Override
    public String updateEditor(Editor editor) {
        if (editorDao.updateEditor(editor)) {
            return "Editor details has been updated.";
        } else {
            return "Editor details failed to update.";
        }
    }

    @Override
    public String deleteEditor(Integer id) {
        if (editorDao.deleteEditor(id)) {
            return "Editor account has been deleted.";
        } else {
            return "Editor account failed to delete.";
        }
    }

    @Override
    public String addEditor(Editor editor) {
        if (editorDao.addEditor(editor)) {
            return "Editor account has been added to the system.";
        } else {
            return "system failed to add new Editor account.";
        }    
    }

    @Override
    public List<Integer> getTopEditors(Integer numberOfEditors) {
        return editorDao.getTopEditors(numberOfEditors);
            }

    @Override
    public Boolean searchForEditor(String email) {
        return editorDao.searchForEditor(email);
    }
    
}
