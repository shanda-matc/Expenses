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

@WebServlet(urlPatterns = {"/deleteStory"})

public class DeleteStory extends HttpServlet {
    private final Logger logger = LogManager.getLogger(this.getClass());
    private final GenericDao<Story> storyDao = DaoFactory.createDao(Story.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Retrieve the story Id from request parameter
            int storyId = Integer.parseInt(request.getParameter("storyId"));

            // Retrieve the existing story from the database
            Story existingStory = storyDao.getById(storyId);

            // check if story with given id is null
            if (existingStory == null) {
                logger.error("Error: Story with ID " + storyId + " not found");
                response.sendRedirect("error.jsp");
                return;
            }

            // Delete the story from the database
            storyDao.delete(existingStory);

            // show success pop up message and redirect to displayStories.jsp
            showPopupAndRedirect(response, request, "story deleted successfully!", "displayStories");

        } catch (Exception e) {
            logger.error("Error processing the request: " + e.getMessage(), e);
            response.sendRedirect("error.jsp");
        }
    }
}
