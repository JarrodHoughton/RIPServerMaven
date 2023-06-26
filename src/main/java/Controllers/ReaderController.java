/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import Models.Reader;
import ServiceLayers.ReaderService_Impl;
import ServiceLayers.ReaderService_Interface;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
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
    
    @Path("/getReader/{accountEmail}")
    @GET
    public Response getReader(@PathParam("accountEmail") String accountEmail) {
        return Response.ok().entity(readerService.getReader(accountEmail)).build();
    }
    
    @Path("/getReaderById/{readerId}")
    @GET
    public Response getReaderById(@PathParam("readerId") Integer readerId) {
        return Response.ok().entity(readerService.getReader(readerId)).build();
    }
    
    @Path("/updateReaderDetails")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateReaderDetails(Reader reader){
        return Response.ok().entity(readerService.updateReader(reader)).build();
    }
}
