/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.util.List;

/**
 *
 * @author 27713
 */
public class Story {
    private Integer id;
    private String title;
    private Integer authorId;
    private String blurb;    
    private Byte[] image;
    private String imageName;
    private String content;
    private Integer likeCount;
    private Integer viewCount;
    private Double rating;    
    private Boolean submitted;
    private Boolean approved;
    private Boolean rejected;
    private Boolean commentsEnabled;
    private List<Integer> genreIds;

    public Story() {
        this.likeCount = 0;
        this.viewCount = 0;
        this.rating = 0.0;
        this.submitted = false;
        this.approved = false;
        this.rejected = false;
        this.commentsEnabled = false;
        this.imageName = "jpeg";
    }

    public Story(Integer id, String title, Integer authorId, String blurb, Byte[] image, String imageName, String content, Integer likeCount, Integer viewCount, Double rating, Boolean submitted, Boolean approved, Boolean rejected, Boolean commentsEnabled, List<Integer> genreIds) {
        this.id = id;
        this.title = title;
        this.authorId = authorId;
        this.blurb = blurb;
        this.image = image;
        this.imageName = imageName;
        this.content = content;
        this.likeCount = likeCount;
        this.viewCount = viewCount;
        this.rating = rating;
        this.submitted = submitted;
        this.approved = approved;
        this.rejected = rejected;
        this.commentsEnabled = commentsEnabled;
        this.genreIds = genreIds;
    }

    

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public String getBlurb() {
        return blurb;
    }

    public void setBlurb(String blurb) {
        this.blurb = blurb;
    }

    public Byte[] getImage() {
        return image;
    }

    public void setImage(Byte[] image) {
        this.image = image;
    }

 

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
    
    public Boolean getSubmitted() {
        return submitted;
    }

    public void setSubmitted(Boolean submitted) {
        this.submitted = submitted;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public Boolean getRejected() {
        return rejected;
    }

    public void setRejected(Boolean rejected) {
        this.rejected = rejected;
    }

    public Boolean getCommentsEnabled() {
        return commentsEnabled;
    }

    public void setCommentsEnabled(Boolean commentsEnabled) {
        this.commentsEnabled = commentsEnabled;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }
    
    @Override
    public String toString() {
        return "Story{" + "id=" + id + ", title=" + title + ", authorId=" + authorId + ", blurb=" + blurb + ", image=" + image + ", imageName=" + imageName + ", content=" + content + ", likeCount=" + likeCount + ", viewCount=" + viewCount + ", rating=" + rating + ", submitted=" + submitted + ", approved=" + approved + ", rejected=" + rejected + ", commentsEnabled=" + commentsEnabled + ", genreIds=" + genreIds + '}';
    }
}
