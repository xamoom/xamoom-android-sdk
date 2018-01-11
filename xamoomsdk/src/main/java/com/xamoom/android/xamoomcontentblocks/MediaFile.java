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
  public enum State {
    IDLE, PLAYING, PAUSED
  }

  private int position;
  private AudioPlayer audioPlayer;
  private Uri uri;
  private EventListener eventListener;
  private State state = State.IDLE;
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

  public void seekForward(long seekTime) {
    audioPlayer.seekForward(seekTime, position);
  }

  public void seekBackward(long seekTime) {
    audioPlayer.seekBackward(seekTime, position);
  }

  void started() {
    state = State.PLAYING;
    startMonitoringPlaybackPosition();
    if (eventListener != null) {
      eventListener.started();
    }
  }

  void paused() {
    state = State.PAUSED;
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
    state = State.IDLE;
    stopMonitoringPlaybackPosition();
    if (eventListener != null) {
      eventListener.finished();
    }
  }

  void updateDuration(long duration) {
    this.duration = duration;
  }

  void updatePlaybackPosition(long playbackPosition) {
    if (playbackPosition < 0) {
      playbackPosition = 0;
    }

    if (playbackPosition > duration) {
      playbackPosition = duration;
    }

    this.playbackPosition = playbackPosition;

    if (eventListener != null) {
      eventListener.updatePlaybackPosition(playbackPosition);
    }
  }

  private void startMonitoringPlaybackPosition() {
    handler.postDelayed(new Runnable() {
      @Override
      public void run() {
        playbackPosition = audioPlayer.getPlaybackPosition(position);
        if (playbackPosition < 0) {
          playbackPosition = 0;
        }

        if (playbackPosition > duration) {
          playbackPosition = duration;
        }

        if (eventListener != null) {
          eventListener.updatePlaybackPosition(playbackPosition);
        }
        handler.postDelayed(this, 100);
      }
    }, 100);
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

  public int getPosition() {
    return position;
  }

  public long getDuration() {
    return duration;
  }

  public long getPlaybackPosition() {
    return playbackPosition;
  }

  public State getState() {
    return state;
  }
}
