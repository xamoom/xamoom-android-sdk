package com.xamoom.android.xamoomcontentblocks;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.xamoom.android.xamoomsdk.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by raphaelseher on 12.01.18.
 */

public class AudioPlayerService extends Service {
  private static final String TAG = AudioPlayerService.class.getSimpleName();
  private static final int NOTIFICATION_ID = 1;

  public static final int MSG_REGISTER_CLIENT = 1;
  public static final int MSG_UNREGISTER_CLIENT = 2;
  public static final int MSG_SET_URL = 3;
  public static final int MSG_ACTION_PLAY = 4;
  public static final int MSG_ACTION_PAUSE = 5;
  public static final int MSG_AUDIO_EVENT_STARTED = 6;
  public static final int MSG_AUDIO_EVENT_PAUSED = 7;
  public static final int MSG_AUDIO_EVENT_FINISHED = 8;
  public static final int MSG_AUDIO_EVENT_UPDATE_PROGRESS = 9;
  public static final int MSG_AUDIO_EVENT_UPDATE_LOADING = 10;
  public static final String AUDIO_URL = "AudioPlayerService.audio_url";

  private ArrayList<Messenger> clients = new ArrayList<Messenger>();
  private HashMap<MediaFile, Messenger> mediaFileMessengers = new HashMap<>();
  private AudioPlayer audioPlayer;
  private MediaSessionCompat mediaSession;
  final Messenger messenger = new Messenger(new IncomingHandler());

  class IncomingHandler extends Handler {
    @Override
    public void handleMessage(final Message msg) {
      int position = 0;
      switch (msg.what) {
        case MSG_REGISTER_CLIENT:
          clients.add(msg.replyTo);
          break;
        case MSG_UNREGISTER_CLIENT:
          clients.remove(msg.replyTo);
          break;
        case MSG_SET_URL:
          position = msg.getData().getInt("POS");
          String url = msg.getData().getString("URL");
          String title = msg.getData().getString("TITLE");
          String artist = msg.getData().getString("ARTIST");

          final MediaFile mediaFile = audioPlayer.createMediaFile(Uri.parse(url), position, title,
              artist, null);
          mediaFileMessengers.put(mediaFile, msg.replyTo);

          mediaFile.setEventListener(new MediaFile.EventListener() {
            @Override
            public void loadingChanged(boolean isLoading) {
              Log.v(TAG, "loadingChanged: " + isLoading);

              Messenger messenger = mediaFileMessengers.get(mediaFile);
              if (messenger == null) {
                Log.e(TAG, "Messenger for mediaFile not found.");
                return;
              }
              try {
                messenger.send(Message.obtain(null, MSG_AUDIO_EVENT_UPDATE_LOADING,
                    isLoading ? 0 : 1,0));
              } catch (RemoteException e) {
                e.printStackTrace();
              }
            }

            @Override
            public void started() {
              Log.v(TAG, "started: " + mediaFile);

              mediaSession.setMetadata(new MediaMetadataCompat.Builder()
                  .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, mediaFile.getUri().toString())
                  .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, mediaFile.getAlbum())
                  .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, mediaFile.getArtist())
                  .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, mediaFile.getDuration())
                  .putString(MediaMetadataCompat.METADATA_KEY_TITLE, mediaFile.getTitle())
                  .build());

              Log.d(TAG, "Duration: " + mediaFile.getDuration());

              mediaSession.setActive(true);

              mediaSession.setPlaybackState(new PlaybackStateCompat.Builder()
                  .setState(PlaybackStateCompat.STATE_PLAYING,
                      PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN, 1.0f)
                  .setActions(PlaybackStateCompat.ACTION_PAUSE |
                          PlaybackStateCompat.ACTION_PLAY_PAUSE |
                      PlaybackStateCompat.ACTION_FAST_FORWARD |
                      PlaybackStateCompat.ACTION_REWIND)
                  .build());

              showMediaNotification(true);

              Messenger messenger = mediaFileMessengers.get(mediaFile);
              if (messenger == null) {
                Log.e(TAG, "Messenger for mediaFile not found.");
                return;
              }
              try {
                messenger.send(Message.obtain(null, MSG_AUDIO_EVENT_STARTED,
                    0,0));
              } catch (RemoteException e) {
                e.printStackTrace();
              }
            }

            @Override
            public void paused() {
              Log.v(TAG, "paused: " + mediaFile);

              mediaSession.setPlaybackState(new PlaybackStateCompat.Builder()
                  .setState(PlaybackStateCompat.STATE_PAUSED,
                      PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN, 0)
                  .setActions(PlaybackStateCompat.ACTION_PLAY |
                      PlaybackStateCompat.ACTION_PLAY_PAUSE)
                  .build());

              showMediaNotification(false);

              Messenger messenger = mediaFileMessengers.get(mediaFile);
              if (messenger == null) {
                Log.e(TAG, "Messenger for mediaFile not found.");
                return;
              }
              try {
                Message msg = Message.obtain(null, MSG_AUDIO_EVENT_PAUSED,
                    0,0);
                messenger.send(msg);
              } catch (RemoteException e) {
                e.printStackTrace();
              }
            }

            @Override
            public void finished() {
              Log.v(TAG, "finished: " + mediaFile);

              cancelMediaNotification();

              mediaSession.setActive(false);

              Messenger messenger = mediaFileMessengers.get(mediaFile);
              if (messenger == null) {
                Log.e(TAG, "Messenger for mediaFile not found.");
                return;
              }
              try {
                messenger.send(Message.obtain(null, MSG_AUDIO_EVENT_FINISHED,
                    0,0));
              } catch (RemoteException e) {
                e.printStackTrace();
              }
            }

            @Override
            public void updatePlaybackPosition(long position) {
              //Log.v(TAG, "updatePlaybackPosition: " + mediaFile);

              /*
              mediaSession.setPlaybackState(new PlaybackStateCompat.Builder()
                  .setState(PlaybackStateCompat.STATE_PLAYING, position, 0)
                   .setActions(PlaybackStateCompat.ACTION_PLAY |
                          PlaybackStateCompat.ACTION_PLAY_PAUSE |
                      PlaybackStateCompat.ACTION_FAST_FORWARD |
                      PlaybackStateCompat.ACTION_REWIND)
                  .build());
              */
              Messenger messenger = mediaFileMessengers.get(mediaFile);
              if (messenger == null) {
                Log.e(TAG, "Messenger for mediaFile not found.");
                return;
              }
              try {
                Message msg = Message.obtain(null, MSG_AUDIO_EVENT_UPDATE_PROGRESS,
                    0,0);
                Bundle bundle = new Bundle();
                bundle.putLong("DURATION", mediaFile.getDuration());
                bundle.putLong("POSITION", position);
                msg.setData(bundle);
                messenger.send(msg);
              } catch (RemoteException e) {
                e.printStackTrace();
              }
            }
          });
          mediaFile.start();
          break;
        case MSG_ACTION_PAUSE:
          position = msg.getData().getInt("POS");
          audioPlayer.pause(position);
          break;
        default:
          super.handleMessage(msg);
      }
    }
  }


  private BroadcastReceiver mNoisyReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      Log.v(TAG, "became noisy");
    }
  };

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return messenger.getBinder();
  }

  private MediaSessionCompat.Callback mediaSessionCallback = new MediaSessionCompat.Callback() {
    @Override
    public boolean onMediaButtonEvent(Intent mediaButtonEvent) {
      Log.e(TAG, "mediaSession onMediaButtonEvent " + mediaButtonEvent);
      return super.onMediaButtonEvent(mediaButtonEvent);
    }

    @Override
    public void onPlay() {
      audioPlayer.getCurrentMediaFile().start();
      Log.e(TAG, "mediaSession onPlay");
      super.onPlay();
    }

    @Override
    public void onPause() {
      audioPlayer.getCurrentMediaFile().pause();
      Log.e(TAG, "mediaSession onPause");
      super.onPause();
    }

    @Override
    public void onFastForward() {
      Log.e(TAG, "mediaSession onFastForward");
      super.onFastForward();
    }

    @Override
    public void onRewind() {
      Log.e(TAG, "mediaSession onRewind");
      super.onRewind();
    }

    @Override
    public void onStop() {
      audioPlayer.getCurrentMediaFile().pause();
      Log.e(TAG, "mediaSession onStop");
      super.onStop();
    }
  };

  @Override
  public void onCreate() {
    super.onCreate();
    audioPlayer = new AudioPlayer(getApplicationContext());
    ComponentName componentName = new ComponentName(getApplicationContext(),
        MediaButtonReceiver.class);
    mediaSession = new MediaSessionCompat(getApplicationContext(), TAG, componentName,
        null);
    mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

    Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
    mediaButtonIntent.setClass(this, MediaButtonReceiver.class);
    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
        mediaButtonIntent, 0);
    mediaSession.setMediaButtonReceiver(pendingIntent);

    mediaSession.setCallback(mediaSessionCallback);

    AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
    audioManager.requestAudioFocus(new AudioManager.OnAudioFocusChangeListener() {
      @Override
      public void onAudioFocusChange(int focusChange) {
        Log.e(TAG, "onAudioFocusChange " + focusChange);
      }
    }, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    Log.v(TAG, "onStartCommand " + intent);
    MediaButtonReceiver.handleIntent(mediaSession, intent);
    return START_STICKY;
  }

  @Override
  public boolean onUnbind(Intent intent) {
    return super.onUnbind(intent);
  }

  @Override
  public boolean stopService(Intent name) {
    return super.stopService(name);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mediaSession.release();
  }

  private void showMediaNotification(boolean playing) {
    NotificationCompat.Builder builder = MediaStyleHelper.from(getApplicationContext(), mediaSession);

    int playPauseButtonIndex = 0;
    if (playing) {
      playPauseButtonIndex = 1;
      builder.addAction(new android.support.v4.app.NotificationCompat.Action(
          R.drawable.ic_notification_rewind, "Backward",
          MediaButtonReceiver.buildMediaButtonPendingIntent(getApplicationContext(), PlaybackStateCompat.ACTION_REWIND))
      );
      builder.addAction(new android.support.v4.app.NotificationCompat.Action(
          R.drawable.ic_notification_pause, "Pause",
          MediaButtonReceiver.buildMediaButtonPendingIntent(getApplicationContext(), PlaybackStateCompat.ACTION_PAUSE))
      );
      builder.addAction(new android.support.v4.app.NotificationCompat.Action(
          R.drawable.ic_notification_forward, "Forward",
          MediaButtonReceiver.buildMediaButtonPendingIntent(getApplicationContext(), PlaybackStateCompat.ACTION_FAST_FORWARD))
      );
    } else {
      builder.addAction(new android.support.v4.app.NotificationCompat.Action(
          R.drawable.ic_notification_play_arrow, "Play",
          MediaButtonReceiver.buildMediaButtonPendingIntent(getApplicationContext(), PlaybackStateCompat.ACTION_PLAY))
      );
    }

    int[] attrs = {R.attr.audio_player_notification_background};
    TypedArray ta = getApplication().obtainStyledAttributes(R.style.ContentBlocksTheme_AudioPlayer, attrs);
    int color = ta.getColor(0, Color.WHITE);
    ta.recycle();

    builder
        .setSmallIcon(R.drawable.ic_notification_stat_audio)
        .setColor(color)
        .setStyle(new NotificationCompat.MediaStyle()
            .setShowActionsInCompactView(playPauseButtonIndex)
            .setMediaSession(mediaSession.getSessionToken())
            .setShowCancelButton(true)
            .setCancelButtonIntent(MediaButtonReceiver.buildMediaButtonPendingIntent(getApplicationContext(), PlaybackStateCompat.ACTION_STOP)));

    Notification notification = builder.build();

    NotificationManager notificationManager =
        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    if (notificationManager != null) {
      notificationManager.notify(NOTIFICATION_ID, notification);
    }
  }

  private void cancelMediaNotification() {
    NotificationManager notificationManager =
        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    if (notificationManager != null) {
      notificationManager.cancel(NOTIFICATION_ID);
    }
  }

  private int getBackgroundColor() {
    return 0;
  }
}
