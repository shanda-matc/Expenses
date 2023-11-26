package edu.shanda.entity;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * The type Story.
 */
@Entity
@Table(name = "stories")
public class Story {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "story_id")
    private int storyId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    @Column(name = "publicationDate")
    private LocalDateTime publicationDate;

    /**
     * Instantiates a new Story.
     */
    public Story() {
    }

    /**
     * Instantiates a new Story.
     *
     * @param title        the title
     * @param content      the content
     * @param category     the category
     * @param author       the author
     * @param creationDate the creation date
     */
    public Story(String title, String content, Category category, User author, LocalDateTime creationDate) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.author = author;
        this.publicationDate = creationDate;
    }

    // Getters and setters

    /**
     * Gets story id.
     *
     * @return the story id
     */
    public int getStoryId() {
        return storyId;
    }

    /**
     * Sets story id.
     *
     * @param storyId the story id
     */
    public void setStoryId(int storyId) {
        this.storyId = storyId;
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
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
     * Gets category.
     *
     * @return the category
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Sets category.
     *
     * @param category the category
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * Gets author.
     *
     * @return the author
     */
    public User getAuthor() {
        return author;
    }

    /**
     * Sets author.
     *
     * @param author the author
     */
    public void setAuthor(User author) {
        this.author = author;
    }

    /**
     * Gets creation date.
     *
     * @return the creation date
     */
    public LocalDateTime getPublicationDate() {
        return publicationDate;
    }

    /**
     * Sets creation date.
     *
     * @param publicationDate the creation date
     */
    public void setPublicationDate(LocalDateTime publicationDate) {
        this.publicationDate = publicationDate;
    }
}
