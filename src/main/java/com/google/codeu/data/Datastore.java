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

package com.google.codeu.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/** Provides access to the data stored in Datastore. */
public class Datastore {

  private DatastoreService datastore;

  public Datastore() {
    datastore = DatastoreServiceFactory.getDatastoreService();
  }

  /** Stores the Review in Datastore. */
  public void storeReview(Review review) {
    Entity reviewEntity = new Entity("Review", review.getID().toString());
    reviewEntity.setProperty("userEmail", review.getUserEmail());
    reviewEntity.setProperty("merchantID", review.getMerchantID().toString());
    reviewEntity.setProperty("text", review.getText());
    reviewEntity.setProperty("rating", review.getRating());
    reviewEntity.setProperty("timestamp", review.getTimestamp());

    datastore.put(reviewEntity);
  }

  /**
   * Gets reviews posted to a specific merchant.
   *
   * @return a list of reviews posted to a merchant, or empty list if the merchant haven't been reviewed.
   * List is sorted by time descending.
   */
  public List<Review> getReviews(UUID merchantID) {
    List<Review> reviews = new ArrayList<>();

    Query query = new Query("Review")
            .setFilter(new Query.FilterPredicate("merchantID", FilterOperator.EQUAL, merchantID.toString()))
            .addSort("timestamp", SortDirection.DESCENDING);
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
        UUID id = UUID.fromString(entity.getKey().getName());
        String userEmail = (String) entity.getProperty("userEmail");
        String text = (String) entity.getProperty("text");
        long rating = (long) entity.getProperty("rating");
        long timestamp = (long) entity.getProperty("timestamp");

        Review review = new Review(id, userEmail, merchantID, text, rating, timestamp);
        reviews.add(review);
      } catch (Exception e) {
        System.err.println("Error reading review.");
        System.err.println(entity.toString());
        e.printStackTrace();
      }
    }

    return reviews;
  }

  /**
   * Gets reviews posted by a specific user.
   *
   * @return a list of reviews posted by a user, or empty list if the user haven't post reviews.
   * List is sorted by time descending.
   */
  public List<Review> getReviewsByUser(String userEmail) {
    List<Review> reviews = new ArrayList<>();

    Query query = new Query("Review")
            .setFilter(new Query.FilterPredicate("userEmail", FilterOperator.EQUAL, userEmail))
            .addSort("timestamp", SortDirection.DESCENDING);
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
        UUID id = UUID.fromString(entity.getKey().getName());
        UUID merchantID = UUID.fromString((String)entity.getProperty("merchantID"));
        String text = (String) entity.getProperty("text");
        long rating = (long) entity.getProperty("rating");
        long timestamp = (long) entity.getProperty("timestamp");

        Review review = new Review(id, userEmail, merchantID, text, rating, timestamp);
        reviews.add(review);
      } catch (Exception e) {
        System.err.println("Error reading review.");
        System.err.println(entity.toString());
        e.printStackTrace();
      }
    }

    return reviews;
  }

  /** Stores the Message in Datastore. */
  public void storeMessage(Message message) {
    Entity messageEntity = new Entity("Message", message.getId().toString());
    messageEntity.setProperty("user", message.getUser());
    messageEntity.setProperty("text", message.getText());
    messageEntity.setProperty("timestamp", message.getTimestamp());

    datastore.put(messageEntity);
  }

  /**
   * Gets messages posted by a specific user.
   *
   * @return a list of messages posted by the user, or empty list if user has
   *         never posted a message. List is sorted by time descending.
   */
  public List<Message> getMessages(String user) {
    List<Message> messages = new ArrayList<>();

    Query query = new Query("Message").setFilter(new Query.FilterPredicate("user", FilterOperator.EQUAL, user))
        .addSort("timestamp", SortDirection.DESCENDING);
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
        String idString = entity.getKey().getName();
        UUID id = UUID.fromString(idString);
        String text = (String) entity.getProperty("text");
        long timestamp = (long) entity.getProperty("timestamp");

        Message message = new Message(id, user, text, timestamp);
        messages.add(message);
      } catch (Exception e) {
        System.err.println("Error reading message.");
        System.err.println(entity.toString());
        e.printStackTrace();
      }
    }

    return messages;
  }

  public Set<String> getUsers() {
    Set<String> users = new HashSet<>();
    Query query = new Query("Message");
    PreparedQuery results = datastore.prepare(query);
    for (Entity entity : results.asIterable()) {
      users.add((String) entity.getProperty("user"));
    }
    return users;
  }

  /** Returns the total number of messages for all users. */
  public int getTotalMessageCount() {
    Query query = new Query("Message");
    PreparedQuery results = datastore.prepare(query);
    return results.countEntities(FetchOptions.Builder.withLimit(1000));
  }

  public List<Message> getAllMessages() {
    List<Message> messages = new ArrayList<>();

    Query query = new Query("Message").addSort("timestamp", SortDirection.DESCENDING);
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
        String idString = entity.getKey().getName();
        UUID id = UUID.fromString(idString);
        String user = (String) entity.getProperty("user");
        String text = (String) entity.getProperty("text");
        long timestamp = (long) entity.getProperty("timestamp");

        Message message = new Message(id, user, text, timestamp);
        messages.add(message);
      } catch (Exception e) {
        System.err.println("Error reading message.");
        System.err.println(entity.toString());
        e.printStackTrace();
      }
    }

    return messages;
  }

  /** Stores the User in Datastore. */
  public void storeUser(User user) {
    Entity userEntity = new Entity("User", user.getEmail());
    userEntity.setProperty("email", user.getEmail());
    userEntity.setProperty("university", user.getUniversity());
    userEntity.setProperty("aboutMe", user.getAboutMe());
    datastore.put(userEntity);
  }

  /**
   * Returns the User owned by the email address, or null if no matching User was
   * found.
   */
  public User getUser(String email) {

    Query query = new Query("User")
            .setFilter(new Query.FilterPredicate("email", FilterOperator.EQUAL, email));
    PreparedQuery results = datastore.prepare(query);
    Entity userEntity = results.asSingleEntity();
    if (userEntity == null) {
      return null;
    }

    long university = (long)userEntity.getProperty("university");
    String aboutMe = (String) userEntity.getProperty("aboutMe");
    User user = new User(email, university, aboutMe);

    return user;
  }

  /** Stores the University in Datastore. */
  public void storeUniversity(University university) {
    Entity universityEntity = new Entity("University", university.getID());
    universityEntity.setProperty("ID", university.getID());
    universityEntity.setProperty("name", university.getName());
    datastore.put(universityEntity);
  }

  /** Returns a list of supported universities. */
  public List<University> getUniversities() {
    List<University> universities = new ArrayList<>();

    Query query = new Query("University")
            .addSort("ID", SortDirection.ASCENDING);
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
        long ID = (long) entity.getProperty("ID");
        String name = (String) entity.getProperty("name");

        University university = new University(ID, name);
        universities.add(university);
      } catch (Exception e) {
        System.err.println("Error loading universities.");
        System.err.println(entity.toString());
        e.printStackTrace();
      }
    }

    return universities;
  }

  /** Returns University by ID */
  public University getUniversity(long ID) {

    Query query = new Query("University")
            .setFilter(new Query.FilterPredicate("ID", FilterOperator.EQUAL, ID));
    PreparedQuery results = datastore.prepare(query);
    Entity universityEntity = results.asSingleEntity();
    if (universityEntity == null) {
      return null;
    }

    String name = (String) universityEntity.getProperty("name");
    University university = new University(ID, name);

    return university;
  }

  /** Stores the Merchant in Datastore. */
  public void storeMerchant(Merchant merchant) {
    Entity merchantEntity = new Entity("Merchant", merchant.getId().toString());
    merchantEntity.setProperty("id", merchant.getId().toString());
    merchantEntity.setProperty("name", merchant.getName());
    merchantEntity.setProperty("cuisine", merchant.getCuisine());
    merchantEntity.setProperty("location", merchant.getLocation());
    merchantEntity.setProperty("latitude", merchant.getLatitude());
    merchantEntity.setProperty("longitude", merchant.getLongitude());
    merchantEntity.setProperty("image", merchant.getImage());

    datastore.put(merchantEntity);
  }

  public List<Merchant> getAllMerchants() {
    List<Merchant> merchants = new ArrayList<>();

    Query query = new Query("Merchant");
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
        String idString = entity.getKey().getName();
        UUID id = UUID.fromString(idString);
        String name = (String) entity.getProperty("name");
        String cuisine = (String) entity.getProperty("cuisine");
        String location = (String) entity.getProperty("location");
        double latitude = (double) entity.getProperty("latitude");
        double longitude = (double) entity.getProperty("longitude");
        String image = (String) entity.getProperty("image");

        Merchant merchant = new Merchant(id, name, cuisine, latitude, longitude, location, image);
        merchants.add(merchant);
      } catch (Exception e) {
        System.err.println("Error reading merchant.");
        System.err.println(entity.toString());
        e.printStackTrace();
      }
    }

    return merchants;
  }

  /** Stores the Merchant in Datastore. */
  public void storeMenu(Menu menu) {
    Entity menuEntity = new Entity("Menu", menu.getId().toString());
    menuEntity.setProperty("name", menu.getName());
    menuEntity.setProperty("image", menu.getImage());
    menuEntity.setProperty("description", menu.getDescription());
    menuEntity.setProperty("price", menu.getPrice());
    menuEntity.setProperty("merchantId", menu.getMerchantId().toString());

    datastore.put(menuEntity);
  }

  public Merchant getMerchant(String id) {

    Query query = new Query("Merchant").setFilter(new Query.FilterPredicate("id", FilterOperator.EQUAL, id));
    PreparedQuery results = datastore.prepare(query);
    Entity entity = results.asSingleEntity();
    if (entity == null) {
      return null;
    }

    String name = (String) entity.getProperty("name");
    String cuisine = (String) entity.getProperty("cuisine");
    String location = (String) entity.getProperty("location");
    double latitude = (double) entity.getProperty("latitude");
    double longitude = (double) entity.getProperty("longitude");
    String image = (String) entity.getProperty("image");
    UUID idMerchant = UUID.fromString(id);

    Merchant merchant = new Merchant(idMerchant, name, cuisine, latitude, longitude, location, image);

    return merchant;
  }

  public List<Menu> getAllMenus(String merchantId) {
    List<Menu> menus = new ArrayList<>();
    
    Query query = new Query("Menu").setFilter(new Query.FilterPredicate("merchantId", FilterOperator.EQUAL, merchantId));;
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
        String idString = entity.getKey().getName();
        UUID id = UUID.fromString(idString);
        String name = (String) entity.getProperty("name");
        String image = (String) entity.getProperty("image"); 
        String description = (String) entity.getProperty("description");
        long price = (long) entity.getProperty("price");
        UUID idMerchant = UUID.fromString(merchantId);

        Menu menu = new Menu(id, name, image, description, price, idMerchant);
        menus.add(menu);
      } catch (Exception e) {
        System.err.println("Error reading menu.");
        System.err.println(entity.toString());
        e.printStackTrace();
      }
    }

    return menus;
  }

}
