package com.ocbg.xyz.apps.common.util;

import com.bumptech.glide.load.model.GlideUrl;

public class V4AuthKeyGlideUrl2 extends GlideUrl {
    private final String mObjKey;

    public V4AuthKeyGlideUrl2(String url, String objKey) {
        super(url);
        mObjKey = objKey;
    }

    @Override
    public String getCacheKey() {
        return mObjKey;
    }
}