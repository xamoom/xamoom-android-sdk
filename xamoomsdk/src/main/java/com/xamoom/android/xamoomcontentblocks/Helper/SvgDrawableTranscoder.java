/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.xamoomcontentblocks.Helper;

import android.graphics.Picture;
import android.graphics.drawable.PictureDrawable;

import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.SimpleResource;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.caverock.androidsvg.PreserveAspectRatio;
import com.caverock.androidsvg.SVG;

/**
 * Convert the SVG's internal representation to an Android-compatible one ({@link android.graphics.Picture Picture}).
 */
public class SvgDrawableTranscoder implements ResourceTranscoder<SVG, PictureDrawable> {
    private int mDeviceWidth;

    @Override
    public Resource<PictureDrawable> transcode(Resource<SVG> toTranscode) {
        SVG svg = toTranscode.get();
        Picture picture;
        if (svg.getDocumentWidth() != -1) {
            svg.setDocumentPreserveAspectRatio(PreserveAspectRatio.FULLSCREEN);
            picture = svg.renderToPicture();
        } else {
            picture = svg.renderToPicture(mDeviceWidth, Math.round(mDeviceWidth/svg.getDocumentAspectRatio()));
        }

        PictureDrawable drawable = new PictureDrawable(picture);
        return new SimpleResource<>(drawable);
    }

    public void setmDeviceWidth(int mDeviceWidth) {
        this.mDeviceWidth = mDeviceWidth;
    }

    @Override
    public String getId() {
        return "";
    }
}
