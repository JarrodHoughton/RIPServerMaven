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
@XmlRootElement(name = "smsresp")
@XmlAccessorType(XmlAccessType.FIELD)
public class SMSResponse {
    @XmlElement(name = "code")
    private String responseCode;
    
    @XmlElement(name = "desc")
    private String description;
    
    public SMSResponse(){
        
    }

    public SMSResponse(String responseCode, String description) {
        this.responseCode = responseCode;
        this.description = description;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "SMSResponse{" + "responseCode=" + responseCode + ", description=" + description + '}';
    }
    
}
