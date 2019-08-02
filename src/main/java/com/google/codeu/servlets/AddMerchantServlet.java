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

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.codeu.data.Datastore;
import com.google.codeu.data.Merchant;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    response.setContentType("application/json");

    String merchantId = request.getParameter("id");
    Merchant merchant = datastore.getMerchant(merchantId);

    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("id", merchantId);
    jsonObject.addProperty("image", merchant.getImage());
    jsonObject.addProperty("name", merchant.getName());
    jsonObject.addProperty("cuisine", merchant.getCuisine());
    jsonObject.addProperty("location", merchant.getLocation());

    response.getWriter().println(jsonObject.toString());
  }

  /** Stores a new {@link Message}. */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    String name = Jsoup.clean(request.getParameter("name"), Whitelist.none());
    String image = request.getParameter("image");
    if (image == null) {
      image = getUploadedFileUrl(request, "file");
    }
    String cuisine = Jsoup.clean(request.getParameter("cuisine"), Whitelist.none());
    String location = Jsoup.clean(request.getParameter("location"), Whitelist.none());
    double latitude = Double.parseDouble(Jsoup.clean(request.getParameter("latitude"), Whitelist.none()));
    double longitude = Double.parseDouble(Jsoup.clean(request.getParameter("longitude"), Whitelist.none()));

    Merchant merchant = new Merchant(name, cuisine, latitude, longitude, location, image);
    datastore.storeMerchant(merchant);

    response.sendRedirect("/search.html");
  }

  private String getUploadedFileUrl(HttpServletRequest request, String formInputElementName) {
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);
    List<BlobKey> blobKeys = blobs.get(formInputElementName);

    // User submitted form without selecting a file, so we can't get a URL.
    // (devserver)
    if (blobKeys == null || blobKeys.isEmpty()) {
      return null;
    }

    // Our form only contains a single file input, so get the first index.
    BlobKey blobKey = blobKeys.get(0);

    // User submitted form without selecting a file, so we can't get a URL. (live
    // server)
    BlobInfo blobInfo = new BlobInfoFactory().loadBlobInfo(blobKey);
    if (blobInfo.getSize() == 0) {
      blobstoreService.delete(blobKey);
      return null;
    }

    // We could check the validity of the file here, e.g. to make sure it's an image
    // file
    // https://stackoverflow.com/q/10779564/873165

    // Use ImagesService to get a URL that points to the uploaded file.
    ImagesService imagesService = ImagesServiceFactory.getImagesService();
    ServingUrlOptions options = ServingUrlOptions.Builder.withBlobKey(blobKey);
    return imagesService.getServingUrl(options);
  }
}
