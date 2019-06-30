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

import java.util.UUID;

/** A single message posted by a user. */
public class Merchant {

  private UUID id;
  private String name;
  private String cuisine;
  private String distance;
  private String priceRange;

  // /**
  //  * Constructs a new {@link Message} posted by {@code user} with {@code text} content. Generates a
  //  * random ID and uses the current system time for the creation time.
  //  */
  public Merchant(String name, String cuisine, String distance, String price) {
    this(UUID.randomUUID(), name, cuisine, distance, price);
  }

  public Merchant(UUID id, String name, String cuisine, String distance, String price) {
    this.id = id;
    this.name = name;
    this.cuisine = cuisine;
    this.distance = distance;
    this.priceRange = price;
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getCuisine() {
    return cuisine;
  }

  public String getDistance() {
    return distance;
  }

  public String getPrice() {
    return priceRange;
  }
}
