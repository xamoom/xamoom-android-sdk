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

    compile 'com.xamoom.android:xamoom-android-sdk:0.9.6'

# Usage

## Setup XMMEnduserApi

Add internet permission to your manifest.

    <uses-permission android:name="android.permission.INTERNET"/>

## Make your first call

Grab a contentId from your [xamoom-system](https://xamoom.net/) (open a page and copy id from url) and make your first call like this:

```java
XamoomEndUserApi.getInstance(this.getApplicationContext(), API_KEY).getContentbyIdFull(mContentId, false, false, null, true, new APICallback<ContentById>() {
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

## API Calls

### [getContentbyIdFull(java.lang.String contentId, boolean style, boolean menu, java.lang.String language, boolean full, APICallback<ContentById> callback)](http://xamoom.github.io/xamoom-android-sdk/docs/com/xamoom/xamoom_android_sdk/xamoom_android_sdk/api/XamoomEndUserApi.html#getContentbyIdFull-java.lang.String-boolean-boolean-java.lang.String-boolean-com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.APICallback-)

#### Parameters
| parameter | description 
| --- | --- |
| contentId | Content id of an xamoom content. 
| style | True for returning xamoom style, else false.
| menu |  True for returning xamoom menu, else false.
| language | The language for the response (if available on xamoom-cloud), else systemLanguage. For german: "de". If null == systemLanguage.
| full | True or false for returning “unsynced” data or not
| APICallback	| [APICallback](http://xamoom.github.io/xamoom-android-sdk/docs/com/xamoom/android/APICallback.html)

#### Response
[ContentById](http://xamoom.github.io/xamoom-android-sdk/docs/com/xamoom/xamoom_android_sdk/xamoom_android_sdk/api/mapping/ContentById.html)

#### Example 

```java
XamoomEndUserApi.getInstance(this.getApplicationContext(), API_KEY).getContentbyIdFull(mContentId, false, false, null, true, new APICallback<ContentById>() {
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

### [getContentByLocationIdentifier(java.lang.String locationIdentifier, boolean style, boolean menu, java.lang.String language, APICallback<ContentByLocationIdentifier> callback](http://xamoom.github.io/xamoom-android-sdk/docs/com/xamoom/android/XamoomEndUserApi.html#getContentByLocationIdentifier-java.lang.String-boolean-boolean-java.lang.String-com.xamoom.android.APICallback-)

#### Parameters
| parameter | description 
| --- | --- |
| locationIdentifier | LocationIdentifier from the scanned QR or NFC.
| style | True for returning xamoom style, else false.
| menu |  True for returning xamoom menu, else false.
| language | The language for the response (if available on xamoom-cloud), else systemLanguage. For german: "de". If null == systemLanguage.
| APICallback	| [APICallback](http://xamoom.github.io/xamoom-android-sdk/docs/com/xamoom/android/APICallback.html)

#### Response
[ContentByLocationIdentifier](http://xamoom.github.io/xamoom-android-sdk/docs/com/xamoom/android/mapping/ContentByLocationIdentifier.html)

#### Example 

```java
XamoomEndUserApi.getInstance(this.getApplicationContext(), API_KEY).getContentByLocationIdentifier(mLocationIdentifier, false, false, null, new APICallback<ContentByLocationIdentifier>() {
            @Override
            public void finished(ContentByLocationIdentifier contentByLocationIdentifier) {
                Log.v(LOG_TAG, "Works: " + contentByLocationIdentifier.getContent().getTitle());
            }

            @Override
            public void error(RetrofitError retrofitError) {
                Log.v(LOG_TAG, "Error: " + retrofitError.getMessage());
            }
        });
```

### [getContentByLocation(double lat, double lon, java.lang.String language, APICallback<ContentByLocation> callback)](http://xamoom.github.io/xamoom-android-sdk/docs/com/xamoom/android/XamoomEndUserApi.html#getContentByLocation-double-double-java.lang.String-com.xamoom.android.APICallback-)

#### Parameters
| parameter | description 
| --- | --- |
| lat | The latitude of a location (for example user location). 
| lon | The longitude of a location (for example user location). 
| language | The language for the response (if available on xamoom-cloud), else systemLanguage. For german: "de". If null == systemLanguage.
| APICallback	| [APICallback](http://xamoom.github.io/xamoom-android-sdk/docs/com/xamoom/android/APICallback.html)

#### Response
[ContentByLocation](http://xamoom.github.io/xamoom-android-sdk/docs/com/xamoom/android/mapping/ContentByLocation.html)

#### Example 

```java
XamoomEndUserApi.getInstance(this.getApplicationContext(), API_KEY).getContentByLocation(46.615115, 14.262196, null, new APICallback<ContentByLocation>() {
            @Override
            public void finished(ContentByLocation contentByLocation) {
                Log.v(LOG_TAG, "Works: " + contentByLocation.getItems().size());
            }

            @Override
            public void error(RetrofitError retrofitError) {
                Log.v(LOG_TAG, "Error: " + retrofitError.getMessage());
            }
        });
```

### [getSpotMap(java.lang.String systemId, java.lang.String[] mapTags, java.lang.String language, APICallback<SpotMap> callback)](http://xamoom.github.io/xamoom-android-sdk/docs/com/xamoom/android/XamoomEndUserApi.html#getSpotMap-java.lang.String-java.lang.String:A-java.lang.String-com.xamoom.android.APICallback-)

#### Parameters
| parameter | description 
| --- | --- |
| systemId | When calling with an API Key, can be 0.
| mapTags | StringArray with mapTags you want to display.
| language | The language for the response (if available on xamoom-cloud), else systemLanguage. For german: "de". If null == systemLanguage.
| APICallback	| [APICallback](http://xamoom.github.io/xamoom-android-sdk/docs/com/xamoom/android/APICallback.html)

#### Response
[SpotMap](http://xamoom.github.io/xamoom-android-sdk/docs/com/xamoom/android/mapping/SpotMap.html)

#### Example 

```java
XamoomEndUserApi.getInstance(this.getApplicationContext(), API_KEY).getSpotMap(null, new String[]{"stw"}, null, new APICallback<SpotMap>() {
            @Override
            public void finished(SpotMap spotMap) {
                Log.v(LOG_TAG, "Works: " + spotMap.getItems().size());
            }

            @Override
            public void error(RetrofitError retrofitError) {
                Log.v(LOG_TAG, "Error: " + retrofitError.getMessage());
            }
        });
```

### [getContentList(java.lang.String language, int pageSize, java.lang.String cursor, java.lang.String[] tags, APICallback<ContentList> callback)](http://xamoom.github.io/xamoom-android-sdk/docs/com/xamoom/android/XamoomEndUserApi.html#getContentList-java.lang.String-int-java.lang.String-java.lang.String:A-com.xamoom.android.APICallback-)

#### Parameters
| parameter | description 
| --- | --- |
| language | The language for the response (if available on xamoom-cloud), else systemLanguage. For german: "de". If null == systemLanguage.
| pageSize | Number of returned items.
| cursor | Send for paging, else null.
| tags | Tags to filter.
| APICallback	| [APICallback](http://xamoom.github.io/xamoom-android-sdk/docs/com/xamoom/android/APICallback.html)

#### Response
[ContentList](http://xamoom.github.io/xamoom-android-sdk/docs/com/xamoom/android/mapping/ContentList.html)

#### Example 

```java
XamoomEndUserApi.getInstance(this.getApplicationContext(), API_KEY).getContentList(null, 10, null, new String[]{"artists"}, new APICallback<ContentList>() {
            @Override
            public void finished(ContentList contentList) {
                Log.v(LOG_TAG, "Works: " + contentList.getItems().size());
            }

            @Override
            public void error(RetrofitError retrofitError) {
                Log.v(LOG_TAG, "Error: " + retrofitError.getMessage());
            }
        });
```

### [getClosestSpots](http://xamoom.github.io/xamoom-android-sdk/docs/com/xamoom/android/XamoomEndUserApi.html#getClosestSpots-double-double-java.lang.String-int-int-com.xamoom.android.APICallback-)

#### Parameters
| parameter | description 
| --- | --- |
| lat | The latitude of a location (for example user location).
| lon | The longitude of a location (for example user location).
| language | The language for the response (if available on xamoom-cloud), else systemLanguage. For german: "de". If null == systemLanguage.
| radius | A search radius in meter
| limit | A limit for results
| APICallback	| [APICallback](http://xamoom.github.io/xamoom-android-sdk/docs/com/xamoom/android/APICallback.html)

#### Response
[SpotMap](http://xamoom.github.io/xamoom-android-sdk/docs/com/xamoom/android/mapping/SpotMap.html)

#### Example 

```java
XamoomEndUserApi.getInstance(this.getApplicationContext(), API_KEY).getClosestSpots(46.615115, 14.262196, null, 1000, 100, new APICallback<SpotMap>() {
            @Override
            public void finished(SpotMap spotMap) {
                Log.v(LOG_TAG, "Works: " + spotMap.getItems().size());
            }

            @Override
            public void error(RetrofitError retrofitError) {
                Log.v(LOG_TAG, "Error: " + retrofitError.getMessage());
            }
        });
```

### [queueGeofenceAnalytics(java.lang.String requestedLanguage, java.lang.String deliveredLanguage, java.lang.String systemId, java.lang.String systemName, java.lang.String contentId, java.lang.String contentName, java.lang.String spotId, java.lang.String spotName)](http://xamoom.github.io/xamoom-android-sdk/docs/com/xamoom/android/XamoomEndUserApi.html#queueGeofenceAnalytics-java.lang.String-java.lang.String-java.lang.String-java.lang.String-java.lang.String-java.lang.String-java.lang.String-java.lang.String-)

Geofence analytics, call when displaying a getContentByLocation().

#### Parameters
| parameter | description 
| --- | --- |
| requestedLanguage | The language you requested.
| deliveredLanguage | The returned language.
| systemId | The returned systemId.
| systemName | The returned systemName.
| contentId | The returned contentId.
| contentName | The returned contentName.
| spotId | The returned spotId.
| spotName | The returned spotName.

#### Example 

```java
XamoomEndUserApi.getInstance(this.getApplicationContext(), API_KEY).queueGeofenceAnalytics(systemLanguage, 
                savedResponse.getLanguage(), savedResponse.getSystemId(), savedResponse.getSystemName(), 
                savedResponse.getContentId(), savedResponse.getTitle(), savedResponse.getSpotId(), savedResponse.getSpotName());

```

### Documentation

Every call is also on our [documentation](http://xamoom.github.io/xamoom-android-sdk/docs/com/xamoom/android/XamoomEndUserApi.html)

# xamoomcontentblocks
[ ![Download](https://api.bintray.com/packages/xamoom/maven/xamoomcontentblocks/images/download.svg) ](https://bintray.com/xamoom/maven/xamoomcontentblocks/_latestVersion)

xamoom has a lot of different contentBlocks. With xamoomcontentblocks you have a easy way to display them.
How to use it is in our [Step by Step Guide](https://github.com/xamoom/xamoom-android-sdk/wiki/Step-by-Step:-New-App-with-xamoom-android-sdk).

# Requirements

*
