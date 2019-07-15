package com.google.codeu.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.codeu.data.Datastore;
import com.google.codeu.data.Message;
import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/**
 * Handles fetching all messages for the public feed.
 */
@WebServlet("/search")
public class SearchServlet extends HttpServlet{

 public void doPost(HttpServletRequest request, HttpServletResponse response)
   throws IOException {
  String merchant = Jsoup.clean(request.getParameter("merchant"), Whitelist.none());
  response.sendRedirect("/search.html?merchant=" + merchant);
 }
}