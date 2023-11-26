package edu.shanda.controller;

import edu.shanda.entity.Category;
import edu.shanda.entity.Story;
import edu.shanda.entity.User;
import edu.shanda.persistence.GenericDao;
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
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Add story.
 */
@WebServlet(urlPatterns = {"/addStory"})
public class AddStory extends HttpServlet {
    private final Logger logger = LogManager.getLogger(this.getClass());
    private final GenericDao<Story> storyDao = DaoFactory.createDao(Story.class);
    private final GenericDao<Category> categoryDao = DaoFactory.createDao(Category.class);
    private final GenericDao<User> userDao = DaoFactory.createDao(User.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Retrieve the username from the session
            HttpSession session = request.getSession();
            String authorName = (String) session.getAttribute("userName");
            String authorEmail = (String) session.getAttribute("userEmail");

            logger.debug("Author Name: " + authorName);
            logger.debug("Author Email: " + authorEmail);

            if (authorName == null || authorName.isEmpty() || authorEmail == null || authorEmail.isEmpty()) {
                logger.error("Username or Email is null or empty");
                response.sendRedirect("error.jsp");
                return;
            }

            // Use the retrieved information directly
            User author = new User();
            author.setUsername(authorName);
            author.setEmail(authorEmail);

           logger.debug("Author Name: " + authorName);

            // Try to find an existing user with the given username and email
            User existingUser = getUserByName(authorName, authorEmail);

            if (existingUser != null) {
                // Use the existing user found in the database
                author = existingUser;
            } else {
                // Save the new user to the database
                userDao.saveOrUpdate(author);
            }
            logger.debug("Author Name: " + authorName);

            String title = request.getParameter("title");
            String content = request.getParameter("content");
            String categoryValue = request.getParameter("category");
            Category category = getCategoryByName(categoryValue);

            // Check if Category object is found
            if (category == null) {
                logger.error("Error: Category is null");
                response.sendRedirect("error.jsp");
                return;
            }

            // Validate input parameters
            if (title == null || content == null || categoryValue == null) {
                logger.error("Validation Error: One or more parameters are null");
                response.sendRedirect("error.jsp");
                return;
            }

            LocalDateTime creationDate = LocalDateTime.now();

            // Create a new Story instance
            Story newStory = new Story();
            newStory.setTitle(title);
            newStory.setContent(content);
            newStory.setPublicationDate(creationDate);
            newStory.setCategory(category);  // Set the Category
            newStory.setAuthor(author);

            //Story newStory = new Story(title, content, category, author, creationDate);
            System.out.println("New Story: " + newStory);

            storyDao.saveOrUpdate(newStory);

            response.sendRedirect("success.jsp"); // Redirect to a success page
        } catch (Exception e) {
            logger.error("Error processing the request: " + e.getMessage(), e);
            response.sendRedirect("error.jsp");
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
}
