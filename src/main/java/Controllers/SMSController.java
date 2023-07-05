/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import ServiceLayers.SMSService_Impl;
import ServiceLayers.SMSService_Interface;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 *
 * @author Kylynn van der Merwe
 */
@Path("/sms")
public class SMSController {
    private final SMSService_Interface smsService;    
    
    public SMSController(){
        this.smsService = new SMSService_Impl();        
    }
    
    @Path("/sendsms/{phoneNumber}/{message}")
    @GET
    @Consumes(MediaType.APPLICATION_XML)
    public Response sendSMS(@PathParam("phoneNumber")String phoneNumber,@PathParam("message")String message){   
        return Response.ok().entity(smsService.sendSMS(phoneNumber, message)).build();
        
        
    }    
    
}
