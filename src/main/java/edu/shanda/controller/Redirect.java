package edu.shanda.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Redirect {
    /**
     * Shows an alert popup with a message and redirects to the specified page.
     *
     * @param response      the HttpServletResponse object for sending the response to the client
     * @param request       the HttpServletRequest object representing the request made by the client
     * @param message       the message to be displayed in the alert popup
     * @param redirectPage  the page to redirect to after displaying the popup
     * @throws IOException  an exception thrown if an I/O error occurs
     */
    public static void showPopupAndRedirect(HttpServletResponse response, HttpServletRequest request, String message, String redirectPage) throws IOException {
        String script = "alert('" + message + "');";
        script += "window.location.href='" + response.encodeRedirectURL(request.getContextPath() + "/" + redirectPage) + "';";
        response.getWriter().write("<script>" + script + "</script>");
    }
}
