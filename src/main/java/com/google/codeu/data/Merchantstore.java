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
public class Merchantstore {

  private DatastoreService merchantstore;

  public Merchantstore() {
    merchantstore = DatastoreServiceFactory.getDatastoreService();
  }

  /** Stores the Merchant in Datastore. */
  public void storeMerchant(Merchant merchant) {
    Entity merchantEntity = new Entity("Merchant", merchant.getId().toString());
    merchantEntity.setProperty("name", merchant.getName());
    merchantEntity.setProperty("distance", merchant.getDistance());
    merchantEntity.setProperty("cuisine", merchant.getCuisine());
    merchantEntity.setProperty("priceRange", merchant.getPrice());

    merchantstore.put(merchantEntity);
  }

  /**
   * Gets messages posted by a specific user.
   *
   * @return a list of messages posted by the user, or empty list if user has
   *         never posted a message. List is sorted by time descending.
   */
  public List<Merchant> getAllMerchants() {
    List<Merchant> merchants = new ArrayList<>();

    Query query = new Query("Merchant").addSort("distance", SortDirection.ASCENDING);
    PreparedQuery results = merchantstore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
        String idString = entity.getKey().getName();
        UUID id = UUID.fromString(idString);
        String name = (String) entity.getProperty("name");
        String cuisine = (String) entity.getProperty("cuisine");
        String distance = (String) entity.getProperty("distance");
        String price = (String) entity.getProperty("priceRange");

        Merchant merchant = new Merchant(id, name, cuisine, distance, price);
        merchants.add(merchant);
      } catch (Exception e) {
        System.err.println("Error reading merchant.");
        System.err.println(entity.toString());
        e.printStackTrace();
      }
    }

    return merchants;
  }

}
