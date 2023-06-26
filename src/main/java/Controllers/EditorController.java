/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import Models.Editor;
import Models.Story;
import ServiceLayers.EditorService_Impl;
import ServiceLayers.EditorService_Interface;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


/**
 *
 * @author Jarrod
 */
@Path("/editor")
public class EditorController {
    private EditorService_Interface editorService;


    public EditorController() {
        this.editorService = new EditorService_Impl();
    }
    
    
    @Path("/registerEditor")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addEditor(Editor editor) {
        return Response.ok().entity(editorService.addEditor(editor)).build();
    }
    
    @Path("/updateEditor")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateEditor(Editor editor) {
        return Response.ok().entity(editorService.updateEditor(editor)).build();
    }
    
    @Path("/deleteEditor/{editorId}")
    @GET
    public Response addApplication(@PathParam("editorId") Integer editorId) {
        return Response.ok().entity(editorService.deleteEditor(editorId)).build();
    }
    
    @Path("/searchForEditor/{accountEmail}")
    @GET
    public Response addApplication(@PathParam("accountEmail") String accountEmail) {
        return Response.ok().entity(editorService.searchForEditor(accountEmail)).build();
    }
    
    @Path("/getEditorByEmail/{accountEmail}")
    @GET
    public Response getEditor(@PathParam("accountEmail") String accountEmail) {
        return Response.ok().entity(editorService.getEditorByEmail(accountEmail)).build();
    }
    
    @Path("/getEditorById/{editorId}")
    @GET
    public Response getEditor(@PathParam("editorId") Integer editorId) {
        return Response.ok().entity(editorService.getEditor(editorId)).build();
    }
    
    @Path("/getAllEditors")
    @GET
    public Response getAllEditors() {
        return Response.ok().entity(editorService.getAllEditors()).build();
    }
}
