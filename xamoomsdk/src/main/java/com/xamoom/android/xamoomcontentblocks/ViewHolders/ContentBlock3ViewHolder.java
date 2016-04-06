package com.xamoom.android.xamoomcontentblocks.ViewHolders;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
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
import com.xamoom.android.xamoomcontentblocks.SvgDecoder;
import com.xamoom.android.xamoomcontentblocks.SvgDrawableTranscoder;
import com.xamoom.android.xamoomcontentblocks.SvgSoftwareLayerSetter;
import com.xamoom.android.xamoomsdk.R;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;

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

    public void setupContentBlock(final ContentBlock contentBlock) {
        mTitleTextView.setVisibility(View.VISIBLE);
        if(contentBlock.getTitle() != null)
            mTitleTextView.setText(contentBlock.getTitle());
        else {
            mTitleTextView.setVisibility(View.GONE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mImageView.getLayoutParams();
            params.setMargins(0,0,0,0);
            mImageView.setLayoutParams(params);
        }

        setImageViewContentDescription(contentBlock);

        if(contentBlock.getFileId() != null) {
            final double scaleX;
            //checking scale and divide it by 100.0f
            if(contentBlock.getScaleX() != 0) {
                scaleX = contentBlock.getScaleX()/100.0f;
            } else {
                scaleX = 100.0f/100;
            }

            if (contentBlock.getFileId().contains(".svg")) {
                Uri uri = Uri.parse(contentBlock.getFileId());
                requestBuilder
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                //.placeholder(R.drawable.placeholder)
                        .load(uri)
                        .into(mImageView);
                mImageProgressBar.setVisibility(View.GONE);

            } else {
                //making the scaleX to a factor scaleX

                Glide.with(mFragment)
                        .load(contentBlock.getFileId())
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

        if(contentBlock.getLinkUrl() != null && contentBlock.getLinkUrl() != "") {
            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(contentBlock.getLinkUrl()));
                    mFragment.getActivity().startActivity(intent);
                }
            });
        }

        mImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (contentBlock.getFileId().contains(".svg") || contentBlock.getFileId().contains(".gif")) {
                    Toast.makeText(mFragment.getActivity(), mFragment.getString(R.string.cannot_save_image), Toast.LENGTH_SHORT).show();
                    return false;
                }
                try {
                    Glide.with(mFragment)
                            .load(contentBlock.getFileId())
                            .asBitmap()
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                    MediaStore.Images.Media.insertImage(mFragment.getActivity().getContentResolver(), resource, contentBlock.getTitle(), "");
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

    public void setImageViewContentDescription(ContentBlock contentBlock) {
        if (contentBlock.getAltText() != null && !contentBlock.getAltText().equalsIgnoreCase("")) {
            mImageView.setContentDescription(contentBlock.getAltText());
        } else if (contentBlock.getTitle() != null && !contentBlock.getTitle().equalsIgnoreCase("")) {
            mImageView.setContentDescription(contentBlock.getTitle());
        }
    }

    public void resizeImageViewWithScaling(ImageView imageView, Fragment fragment, double scaleX) {
        int deviceWidth = mFragment.getResources().getDisplayMetrics().widthPixels;
        double margin = 0;// TODO mFragment.getView().getLayoutParams().width;

        //calculate the diff between imageSize and scaledImageSize
        double deviceWidthWithMargins =  (deviceWidth - (margin * 2));
        double diff = deviceWidthWithMargins - (deviceWidthWithMargins*scaleX);
        //set left and right margin to the half of the difference
        //so the imageView is doing all the resizing
        mImageView.setPadding((int)(diff/2),0,(int)(diff/2),0);
    }
}