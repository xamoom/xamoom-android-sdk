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
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.xamoom.android.xamoomsdk.EnduserApi;
import com.xamoom.android.xamoomsdk.R;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;

/**
 * Displays the forms ContentBlock.
 */
public class ContentBlock15ViewHolder extends RecyclerView.ViewHolder {
    WebView formWebView;
    CoordinatorLayout rootLayout;
    ProgressBar progressBar;

    EnduserApi mEnduserApi;
    SharedPreferences sharedPreferences;


    @RequiresApi(api = Build.VERSION_CODES.N)
    public ContentBlock15ViewHolder(View itemView, EnduserApi enduserApi, Context context) {
        super(itemView);
        this.mEnduserApi = enduserApi;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        rootLayout = itemView.findViewById(R.id.rootLayout);
        formWebView = itemView.findViewById(R.id.webView_form);
        progressBar = itemView.findViewById(R.id.form_progressBar);

    }

    @SuppressLint("SetJavaScriptEnabled")
    public void setupContentBlock(ContentBlock contentBlock) {
        boolean isFormActive = sharedPreferences.getBoolean("is_form_active", false);
        if (isFormActive) {
            setWebViewSettings();
            String formBaseUrl = getModifiedFormUrl(sharedPreferences.getString("form_base_url", null));
            String formId = contentBlock.getText();
            String html = formBaseUrl + "/gfembed/?f=" + formId;
            formWebView.loadUrl(html);
//            String html = "<iframe src=\"" + formBaseUrl + "/gfembed/?f=" + formId + "\" width=\"100%\" height=\"500\" frameBorder=\"0\" class=\"gfiframe\"></iframe>\n" +
//                    "<script src=\"" + formBaseUrl + "/wp-content/plugins/gravity-forms-iframe-develop/assets/scripts/gfembed.js\" type=\"text/javascript\"></script>\n";

//            formWebView.loadData(html, "text/html", null);
        } else {
            rootLayout.setVisibility(View.GONE);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setWebViewSettings() {
        formWebView.getSettings().setJavaScriptEnabled(true);
        formWebView.getSettings().setStandardFontFamily("Roboto-Light");
        formWebView.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
        formWebView.setWebViewClient(new WebViewClient() {
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
    }

    private String getModifiedFormUrl(String url) {
        if (!TextUtils.isEmpty(url))
            return url.charAt(url.length() - 1) != '/' ? url : new StringBuilder(url).deleteCharAt(url.length() - 1).toString();
        else return "";
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }


}
