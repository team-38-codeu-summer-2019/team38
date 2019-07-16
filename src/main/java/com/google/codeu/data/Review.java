package com.google.codeu.data;

import java.util.UUID;

// TODO: add field for  image
public class Review {
    private String userEmail;
    private UUID merchantID;
    private String text;
    private long rating;
    private long timestamp;


    /**
     * Constructs a new {@link Review} posted by {@code user} to {@code merchant} with {@code text} content and
     * {@code rating}.
     * Generates a random ID and uses the current system time for the creation time.
     */
    public Review(String userEmail, UUID merchantID, String text, long rating) {
        this(userEmail, merchantID, text, rating,  System.currentTimeMillis());
    }

    public Review(String userEmail, UUID merchantID, String text, long rating, long timestamp) {
        this.userEmail = userEmail;
        this.merchantID = merchantID;
        this.text = text;
        this.rating = rating;
        this.timestamp = timestamp;
    }

    public String getID() { return userEmail + "," + merchantID.toString(); }

    public String getUserEmail() {
        return userEmail;
    }

    public UUID getMerchantID() {
        return merchantID;
    }

    public String getText() {
        return text;
    }

    public long getRating() { return rating; }

    public long getTimestamp() {
        return timestamp;
    }

}
