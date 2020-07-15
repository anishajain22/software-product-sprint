package com.google.sps.data;

/** An item on a comment section. */
public final class Comment {

  private final long id;
  private final String text;
  private final long timestamp;
  private final String imageUrl;

  public Comment(long id, String text, long timestamp, String imageUrl) {
    this.id = id;
    this.text = text;
    this.timestamp = timestamp;
    this.imageUrl =  imageUrl;
  }
}