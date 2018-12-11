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

    compile 'com.xamoom.android:xamoomsdk:3.10.6'

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

## Java Implementation

Check out the [documentation](https://github.com/xamoom/xamoom-android-sdk/wiki/Java-Implementation-Guide)

## Kotlin Implementation

Check out the [documentation](https://github.com/xamoom/xamoom-android-sdk/wiki/Kotlin-Implementation-Guide)

### Documentation

Here you can find the full java [documentation](https://xamoom.github.io/xamoom-android-sdk/3.1.0/).

# Requirements

* Android 4.3
