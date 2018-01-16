package com.xamoom.android.xamoomcontentblocks;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by raphaelseher on 12.01.18.
 */

public class AudioPlayerService extends Service {
  private static final String TAG = AudioPlayerService.class.getSimpleName();

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
          String url = msg.getData().getString("URL");
          position = msg.getData().getInt("POS");
          final MediaFile mediaFile = audioPlayer.createMediaFile(Uri.parse(url), position);
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


  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return messenger.getBinder();
  }

  @Override
  public void onCreate() {
    super.onCreate();
    audioPlayer = new AudioPlayer(getApplicationContext());
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    Log.v(TAG, "onStartCommand " + intent);
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
  }
}
