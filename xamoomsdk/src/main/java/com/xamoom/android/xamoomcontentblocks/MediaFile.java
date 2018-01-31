package com.xamoom.android.xamoomcontentblocks;

import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by raphaelseher on 10.01.18.
 */

public class MediaFile {
  public enum State {
    IDLE, PLAYING, PAUSED
  }

  private State state = State.IDLE;
  private AudioPlayer audioPlayer;
  private Uri uri;
  private int position;
  private String title;
  private String artist;
  private String album;
  private long duration;
  private long playbackPosition = (long) 0.0;
  private EventListener eventListener;
  private final Handler handler = new Handler();


  public MediaFile(@NonNull AudioPlayer audioPlayer, @NonNull Uri uri, @NonNull int position,
                   @Nullable String title, @Nullable String artist, @Nullable String album) {
    this.audioPlayer = audioPlayer;
    this.uri = uri;
    this.position = position;
    this.title = title;
    this.artist = artist;
    this.album = album;
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

  public String getTitle() {
    return title;
  }

  public String getArtist() {
    return artist;
  }

  public String getAlbum() {
    return album;
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
