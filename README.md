![](https://xamoom.com/wp-inhalte/uploads/2015/02/logo-black-claim1.png)

# xamoom-android-sdk
[ ![Download](https://api.bintray.com/packages/xamoom/maven/xamoom-android-sdk/images/download.svg) ](https://bintray.com/xamoom/maven/xamoom-android-sdk/_latestVersion)

With the xamoom-android-sdk we created a simple SDK to let you create apps based on our system.

More informations about xamoom and how xamoom works? Visit our Github page [xamoom Github Wiki](https://github.com/xamoom/xamoom.github.io/wiki)

# Getting Started

* [Download](/xamoom/xamoom-android-sdk/archive/master.zip) the xamoom-android-sdk and try it
* Read the ["Getting Started"](https://github.com/xamoom/xamoom-android-sdk/wiki#getting-started) guide in the wiki
* Check out the [documentation](http://xamoom.github.io/xamoom-android-sdk/docs/com/xamoom/android/XamoomEndUserApi.html)
* Check out our sample app: ["pingeborg App"](https://github.com/xamoom/xamoom-pingeborg-android)

# Installation

## Installation with Gradle

Add in your Gradle in dependencies

    compile 'com.xamoom.android:xamoom-android-sdk:1.0.0'

# Usage

## Setup XamoomEnduserApi

Add internet permission to your manifest.

    <uses-permission android:name="android.permission.INTERNET"/>

## Make your first call

Grab a contentId from your [xamoom-system](https://xamoom.net/) (open a page and copy id from url) and make your first call like this:

```java
XamoomEndUserApi.getInstance(this.getApplicationContext(), API_KEY).getContentbyId(mContentId, false, false, null, true, false, new APICallback<ContentById>() {
            @Override
            public void finished(ContentById contentById) {
                Log.v(LOG_TAG, "Works: " + contentById.getContent().getTitle());
            }

            @Override
            public void error(RetrofitError retrofitError) {
                Log.v(LOG_TAG, "Error: " + retrofitError.getMessage());
            }
        });
```

## Beacons

To use the integrated beacon support, look at [XamoomBeaconService](https://github.com/xamoom/xamoom-android-sdk/wiki/XamoomBeaconService).

## API Calls

You find every API call in the wiki: [API Calls](https://github.com/xamoom/xamoom-android-sdk/wiki/API-Calls).

### Documentation

Every call is also on our [documentation](http://xamoom.github.io/xamoom-android-sdk/docs/com/xamoom/android/XamoomEndUserApi.html)

# xamoomcontentblocks
[ ![Download](https://api.bintray.com/packages/xamoom/maven/xamoomcontentblocks/images/download.svg) ](https://bintray.com/xamoom/maven/xamoomcontentblocks/_latestVersion)

xamoom has a lot of different contentBlocks. With xamoomcontentblocks you have a easy way to display them.
How to use it is in our [Step by Step Guide](https://github.com/xamoom/xamoom-android-sdk/wiki/Step-by-Step:-New-App-with-xamoom-android-sdk).

# Requirements

* Android 4.3
