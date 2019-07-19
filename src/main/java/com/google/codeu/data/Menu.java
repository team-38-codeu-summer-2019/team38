package com.google.codeu.data;

import java.util.UUID;

public class Menu {

  private UUID id;
  private String name;
  private String image;
  private String description;
  private long price;
  private UUID merchantId;

  public Menu(String name, String image, String description, long price, UUID merchantId) {
    this.id = UUID.randomUUID();
    this.name = name;
    this.image = image;
    this.description = description;
    this.price = price;
    this.merchantId = merchantId;
  }

  public Menu(UUID id, String name, String image, String description, long price, UUID merchantId) {
    this.id = id;
    this.name = name;
    this.image = image;
    this.description = description;
    this.price = price;
    this.merchantId = merchantId;
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

  public String getDescription() {
    return description;
  }

  public long getPrice() {
    return price;
  }

  public UUID getMerchantId() {
    return merchantId;
  }
}
