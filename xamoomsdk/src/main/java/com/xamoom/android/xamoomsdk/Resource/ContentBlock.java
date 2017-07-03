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

import java.util.List;

import at.rags.morpheus.Resource;

/**
 *  xamoom ContentBlock model.
 */
public class ContentBlock extends Resource implements Parcelable {
  @SerializedName("block-type")
  private int blockType;
  @SerializedName("is-public")
  private boolean publicStatus;
  private String title;
  private String text;
  private String artists;
  @SerializedName("file-id")
  private String fileId;
  @SerializedName("soundcloud-url")
  private String soundcloudUrl;
  @SerializedName("link-type")
  private int linkType;
  @SerializedName("link-url")
  private String linkUrl;
  @SerializedName("content-id")
  private String contentId;
  @SerializedName("download-type")
  private int downloadType;
  @SerializedName("spot-map-tags")
  private List<String> spotMapTags;
  @SerializedName("scale-x")
  private double scaleX;
  @SerializedName("video-url")
  private String videoUrl;
  @SerializedName("should-show-content-on-spotmap")
  private boolean showContentOnSpotmap;
  @SerializedName("alt-text")
  private String altText;
  @SerializedName("copyright")
  private String copyright;

  public ContentBlock() {
  }

  protected ContentBlock(Parcel in) {
    this.setId(in.readString());
    blockType = in.readInt();
    publicStatus = in.readByte() != 0;
    title = in.readString();
    text = in.readString();
    artists = in.readString();
    fileId = in.readString();
    soundcloudUrl = in.readString();
    linkType = in.readInt();
    linkUrl = in.readString();
    contentId = in.readString();
    downloadType = in.readInt();
    spotMapTags = in.createStringArrayList();
    scaleX = in.readDouble();
    videoUrl = in.readString();
    showContentOnSpotmap = in.readByte() != 0;
    altText = in.readString();
    copyright = in.readString();
  }

  public static final Creator<ContentBlock> CREATOR = new Creator<ContentBlock>() {
    @Override
    public ContentBlock createFromParcel(Parcel in) {
      return new ContentBlock(in);
    }

    @Override
    public ContentBlock[] newArray(int size) {
      return new ContentBlock[size];
    }
  };

  public int getBlockType() {
    return blockType;
  }

  public void setBlockType(int blockType) {
    this.blockType = blockType;
  }

  public boolean isPublicStatus() {
    return publicStatus;
  }

  public void setPublicStatus(boolean publicStatus) {
    this.publicStatus = publicStatus;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getArtists() {
    return artists;
  }

  public void setArtists(String artists) {
    this.artists = artists;
  }

  public String getFileId() {
    return fileId;
  }

  public void setFileId(String fileId) {
    this.fileId = fileId;
  }

  public String getSoundcloudUrl() {
    return soundcloudUrl;
  }

  public void setSoundcloudUrl(String soundcloudUrl) {
    this.soundcloudUrl = soundcloudUrl;
  }

  public int getLinkType() {
    return linkType;
  }

  public void setLinkType(int linkType) {
    this.linkType = linkType;
  }

  public String getLinkUrl() {
    return linkUrl;
  }

  public void setLinkUrl(String linkUrl) {
    this.linkUrl = linkUrl;
  }

  public String getContentId() {
    return contentId;
  }

  public void setContentId(String contentId) {
    this.contentId = contentId;
  }

  public int getDownloadType() {
    return downloadType;
  }

  public void setDownloadType(int downloadType) {
    this.downloadType = downloadType;
  }

  public List<String> getSpotMapTags() {
    return spotMapTags;
  }

  public void setSpotMapTags(List<String> spotMapTags) {
    this.spotMapTags = spotMapTags;
  }

  public double getScaleX() {
    return scaleX;
  }

  public void setScaleX(double scaleX) {
    this.scaleX = scaleX;
  }

  public String getVideoUrl() {
    return videoUrl;
  }

  public void setVideoUrl(String videoUrl) {
    this.videoUrl = videoUrl;
  }

  public boolean isShowContentOnSpotmap() {
    return showContentOnSpotmap;
  }

  public void setShowContentOnSpotmap(boolean showContentOnSpotmap) {
    this.showContentOnSpotmap = showContentOnSpotmap;
  }

  public String getAltText() {
    return altText;
  }

  public void setAltText(String altText) {
    this.altText = altText;
  }

  public String getCopyright() {
    return copyright;
  }

  public void setCopyright(String copyright) {
    this.copyright = copyright;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.getId());
    dest.writeInt(blockType);
    dest.writeByte((byte) (publicStatus ? 1 : 0));
    dest.writeString(title);
    dest.writeString(text);
    dest.writeString(artists);
    dest.writeString(fileId);
    dest.writeString(soundcloudUrl);
    dest.writeInt(linkType);
    dest.writeString(linkUrl);
    dest.writeString(contentId);
    dest.writeInt(downloadType);
    dest.writeStringList(spotMapTags);
    dest.writeDouble(scaleX);
    dest.writeString(videoUrl);
    dest.writeByte((byte) (showContentOnSpotmap ? 1 : 0));
    dest.writeString(altText);
    dest.writeString(copyright);
  }
}
