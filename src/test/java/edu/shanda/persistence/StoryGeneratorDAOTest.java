package edu.shanda.persistence;

import edu.shanda.storyGeneratorAPI.StoryGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

class StoryGeneratorDAOTest {
    private final Logger logger = LogManager.getLogger(this.getClass());
    StoryGeneratorDAO storyGeneratorDAO = new StoryGeneratorDAO();

    @Test
    void getStoryByTitleSuccess() throws IOException {
        String expectedTitle = "The Frog and The Ox";
        List<StoryGenerator> stories = storyGeneratorDAO.getStories();

        assertEquals("The Frog and The Ox", stories.get(0).getTitle());
        assertEquals("Belling the Cat", stories.get(1).getTitle());
    }

    @Test
    void getNumberOfStoriesSuccess() throws IOException {
        List<StoryGenerator> stories = storyGeneratorDAO.getStories();

        logger.debug(stories.toString());
        assertEquals(146, stories.size());
    }
}

