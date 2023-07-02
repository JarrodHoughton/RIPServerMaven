/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

/**
 *
 * @author Kylynn van der Merwe
 */
import Models.Comment;
import ServiceLayers.CommentService_Impl;
import ServiceLayers.CommentService_Interface;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/comment")
public class CommentController {
    private final CommentService_Interface commentService;
    
    public CommentController(){
        this.commentService = new CommentService_Impl();
    }
    
    @Path("/getAllComments/{storyId}")
    @GET
    public Response getAllCommentForStory(@PathParam("storyId")Integer storyId){
        return Response.ok().entity(commentService.getAllCommentForStory(storyId)).build();
    }
    
    @Path("/addComment")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addComment(Comment comment){
        return Response.ok().entity(commentService.addComment(comment)).build();
    }
    
    @Path("/getCommentMessage/{commentId}")
    @GET
    public Response getCommentMessage(@PathParam("commentId")Integer commentId){
        return Response.ok().entity(commentService.getCommentMessage(commentId)).build();
    }
    
    @Path("/getCommentById/{commentId}")
    @GET
    public Response getCommentById(@PathParam("commentId")Integer commentId){
        return Response.ok().entity(commentService.getCommentById(commentId)).build();
    }
}
