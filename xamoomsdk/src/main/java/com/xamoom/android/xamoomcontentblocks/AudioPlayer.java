package com.xamoom.android.xamoomcontentblocks;

import android.content.Context;
import android.net.Uri;
import android.support.v4.util.SparseArrayCompat;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.HashMap;

public class AudioPlayer implements Player.EventListener {

  public enum State {
    NOT_READY, IDLE, LOADING, PLAYING, PAUSED
  }

  private static final String TAG = AudioPlayer.class.getSimpleName();

  private static AudioPlayer sharedInstance;

  private Context context;
  private ExoPlayer exoPlayer;
  private String appName;
  private State state = State.NOT_READY;
  private SparseArrayCompat<MediaFile> mediaFiles = new SparseArrayCompat<>();
  private MediaFile currentMediaFile;

  public AudioPlayer(Context context) {
    this.context = context;
    appName = context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();

    BandwidthMeter defaultBandwithMeter = new DefaultBandwidthMeter();
    TrackSelection.Factory videoTrackSelectionFactory =
        new AdaptiveTrackSelection.Factory(defaultBandwithMeter);
    TrackSelector trackSelector =
        new DefaultTrackSelector(videoTrackSelectionFactory);

    exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
    exoPlayer.addListener(this);
  }

  public AudioPlayer(Context context, ExoPlayer exoPlayer) {
    this.context = context;
    appName = context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();

    this.exoPlayer = exoPlayer;
    exoPlayer.addListener(this);
  }

  public MediaFile createMediaFile(Uri uri, int position) {
    MediaFile mediaFile = mediaFiles.get(position);
    if (mediaFile == null) {
      mediaFile = new MediaFile(this, uri, position);
      mediaFiles.put(mediaFile.getPosition(), mediaFile);
    }
    return mediaFile;
  }

  private void prepareStreaming(Uri uri) {
    DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
        Util.getUserAgent(context, appName));
    MediaSource source = new ExtractorMediaSource.Factory(dataSourceFactory)
        .createMediaSource(uri);
    exoPlayer.prepare(source);
  }

  public void start(int position) {
    Log.v(TAG, "start");

    if (currentMediaFile == null || currentMediaFile != mediaFiles.get(position)) {
      Log.v(TAG, "PREPARING");
      currentMediaFile = mediaFiles.get(position);
      prepareStreaming(currentMediaFile.getUri());
    }

    exoPlayer.setPlayWhenReady(true);
  }

  public void pause(int position) {
    Log.v(TAG, "paused");

    if (currentMediaFile == null || currentMediaFile != mediaFiles.get(position)) {
      return;
    }

    if (state == State.PLAYING) {
      exoPlayer.setPlayWhenReady(false);
      state = State.PAUSED;
    }
  }

  @Override
  public void onTimelineChanged(Timeline timeline, Object manifest) {
    Log.v(TAG, "onTimelineChanged " + timeline);
  }

  @Override
  public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
    Log.v(TAG, "onTracksChanged");
  }

  @Override
  public void onLoadingChanged(boolean isLoading) {
    Log.v(TAG, "onLoadingChanged " + String.valueOf(isLoading));
    if (isLoading) {
      currentMediaFile.loading();
    }
  }

  @Override
  public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
    Log.v(TAG, "onPlayerStateChanged " + String.valueOf(playbackState));

    switch (playbackState) {
      case Player.STATE_BUFFERING:
        Log.v(TAG, "State: BUFFERING");
        break;
      case Player.STATE_READY:
        Log.v(TAG, "State: READY");
        if (playWhenReady) {
          currentMediaFile.started();
          state = State.PLAYING;
        } else {
          currentMediaFile.paused();
          state = State.PAUSED;
        }
        break;
      case Player.STATE_IDLE:
        Log.v(TAG, "State: IDLE");
        break;
      case Player.STATE_ENDED:
        Log.v(TAG, "State: ENDED");
        exoPlayer.stop();
        state = State.NOT_READY;
        currentMediaFile.finished();
        mediaFiles.remove(currentMediaFile.getPosition());
        currentMediaFile = null;
        break;
    }
  }

  @Override
  public void onRepeatModeChanged(int repeatMode) {
    Log.v(TAG, "onRepeatModeChanged");
  }

  @Override
  public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
    Log.v(TAG, "onShuffleModeEnabledChanged");

  }

  @Override
  public void onPlayerError(ExoPlaybackException error) {
    Log.v(TAG, "onPlayerError" + error);

  }

  @Override
  public void onPositionDiscontinuity(int reason) {
    Log.v(TAG, "onPositionDiscontinuity");
  }

  @Override
  public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
    Log.v(TAG, "onPlaybackParametersChanged " + playbackParameters);
  }

  @Override
  public void onSeekProcessed() {
    Log.v(TAG, "onSeekProcessed");
  }

  public State getState() {
    return state;
  }
}
