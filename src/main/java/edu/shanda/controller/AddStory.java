package edu.shanda.controller;

import edu.shanda.entity.Category;
import edu.shanda.storyGeneratorAPI.StoryGenerator;
import edu.shanda.entity.Story;
import edu.shanda.entity.User;
import edu.shanda.persistence.GenericDao;
import edu.shanda.persistence.StoryGeneratorDAO;
import edu.shanda.util.DaoFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static edu.shanda.controller.Redirect.showPopupAndRedirect;

/**
 * The type Add story.
 */
@WebServlet(urlPatterns = {"/addStory"})
public class AddStory extends HttpServlet {
    private final Logger logger = LogManager.getLogger(this.getClass());
    private final GenericDao<Story> storyDao = DaoFactory.createDao(Story.class);
    private final GenericDao<Category> categoryDao = DaoFactory.createDao(Category.class);
    private final GenericDao<User> userDao = DaoFactory.createDao(User.class);

    StoryGeneratorDAO storyGeneratorDAO = new StoryGeneratorDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Check if the user is authenticated
            HttpSession session = request.getSession();
            String userName = (String) session.getAttribute("userName");
            if (userName == null) {
                showPopupAndRedirect(response, request, "Please login to write a story..", "logIn");
                return;
            }

            // Retrieve story details from the request
            String title = request.getParameter("title");
            String contentSource = request.getParameter("contentSource");
            String categoryValue = request.getParameter("category");

            // Retrieve the author from the session
            String authorName = (String) session.getAttribute("userName");
            String authorEmail = (String) session.getAttribute("userEmail");

            // Validate input parameters
            if (title == null || contentSource == null || categoryValue == null || authorName == null || authorEmail == null) {
                logger.error("Validation Error: One or more parameters are null");
                response.sendRedirect("error.jsp");
                return;
            }

            // Determine the content based on the content source
            String content = determineContent(contentSource, title, request, response);
            if (content == null) {
                return;
            }

            // Retrieve the existing user or save the new user to the database
            User author = getOrCreateUser(authorName, authorEmail);

            // Fetch story content from the API based on the title
            String generatedContent = fetchStoryContentFromAPI(title);
            logger.debug("API Response: {}", generatedContent);

            logger.debug("API Response: {}", generatedContent);

            // Check if the API response contains story content
            if (generatedContent != null && !generatedContent.isEmpty()){
                content = String.join(" ", generatedContent);
            } else {
                logger.warn("API response is empty.");
            }
            Category category = getCategoryByName(categoryValue);
            // Check if Category is found
            if (category == null) {
                logger.error("Error: Category is null");
                response.sendRedirect("error.jsp");
                return;
            }

            LocalDateTime publicationDate = LocalDateTime.now();

            // Create a new Story instance
            Story newStory = new Story(title, content, category, author, publicationDate);
            storyDao.saveOrUpdate(newStory);
            logger.info("Story added successfully. Title: {}", title);

            // Show success popup and redirect to addStory.jsp page
            showPopupAndRedirect(response, request, "story added successfully!", "addStory.jsp");
            //request.getRequestDispatcher("addStory.jsp").forward(request, response);
        } catch (Exception e) {
            logger.error("Error processing the request: " + e.getMessage(), e);
            response.sendRedirect("error.jsp");
        }
    }

    private String fetchStoryContentFromAPI(String title) {
        try {
            List<StoryGenerator> generatedStory = storyGeneratorDAO.getStories();

            if (generatedStory != null && !generatedStory.isEmpty()) {
                for (StoryGenerator response : generatedStory) {
                    if (response != null && title.equals(response.getTitle()) && response.getStory() != null) {
                        return response.getStory();
                    }
                }
                logger.warn("No matching story found for the title: {}", title);
            } else {
                logger.warn("API response is empty.");
            }
            return null;
        } catch (IOException e) {
            logger.error("Error fetching story content from the API: " + e.getMessage(), e);
            return null;
        }
    }

    private User getOrCreateUser(String name, String email) {
        User existingUser = getUserByName(name, email);

        if (existingUser != null) {
            // Use the existing user found in the database
            return existingUser;
        } else {
            // Save the new user to the database
            User newUser = new User();
            newUser.setUsername(name);
            newUser.setEmail(email);
            userDao.saveOrUpdate(newUser);
            return newUser;
        }
    }

    private Category getCategoryByName(String name) {
        Map<String, Object> params = new HashMap<>();
        params.put("categoryName", name);
        List<Category> categories = categoryDao.findByPropertyEqual(params);

        if (!categories.isEmpty()) {
            return categories.get(0);
        } else {
            return null;
        }
    }

    private User getUserByName(String name, String email) {
        Map<String, Object> params = new HashMap<>();
        params.put("username", name);
        params.put("email", email);
        List<User> users = userDao.findByPropertyEqual(params);

        if (!users.isEmpty()) {
            return users.get(0);
        } else {
            return null;
        }
    }

    private String determineContent(String contentSource, String title, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            if ("api".equals(contentSource)) {
                // Fetch content from the API
                String generatedContent = fetchStoryContentFromAPI(title);
                logger.debug("API Response: {}", generatedContent);

                if (!generatedContent.isEmpty()) {
                    // Set the content to the first response's story
                    // Concatenate all story contents into one string
                    return String.join(" ", generatedContent);
                } else {
                    logger.warn("API response is empty.");
                }

            } else if ("user".equals(contentSource)) {
                // Retrieve user-written content from the request
                String userContent = request.getParameter("userContent");

                // Validate user-written content
                if (userContent == null || userContent.trim().isEmpty()) {
                    logger.error("Validation Error: User-written content is null or empty");
                    response.sendRedirect("error.jsp?message=Invalid user content");
                    return null;
                }

                // Use user-written content
                return userContent;
            }

            // If the content source is neither "api" nor "user"
            logger.error("Invalid content source: {}", contentSource);
            response.sendRedirect("error.jsp?message=Invalid content source");
            return null;
        } catch (Exception e) {
            logger.error("Error in determineContent: " + e.getMessage(), e);
            response.sendRedirect("error.jsp?message=Error in determineContent");
            return null;
        }
    }
}
