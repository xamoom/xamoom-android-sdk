![](https://xamoom.com/wp-inhalte/uploads/2015/02/logo-black-claim1.png)

# xamoom-android-sdk
[ ![Download](https://api.bintray.com/packages/xamoom/maven/xamoom-android-sdk/images/download.svg) ](https://bintray.com/xamoom/maven/xamoom-android-sdk/_latestVersion)

With the xamoom-android-sdk we created a simple SDK to let you create apps based on our system.

More informations about xamoom and how xamoom works? Visit our Github page [xamoom Github Wiki](https://github.com/xamoom/xamoom.github.io/wiki)

# Getting Started

* [Download](/xamoom/xamoom-android-sdk/archive/master.zip) the xamoom-android-sdk and try it
* Read the ["Getting Started"](https://github.com/xamoom/xamoom-android-sdk/wiki#getting-started) guide in the wiki
* Check out the [documentation](http://xamoom.github.io/xamoom-android-sdk/docs/)
* Check out our sample app: ["pingeborg App"](https://github.com/xamoom/xamoom-pingeborg-android)

# Installation

## Installation with Gradle

Add in your Gradle in dependencies

    compile 'com.xamoom.android:xamoom-android-sdk:0.9.6'

# Usage

## Setup XMMEnduserApi

Add internet permission to your manifest.

    <uses-permission android:name="android.permission.INTERNET"/>

```java

```

## Make your first call

Grab a contentId from your [xamoom-system](https://xamoom.net/) (open a page and copy id from url) and make your first call like this:

```java

```

## API Calls


### [contentWithContentId: includeStyle: includeMenu: withLanguage: full: completion: error:](http://xamoom.github.io/xamoom-ios-sdk/docs/html/Classes/XMMEnduserApi.html#//api/name/contentWithContentId:includeStyle:includeMenu:withLanguage:full:completion:error:)

#### Parameters
| parameter | description |
| --- | --- |
| contentId | The id of the content from xamoom backend |
| style | True or False for returning the style from xamoom backend as [XMMResponseStyle](http://xamoom.github.io/xamoom-ios-sdk/docs/html/Classes/XMMResponseStyle.html)
| menu | True or False for returning the menu from xamoom backend as Array of [XMMResponseMenuItem](http://xamoom.github.io/xamoom-ios-sdk/docs/html/Classes/XMMResponseGetSpotMapItem.html)
| language | The requested language of the content from xamoom backend
| full | True or false for returning “unsynced” data or not
| completionHandler	| CompletionHandler returns the result
| errorHandler | ErrorHandler returns an error if one occures

#### Example 

```java
//make your call

```


Every call is also on our [documentation](http://xamoom.github.io/xamoom-android-sdk/docs/)

# xamoomcontentblocks
[ ![Download](https://api.bintray.com/packages/xamoom/maven/xamoomcontentblocks/images/download.svg) ](https://bintray.com/xamoom/maven/xamoomcontentblocks/_latestVersion)

xamoom has a lot of different contentBlocks. With xamoomcontentblocks you have a easy way to display them.
How to use it is in our [Step by Step Guide](https://github.com/xamoom/xamoom-android-sdk/wiki/Step-by-Step:-New-App-with-xamoom-android-sdk).

# Requirements

*
