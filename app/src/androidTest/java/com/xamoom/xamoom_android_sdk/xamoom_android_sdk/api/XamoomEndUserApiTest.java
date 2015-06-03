package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api;

import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentById;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentByLocationIdentifier;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentList;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.SpotMap;

import junit.framework.TestCase;

/**
 * Created by raphaelseher on 03.06.15.
 */
public class XamoomEndUserApiTest extends TestCase {

    public void testGetInstance() throws Exception {
        assertNotNull("getInstance() should return an object", XamoomEndUserApi.getInstance());
    }

    public void testGetContentById() throws Exception {
        XamoomEndUserApi.getInstance().getContentById("a3911e54085c427d95e1243844bd6aa3", false, false, "de", new APICallback<ContentById>() {
            @Override
            public void finished(ContentById result) {
                assertNotNull("getContentById() should return an object", result);
            }
        });
    }

    public void testGetContentbyIdFull() throws Exception {
        XamoomEndUserApi.getInstance().getContentbyIdFull("a3911e54085c427d95e1243844bd6aa3", false, false, "de", true, new APICallback<ContentById>() {
            @Override
            public void finished(ContentById result) {
                assertNotNull("getContentbyIdFull() should return an object", result);
            }
        });
    }

    public void testGetContentByLocationIdentifier() throws Exception {
        XamoomEndUserApi.getInstance().getContentByLocationIdentifier("0ana0", false, false, "de", new APICallback<ContentByLocationIdentifier>() {
            @Override
            public void finished(ContentByLocationIdentifier result) {
                assertNotNull("getContentByLocationIdentifier() should return an object", result);
            }
        });
    }

    public void testGetContentByLocation() throws Exception {

    }

    public void testGetSpotMap() throws Exception {
        XamoomEndUserApi.getInstance().getSpotMap("0", new String[]{"stw"}, "de", new APICallback<SpotMap>() {
            @Override
            public void finished(SpotMap result) {
                assertNotNull("getSpotMap() should return an object", result);
            }
        });
    }

    public void testGetClosestSpots() throws Exception {
        XamoomEndUserApi.getInstance().getClosestSpots(46.615119, 14.262106, "de", 100, 10, new APICallback<SpotMap>() {
            @Override
            public void finished(SpotMap result) {
                assertNotNull("getClosestSpots() should return an object", result);
            }
        });
    }

    public void testGetContentList() throws Exception {
        XamoomEndUserApi.getInstance().getContentList("de", 7, null, new String[]{"artists"}, new APICallback<ContentList>() {
            @Override
            public void finished(ContentList result) {
                assertNotNull("getContentList() should return an object", result);
            }
        });
    }
}