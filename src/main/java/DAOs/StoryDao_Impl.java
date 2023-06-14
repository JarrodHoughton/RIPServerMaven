/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import Models.Genre;
import Models.Story;
import Utils.DBManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jarro
 */
public class StoryDao_Impl implements StoryDao_Interface{
    private Connection connection;
    private PreparedStatement prepStmt;
    private ResultSet rs;

    public StoryDao_Impl() {}
    
    @Override
    public Story getStory(Integer storyId) {
//        "SELECT S.storyId, S.title, S.blurb, I.image, S.content, S.accountId, S.likeCount, S.viewCount, S.rating, S.submitted, S.approved, S.commentsEnabled " +
//                    "FROM stories as S " +
//                    "INNER JOIN images as I " +
//                    "ON S.storyId=I.storyId " +
//                    "WHERE S.storyId = ?;"
        Story story = null;
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT * FROM stories WHERE storyId=?;");
            prepStmt.setInt(1, storyId);
            rs = prepStmt.executeQuery();
            if (rs.next()) {
                story = new Story();
                story.setId(rs.getInt("storyId"));
                story.setTitle(rs.getString("title"));
                story.setBlurb(rs.getString("blurb"));
                story.setContent(rs.getString("content"));
                story.setAuthorId(rs.getInt("accountId"));
                story.setLikeCount(rs.getInt("likeCount"));
                story.setViewCount(rs.getInt("viewCount"));
                story.setRating(rs.getDouble("rating"));
                story.setIsApproved(rs.getString("approved").charAt(0)=='T');
                story.setIsSubmitted(rs.getString("submitted").charAt(0)=='T');
                story.setCommentsEnabled(rs.getString("commentsEnabled").charAt(0)=='T');
                story.setImage("");
            }
        } catch (SQLException ex) {
            Logger.getLogger(EditorDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (prepStmt != null) {
                try {
                    prepStmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EditorDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EditorDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EditorDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return story;
    }

    @Override
    public List<Story> getAllStories() {
        List<Story> stories = new ArrayList<>();
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT * FROM stories;");
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                Story story = new Story();
                story.setId(rs.getInt("storyId"));
                story.setTitle(rs.getString("title"));
                story.setBlurb(rs.getString("blurb"));
                story.setContent(rs.getString("content"));
                story.setAuthorId(rs.getInt("accountId"));
                story.setLikeCount(rs.getInt("likeCount"));
                story.setViewCount(rs.getInt("viewCount"));
                story.setRating(rs.getDouble("rating"));
                story.setIsApproved(rs.getString("approved").charAt(0)=='T');
                story.setIsSubmitted(rs.getString("submitted").charAt(0)=='T');
                story.setCommentsEnabled(rs.getString("commentsEnabled").charAt(0)=='T');
                story.setImage("");
                stories.add(story);
            }
        } catch (SQLException ex) {
            Logger.getLogger(EditorDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (prepStmt != null) {
                try {
                    prepStmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EditorDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EditorDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EditorDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return stories;
    }

    @Override
    public List<Story> getStoriesInGenre(Integer genreId) {
        List<Story> stories = new ArrayList<>();
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("SELECT storyId FROM stories_genres WHERE genreId=?;");
            prepStmt.setInt(1, genreId);
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                stories.add(getStory(rs.getInt(1)));
            }
        } catch (SQLException ex) {
            Logger.getLogger(EditorDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (prepStmt != null) {
                try {
                    prepStmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EditorDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EditorDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EditorDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return stories;
    }

    @Override
    public Boolean updateStory(Story story) {
        Boolean updated = false;
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("UPDATE `ripdb`.`accounts` SET `title`=?, `blurb`=?, `approved`=?, `submitted`=?, `commentsEnabled`=?, `content`=? `accountId`=? `viewCount`=? `rating`=? `likeCount`=? storyId`=?;");
            prepStmt.setString(1, story.getTitle());
            prepStmt.setString(2, story.getBlurb());
            prepStmt.setString(3, story.getIsApproved() ? "T" : "F");
            prepStmt.setString(4, story.getIsSubmitted()? "T" : "F");
            prepStmt.setString(5, story.getCommentsEnabled()? "T" : "F");
            prepStmt.setString(6, story.getContent());
            prepStmt.setInt(7, story.getAuthorId());
            prepStmt.setInt(8, story.getViewCount());
            prepStmt.setInt(9, story.getLikeCount());
            prepStmt.setDouble(10, story.getRating());
            prepStmt.executeUpdate();
            updateImageString(story.getId(), story.getImage());
            updated = true;
        } catch (SQLException ex) {
            Logger.getLogger(EditorDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            if (prepStmt != null) {
                try {
                    prepStmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EditorDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EditorDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return updated;        
    }

    @Override
    public Boolean deleteStory(Integer storyId) {
        Boolean deleted = false;
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("DELETE FROM `ripdb`.`stories` WHERE `accountId`=?;");
            prepStmt.setInt(1, storyId);
            prepStmt.executeUpdate();
            deleteImage(storyId);
            deleted = true;
        } catch (SQLException ex) {
            Logger.getLogger(EditorDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            if (prepStmt != null) {
                try {
                    prepStmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EditorDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EditorDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return deleted;
    }

    @Override
    public Boolean addStory(Story story) {
        Boolean added = false;
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement(
                    "INSERT IGNORE INTO `ripdb`.`stories` (`title`, `blurb`, `approved`, `submitted`, `commentsEnabled`, `content`, `accountId`, `viewCount`, `likeCount`, `rating`) "
                            + "VALUES (?,?,?,?,?,?,?,?,?,?);"
            );
            prepStmt.setString(1, story.getTitle());
            prepStmt.setString(2, story.getBlurb());
            prepStmt.setString(3, story.getIsApproved() ? "T" : "F");
            prepStmt.setString(4, story.getIsSubmitted()? "T" : "F");
            prepStmt.setString(5, story.getCommentsEnabled()? "T" : "F");
            prepStmt.setString(6, story.getContent());
            prepStmt.setInt(7, story.getAuthorId());
            prepStmt.setInt(8, story.getViewCount());
            prepStmt.setInt(9, story.getLikeCount());
            prepStmt.setDouble(10, story.getRating());
            prepStmt.executeUpdate();
            addImageString(story.getImage());
            added = true;
        } catch (SQLException ex) {
            Logger.getLogger(EditorDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            if (prepStmt != null) {
                try {
                    prepStmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EditorDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EditorDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return added;        
    }
    
    private void addImageString(String image) {
        try {
            prepStmt = connection.prepareStatement("INSERT IGNORE INTO `ripdb`.`images` (`image`) VALUES (?);");
            prepStmt.setString(1, image);
            prepStmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (prepStmt != null) {
                try {
                    prepStmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EditorDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EditorDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    
    private void updateImageString(Integer storyId, String image) {
        try {
            prepStmt = connection.prepareStatement("UPDATE `ripdb`.`images` SET `image`=? WHERE storyId=?;");
            prepStmt.setString(1, image);
            prepStmt.setInt(2, storyId);
            prepStmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(StoryDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (prepStmt != null) {
                try {
                    prepStmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EditorDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EditorDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    private void deleteImage(Integer storyId) {
        try {
            connection = DBManager.getConnection();
            prepStmt = connection.prepareStatement("DELETE FROM `ripdb`.`images` WHERE `storyId`=?;");
            prepStmt.setInt(1, storyId);
            prepStmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(EditorDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (prepStmt != null) {
                try {
                    prepStmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EditorDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EditorDao_Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
