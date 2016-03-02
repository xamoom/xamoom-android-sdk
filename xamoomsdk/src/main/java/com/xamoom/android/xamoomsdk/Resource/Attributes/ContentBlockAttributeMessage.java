package com.xamoom.android.xamoomsdk.Resource.Attributes;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by raphaelseher on 01/03/16.
 */
public class ContentBlockAttributeMessage {
  @SerializedName("block-type")
  private int blockType;
  @SerializedName("is-public")
  private boolean publicStatus;
  @SerializedName("title")
  private String title;
  @SerializedName("text")
  private String text;
  @SerializedName("artists")
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
  private float scaleX;
  @SerializedName("video-url")
  private String videoUrl;
  @SerializedName("should-show-content-on-spotmap")
  private boolean showContentOnSpotmap;
  @SerializedName("alt-text")
  private String altText;

  public int getBlockType() {
    return blockType;
  }

  public boolean isPublic() {
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
