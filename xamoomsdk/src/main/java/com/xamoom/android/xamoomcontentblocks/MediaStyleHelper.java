package com.xamoom.android.xamoomcontentblocks;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import androidx.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.xamoom.android.xamoomsdk.R;

/**
 * Helper APIs for constructing MediaStyle notifications
 */
public class MediaStyleHelper {

  private static final String CHANNEL_ID = "media_playback_channel";

  @RequiresApi(Build.VERSION_CODES.O)
  private static void createChannel(Context context) {
    NotificationManager
        notificationManager =
        (NotificationManager) context
            .getSystemService(Context.NOTIFICATION_SERVICE);

    if (notificationManager == null) {
      return;
    }

    String id = CHANNEL_ID;
    // The user-visible name of the channel.
    CharSequence name = context.getString(R.string.audioplayer_notification_channel_name);
    // The user-visible description of the channel.
    String description = context.getString(R.string.audioplayer_notification_channel_description);
    int importance = NotificationManager.IMPORTANCE_LOW;
    NotificationChannel channel = new NotificationChannel(id, name, importance);
    // Configure the notification channel.
    channel.setDescription(description);
    channel.setShowBadge(false);
    channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
    notificationManager.createNotificationChannel(channel);
  }
  /**
   * Build a notification using the information from the given media session. Makes heavy use
   * of {@link MediaMetadataCompat#getDescription()} to extract the appropriate information.
   * @param context Context used to construct the notification.
   * @param mediaSession Media session to get information.
   * @return A pre-built notification with information from the given media session.
   */
  public static NotificationCompat.Builder from(
      Context context, MediaSessionCompat mediaSession) {
    MediaControllerCompat controller = mediaSession.getController();
    MediaMetadataCompat mediaMetadata = controller.getMetadata();
    MediaDescriptionCompat description = mediaMetadata.getDescription();

    // create notification channel if on android oreo
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      MediaStyleHelper.createChannel(context);
    }

    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
    builder
        .setContentTitle(description.getTitle())
        .setContentText(description.getSubtitle())
        .setSubText(description.getDescription())
        .setLargeIcon(description.getIconBitmap())
        .setContentIntent(controller.getSessionActivity())
        .setDeleteIntent(
            MediaButtonReceiver.buildMediaButtonPendingIntent(context, PlaybackStateCompat.ACTION_STOP))
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
    return builder;
  }
}