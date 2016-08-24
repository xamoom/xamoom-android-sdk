![](https://xamoom.com/wp-inhalte/uploads/2015/02/logo-black-claim1.png)

# xamoom-android-sdk
[ ![Download](https://api.bintray.com/packages/xamoom/maven/xamoomsdk/images/download.svg) ](https://bintray.com/xamoom/maven/xamoomsdk/_latestVersion)

With the xamoom-android-sdk we created a simple SDK to let you create apps based on our system.

More informations about xamoom and how xamoom works? Visit our Github page [xamoom Github Wiki](https://github.com/xamoom/xamoom.github.io/wiki).

# Getting Started

* Read the ["Getting Started"](https://github.com/xamoom/xamoom-android-sdk/wiki#getting-started) guide in the wiki (not up to date)
* Check out the [documentation](http://xamoom.github.io/xamoom-android-sdk/docs/com/xamoom/android/XamoomEndUserApi.html) (not up to date)
* Check out our sample app: ["pingeborg App"](https://github.com/xamoom/xamoom-pingeborg-android)

# Installation

## Installation with Gradle

Add in your Gradle in dependencies

    compile 'com.xamoom.android:xamoomsdk:2.2.3'

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
EnduserApi mEnduserApi = new EnduserApi(API_KEY);
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

### Documentation

Here you can find the full java   [documentation](http://xamoom.github.io/xamoom-android-sdk/docs/com/xamoom/android/xamoomsdk/EnduserApi.html).

# Requirements

* Android 4.3
