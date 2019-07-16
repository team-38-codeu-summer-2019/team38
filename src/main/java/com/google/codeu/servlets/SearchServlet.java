package com.google.codeu.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.codeu.data.Datastore;
import com.google.codeu.data.Merchant;
import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/**
 * Handles fetching all merchants for the merchant feed.
 */
@WebServlet("/search")
public class SearchServlet extends HttpServlet{

 private Datastore datastore;

 @Override
 public void init() {
  datastore = new Datastore();
 }

 public void doPost(HttpServletRequest request, HttpServletResponse response)
   throws IOException {
  String merchant = Jsoup.clean(request.getParameter("merchant"), Whitelist.none());
  response.sendRedirect("/search.html?merchant=" + merchant);
 }

 public void doGet(HttpServletRequest request, HttpServletResponse response)
   throws IOException {

  response.setContentType("application/json");
  
  List<Merchant> merchants = datastore.getAllMerchants();
  Gson gson = new Gson();
  String json = gson.toJson(merchants);
  
  response.getOutputStream().println(json);
 }
}