package com.xamoom.android.xamoomsdk.Resource;

import java.util.List;
import java.util.Map;

import at.rags.morpheus.Annotations.Relationship;
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
  private Map<String, Double> location;
  private List<String> tags;
  @Relationship("markers")
  private List<Marker> markers;
  @Relationship("system")
  private System system;
  @Relationship("content")
  private Content content;

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

  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  public List<Marker> getMarkers() {
    return markers;
  }

  public void setMarkers(List<Marker> markers) {
    this.markers = markers;
  }

  public System getSystem() {
    return system;
  }

  public void setSystem(System system) {
    this.system = system;
  }

  public Content getContent() {
    return content;
  }

  public void setContent(Content content) {
    this.content = content;
  }

  public Map<String, Double> getLocation() {
    return location;
  }

  public void setLocation(Map<String, Double> location) {
    this.location = location;
  }

  public double getLat() {
    return location.get("lat");
  }

  public double getLon() {
    return location.get("lon");
  }
}
