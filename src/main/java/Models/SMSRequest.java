/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Kylynn van der Merwe
 */
@XmlRootElement(name = "smsreq")
@XmlAccessorType(XmlAccessType.FIELD)
public class SMSRequest {
    @XmlElement(name = "datetime")
    private String dateTime;
    
    @XmlElement(name = "user")
    private String user;
    
    @XmlElement(name = "pass")
    private String password;
    
    @XmlElement(name = "msisdn")
    private String msisdn;
    
    @XmlElement(name = "message")
    private String message;
    
    public SMSRequest(){
        
    }

    public SMSRequest(String dateTime, String user, String password, String msisdn, String message) {
        this.dateTime = dateTime;
        this.user = user;
        this.password = password;
        this.msisdn = msisdn;
        this.message = message;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "SMSRequest{" + "dateTime=" + dateTime + ", user=" + user + ", password=" + password + ", msisdn=" + msisdn + ", message=" + message + '}';
    }
    
    
}
