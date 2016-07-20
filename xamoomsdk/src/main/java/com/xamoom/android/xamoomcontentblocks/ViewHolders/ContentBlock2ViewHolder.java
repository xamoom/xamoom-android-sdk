package com.xamoom.android.xamoomcontentblocks.ViewHolders;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.xamoom.android.xamoomsdk.R;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;
import com.xamoom.android.xamoomsdk.Resource.Style;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * VideoBlock.
 */
public class ContentBlock2ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
  private final static String RESET_YOUTUBE = "ContentBlock2ViewHolder.RESET_YOUTUBE";
  private final static String youtubeRegex = "(?:youtube(?:-nocookie)?\\.com\\/(?:[^\\/\\n\\s]+\\/\\S+\\/|(?:v|e(?:mbed)?)\\/|\\S*?[?&]v=)|youtu\\.be\\/)([a-zA-Z0-9_-]{11})";
  private final static String vimeoRegex = "^.*(?:vimeo.com)\\/(?:channels\\/|groups\\/[^\\/]*\\/videos\\/|album\\/\\d+\\/video\\/|video\\/|)(\\d+)(?:$|\\/|\\?)";

  private int frameId = 1;

  private Context mContext;
  private Fragment mFragment;
  private Style mStyle;
  private TextView mTitleTextView;
  private WebView mVideoWebView;
  private View mWebViewOverlay;
  private FrameLayout mFramelayout;
  private YouTubeThumbnailView mYouTubeThumbnailView;
  private ImageView mVideoPlayImageView;
  private ProgressBar mProgressBar;
  private Intent mIntent;
  private String mYoutubeApiKey;
  private LruCache<String, Bitmap> mBitmapCache;
  private int mTextColor = Color.BLACK;

  public ContentBlock2ViewHolder(View itemView, Fragment fragment, String youtubeApiKey,
                                 LruCache<String, Bitmap> bitmapCache) {
    super(itemView);

    mYoutubeApiKey = youtubeApiKey;
    mContext = fragment.getContext();
    mFragment = fragment;
    mBitmapCache = bitmapCache;

    mTitleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
    mFramelayout = (FrameLayout) itemView.findViewById(R.id.youtube_frame_layout);
    mYouTubeThumbnailView = (YouTubeThumbnailView) itemView.findViewById(R.id.youtube_thumbnail_view);
    mVideoWebView = (WebView) itemView.findViewById(R.id.videoWebView);
    mVideoWebView.setWebViewClient(new WebViewClient());
    mWebViewOverlay = itemView.findViewById(R.id.webViewOverlay);
    mVideoPlayImageView = (ImageView) itemView.findViewById(R.id.video_play_image_view);
    mProgressBar = (ProgressBar) itemView.findViewById(R.id.video_progress_bar);

    itemView.setOnClickListener(this);

    WebSettings webSettings = mVideoWebView.getSettings();
    webSettings.setJavaScriptEnabled(true);
  }

  public void setupContentBlock(ContentBlock contentBlock) {
    mTitleTextView.setVisibility(View.GONE);
    mVideoWebView.setVisibility(View.GONE);
    mWebViewOverlay.setVisibility(View.GONE);
    mVideoPlayImageView.setVisibility(View.GONE);
    mProgressBar.setVisibility(View.GONE);
    mYouTubeThumbnailView.setImageBitmap(null);

    if(contentBlock.getTitle() != null && !contentBlock.getTitle().equalsIgnoreCase("")) {
      mTitleTextView.setVisibility(View.VISIBLE);
      mTitleTextView.setText(contentBlock.getTitle());
      mTitleTextView.setTextColor(mTextColor);
    }

    if(getYoutubeVideoId(contentBlock.getVideoUrl()) != null) {
      mVideoPlayImageView.setVisibility(View.VISIBLE);
      mProgressBar.setVisibility(View.VISIBLE);
      setupYoutube(contentBlock);
    } else if (contentBlock.getVideoUrl().contains("vimeo.com/")) {
      mWebViewOverlay.setVisibility(View.VISIBLE);
      mVideoWebView.setVisibility(View.VISIBLE);
      setupVimeo(contentBlock);
    } else {
      mProgressBar.setVisibility(View.VISIBLE);
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
      mProgressBar.setVisibility(View.GONE);
    } else {
      new VideoThumbnailAsync().execute(contentBlock.getVideoUrl());
    }

    mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(contentBlock.getVideoUrl()));
    mIntent.setDataAndType(Uri.parse(contentBlock.getVideoUrl()), "video/mp4");
  }

  private void setupYoutube(ContentBlock contentBlock) {
    final String youtubeVideoId = getYoutubeVideoId(contentBlock.getVideoUrl());

    Bitmap savedBitmap = mBitmapCache.get(youtubeVideoId);
    if (savedBitmap != null) {
      mProgressBar.setVisibility(View.GONE);
      mYouTubeThumbnailView.setImageBitmap(savedBitmap);
      Log.e("tag", "Loaded bitmap from cache.");
    } else {
      mYouTubeThumbnailView.initialize(mYoutubeApiKey, new YouTubeThumbnailView.OnInitializedListener() {
        @Override
        public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
          youTubeThumbnailLoader.setVideo(youtubeVideoId);
          youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
            @Override
            public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
              mProgressBar.setVisibility(View.GONE);

              Drawable drawable = mYouTubeThumbnailView.getDrawable();
              if (drawable instanceof BitmapDrawable) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable)drawable;
                mBitmapCache.put(youtubeVideoId, bitmapDrawable.getBitmap());
              }
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
    }

    mYouTubeThumbnailView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        sendResetYoutubeBroadcast();
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mResetYoutubeBroadCastReciever,
            new IntentFilter(RESET_YOUTUBE));

        mVideoPlayImageView.setVisibility(View.GONE);

        final FrameLayout frame = new FrameLayout(mContext);
        frame.setId(frameId);

        FrameLayout.LayoutParams layoutParams = layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
              FrameLayout.LayoutParams.MATCH_PARENT);
        frame.setLayoutParams(layoutParams);

        mFramelayout.addView(frame);

        final YouTubePlayerSupportFragment youTubePlayerSupportFragment = YouTubePlayerSupportFragment.newInstance();
        mFragment.getChildFragmentManager()
            .beginTransaction()
            .replace(frame.getId(), youTubePlayerSupportFragment)
            .commit();

        youTubePlayerSupportFragment.initialize(mYoutubeApiKey, new YouTubePlayer.OnInitializedListener() {
          @Override
          public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
            youTubePlayer.loadVideo(youtubeVideoId);
            mProgressBar.setVisibility(View.GONE);
          }

          @Override
          public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
            mProgressBar.setVisibility(View.GONE);
            Log.e("tag", youTubeInitializationResult.toString());
          }
        });
      }
    });
  }

  public BroadcastReceiver mResetYoutubeBroadCastReciever = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      resetYoutube();
      LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mResetYoutubeBroadCastReciever);
      mVideoPlayImageView.setVisibility(View.VISIBLE );

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
      //mYouTubeThumbnailView.setImageBitmap(bitmap);
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

  public void unregisterBroadcast() {
    LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mResetYoutubeBroadCastReciever);
  }

  public void setStyle(Style style) {
    mStyle = style;

    if (style != null && style.getForegroundFontColor() != null) {
      mTextColor = Color.parseColor(style.getForegroundFontColor());
    }
  }
}
