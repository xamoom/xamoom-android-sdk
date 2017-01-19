/*
* Copyright 2017 by xamoom GmbH <apps@xamoom.com>
*
* This file is part of some open source application.
*
* Some open source application is free software: you can redistribute
* it and/or modify it under the terms of the GNU General Public
* License as published by the Free Software Foundation, either
* version 2 of the License, or (at your option) any later version.
*
* Some open source application is distributed in the hope that it will
* be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
* of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with xamoom-android-sdk. If not, see <http://www.gnu.org/licenses/>.
*
* author: Raphael Seher <raphael@xamoom.com>
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
