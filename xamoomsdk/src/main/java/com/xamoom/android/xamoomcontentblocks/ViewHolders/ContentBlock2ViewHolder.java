/*
 * Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the root of this project.
 */

package com.xamoom.android.xamoomcontentblocks.ViewHolders;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.collection.LruCache;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.xamoom.android.xamoomsdk.R;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;
import com.xamoom.android.xamoomsdk.Resource.Style;
import com.xamoom.android.xamoomsdk.Storage.FileManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * VideoBlock.
 */
public class ContentBlock2ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
  public final static String RESET_YOUTUBE = "ContentBlock2ViewHolder.RESET_YOUTUBE";
  private final static String youtubeRegex = "(?:youtube(?:-nocookie)?\\.com\\/(?:[^\\/\\n\\s]+\\/\\S+\\/|(?:v|e(?:mbed)?)\\/|\\S*?[?&]v=)|youtu\\.be\\/)([a-zA-Z0-9_-]{11})";
  private final static String youtubeTimeRegex = "(&t=|\\?t=)(?:(\\d+h)?(\\d+m)?(\\d+s)?(\\d+)?)";
  private final static String vimeoRegex = "^.*(?:vimeo.com)\\/(?:channels\\/|groups\\/[^\\/]*\\/videos\\/|album\\/\\d+\\/video\\/|video\\/|)(\\d+)(?:$|\\/|\\?)";

  private Context mContext;
  private Fragment mFragment;
  private Style mStyle;
  private TextView mTitleTextView;
  private WebView mVideoWebView;
  private View mWebViewOverlay;
  private VideoView mVideoView;
  private FrameLayout mFramelayout;
  private YouTubeThumbnailView mYouTubeThumbnailView;
  private ImageView mVideoPlayImageView;
  private ProgressBar mProgressBar;
  private Intent mIntent;
  private String mYoutubeApiKey;
  private LruCache<String, Bitmap> mBitmapCache;
  private Integer mTextColor = null;
  private FileManager mFileManager;

  @SuppressLint("SetJavaScriptEnabled")
  public ContentBlock2ViewHolder(View itemView, Fragment fragment, String youtubeApiKey,
                                 LruCache<String, Bitmap> bitmapCache) {
    super(itemView);

    mYoutubeApiKey = youtubeApiKey;
    mContext = fragment.getContext();
    mFragment = fragment;
    mBitmapCache = bitmapCache;
    mFileManager = FileManager.getInstance(fragment.getContext());

    mTitleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
    mFramelayout = (FrameLayout) itemView.findViewById(R.id.youtube_frame_layout);
    mYouTubeThumbnailView = (YouTubeThumbnailView) itemView.findViewById(R.id.youtube_thumbnail_view);
    mVideoWebView = (WebView) itemView.findViewById(R.id.videoWebView);
    mVideoWebView.setWebViewClient(new WebViewClient());
    mWebViewOverlay = itemView.findViewById(R.id.webViewOverlay);
    mVideoPlayImageView = (ImageView) itemView.findViewById(R.id.video_play_image_view);
    mProgressBar = (ProgressBar) itemView.findViewById(R.id.video_progress_bar);
    mVideoView = (VideoView) itemView.findViewById(R.id.videoView);
    mIntent = null;

    itemView.setOnClickListener(this);

    WebSettings webSettings = mVideoWebView.getSettings();
    webSettings.setJavaScriptEnabled(true);
  }

  public void setupContentBlock(ContentBlock contentBlock, boolean offline) {
    mTitleTextView.setVisibility(View.GONE);
    mVideoWebView.setVisibility(View.GONE);
    mVideoWebView.setBackgroundColor(Color.TRANSPARENT);
    mWebViewOverlay.setVisibility(View.GONE);
    mVideoPlayImageView.setVisibility(View.GONE);
    mProgressBar.setVisibility(View.GONE);
    mVideoView.setVisibility(View.GONE);
    mYouTubeThumbnailView.setVisibility(View.GONE);
    mYouTubeThumbnailView.setImageBitmap(null);
    mYouTubeThumbnailView.setOnClickListener(null);

    if(contentBlock.getTitle() != null && !contentBlock.getTitle().equalsIgnoreCase("")) {
      mTitleTextView.setVisibility(View.VISIBLE);
      mTitleTextView.setText(contentBlock.getTitle());
    }

    if (mTextColor != null) {
      mTitleTextView.setTextColor(mTextColor);
    }

    if(getYoutubeVideoId(contentBlock.getVideoUrl()) != null) {
      mVideoPlayImageView.setVisibility(View.VISIBLE);
      mYouTubeThumbnailView.setVisibility(View.VISIBLE);
      mProgressBar.setVisibility(View.VISIBLE);

      int seekSeconds = getYoutubeVideoStart(contentBlock.getVideoUrl());

      mVideoWebView.setVisibility(View.VISIBLE);
      setupYoutube(contentBlock, seekSeconds);
    } else if (contentBlock.getVideoUrl().contains("vimeo.com/")) {
      mWebViewOverlay.setVisibility(View.VISIBLE);
      mVideoWebView.setVisibility(View.VISIBLE);
      setupVimeo(contentBlock);
    } else {
      mProgressBar.setVisibility(View.VISIBLE);
      mVideoPlayImageView.setVisibility(View.VISIBLE);
      mVideoView.setVisibility(View.VISIBLE);

      if (offline) {
        String filePath = mFileManager.getFilePath(contentBlock.getVideoUrl());
        if (filePath != null) {
          setupHTMLPlayer(filePath, offline);
        }
      } else {
        setupHTMLPlayer(contentBlock.getVideoUrl(), offline);
      }
    }
  }

  private int getYoutubeVideoStart(String videoUrl) {
    Integer startInSeconds = 0;

    if (videoUrl == null || videoUrl.trim().length() <= 0) {
      return 0;
    }

    Pattern pattern = Pattern.compile(youtubeTimeRegex, Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(videoUrl);

    if (matcher.find()) {
      String hours = matcher.group(2);
      if (hours != null) {
        startInSeconds += Integer.valueOf(hours.replace("h", "")) * 60 * 60; // hours to seconds
      }

      String minutes = matcher.group(3);
      if (minutes != null) {
        startInSeconds += Integer.valueOf(minutes.replace("m", "")) * 60; // minutes to seconds
      }

      String seconds = matcher.group(4);
      if (seconds != null) {
        startInSeconds += Integer.valueOf(seconds.replace("s", ""));
      }

      String timeInSeconds = matcher.group(5);
      if (timeInSeconds != null) {
        startInSeconds = Integer.valueOf(timeInSeconds);
      }
    }

    return startInSeconds;
  }

  @Override
  public void onClick(View v) {
    if (mVideoView.isPlaying()) {
      mVideoView.pause();
      mVideoPlayImageView.setVisibility(View.VISIBLE);
    } else {
      mVideoView.start();
      mVideoPlayImageView.setVisibility(View.GONE);
    }

    if (mIntent != null) {
      mContext.startActivity(mIntent);
    }
  }

  private void setupVimeo(final ContentBlock contentBlock) {
    String vimeoEmbed = "<style>html,body,iframe{padding:0; margin:0;}</style><iframe src=\"https://player.vimeo.com/video/"
        + getVimeoVideoId(contentBlock.getVideoUrl()) + "?badge=0&byline=0\" width=\"100%%\" " +
        "height=\"100%%\" frameborder=\"0\" webkitallowfullscreen mozallowfullscreen " +
        "allowfullscreen></iframe>";
    mVideoWebView.loadData(vimeoEmbed, "text/html", "UTF-8");
  }

  private void setupHTMLPlayer(final String videoPath, boolean offline) {
    mVideoView.setVideoURI(Uri.parse(videoPath));
    mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
      @Override
      public void onPrepared(MediaPlayer mediaPlayer) {
        mVideoView.seekTo(0);
        mProgressBar.setVisibility(View.GONE);
      }
    });
  }

  private void youtubeFallback(final String url) {
    resetYoutube();
    mVideoPlayImageView.setVisibility(View.VISIBLE);
    mYouTubeThumbnailView.setVisibility(View.VISIBLE);
    mYouTubeThumbnailView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        mContext.startActivity(mIntent);
      }
    });
    mYouTubeThumbnailView.setBackgroundColor(
        mContext.getResources().getColor(R.color.black));
  }

  private void setupYoutube(final ContentBlock contentBlock, final int seekSeconds) {
    final String youtubeVideoId = getYoutubeVideoId(contentBlock.getVideoUrl());
    String urlYoutube = "https://www.youtube.com/embed/" + youtubeVideoId;
    if(seekSeconds>0){
      urlYoutube += "?t=" + seekSeconds;
    }
    mVideoPlayImageView.setVisibility(View.GONE);
    mYouTubeThumbnailView.setVisibility(View.GONE);
    mProgressBar.setVisibility(View.GONE);
    String frameVideo = "<html>" +
            "<body>" +
            "<style>html,body,iframe{padding:0; margin:0;}</style>"+
            "<iframe width=\"100%%\" height=\"100%%\" src=\""+urlYoutube+"\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>" +
            "</body>" +
            "</html>";
    WebSettings webSettings = mVideoWebView.getSettings();
    webSettings.setJavaScriptEnabled(true);
    mVideoWebView.loadData(frameVideo, "text/html", "utf-8");
  }

  public BroadcastReceiver mResetYoutubeBroadCastReciever = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      resetYoutube();
      LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mResetYoutubeBroadCastReciever);
      mVideoPlayImageView.setVisibility(View.VISIBLE);
    }
  };

  public void sendResetYoutubeBroadcast() {
    Intent intent = new Intent(RESET_YOUTUBE);
    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
  }

  public void resetYoutube() {
    mFramelayout.removeAllViews();
  }

  private String getYoutubeVideoId(String videoUrl) {
    if (videoUrl == null || videoUrl.trim().length() <= 0) {
      return null;
    }

    Pattern pattern = Pattern.compile(youtubeRegex, Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(videoUrl);

    if (matcher.find()) {
      return matcher.group(1);
    }

    return null;
  }

  public String getVimeoVideoId(String videoUrl) {
    if (videoUrl == null || videoUrl.trim().length() <= 0) {
      return null;
    }

    Pattern pattern = Pattern.compile(vimeoRegex, Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(videoUrl);

    if (matcher.find()) {
      return matcher.group(1);
    }

    return null;
  }

  public void unregisterBroadcast() {
    LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mResetYoutubeBroadCastReciever);
  }

  // getters & setters

  public void setStyle(Style style) {
    mStyle = style;

    if (style != null && style.getForegroundFontColor() != null) {
      mTextColor = Color.parseColor(style.getForegroundFontColor());
    }
  }

  public TextView getTitleTextView() {
    return mTitleTextView;
  }

  public WebView getVideoWebView() {
    return mVideoWebView;
  }

  public View getWebViewOverlay() {
    return mWebViewOverlay;
  }

  public FrameLayout getFramelayout() {
    return mFramelayout;
  }

  public VideoView getVideoView() {
    return mVideoView;
  }

  public void setVideoView(VideoView videoView) {
    mVideoView = videoView;
  }

  public YouTubeThumbnailView getYouTubeThumbnailView() {
    return mYouTubeThumbnailView;
  }

  public ImageView getVideoPlayImageView() {
    return mVideoPlayImageView;
  }

  public ProgressBar getProgressBar() {
    return mProgressBar;
  }

  public int getTextColor() {
    return mTextColor;
  }

  public LruCache<String, Bitmap> getBitmapCache() {
    return mBitmapCache;
  }

  public Style getStyle() {
    return mStyle;
  }

  public void setFileManager(FileManager fileManager) {
    mFileManager = fileManager;
  }
}
