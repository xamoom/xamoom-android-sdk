package com.xamoom.android.xamoomsdk.Resource;

import java.util.List;

import at.rags.morpheus.Annotations.SerializeName;
import at.rags.morpheus.Resource;

/**
 * xamoom Spot model.
 */
public class Spot extends Resource {
  private String name;
  @SerializeName("image")
  private String publicImageUrl;
  private String description;
  @SerializeName("position-longitude")
  private double lat;
  @SerializeName("position-latitude")
  private double lon;
  private List<String> tags;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPublicImageUrl() {
    return publicImageUrl;
  }

  public void setPublicImageUrl(String publicImageUrl) {
    this.publicImageUrl = publicImageUrl;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public double getLat() {
    return lat;
  }

  public void setLat(double lat) {
    this.lat = lat;
  }

  public double getLon() {
    return lon;
  }

  public void setLon(double lon) {
    this.lon = lon;
  }

  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }
}
