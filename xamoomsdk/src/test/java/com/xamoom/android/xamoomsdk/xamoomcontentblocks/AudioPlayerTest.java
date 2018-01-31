package com.xamoom.android.xamoomsdk.xamoomcontentblocks;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.MediaSource;
import com.xamoom.android.xamoomcontentblocks.AudioPlayer;
import com.xamoom.android.xamoomcontentblocks.MediaFile;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
    when(mockContext.getApplicationInfo()).thenReturn(mockApplicationInfo);
    when(mockApplicationInfo.loadLabel(Mockito.any(PackageManager.class)))
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

    MediaFile mediaFile = audioPlayer.createMediaFile(uri, "0", "title", "artist", "album");

    assertEquals(uri, mediaFile.getUri());
    assertEquals(audioPlayer, mediaFile.getAudioPlayer());
    assertEquals("0", mediaFile.getId());
    assertEquals("title", mediaFile.getTitle());
    assertEquals("artist", mediaFile.getArtist());
    assertEquals("album", mediaFile.getAlbum());
  }

  @Test
  public void testCreateExistingMediaFile() {
    AudioPlayer audioPlayer = new AudioPlayer(mockContext, mockExoPlayer);
    Uri uri = Uri.parse("www.xamoom.com");

    MediaFile mediaFile1 = audioPlayer.createMediaFile(uri, "0", "title", "artist", "album");
    MediaFile mediaFile2 = audioPlayer.createMediaFile(uri, "1", "title", "artist", "album");

    assertNotSame(mediaFile1, mediaFile2);
  }

  @Test
  public void testStart() {
    AudioPlayer audioPlayer = new AudioPlayer(RuntimeEnvironment.application, mockExoPlayer);

    Uri uri = Uri.parse("www.xamoom.com");
    MediaFile mediaFile1 = audioPlayer.createMediaFile(uri, "0", "title", "artist", "album");

    audioPlayer.start("0");

    Mockito.verify(mockExoPlayer).prepare(Mockito.any(MediaSource.class));
    Mockito.verify(mockExoPlayer).setPlayWhenReady(eq(true));
    Mockito.verify(mockExoPlayer).seekTo(eq(0L));
  }

  @Test
  public void testStartWithDifferentPositions() {
    AudioPlayer audioPlayer = new AudioPlayer(RuntimeEnvironment.application, mockExoPlayer);

    Uri uri = Uri.parse("www.xamoom.com");
    MediaFile mediaFile1 = audioPlayer.createMediaFile(uri, "0", "title", "artist", "album");
    mediaFile1.setEventListener(mockEventListener);
    MediaFile mediaFile2 = audioPlayer.createMediaFile(uri, "1", "title", "artist", "album");

    audioPlayer.start("0");
    audioPlayer.onPlayerStateChanged(false, 3);
    audioPlayer.start("1");

    Mockito.verify(mockEventListener).paused();
    Mockito.verify(mockExoPlayer).setPlayWhenReady(false);
    Mockito.verify(mockExoPlayer, Mockito.times(2))
        .prepare(Mockito.any(MediaSource.class));
    Mockito.verify(mockExoPlayer, Mockito.times(2))
        .setPlayWhenReady(eq(true));
  }

  @Test
  public void testPause() {
    AudioPlayer audioPlayer = new AudioPlayer(RuntimeEnvironment.application, mockExoPlayer);
    Uri uri = Uri.parse("www.xamoom.com");
    MediaFile mediaFile1 = audioPlayer.createMediaFile(uri, "0", "title", "artist", "album");
    mediaFile1.setEventListener(mockEventListener);

    audioPlayer.start("0");
    audioPlayer.onPlayerStateChanged(true, 3);

    audioPlayer.pause("0");
    audioPlayer.onPlayerStateChanged(false, 3);

    Mockito.verify(mockExoPlayer).setPlayWhenReady(eq(false));
    Mockito.verify(mockExoPlayer, times(2)).getDuration();
    Mockito.verify(mockEventListener).started();
    Mockito.verify(mockEventListener).paused();
  }

  @Test
  public void testStop() {
    AudioPlayer audioPlayer = new AudioPlayer(RuntimeEnvironment.application, mockExoPlayer);
    Uri uri = Uri.parse("www.xamoom.com");
    MediaFile mediaFile1 = audioPlayer.createMediaFile(uri, "0", "title", "artist", "album");
    mediaFile1.setEventListener(mockEventListener);

    audioPlayer.start("0");
    audioPlayer.onPlayerStateChanged(true, 3);

    audioPlayer.stop();
    audioPlayer.onPlayerStateChanged(false, Player.STATE_ENDED);

    Mockito.verify(mockEventListener).started();
    Mockito.verify(mockEventListener).finished();
    Mockito.verify(mockExoPlayer).stop();
  }


  @Test
  public void testEndOfPlayback() {
    AudioPlayer audioPlayer = new AudioPlayer(RuntimeEnvironment.application, mockExoPlayer);
    Uri uri = Uri.parse("www.xamoom.com");
    MediaFile mediaFile1 = audioPlayer.createMediaFile(uri, "0", "title", "artist", "album");
    mediaFile1.setEventListener(mockEventListener);
    audioPlayer.start("0");

    audioPlayer.onPlayerStateChanged(true, 4);

    Mockito.verify(mockEventListener).finished();
  }

  @Test
  public void testLoading() {
    AudioPlayer audioPlayer = new AudioPlayer(RuntimeEnvironment.application, mockExoPlayer);
    Uri uri = Uri.parse("www.xamoom.com");
    MediaFile mediaFile1 = audioPlayer.createMediaFile(uri, "0", "title", "artist", "album");
    mediaFile1.setEventListener(mockEventListener);
    audioPlayer.start("0");

    audioPlayer.onLoadingChanged(true);

    Mockito.verify(mockEventListener).loadingChanged(eq(true));
  }

  @Test
  public void testPlaybackPosition() {
    AudioPlayer audioPlayer = new AudioPlayer(RuntimeEnvironment.application, mockExoPlayer);
    Uri uri = Uri.parse("www.xamoom.com");
    MediaFile mediaFile1 = audioPlayer.createMediaFile(uri, "0", "title", "artist", "album");
    mediaFile1.setEventListener(mockEventListener);
    audioPlayer.start("0");

    when(mockExoPlayer.getCurrentPosition()).thenReturn(101L);

    Assert.assertEquals(101L, audioPlayer.getPlaybackPosition("0"));
  }

  @Test
  public void testSeekForward() {
    AudioPlayer audioPlayer = new AudioPlayer(RuntimeEnvironment.application, mockExoPlayer);
    Uri uri = Uri.parse("www.xamoom.com");
    MediaFile mediaFile1 = audioPlayer.createMediaFile(uri, "0", "title", "artist", "album");
    mediaFile1.setEventListener(mockEventListener);
    when(mockExoPlayer.getCurrentPosition()).thenReturn(0L);

    audioPlayer.seekForward(50000, "0");

    verify(mockExoPlayer).seekTo(eq(50000L));
  }

  @Test
  public void testSeekBackward() {
    AudioPlayer audioPlayer = new AudioPlayer(RuntimeEnvironment.application, mockExoPlayer);
    Uri uri = Uri.parse("www.xamoom.com");
    MediaFile mediaFile1 = audioPlayer.createMediaFile(uri, "0", "title", "artist", "album");
    mediaFile1.setEventListener(mockEventListener);
    when(mockExoPlayer.getCurrentPosition()).thenReturn(0L);

    audioPlayer.seekBackward(50000L, "0");

    verify(mockExoPlayer).seekTo(eq(-50000L));
  }

  @Test
  public void testUpdatePlaybackPosition() {
    AudioPlayer audioPlayer = new AudioPlayer(RuntimeEnvironment.application, mockExoPlayer);
    Uri uri = Uri.parse("www.xamoom.com");
    MediaFile mediaFile1 = audioPlayer.createMediaFile(uri, "0", "title", "artist", "album");
    mediaFile1.setEventListener(mockEventListener);

    when(mockExoPlayer.getCurrentPosition()).thenReturn(100L);
    when(mockExoPlayer.getDuration()).thenReturn(10000L);
    audioPlayer.start("0");
    audioPlayer.onPlayerStateChanged(true, 3);
    audioPlayer.onSeekProcessed();

    verify(mockEventListener).updatePlaybackPosition(eq(100L));
  }

  @Test
  public void testUpdatePlaybackPositionOverDurationCheck() {
    AudioPlayer audioPlayer = new AudioPlayer(RuntimeEnvironment.application, mockExoPlayer);
    Uri uri = Uri.parse("www.xamoom.com");
    MediaFile mediaFile1 = audioPlayer.createMediaFile(uri, "0", "title", "artist", "album");
    mediaFile1.setEventListener(mockEventListener);

    when(mockExoPlayer.getCurrentPosition()).thenReturn(10001L);
    when(mockExoPlayer.getDuration()).thenReturn(10000L);
    audioPlayer.start("0");
    audioPlayer.onPlayerStateChanged(true, 3);
    audioPlayer.onSeekProcessed();

    verify(mockEventListener).updatePlaybackPosition(eq(10000L));
  }

  @Test
  public void testUpdatePlaybackPositionLower0Check() {
    AudioPlayer audioPlayer = new AudioPlayer(RuntimeEnvironment.application, mockExoPlayer);
    Uri uri = Uri.parse("www.xamoom.com");
    MediaFile mediaFile1 = audioPlayer.createMediaFile(uri, "0", "title", "artist", "album");
    mediaFile1.setEventListener(mockEventListener);

    when(mockExoPlayer.getCurrentPosition()).thenReturn(-100L);
    when(mockExoPlayer.getDuration()).thenReturn(10000L);
    audioPlayer.start("0");
    audioPlayer.onPlayerStateChanged(true, 3);
    audioPlayer.onSeekProcessed();

    verify(mockEventListener).updatePlaybackPosition(eq(0L));
  }
}
