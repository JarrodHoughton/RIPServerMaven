/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import Models.Reader;
import ServiceLayers.MailService_Impl;
import ServiceLayers.MailService_Interface;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jarrod
 */
@Path("/mail")
public class MailController {

    private MailService_Interface mailService;

    public MailController() {
        try {
            this.mailService = new MailService_Impl();
        } catch (IOException ex) {
            Logger.getLogger(MailController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralSecurityException ex) {
            Logger.getLogger(MailController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Path("/sendMail")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendMail(HashMap<String, String> emailDetails) {
        return Response.ok().entity(
                mailService.sendMail(emailDetails.get("recipient"), emailDetails.get("content"), emailDetails.get("subject"))).build();
    }
    
    @Path("/sendVerificationEmail")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendVerificationEmail(Reader reader) {
        return Response.ok().entity(mailService.sendVerificationEmail(reader)).build();
    }
    
    @Path("/sendChangePasswordEmail/{accountEmail}")
    @GET
    public Response sendChangePasswordEmail(@PathParam("accountEmail") String accountEmail) {
        return Response.ok().entity(mailService.sendChangePasswordEmail(accountEmail)).build();
    }
    
    @Path("/sendReferralEmail/{recipientEmail}/{recipientName}/{recipientPhoneNumber}")
    @GET
    public Response sendReferralEmail(@PathParam("recipientEmail") String recipientEmail, @PathParam("recipientName") String recipientName, @PathParam("recipientPhoneNumber") String recipientPhoneNumber) {
        return Response.ok().entity(mailService.sendReferralEmail(recipientEmail, recipientName, recipientPhoneNumber)).build();
    }
    
    @Path("/notifyWriterOfStorySubmission/{writerId}/{approved}")
    @GET
    public Response notifyWriterOfStorySubmission(@PathParam("writerId") Integer writerId, @PathParam("approved") Boolean approved) {
        return Response.ok().entity(mailService.notifyWriterOfStorySubmission(writerId, approved)).build();
    }
    
    @Path("/notifyApprovedWriters")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response notifyApprovedWriters(List<Integer> accountIds) {
        return Response.ok().entity(mailService.notifyApprovedWriters(accountIds, Boolean.TRUE)).build();
    }
    
    @Path("/notifyBlockedWriters")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response notifyBlockedWriters(List<Integer> accountIds) {
        return Response.ok().entity(mailService.notifyBlockedWriters(accountIds)).build();
    }
    
    @Path("/notifyRejectedWriters")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response notifyRejectedWriters(List<Integer> accountIds) {
        return Response.ok().entity(mailService.notifyApprovedWriters(accountIds, Boolean.FALSE)).build();
    }
}
