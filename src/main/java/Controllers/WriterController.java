/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import Models.Writer;
import ServiceLayers.WriterService_Impl;
import ServiceLayers.WriterService_Interface;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

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
    
    @Path("/getWriterByEmail/{accountEmail}")
    @GET
    public Response getWriterByEmail(@PathParam("accountEmail") String accountEmail) {
        return Response.ok().entity(writerService.getWriterByEmail(accountEmail)).build();
    }
    
    @Path("/getWriterById/{writerId}")
    @GET
    public Response getWriterById(@PathParam("writerId") Integer writerId) {
        return Response.ok().entity(writerService.getWriter(writerId)).build();
    }
    
    @Path("/getWriters")
    @GET
    public Response getWriters(
            @QueryParam("numberOfWriters") Integer numberOfWriters,
            @QueryParam("pageNumber") Integer pageNumber
            
    ) {
        return Response.ok().entity(writerService.getWriters(numberOfWriters, pageNumber)).build();
    }
    
    @Path("/searchForWriters")
    @GET
    public Response searchForWriters(
            @QueryParam("searchValue") String searchValue,
            @QueryParam("numberOfWriters") Integer numberOfWriters,
            @QueryParam("pageNumber") Integer pageNumber
            ) {
        return Response.ok().entity(writerService.searchForWriters(searchValue , numberOfWriters, pageNumber)).build();
    }
    
    @Path("/updateWriter")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateWriter(Writer writer) {
        return Response.ok().entity(writerService.updateWriter(writer)).build();
    }
    
    @Path("/blockWriters")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response blockWriters(List<Integer> writerIds) {
        return Response.ok().entity(writerService.blockWriters(writerIds)).build();
    }
    
    @Path("/addWriters")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addWriters(List<Integer> writerIds) {
        return Response.ok().entity(writerService.addWriters(writerIds)).build();
    }
}
