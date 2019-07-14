package com.google.codeu.data;

import java.util.UUID;

public class Merchant {

  private UUID id;
  private String name;
  private String image;
  private long latitude;
  private long longitude;
  private String location;
  private Enum cuisine;
  private String[] paymentOptions;
  private UUID cafeId;

  public Merchant(String name, String image, long latitude, long longitude, String location, Enum cuisine,
      String[] paymentOptions, UUID cafeId) {
    this.id = UUID.randomUUID();
    this.name = name;
    this.image = image;
    this.latitude = latitude;
    this.longitude = longitude;
    this.location = location;
    this.cuisine = cuisine;
    this.paymentOptions = paymentOptions;
    this.cafeId = cafeId;
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getImage() {
    return image;
  }

  public String getLocation() {
    return location;
  }

  public Enum getCuisine() {
    return cuisine;
  }

  public String[] getPaymentOptions() {
    return paymentOptions;
  }
}
