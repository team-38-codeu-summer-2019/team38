package com.google.codeu.data;

import javax.servlet.annotation.WebServlet;

@WebServlet("/merchants")
public class Merchant {

  private String name;
  private String image;
  private String location;
  private String typeOfCuisine;
  private int priceRange;
  // private Menu menu;
  private String[] paymentOptions;
  // private int crowdedness;

  public Merchant(String name, String image, String location, String typeOfCuisine, int priceRange,
      String[] paymentOptions) {
    this.name = name;
    this.image = image;
    this.location = location;
    this.typeOfCuisine = typeOfCuisine;
    this.priceRange = priceRange;
    this.paymentOptions = paymentOptions;
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

  public String getTypeOfCuisine() {
    return typeOfCuisine;
  }

  public int getPriceRange() {
    return priceRange;
  }

  public String[] getPaymentOptions() {
    return paymentOptions;
  }
}