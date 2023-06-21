/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import Models.Genre;
import ServiceLayers.GenreService_Impl;
import ServiceLayers.GenreService_Interface;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

/**
 *
 * @author jarro
 */
@Path("/genre")
public class GenreController {
    private GenreService_Interface genreService;

    public GenreController() {
        this.genreService = new GenreService_Impl();
    }
    
    @Path("/getGenre/{genreId}")
    @GET
    public Response getGenre(@PathParam("genreId") Integer id) {
        return Response.ok().entity(genreService.getGenre(id)).build();
    }
    
    @Path("/getAllGenres")
    @GET
    public Response getAllGenres(){
        return Response.ok().entity(genreService.getAllGenres()).build();
    }
    
    @Path("/deleteGenre/{genreId}")
    @GET
    public Response deleteGenre(@PathParam("genreId") Integer id) {
        return Response.ok().entity(genreService.deleteGenre(id)).build();
    }
    
    @Path("/addGenre")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addGenre(Genre genre) {
        return Response.ok().entity(genreService.addGenre(genre)).build();
    }
    
    @Path("/searchForGenres/{searchValue}")
    @GET
    public Response searchForGenres(@PathParam("searchValue") String searchValue) {
        return Response.ok().entity(genreService.searchForGenres(searchValue)).build();
    }
}
