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
}
