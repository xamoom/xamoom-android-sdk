/*
* Copyright 2017 by xamoom GmbH <apps@xamoom.com>
*
* This file is part of some open source application.
*
* Some open source application is free software: you can redistribute
* it and/or modify it under the terms of the GNU General Public
* License as published by the Free Software Foundation, either
* version 2 of the License, or (at your option) any later version.
*
* Some open source application is distributed in the hope that it will
* be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
* of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with xamoom-android-sdk. If not, see <http://www.gnu.org/licenses/>.
*
* author: Raphael Seher <raphael@xamoom.com>
*/

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
