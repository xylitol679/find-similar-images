package com.ocbg.xyz.apps.common.util;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.Headers;

public class V4AuthKeyGlideUrl extends GlideUrl {

    private final String mUrl;

    public V4AuthKeyGlideUrl(String url) {
        super(url);
        mUrl = url;
    }

    public V4AuthKeyGlideUrl(String url, Headers headers) {
        super(url, headers);
        mUrl = url;
    }

    @Override
    public String getCacheKey() {
        return mUrl.replace(findTokenParam(), "");
    }

    private String findTokenParam() {
        String tokenParam = "";
        int tokenKeyIndex = mUrl.contains("?auth_key=") ? mUrl.indexOf("?auth_key=") : mUrl.indexOf("&auth_key=");
        if (tokenKeyIndex != -1) {
            int nextAndIndex = mUrl.indexOf("&", tokenKeyIndex + 1);
            if (nextAndIndex != -1) {
                tokenParam = mUrl.substring(tokenKeyIndex + 1, nextAndIndex + 1);
            } else {
                tokenParam = mUrl.substring(tokenKeyIndex);
            }
        }
        return tokenParam;
    }
}