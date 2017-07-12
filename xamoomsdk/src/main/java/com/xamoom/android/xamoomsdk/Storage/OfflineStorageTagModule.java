/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.xamoomsdk.Storage;

import android.content.Context;
import android.util.Log;

import com.xamoom.android.xamoomsdk.APICallback;
import com.xamoom.android.xamoomsdk.APIListCallback;
import com.xamoom.android.xamoomsdk.EnduserApi;
import com.xamoom.android.xamoomsdk.Enums.SpotFlags;
import com.xamoom.android.xamoomsdk.Resource.Content;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;
import com.xamoom.android.xamoomsdk.Resource.Spot;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import at.rags.morpheus.Error;

/**
 * OfflineStorageTagModule is used to download and delete spots marked with special tags.
 * Use this if you want to download e.g. tours.
 *
 * This module will automatically save everything needed to show data when downloading spots with
 * a custom tag.
 *
 * This module will automatically check if there are dependencies between entities and files.
 */
public class OfflineStorageTagModule {
  private static final String TAG = OfflineStorageTagModule.class.getSimpleName();
  private static final int PAGE_SIZE = 100;

  private OfflineStorageManager mOfflineStorageManager;
  private EnduserApi mEnduserApi;
  private ArrayList<String> mOfflineTags;
  private SimpleTagStorage mSimpleTagStorage;

  private ArrayList<Spot> mAllSpots = new ArrayList<>();
  private ArrayList<Content> mAllContents = new ArrayList<>();

  /**
   * Construct with your storagemanager and already setup enduserApi.
   * @param manager OfflineStorageAdapter instance.
   * @param api With api key setup enduserApi instance.
   */
  public OfflineStorageTagModule(OfflineStorageManager manager, EnduserApi api, Context context) {
    mOfflineStorageManager = manager;
    mEnduserApi = api;
    mSimpleTagStorage = new SimpleTagStorage(context);
  }

  /**
   * Will download all spots with tag and their content and save entities and files.
   *  @param tags ArrayList<String> of tags.
   * @param queueDownloads Queue downloads to load all at once. Use
   * {@link DownloadManager#downloadQueriedTasks()} to start downloads.
   * @param callback Callback when finished saving to database.
   * @param downloadCallback Download callback for file downloads.
   */
  public void downloadAndSaveWithTags(ArrayList<String> tags, final boolean queueDownloads,
                                      final APIListCallback<List<Spot>, List<Error>> callback,
                                      final DownloadManager.OnDownloadManagerCompleted downloadCallback) {
    mAllSpots.clear();
    mOfflineTags = mSimpleTagStorage.getTags();
    addOfflineTags(tags);
    mSimpleTagStorage.saveTags(mOfflineTags);

    downloadAllSpots(tags, null, new APIListCallback<List<Spot>, List<Error>>() {
      @Override
      public void finished(List<Spot> result, String cursor, boolean hasMore) {
        for (Spot spot : result) {
          try {
            mOfflineStorageManager.saveSpot(spot, queueDownloads, downloadCallback);
          } catch (MalformedURLException e) {
            e.printStackTrace();
          }
        }

        downloadContentsFromSpots((ArrayList<Spot>) result, new APIListCallback<List<Content>, List<Error>>() {
          @Override
          public void finished(List<Content> result, String cursor, boolean hasMore) {
            for (Content content : result) {
              try {
                mOfflineStorageManager.saveContent(content, queueDownloads, downloadCallback);
              } catch (MalformedURLException e) {
                e.printStackTrace();
              }
            }

            callback.finished(mAllSpots, null, false);
          }

          @Override
          public void error(List<Error> error) {
            callback.error(error);
          }
        });
      }

      @Override
      public void error(List<Error> error) {
        callback.error(error);
      }
    });
  }

  private void downloadAllSpots(final ArrayList<String> tags, String cursor,
                                final APIListCallback<List<Spot>, List<Error>> callback) {
    mEnduserApi.getSpotsByTags(tags, PAGE_SIZE, cursor,
        EnumSet.of(SpotFlags.INCLUDE_CONTENT, SpotFlags.INCLUDE_MARKERS), null,
        new APIListCallback<List<Spot>, List<Error>>() {
          @Override
          public void finished(List<Spot> result, String cursor, boolean hasMore) {
            mAllSpots.addAll(result);

            if (hasMore) {
              downloadAllSpots(tags, cursor, callback);
            } else {
              callback.finished(mAllSpots, null, false);
            }
          }

          @Override
          public void error(List<Error> error) {
            callback.error(error);
          }
        });
  }

  private void downloadContentsFromSpots(ArrayList<Spot> spots,
                                         final APIListCallback<List<Content>, List<Error>> callback) {
    final ArrayList<Content> contents = new ArrayList<>();
    int contentCount = 0;

    for (Spot spot : spots) {
      if (spot.getContent() != null) {
        contentCount++;
        final int finalContentCount = contentCount;

        mEnduserApi.getContent(spot.getContent().getId(), new APICallback<Content, List<Error>>() {
          @Override
          public void finished(Content result) {
            contents.add(result);

            if (contents.size() == finalContentCount) {
              callback.finished(contents, null, false);
            }
          }

          @Override
          public void error(List<Error> error) {
            callback.error(error);
          }
        });
      }

      contentCount++;
    }
  }

  /**
   * Deletes all spots and connected content with tag, when there is no dependency to another tag.
   *
   * @param tags List of tags to delete.
   */
  public void deleteWithTags(final ArrayList<String> tags) {
    mOfflineTags = mSimpleTagStorage.getTags();
    mOfflineTags.removeAll(tags);
    mSimpleTagStorage.saveTags(mOfflineTags);

    final ArrayList<Spot> spotsToDelete = new ArrayList<>();

    getAllSpotsWithTags(tags, null, new APIListCallback<List<Spot>, List<Error>>() {
      @Override
      public void finished(List<Spot> result, String cursor, boolean hasMore) {
        for (Spot spot : result) {
          boolean shouldDelete = true;
          for (String tag : spot.getTags()) {
            if (mOfflineTags.contains(tag)) {
              shouldDelete = false;
            }
          }

          if (shouldDelete) {
            if (spot.getPublicImageUrl() != null) {
              queueSpotFilesForDeletion(spot.getPublicImageUrl());
            }
            spotsToDelete.add(spot);
          }
        }

        for (Spot spot : spotsToDelete) {
          mOfflineStorageManager.deleteSpot(spot.getId());

          if (spot.getContent() != null) {
            ArrayList<Spot> spotsWithThatContent = getSpotsWithContent(spot.getContent().getId());
            spotsWithThatContent.removeAll(spotsToDelete);

            if (spotsWithThatContent.size() == 0) {
              mOfflineStorageManager.deleteContent(spot.getContent().getId());
              queueContentFilesForDeletion(spot.getContent());
            }
          }
        }

        try {
          mOfflineStorageManager.deleteFilesWithSafetyCheck();
        } catch (IOException e) {
          Log.e(TAG, "File not found to delete.");
        }
      }

      @Override
      public void error(List<Error> error) {
        Log.e(TAG, "Error getting all Spots: " + error);
      }
    });
  }

  private void getAllSpotsWithTags(final ArrayList<String> tags, String cursor,
                                   final APIListCallback<List<Spot>, List<Error>> callback) {
    mAllSpots.clear();
    mOfflineStorageManager.getSpotsByTags(tags, PAGE_SIZE, cursor, null, new APIListCallback<List<Spot>, List<Error>>() {
      @Override
      public void finished(List<Spot> result, String cursor, boolean hasMore) {
        mAllSpots.addAll(result);
        if (hasMore) {
          getAllSpotsWithTags(tags, cursor, callback);
        } else {
          callback.finished(mAllSpots, null, false);
        }
      }

      @Override
      public void error(List<Error> error) {
        callback.error(error);
      }
    });
  }

  private ArrayList<Spot> getSpotsWithContent(String contentId) {
    ArrayList<Spot> spotsWithContent = new ArrayList<>();
    ArrayList<Spot> allSpots = mOfflineStorageManager.getSpotDatabaseAdapter().getAllSpots();
    for (Spot spot : allSpots) {
      if (spot.getContent() != null && spot.getContent().getId() == contentId) {
        spotsWithContent.add(spot);
      }
    }
    return spotsWithContent;
  }

  private void queueSpotFilesForDeletion(String urlString) {
    try {
      mOfflineStorageManager.deleteFile(urlString, true);
    } catch (IOException e) {
      Log.e(TAG, urlString + " not found in internal storage");
    }
  }

  private void queueContentFilesForDeletion(Content content) {
    if (content.getPublicImageUrl() != null) {
      try {
        mOfflineStorageManager.deleteFile(content.getPublicImageUrl(), true);
      } catch (IOException e) {
        Log.e(TAG, content.getPublicImageUrl() + " not found in internal storage");
      }
    }

    if (content.getContentBlocks() != null) {
      for (ContentBlock block : content.getContentBlocks()) {
        if (block.getVideoUrl() != null) {
          try {
            mOfflineStorageManager.deleteFile(block.getVideoUrl(), true);
          } catch (IOException e) {
            Log.e(TAG, block.getVideoUrl() + " not found in internal storage");
          }
        }

        if (block.getFileId() != null) {
          try {
            mOfflineStorageManager.deleteFile(block.getFileId(), true);
          } catch (IOException e) {
            Log.e(TAG, block.getFileId() + " not found in internal storage");
          }
        }
      }
    }
  }

  private void addOfflineTags(ArrayList<String> tags) {
    for (String tag : tags) {
      if (!mOfflineTags.contains(tag)) {
        mOfflineTags.add(tag);
      }
    }
  }

  // getter & setter

  public EnduserApi getEnduserApi() {
    return mEnduserApi;
  }

  public void setEnduserApi(EnduserApi enduserApi) {
    mEnduserApi = enduserApi;
  }

  public OfflineStorageManager getOfflineStorageManager() {
    return mOfflineStorageManager;
  }

  public void setOfflineStorageManager(OfflineStorageManager offlineStorageManager) {
    mOfflineStorageManager = offlineStorageManager;
  }

  public void setSimpleTagStorage(SimpleTagStorage simpleTagStorage) {
    mSimpleTagStorage = simpleTagStorage;
  }

  public ArrayList<String> getOfflineTags() {
    return mOfflineTags;
  }
}
