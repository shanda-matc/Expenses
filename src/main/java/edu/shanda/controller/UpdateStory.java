package edu.shanda.controller;

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

import static edu.shanda.controller.Redirect.showPopupAndRedirect;

@WebServlet(
        urlPatterns = {"/updateStory"}
)
public class UpdateStory extends HttpServlet {
    private final Logger logger = LogManager.getLogger(this.getClass());
    private final GenericDao<Story> storyDao = DaoFactory.createDao(Story.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Retrieve story ID from the request parameters
            int storyId = Integer.parseInt(request.getParameter("storyId"));
            logger.debug("StoryId: " + storyId);

            // Retrieve the existing story from the database
            Story existingStory = storyDao.getById(storyId);
            logger.info("Existing Story:" + existingStory);

            // set the existing story as an attribute
            request.setAttribute("existingStory", existingStory);

            // Forward the request to the updateStory.jsp page
            request.getRequestDispatcher("updateStory.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            logger.error("Invalid story ID provided: " + e.getMessage(), e);
            response.sendRedirect("error.jsp");
        } catch (Exception e) {
            logger.error("Error processing the request: " + e.getMessage(), e);
            response.sendRedirect("error.jsp");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Retrieve updated story details from the request parameters
            int storyId = Integer.parseInt(request.getParameter("storyId"));
            String updatedTitle = request.getParameter("title");
            String updatedContent = request.getParameter("content");

            // Retrieve the existing story from the database
            Story existingStory = storyDao.getById(storyId);

            // Update the story details
            existingStory.setTitle(updatedTitle);
            existingStory.setContent(updatedContent);

            // Update the story in the database
            storyDao.saveOrUpdate(existingStory);

            // Redirect to a page indicating the update was successful
            showPopupAndRedirect(response, request, "Story updated successfully!", "displayStories");
        } catch (NumberFormatException e) {
            logger.error("Invalid story ID provided: " + e.getMessage(), e);
            response.sendRedirect("error.jsp");
        } catch (Exception e) {
            logger.error("Error processing the request: " + e.getMessage(), e);
            response.sendRedirect("error.jsp");
        }
    }
}
