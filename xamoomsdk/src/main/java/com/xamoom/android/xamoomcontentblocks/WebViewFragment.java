package com.xamoom.android.xamoomcontentblocks;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.DialogFragment;

import com.xamoom.android.xamoomsdk.R;


public class WebViewFragment extends DialogFragment {

    private static String iFrameUrl = "";


    public static WebViewFragment newInstance(String url) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle args = new Bundle();
        args.putString("iFrameUrl", url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            iFrameUrl = (String) getArguments().get("iFrameUrl");
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_webview, container, false);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        WebView webView = view.findViewById(R.id.iframe_web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        WebViewClient webViewClient = new WebViewClient();
        webView.setWebViewClient(webViewClient);
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(iFrameUrl);
       return view;
    }

    @Override
    public int getTheme() {
        return R.style.FullScreenWebViewDialogFragment;
    }
}
