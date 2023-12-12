package edu.shanda.controller;

import edu.shanda.entity.Category;
import edu.shanda.entity.Comment;
import edu.shanda.entity.Story;
import edu.shanda.persistence.GenericDao;
import edu.shanda.util.DaoFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Display stories.
 */
@WebServlet(urlPatterns = {"/displayStories"})
public class DisplayStories extends HttpServlet {
    private final Logger logger = LogManager.getLogger(this.getClass());
    private final GenericDao<Story> storyDao = DaoFactory.createDao(Story.class);
    private final GenericDao<Comment> commentDao = DaoFactory.createDao(Comment.class);
    private final GenericDao<Category> categoryDao = DaoFactory.createDao(Category.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Category> categories = categoryDao.getAll();
            Map<Category, List<Story>> categoryStoriesMap = createCategoryStoriesMap(categories);

            // Set stories as an attribute
            request.setAttribute("categoryStoriesMap", categoryStoriesMap);

            // Log the contents of categoryStoriesMap
            logCategoryStoriesMap(categoryStoriesMap);

            request.getRequestDispatcher("displayStories.jsp").forward(request, response);
        } catch (Exception e) {
            handleException(response, e);
        }
    }

    private Map<Category, List<Story>> createCategoryStoriesMap(List<Category> categories) {
        Map<Category, List<Story>> categoryStoriesMap = new HashMap<>();
        logger.debug("Number of categories: " + categories.size());

        for (Category category : categories) {
            Map<String, Object> params = new HashMap<>();
            params.put("category", category);
            List<Story> stories = storyDao.findByPropertyEqual(params);
            categoryStoriesMap.put(category, stories);
            logger.debug("Category: " + category.getName() + ", Number of Stories: " + stories.size());

            // Retrieve the comments for each story
            for (Story story : stories) {
                List<Comment> comments = getCommentsForStory(story);
                story.setComments(comments);
            }
        }
        return categoryStoriesMap;
    }

    private void logCategoryStoriesMap(Map<Category, List<Story>> categoryStoriesMap) {
        for (Map.Entry<Category, List<Story>> entry : categoryStoriesMap.entrySet()) {
            Category category = entry.getKey();
            List<Story> stories = entry.getValue();
            logger.debug("Category: " + category.getName() + ", Number of Stories: " + stories.size());
        }
        logger.debug("Category Stories Map: " + categoryStoriesMap);
    }

    private void handleException(HttpServletResponse response, Exception e) throws IOException {
        logger.error("Error processing the request: " + e.getMessage(), e);
        response.sendRedirect("error.jsp");
    }

    private List<Comment> getCommentsForStory(Story story) {
        try {
            Map<String, Object> properties = new HashMap<>();
            properties.put("story", story);
            return commentDao.findByPropertyEqual(properties);
        } catch (Exception e) {
            // Log the error
            logger.error("Error retrieving comments for story ID " + story.getStoryId() + ": " + e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}
