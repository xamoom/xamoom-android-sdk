package com.xamoom.android.xamoomsdk.Resource;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import at.rags.morpheus.Annotations.SerializeName;
import at.rags.morpheus.Resource;

/**
 *  xamoom ContentBlock model.
 */
public class ContentBlock extends Resource implements Parcelable {
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
  private double scaleX;
  @SerializeName("video-url")
  private String videoUrl;
  @SerializeName("should-show-content-on-spotmap")
  private boolean showContentOnSpotmap;
  @SerializeName("alt-text")
  private String altText;

  public ContentBlock() {
  }

  protected ContentBlock(Parcel in) {
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

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
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
  }
}
