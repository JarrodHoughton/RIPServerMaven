/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import ServiceLayers.ReaderService_Impl;
import ServiceLayers.ReaderService_Interface;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

/**
 *
 * @author jarro
 */
@Path("/readers")
public class ReaderController {
    private ReaderService_Interface readerService;

    public ReaderController() {
        this.readerService = new ReaderService_Impl();
    }
    
    @Path("/userExists/{email}")
    @GET
    public Response userExists(@PathParam("email") String email) {
        return Response.ok().entity(readerService.userExists(email)).build();
    }
    
    @Path("/setVerified/{readerId}")
    @GET
    public Response userExists(@PathParam("readerId") Integer readerId) {
        return Response.ok().entity(readerService.setVerified(readerId)).build();
    }
    
    @Path("/isVerified/{readerId}")
    @GET
    public Response isVerified(@PathParam("readerId") Integer readerId) {
        return Response.ok().entity(readerService.isVerified(readerId)).build();
    }
    
    @Path("/getVerifyToken/{readerId}")
    @GET
    public Response getVerifyToken(@PathParam("readerId") Integer readerId) {
        return Response.ok().entity(readerService.getVerifyToken(readerId)).build();
    }
}
