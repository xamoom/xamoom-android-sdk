package com.xamoom.android.xamoomsdk.Storage;

import com.xamoom.android.xamoomsdk.APICallback;
import com.xamoom.android.xamoomsdk.APIListCallback;
import com.xamoom.android.xamoomsdk.EnduserApi;
import com.xamoom.android.xamoomsdk.Enums.SpotFlags;
import com.xamoom.android.xamoomsdk.Resource.Content;
import com.xamoom.android.xamoomsdk.Resource.Spot;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import at.rags.morpheus.Error;

public class OfflineStorageTagModule {
  private static final int PAGE_SIZE = 100;

  private OfflineStorageManager mOfflineStorageManager;
  private EnduserApi mEnduserApi;
  private ArrayList<String> mOfflineTags = new ArrayList<>();

  private ArrayList<Spot> mAllSpots = new ArrayList<>();
  private ArrayList<Content> mAllContents = new ArrayList<>();


  public OfflineStorageTagModule(OfflineStorageManager manager, EnduserApi api) {
    mOfflineStorageManager = manager;
    mEnduserApi = api;
  }

  public void downloadAndSaveWithTags(ArrayList<String> tags,
                                      final DownloadManager.OnDownloadManagerCompleted downloadCallback,
                                      final APIListCallback<List<Spot>, List<Error>> callback) {
    mAllSpots.clear();
    mOfflineTags.addAll(tags);
    downloadAllSpots(tags, null, new APIListCallback<List<Spot>, List<Error>>() {
      @Override
      public void finished(List<Spot> result, String cursor, boolean hasMore) {
        for (Spot spot : result) {
          try {
            mOfflineStorageManager.saveSpot(spot, downloadCallback);
          } catch (MalformedURLException e) {
            e.printStackTrace();
          }
        }

        downloadContentsFromSpots((ArrayList<Spot>) result, new APIListCallback<List<Content>, List<Error>>() {
          @Override
          public void finished(List<Content> result, String cursor, boolean hasMore) {
            for (Content content : result) {
              try {
                mOfflineStorageManager.saveContent(content, downloadCallback);
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

  public void deleteWithTags(final ArrayList<String> tags) {
    mOfflineTags.removeAll(tags);

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
            mOfflineStorageManager.deleteSpot(spot.getId());
          }
        }
      }

      @Override
      public void error(List<Error> error) {
        return;
      }
    });

    getAllContentsWithTags(tags, null, new APIListCallback<List<Content>, List<Error>>() {
      @Override
      public void finished(List<Content> result, String cursor, boolean hasMore) {
        for (Content content : result) {
          boolean shouldDelete = true;
          for (String tag : content.getTags()) {
            if (mOfflineTags.contains(tag)) {
              shouldDelete = false;
            }
          }

          if (shouldDelete) {
            mOfflineStorageManager.deleteContent(content.getId());
          }
        }
      }

      @Override
      public void error(List<Error> error) {
        return;
      }
    });
  }

  private void getAllSpotsWithTags(final ArrayList<String> tags, String cursor,
                                   final APIListCallback<List<Spot>, List<Error>> callback) {
    mAllSpots.clear();
    mOfflineStorageManager.getSpotsByTags(tags, PAGE_SIZE, cursor, null, null, new APIListCallback<List<Spot>, List<Error>>() {
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

  private void getAllContentsWithTags(final ArrayList<String> tags, String cursor,
                                      final APIListCallback<List<Content>, List<Error>> callback) {
    mAllContents.clear();
    mOfflineStorageManager.getContentByTags(tags, PAGE_SIZE, cursor, null, new APIListCallback<List<Content>, List<Error>>() {
      @Override
      public void finished(List<Content> result, String cursor, boolean hasMore) {
        mAllContents.addAll(result);
        if (hasMore) {
          getAllContentsWithTags(tags, cursor, callback);
        } else {
          callback.finished(mAllContents, null, false);
        }
      }

      @Override
      public void error(List<Error> error) {
        callback.error(error);
      }
    });
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

  public ArrayList<String> getOfflineTags() {
    return mOfflineTags;
  }
}
