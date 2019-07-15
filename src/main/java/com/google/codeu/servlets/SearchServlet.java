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
  
//  private Datastore datastore;
 

 @Override
 public void init() {
//   datastore = new Datastore();
    // datastore = [{"id":"123123","cafe":"McDonald's","name":"McDonald's","location":"2/F ABC Building, Centennial Campus","distance_meters":50,"cuisine":"Western"},
    //             {"id":"789789","cafe":"Swire Canteen","name":"Ebeneezer's","location":"5/F Computing Building, Main Campus","distance_meters":100,"cuisine":"Indian"}];
 }
 
 /**
  * Responds with a JSON representation of Message data for all users.
  */
 @Override
 public void doGet(HttpServletRequest request, HttpServletResponse response)
   throws IOException {

//   response.setContentType("application/json");

  String merchant = Jsoup.clean(request.getParameter("merchant"), Whitelist.none());
  
//   List<Message> messages = datastore.getAllMessages();
//   Gson gson = new Gson();
//   String json = gson.toJson(messages);
  
//   response.getOutputStream().println(json);
  response.sendRedirect("/search.html?merchant=" + merchant);
 }
}