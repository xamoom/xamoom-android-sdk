/*
 * Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the root of this project.
 */

package com.xamoom.android.xamoomcontentblocks.ViewHolders;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.xamoom.android.xamoomcontentblocks.XamoomContentFragment;
import com.xamoom.android.xamoomsdk.EnduserApi;
import com.xamoom.android.xamoomsdk.R;
import com.xamoom.android.xamoomsdk.Resource.Content;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;

/**
 * Displays the forms ContentBlock.
 */
public class ContentBlock15ViewHolder extends RecyclerView.ViewHolder {
    private WebView formWebView;
    private CoordinatorLayout rootLayout;
    private ProgressBar progressBar;

    private EnduserApi mEnduserApi;
    private SharedPreferences sharedPreferences;
    private Context context;

    private String loadedUrl;
    private ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> uploadMessage;


    public static final int REQUEST_SELECT_FILE = 100;
    private final static int FILECHOOSER_RESULTCODE = 777;

    private OnContentBlock15ViewHolderInteractionListener mListener;


    @RequiresApi(api = Build.VERSION_CODES.N)
    public ContentBlock15ViewHolder(View itemView, EnduserApi enduserApi, Context context,
                                    OnContentBlock15ViewHolderInteractionListener onContentBlock15ViewHolderInteractionListener) {
        super(itemView);
        this.mListener = onContentBlock15ViewHolderInteractionListener;
        this.context = context;
        this.mEnduserApi = enduserApi;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.rootLayout = itemView.findViewById(R.id.rootLayout);
        this.formWebView = itemView.findViewById(R.id.webView_form);
        this.progressBar = itemView.findViewById(R.id.form_progressBar);

    }

    @SuppressLint("SetJavaScriptEnabled")
    public void setupContentBlock(ContentBlock contentBlock) {
        boolean isFormActive = sharedPreferences.getBoolean("is_form_active", true);
        if (isFormActive) {
            setWebViewSettings();
            String formBaseUrl = getModifiedFormUrl(sharedPreferences.getString("form_base_url", null));
            String formId = contentBlock.getText();
            String html = formBaseUrl + "/gfembed/?f=" + formId;
            loadedUrl = html;
            formWebView.loadUrl(html);
        } else {
            rootLayout.setVisibility(View.GONE);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setWebViewSettings() {

        formWebView.getSettings().setJavaScriptEnabled(true);
        formWebView.getSettings().setStandardFontFamily("Roboto-Light");
        formWebView.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
        formWebView.getSettings().setAllowFileAccess(true);
        formWebView.getSettings().setGeolocationEnabled(true);
        formWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        if (Build.VERSION.SDK_INT >= 21) {
            formWebView.getSettings().setMixedContentMode(0);
            formWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if (Build.VERSION.SDK_INT >= 19) {
            formWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if (Build.VERSION.SDK_INT < 19) {
            formWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

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

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

        });

        formWebView.setWebChromeClient(new WebChromeClient() {

            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }

            //The undocumented magic method override
            //Eclipse will swear at you if you try to put @Override here
            // For Android 3.0+
            public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> uploadMsg, WebChromeClient.FileChooserParams fileChooserParams) {
                if (uploadMessage != null) {
                    uploadMessage = null;
                }
                uploadMessage = uploadMsg;
                Intent intent = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    intent = fileChooserParams.createIntent();
                }
                try {
                    mListener.startCameraForResult(intent, REQUEST_SELECT_FILE, null, uploadMessage);
                    return true;
                } catch (ActivityNotFoundException e) {
                    uploadMessage = null;
                    return false;
                }
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                mListener.startCameraForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE, uploadMsg, null);
            }

            // For Android 3.0+
            public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                mListener.startCameraForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE, uploadMsg, null);
            }

            //For Android 4.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                mListener.startCameraForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE, uploadMsg, null);

            }
        });

        formWebView.addJavascriptInterface(new FormsJavaScriptInterface(context), "AndroidFunction");
    }

    public class FormsJavaScriptInterface {
        Context mContext;

        FormsJavaScriptInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public void openAndroidDialog() {
            AlertDialog.Builder myDialog
                    = new AlertDialog.Builder(mContext);
            myDialog.setTitle("DANGER!");
            myDialog.setMessage("You can do what you want!");
            myDialog.setPositiveButton("ON", null);
            myDialog.show();
        }
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


    public interface OnContentBlock15ViewHolderInteractionListener {
        void startCameraForResult(Intent intent, Integer resultCode, ValueCallback<Uri> mUploadMessage, ValueCallback<Uri[]> uploadMessage);
    }
}
