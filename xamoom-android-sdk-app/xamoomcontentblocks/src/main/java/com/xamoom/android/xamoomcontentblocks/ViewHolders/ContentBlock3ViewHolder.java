package com.xamoom.android.xamoomcontentblocks.ViewHolders;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.caverock.androidsvg.SVG;
import com.xamoom.android.mapping.ContentBlocks.ContentBlockType3;
import com.xamoom.android.xamoomcontentblocks.R;
import com.xamoom.android.xamoomcontentblocks.SvgDecoder;
import com.xamoom.android.xamoomcontentblocks.SvgDrawableTranscoder;
import com.xamoom.android.xamoomcontentblocks.SvgSoftwareLayerSetter;

import java.io.InputStream;

/**
 * ImageBlock
 */
public class ContentBlock3ViewHolder extends RecyclerView.ViewHolder {

    private Fragment mFragment;
    public TextView mTitleTextView;
    private ProgressBar mImageProgressBar;
    public ImageView mImageView;
    private GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> requestBuilder;

    public ContentBlock3ViewHolder(View itemView, Fragment fragment) {
        super(itemView);
        mFragment = fragment;
        mTitleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
        mImageView = (ImageView) itemView.findViewById(R.id.imageImageView);
        mImageProgressBar = (ProgressBar) itemView.findViewById(R.id.imageProgressBar);

        SvgDrawableTranscoder svgDrawableTranscoder =  new SvgDrawableTranscoder();
        svgDrawableTranscoder.setmDeviceWidth(mFragment.getActivity().getResources().getDisplayMetrics().widthPixels);

        requestBuilder = Glide.with(mFragment)
                .using(Glide.buildStreamModelLoader(Uri.class, mFragment.getActivity()), InputStream.class)
                .from(Uri.class)
                .as(SVG.class)
                .transcode(svgDrawableTranscoder, PictureDrawable.class)
                .sourceEncoder(new StreamEncoder())
                .cacheDecoder(new FileToStreamDecoder<>(new SvgDecoder()))
                .decoder(new SvgDecoder())
                .listener(new SvgSoftwareLayerSetter<Uri>());
    }

    public void setupContentBlock(final ContentBlockType3 cb3) {
        mTitleTextView.setVisibility(View.VISIBLE);
        if(cb3.getTitle() != null)
            mTitleTextView.setText(cb3.getTitle());
        else {
            mTitleTextView.setVisibility(View.GONE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mImageView.getLayoutParams();
            params.setMargins(0,0,0,0);
            mImageView.setLayoutParams(params);
        }

        setImageViewContentDescription(cb3);

        if(cb3.getFileId() != null) {
            final float scaleX;
            //checking scale and divide it by 100.0f
            if(cb3.getScaleX() != 0) {
                scaleX = cb3.getScaleX()/100.0f;
            } else {
                scaleX = 100.0f/100;
            }

            if (cb3.getFileId().contains(".svg")) {
                Uri uri = Uri.parse(cb3.getFileId());
                requestBuilder
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                //.placeholder(R.drawable.placeholder)
                        .load(uri)
                        .into(mImageView);
                mImageProgressBar.setVisibility(View.GONE);

            } else {
                //making the scaleX to a factor scaleX

                Glide.with(mFragment)
                        .load(cb3.getFileId())
                                //.placeholder(R.drawable.placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                //.fitCenter()
                        .dontAnimate()
                        .into(new GlideDrawableImageViewTarget(mImageView) {
                            public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                                super.onResourceReady(drawable, anim);
                                mImageProgressBar.setVisibility(View.GONE);
                            }
                        });
            }
            resizeImageViewWithScaling(mImageView, mFragment, scaleX);
        }

        if(cb3.getLinkUrl() != null && cb3.getLinkUrl() != "") {
            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(cb3.getLinkUrl()));
                    mFragment.getActivity().startActivity(intent);
                }
            });
        }

        mImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (cb3.getFileId().contains(".svg") || cb3.getFileId().contains(".gif")) {
                    Toast.makeText(mFragment.getActivity(), mFragment.getString(R.string.cannot_save_image), Toast.LENGTH_SHORT).show();
                    return false;
                }
                try {
                    Glide.with(mFragment)
                            .load(cb3.getFileId())
                            .asBitmap()
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                    MediaStore.Images.Media.insertImage(mFragment.getActivity().getContentResolver(), resource, cb3.getTitle(), "");
                                    Toast.makeText(mFragment.getActivity(), mFragment.getString(R.string.image_saved_text), Toast.LENGTH_SHORT).show();
                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return false;
            }
        });
    }

    public void setImageViewContentDescription(ContentBlockType3 contentBlockType3) {
        if (contentBlockType3.getAltText() != null && !contentBlockType3.getAltText().equalsIgnoreCase("")) {
            mImageView.setContentDescription(contentBlockType3.getAltText());
        } else if (contentBlockType3.getTitle() != null && !contentBlockType3.getTitle().equalsIgnoreCase("")) {
            mImageView.setContentDescription(contentBlockType3.getTitle());
        }
    }

    public void resizeImageViewWithScaling(ImageView imageView, Fragment fragment, float scaleX) {
        int deviceWidth = mFragment.getResources().getDisplayMetrics().widthPixels;
        float margin = mFragment.getResources().getDimension(R.dimen.fragment_margin);

        //calculate the diff between imageSize and scaledImageSize
        float deviceWidthWithMargins =  (deviceWidth - (margin * 2));
        float diff = deviceWidthWithMargins - (deviceWidthWithMargins*scaleX);
        //set left and right margin to the half of the difference
        //so the imageView is doing all the resizing
        mImageView.setPadding((int)(diff/2),0,(int)(diff/2),0);
    }
}