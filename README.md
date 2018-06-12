![](https://storage.googleapis.com/xamoom-files/cb9dcdd940f44b53baf5c27f331c4079.png)

# xamoom-android-sdk
[ ![Download](https://api.bintray.com/packages/xamoom/maven/xamoomsdk/images/download.svg) ](https://bintray.com/xamoom/maven/xamoomsdk/_latestVersion)

With the xamoom-android-sdk we created a simple SDK to let you create apps based on our system.

More informations about xamoom and how xamoom works? Visit our Github page [xamoom Github Wiki](https://github.com/xamoom/xamoom.github.io/wiki).

# Getting Started

* Read the ["Getting Started"](https://github.com/xamoom/xamoom-android-sdk/wiki#getting-started) guide in the wiki (not up to date)
* Check out the [documentation](https://xamoom.github.io/xamoom-android-sdk/3.7.0/)
* Check out our sample app: ["pingeborg App"](https://github.com/xamoom/xamoom-pingeborg-android)

# Installation

## Installation with Gradle

Add in your Gradle in dependencies

    compile 'com.xamoom.android:xamoomsdk:3.10.2'

# Usage

## Setup XamoomEnduserApi

Add internet permission to your manifest and your google play services with
a geo api key.

    <uses-permission android:name="android.permission.INTERNET"/>

    <meta-data
        android:name="com.google.android.gms.version"
        android:value="@integer/google_play_services_version" />

    <meta-data
        android:name="com.google.android.geo.API_KEY"
        android:value="YOUR_API_KEY" />

## Make your first call

Grab a contentId from your [xamoom-system](https://xamoom.net/) (open a page and copy id from url) and make your first call like this:

```java
EnduserApi mEnduserApi = new EnduserApi(API_KEY, context);
mEnduserApi.getContent(CONTENT_ID, new APICallback<Content, List<Error>>() {
  @Override
  public void finished(Content result) {
    Log.v(TAG, "Content: " + result;)
  }

  @Override
  public void error(List<Error> error) {

  }
});
```

## Show your content

Use the XamoomContentFragment to show your content.

```java
XamoomContentFragment xamoomFragment = XamoomContentFragment.newInstance("YOUTUBE_API_KEY"); //create new instance
xamoomFragment.setEnduserApi(mEnduserApi);
xamoomFragment.setDisplayAllStoreLinks(true);
xamoomFragment.setContent(result);
xamoomFragment.setShowSpotMapContentLinks(true);
getSupportFragmentManager().beginTransaction()
    .replace(R.id.main_frame, xamoomFragment, "XamoomFragment")
    .commit();
```

## Beacons

To monitor and range beacons use the [Android-Beacon-Library](https://github.com/AltBeacon/android-beacon-library).
Then get your connected content with:
```java
mEnduserApi.getContentByBeacon(MAJOR, MINOR, new APICallback<Content, List<Error>>() {
      @Override
      public void finished(Content result) {
        Log.v(TAG, "Content: " + result;)
      }

      @Override
      public void error(List<Error> error) {

      }
    });
```

## Offline

### Save & get entities offline

You can save your entities offline and use them even if you are not connected
to the internet.

To save entities offline use `OfflineStorageManager` methods for saving.
For example to save a content works like this:

```java
OfflineStorageManager manager =
OfflineStorageManager.getInstance(getApplicationContext());
       try {
         manager.saveContent(result, false, new DownloadManager.OnDownloadManagerCompleted() {
           @Override
           public void completed(String urlString) {
             Log.v(TAG, "Content image saved: " + urlString);
           }

           @Override
           public void failed(String urlString, DownloadError downloadError) {
             Log.v(TAG, "Failed saving: " + urlString);
           }
         });
       } catch (MalformedURLException e) {
         e.printStackTrace();
       }
```

To get offline saved entities use the `EnduserApi` and set the offline property
to true:
```java
mEnduserApi.setOffline(true);
```
All `EnduserApi` calls will now return you the offline saved entities.

### Push Notification

To use our Push Notifications please check our [Push Notification Documentation](https://github.com/xamoom/xamoom-android-sdk/wiki/Push-Notifications).


### Documentation

Here you can find the full java [documentation](https://xamoom.github.io/xamoom-android-sdk/3.1.0/).

# Requirements

* Android 4.3
