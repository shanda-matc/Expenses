package edu.shanda.controller;

import edu.shanda.util.PropertiesLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.util.Properties;
import javax.servlet.ServletContext;

/**
 * The type Startup servlet.
 */
@WebServlet(
        name = "applicationStartup",
        urlPatterns = {"/startup"},
        loadOnStartup = 1
)

public class StartupServlet extends HttpServlet implements PropertiesLoader {
    /**
     * The Properties.
     */
    Properties properties;
    private final Logger logger = LogManager.getLogger(this.getClass());
    public static String CLIENT_ID;
    public static String LOGIN_URL;
    public static String REDIRECT_URL;
    public static String CLIENT_SECRET;
    public static String POOL_ID;
    public static String REGION;
    public static String OAUTH_URL;

    @Override
    public void init() throws ServletException {
        super.init();
        loadProperties();
        ServletContext servletContext = getServletContext();

        // Set the attributes in the servlet context
        getServletContext().setAttribute("CLIENT_ID", CLIENT_ID);
        getServletContext().setAttribute("LOGIN_URL", LOGIN_URL);
        getServletContext().setAttribute("REDIRECT_URL", REDIRECT_URL);
        getServletContext().setAttribute("POOL_ID", POOL_ID);
        getServletContext().setAttribute("OAUTH_URL", OAUTH_URL);
        getServletContext().setAttribute("CLIENT_SECRET", CLIENT_SECRET);
        getServletContext().setAttribute("REGION", REGION);
    }

    /**
     * Read in the cognito props file and get the client id and required urls
     * for authenticating a user.
     */
    private void loadProperties() {
        try {
            properties = loadProperties("/cognito.properties");
            //logger.info(properties);

            CLIENT_ID = properties.getProperty("client.id");
            //logger.info("client id:" + CLIENT_ID);
            LOGIN_URL = properties.getProperty("loginURL");

            REDIRECT_URL = properties.getProperty("redirectURL");
            CLIENT_SECRET = properties.getProperty("client.secret");
            OAUTH_URL = properties.getProperty("oauthURL");
            REGION = properties.getProperty("region");
            logger.info("region:" + REGION);
            POOL_ID = properties.getProperty("poolId");
        } catch (IOException ioException) {
            logger.error("Cannot load properties..." + ioException.getMessage(), ioException);
        } catch (Exception e) {
            logger.error("Error loading properties" + e.getMessage(), e);
        }
    }
}


