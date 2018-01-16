package com.xamoom.android.xamoomsdk.xamoomcontentblocks;

import android.media.MediaPlayer;
import android.net.Uri;

import com.xamoom.android.xamoomcontentblocks.AudioPlayer;
import com.xamoom.android.xamoomcontentblocks.MediaFile;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

/**
 * Created by raphaelseher on 10.01.18.
 */

@RunWith(JUnit4.class)
public class MediaFileTest {

  private AudioPlayer mockAudioPlayer;
  private MediaFile.EventListener mockEventListener;

  @Before
  public void setup() {
    mockAudioPlayer = Mockito.mock(AudioPlayer.class);
    mockEventListener = Mockito.mock(MediaFile.EventListener.class);
  }

  @Test
  public void testConstructor() {
    Uri uri = Uri.parse("www.xamoom.com");

    MediaFile mediaFile = new MediaFile(mockAudioPlayer, uri, 0, "title",
        "artist", "album");

    Assert.assertEquals(mockAudioPlayer, mediaFile.getAudioPlayer());
    Assert.assertEquals(uri, mediaFile.getUri());
    Assert.assertEquals(0, mediaFile.getPosition());
    Assert.assertEquals("title", mediaFile.getTitle());
    Assert.assertEquals("artist", mediaFile.getArtist());
    Assert.assertEquals("album", mediaFile.getAlbum());
  }

  @Test
  public void testStart() {
    Uri uri = Uri.parse("www.xamoom.com");

    MediaFile mediaFile = new MediaFile(mockAudioPlayer, uri, 0, null, null, null);
    mediaFile.start();

    Mockito.verify(mockAudioPlayer).start(Mockito.eq(0));
  }

  @Test
  public void testPause() {
    Uri uri = Uri.parse("www.xamoom.com");

    MediaFile mediaFile = new MediaFile(mockAudioPlayer, uri, 0, null, null, null);
    mediaFile.pause();

    Mockito.verify(mockAudioPlayer).pause(Mockito.eq(0));
  }
}
