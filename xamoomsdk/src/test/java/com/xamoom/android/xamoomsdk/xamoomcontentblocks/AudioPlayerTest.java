package com.xamoom.android.xamoomsdk.xamoomcontentblocks;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.xamoom.android.xamoomcontentblocks.AudioPlayer;
import com.xamoom.android.xamoomcontentblocks.MediaFile;

import junit.framework.Assert;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Any;
import org.mockito.runners.MockitoJUnitRunner;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.internal.Shadow;
import org.robolectric.shadows.ShadowMediaPlayer;
import org.robolectric.shadows.util.DataSource;

import java.io.IOException;
import java.util.concurrent.Semaphore;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;
import static org.hamcrest.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.robolectric.Shadows.shadowOf;
import static org.robolectric.shadows.util.DataSource.toDataSource;

@RunWith(RobolectricTestRunner.class)
public class AudioPlayerTest {

  private Context mockContext;
  private ExoPlayer mockExoPlayer;
  private MediaFile.EventListener mockEventListener;

  @Before
  public void setup() {
    mockContext = Mockito.mock(Context.class);
    mockExoPlayer = Mockito.mock(ExoPlayer.class);
    mockEventListener = Mockito.mock(MediaFile.EventListener.class);

    ApplicationInfo mockApplicationInfo = Mockito.mock(ApplicationInfo.class);
    Mockito.when(mockContext.getApplicationInfo()).thenReturn(mockApplicationInfo);
    Mockito.when(mockApplicationInfo.loadLabel(Mockito.any(PackageManager.class)))
        .thenReturn("AppName");
  }

  @Test
  public void testSharedInstance() {
    AudioPlayer audioPlayer = new AudioPlayer(mockContext, mockExoPlayer);

    Assert.assertTrue(audioPlayer != null);
  }

  @Test
  public void testCreateMediaFile() {
    AudioPlayer audioPlayer = new AudioPlayer(mockContext, mockExoPlayer);
    Uri uri = Uri.parse("www.xamoom.com");

    MediaFile mediaFile = audioPlayer.createMediaFile(uri, 0);

    assertEquals(uri, mediaFile.getUri());
    assertEquals(audioPlayer, mediaFile.getAudioPlayer());
    assertEquals(0, mediaFile.getPosition());
  }

  @Test
  public void testCreateExistingMediaFile() {
    AudioPlayer audioPlayer = new AudioPlayer(mockContext, mockExoPlayer);
    Uri uri = Uri.parse("www.xamoom.com");

    MediaFile mediaFile1 = audioPlayer.createMediaFile(uri, 0);
    MediaFile mediaFile2 = audioPlayer.createMediaFile(uri, 1);

    assertNotSame(mediaFile1, mediaFile2);
  }

  @Test
  public void testStart() {
    AudioPlayer audioPlayer = new AudioPlayer(RuntimeEnvironment.application, mockExoPlayer);

    Uri uri = Uri.parse("www.xamoom.com");
    MediaFile mediaFile1 = audioPlayer.createMediaFile(uri, 0);

    audioPlayer.start(0);

    Mockito.verify(mockExoPlayer).prepare(Mockito.any(MediaSource.class));
    Mockito.verify(mockExoPlayer).setPlayWhenReady(eq(true));
  }

  @Test
  public void testStartWithDifferentPositions() {
    AudioPlayer audioPlayer = new AudioPlayer(RuntimeEnvironment.application, mockExoPlayer);

    Uri uri = Uri.parse("www.xamoom.com");
    MediaFile mediaFile1 = audioPlayer.createMediaFile(uri, 0);
    MediaFile mediaFile2 = audioPlayer.createMediaFile(uri, 1);

    audioPlayer.start(0);
    audioPlayer.start(1);

    Mockito.verify(mockExoPlayer, Mockito.times(2))
        .prepare(Mockito.any(MediaSource.class));
    Mockito.verify(mockExoPlayer, Mockito.times(2))
        .setPlayWhenReady(eq(true));
  }

  @Test
  public void testPause() {
    AudioPlayer audioPlayer = new AudioPlayer(RuntimeEnvironment.application, mockExoPlayer);
    Uri uri = Uri.parse("www.xamoom.com");
    MediaFile mediaFile1 = audioPlayer.createMediaFile(uri, 0);
    mediaFile1.setEventListener(mockEventListener);

    audioPlayer.start(0);
    audioPlayer.onPlayerStateChanged(true, 3);

    audioPlayer.pause(0);
    audioPlayer.onPlayerStateChanged(false, 3);

    Mockito.verify(mockExoPlayer).setPlayWhenReady(eq(false));
    Mockito.verify(mockEventListener).started();
    Mockito.verify(mockEventListener).paused();
  }

  @Test
  public void testEndOfPlayback() {
    AudioPlayer audioPlayer = new AudioPlayer(RuntimeEnvironment.application, mockExoPlayer);
    Uri uri = Uri.parse("www.xamoom.com");
    MediaFile mediaFile1 = audioPlayer.createMediaFile(uri, 0);
    mediaFile1.setEventListener(mockEventListener);
    audioPlayer.start(0);

    audioPlayer.onPlayerStateChanged(true, 4);

    Mockito.verify(mockExoPlayer).stop();
    Mockito.verify(mockEventListener).finished();
  }

  @Test
  public void testLoading() {
    AudioPlayer audioPlayer = new AudioPlayer(RuntimeEnvironment.application, mockExoPlayer);
    Uri uri = Uri.parse("www.xamoom.com");
    MediaFile mediaFile1 = audioPlayer.createMediaFile(uri, 0);
    mediaFile1.setEventListener(mockEventListener);
    audioPlayer.start(0);

    audioPlayer.onLoadingChanged(true);

    Mockito.verify(mockEventListener).loading();
  }
}
