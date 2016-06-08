package com.xamoom.android.xamoomcontentblocks.ViewHolders;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xamoom.android.xamoomsdk.R;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;
import com.xamoom.android.xamoomsdk.Resource.Style;

/**
 * Displays the text ContentBlock.
 */
public class ContentBlock0ViewHolder extends RecyclerView.ViewHolder {
  private static final String FALLBACK_LINK_COLOR = "#0000FF";
  private static final String FALLBACK_TEXT_COLOR = "#000000";

  private TextView mTitleTextView;
  private WebView mWebView;
  private Style mStyle;
  private float mTextSize = 22.0f;
  private String mLinkColor;
  private String mTextColor;

  public ContentBlock0ViewHolder(View itemView) {
    super(itemView);
    mTitleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
    mWebView = (WebView) itemView.findViewById(R.id.webView);
    mWebView.setBackgroundColor(Color.TRANSPARENT);

    if (Build.VERSION.SDK_INT >= 19) {
      mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
    }
    else {
      mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    //override to open links in Webbrowser
    mWebView.setWebViewClient(new WebViewClient() {
      public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.getContext().startActivity(
            new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        return true;
      }
    });
  }

  public void setupContentBlock(ContentBlock contentBlock){
    colorsFromStyle(mStyle);

    mTitleTextView.setVisibility(View.VISIBLE);
    mWebView.setVisibility(View.VISIBLE);

    if(contentBlock.getTitle() != null && !contentBlock.getTitle().equalsIgnoreCase("")) {
      mTitleTextView.setTextSize(mTextSize);
      mTitleTextView.setText(contentBlock.getTitle());

      if (mTextColor != null) {
        mTitleTextView.setTextColor(Color.parseColor(mTextColor));
      }
    } else {
      mTitleTextView.setVisibility(View.GONE);
      LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mWebView.getLayoutParams();
      params.setMargins(0,0,0,0);
      mWebView.setLayoutParams(params);
    }

    if((contentBlock.getText() != null) && !(contentBlock.getText().equalsIgnoreCase("<p><br></p>"))) {
      String style = "<style type=\"text/css\">html, body {margin: 0; padding: 0dp; color: "+mTextColor+"} a {color: "+mLinkColor+"}</style>";
      String htmlAsString = String.format("%s%s", style, cleanHtml(contentBlock.getText()));
      mWebView.loadDataWithBaseURL(null, htmlAsString, "text/html", "UTF-8", null);
    } else {
      mWebView.setVisibility(View.GONE);
    }
  }

  private String cleanHtml(String text) {
    text = text.replace("<br></p>", "</p>");
    return text.replace("<p></p>", "");
  }

  public void setStyle(Style style) {
    mStyle = style;
  }

  private void colorsFromStyle(Style style) {
    if (style == null) {
      mLinkColor = FALLBACK_LINK_COLOR;
      mTextColor = FALLBACK_TEXT_COLOR;
      return;
    }

    if (style.getHighlightFontColor() != null) {
      mLinkColor = style.getHighlightFontColor();
    }

    if (style.getForegroundFontColor() != null) {
      mTextColor = style.getForegroundFontColor();
    }
  }

  public void setTextSize(float textSize) {
    mTextSize = textSize;
  }
}
