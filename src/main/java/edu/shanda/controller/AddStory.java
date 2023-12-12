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
            // Check if the user is authenticated
            HttpSession session = request.getSession();
            if (!authenticateUser(session)) {
                showLoginPopupAndRedirect(response);
                return;
            }

            // Retrieve story details from the request
            String title = request.getParameter("title");
            String content = request.getParameter("content");
            String categoryValue = request.getParameter("category");

            // Retrieve the author from the session
            String authorName = (String) session.getAttribute("userName");
            String authorEmail = (String) session.getAttribute("userEmail");

            // Validate input parameters
            if (title == null || content == null || categoryValue == null || authorName == null || authorEmail == null) {
                logger.error("Validation Error: One or more parameters are null");
                response.sendRedirect("error.jsp");
                return;
            }

            // Retrieve the existing user or save the new user to the database
            User author = getOrCreateUser(authorName, authorEmail);

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

            request.setAttribute("storyAddedSuccessfully", true);
            request.getRequestDispatcher("addStory.jsp").forward(request, response);
        } catch (Exception e) {
            logger.error("Error processing the request: " + e.getMessage(), e);
            response.sendRedirect("error.jsp");
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

    private boolean authenticateUser(HttpSession session) {
        // Check if the user is authenticated
        return session.getAttribute("userName") != null && session.getAttribute("userEmail") != null;
    }

    private void showLoginPopupAndRedirect(HttpServletResponse response) throws IOException {
        // Use JavaScript to show a popup and redirect
        String script = "alert('You need to log in to write a story.');";
        script += "window.location.href='logIn';";
        response.getWriter().write("<script>" + script + "</script>");
    }
}
