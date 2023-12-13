package edu.shanda.controller;

import edu.shanda.entity.Comment;
import edu.shanda.entity.Story;
import edu.shanda.entity.User;
import edu.shanda.persistence.GenericDao;
import edu.shanda.util.DaoFactory;
import edu.shanda.util.EmailSender;
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

@WebServlet(urlPatterns = {"/comment"})
public class CommentServlet extends HttpServlet {
    private final Logger logger = LogManager.getLogger(this.getClass());
    private final GenericDao<Story> storyDao = DaoFactory.createDao(Story.class);
    private final GenericDao<Comment> commentDao = DaoFactory.createDao(Comment.class);
    private final GenericDao<User> userDao = DaoFactory.createDao(User.class);
    private EmailSender emailSender;
    // Constructor
    public CommentServlet() {
        emailSender = new EmailSender("shilpa25", "MyGoogleGmail@2429", "smtp.gmail.com", "shilpahanda49@gmail.com");
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int storyId = Integer.parseInt(request.getParameter("storyId"));
            Story existingStory = storyDao.getById(storyId);
            request.setAttribute("existingStory", existingStory);
            request.getRequestDispatcher("comment.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            logger.error("Error processing the request: {}", e.getMessage(), e);
            response.sendRedirect("errorPage.jsp");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            String userName = (String) session.getAttribute("userName");
            logger.info("Retrieved username from session: {}", userName);

            if (userName != null) {
                int storyId = Integer.parseInt(request.getParameter("storyId"));
                String commentText = request.getParameter("comment");

                Story existingStory = storyDao.getById(storyId);
                Comment newComment = new Comment();
                newComment.setStory(existingStory);
                newComment.setContent(commentText);

                List<User> users = userDao.findByPropertyEqual("username", userName);
                logger.info("Found users with username {}: {}", userName, users);

                if (!users.isEmpty()) {
                    User currentUser = users.get(0);
                    newComment.setUser(currentUser);
                    newComment.setCreationDate(LocalDateTime.now());
                    commentDao.insert(newComment);

                    // Show success popup and redirect to index page
                    showPopupAndRedirect(response, request, "Comment added successfully!", "index.jsp");
                    sendEmailToWriter(existingStory, newComment);
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
                    showPopupAndRedirect(response, request, "Comment added successfully!", "index.jsp");

                    sendEmailToWriter(existingStory, newComment);
                }
            } else {
                showPopupAndRedirect(response, request, "Please login to comment..", "logIn");
            }
        } catch (Exception e) {
            logger.error("Error processing the comment submission: {}", e.getMessage(), e);
            response.sendRedirect("errorPage.jsp");
        }
    }

    private void sendEmailToWriter(Story existingStory, Comment newComment) {
        try {
            User writer = existingStory.getAuthor();

            if (writer != null && writer.getEmail() != null && !writer.getEmail().isEmpty()) {
                String subject = "New Comment on Your Story";
                String body = "Hello,\n\nSomeone commented on your story:\n\n" + newComment.getContent();

                // Use the instance of EmailSender to send the email
                emailSender.sendEmail(writer.getEmail(), subject, body);
            } else {
                logger.warn("Writer has no valid email address. Email not sent.");
            }
        } catch (Exception e) {
            logger.error("Error sending email to writer: {}", e.getMessage(), e);
        }
    }

    private void showPopupAndRedirect(HttpServletResponse response, HttpServletRequest request, String message, String redirectPage) throws IOException {
        String script = "alert('" + message + "');";
        script += "window.location.href='" + response.encodeRedirectURL(request.getContextPath() + "/" + redirectPage) + "';";
        response.getWriter().write("<script>" + script + "</script>");
    }

}
