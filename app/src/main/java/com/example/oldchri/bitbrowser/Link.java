package com.example.oldchri.bitbrowser;

import android.graphics.Bitmap;

/**
 * Created by oldchri on 18/10/2017.
 */

public class Link {
    private String title;
    private String url;
    private Bitmap favicon;

    public Link(String title, String url, Bitmap favicon) {
        this.title = title;
        this.url = url;
        this.favicon = favicon;
    }
    public Bitmap getFavicon() {
        return favicon;
    }

    public void setFavicon(Bitmap favicon) {
        this.favicon = favicon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
