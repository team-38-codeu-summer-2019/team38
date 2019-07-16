package com.google.codeu.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.codeu.data.Datastore;
import com.google.codeu.data.Review;
import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/**
 * Handles fetching and saving {@link Review} instances.
 */
@WebServlet("/reviews")
public class ReviewServlet extends HttpServlet {

    private Datastore datastore;

    @Override
    public void init() {
        datastore = new Datastore();
    }

    /**
     * Responds with a JSON representation of {@link Review} data for a specific merchant. Responds with an empty array
     * if the merchant is not provided.
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");

        String merchant = request.getParameter("merchant");

        if (merchant == null || merchant.equals("")) {
            // Request is invalid, return empty array
            response.getWriter().println("[]");
            return;
        }

        List<Review> reviews = datastore.getReviews(merchant);
        Gson gson = new Gson();
        String json = gson.toJson(reviews);

        response.getWriter().println(json);
    }

    /**
     * Stores a new {@link Review}.
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        UserService userService = UserServiceFactory.getUserService();
        if (!userService.isUserLoggedIn()) {
            response.sendRedirect("/index.html");
            return;
        }

        String user = userService.getCurrentUser().getEmail();
        String merchant = request.getParameter("merchant");
        String text = Jsoup.clean(request.getParameter("text"), Whitelist.none());
        int rating = Integer.parseInt(request.getParameter("rating"));


        String regex = "(https?://\\S+\\.(png|jpg))";
        String replacement = "<img src=\"$1\" />";
        String textWithImagesReplaced = text.replaceAll(regex, replacement);

        Review review = new Review(user, merchant, textWithImagesReplaced, rating);
        datastore.storeReview(review);

        // TODO: change this to info page of a merchant.
        response.sendRedirect("/review.html?merchant=" + merchant);
    }
}
