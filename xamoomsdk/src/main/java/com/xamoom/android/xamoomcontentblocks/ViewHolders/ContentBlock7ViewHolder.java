package com.xamoom.android.xamoomcontentblocks.ViewHolders;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xamoom.android.xamoomsdk.R;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;

import java.util.HashMap;

/**
 * SoundcloudBlock
 */
public class ContentBlock7ViewHolder extends RecyclerView.ViewHolder {

  private TextView mTitleTextView;
  private WebView mSoundCloudWebview;
  private String mSoundCloudHTML = "<body style=\"margin: 0; padding: 0\">" +
      "<iframe width='100%%' height='100%%' scrolling='no'" +
      " frameborder='no' src='https://w.soundcloud.com/player/?url=%s&auto_play=false" +
      "&hide_related=true&show_comments=false&show_comments=false" +
      "&show_user=false&show_reposts=false&sharing=false&download=false&buying=false" +
      "&visual=true'></iframe>" +
      "<script src=\"https://w.soundcloud.com/player/api.js\" type=\"text/javascript\"></script>" +
      "</body>";
  private boolean isSetup = false;

  private static HashMap<String, WebView> mWebCache = new HashMap<>();

  public ContentBlock7ViewHolder(View itemView) {
    super(itemView);
    mTitleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
    mSoundCloudWebview = (WebView) itemView.findViewById(R.id.soundcloudWebview);
    WebSettings webSettings = mSoundCloudWebview.getSettings();
    webSettings.setJavaScriptEnabled(true);
    mSoundCloudWebview.setWebViewClient(new WebViewClient() {
      public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.getContext().startActivity(
            new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        return true;
      }
    });
    mSoundCloudWebview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
    if (Build.VERSION.SDK_INT >= 19) {
      mSoundCloudWebview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
    } else {
      mSoundCloudWebview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }
  }

  public void setupContentBlock(final ContentBlock cb7) {
    mTitleTextView.setVisibility(View.VISIBLE);

    if(cb7.getTitle() != null)
      mTitleTextView.setText(cb7.getTitle());
    else {
      mTitleTextView.setVisibility(View.GONE);
      LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mSoundCloudWebview.getLayoutParams();
      params.setMargins(0,0,0,0);
      mSoundCloudWebview.setLayoutParams(params);
    }

    if(!isSetup) {
      String html = String.format(mSoundCloudHTML, cb7.getSoundcloudUrl());
      mSoundCloudWebview.loadData(html, "text/html", "utf-8");
      isSetup = true;

      mSoundCloudWebview.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
        @Override
        public void onViewAttachedToWindow(View v) {
        }

        @Override
        public void onViewDetachedFromWindow(View v) {
          WebView webview = (WebView)v;
          webview.reload();
        }
      });
    }
  }
}