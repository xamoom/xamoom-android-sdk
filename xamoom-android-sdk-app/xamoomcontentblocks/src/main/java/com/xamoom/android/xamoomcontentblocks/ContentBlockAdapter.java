package com.xamoom.android.xamoomcontentblocks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.PictureDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.provider.Telephony;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.xamoom.android.APICallback;
import com.xamoom.android.XamoomEndUserApi;
import com.xamoom.android.mapping.ContentBlocks.ContentBlock;
import com.xamoom.android.mapping.ContentBlocks.ContentBlockType0;
import com.xamoom.android.mapping.ContentBlocks.ContentBlockType1;
import com.xamoom.android.mapping.ContentBlocks.ContentBlockType2;
import com.xamoom.android.mapping.ContentBlocks.ContentBlockType3;
import com.xamoom.android.mapping.ContentBlocks.ContentBlockType4;
import com.xamoom.android.mapping.ContentBlocks.ContentBlockType5;
import com.xamoom.android.mapping.ContentBlocks.ContentBlockType6;
import com.xamoom.android.mapping.ContentBlocks.ContentBlockType7;
import com.xamoom.android.mapping.ContentBlocks.ContentBlockType8;
import com.xamoom.android.mapping.ContentBlocks.ContentBlockType9;
import com.xamoom.android.mapping.ContentById;
import com.xamoom.android.mapping.Spot;
import com.xamoom.android.mapping.SpotMap;

import org.kobjects.htmlview.Element;
import org.kobjects.htmlview.HtmlView;
import org.kobjects.htmlview.RequestHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit.RetrofitError;

/**
 * ContentBlockAdapter will display all the contentBlocks you get from the xamoom cloud
 * like we think it is good.
 *
 * Create
 *
 * @author Raphael Seher
 */
public class ContentBlockAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Fragment mFragment;
    private List<ContentBlock> mContentBlocks;
    private String mYoutubeApiKey;
    private String mLinkColor;

    public ContentBlockAdapter(Fragment context, List<ContentBlock> contentBlocks, String youtubeApiKey, String linkColor) {
        mFragment = context;
        mContentBlocks = contentBlocks;
        mYoutubeApiKey = youtubeApiKey;
        mLinkColor = linkColor;
    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        return mContentBlocks.get(position).getContentBlockType();
    }

    @Override
    public int getItemCount() {
        return mContentBlocks.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.content_block_0_layout, parent, false);
                return new ContentBlock0ViewHolder(view);
            case 1:
                View view1 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.content_block_1_layout, parent, false);
                return new ContentBlock1ViewHolder(view1, mFragment);
            case 2:
                View view2 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.content_block_2_layout, parent, false);
                return new ContentBlock2ViewHolder(view2, mFragment, mYoutubeApiKey);
            case 3:
                View view3 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.content_block_3_layout, parent, false);
                return new ContentBlock3ViewHolder(view3, mFragment);
            case 4:
                View view4 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.content_block_4_layout, parent, false);
                return new ContentBlock4ViewHolder(view4, mFragment);
            case 5:
                View view5 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.content_block_5_layout, parent, false);
                return new ContentBlock5ViewHolder(view5, mFragment);
            case 6:
                View view6 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.content_block_6_layout, parent, false);
                return new ContentBlock6ViewHolder(view6, mFragment);
            case 7:
                View view7 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.content_block_7_layout, parent, false);
                return new ContentBlock7ViewHolder(view7);
            case 8:
                View view8 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.content_block_8_layout, parent, false);
                return new ContentBlock8ViewHolder(view8, mFragment);
            case 9:
                View view9 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.content_block_9_layout, parent, false);
                return new ContentBlock9ViewHolder(view9, mFragment);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ContentBlock cb = mContentBlocks.get(position);

        switch (holder.getClass().toString()) {
            case "class com.xamoom.android.xamoomcontentblocks.ContentBlock0ViewHolder":
                ContentBlockType0 cb0 = (ContentBlockType0)cb;
                ContentBlock0ViewHolder newHolder = (ContentBlock0ViewHolder) holder;
                newHolder.setLinkColor(mLinkColor);
                newHolder.setupContentBlock(cb0);
                break;
            case "class com.xamoom.android.xamoomcontentblocks.ContentBlock1ViewHolder":
                ContentBlockType1 cb1 = (ContentBlockType1)cb;
                ContentBlock1ViewHolder newHolder1 = (ContentBlock1ViewHolder) holder;
                newHolder1.setupContentBlock(cb1);
                break;
            case "class com.xamoom.android.xamoomcontentblocks.ContentBlock2ViewHolder":
                ContentBlockType2 cb2 = (ContentBlockType2)cb;
                ContentBlock2ViewHolder newHolder2 = (ContentBlock2ViewHolder) holder;
                newHolder2.setupContentBlock(cb2);
                break;
            case "class com.xamoom.android.xamoomcontentblocks.ContentBlock3ViewHolder":
                ContentBlockType3 cb3 = (ContentBlockType3)cb;
                ContentBlock3ViewHolder newHolder3 = (ContentBlock3ViewHolder) holder;
                newHolder3.setupContentBlock(cb3);
                break;
            case "class com.xamoom.android.xamoomcontentblocks.ContentBlock4ViewHolder":
                ContentBlockType4 cb4 = (ContentBlockType4) cb;
                ContentBlock4ViewHolder newHolder4 = (ContentBlock4ViewHolder) holder;
                newHolder4.setupContentBlock(cb4);
                break;
            case "class com.xamoom.android.xamoomcontentblocks.ContentBlock5ViewHolder":
                ContentBlockType5 cb5 = (ContentBlockType5) cb;
                ContentBlock5ViewHolder newHolder5 = (ContentBlock5ViewHolder) holder;
                newHolder5.setupContentBlock(cb5);
                break;
            case "class com.xamoom.android.xamoomcontentblocks.ContentBlock6ViewHolder":
                ContentBlockType6 cb6 = (ContentBlockType6) cb;
                ContentBlock6ViewHolder newHolder6 = (ContentBlock6ViewHolder) holder;
                newHolder6.setupContentBlock(cb6);
                break;
            case "class com.xamoom.android.xamoomcontentblocks.ContentBlock7ViewHolder":
                ContentBlockType7 cb7 = (ContentBlockType7) cb;
                ContentBlock7ViewHolder newHolder7 = (ContentBlock7ViewHolder) holder;
                newHolder7.setupContentBlock(cb7);
                break;
            case "class com.xamoom.android.xamoomcontentblocks.ContentBlock8ViewHolder":
                ContentBlockType8 cb8 = (ContentBlockType8) cb;
                ContentBlock8ViewHolder newHolder8 = (ContentBlock8ViewHolder) holder;
                newHolder8.setupContentBlock(cb8);
                break;
            case "class com.xamoom.android.xamoomcontentblocks.ContentBlock9ViewHolder":
                ContentBlockType9 cb9 = (ContentBlockType9) cb;
                ContentBlock9ViewHolder newHolder9 = (ContentBlock9ViewHolder) holder;
                newHolder9.setupContentBlock(cb9);
                break;
        }
    }
}

/**
 * TextBlock
 */
class ContentBlock0ViewHolder extends RecyclerView.ViewHolder {

    public TextView mTitleTextView;
    public TextView mContentTextView;
    public HtmlView mHTMLView;
    private String mLinkColor = "#00F";

    public ContentBlock0ViewHolder(View itemView) {
        super(itemView);
        mTitleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
        mContentTextView = (TextView) itemView.findViewById(R.id.contentTextView);
        LinearLayout linearLayout = (LinearLayout) itemView.findViewById(R.id.rootLayout);
        mHTMLView = new HtmlView(itemView.getContext());
        linearLayout.addView(mHTMLView);
    }

    public void setupContentBlock(ContentBlockType0 cb0){
        mTitleTextView.setVisibility(View.VISIBLE);
        mContentTextView.setVisibility(View.VISIBLE);

        if(cb0.getTitle() != null) {
            mTitleTextView.setText(cb0.getTitle());
        } else {
            mTitleTextView.setVisibility(View.GONE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mContentTextView.getLayoutParams();
            params.setMargins(0,0,0,0);
            mContentTextView.setLayoutParams(params);
        }

        if(cb0.getText() != null) {
            //mContentTextView.setText(Html.fromHtml(cb0.getText()));
            try {
                mContentTextView.setVisibility(View.GONE);
                mHTMLView.loadHtml("<style>html, body {margin: 0; padding: 0dp;} a {color: #" + mLinkColor.substring(1) + "}</style>" + cb0.getText());

            } catch (Exception e) {
                mContentTextView.setVisibility(View.VISIBLE);
                mContentTextView.setText(Html.fromHtml(cb0.getText()));
            }
        } else {
            mContentTextView.setVisibility(View.GONE);
        }
    }

    public void setLinkColor(String mLinkColor) {
        this.mLinkColor = mLinkColor;
    }
}

/**
 * AudioBlock
 */
class ContentBlock1ViewHolder extends RecyclerView.ViewHolder {
    private Fragment mFragment;
    private TextView mTitleTextView;
    private TextView mArtistTextView;
    private TextView mRemainingSongTimeTextView;
    private Button mPlayPauseButton;
    private MediaPlayer mMediaPlayer;
    private ProgressBar mSongProgressBar;
    private final Handler mHandler = new Handler();
    private Runnable mRunnable;

    public ContentBlock1ViewHolder(View itemView, Fragment activity) {
        super(itemView);
        mFragment = activity;
        mTitleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
        mArtistTextView = (TextView) itemView.findViewById(R.id.artistTextView);
        mPlayPauseButton = (Button) itemView.findViewById(R.id.playPauseButton);
        mRemainingSongTimeTextView = (TextView) itemView.findViewById(R.id.remainingSongTimeTextView);
        mSongProgressBar = (ProgressBar) itemView.findViewById(R.id.songProgressBar);
    }

    public void setupContentBlock(ContentBlockType1 cb1) {
        if (cb1.getTitle() != null)
            mTitleTextView.setText(cb1.getTitle());
        else {
            mTitleTextView.setText(null);
        }

        if (cb1.getArtist() != null)
            mArtistTextView.setText(cb1.getArtist());
        else {
            mArtistTextView.setText(null);
        }

        if (cb1.getFileId() != null) {
            setupMusicPlayer(cb1);
        }
    }

    private void setupMusicPlayer(final ContentBlockType1 cb1) {
        if(mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();

            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mMediaPlayer.setDataSource(mFragment.getActivity(), Uri.parse(cb1.getFileId()));
                mMediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mSongProgressBar.setMax(mMediaPlayer.getDuration());
                    mRemainingSongTimeTextView.setText(getTimeString(mMediaPlayer.getDuration()));

                    mPlayPauseButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mMediaPlayer.isPlaying()) {
                                mMediaPlayer.pause();
                                mPlayPauseButton.setBackgroundResource(R.drawable.ic_play);
                            } else {
                                mMediaPlayer.start();
                                mPlayPauseButton.setBackgroundResource(R.drawable.ic_pause);
                                startUpdatingProgress();
                            }
                        }
                    });

                    mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            stopUpdatingProgress();
                            if (mFragment.getActivity() != null) {
                                setupMusicPlayer(cb1);
                            }
                        }
                    });
                }
            });
        }
    }

    private String getTimeString(int milliseconds) {
        long s = milliseconds % 60;
        long m = (milliseconds / 60) % 60;
        long h = (milliseconds / (60 * 60)) % 24;

        String output;

        if (TimeUnit.MILLISECONDS.toHours(milliseconds) > 0) {
            output = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(milliseconds),
                    TimeUnit.MILLISECONDS.toMinutes(milliseconds) % TimeUnit.HOURS.toMinutes(1),
                    TimeUnit.MILLISECONDS.toSeconds(milliseconds) % TimeUnit.MINUTES.toSeconds(1));
        } else {
            output = String.format("%02d:%02d",TimeUnit.MILLISECONDS.toMinutes(milliseconds) % TimeUnit.HOURS.toMinutes(1),
                    TimeUnit.MILLISECONDS.toSeconds(milliseconds) % TimeUnit.MINUTES.toSeconds(1));
        }

        return output;
    }

    private void startUpdatingProgress() {
        //Make sure you update Seekbar on UI thread
        mRunnable = new Runnable() {
            @Override
            public void run() {
                if (mFragment.getActivity() == null) {
                    stopUpdatingProgress();
                }

                if (mMediaPlayer != null) {
                    int mCurrentPosition = mMediaPlayer.getCurrentPosition();
                    mSongProgressBar.setProgress(mCurrentPosition);
                    mRemainingSongTimeTextView.setText(getTimeString((mMediaPlayer.getDuration() - mCurrentPosition)));
                } else {
                    mHandler.removeCallbacks(this);
                }
                mHandler.postDelayed(this, 100);
            }
        };

        mFragment.getActivity().runOnUiThread(mRunnable);
    }

    private void stopUpdatingProgress() {
        mHandler.removeCallbacks(mRunnable);
        if (mMediaPlayer != null)
            mMediaPlayer.stop();
        mPlayPauseButton.setBackgroundResource(R.drawable.ic_play);
        mSongProgressBar.setProgress(0);
    }
}

/**
 * YoutubeBlock
 */
class ContentBlock2ViewHolder extends RecyclerView.ViewHolder {

    final static String reg = "(?:youtube(?:-nocookie)?\\.com\\/(?:[^\\/\\n\\s]+\\/\\S+\\/|(?:v|e(?:mbed)?)\\/|\\S*?[?&]v=)|youtu\\.be\\/)([a-zA-Z0-9_-]{11})";

    private Fragment mFragment;
    private TextView mTitleTextView;
    private String mYoutubeVideoCode;
    private String mYoutubeApiKey;
    private YouTubePlayerSupportFragment youTubePlayerSupportFragmentFragment;

    public ContentBlock2ViewHolder(View itemView, Fragment activity, String youtubeApiKey) {
        super(itemView);
        mFragment = activity;
        mTitleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
        mYoutubeApiKey = youtubeApiKey;
    }

    public void setupContentBlock(ContentBlockType2 cb2) {
        mYoutubeVideoCode = getVideoId(cb2.getYoutubeUrl());

        mTitleTextView.setVisibility(View.VISIBLE);
        if(cb2.getTitle() != null)
            mTitleTextView.setText(cb2.getTitle());
        else {
            mTitleTextView.setVisibility(View.GONE);
        }

        try {
            if(youTubePlayerSupportFragmentFragment == null) {
                youTubePlayerSupportFragmentFragment = new YouTubePlayerSupportFragment();
                mFragment.getChildFragmentManager().beginTransaction().add(R.id.youtubePlayerFrameLayout, youTubePlayerSupportFragmentFragment).commit();
                youTubePlayerSupportFragmentFragment.initialize(mYoutubeApiKey, new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                        youTubePlayer.setFullscreenControlFlags(0);
                        youTubePlayer.cueVideo(mYoutubeVideoCode);
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String getVideoId(String videoUrl) {
        if (videoUrl == null || videoUrl.trim().length() <= 0)
            return null;

        Pattern pattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(videoUrl);

        if (matcher.find())
            return matcher.group(1);
        return null;
    }

}

/**
 * ImageBlock
 */
class ContentBlock3ViewHolder extends RecyclerView.ViewHolder {

    private Fragment mFragment;
    public TextView mTitleTextView;
    public ImageView mImageView;
    private GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> requestBuilder;

    public ContentBlock3ViewHolder(View itemView, Fragment activity) {
        super(itemView);
        mFragment = activity;
        mTitleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
        mImageView = (ImageView) itemView.findViewById(R.id.imageImageView);

        SvgDrawableTranscoder svgDrawableTranscoder =  new SvgDrawableTranscoder();
        svgDrawableTranscoder.setmDeviceWidth(mFragment.getResources().getDisplayMetrics().widthPixels);

        requestBuilder = Glide.with(mFragment)
        .using(Glide.buildStreamModelLoader(Uri.class, mFragment.getActivity()), InputStream.class)
                .from(Uri.class)
                .as(SVG.class)
                .transcode(svgDrawableTranscoder, PictureDrawable.class)
                .sourceEncoder(new StreamEncoder())
                .cacheDecoder(new FileToStreamDecoder<SVG>(new SvgDecoder()))
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

        if(cb3.getFileId() != null) {
            int deviceWidth = mFragment.getResources().getDisplayMetrics().widthPixels;
            float margin = mFragment.getResources().getDimension(R.dimen.fragment_margin);

            if (cb3.getFileId().contains(".svg")) {
                Uri uri = Uri.parse(cb3.getFileId());
                requestBuilder
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .placeholder(R.drawable.placeholder)
                        .load(uri)
                        .into(mImageView);
            } else {
                Glide.with(mFragment)
                        .load(cb3.getFileId())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        //.placeholder(R.drawable.placeholder)
                        .fitCenter()
                        .override(deviceWidth-(int)(margin*2),2500)
                        .into(mImageView);
            }
        }

        mImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(cb3.getFileId().contains(".svg") || cb3.getFileId().contains(".gif")) {
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
                                    MediaStore.Images.Media.insertImage(mFragment.getActivity().getContentResolver(), resource, cb3.getTitle() , "");
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
}

/**
 * LinkBlock
 */
class ContentBlock4ViewHolder extends RecyclerView.ViewHolder {

    private Fragment mFragment;
    private LinearLayout mRootLayout;
    private TextView mTitleTextView;
    private TextView mContentTextView;
    private ImageView mIcon;

    public ContentBlock4ViewHolder(View itemView, Fragment activity) {
        super(itemView);
        mFragment = activity;
        mRootLayout = (LinearLayout) itemView.findViewById(R.id.linkBlockLinearLayout);
        mTitleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
        mContentTextView = (TextView) itemView.findViewById(R.id.contentTextView);
        mIcon = (ImageView) itemView.findViewById(R.id.iconImageView);
    }

    public void setupContentBlock(final ContentBlockType4 cb4) {
        if(cb4.getTitle() != null)
            mTitleTextView.setText(cb4.getTitle());
        else
            mTitleTextView.setText(null);

        if(cb4.getText() != null)
            mContentTextView.setText(cb4.getText());
        else
            mContentTextView.setText(null);

        mRootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(cb4.getLinkUrl()));
                mFragment.startActivity(i);
            }
        });

        switch (cb4.getLinkType()) {
            case 0:
                mRootLayout.setBackgroundResource(R.color.facebook_linkblock_background_color);
                mIcon.setImageResource(R.drawable.ic_facebook);
                mTitleTextView.setTextColor(Color.WHITE);
                mContentTextView.setTextColor(Color.WHITE);
                break;
            case 1:
                mRootLayout.setBackgroundResource(R.color.twitter_linkblock_background_color);
                mIcon.setImageResource(R.drawable.ic_twitter);
                mTitleTextView.setTextColor(Color.WHITE);
                mContentTextView.setTextColor(Color.WHITE);
                break;
            case 2:
                mRootLayout.setBackgroundResource(R.color.default_linkblock_background_color);
                mIcon.setImageResource(R.drawable.ic_web);
                mTitleTextView.setTextColor(Color.parseColor("#333333"));
                mContentTextView.setTextColor(Color.parseColor("#333333"));
                break;
            case 3:
                mRootLayout.setBackgroundResource(R.color.amazon_linkblock_background_color);
                mIcon.setImageResource(R.drawable.ic_cart);
                mTitleTextView.setTextColor(Color.BLACK);
                mContentTextView.setTextColor(Color.BLACK);
                break;
            case 4:
                mRootLayout.setBackgroundResource(R.color.default_linkblock_background_color);
                mIcon.setImageResource(R.drawable.ic_wikipedia);
                mTitleTextView.setTextColor(Color.BLACK);
                mContentTextView.setTextColor(Color.BLACK);
                break;
            case 5:
                mRootLayout.setBackgroundResource(R.color.linkedin_linkblock_background_color);
                mIcon.setImageResource(R.drawable.ic_linkedin_box);
                mTitleTextView.setTextColor(Color.WHITE);
                mContentTextView.setTextColor(Color.WHITE);
                break;
            case 6:
                mRootLayout.setBackgroundResource(R.color.flickr_linkblock_background_color);
                mIcon.setImageResource(R.drawable.ic_flickr9);
                mTitleTextView.setTextColor(Color.WHITE);
                mContentTextView.setTextColor(Color.WHITE);
                break;
            case 7:
                mRootLayout.setBackgroundResource(R.color.soundcloud_linkblock_background_color);
                mIcon.setImageResource(R.drawable.ic_soundcloud);
                mTitleTextView.setTextColor(Color.WHITE);
                mContentTextView.setTextColor(Color.WHITE);
                break;
            case 8:
                mRootLayout.setBackgroundResource(R.color.default_linkblock_background_color);
                mIcon.setImageResource(R.drawable.ic_itunes);
                mTitleTextView.setTextColor(Color.parseColor("#333333"));
                mContentTextView.setTextColor(Color.parseColor("#333333"));
                break;
            case 9:
                mRootLayout.setBackgroundResource(R.color.youtube_linkblock_background_color);
                mIcon.setImageResource(R.drawable.ic_youtube_play);
                mTitleTextView.setTextColor(Color.WHITE);
                mContentTextView.setTextColor(Color.WHITE);
                break;
            case 10:
                mRootLayout.setBackgroundResource(R.color.googleplus_linkblock_background_color);
                mIcon.setImageResource(R.drawable.ic_google_plus);
                mTitleTextView.setTextColor(Color.WHITE);
                mContentTextView.setTextColor(Color.WHITE);
                break;
            case 11:
                mRootLayout.setBackgroundResource(R.color.default_linkblock_background_color);
                mIcon.setImageResource(R.drawable.ic_phone);
                mTitleTextView.setTextColor(Color.parseColor("#333333"));
                mContentTextView.setTextColor(Color.parseColor("#333333"));
                break;
            case 12:
                mRootLayout.setBackgroundResource(R.color.default_linkblock_background_color);
                mIcon.setImageResource(R.drawable.ic_email);
                mTitleTextView.setTextColor(Color.parseColor("#333333"));
                mContentTextView.setTextColor(Color.parseColor("#333333"));
                break;
            case 13:
                mRootLayout.setBackgroundResource(R.color.spotify_linkblock_background_color);
                mIcon.setImageResource(R.drawable.ic_spotify);
                mTitleTextView.setTextColor(Color.WHITE);
                mContentTextView.setTextColor(Color.WHITE);
                break;
            case 14:
                mRootLayout.setBackgroundResource(R.color.googlemaps_linkblock_background_color);
                mIcon.setImageResource(R.drawable.ic_navigation);
                mTitleTextView.setTextColor(Color.WHITE);
                mContentTextView.setTextColor(Color.WHITE);
                break;
            default:
                mRootLayout.setBackgroundResource(R.color.default_linkblock_background_color);
                mIcon.setImageResource(R.drawable.ic_web);
                mTitleTextView.setTextColor(Color.parseColor("#333333"));
                mContentTextView.setTextColor(Color.parseColor("#333333"));
                break;
        }
    }
}

/**
 * EbookBlock
 */
class ContentBlock5ViewHolder extends RecyclerView.ViewHolder {

    private Fragment mFragment;
    private LinearLayout mRootLayout;
    private TextView mTitleTextView;
    private TextView mContentTextView;

    public ContentBlock5ViewHolder(View itemView, Fragment activity) {
        super(itemView);
        mFragment = activity;
        mRootLayout = (LinearLayout) itemView.findViewById(R.id.linkBlockLinearLayout);
        mTitleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
        mContentTextView = (TextView) itemView.findViewById(R.id.contentTextView);
    }

    public void setupContentBlock(final ContentBlockType5 cb5) {
        if(cb5.getTitle() != null)
            mTitleTextView.setText(cb5.getTitle());
        else
            mTitleTextView.setText(null);

        if(cb5.getArtist() != null)
            mContentTextView.setText(cb5.getArtist());
        else
            mContentTextView.setText(null);

        mRootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(cb5.getFileId()));
                mFragment.startActivity(i);
            }
        });
    }
}

/**
 * ContentBlock
 */
class ContentBlock6ViewHolder extends RecyclerView.ViewHolder {
    private Fragment mFragment;
    private TextView mTitleTextView;
    private TextView mDescriptionTextView;
    private LinearLayout mRootLayout;
    private ImageView mContentThumbnailImageView;

    public ContentBlock6ViewHolder(View itemView, Fragment activity) {
        super(itemView);
        mTitleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
        mDescriptionTextView = (TextView) itemView.findViewById(R.id.descriptionTextView);
        mRootLayout = (LinearLayout) itemView.findViewById(R.id.contentBlockLinearLayout);
        mContentThumbnailImageView = (ImageView) itemView.findViewById(R.id.contentThumbnailImageView);
        mFragment = activity;
    }

    public void setupContentBlock(final ContentBlockType6 cb6) {
        mContentThumbnailImageView.setImageDrawable(null);
        mTitleTextView.setText(null);
        mDescriptionTextView.setText(null);

        XamoomEndUserApi.getInstance().getContentbyIdFull(cb6.getContentId(), false, false, null, false, new APICallback<ContentById>() {
            @Override
            public void finished(ContentById result) {
                mTitleTextView.setText(result.getContent().getTitle());
                mDescriptionTextView.setText(result.getContent().getDescriptionOfContent());

                if (mFragment.isAdded()) {
                    Glide.with(mFragment)
                            .load(result.getContent().getImagePublicUrl())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .crossFade()
                            .centerCrop()
                            .into(mContentThumbnailImageView);
                }
            }

            @Override
            public void error(RetrofitError error) {
            }
        });

        mRootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XamoomContentFragment xamoomContentFragment = (XamoomContentFragment)mFragment;
                xamoomContentFragment.contentBlockClick(cb6.getContentId());
            }
        });
    }
}

/**
 * SoundcloudBlock
 */
class ContentBlock7ViewHolder extends RecyclerView.ViewHolder {

    private TextView mTitleTextView;
    private WebView mSoundCloudWebview;
    private String mSoundCloudHTML = "<body style=\"margin: 0; padding: 0\">" +
            "<iframe width='100%%' height='100%%' scrolling='no'" +
            " frameborder='no' src='https://w.soundcloud.com/player/?url=%s&auto_play=false" +
            "&hide_related=true&show_comments=false&show_comments=false" +
            "&show_user=false&show_reposts=false&sharing=false&download=false&buying=false" +
            "&visual=true'></iframe>" +
            "<script src=\"https://w.soundcloud.com/player/api.js\" type=\"text/javascript\"></script>" +
            "</body>";

    public ContentBlock7ViewHolder(View itemView) {
        super(itemView);
        mTitleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
        mSoundCloudWebview = (WebView) itemView.findViewById(R.id.soundcloudWebview);
        WebSettings webSettings = mSoundCloudWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    public void setupContentBlock(ContentBlockType7 cb7) {
        mTitleTextView.setVisibility(View.VISIBLE);

        if(cb7.getTitle() != null)
            mTitleTextView.setText(cb7.getTitle());
        else {
            mTitleTextView.setVisibility(View.GONE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mSoundCloudWebview.getLayoutParams();
            params.setMargins(0,0,0,0);
            mSoundCloudWebview.setLayoutParams(params);
        }

        String test = String.format(mSoundCloudHTML, cb7.getSoundcloudUrl());
        mSoundCloudWebview.loadData(test, "text/html", "utf-8");
    }
}

/**
 * DownloadBlock
 */
class ContentBlock8ViewHolder extends RecyclerView.ViewHolder {
    private Fragment mFragment;
    private TextView mTitleTextView;
    private TextView mContentTextView;
    private ImageView mIconImageView;
    private LinearLayout mRootLayout;

    public ContentBlock8ViewHolder(View itemView, Fragment activity) {
        super(itemView);
        mRootLayout = (LinearLayout) itemView.findViewById(R.id.downloadBlockLinearLayout);
        mTitleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
        mContentTextView = (TextView) itemView.findViewById(R.id.contentTextView);
        mIconImageView = (ImageView) itemView.findViewById(R.id.iconImageView);
        mFragment = activity;
    }

    public void setupContentBlock(final ContentBlockType8 cb8) {
        if(cb8.getTitle() != null)
            mTitleTextView.setText(cb8.getTitle());
        else
            mTitleTextView.setText(null);

        if(cb8.getText() != null)
            mContentTextView.setText(cb8.getText());
        else
            mContentTextView.setText(null);

        mRootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(cb8.getFileId()));
                mFragment.startActivity(i);
            }
        });

        switch (cb8.getDownloadType()) {
            case 0:
                mRootLayout.setBackgroundResource(R.color.vcf_downloadBlock_background_color);
                mIconImageView.setImageResource(R.drawable.ic_account_plus);
                mTitleTextView.setTextColor(Color.WHITE);
                mContentTextView.setTextColor(Color.WHITE);
                break;
            case 1:
                mRootLayout.setBackgroundResource(R.color.ical_downloadBlock_background_color);
                mIconImageView.setImageResource(R.drawable.ic_calendar);
                mTitleTextView.setTextColor(Color.WHITE);
                mContentTextView.setTextColor(Color.WHITE);
                break;
            default:
                mRootLayout.setBackgroundResource(R.color.default_linkblock_background_color);
                mIconImageView.setImageResource(R.drawable.ic_web);
                mTitleTextView.setTextColor(Color.parseColor("#333333"));
                mContentTextView.setTextColor(Color.parseColor("#333333"));
                break;
        }
    }
}

/**
 * SpotMapBlock
 */
class ContentBlock9ViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {
    private Fragment mFragment;
    private TextView mTitleTextView;
    private SupportMapFragment mMapFragment;
    private ContentBlockType9 mContentBlock;
    private ImageView mCustomMarkerImageView;

    public ContentBlock9ViewHolder(View itemView, Fragment fragment) {
        super(itemView);
        mFragment = fragment;
        mTitleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
        //mMapFragment = (SupportMapFragment) fragment.getActivity().getSupportFragmentManager().findFragmentById(R.id.map);
        mMapFragment = new SupportMapFragment().newInstance();
        mFragment.getFragmentManager().beginTransaction().replace(R.id.map, mMapFragment).commit();
    }

    public void setupContentBlock(ContentBlockType9 cb9) {
        mTitleTextView.setVisibility(View.VISIBLE);
        if (cb9.getTitle() != null)
            mTitleTextView.setText(cb9.getTitle());
        else
            mTitleTextView.setVisibility(View.GONE);

        mContentBlock = cb9;
        mMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        final ArrayList<Marker> mMarkerArray = new ArrayList<Marker>();
        XamoomEndUserApi.getInstance().getSpotMap(null, mContentBlock.getSpotMapTag().split(","), null, new APICallback<SpotMap>() {
            @Override
            public void finished(SpotMap result) {
                if (mMapFragment.isAdded()) {
                    Bitmap icon;
                    if (result.getStyle().getCustomMarker() != null) {
                        String iconString = result.getStyle().getCustomMarker();
                        icon = getIconFromBase64(iconString);
                    } else {
                        icon = BitmapFactory.decodeResource(mFragment.getResources(), R.drawable.ic_default_map_marker);
                        float imageRatio = (float) icon.getWidth() / (float) icon.getHeight();
                        icon = Bitmap.createScaledBitmap(icon, 70, (int) (70 / imageRatio), false);
                    }

                    //show all markers
                    for (Spot s : result.getItems()) {
                        Marker marker = googleMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.fromBitmap(icon))
                                .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                                .title(s.getDisplayName())
                                .position(new LatLng(s.getLocation().getLat(), s.getLocation().getLon())));

                        mMarkerArray.add(marker);
                    }

                    //zoom to display all markers
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for (Marker marker : mMarkerArray) {
                        builder.include(marker.getPosition());
                    }
                    LatLngBounds bounds = builder.build();

                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 70);
                    googleMap.moveCamera(cu);
                }
            }

            @Override
            public void error(RetrofitError error) {
                Log.e("xamoom-android-sdk", "Error:" + error);
            }
        });
    }

    /**
     * Decodes a base64 string to an icon for mapMarkers.
     * Can handle normal image formats and also svgs.
     * The icon will be resized to width: 70, height will be resized to maintain imageRatio.
     *
     * @param base64String Base64 string that will be resized. Must start with "data:image/"
     * @return icon as BitMap, or null if there was a problem
     */
    public Bitmap getIconFromBase64(String base64String) {
        Bitmap icon = null;
        byte[] data1;
        byte[] data2 = "".getBytes();
        String decodedString1 = "";
        String decodedString2 = "";

        if (base64String == null)
            return null;

        try {
            //encode 2 times
            data1 = Base64.decode(base64String, Base64.DEFAULT);
            decodedString1 = new String(data1, "UTF-8");

            //get rid of image/xxxx base64,
            int index = decodedString1.indexOf("base64,");
            String decodedString1WithoutPrefix = decodedString1.substring(index + 7);

            data2 = Base64.decode(decodedString1WithoutPrefix, Base64.DEFAULT);
            decodedString2 = new String(data2, "UTF-8");

            if (decodedString1.contains("data:image/svg+xml")) {
                //svg stuff
                SVG svg = null;
                svg = SVG.getFromString(decodedString2);

                if (svg != null) {
                    Log.v("pingeborg", "HELLYEAH SVG: " + svg);

                    //resize svg
                    float imageRatio = svg.getDocumentWidth() / svg.getDocumentHeight();
                    svg.setDocumentWidth(70.0f);
                    svg.setDocumentHeight(70 / imageRatio);

                    icon = Bitmap.createBitmap((int) svg.getDocumentWidth(), (int) svg.getDocumentHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas1 = new Canvas(icon);
                    svg.renderToCanvas(canvas1);
                }
            } else if (decodedString1.contains("data:image/")) {
                //normal image stuff
                icon = BitmapFactory.decodeByteArray(data2, 0, data2.length);
                //resize the icon
                double imageRatio = (double) icon.getWidth() / (double) icon.getHeight();
                double newHeight = 70.0 / imageRatio;
                icon = Bitmap.createScaledBitmap(icon, 70, (int) newHeight, false);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (SVGParseException e ) {
            e.printStackTrace();
        }

        return icon;
    }
}