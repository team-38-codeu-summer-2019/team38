package com.google.codeu.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.codeu.data.Merchantstore;
import com.google.codeu.data.Merchant;
import com.google.gson.Gson;

/**
 * Handles fetching all messages for the public feed.
 */
@WebServlet("/search")
public class MerchantServlet extends HttpServlet{
  
 private Merchantstore merchantstore;

 @Override
 public void init() {
  merchantstore = new Merchantstore();
 }
 
 /**
  * Responds with a JSON representation of Message data for all users.
  */
 @Override
 public void doGet(HttpServletRequest request, HttpServletResponse response)
   throws IOException {

  response.setContentType("application/json");
  
  List<Merchant> merchants = merchantstore.getAllMerchants();
  Gson gson = new Gson();
  String json = gson.toJson(merchants);
  
  response.getOutputStream().println(json);
 }
}