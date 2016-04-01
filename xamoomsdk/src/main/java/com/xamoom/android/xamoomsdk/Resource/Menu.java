package com.xamoom.android.xamoomsdk.Resource;

import java.util.List;

import at.rags.morpheus.Annotations.Relationship;
import at.rags.morpheus.Resource;

/**
 * xamoom Menu model.
 */
public class Menu extends Resource {
  @Relationship("items")
  private List<Content> items;

  public List<Content> getItems() {
    return items;
  }

  public void setItems(List<Content> items) {
    this.items = items;
  }
}
