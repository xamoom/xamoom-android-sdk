package com.xamoom.android.xamoomcontentblocks.ViewHolders;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Utils for ContentBlock0ViewHolder and ContentHeaderViewHolder.
 */
public class ContentBlock0ViewHolderUtil {

  public static void prepareWebView(WebView webView) {
    if (Build.VERSION.SDK_INT == 19) { // needed for displaying html formatted text on api lvl 19
      webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
    } else {
      webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    webView.setWebViewClient(new WebViewClient(){
      @Override
      public void onPageFinished(WebView view, String url) {
        view.getParent().requestLayout();
        view.requestLayout();
        view.invalidate();
        view.getParent().requestLayout();
        super.onPageFinished(view, url);
      }

      @Override
      public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.getContext().startActivity(
            new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        return true;
      }
    });

    webView.setWebChromeClient(new WebChromeClient(){
      @Override
      public void onProgressChanged(WebView view, int newProgress) {
        if(newProgress == 100){
          view.getParent().requestLayout();
          view.requestLayout();
          view.invalidate();
          view.getParent().requestLayout();
        }

        super.onProgressChanged(view, newProgress);
      }
    });

    WebSettings webSettings = webView.getSettings();
    webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
    webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
    webSettings.setAppCacheEnabled(false);
    webSettings.setBlockNetworkImage(true);
    webSettings.setLoadsImagesAutomatically(true);
    webSettings.setGeolocationEnabled(false);
    webSettings.setNeedInitialFocus(false);
    webSettings.setSaveFormData(false);
  }
}
