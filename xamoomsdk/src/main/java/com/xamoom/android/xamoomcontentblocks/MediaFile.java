package com.xamoom.android.xamoomcontentblocks;

import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * Created by raphaelseher on 10.01.18.
 */

public class MediaFile {
  private int position;
  private AudioPlayer audioPlayer;
  private Uri uri;
  private EventListener eventListener;

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
    if (eventListener != null) {
      eventListener.started();
    }
  }

  public void paused() {
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
    if (eventListener != null) {
      eventListener.finished();
    }
  }

  public interface EventListener {
    void loadingChanged(boolean isLoading);
    void started();
    void paused();
    void finished();
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
}
