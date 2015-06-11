package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api;

import android.util.Log;

import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentById;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentByLocationIdentifier;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentList;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.SpotMap;

import junit.framework.TestCase;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by raphaelseher on 03.06.15.
 */
public class
        XamoomEndUserApiTest extends TestCase {

    private static String TESTING_CONTENT_ID = "bc79d8a22a584604b6c9e8d04e4b0834";
    private static String TESTING_MARKER_ID = "dkriw";

    public void testGetInstance() throws Exception {
        assertNotNull("getInstance() should return an object", XamoomEndUserApi.getInstance());
    }

    public void testGetContentById() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);

        XamoomEndUserApi.getInstance().getContentById(TESTING_CONTENT_ID, false, false, "de", new APICallback<ContentById>() {
            @Override
            public void finished(ContentById result) {
                //test
                assertNotNull("getContentById() should return an object", result);
                assertNotNull("getContentById() should return a systemName", result.getSystemName());
                assertNotNull("getContentById() should return a systemUrl", result.getSystemUrl());
                assertNotNull("getContentById() should return a systemId", result.getSystemId());
                assertNull("getContentById() should not return a style", result.getStyle());
                assertNull("getContentById() should not return a menu", result.getMenu());

                signal.countDown();
            }
        });

        signal.await();
    }

    public void testGetContentByIdWithStyleAndMenu() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);

        XamoomEndUserApi.getInstance().getContentById(TESTING_CONTENT_ID, true, true, "de", new APICallback<ContentById>() {
            @Override
            public void finished(ContentById result) {
                assertNotNull("getContentById() should return an object", result);

                //variable test
                assertNotNull("getContentById() should return a systemName", result.getSystemName());
                assertNotNull("getContentById() should return a systemUrl", result.getSystemUrl());
                assertNotNull("getContentById() should return a systemId", result.getSystemId());
                assertNotNull("getContentById() should return a style", result.getStyle());
                assertNotNull("getContentById() should  return a menu", result.getMenu());

                signal.countDown();
            }
        });

        signal.await();
    }

    public void testGetContentbyIdFull() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);

        XamoomEndUserApi.getInstance().getContentbyIdFull(TESTING_CONTENT_ID, false, false, "de", true, new APICallback<ContentById>() {
            @Override
            public void finished(ContentById result) {
                assertNotNull("getContentbyIdFull() should return an object", result);

                //variable test
                assertNotNull("getContentbyIdFull() should return a systemName", result.getSystemName());
                assertNotNull("getContentbyIdFull() should return a systemUrl", result.getSystemUrl());
                assertNotNull("getContentbyIdFull() should return a systemId", result.getSystemId());
                assertNull("getContentbyIdFull() should not return a style", result.getStyle());
                assertNull("getContentbyIdFull() should not return a menu", result.getMenu());

                signal.countDown();
            }
        });

        signal.await();
    }

    public void testGetContentbyIdFullWithStyleAndMenu() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);

        XamoomEndUserApi.getInstance().getContentbyIdFull(TESTING_CONTENT_ID, true, true, "de", true, new APICallback<ContentById>() {
            @Override
            public void finished(ContentById result) {
                assertNotNull("getContentbyIdFull() should return an object", result);

                //variable test
                assertNotNull("getContentbyIdFull() should return a systemName", result.getSystemName());
                assertNotNull("getContentbyIdFull() should return a systemUrl", result.getSystemUrl());
                assertNotNull("getContentbyIdFull() should return a systemId", result.getSystemId());
                assertNotNull("getContentbyIdFull() should return a style", result.getStyle());
                assertNotNull("getContentbyIdFull() should return a menu", result.getMenu());

                signal.countDown();
            }
        });

        signal.await();
    }

    public void testGetContentByLocationIdentifier() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);

        XamoomEndUserApi.getInstance().getContentByLocationIdentifier(TESTING_MARKER_ID, false, false, "de", new APICallback<ContentByLocationIdentifier>() {
            @Override
            public void finished(ContentByLocationIdentifier result) {
                assertNotNull("getContentByLocationIdentifier() should return an object", result);

                //variable test
                assertNotNull("getContentByLocationIdentifier() should return a systemName", result.getSystemName());
                assertNotNull("getContentByLocationIdentifier() should return a systemUrl", result.getSystemUrl());
                assertNotNull("getContentByLocationIdentifier() should return a systemId", result.getSystemId());
                assertNotNull("getContentByLocationIdentifier() should return a hasContent", result.isHasContent());
                assertNotNull("getContentByLocationIdentifier() should return a hasSpot", result.isHasSpot());
                assertNull("getContentByLocationIdentifier() should return a style", result.getStyle());
                assertNull("getContentByLocationIdentifier() should return a menu", result.getMenu());

                signal.countDown();
            }
        });

        signal.await();
    }

    public void testGetSpotMap() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);

        XamoomEndUserApi.getInstance().getSpotMap("0", new String[]{"stw"}, "de", new APICallback<SpotMap>() {
            @Override
            public void finished(SpotMap result) {
                assertNotNull("getSpotMap() should return an object", result);
                if(result.getItems().size() > 0) {
                    assertNotNull("getSpotMap() should return items", result.getItems());
                }
                assertNotNull("getSpotMap() should return a style", result.getStyle());

                signal.countDown();
            }
        });

        signal.await();
    }

    public void testGetClosestSpots() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);

        XamoomEndUserApi.getInstance().getClosestSpots(46.615119, 14.262106, "de", 100, 10, new APICallback<SpotMap>() {
            @Override
            public void finished(SpotMap result) {
                assertNotNull("getClosestSpots() should return an object", result);
                if(result.getItems().size() > 0) {
                    assertNotNull("getSpotMap() should return items", result.getItems());
                }
                assertNotNull("getClosestSpots() should return a limit", result.getLimit());
                assertNotNull("getClosestSpots() should return a radius", result.getRadius());

                signal.countDown();
            }
        });

        signal.await();
    }

    public void testGetContentList() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);

        XamoomEndUserApi.getInstance().getContentList("de", 7, null, new String[]{"artists"}, new APICallback<ContentList>() {
            @Override
            public void finished(ContentList result) {
                assertNotNull("getContentList() should return an object", result);
                if(result.getItems().size() > 0) {
                    assertNotNull("getContentList() should return items", result.getItems());
                }
                assertNotNull("getContentList() should return a cursor", result.getCursor());
                assertNotNull("getContentList() should return an isMore", result.isMore());

                signal.countDown();
            }
        });

        signal.await();
    }
}