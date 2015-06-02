package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api;

import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentById;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentByLocation;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentByLocationIdentifier;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentList;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ResponseSpotMap;

/**
 *
 */
public interface XamoomApiListener {
    //
    void gotContentByLocationIdentifier(ContentByLocationIdentifier result);

    //
    void gotContentById(ContentById result);

    //
    void gotContentByLocation(ContentByLocation result);

    //
    void gotSpotMap(ResponseSpotMap result);

    //
    void gotClosestSpots(ResponseSpotMap result);

    //
    void gotContentList(ContentList result);
}
