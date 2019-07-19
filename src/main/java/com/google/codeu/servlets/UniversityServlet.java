package com.google.codeu.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.codeu.data.Datastore;
import com.google.codeu.data.University;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

/**
 * Handles fetching and saving university data.
 */
@WebServlet("/university")
public class UniversityServlet extends HttpServlet {

    private Datastore datastore;

    @Override
    public void init() {
        datastore = new Datastore();
    }

    /**
     * Responds with a list of supported universities.
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");

        List<University> universities = datastore.getUniversities();

        JsonArray jsonArray = new Gson().toJsonTree(universities).getAsJsonArray();

        response.getWriter().println(jsonArray.toString());
    }
}
