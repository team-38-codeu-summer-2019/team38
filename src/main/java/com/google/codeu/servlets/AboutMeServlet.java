package com.google.codeu.servlets;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.codeu.data.Datastore;
import com.google.codeu.data.University;
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

        response.setContentType("application/json");

        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("universityID", -1);
        jsonObject.addProperty("universityName", "");
        jsonObject.addProperty("aboutMe", "");

        String user = request.getParameter("user");

        if (user == null || user.equals("")) {
            // Request is invalid, return empty response
            response.getWriter().println(jsonObject.toString());
            return;
        }

        User userData = datastore.getUser(user);

        if (userData == null || userData.getUniversity() == -1 && userData.getAboutMe() == null) {
            response.getWriter().println(jsonObject.toString());
            return;
        }

        long universityID = userData.getUniversity();
        University university = datastore.getUniversity(universityID);
        String universityName = university.getName();
        String aboutMe = userData.getAboutMe();

        jsonObject.addProperty("universityID", universityID);
        jsonObject.addProperty("universityName", universityName);
        jsonObject.addProperty("aboutMe", aboutMe);

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
        long university = Long.parseLong(request.getParameter("university"));
        String aboutMe = Jsoup.clean(request.getParameter("about-me"), Whitelist.none());

        User user = new User(userEmail, university, aboutMe);
        datastore.storeUser(user);

        /* to add University to Datastore
        University a = new University(1, "National University of Singapore");
        University b = new University(2, "Universitas Indonesia");
        University c = new University(3, "The University of Hongkong");
        datastore.storeUniversity(a);
        datastore.storeUniversity(b);
        datastore.storeUniversity(c);*/

        response.sendRedirect("/user-page.html?user=" + userEmail);
    }
}