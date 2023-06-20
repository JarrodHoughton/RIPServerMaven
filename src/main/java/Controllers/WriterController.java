/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import ServiceLayers.WriterService_Impl;
import ServiceLayers.WriterService_Interface;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

/**
 *
 * @author jarro
 */
@Path("/writer")
public class WriterController {
    private WriterService_Interface writerService;

    public WriterController() {
        this.writerService = new WriterService_Impl();
    }
    
    @Path("/addWriter/{readerId}")
    @GET
    public Response addWriter(@PathParam("readerId") Integer readerId) {
        return Response.ok().entity(writerService.addWriter(readerId)).build();
    }
}
