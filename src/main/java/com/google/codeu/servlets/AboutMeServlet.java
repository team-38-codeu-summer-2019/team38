package com.google.codeu.servlets;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.codeu.data.Datastore;
import com.google.codeu.data.User;
import com.google.gson.JsonObject;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/**
 * Handles fetching and saving user data.
 */
@WebServlet("/about")
public class AboutMeServlet extends HttpServlet {

    private Datastore datastore;

    @Override
    public void init() {
        datastore = new Datastore();
    }

    /**
     * Responds with the "about me" section for a particular user.
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/html");

        String user = request.getParameter("user");

        if (user == null || user.equals("")) {
            // Request is invalid, return empty response
            return;
        }

        User userData = datastore.getUser(user);

        if (userData == null || userData.getUniversity() == null && userData.getAboutMe() == null) {
            return;
        }

        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("university", userData.getUniversity());
        jsonObject.addProperty("aboutMe", userData.getAboutMe());

        response.setContentType("application/json");
        response.getWriter().println(jsonObject.toString());
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        UserService userService = UserServiceFactory.getUserService();
        if (!userService.isUserLoggedIn()) {
            response.sendRedirect("/index.html");
            return;
        }

        String userEmail = userService.getCurrentUser().getEmail();
        String university = Jsoup.clean(request.getParameter("university"), Whitelist.none());
        String aboutMe = Jsoup.clean(request.getParameter("about-me"), Whitelist.none());

        User user = new User(userEmail, university, aboutMe);
        datastore.storeUser(user);

        response.sendRedirect("/user-page.html?user=" + userEmail);
    }
}