package edu.shanda.controller;

import edu.shanda.entity.Comment;
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
import java.util.List;

import static edu.shanda.controller.Redirect.showPopupAndRedirect;

@WebServlet(urlPatterns = {"/comment"})
public class CommentServlet extends HttpServlet {
    private final Logger logger = LogManager.getLogger(this.getClass());
    private final GenericDao<Story> storyDao = DaoFactory.createDao(Story.class);
    private final GenericDao<Comment> commentDao = DaoFactory.createDao(Comment.class);
    private final GenericDao<User> userDao = DaoFactory.createDao(User.class);
    //private final EmailSender emailSender = new EmailSender();


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Retrieve storyId from the request
            int storyId = Integer.parseInt(request.getParameter("storyId"));

            // Get the existing story based on storyId
            Story existingStory = storyDao.getById(storyId);

            // set the existingStory attribute to be used in jsp
            request.setAttribute("existingStory", existingStory);

            // Forward the request to the comment.jsp page
            request.getRequestDispatcher("comment.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            logger.error("Error processing the request: {}", e.getMessage(), e);
            response.sendRedirect("errorPage.jsp");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Retrieve user information from the session
            HttpSession session = request.getSession();
            String userName = (String) session.getAttribute("userName");
            logger.info("Retrieved username from session: {}", userName);

            // Check if the user is logged in
            if (userName != null) {
                int storyId = Integer.parseInt(request.getParameter("storyId"));
                String commentText = request.getParameter("comment");

                // Get the existing story based on storyId
                Story existingStory = storyDao.getById(storyId);

                // Create a new comment instance
                Comment newComment = new Comment();
                newComment.setStory(existingStory);
                newComment.setContent(commentText);

                // Find the uer by userName
                List<User> users = userDao.findByPropertyEqual("username", userName);
                logger.info("Found users with username {}: {}", userName, users);

                if (!users.isEmpty()) {
                    // if user is found set the user to the comment
                    User currentUser = users.get(0);
                    newComment.setUser(currentUser);
                    newComment.setCreationDate(LocalDateTime.now());

                    // Insert the new comment into the database
                    commentDao.insert(newComment);

                    // Show success popup and redirect to index page
                    showPopupAndRedirect(response, request, "Comment added successfully!", "index.jsp");

                } else {
                    // If user is not found, create a new user and store in the database
                    User newUser = new User();
                    String newUserName = (String) session.getAttribute("userName");
                    String userEmail = (String) session.getAttribute("userEmail");

                    newUser.setUsername(newUserName);
                    newUser.setEmail(userEmail);

                    // Set other user properties as needed
                    userDao.insert(newUser);

                    // Set the newly created user to the comment
                    newComment.setUser(newUser);
                    newComment.setCreationDate(LocalDateTime.now());
                    commentDao.insert(newComment);

                    // Show success popup and redirect to index page
                    showPopupAndRedirect(response, request, "Comment added successfully!", "displayStories");
                }
            } else {
                showPopupAndRedirect(response, request, "Please login to comment..", "logIn");
            }
        } catch (Exception e) {
            logger.error("Error processing the comment submission: {}", e.getMessage(), e);
            response.sendRedirect("errorPage.jsp");
        }
    }

}
