package com.xamoom.android.xamoomsdk.Resource;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import at.rags.morpheus.Annotations.Relationship;
import at.rags.morpheus.Resource;

/**
 * xamoom Menu model.
 */
public class Menu extends Resource implements Parcelable {
  @Relationship("items")
  private List<Content> items;

  public Menu() {
  }

  protected Menu(Parcel in) {
    this.setId(in.readString());
    items = in.createTypedArrayList(Content.CREATOR);
  }

  public static final Creator<Menu> CREATOR = new Creator<Menu>() {
    @Override
    public Menu createFromParcel(Parcel in) {
      return new Menu(in);
    }

    @Override
    public Menu[] newArray(int size) {
      return new Menu[size];
    }
  };

  public List<Content> getItems() {
    return items;
  }

  public void setItems(List<Content> items) {
    this.items = items;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.getId());
    dest.writeTypedList(items);
  }
}
