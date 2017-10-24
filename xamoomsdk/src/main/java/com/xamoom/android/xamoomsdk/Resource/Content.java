/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.xamoomsdk.Resource;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.xamoom.android.xamoomsdk.Utils.DateUtil;

import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Date;

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
  @SerializedName("social-sharing-url")
  private String sharingUrl;
  @Relationship("related-spot")
  private Spot relatedSpot;
  @SerializedName("meta-datetime-from")
  private String fromDate;
  @SerializedName("meta-datetime-to")
  private String toDate;

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
    sharingUrl = in.readString();
    relatedSpot = in.readParcelable(Spot.class.getClassLoader());
    fromDate = in.readString();
    toDate = in.readString();
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
    dest.writeString(sharingUrl);
    dest.writeParcelable(relatedSpot, flags);
    dest.writeString(fromDate);
    dest.writeString(toDate);
  }

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

  public String getSharingUrl() {
    return sharingUrl;
  }

  public void setSharingUrl(String sharingUrl) {
    this.sharingUrl = sharingUrl;
  }

  public Spot getRelatedSpot() {
    return relatedSpot;
  }

  public void setRelatedSpot(Spot relatedSpot) {
    this.relatedSpot = relatedSpot;
  }

  public Date getFromDate() {
    return DateUtil.parse(fromDate);
  }

  public void setFromDate(Date fromDate) {
    this.fromDate = DateUtil.format(fromDate);
  }

  public Date getToDate() {
    return DateUtil.parse(toDate);
  }

  public void setToDate(Date toDate) {
    this.toDate = DateUtil.format(toDate);
  }

  @Override
  public int describeContents() {
    return 0;
  }
}
