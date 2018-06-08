package com.cpm.fragment;


import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.preference.PreferenceManager;

import com.cpm.Constants.CommonString;
import com.cpm.lorealpromoter.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private SharedPreferences preferences = null;
    String noticeboard;
    public MainFragment() {
        // Required empty public constructor
    }

    WebView webView;
    ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       View view = inflater.inflate(R.layout.fragment_main, container, false);
        imageView = (ImageView) view.findViewById(R.id.img_main);
        webView = (WebView) view.findViewById(R.id.webview);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        noticeboard = preferences.getString(CommonString.KEY_notice_board, null);
        webView.setWebViewClient(new MyWebViewClient());


        webView.getSettings().setJavaScriptEnabled(true);
        if (noticeboard!=null){
            webView.loadUrl(noticeboard);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();


    }
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            imageView.setVisibility(View.INVISIBLE);
            webView.setVisibility(View.VISIBLE);
            super.onPageFinished(view, url);
            view.clearCache(true);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            super.onPageStarted(view, url, favicon);
        }

    }
}
