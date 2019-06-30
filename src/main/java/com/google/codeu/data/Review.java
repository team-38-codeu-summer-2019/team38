package com.google.codeu.data;

import java.util.UUID;

// TODO: add field for  image
public class Review {
    private UUID id;
    private String user;
    private String merchant;
    private String text;
    private long rating;
    private long timestamp;


    /**
     * Constructs a new {@link Review} posted by {@code user} to {@code merchant} with {@code text} content and
     * {@code rating}.
     * Generates a random ID and uses the current system time for the creation time.
     */
    public Review(String user, String merchant, String text, long rating) {
        this(UUID.randomUUID(), user, merchant, text, rating,  System.currentTimeMillis());
    }

    public Review(UUID id, String user, String merchant, String text, long rating, long timestamp) {
        this.id = id;
        this.user = user;
        this.merchant = merchant;
        this.text = text;
        this.rating = rating;
        this.timestamp = timestamp;
    }

    public UUID getId() { return id; }

    public String getUser() {
        return user;
    }

    public String getMerchant() {
        return merchant;
    }

    public String getText() {
        return text;
    }

    public long getRating() { return rating; }

    public long getTimestamp() {
        return timestamp;
    }

}
