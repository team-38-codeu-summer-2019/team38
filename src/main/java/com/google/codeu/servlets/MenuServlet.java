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

import com.google.codeu.data.Datastore;
import com.google.codeu.data.Merchant;
import com.google.codeu.data.Menu;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/** Handles fetching and saving {@link Message} instances. */
@WebServlet("/menu")
public class MenuServlet extends HttpServlet {

  private Datastore datastore;

  @Override
  public void init() {
    datastore = new Datastore();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    response.setContentType("application/json");

    String merchantId = Jsoup.clean(request.getParameter("merchantId"), Whitelist.none());
    List<Menu> menus = datastore.getAllMenus(merchantId);
    Gson gson = new Gson();
    String json = gson.toJson(menus);

    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    String name = Jsoup.clean(request.getParameter("name"), Whitelist.none());
    String image = Jsoup.clean(request.getParameter("image"), Whitelist.none());
    String description = Jsoup.clean(request.getParameter("description"), Whitelist.none());
    long price = Long.parseLong(Jsoup.clean(request.getParameter("price"), Whitelist.none()));
    UUID merchantId = UUID.fromString(Jsoup.clean(request.getParameter("merchantId"), Whitelist.none()));

    // String regex = "(https?://\\S+\\.(png|jpg))";
    // String replacement = "<img src=\"$1\" />";
    // String textWithImagesReplaced = text.replaceAll(regex, replacement);

    Menu menu = new Menu(name,image,description,price,merchantId);
    datastore.storeMerchant(menu);

    response.sendRedirect("/merchants.html?id=" + merchantId);
  }
}
