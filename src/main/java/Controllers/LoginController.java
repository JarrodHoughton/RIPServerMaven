/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import Models.Account;
import Models.Reader;
import ServiceLayers.EditorService_Impl;
import ServiceLayers.EditorService_Interface;
import ServiceLayers.ReaderService_Impl;
import ServiceLayers.ReaderService_Interface;
import ServiceLayers.WriterService_Impl;
import ServiceLayers.WriterService_Interface;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;

/**
 *
 * @author jarro
 */
@Path("/login")
public class LoginController {

    private ReaderService_Interface readerService;
    private WriterService_Interface writerService;
    private EditorService_Interface editorService;

    public LoginController() {
        this.readerService = new ReaderService_Impl();
        this.writerService = new WriterService_Impl();
        this.editorService = new EditorService_Impl();
    }

    @Path("/getUserSalt/{email}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserSalt(@PathParam("email") String email) {
        String userFound = "false";
        String salt = "";
        String userType = "";
        String userVerified = "false";
        HashMap<String, String> userDetails = new HashMap<>();
        if (readerService.userExists(email)) {
            Reader user = readerService.getReader(email);
            userFound = "true";
            salt = user.getSalt();
            userType = user.getUserType();
            userVerified = String.valueOf(user.getVerified());
        } else {
            System.out.println("User not found!!!!");
        }
        userDetails.put("salt", salt);
        userDetails.put("userFound", userFound);
        userDetails.put("userType", userType);
        userDetails.put("userVerified", userVerified);
        return Response.ok().entity(userDetails).build();
    }

    @Path("/getUser")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getUser(Reader reader) {
        if (!readerService.userExists(reader.getEmail())) {
            return Response.noContent().build();
        } else {
            Account user = readerService.getReader(reader.getEmail());
            if (!user.getPasswordHash().equals(reader.getPasswordHash())) {
                return Response.ok().entity(null).build();
            }
            switch (user.getUserType()) {
                case "R":
                    return Response.ok().entity(readerService.getReader(user.getEmail())).build();
                case "W":
                    return Response.ok().entity(writerService.getWriterByEmail(user.getEmail())).build();
                case "E":
                    return Response.ok().entity(editorService.getEditorByEmail(user.getEmail())).build();
                case "A":
                    return Response.ok().entity(editorService.getEditorByEmail(user.getEmail())).build();
                default:
                    return Response.ok().entity(null).build();
            }
        }
    }

    @Path("/register")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(Reader reader) {
        return Response.ok().entity(readerService.addReader(reader)).build();
    }
}
