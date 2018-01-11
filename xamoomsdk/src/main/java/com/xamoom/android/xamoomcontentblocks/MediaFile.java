package com.xamoom.android.xamoomcontentblocks;

import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by raphaelseher on 10.01.18.
 */

public class MediaFile {
  private int position;
  private AudioPlayer audioPlayer;
  private Uri uri;
  private EventListener eventListener;
  private long duration;
  private long playbackPosition;
  private final Handler handler = new Handler();


  public MediaFile(@NonNull AudioPlayer audioPlayer, @NonNull Uri uri, @NonNull int position) {
    this.audioPlayer = audioPlayer;
    this.uri = uri;
    this.position = position;
  }

  public AudioPlayer getAudioPlayer() {
    return audioPlayer;
  }

  public Uri getUri() {
    return uri;
  }

  public void start() {
    audioPlayer.start(position);
  }

  public void pause() {
    audioPlayer.pause(position);

  }

  void started() {
    startMonitoringPlaybackPosition();
    if (eventListener != null) {
      eventListener.started();
    }
  }

  void paused() {
    stopMonitoringPlaybackPosition();
    if (eventListener != null) {
      eventListener.paused();
    }
  }

  void loading(boolean isLoading) {
    if (eventListener != null) {
      eventListener.loadingChanged(isLoading);
    }
  }

  void finished() {
    stopMonitoringPlaybackPosition();
    if (eventListener != null) {
      eventListener.finished();
    }
  }

  void updateDuration(long duration) {
    this.duration = duration;
  }

  private void startMonitoringPlaybackPosition() {
    handler.postDelayed(new Runnable() {
      @Override
      public void run() {
        playbackPosition = audioPlayer.getPlaybackPosition(position);
        if (eventListener != null) {
          eventListener.updatePlaybackPosition(playbackPosition);
        }
        handler.postDelayed(this, 300);
      }
    }, 300);
  }

  private void stopMonitoringPlaybackPosition() {
    handler.removeCallbacksAndMessages(null);
  }

  public interface EventListener {
    void loadingChanged(boolean isLoading);
    void started();
    void paused();
    void finished();
    void updatePlaybackPosition(long position);
  }

  public void setEventListener(EventListener eventListener) {
    this.eventListener = eventListener;
  }

  public AudioPlayer.State getState() {
    return audioPlayer.getState();
  }

  public int getPosition() {
    return position;
  }

  public long getDuration() {
    return duration;
  }

  public long getPlaybackPosition() {
    return playbackPosition;
  }
}
