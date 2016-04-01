package com.xamoom.android.xamoomsdk.Resource;

import at.rags.morpheus.Annotations.SerializeName;
import at.rags.morpheus.Resource;

/**
 * Created by raphaelseher on 01/04/16.
 */
public class MenuItem extends Resource {
  @SerializeName("content-title")
  private String contentTitle;
  private int category;
}
