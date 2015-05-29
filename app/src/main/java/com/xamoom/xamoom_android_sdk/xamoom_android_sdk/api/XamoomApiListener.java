package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api;

import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentById;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentByLocationIdentifier;

/**
 *
 */
public interface XamoomApiListener {
    //
    void gotContentByLocationIdentifier(ContentByLocationIdentifier result);

    //
    void gotContentById(ContentById result);
}
