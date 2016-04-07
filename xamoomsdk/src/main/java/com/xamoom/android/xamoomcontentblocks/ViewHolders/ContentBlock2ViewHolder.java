package com.xamoom.android.xamoomcontentblocks.ViewHolders;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeIntents;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.squareup.picasso.Picasso;
import com.xamoom.android.xamoomsdk.R;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * VideoBlock
 */
public class ContentBlock2ViewHolder extends RecyclerView.ViewHolder {

  final static String youtubeRegex = "(?:youtube(?:-nocookie)?\\.com\\/(?:[^\\/\\n\\s]+\\/\\S+\\/|(?:v|e(?:mbed)?)\\/|\\S*?[?&]v=)|youtu\\.be\\/)([a-zA-Z0-9_-]{11})";
  final static String vimeoRegex = "^.*(?:vimeo.com)\\/(?:channels\\/|groups\\/[^\\/]*\\/videos\\/|album\\/\\d+\\/video\\/|video\\/|)(\\d+)(?:$|\\/|\\?)";

  private Fragment mFragment;
  private TextView mTitleTextView;
  private WebView mVideoWebView;
  private View mWebViewOverlay;
  private YouTubeThumbnailView mYouTubeThumbnailView;
  private ImageView mVideoPlayImageView;

  private static HashMap<String, Bitmap> mBitmapCache = new HashMap<>();

  public ContentBlock2ViewHolder(View itemView, Fragment fragment) {
    super(itemView);
    mFragment = fragment;
    mTitleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
    mVideoWebView = (WebView) itemView.findViewById(R.id.videoWebView);
    mWebViewOverlay = (View) itemView.findViewById(R.id.webViewOverlay);
    mYouTubeThumbnailView = (YouTubeThumbnailView) itemView.findViewById(R.id.youtube_thumbnail_view);
    mVideoPlayImageView = (ImageView) itemView.findViewById(R.id.video_play_image_view);
    WebSettings webSettings = mVideoWebView.getSettings();
    webSettings.setJavaScriptEnabled(true);
  }

  public void setupContentBlock(ContentBlock contentBlock) {
    mTitleTextView.setVisibility(View.GONE);
    mYouTubeThumbnailView.setVisibility(View.GONE);
    mVideoWebView.setVisibility(View.GONE);
    mWebViewOverlay.setVisibility(View.GONE);
    mVideoPlayImageView.setVisibility(View.GONE);

    if(contentBlock.getTitle() != null) {
      mTitleTextView.setVisibility(View.VISIBLE);
      mTitleTextView.setText(contentBlock.getTitle());
    }

    if(getYoutubeVideoId(contentBlock.getVideoUrl()) != null) {
      mYouTubeThumbnailView.setVisibility(View.VISIBLE);
      mVideoPlayImageView.setVisibility(View.VISIBLE);
      setupYoutube(contentBlock);
    } else if (contentBlock.getVideoUrl().contains("vimeo.com/")) {
      mWebViewOverlay.setVisibility(View.VISIBLE);
      mVideoWebView.setVisibility(View.VISIBLE);
      setupVimeo(contentBlock);
    } else {
      mYouTubeThumbnailView.setVisibility(View.VISIBLE);
      mVideoPlayImageView.setVisibility(View.VISIBLE);
      setupHTMLPlayer(contentBlock);
    }
  }

  private void setupVimeo(final ContentBlock contentBlock) {
    String vimeoEmbed = "<iframe src=\"https://player.vimeo.com/video/"
        + getVimeoVideoId(contentBlock.getVideoUrl()) + "?badge=0\" width=\"100%%\" " +
        "height=\"100%%\" frameborder=\"0\" webkitallowfullscreen mozallowfullscreen " +
        "allowfullscreen></iframe>";
    mVideoWebView.loadData(vimeoEmbed, "text/html", "UTF-8");
    mWebViewOverlay.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(contentBlock.getVideoUrl()));
        mFragment.getActivity().startActivity(browserIntent);
      }
    });
  }

  public void setupHTMLPlayer(final ContentBlock contentBlock) {
    if (mBitmapCache.get(contentBlock.getVideoUrl()) != null) {
      mYouTubeThumbnailView.setImageBitmap(mBitmapCache.get(contentBlock.getVideoUrl()));
    } else {
        new VideoThumbnail().execute(contentBlock.getVideoUrl());
    }

    mYouTubeThumbnailView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(contentBlock.getVideoUrl()));
        intent.setDataAndType(Uri.parse(contentBlock.getVideoUrl()), "video/mp4");
        mFragment.startActivity(intent);
      }
    });
  }

  public void setupYoutube(ContentBlock contentBlock) {
    final String youtubeVideoId = getYoutubeVideoId(contentBlock.getVideoUrl());
    mYouTubeThumbnailView.initialize("AIzaSyBNZUh3-dj4YYY9-csOtQeHG_MpoE8x69Q", new YouTubeThumbnailView.OnInitializedListener() {
      @Override
      public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
        youTubeThumbnailLoader.setVideo(youtubeVideoId);
        youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
          @Override
          public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
            Log.v("Test", "Success " + youTubeThumbnailView);
          }

          @Override
          public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
            mYouTubeThumbnailView.setBackgroundColor(R.color.black);
          }
        });
      }

      @Override
      public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
      }
    });

    mYouTubeThumbnailView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = YouTubeIntents.createPlayVideoIntent(mFragment.getActivity(), youtubeVideoId);
        mFragment.getActivity().startActivity(intent);
      }
    });
  }

  public String getYoutubeVideoId(String videoUrl) {
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

  private class VideoThumbnail extends AsyncTask<String, Void, Bitmap> {
    private String videoUrl;

    @Override
    protected Bitmap doInBackground(String... params) {
      videoUrl = params[0];
      return retriveVideoFrameFromVideo(params[0]);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
      super.onPostExecute(bitmap);
      mYouTubeThumbnailView.setImageBitmap(bitmap);
      mBitmapCache.put(videoUrl, bitmap);
    }

    public Bitmap retriveVideoFrameFromVideo(String videoPath) {
      Bitmap bitmap = null;
      MediaMetadataRetriever mediaMetadataRetriever = null;
      try {
        mediaMetadataRetriever = new MediaMetadataRetriever();
        if (Build.VERSION.SDK_INT >= 14) {
          mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
        } else {
          mediaMetadataRetriever.setDataSource(videoPath);
        }
        bitmap = mediaMetadataRetriever.getFrameAtTime();
      }
      catch (Exception e) {
        e.printStackTrace();
        return null;
      } finally {
        if (mediaMetadataRetriever != null) {
          mediaMetadataRetriever.release();
        }
      }
      return bitmap;
    }
  }
}
