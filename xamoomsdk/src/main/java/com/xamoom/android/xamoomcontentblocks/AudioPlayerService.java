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
  public static final int MSG_PLAY = 4;

  public static final String AUDIO_URL = "AudioPlayerService.audio_url";

  ArrayList<Messenger> clients = new ArrayList<Messenger>();
  HashMap<String, MediaFile> mediaFiles = new HashMap<>();

  private AudioPlayer audioPlayer;

  class IncomingHandler extends Handler {
    @Override
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case MSG_REGISTER_CLIENT:
          clients.add(msg.replyTo);
          break;
        case MSG_UNREGISTER_CLIENT:
          clients.remove(msg.replyTo);
          break;
        case MSG_SET_URL:
          for (int i=clients.size()-1; i>=0; i--) {
            try {
              String url = msg.getData().getString("URL");
              int position = msg.getData().getInt("POS");
              Log.v(TAG, "Got message from ViewHolder: " + url);
              mediaFiles.put(url, audioPlayer.createMediaFile(Uri.parse(url), position));
              clients.get(i).send(Message.obtain(null,
                  MSG_SET_URL, 0, 0));
            } catch (RemoteException e) {
              // The client is dead.  Remove it from the list;
              // we are going through the list from back to front
              // so this is safe to do inside the loop.
              clients.remove(i);
            }
          }
          break;
        case MSG_PLAY:
          Log.v(TAG, "Got play message");
          String url = msg.getData().getString("URL");
          MediaFile mediaFile = mediaFiles.get(url);
          mediaFile.start();
        break;
        default:
          super.handleMessage(msg);
      }
    }
  }

  final Messenger messenger = new Messenger(new IncomingHandler());

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
    Log.v(TAG, "onStartCommand");
    if (intent != null) {
      if (intent.getExtras() != null) {
        String url = intent.getExtras().getString(AUDIO_URL);
        Log.v(TAG, "url " + url);
      }
    }
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
