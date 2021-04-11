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
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
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
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.xamoom.android.xamoomcontentblocks.XamoomContentFragment;
import com.xamoom.android.xamoomsdk.EnduserApi;
import com.xamoom.android.xamoomsdk.R;
import com.xamoom.android.xamoomsdk.Resource.Content;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Displays the forms ContentBlock.
 */
public class ContentBlock15ViewHolder extends RecyclerView.ViewHolder {
    private WebView formWebView;
    private CoordinatorLayout rootLayout;
    private ProgressBar progressBar;
    private FrameLayout coverLayout;

    private EnduserApi mEnduserApi;
    private SharedPreferences sharedPreferences;
    private Context context;

    private OnContentBlock15ViewHolderInteractionListener mListener;
    private XamoomContentFragment.OnXamoomContentFragmentInteractionListener onQuizResponseListener;

    private ValueCallback<Uri> mUploadMessage;
    private Uri mCapturedImageURI = null;
    private ValueCallback<Uri[]> mFilePathCallback;
    private String mCameraPhotoPath;
    private static final int INPUT_FILE_REQUEST_CODE = 1;
    private static final int FILECHOOSER_RESULTCODE = 1;
    private static final String TAG = ContentBlock15ViewHolder.class.getSimpleName();


    @RequiresApi(api = Build.VERSION_CODES.N)
    public ContentBlock15ViewHolder(View itemView, EnduserApi enduserApi, Context context,
                                    OnContentBlock15ViewHolderInteractionListener onContentBlock15ViewHolderInteractionListener, XamoomContentFragment.OnXamoomContentFragmentInteractionListener onXamoomContentFragmentInteractionListener) {
        super(itemView);
        this.mListener = onContentBlock15ViewHolderInteractionListener;
        this.onQuizResponseListener = onXamoomContentFragmentInteractionListener;
        this.context = context;
        this.mEnduserApi = enduserApi;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.rootLayout = itemView.findViewById(R.id.rootLayout);
        this.formWebView = itemView.findViewById(R.id.webView_form);
        this.progressBar = itemView.findViewById(R.id.form_progressBar);
        this.coverLayout = itemView.findViewById(R.id.form_cover_layout);
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void setupContentBlock(ContentBlock contentBlock) {
        boolean isFormActive = sharedPreferences.getBoolean("is_form_active", true);
        if (isFormActive) {
            setWebViewSettings();
            String formBaseUrl = getModifiedFormUrl(sharedPreferences.getString("form_base_url", null));
            String formId = contentBlock.getText();
            String html = formBaseUrl + "/gfembed/?f=" + formId;
            formWebView.loadUrl(html);
        } else {
            rootLayout.setVisibility(View.GONE);
        }
    }

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface", "ClickableViewAccessibility"})
    private void setWebViewSettings() {


        formWebView.getSettings().setJavaScriptEnabled(true);
        formWebView.getSettings().setStandardFontFamily("Roboto-Light");
        formWebView.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
        formWebView.getSettings().setAllowFileAccess(true);
        formWebView.getSettings().setGeolocationEnabled(true);
        formWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        formWebView.getSettings().setGeolocationDatabasePath(context.getFilesDir().getPath());
        if (Build.VERSION.SDK_INT >= 21) {
            formWebView.getSettings().setMixedContentMode(0);
            formWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if (Build.VERSION.SDK_INT >= 19) {
            formWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if (Build.VERSION.SDK_INT < 19) {
            formWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }


        if (mListener.isQuizSubmitted()) {
            coverLayout.setVisibility(View.VISIBLE);
            formWebView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return true;
                }
            });
        }

        formWebView.addJavascriptInterface(new ShowHtmlJavaScriptInterface(onQuizResponseListener), "HTMLOUT");

        formWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                hideLoading();
                view.loadUrl("javascript:window.HTMLOUT.showHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
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

            private File createImageFile() throws IOException {

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "_";
                File storageDir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES);
                File imageFile = File.createTempFile(
                        imageFileName,  /* prefix */
                        ".jpg",         /* suffix */
                        storageDir      /* directory */
                );
                return imageFile;

            }

            public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> filePath, WebChromeClient.FileChooserParams fileChooserParams) {
                mFilePathCallback = filePath;

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                        takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        Log.e(TAG, "Unable to create Image File", ex);
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(photoFile));
                    } else {
                        takePictureIntent = null;
                    }
                }
                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("image/*");
                Intent[] intentArray;
                if (takePictureIntent != null) {
                    intentArray = new Intent[]{takePictureIntent};
                } else {
                    intentArray = new Intent[0];
                }
                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                mListener.startCameraForResult(chooserIntent, FILECHOOSER_RESULTCODE, null, mFilePathCallback, mCameraPhotoPath, mCapturedImageURI);
                return true;
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;
                File imageStorageDir = new File(
                        Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES)
                        , "AndroidExampleFolder");
                if (!imageStorageDir.exists()) {
                    // Create AndroidExampleFolder at sdcard
                    imageStorageDir.mkdirs();
                }
                // Create camera captured image file path and name
                File file = new File(
                        imageStorageDir + File.separator + "IMG_"
                                + String.valueOf(System.currentTimeMillis())
                                + ".jpg");
                mCapturedImageURI = Uri.fromFile(file);
                // Camera capture image intent
                final Intent captureIntent = new Intent(
                        android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                // Create file chooser intent
                Intent chooserIntent = Intent.createChooser(i, "Image Chooser");
                // Set camera intent to file chooser
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS
                        , new Parcelable[]{captureIntent});
                // On select image call onActivityResult method of activity
                mListener.startCameraForResult(chooserIntent, FILECHOOSER_RESULTCODE, mUploadMessage, null, mCameraPhotoPath, mCapturedImageURI);
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                        String acceptType,
                                        String capture) {
                openFileChooser(uploadMsg, acceptType);
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

    private boolean isQuizSubmitted(String pageId) {
        return false;
    }


    public interface OnContentBlock15ViewHolderInteractionListener {
        void startCameraForResult(Intent intent, Integer resultCode, ValueCallback<Uri> mUploadMessage, ValueCallback<Uri[]> mFilePathCallback, String mCameraPhotoPath, Uri mCapturedImageURI);

        boolean isQuizSubmitted();

    }
}

class ShowHtmlJavaScriptInterface {

    private XamoomContentFragment.OnXamoomContentFragmentInteractionListener onQuizResponseListener;

    public ShowHtmlJavaScriptInterface(XamoomContentFragment.OnXamoomContentFragmentInteractionListener onQuizResponseListener) {
        this.onQuizResponseListener = onQuizResponseListener;
    }

    @JavascriptInterface
    public void showHTML(String result) {
        onQuizResponseListener.onQuizHtmlResponse(result);

    }

}
