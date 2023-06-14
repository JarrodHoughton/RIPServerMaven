/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author 27713
 */
public class Story {
    private Integer id;
    private String title;
    private Integer authorId;
    private String blurb;    
    private String image;
    private String content;
    private Integer likeCount;
    private Integer viewCount;
    private Double rating;    
    private Boolean isSubmitted;
    private Boolean isApproved;
    private Boolean commentsEnabled;

    public Story() {
    }

    public Story(Integer id, String title, String blurb, String image, String content, Integer authorId, Integer likeCount, Integer viewCount, Double rating, Boolean isSubmitted, Boolean isApproved, Boolean commentsEnabled) {
        this.id = id;
        this.title = title;
        this.authorId = authorId;
        this.blurb = blurb;
        this.image = image;
        this.content = content;
        this.likeCount = likeCount;
        this.viewCount = viewCount;
        this.rating = rating;
        this.isSubmitted = isSubmitted;
        this.isApproved = isApproved;
        this.commentsEnabled = commentsEnabled;
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

    public String getImage() {
        return image;
    }

    public void setImage(String imageName) {
        this.image = imageName;
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

    public Boolean getIsSubmitted() {
        return isSubmitted;
    }

    public void setIsSubmitted(Boolean isSubmitted) {
        this.isSubmitted = isSubmitted;
    }

    public Boolean getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }

    public Boolean getCommentsEnabled() {
        return commentsEnabled;
    }

    public void setCommentsEnabled(Boolean commentsEnabled) {
        this.commentsEnabled = commentsEnabled;
    }

    @Override
    public String toString() {
        return "Story{" + "id=" + id + ", title=" + title + ", authorId=" + authorId + ", blurb=" + blurb + ", imageName=" + image + ", content=" + content + ", likeCount=" + likeCount + ", viewCount=" + viewCount + ", rating=" + rating + ", isSubmitted=" + isSubmitted + ", isApproved=" + isApproved + ", commentsEnabled=" + commentsEnabled + '}';
    }

    
    
}
