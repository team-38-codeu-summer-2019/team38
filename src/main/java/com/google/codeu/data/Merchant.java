package com.google.codeu.data;

import java.util.UUID;

public class Merchant {

  private UUID id;
  private String name;
  private String image;
  private double latitude;
  private double longitude;
  private String location;
  private String cuisine;
  private String[] paymentOptions;
  private UUID cafeId;

  public Merchant(String name, String image, double latitude, double longitude, String location, String cuisine,
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

  public Merchant(UUID id, String name, String cuisine, double latitude, double longitude, String location) {
    this.id = id;
    this.name = name;
    this.cuisine = cuisine;
    this.latitude = latitude;
    this.longitude = longitude;
    this.location = location;
  }

  public Merchant(String name, String cuisine, double latitude, double longitude, String location) {
    this.id = UUID.randomUUID();
    this.name = name;
    this.cuisine = cuisine;
    this.latitude = latitude;
    this.longitude = longitude;
    this.location = location;
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

  public double getLatitude() {
    return latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public String getLocation() {
    return location;
  }

  public String getCuisine() {
    return cuisine;
  }

  public String[] getPaymentOptions() {
    return paymentOptions;
  }
}
