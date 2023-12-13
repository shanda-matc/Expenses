package edu.shanda.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(
        urlPatterns = {"/logIn"}
)

/** Begins the authentication process using AWS Cognito
 *
 */
public class LogIn extends HttpServlet {
    private final Logger logger = LogManager.getLogger(this.getClass());
    @Override
    public void init() throws ServletException {
        super.init();
    }
    /**
     * Route to the aws-hosted cognito login page.
     * @param req servlet request
     * @param resp servlet response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String CLIENT_ID = (String) getServletContext().getAttribute("CLIENT_ID");
        String LOGIN_URL = (String) getServletContext().getAttribute("LOGIN_URL");
        String REDIRECT_URL = (String) getServletContext().getAttribute("REDIRECT_URL");

        // if properties weren't loaded properly, route to an error page
        if (LOGIN_URL == null || CLIENT_ID == null || REDIRECT_URL == null) {
            resp.sendRedirect("errorPage.jsp");
        } else {
            String url = LOGIN_URL + "?response_type=code&client_id=" + CLIENT_ID + "&redirect_uri=" + REDIRECT_URL;
            //https://scribble-amplify-kanck.auth.us-east-2.amazoncognito.com/login?client_id=2tsojp2s768rkn45j8m2514p7b&response_type=code&scope=email+openid+phone&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2FScribbleAmplifyKnack_war%2Fauth%2Chttps%3A%2F%2Fscribbleamplifyknack-env.eba-bjetyabz.us-east-2.elasticbeanstalk.com%2Fauth
            resp.sendRedirect(url);
            logger.info("Constructed URL: {}", url);
        }
    }
}
