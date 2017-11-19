package com.example.oldchri.bitbrowser;

import android.graphics.Bitmap;
import android.webkit.WebViewClient;

/**
 * Created by oldchri on 19/10/2017.
 */

public class Tab {
    private WebViewClient wvc;
    private String url;
    private int pos;

    public Tab(WebViewClient wvc, String url, int pos) {
        this.wvc = wvc;
        this.url = url;
        this.pos = pos;
    }
    public WebViewClient getWvc() { return wvc; }

    public void setWvc(WebViewClient wvc) { this.wvc = wvc; }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
