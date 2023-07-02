/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import Models.Account;

/**
 *
 * @author jarro
 */
public class Editor extends Account{
    private Integer approvalCount;

    public Editor(Integer id, String name, String surname, String email, String passwordHash, String salt, String phoneNumber, String userType, Integer approvalCount) {
        super(id, name, surname, email, passwordHash, salt, phoneNumber, userType);
        this.approvalCount = approvalCount;
    }
    
    public Editor() {
        super();
        super.setUserType("E");
        this.approvalCount = 0;
    }

    public Integer getApprovalCount() {
        return approvalCount;
    }

    public void setApprovalCount(Integer approvalCount) {
        this.approvalCount = approvalCount;
    }

    @Override
    public String toString() {
        return "Editor{" + super.toString() +"approvalCount=" + approvalCount + '}';
    }
}
