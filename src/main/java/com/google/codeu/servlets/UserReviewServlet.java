package com.google.codeu.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.codeu.data.Datastore;
import com.google.codeu.data.Merchant;
import com.google.codeu.data.Review;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Handles fetching and saving {@link Review} instances.
 */
@WebServlet("/user-reviews")
public class UserReviewServlet extends HttpServlet {

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

        String user = request.getParameter("user");
        if (user.equals("")) {
            // Request is invalid, return empty array
            response.getWriter().println("[]");
            return;
        }

        List<Review> reviews = datastore.getReviewsByUser(user);

        List<JsonObject> userReviews = new ArrayList<>();

        Gson gson = new Gson();
        JsonParser jsonParser = new JsonParser();

        for (Review review : reviews){
            String jsonString = gson.toJson(review);
            JsonObject jsonObject = (JsonObject)jsonParser.parse(jsonString);
            String merchantID = review.getMerchantID().toString();
            Merchant merchant = datastore.getMerchant(merchantID);
            jsonObject.addProperty("merchantName", merchant.getName());
            userReviews.add(jsonObject);
        }

        JsonArray jsonArray = gson.toJsonTree(userReviews).getAsJsonArray();

        response.getWriter().println(jsonArray.toString());
    }
}
