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

Add xamoom-sdk dependencie to your Gradle

    compile 'com.xamoom.android:xamoomsdk:3.10.6'
    
## Requirements with Gradle

We are using following techiques
* Gradle version 3.1.3
* Kotlin version 1.2.50
* Google Service Version 3.0.0

        buildscript {
            ext.kotlin_version = '1.2.50'
            repositories {
                jcenter()
                maven {
                    url 'https://maven.google.com/'
                    name 'Google'
                }
                google()
            }
            dependencies {
                classpath 'com.android.tools.build:gradle:3.1.3'
                classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
                classpath 'com.google.gms:google-services:3.0.0'
            }
        }

        allprojects {
            repositories {
                jcenter()
                maven {
                    url 'https://maven.google.com/'
                    name 'Google'
                }
            }
        }
    
XamoomSdk also requires following dependencie (Since Gradle update)

    implementation 'com.android.support:multidex:1.0.3'
    implementation "com.android.support:appcompat-v7:$supportLibraryVersion"
    implementation "com.android.support:support-v4:$supportLibraryVersion"
    implementation "com.android.support:design:$supportLibraryVersion"
    implementation "com.android.support:support-v4:$supportLibraryVersion"
    implementation 'com.android.support.constraint:constraint-layout:1.1.3'
    implementation 'com.jakewharton:butterknife:8.8.1'
    implementation 'com.google.dagger:dagger:2.15'
    implementation 'com.google.dagger:dagger-android:2.15'
    implementation 'com.google.dagger:dagger-android-support:2.15'
    implementation 'com.squareup.picasso:picasso:2.3.2'
    implementation 'com.nineoldandroids:library:2.4.0'
    implementation 'com.daimajia.slider:library:1.1.5@aar'
    implementation 'com.github.bumptech.glide:glide:3.7.0'
    implementation 'org.altbeacon:android-beacon-library:2.15'
    implementation 'com.google.android.gms:play-services-maps:11.8.0'
    implementation 'com.google.android.gms:play-services-location:11.8.0'
    implementation 'com.google.android.exoplayer:exoplayer-core:2.7.0'
    implementation 'com.xamoom.android:morpheus:0.5.2'
    implementation 'com.squareup.okhttp3:logging-interceptor:3.0.1'
    implementation 'com.squareup.retrofit2:retrofit:2.3.0'
    implementation 'com.squareup.retrofit2:converter-scalars:2.1.0'
    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
    implementation 'com.xamoom.android:htmltextview:1.0.9'
    
    annotationProcessor 'com.google.dagger:dagger-compiler:2.15'
    annotationProcessor 'com.google.dagger:dagger-android-processor:2.15'
    annotationProcessor 'com.jakewharton:butterknife-compiler:8.5.1'
    
    // If you are using FCM (Geo Push)
    implementation 'com.google.firebase:firebase-core:11.8.0'
    implementation 'com.google.firebase:firebase-messaging:11.8.0'

# Usage

## Setup Manifest

Add internet permission to your manifest and your google play services with
a geo api key.

    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.VIBRATE"/>

    <meta-data
        android:name="com.google.android.geo.API_KEY"
        android:value="YOUR_API_KEY" />

## Java Implementation

Check out the [java documentation](https://github.com/xamoom/xamoom-android-sdk/wiki/Java-Implementation-Guide)

## Kotlin Implementation

Check out the [kotlin documentation](https://github.com/xamoom/xamoom-android-sdk/wiki/Kotlin-Implementation-Guide)

### Documentation

Here you can find the full java [documentation](https://xamoom.github.io/xamoom-android-sdk/3.1.0/).

# Requirements

* Android 4.3
