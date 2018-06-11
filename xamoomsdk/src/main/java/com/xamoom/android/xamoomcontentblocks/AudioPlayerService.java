package com.xamoom.android.xamoomcontentblocks;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
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
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.app.NotificationCompat.MediaStyle;
import com.xamoom.android.xamoomsdk.R;

import java.util.ArrayList;
import java.util.HashMap;

public class AudioPlayerService extends Service {
  private static final String TAG = AudioPlayerService.class.getSimpleName();
  private static final int NOTIFICATION_ID = 1;
  private static final int SEEK_TIME = 30000;
  private static final String CHANNEL_ID = "media_playback_channel";

  public static final int MSG_REGISTER_CLIENT = 1;
  public static final int MSG_UNREGISTER_CLIENT = 2;
  public static final int MSG_SET_URL = 3;
  public static final int MSG_ACTION_SEEK_FORWARD = 4;
  public static final int MSG_ACTION_SEEK_BACKWARD = 5;
  public static final int MSG_ACTION_PLAY = 12;
  public static final int MSG_ACTION_PAUSE = 6;
  public static final int MSG_AUDIO_EVENT_STARTED = 7;
  public static final int MSG_AUDIO_EVENT_PAUSED = 8;
  public static final int MSG_AUDIO_EVENT_FINISHED = 9;
  public static final int MSG_AUDIO_EVENT_UPDATE_PROGRESS = 10;
  public static final int MSG_AUDIO_EVENT_UPDATE_LOADING = 11;

  private ArrayList<Messenger> clients = new ArrayList<Messenger>();
  private HashMap<MediaFile, Messenger> mediaFileMessengers = new HashMap<>();
  private AudioPlayer audioPlayer;
  private MediaSessionCompat mediaSession;
  private AudioManager audioManager;
  final Messenger messenger = new Messenger(new IncomingHandler());

  class IncomingHandler extends Handler {
    @Override
    public void handleMessage(final Message msg) {
      String position;
      switch (msg.what) {
        case MSG_REGISTER_CLIENT:
          clients.add(msg.replyTo);
          break;
        case MSG_UNREGISTER_CLIENT:
          clients.remove(msg.replyTo);
          break;
        case MSG_SET_URL:
          position = msg.getData().getString("POS");
          String url = msg.getData().getString("URL");
          String title = msg.getData().getString("TITLE");
          String artist = msg.getData().getString("ARTIST");

          MediaFile mediaFile = audioPlayer.createMediaFile(Uri.parse(url), position, title,
              artist, null);
          mediaFileMessengers.put(mediaFile, msg.replyTo);
          mediaFile.setEventListener(mediaFileEventListener);

          try {
            Message msgAnswer = Message.obtain(null, MSG_AUDIO_EVENT_UPDATE_PROGRESS,
                0, 0);
            Bundle bundle = new Bundle();
            bundle.putLong("DURATION", mediaFile.getDuration());
            bundle.putLong("POSITION", mediaFile.getPlaybackPosition());
            msgAnswer.setData(bundle);
            msg.replyTo.send(msgAnswer);
          } catch (RemoteException e) {
            e.printStackTrace();
          }

          if (mediaFile.getState() == MediaFile.State.PLAYING) {
            try {
              msg.replyTo.send(Message.obtain(null, MSG_AUDIO_EVENT_STARTED,
                  0,0));
            } catch (RemoteException e) {
              e.printStackTrace();
            }
          }
          break;
        case MSG_ACTION_PLAY:
          position = msg.getData().getString("POS");
          if (requestAudioFocus()) {
            audioPlayer.start(position);
          }
          break;
        case MSG_ACTION_PAUSE:
          if (audioPlayer.getCurrentMediaFile() != null) {
            audioPlayer.getCurrentMediaFile().pause();
          }
          break;
        case MSG_ACTION_SEEK_FORWARD:
          mediaFile = audioPlayer.getCurrentMediaFile();
          if (mediaFile != null) {
            mediaFile.seekForward(SEEK_TIME);
          }
          break;
        case MSG_ACTION_SEEK_BACKWARD:
          mediaFile = audioPlayer.getCurrentMediaFile();
          if (mediaFile != null) {
            mediaFile.seekBackward(SEEK_TIME);
          }
          break;
        default:
          super.handleMessage(msg);
      }
    }
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return messenger.getBinder();
  }

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

    audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

    registerBecomingNoisyReceiver();
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    MediaButtonReceiver.handleIntent(mediaSession, intent);
    return START_NOT_STICKY;
  }

  @Override
  public boolean onUnbind(Intent intent) {
    return super.onUnbind(intent);
  }

  @Override
  public boolean stopService(Intent name) {
    stopForeground(true);
    cancelMediaNotification();
    return super.stopService(name);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mediaSession.release();
    cancelMediaNotification();
    unregisterBecomingNoisyReceiver();
  }

  private MediaFile.EventListener mediaFileEventListener = new MediaFile.EventListener() {
    @Override
    public void loadingChanged(boolean isLoading) {
      if (audioPlayer.getCurrentMediaFile() == null) {
        return;
      }

      Messenger messenger = mediaFileMessengers.get(audioPlayer.getCurrentMediaFile());
      if (messenger == null) {
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
      MediaFile mediaFile = audioPlayer.getCurrentMediaFile();
      if (mediaFile == null) {
        return;
      }
      mediaSession.setActive(true);

      mediaSession.setMetadata(new MediaMetadataCompat.Builder()
          .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, mediaFile.getUri().toString())
          .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, mediaFile.getAlbum())
          .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, mediaFile.getArtist())
          .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, mediaFile.getDuration())
          .putString(MediaMetadataCompat.METADATA_KEY_TITLE, mediaFile.getTitle())
          .build());

      mediaSession.setPlaybackState(new PlaybackStateCompat.Builder()
          .setState(PlaybackStateCompat.STATE_PLAYING,
              PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN, 1.0f)
          .setActions(PlaybackStateCompat.ACTION_PAUSE |
              PlaybackStateCompat.ACTION_PLAY_PAUSE |
              PlaybackStateCompat.ACTION_FAST_FORWARD |
              PlaybackStateCompat.ACTION_REWIND |
              PlaybackStateCompat.ACTION_STOP)
          .build());

      startForeground(NOTIFICATION_ID, createMediaNotification(true));

      Messenger messenger = mediaFileMessengers.get(mediaFile);
      if (messenger == null) {
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
      mediaSession.setPlaybackState(new PlaybackStateCompat.Builder()
          .setState(PlaybackStateCompat.STATE_PAUSED,
              PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN, 0)
          .setActions(PlaybackStateCompat.ACTION_PLAY |
              PlaybackStateCompat.ACTION_PLAY_PAUSE |
              PlaybackStateCompat.ACTION_FAST_FORWARD |
              PlaybackStateCompat.ACTION_REWIND |
              PlaybackStateCompat.ACTION_STOP)
          .build());

      stopForeground(false);
      Notification notification = createMediaNotification(false);
      NotificationManager notificationManager =
          (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
      if (notificationManager != null) {
        notificationManager.notify(NOTIFICATION_ID, notification);
      }

      Messenger messenger = mediaFileMessengers.get(audioPlayer.getCurrentMediaFile());
      if (messenger == null) {
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
      stopForeground(true);
      dismissAudioFocus();

      mediaSession.setActive(false);

      if (audioPlayer.getCurrentMediaFile() == null) {
        return;
      }

      Messenger messenger = mediaFileMessengers.get(audioPlayer.getCurrentMediaFile());
      if (messenger == null) {
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
      MediaFile mediaFile = audioPlayer.getCurrentMediaFile();
      if (mediaFile == null) {
        return;
      }

      mediaSession.setMetadata(new MediaMetadataCompat.Builder()
          .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, mediaFile.getUri().toString())
          .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, mediaFile.getAlbum())
          .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, mediaFile.getArtist())
          .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, mediaFile.getDuration())
          .putString(MediaMetadataCompat.METADATA_KEY_TITLE, mediaFile.getTitle())
          .build());

      mediaSession.setPlaybackState(new PlaybackStateCompat.Builder()
          .setState(PlaybackStateCompat.STATE_PLAYING,
              PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN, 1.0f)
          .setActions(PlaybackStateCompat.ACTION_PAUSE |
              PlaybackStateCompat.ACTION_PLAY_PAUSE |
              PlaybackStateCompat.ACTION_FAST_FORWARD |
              PlaybackStateCompat.ACTION_REWIND |
              PlaybackStateCompat.ACTION_STOP)
          .build());

      Messenger messenger = mediaFileMessengers.get(mediaFile);
      if (messenger == null) {
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
  };

  private MediaSessionCompat.Callback mediaSessionCallback = new MediaSessionCompat.Callback() {
    @Override
    public boolean onMediaButtonEvent(Intent mediaButtonEvent) {
      return super.onMediaButtonEvent(mediaButtonEvent);
    }

    @Override
    public void onPlay() {
      MediaFile mediaFile = audioPlayer.getCurrentMediaFile();
      if (mediaFile != null) {
        mediaFile.start();
      }
      super.onPlay();
    }

    @Override
    public void onPause() {
      MediaFile mediaFile = audioPlayer.getCurrentMediaFile();
      if (mediaFile != null) {
        audioPlayer.getCurrentMediaFile().pause();
      }
      super.onPause();
    }

    @Override
    public void onFastForward() {
      MediaFile mediaFile = audioPlayer.getCurrentMediaFile();
      if (mediaFile != null) {
        mediaFile.seekForward(SEEK_TIME);
      }
      super.onFastForward();
    }

    @Override
    public void onRewind() {
      MediaFile mediaFile = audioPlayer.getCurrentMediaFile();
      if (mediaFile != null) {
        mediaFile.seekBackward(SEEK_TIME);
      }
      super.onRewind();
    }

    @Override
    public void onStop() {
      MediaFile mediaFile = audioPlayer.getCurrentMediaFile();
      if (mediaFile != null) {
        mediaFile.pause();
      }
      super.onStop();
    }
  };

  AudioManager.OnAudioFocusChangeListener audioFocusChangeListener =
      new AudioManager.OnAudioFocusChangeListener() {
    @Override
    public void onAudioFocusChange(int focusChange) {
      switch (focusChange) {
        case AudioManager.AUDIOFOCUS_LOSS:
          break;
        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
          // fall trough
        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
          if (audioPlayer.getCurrentMediaFile() != null) {
            audioPlayer.getCurrentMediaFile().pause();
          }
          break;
        case AudioManager.AUDIOFOCUS_GAIN:
          break;
      }
    }
  };

  private boolean requestAudioFocus() {
    int result = audioManager.requestAudioFocus(audioFocusChangeListener, AudioManager.STREAM_MUSIC,
        AudioManager.AUDIOFOCUS_GAIN);
    return result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
  }

  private void dismissAudioFocus() {
    audioManager.abandonAudioFocus(audioFocusChangeListener);
  }

  private void registerBecomingNoisyReceiver() {
    //register after getting audio focus
    IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
    registerReceiver(noisyReceiver, intentFilter);
  }

  private void unregisterBecomingNoisyReceiver() {
    unregisterReceiver(noisyReceiver);
  }

  private BroadcastReceiver noisyReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      if (audioPlayer.getCurrentMediaFile() != null) {
        audioPlayer.getCurrentMediaFile().pause();
      }
    }
  };

  private Notification createMediaNotification(boolean playing) {

    NotificationCompat.Builder builder = MediaStyleHelper.from(getApplicationContext(), mediaSession);

    int playPauseButtonIndex = 0;
    if (playing) {
      playPauseButtonIndex = 1;
      builder.addAction(new android.support.v4.app.NotificationCompat.Action(
          R.drawable.ic_notification_rewind, "Backward",
          MediaButtonReceiver.buildMediaButtonPendingIntent(getApplicationContext(),
              PlaybackStateCompat.ACTION_REWIND))
      );
      builder.addAction(new android.support.v4.app.NotificationCompat.Action(
          R.drawable.ic_notification_pause, "Pause",
          MediaButtonReceiver.buildMediaButtonPendingIntent(getApplicationContext(),
              PlaybackStateCompat.ACTION_PAUSE))
      );
      builder.addAction(new android.support.v4.app.NotificationCompat.Action(
          R.drawable.ic_notification_forward, "Forward",
          MediaButtonReceiver.buildMediaButtonPendingIntent(getApplicationContext(),
              PlaybackStateCompat.ACTION_FAST_FORWARD))
      );
    } else {
      builder.addAction(new android.support.v4.app.NotificationCompat.Action(
          R.drawable.ic_notification_play_arrow, "Play",
          MediaButtonReceiver.buildMediaButtonPendingIntent(getApplicationContext(),
              PlaybackStateCompat.ACTION_PLAY))
      );
    }

    builder
        .setSmallIcon(R.drawable.ic_notification_stat_audio)
        .setStyle(new MediaStyle()
            .setShowActionsInCompactView(playPauseButtonIndex)
            .setMediaSession(mediaSession.getSessionToken())
            .setShowCancelButton(true)
            .setCancelButtonIntent(
                MediaButtonReceiver.buildMediaButtonPendingIntent(getApplicationContext(),
                    PlaybackStateCompat.ACTION_STOP)));

    // setting the color only on Nougat (7.0.0 - API Level 24) and above, because 6.0.0 Samsung
    // devices would also use the color without adjusting text colors
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      builder.setColor(getBackgroundColor());
    }

    // add default largeIcon, when below Nougat, cause some devices will else use the small
    // icon and this does not look great
    if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
      Bitmap image = getDefaultAudioPlayerAsset();
      if (image != null) {
        builder.setLargeIcon(image);
      }
    }

    return builder.build();
  }

  private void cancelMediaNotification() {
    NotificationManager notificationManager =
        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    if (notificationManager != null) {
      notificationManager.cancel(NOTIFICATION_ID);
    }
  }

  private int getBackgroundColor() {
    int[] attrs = {R.attr.audio_player_notification_background};
    TypedArray ta = getApplication().obtainStyledAttributes(R.style.ContentBlocksTheme_AudioPlayer, attrs);
    int color = ta.getColor(0, Color.WHITE);
    ta.recycle();

    return color;
  }

  private Bitmap getDefaultAudioPlayerAsset() {
    int[] attrs = {R.attr.audio_player_default_asset_drawable};
    TypedArray ta = getApplication().obtainStyledAttributes(R.style.ContentBlocksTheme_AudioPlayer, attrs);
    int resourceId = ta.getResourceId(0, -1);
    if (resourceId == -1) {
      return null;
    }
    ta.recycle();

    return BitmapFactory.decodeResource(getApplicationContext().getResources(),
        resourceId);
  }
}
