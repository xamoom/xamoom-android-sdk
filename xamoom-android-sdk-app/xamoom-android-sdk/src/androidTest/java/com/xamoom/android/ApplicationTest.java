package com.xamoom.android;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.xamoom.android.mapping.ContentById;
import com.xamoom.android.mapping.ContentByLocationIdentifier;
import com.xamoom.android.mapping.ContentList;
import com.xamoom.android.mapping.SpotMap;

import java.util.concurrent.CountDownLatch;

import retrofit.RetrofitError;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    private String API_KEY;
    private static String TESTING_CONTENT_ID = "a814886d3ef9409682b71b46802031c4";
    private static String TESTING_MARKER_ID = "0ana0";
    private static String[] TESTING_BEACON = new String[]{"de2b94ae-ed98-11e4-3432-78616d6f6f6d","5704","1209"};

    @Override
    public void setUp() throws Exception {
        super.setUp();
        API_KEY = this.getSystemContext().getString(R.string.apiKey);
    }

    public void testGetInstance() throws Exception {
        assertNotNull("getInstance() should return an object", XamoomEndUserApi.getInstance(getContext(), API_KEY));
    }

    public void testGetContentbyIdFull() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);

        XamoomEndUserApi.getInstance(getContext(), API_KEY).getContentbyIdFull(TESTING_CONTENT_ID, false, false, "de", true, new APICallback<ContentById>() {
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

            @Override
            public void error(RetrofitError error) {
                assertTrue(false);
                signal.countDown();
            }
        });

        signal.await();
    }

    public void testGetContentbyIdFullWithStyleAndMenu() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);

        XamoomEndUserApi.getInstance(getContext(), API_KEY).getContentbyIdFull(TESTING_CONTENT_ID, true, true, "de", true, new APICallback<ContentById>() {
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

            @Override
            public void error(RetrofitError error) {
                assertTrue(false);
                signal.countDown();
            }
        });

        signal.await();
    }

    public void testGetContentByLocationIdentifier() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);

        XamoomEndUserApi.getInstance(getContext(), API_KEY).getContentByLocationIdentifier(TESTING_MARKER_ID, null, false, false, "de", new APICallback<ContentByLocationIdentifier>() {
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

            @Override
            public void error(RetrofitError error) {
                assertTrue(false);
                signal.countDown();
            }
        });

        signal.await();
    }

    public void testGetContentByLocationIdentifierBeacon() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);

        XamoomEndUserApi.getInstance(getContext(), API_KEY).getContentByLocationIdentifier(TESTING_BEACON[2], TESTING_BEACON[1], false, false, "de", new APICallback<ContentByLocationIdentifier>() {
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

            @Override
            public void error(RetrofitError error) {
                assertTrue(false);
                signal.countDown();
            }
        });

        signal.await();
    }

    public void testGetSpotMap() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);

        XamoomEndUserApi.getInstance(getContext(), API_KEY).getSpotMap("0", new String[]{"stw"}, "de", new APICallback<SpotMap>() {
            @Override
            public void finished(SpotMap result) {
                assertNotNull("getSpotMap() should return an object", result);
                if(result.getItems().size() > 0) {
                    assertNotNull("getSpotMap() should return items", result.getItems());
                }
                assertNotNull("getSpotMap() should return a style", result.getStyle());

                signal.countDown();
            }

            @Override
            public void error(RetrofitError error) {
                assertTrue(false);
                signal.countDown();
            }
        });

        signal.await();
    }

    public void testGetClosestSpots() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);

        XamoomEndUserApi.getInstance(getContext(), API_KEY).getClosestSpots(46.615119, 14.262106, "de", 100, 10, new APICallback<SpotMap>() {
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

            @Override
            public void error(RetrofitError error) {
                assertTrue(false);
                signal.countDown();
            }

        });

        signal.await();
    }

    public void testGetContentList() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);

        XamoomEndUserApi.getInstance(getContext(), API_KEY).getContentList("de", 7, null, new String[]{"artists"}, new APICallback<ContentList>() {
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

            @Override
            public void error(RetrofitError error) {
                assertTrue(false);
                signal.countDown();
            }
        });

        signal.await();
    }
}