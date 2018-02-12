package com.xamoom.android.xamoomcontentblocks;

import android.content.Context;
import android.net.Uri;
import android.support.v4.util.SparseArrayCompat;

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
  private static final String TAG = AudioPlayer.class.getSimpleName();

  private Context context;
  private ExoPlayer exoPlayer;
  private String appName;
  private HashMap<String, MediaFile> mediaFiles = new HashMap<>();
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

  public MediaFile createMediaFile(Uri uri, String id, String title, String artist, String album) {
    MediaFile mediaFile = mediaFiles.get(id);
    if (mediaFile == null) {
      mediaFile = new MediaFile(this, uri, id, title, artist, album);
      mediaFiles.put(mediaFile.getId(), mediaFile);
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

  public void start(String position) {
    if (currentMediaFile != null) {
      currentMediaFile.pause();
    }

    if (currentMediaFile != mediaFiles.get(position) || currentMediaFile == null) {
      currentMediaFile = mediaFiles.get(position);
      prepareStreaming(currentMediaFile.getUri());
    }

    exoPlayer.seekTo(currentMediaFile.getPlaybackPosition());
    exoPlayer.setPlayWhenReady(true);
  }

  public void pause(String position) {
    if (currentMediaFile == null || currentMediaFile != mediaFiles.get(position)) {
      return;
    }

    exoPlayer.setPlayWhenReady(false);
  }

  public void stop() {
    exoPlayer.stop();
  }

  public void seekForward(long seekTime, String id) {
    long currentPosition = exoPlayer.getCurrentPosition();
    exoPlayer.seekTo(currentPosition + seekTime);
  }

  public void seekBackward(long seekTime, String id) {
    long currentPosition = exoPlayer.getCurrentPosition();
    exoPlayer.seekTo(currentPosition - seekTime);
  }

  @Override
  public void onTimelineChanged(Timeline timeline, Object manifest) {
  }

  @Override
  public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
  }

  @Override
  public void onLoadingChanged(boolean isLoading) {
    if (currentMediaFile != null) {
      currentMediaFile.loading(isLoading);
    }
  }

  @Override
  public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    switch (playbackState) {
      case Player.STATE_BUFFERING:
        break;
      case Player.STATE_READY:
        currentMediaFile.updateDuration(exoPlayer.getDuration());

        if (playWhenReady) {
          currentMediaFile.started();
        } else {
          currentMediaFile.paused();
        }
        break;
      case Player.STATE_IDLE:
        if (currentMediaFile.getPlaybackPosition() > 0) {
          currentMediaFile.updatePlaybackPosition(0);
          currentMediaFile.finished();
        }
        break;
      case Player.STATE_ENDED:
        currentMediaFile.updatePlaybackPosition(0);
        currentMediaFile.finished();

        currentMediaFile = null;
        break;
    }
  }

  @Override
  public void onRepeatModeChanged(int repeatMode) {

  }

  @Override
  public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
  }

  @Override
  public void onPlayerError(ExoPlaybackException error) {
  }

  @Override
  public void onPositionDiscontinuity(int reason) {
  }

  @Override
  public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
  }

  @Override
  public void onSeekProcessed() {
    long position = exoPlayer.getCurrentPosition();
    if (currentMediaFile != null) {
      currentMediaFile.updatePlaybackPosition(position);
    }
  }

  public long getPlaybackPosition(String id) {
    if (currentMediaFile == null || currentMediaFile != mediaFiles.get(id)) {
      return -1;
    }
    return exoPlayer.getCurrentPosition();
  }

  public MediaFile getCurrentMediaFile() {
    return currentMediaFile;
  }
}
