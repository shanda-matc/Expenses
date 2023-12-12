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

@WebServlet(
        urlPatterns = {"/comment"}
)
public class CommentServlet extends HttpServlet {
    private final Logger logger = LogManager.getLogger(this.getClass());
    private final GenericDao<Story> storyDao = DaoFactory.createDao(Story.class);
    private final GenericDao<Comment> commentDao = DaoFactory.createDao(Comment.class);

    private final GenericDao<User> userDao = DaoFactory.createDao(User.class);
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Retrieve information about the story
            int storyId = Integer.parseInt(request.getParameter("storyId"));

            // Fetch the existing story from the database
            Story existingStory = storyDao.getById(storyId);

            // Set the existing story as an attribute
            request.setAttribute("existingStory", existingStory);

            // Forward the request to the comment.jsp page
            request.getRequestDispatcher("comment.jsp").forward(request, response);
        } catch (Exception e) {
            logger.error("Error processing the request: " + e.getMessage(), e);
            response.sendRedirect("errorPage.jsp");
        }
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Check if the user is authenticated
            HttpSession session = request.getSession();
            String userName = (String) session.getAttribute("userName");

            if (userName != null) {
                // Retrieve information about the comment
                int storyId = Integer.parseInt(request.getParameter("storyId"));
                String commentText = request.getParameter("comment");

                // Fetch the existing story from the database
                Story existingStory = storyDao.getById(storyId);

                // Create a new comment
                Comment newComment = new Comment();
                newComment.setStory(existingStory);
                newComment.setContent(commentText);

                // Fetch the user object based on the username
                List<User> users = userDao.findByPropertyEqual("username", userName);

                if (!users.isEmpty()) {
                    User currentUser = users.get(0);
                    newComment.setUser(currentUser);

                    // Set the creation date
                    newComment.setCreationDate(LocalDateTime.now());

                    // Save the comment to the database using the GenericDao
                    commentDao.insert(newComment);

                    // Send email to the writer
                    sendEmailToWriter(existingStory, newComment);

                    // Set success message in request attribute
                    request.setAttribute("commentSuccessMessage", "Comment submitted successfully!");

                    // Forward back to the comment.jsp page with the updated comments
                    request.getRequestDispatcher("comment.jsp").forward(request, response);
                } else {
                    //  if user is not found, then redirect to error page
                    logger.error("User with username {} not found.", userName);
                }
            } else {
                // User is not authenticated, forward to the login servlet
                showLoginPopupAndRedirect(response);
            }
        } catch (Exception e) {
            logger.error("Error processing the comment submission: " + e.getMessage(), e);
            response.sendRedirect("errorPage.jsp");
        }
    }

    private void sendEmailToWriter(Story existingStory, Comment newComment) {
        try {
            // Retrieve the writer of the story
            User writer = existingStory.getAuthor();

            // Check if the writer has an email
            if (writer != null && writer.getEmail() != null && !writer.getEmail().isEmpty()) {
                // Use EmailServlet method to send email
                new EmailServlet().sendEmailToWriter(writer.getEmail(), newComment.getContent());
            } else {
                logger.warn("Writer has no valid email address. Email not sent.");
            }

        } catch (Exception e) {
            logger.error("Error sending email to writer: " + e.getMessage(), e);
        }
    }
    private void showLoginPopupAndRedirect(HttpServletResponse response) throws IOException {
        // Use JavaScript to show a popup and redirect
        String script = "alert('You need to log in to write a comment on a story.');";
        script += "window.location.href='logIn';";
        response.getWriter().write("<script>" + script + "</script>");
    }
}