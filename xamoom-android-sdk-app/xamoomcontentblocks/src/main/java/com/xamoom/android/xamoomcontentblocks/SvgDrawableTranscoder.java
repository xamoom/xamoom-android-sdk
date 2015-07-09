package com.xamoom.android.xamoomcontentblocks;
import android.graphics.Picture;
import android.graphics.drawable.PictureDrawable;
import android.provider.Settings;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.SimpleResource;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.caverock.androidsvg.PreserveAspectRatio;
import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;


/**
 * Convert the {@link SVG}'s internal representation to an Android-compatible one ({@link Picture}).
 */
public class SvgDrawableTranscoder implements ResourceTranscoder<SVG, PictureDrawable> {
    private int mDeviceWidth;

    @Override
    public Resource<PictureDrawable> transcode(Resource<SVG> toTranscode) {
        SVG svg = toTranscode.get();
        //Log.v("pingeborg.xamoom.at", "Width: " + svg.getDocumentWidth() + " Height: " + svg.getDocumentHeight() + " Ratio: " + svg.getDocumentAspectRatio());
        Picture picture;
        if (svg.getDocumentWidth() != -1) {
            svg.setDocumentPreserveAspectRatio(PreserveAspectRatio.FULLSCREEN);
            picture = svg.renderToPicture();
        } else {
            picture = svg.renderToPicture(mDeviceWidth, Math.round(mDeviceWidth/svg.getDocumentAspectRatio()));
        }

        PictureDrawable drawable = new PictureDrawable(picture);
        return new SimpleResource<PictureDrawable>(drawable);
    }

    public void setmDeviceWidth(int mDeviceWidth) {
        this.mDeviceWidth = mDeviceWidth;
    }

    @Override
    public String getId() {
        return "";
    }
}
