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

import com.google.gson.annotations.SerializedName;

import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import at.rags.morpheus.Annotations.Relationship;
import at.rags.morpheus.Resource;

/**
 *  xamoom Content model.
 */
public class Content extends Resource implements Parcelable {
  @SerializedName("display-name")
  private String title;
  private String description;
  private String language;
  private int category;
  private List<String> tags;
  @SerializedName("cover-image-url")
  private String publicImageUrl;
  @Relationship("blocks")
  private List<ContentBlock> contentBlocks;
  @Relationship("system")
  private System system;
  @SerializedName("custom-meta")
  private ArrayList<KeyValueObject> customMeta;

  public Content() {
  }

  protected Content(Parcel in) {
    this.setId(in.readString());
    title = in.readString();
    description = in.readString();
    language = in.readString();
    category = in.readInt();
    tags = in.createStringArrayList();
    publicImageUrl = in.readString();
    contentBlocks = in.createTypedArrayList(ContentBlock.CREATOR);
    system = in.readParcelable(System.class.getClassLoader());
  }

  public static final Creator<Content> CREATOR = new Creator<Content>() {
    @Override
    public Content createFromParcel(Parcel in) {
      return new Content(in);
    }

    @Override
    public Content[] newArray(int size) {
      return new Content[size];
    }
  };

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public String getLanguage() {
    return language;
  }

  public int getCategory() {
    return category;
  }

  public List<String> getTags() {
    return tags;
  }

  public String getPublicImageUrl() {
    return publicImageUrl;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public void setCategory(int category) {
    this.category = category;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  public void setPublicImageUrl(String publicImageUrl) {
    this.publicImageUrl = publicImageUrl;
  }

  public System getSystem() {
    return system;
  }

  public void setSystem(System system) {
    this.system = system;
  }

  public List<ContentBlock> getContentBlocks() {
    return contentBlocks;
  }

  public void setContentBlocks(List<ContentBlock> contentBlocks) {
    this.contentBlocks = contentBlocks;
  }

  public HashMap<String, String> getCustomMeta() {
    if (customMeta == null) {
      return null;
    }

    HashMap<String, String> customMetaMap = new HashMap<>();
    for (KeyValueObject meta : customMeta) {
      customMetaMap.put(meta.getKey(), meta.getValue());
    }
    return customMetaMap;
  }

  public void setCustomMeta(HashMap<String, String> customMeta) {
    ArrayList<KeyValueObject> customMetaList = new ArrayList<>();

    for (Object o : customMeta.entrySet()) {
      Map.Entry pair = (Map.Entry) o;
      customMetaList.add(new KeyValueObject(pair.getKey().toString(),
          pair.getValue().toString()));
    }

    this.customMeta = customMetaList;
  }

  public void setCustomMeta(ArrayList<KeyValueObject> customMeta) {
    this.customMeta = customMeta;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.getId());
    dest.writeString(title);
    dest.writeString(description);
    dest.writeString(language);
    dest.writeInt(category);
    dest.writeStringList(tags);
    dest.writeString(publicImageUrl);
    dest.writeTypedList(contentBlocks);
    dest.writeParcelable(system, flags);
  }

}
