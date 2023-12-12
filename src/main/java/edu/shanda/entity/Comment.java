package edu.shanda.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * The type Comment.
 */
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private int commentId;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "story_id")
    private Story story;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    /**
     * Instantiates a new Comment.
     */
    public Comment() {
    }


    /**
     * Instantiates a new Comment.
     *
     * @param commentId    the comment id
     * @param content      the content
     * @param story        the story
     * @param user         the user
     * @param creationDate the creation date
     */
    public Comment(int commentId, String content, Story story, User user, LocalDateTime creationDate) {
        this.commentId = commentId;
        this.content = content;
        this.story = story;
        this.user = user;
        this.creationDate = creationDate;
    }

    // Getters and setters

    /**
     * Gets comment id.
     *
     * @return the comment id
     */
    public int getCommentId() {
        return commentId;
    }


    /**
     * Sets comment id.
     *
     * @param commentId the comment id
     */
    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    /**
     * Gets content.
     *
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets content.
     *
     * @param content the content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Gets story.
     *
     * @return the story
     */
    public Story getStory() {
        return story;
    }

    /**
     * Sets story.
     *
     * @param story the story
     */
    public void setStory(Story story) {
        this.story = story;
    }

    /**
     * Gets user.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets user.
     *
     * @param user the user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets creation date.
     *
     * @return the creation date
     */
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * Sets creation date.
     *
     * @param creationDate the creation date
     */
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", user=" + user +
                ", content='" + content + '\'' +
                ", creationDate=" + creationDate +
                ", story=" + story.getTitle() +
                '}';
    }

}
