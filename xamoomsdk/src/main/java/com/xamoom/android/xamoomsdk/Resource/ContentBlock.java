package com.xamoom.android.xamoomsdk.Resource;

import com.xamoom.android.xamoomsdk.Resource.Attributes.ContentBlockAttributeMessage;
import com.xamoom.android.xamoomsdk.Resource.Base.DataMessage;
import com.xamoom.android.xamoomsdk.Resource.Base.EmptyMessage;

import java.util.ArrayList;
import java.util.List;

public class ContentBlock {
  private String ID;
  private int blockType;
  private boolean publicStatus;
  private String title;
  private String text;
  private String artists;
  private String fileId;
  private String soundcloudUrl;
  private int linkType;
  private String linkUrl;
  private String contentId;
  private int downloadType;
  private List<String> spotMapTags;
  private float scaleX;
  private String videoUrl;
  private boolean showContentOnSpotmap;
  private String altText;

  public ContentBlock(String ID, int blockType, boolean publicStatus, String title, String text, String artists, String fileId, String soundcloudUrl, int linkType, String linkUrl, String contentId, int downloadType, List<String> spotMapTags, float scaleX, String videoUrl, boolean showContentOnSpotmap, String altText) {
    this.ID = ID;
    this.blockType = blockType;
    this.publicStatus = publicStatus;
    this.title = title;
    this.text = text;
    this.artists = artists;
    this.fileId = fileId;
    this.soundcloudUrl = soundcloudUrl;
    this.linkType = linkType;
    this.linkUrl = linkUrl;
    this.contentId = contentId;
    this.downloadType = downloadType;
    this.spotMapTags = spotMapTags;
    this.scaleX = scaleX;
    this.videoUrl = videoUrl;
    this.showContentOnSpotmap = showContentOnSpotmap;
    this.altText = altText;
  }

  public String getID() {
    return ID;
  }

  public int getBlockType() {
    return blockType;
  }

  public boolean isPublicStatus() {
    return publicStatus;
  }

  public String getTitle() {
    return title;
  }

  public String getText() {
    return text;
  }

  public String getArtists() {
    return artists;
  }

  public String getFileId() {
    return fileId;
  }

  public String getSoundcloudUrl() {
    return soundcloudUrl;
  }

  public int getLinkType() {
    return linkType;
  }

  public String getLinkUrl() {
    return linkUrl;
  }

  public String getContentId() {
    return contentId;
  }

  public int getDownloadType() {
    return downloadType;
  }

  public List<String> getSpotMapTags() {
    return spotMapTags;
  }

  public float getScaleX() {
    return scaleX;
  }

  public String getVideoUrl() {
    return videoUrl;
  }

  public boolean isShowContentOnSpotmap() {
    return showContentOnSpotmap;
  }

  public String getAltText() {
    return altText;
  }
}
