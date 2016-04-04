package com.xamoom.android.xamoomsdk.Resource;

import java.util.List;

import at.rags.morpheus.Annotations.SerializeName;
import at.rags.morpheus.Resource;

public class ContentBlock extends Resource {
  @SerializeName("block-type")
  private int blockType;
  @SerializeName("is-public")
  private boolean publicStatus;
  private String title;
  private String text;
  private String artists;
  @SerializeName("file-id")
  private String fileId;
  @SerializeName("soundcloud-url")
  private String soundcloudUrl;
  @SerializeName("link-type")
  private int linkType;
  @SerializeName("link-url")
  private String linkUrl;
  @SerializeName("content-id")
  private String contentId;
  @SerializeName("download-type")
  private int downloadType;
  @SerializeName("spot-map-tags")
  private List<String> spotMapTags;
  @SerializeName("scale-x")
  private float scaleX;
  @SerializeName("video-url")
  private String videoUrl;
  @SerializeName("should-show-content-on-spotmap")
  private boolean showContentOnSpotmap;
  @SerializeName("alt-text")
  private String altText;

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
