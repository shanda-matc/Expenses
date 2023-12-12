package edu.shanda.persistence;

import edu.shanda.entity.Category;
import edu.shanda.entity.Story;
import edu.shanda.entity.User;
import edu.shanda.test.util.Database;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StoryTest {
    private final Logger logger = LogManager.getLogger(this.getClass());
    private GenericDao<Story> storyDao;
    private GenericDao<User> userDao;
    private GenericDao<Category> categoryDao;
    private Story story;
    private Category category;
    private final Database database = new Database();

    @BeforeEach
    public void setUp() {
        database.runSQL("cleandb.sql");
        database.runSQL("createTestData.sql");

        userDao = new GenericDao<>(User.class);
        storyDao = new GenericDao<>(Story.class);
        categoryDao = new GenericDao<>(Category.class);

        story = storyDao.getAll().get(0);
        category = categoryDao.getAll().get(0);
    }

    @Test
    public void testCreate() throws Exception {
        Category category = categoryDao.getById(37);
        assertNotNull(category);

        User user = userDao.getById(20);
        assertNotNull(user);

        Story createdStory = new Story("Test Title", "Test Content", category, user, LocalDateTime.now());
        int id = storyDao.insert(createdStory);
        Story retrievedStory = storyDao.getById(id);

        logger.info("Retrieved Story: " + retrievedStory);
        logger.info("Expected Story: " + story);

        assertNotNull(retrievedStory);

        // Update the assertEquals to compare all fields
        assertEquals(createdStory, retrievedStory);
    }


    @Test
    public void testGet() throws Exception {
        assertNotNull(story);
        //logger.info("actual story:" + story);

        int storyId = story.getStoryId();
        Story retrievedStory = storyDao.getById(storyId);
        //logger.info("Retrieved story:" + retrievedStory);

        assertNotNull(retrievedStory);
        assertEquals(story, retrievedStory);
    }

    @Test
    public void testGetAll() throws Exception {
        assertNotNull(storyDao, "storyDao should not be null");
        List<Story> stories = storyDao.getAll();
        assertTrue(stories.size() > 0);
    }

    @Test
    public void testUpdate() throws Exception {
        assertNotNull(story);
        story.setTitle("Updated Title");
        story.setContent("Updated Content");
        storyDao.saveOrUpdate(story);

        Story updatedStory = storyDao.getById(story.getStoryId());
        assertEquals(story, updatedStory);
    }

    @Test
    public void testDelete() throws Exception {
        assertNotNull(story);
        storyDao.delete(story);
        Story deletedStory = storyDao.getById(story.getStoryId());
        assertNull(deletedStory);
    }
}
