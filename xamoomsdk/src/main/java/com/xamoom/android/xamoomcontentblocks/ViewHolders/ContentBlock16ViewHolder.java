/*
 * Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the root of this project.
 */

package com.xamoom.android.xamoomcontentblocks.ViewHolders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Base64;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.xamoom.android.xamoomcontentblocks.WebViewFragment;
import com.xamoom.android.xamoomcontentblocks.XamoomContentFragment;
import com.xamoom.android.xamoomsdk.EnduserApi;
import com.xamoom.android.xamoomsdk.R;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;


/**
 * Displays the forms ContentBlock.
 */
public class ContentBlock16ViewHolder extends RecyclerView.ViewHolder {
    private final WebView formWebView;
    private final ProgressBar progressBar;
    private final TextView iframeTitle;
    private final Context context;
    private final Fragment fragment;
    private static final String TAG = ContentBlock16ViewHolder.class.getSimpleName();


    @RequiresApi(api = Build.VERSION_CODES.N)
    public ContentBlock16ViewHolder(View itemView, Fragment fragment) {
        super(itemView);
        this.fragment = fragment;
        this.context = fragment.getContext();
        this.formWebView = itemView.findViewById(R.id.webView_form);
        this.progressBar = itemView.findViewById(R.id.form_progressBar);
        this.iframeTitle = itemView.findViewById(R.id.iframeTitle);

    }

    @SuppressLint({"SetJavaScriptEnabled"})
    public void setupContentBlock(ContentBlock contentBlock) {
        String iframeHtml = contentBlock.getIframeUrl();
        String title = contentBlock.getTitle();
        Boolean isFullscreen = contentBlock.getFullScreen();
        String encodedHtml = Base64.encodeToString(iframeHtml.getBytes(), Base64.NO_PADDING);
        setWebViewSettings();
        if (isFullscreen) {
            showFullScreen(iframeHtml);
        }
        formWebView.loadData(encodedHtml, "text/html", "base64");
        if (title != null) {
            iframeTitle.setText(title);
        } else {
            iframeTitle.setVisibility(View.GONE);
        }
    }

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface", "ClickableViewAccessibility"})
    private void setWebViewSettings() {

        formWebView.getSettings().setJavaScriptEnabled(true);
        formWebView.getSettings().setStandardFontFamily("Roboto-Light");
        formWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        formWebView.addJavascriptInterface(new WebAppInterface(context), "Android");
        formWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                hideLoading();
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                hideLoading();
                super.onReceivedError(view, request, error);
            }
        });
        formWebView.setWebChromeClient(new WebChromeClient());
    }

    private void showFullScreen(String url) {
        DialogFragment newFragment = WebViewFragment.newInstance(extractUrlFromString(url));
        newFragment.show(fragment.requireActivity().getSupportFragmentManager(), "TAG");
    }

    private String extractUrlFromString(String iframeHtml) {
        String[] part = iframeHtml.split("\"");
        String url = "";
        for (String key : part) {
            if (key.startsWith("https://")) {
                url = key;
                break;
            }
        }
        return url;
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }
}

class WebAppInterface {
    Context mContext;

    WebAppInterface(Context context){
        mContext = context;
    }

    @JavascriptInterface
    public void showToast (String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }
}
