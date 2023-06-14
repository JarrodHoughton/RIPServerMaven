/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author jarro
 */
public abstract class Account{
    private Integer id;
    private String name;
    private String surname;
    private String email;
    private String passwordHash;
    private String salt;
    private String phoneNumber;
    private String userType;

    public Account(Integer id, String name, String surname, String email, String passwordHash, String salt, String phoneNumber, String userType) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.passwordHash = passwordHash;
        this.salt = salt;
        this.phoneNumber = phoneNumber;
        this.userType = userType;
    }
    
    public Account(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "id=" + id + ", name=" + name + ", surname=" + surname + ", email=" + email + ", passwordHash=" + passwordHash + ", salt=" + salt + ", phoneNumber=" + phoneNumber + ", userType=" + userType;
    }
}
