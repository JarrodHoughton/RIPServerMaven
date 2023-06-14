/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import Models.Comment;
import java.util.List;

/**
 *
 * @author 27713
 */
public interface CommentDao_Interface {
    public List<Comment> getAllCommentForStory(Integer storyId);
}
