/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.util.List;

/**
 *
 * @author jarro
 */
public class Writer extends Reader{
    private List<Integer> submittedStoryIds;
    private List<Integer> draftedStoryIds;

    public Writer(Integer id, String name, String surname, String email, String passwordHash, String salt, String phoneNumber, String userType, List<Integer> favouriteGenreIds, List<Integer> favouriteStoryIds, List<Integer> submittedStoryIds, List<Integer> draftedStoryIds) {
        super(id, name, surname, email, passwordHash, salt, phoneNumber, userType, favouriteGenreIds, favouriteStoryIds);
        this.draftedStoryIds = draftedStoryIds;
        this.submittedStoryIds = submittedStoryIds;
    }
    
    public Writer(){
        super();
    }
    
    public List<Integer> getSubmittedStoryIds() {
        return submittedStoryIds;
    }

    public void setSubmittedStoryIds(List<Integer> submittedStoryIds) {
        this.submittedStoryIds = submittedStoryIds;
    }

    public List<Integer> getDraftedStoryIds() {
        return draftedStoryIds;
    }

    public void setDraftedStoryIds(List<Integer> draftedStoryIds) {
        this.draftedStoryIds = draftedStoryIds;
    }

    @Override
    public String toString() {
        return "Writer{" + super.toString().replace("{", " ").replace("}", " ").replace("Reader", "") + "submittedStoryIds=" + submittedStoryIds + ", draftedStoryIds=" + draftedStoryIds + '}';
    }
}
