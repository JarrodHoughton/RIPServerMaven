/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServiceLayers;

import DAOs.CommentDao_Impl;
import DAOs.CommentDao_Interface;
import Models.Comment;
import java.util.List;

/**
 *
 * @author 27713
 */
public class CommentService_Impl implements CommentService_Interface {
    private final CommentDao_Interface commentDao;

    public CommentService_Impl() {
        this.commentDao = new CommentDao_Impl();
    }

    @Override
    public List<Comment> getAllCommentForStory(Integer storyId) {
        return commentDao.getAllCommentForStory(storyId);
    }

    @Override
    public Comment getCommentById(Integer commentId) {
        return commentDao.getCommentById(commentId);
    }

    @Override
    public String getCommentMessage(Integer commentId) {
        return commentDao.getCommentMessage(commentId);
    }

    @Override
    public String addComment(Comment comment) {
        if(commentDao.addComment(comment)){
            return "Comment has been added to the story";
        }else{
            return "System failed to add comment to story";
        }
    }
    
}
