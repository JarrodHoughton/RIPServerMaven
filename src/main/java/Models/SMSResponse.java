/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Kylynn van der Merwe
 */
@XmlRootElement
public class SMSResponse {
    private String responseCode;
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
