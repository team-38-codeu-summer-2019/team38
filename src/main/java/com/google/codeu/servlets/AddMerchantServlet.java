/*
 * Copyright 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.codeu.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.codeu.data.Datastore;
import com.google.codeu.data.Merchant;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/** Handles fetching and saving {@link Message} instances. */
@WebServlet("/add-merchant")
public class AddMerchantServlet extends HttpServlet {

  private Datastore datastore;

  @Override
  public void init() {
    datastore = new Datastore();
  }

//   /**
//    * Responds with a JSON representation of {@link Message} data for a specific user. Responds with
//    * an empty array if the user is not provided.
//    */
//   @Override
//   public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

//     response.setContentType("application/json");

//     List<Merchant> merchants = datastore.getAllMerchants();
//     Gson gson = new Gson();
//     String json = gson.toJson(merchants);

//     response.getWriter().println(json);
//   }

  /** Stores a new {@link Message}. */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    String name = Jsoup.clean(request.getParameter("name"), Whitelist.none());
    String cuisine = Jsoup.clean(request.getParameter("cuisine"), Whitelist.none());
    String location = Jsoup.clean(request.getParameter("location"), Whitelist.none());
    double latitude = Double.parseDouble(Jsoup.clean(request.getParameter("latitude"), Whitelist.none()));
    double longitude = Double.parseDouble(Jsoup.clean(request.getParameter("longitude"), Whitelist.none()));

    // String regex = "(https?://\\S+\\.(png|jpg))";
    // String replacement = "<img src=\"$1\" />";
    // String textWithImagesReplaced = text.replaceAll(regex, replacement);

    Merchant merchant = new Merchant(name,cuisine,latitude,longitude,location);
    datastore.storeMerchant(merchant);

    response.sendRedirect("/search.html");
  }
}
