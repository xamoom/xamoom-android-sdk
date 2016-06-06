package com.xamoom.android.xamoomcontentblocks.ViewHolders;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeIntents;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.xamoom.android.xamoomsdk.R;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;
import com.xamoom.android.xamoomsdk.Resource.Style;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * VideoBlock
 */
public class ContentBlock2ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
  private final static String youtubeRegex = "(?:youtube(?:-nocookie)?\\.com\\/(?:[^\\/\\n\\s]+\\/\\S+\\/|(?:v|e(?:mbed)?)\\/|\\S*?[?&]v=)|youtu\\.be\\/)([a-zA-Z0-9_-]{11})";
  private final static String vimeoRegex = "^.*(?:vimeo.com)\\/(?:channels\\/|groups\\/[^\\/]*\\/videos\\/|album\\/\\d+\\/video\\/|video\\/|)(\\d+)(?:$|\\/|\\?)";

  private Context mContext;
  private Style mStyle;
  private TextView mTitleTextView;
  private WebView mVideoWebView;
  private View mWebViewOverlay;
  private YouTubeThumbnailView mYouTubeThumbnailView;
  private ImageView mVideoPlayImageView;
  private ProgressBar mProgressBar;
  private Intent mIntent;
  private String mYoutubeApiKey;
  private LruCache<String, Bitmap> mBitmapCache;

  public ContentBlock2ViewHolder(View itemView, Context context, String youtubeApiKey, LruCache<String, Bitmap> bitmapCache) {
    super(itemView);
    mYoutubeApiKey = youtubeApiKey;
    mContext = context;
    mBitmapCache = bitmapCache;

    mTitleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
    mVideoWebView = (WebView) itemView.findViewById(R.id.videoWebView);
    mVideoWebView.setWebViewClient(new WebViewClient());
    mWebViewOverlay = itemView.findViewById(R.id.webViewOverlay);
    mYouTubeThumbnailView = (YouTubeThumbnailView) itemView.findViewById(R.id.youtube_thumbnail_view);
    mVideoPlayImageView = (ImageView) itemView.findViewById(R.id.video_play_image_view);
    mProgressBar = (ProgressBar) itemView.findViewById(R.id.video_progress_bar);

    itemView.setOnClickListener(this);

    WebSettings webSettings = mVideoWebView.getSettings();
    webSettings.setJavaScriptEnabled(true);
  }

  public void setupContentBlock(ContentBlock contentBlock) {
    mTitleTextView.setVisibility(View.GONE);
    mYouTubeThumbnailView.setVisibility(View.GONE);
    mVideoWebView.setVisibility(View.GONE);
    mWebViewOverlay.setVisibility(View.GONE);
    mVideoPlayImageView.setVisibility(View.GONE);
    mProgressBar.setVisibility(View.GONE);

    mYouTubeThumbnailView.setImageBitmap(null);

    if(contentBlock.getTitle() != null && !contentBlock.getTitle().equalsIgnoreCase("")) {
      mTitleTextView.setVisibility(View.VISIBLE);
      mTitleTextView.setText(contentBlock.getTitle());
    }

    if(getYoutubeVideoId(contentBlock.getVideoUrl()) != null) {
      mYouTubeThumbnailView.setVisibility(View.VISIBLE);
      mVideoPlayImageView.setVisibility(View.VISIBLE);
      mProgressBar.setVisibility(View.VISIBLE);
      setupYoutube(contentBlock);
    } else if (contentBlock.getVideoUrl().contains("vimeo.com/")) {
      mWebViewOverlay.setVisibility(View.VISIBLE);
      mVideoWebView.setVisibility(View.VISIBLE);
      setupVimeo(contentBlock);
    } else {
      mProgressBar.setVisibility(View.VISIBLE);
      mYouTubeThumbnailView.setVisibility(View.VISIBLE);
      mVideoPlayImageView.setVisibility(View.VISIBLE);
      setupHTMLPlayer(contentBlock);
    }
  }

  @Override
  public void onClick(View v) {
    mContext.startActivity(mIntent);
  }

  private void setupVimeo(ContentBlock contentBlock) {
    String vimeoEmbed = "<style>html,body,iframe{padding:0; margin:0;}</style><iframe src=\"https://player.vimeo.com/video/"
        + getVimeoVideoId(contentBlock.getVideoUrl()) + "?badge=0&byline=0\" width=\"100%%\" " +
        "height=\"100%%\" frameborder=\"0\" webkitallowfullscreen mozallowfullscreen " +
        "allowfullscreen></iframe>";
    mVideoWebView.loadData(vimeoEmbed, "text/html", "UTF-8");

    mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(contentBlock.getVideoUrl()));
  }

  private void setupHTMLPlayer(final ContentBlock contentBlock) {
    if (mBitmapCache.get(contentBlock.getVideoUrl()) != null) {
      mYouTubeThumbnailView.setImageBitmap(mBitmapCache.get(contentBlock.getVideoUrl()));
      mProgressBar.setVisibility(View.GONE);
    } else {
      new VideoThumbnailAsync().execute(contentBlock.getVideoUrl());
    }


    mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(contentBlock.getVideoUrl()));
    mIntent.setDataAndType(Uri.parse(contentBlock.getVideoUrl()), "video/mp4");
  }

  private void setupYoutube(ContentBlock contentBlock) {
    final String youtubeVideoId = getYoutubeVideoId(contentBlock.getVideoUrl());

    mYouTubeThumbnailView.initialize(mYoutubeApiKey, new YouTubeThumbnailView.OnInitializedListener() {
      @Override
      public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
        youTubeThumbnailLoader.setVideo(youtubeVideoId);
        youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
          @Override
          public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
            mProgressBar.setVisibility(View.GONE);
            youTubeThumbnailLoader.release();
          }

          @Override
          public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
            youTubeThumbnailView.setBackgroundColor(Color.BLACK);
            mProgressBar.setVisibility(View.GONE);
            youTubeThumbnailLoader.release();
          }
        });
      }

      @Override
      public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
        mProgressBar.setVisibility(View.GONE);
      }
    });

    mIntent = YouTubeIntents.createPlayVideoIntent(mContext, youtubeVideoId);
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

  private class VideoThumbnailAsync extends AsyncTask<String, Void, Bitmap> {
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
      if (videoUrl != null && bitmap != null) {
        mBitmapCache.put(videoUrl, bitmap);
      }
      mProgressBar.setVisibility(View.GONE);
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

  public void setStyle(Style style) {
    mStyle = style;
  }
}
