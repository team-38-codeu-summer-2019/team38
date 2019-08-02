package com.google.codeu.data;

import java.util.UUID;

public class Merchant {

  private UUID id;
  private String name;
  private String image;
  private double latitude;
  private double longitude;
  private long university;
  private String location;
  private String cuisine;
  private String[] paymentOptions;

  public Merchant(UUID id, String name, String cuisine, long university, double latitude, double longitude, String location, String image) {
    this.id = id;
    this.name = name;
    this.cuisine = cuisine;
    this.university = university;
    this.latitude = latitude;
    this.longitude = longitude;
    this.location = location;
    this.image = image;
  }

  public Merchant(String name, String cuisine, long university, double latitude, double longitude, String location, String image) {
    this.id = UUID.randomUUID();
    this.name = name;
    this.cuisine = cuisine;
    this.university = university;
    this.latitude = latitude;
    this.longitude = longitude;
    this.location = location;
    this.image = image;
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

  public long getUniversity() {
    return university;
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
