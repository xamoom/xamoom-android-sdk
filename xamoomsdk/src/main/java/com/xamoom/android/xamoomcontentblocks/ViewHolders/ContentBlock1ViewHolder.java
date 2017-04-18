/*
* Copyright 2017 by xamoom GmbH <apps@xamoom.com>
*
* This file is part of some open source application.
*
* Some open source application is free software: you can redistribute
* it and/or modify it under the terms of the GNU General Public
* License as published by the Free Software Foundation, either
* version 2 of the License, or (at your option) any later version.
*
* Some open source application is distributed in the hope that it will
* be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
* of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with xamoom-android-sdk. If not, see <http://www.gnu.org/licenses/>.
*
* author: Raphael Seher <raphael@xamoom.com>
*/

package com.xamoom.android.xamoomcontentblocks.ViewHolders;

import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xamoom.android.xamoomcontentblocks.Views.MovingBarsView;
import com.xamoom.android.xamoomsdk.R;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;
import com.xamoom.android.xamoomsdk.Storage.FileManager;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Displays audio content blocks.
 */
public class ContentBlock1ViewHolder extends RecyclerView.ViewHolder {
  private final static int SEEK_TIME = 5000;

  private Fragment mFragment;
  private TextView mTitleTextView;
  private TextView mArtistTextView;
  private TextView mRemainingSongTimeTextView;
  private Button mPlayPauseButton;
  private Button mForwardButton;
  private Button mBackwardButton;
  private MediaPlayer mMediaPlayer;
  private ProgressBar mSongProgressBar;
  private MovingBarsView mMovingBarsView;
  private final Handler mHandler = new Handler();
  private Runnable mRunnable;
  private FileManager mFileManager;
  private boolean mPrepared = false;

  public ContentBlock1ViewHolder(View itemView, Fragment fragment) {
    super(itemView);
    mFragment = fragment;
    mTitleTextView = (TextView) itemView.findViewById(R.id.title_text_view);
    mArtistTextView = (TextView) itemView.findViewById(R.id.artist_text_view);
    mPlayPauseButton = (Button) itemView.findViewById(R.id.play_pause_button);
    mForwardButton = (Button) itemView.findViewById(R.id.forward_button);
    mBackwardButton = (Button) itemView.findViewById(R.id.backward_button);
    mRemainingSongTimeTextView = (TextView) itemView.findViewById(R.id.remaining_song_time_text_view);
    mSongProgressBar = (ProgressBar) itemView.findViewById(R.id.song_progress_bar);
    mFileManager = FileManager.getInstance(fragment.getContext());
    mMovingBarsView = (MovingBarsView) itemView.findViewById(R.id.moving_bars_view);

    mForwardButton.setOnClickListener(mForwardButtonClickListener);
    mBackwardButton.setOnClickListener(mBackwardButtonClickListener);
  }

  public void setupContentBlock(ContentBlock contentBlock, boolean offline) {
    if (contentBlock.getTitle() != null)
      mTitleTextView.setText(contentBlock.getTitle());
    else {
      mTitleTextView.setText(null);
    }

    if (contentBlock.getArtists() != null)
      mArtistTextView.setText(contentBlock.getArtists());
    else {
      mArtistTextView.setText(null);
    }

    Uri fileUrl = null;
    if (offline) {
      String filePath = mFileManager.getFilePath(contentBlock.getFileId());
      fileUrl = Uri.parse(filePath);
    } else {
      if (contentBlock.getFileId() != null) {
        fileUrl = Uri.parse(contentBlock.getFileId());
      }
    }

    if (fileUrl != null) {
      setupMusicPlayer(fileUrl);
    }
  }

  private void setupMusicPlayer(final Uri fileUrl) {
    if(mMediaPlayer == null) {
      mMediaPlayer = new MediaPlayer();
      mMediaPlayer.setOnCompletionListener(mOnCompletionListener);

      mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
      try {
        mMediaPlayer.setDataSource(mFragment.getActivity(), fileUrl);
        mMediaPlayer.prepareAsync();
      } catch (IOException e) {
        e.printStackTrace();
      }

      mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
          mPrepared = true;
          mMediaPlayer.seekTo(0);
          mSongProgressBar.setMax(mMediaPlayer.getDuration());
          mSongProgressBar.setProgress(0);
          mRemainingSongTimeTextView.setText(getTimeString(mMediaPlayer.getDuration()));
        }
      });

      mPlayPauseButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (!mPrepared) {
            return;
          }

          if (mMediaPlayer.isPlaying()) {
            mMovingBarsView.stopAnimation();
            mMediaPlayer.pause();
            mPlayPauseButton.setBackgroundResource(R.drawable.ic_play);
          } else {
            mMovingBarsView.startAnimation();
            mMediaPlayer.start();
            mPlayPauseButton.setBackgroundResource(R.drawable.ic_pause);
            startUpdatingProgress();
          }
        }
      });
    }
  }

  private void resetMediaPlayer() {
    // if the play button is pressed to times fast, it could happen to be in the wrong state
    // when calling the prepareAsync() method.
    if (!mPrepared) {
      return;
    }

    mPrepared = false;
    stopUpdatingProgress();
    mMovingBarsView.stopAnimation();
    if (!mPrepared) {
      mMediaPlayer.prepareAsync();
    }
  }

  private MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
    @Override
    public void onCompletion(MediaPlayer mp) {
      resetMediaPlayer();
    }
  };

  @SuppressLint("DefaultLocale")
  private String getTimeString(int milliseconds) {
    String output;

    if (TimeUnit.MILLISECONDS.toHours(milliseconds) > 0) {
      output = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(milliseconds),
          TimeUnit.MILLISECONDS.toMinutes(milliseconds) % TimeUnit.HOURS.toMinutes(1),
          TimeUnit.MILLISECONDS.toSeconds(milliseconds) % TimeUnit.MINUTES.toSeconds(1));
    } else {
      output = String.format("%02d:%02d",TimeUnit.MILLISECONDS.toMinutes(milliseconds) % TimeUnit.HOURS.toMinutes(1),
          TimeUnit.MILLISECONDS.toSeconds(milliseconds) % TimeUnit.MINUTES.toSeconds(1));
    }

    return output;
  }

  private View.OnClickListener mForwardButtonClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      Log.v("MediaPlayer", "Forward");
      if (mMediaPlayer != null && mPrepared) {
        int seekTime = mMediaPlayer.getCurrentPosition() + SEEK_TIME;
        if (seekTime > mMediaPlayer.getCurrentPosition()) {
          seekTime = mMediaPlayer.getDuration();
        }
        mMediaPlayer.seekTo(seekTime);
        updateProgress();
      }
    }
  };

  private View.OnClickListener mBackwardButtonClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      Log.v("MediaPlayer", "Backward");
      if (mMediaPlayer != null && mPrepared) {
        int seekTime = mMediaPlayer.getCurrentPosition() - SEEK_TIME;
        if (seekTime < 0) {
          seekTime = 0;
        }
        mMediaPlayer.seekTo(seekTime);
        updateProgress();
      }
    }
  };

  private void startUpdatingProgress() {
    //Make sure you update Seekbar on UI thread
    mRunnable = new Runnable() {
      @Override
      public void run() {
        if (mFragment.getActivity() == null) {
          stopUpdatingProgress();
        }

        if (mMediaPlayer != null) {
          updateProgress();
        } else {
          mHandler.removeCallbacks(this);
        }
        mHandler.postDelayed(this, 100);
      }
    };

    mFragment.getActivity().runOnUiThread(mRunnable);
  }

  private void updateProgress() {
    int currentPosition = mMediaPlayer.getCurrentPosition();
    mSongProgressBar.setProgress(currentPosition);
    mRemainingSongTimeTextView.setText(getTimeString((mMediaPlayer.getDuration() - currentPosition)));
  }

  private void stopUpdatingProgress() {
    mHandler.removeCallbacks(mRunnable);
    if (mMediaPlayer != null)
      mMediaPlayer.stop();
    mPlayPauseButton.setBackgroundResource(R.drawable.ic_play);
    mSongProgressBar.setProgress(0);
  }

  public void setMediaPlayer(MediaPlayer mediaPlayer) {
    mMediaPlayer = mediaPlayer;
  }

  public void setFileManager(FileManager fileManager) {
    mFileManager = fileManager;
  }
}