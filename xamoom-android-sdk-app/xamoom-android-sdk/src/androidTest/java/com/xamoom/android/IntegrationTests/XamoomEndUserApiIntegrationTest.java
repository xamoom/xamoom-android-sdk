package com.xamoom.android.IntegrationTests;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.xamoom.android.APICallback;
import com.xamoom.android.R;
import com.xamoom.android.XamoomEndUserApi;
import com.xamoom.android.mapping.MenuItem;
import com.xamoom.android.mapping.ContentById;
import com.xamoom.android.mapping.ContentByLocationIdentifier;
import com.xamoom.android.mapping.ContentList;
import com.xamoom.android.mapping.Spot;
import com.xamoom.android.mapping.SpotMap;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import retrofit.RetrofitError;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class XamoomEndUserApiIntegrationTest extends ApplicationTestCase<Application> {
    public XamoomEndUserApiIntegrationTest() {
        super(Application.class);
    }

    private static final String TAG = XamoomEndUserApiIntegrationTest.class.getSimpleName();

    private String API_KEY;
    private static String TESTING_CONTENT_ID;
    private static String TESTING_QR_MARKER_ID;
    private static String TESTING_NFC_MARKER_ID;
    private static String[] TESTING_BEACON;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        API_KEY = this.getSystemContext().getString(R.string.prod_apiKey);
        TESTING_CONTENT_ID = this.getSystemContext().getString(R.string.prod_content_id);
        TESTING_QR_MARKER_ID = this.getSystemContext().getString(R.string.prod_qr_marker_id);
        TESTING_NFC_MARKER_ID = this.getSystemContext().getString(R.string.prod_nfc_marker_id);
        TESTING_BEACON = new String[]{
                this.getSystemContext().getString(R.string.prod_ibeacon_id_1),
                this.getSystemContext().getString(R.string.prod_ibeacon_id_2),
                this.getSystemContext().getString(R.string.prod_ibeacon_id_3)};
    }

    /*
     * getContentByIdFull tests
     */

    /**
     * Check if getContentById returns a systemName, a systemUrl
     * but without style and menu.
     *
     * @throws Exception
     */
    public void testThatGetContentbyIdFullReturnsResult() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);

        XamoomEndUserApi.getInstance(getContext(), API_KEY).getContentbyId(TESTING_CONTENT_ID,
                false, false, "de", true, false, new APICallback<ContentById>() {
                    @Override
                    public void finished(ContentById result) {
                        assertNotNull("getContentbyId() should return an object", result);

                        assertNotNull("getContentbyId() should return a systemName",
                                result.getSystemName());
                        assertNotNull("getContentbyId() should return a systemUrl",
                                result.getSystemUrl());
                        assertNotNull("getContentbyId() should return a systemId",
                                result.getSystemId());
                        assertNull("getContentbyId() should not return a style",
                                result.getStyle());
                        assertNull("getContentbyId() should not return a menu",
                                result.getMenu());

                        signal.countDown();
                    }

                    @Override
                    public void error(RetrofitError error) {
                        assertNull(error);
                        signal.countDown();
                    }
                });

        signal.await();
    }

    /**
     * Check if getContentById returns a systemName, a systemUrl
     * with style and menu.
     *
     * @throws Exception
     */
    public void testThatGetContentbyIdFullReturnsResultWithStyleMenu() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);

        XamoomEndUserApi.getInstance(getContext(), API_KEY).getContentbyId(TESTING_CONTENT_ID,
                true, true, "de", true, false, new APICallback<ContentById>() {
                    @Override
                    public void finished(ContentById result) {
                        assertNotNull("getContentbyId() should return an object", result);

                        assertNotNull("getContentbyId() should return a systemName",
                                result.getSystemName());
                        assertNotNull("getContentbyId() should return a systemUrl",
                                result.getSystemUrl());
                        assertNotNull("getContentbyId() should return a systemId",
                                result.getSystemId());
                        assertNotNull("getContentbyId() should return a style",
                                result.getStyle());
                        assertNotNull("getContentbyId() should return a menu",
                                result.getMenu());

                        signal.countDown();
                    }

                    @Override
                    public void error(RetrofitError error) {
                        assertNull(error);
                        signal.countDown();
                    }
                });

        signal.await();
    }

    /*
     * getContentContentByLocationIdentifier tests
     */

    /*
     * QR
     */

    /**
     * Check if getContentByLocationIdentifier with QR returns a systemName, a systemUrl
     * but without style and menu.
     *
     * @throws Exception
     */
    public void testThatGetContentByLocationIdentifierWithQRReturnsResult() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);

        XamoomEndUserApi.getInstance(getContext(), API_KEY)
                .getContentByLocationIdentifier(TESTING_QR_MARKER_ID, null, false, false, "de",
                        new APICallback<ContentByLocationIdentifier>() {
                            @Override
                            public void finished(ContentByLocationIdentifier result) {
                                assertNotNull("getContentByLocationIdentifier() should return an " +
                                        "object", result);

                                assertNotNull("getContentByLocationIdentifier() should return a " +
                                        "systemName", result.getSystemName());
                                assertNotNull("getContentByLocationIdentifier() should return a " +
                                        "systemUrl", result.getSystemUrl());
                                assertNotNull("getContentByLocationIdentifier() should return a " +
                                        "systemId", result.getSystemId());
                                assertNotNull("getContentByLocationIdentifier() should return a " +
                                        "hasContent", result.isHasContent());
                                assertNotNull("getContentByLocationIdentifier() should return a " +
                                        "hasSpot", result.isHasSpot());
                                assertNull("getContentByLocationIdentifier() should return a " +
                                        "style", result.getStyle());
                                assertNull("getContentByLocationIdentifier() should return a " +
                                        "menu", result.getMenu());

                                signal.countDown();
                            }

                            @Override
                            public void error(RetrofitError error) {
                                assertNull(error);
                                signal.countDown();
                            }
                        });

        signal.await();
    }

    /*
     * NFC
     */

    /**
     * Check if getContentByLocationIdentifier with NFC returns a systemName, a systemUrl
     * but without style and menu.
     *
     * @throws Exception
     */
    public void testThatGetContentByLocationIdentifierWithNFCReturnsResult() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);

        XamoomEndUserApi.getInstance(getContext(), API_KEY)
                .getContentByLocationIdentifier(TESTING_NFC_MARKER_ID, null, false, false, "de",
                        new APICallback<ContentByLocationIdentifier>() {
                            @Override
                            public void finished(ContentByLocationIdentifier result) {
                                assertNotNull("getContentByLocationIdentifier() should return an " +
                                        "object", result);

                                assertNotNull("getContentByLocationIdentifier() should return a " +
                                        "systemName", result.getSystemName());
                                assertNotNull("getContentByLocationIdentifier() should return a " +
                                        "systemUrl", result.getSystemUrl());
                                assertNotNull("getContentByLocationIdentifier() should return a " +
                                        "systemId", result.getSystemId());
                                assertNotNull("getContentByLocationIdentifier() should return a " +
                                        "hasContent", result.isHasContent());
                                assertNotNull("getContentByLocationIdentifier() should return a " +
                                        "hasSpot", result.isHasSpot());
                                assertNull("getContentByLocationIdentifier() should return a " +
                                        "style", result.getStyle());
                                assertNull("getContentByLocationIdentifier() should return a " +
                                        "menu", result.getMenu());

                                signal.countDown();
                            }

                            @Override
                            public void error(RetrofitError error) {
                                assertNull(error);
                                signal.countDown();
                            }
                        });

        signal.await();
    }

    /*
     * Beacon
     */

    /**
     * Check if getContentByLocationIdentifier with Beacon returns a systemName, a systemUrl
     * but without style and menu.
     *
     * @throws Exception
     */
    public void testThatGetContentByLocationIdentifierWithBeaconReturnsResult() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);

        XamoomEndUserApi.getInstance(getContext(), API_KEY).
                getContentByLocationIdentifier(TESTING_BEACON[2], TESTING_BEACON[1], false, false,
                        "de", new APICallback<ContentByLocationIdentifier>() {
                            @Override
                            public void finished(ContentByLocationIdentifier result) {
                                assertNotNull("getContentByLocationIdentifier() should return an" +
                                        " object", result);

                                assertNotNull("getContentByLocationIdentifier() should return a " +
                                        "systemName", result.getSystemName());
                                assertNotNull("getContentByLocationIdentifier() should return a " +
                                        "systemUrl", result.getSystemUrl());
                                assertNotNull("getContentByLocationIdentifier() should return a " +
                                        "systemId", result.getSystemId());
                                assertNotNull("getContentByLocationIdentifier() should return a " +
                                        "hasContent", result.isHasContent());
                                assertNotNull("getContentByLocationIdentifier() should return a " +
                                        "hasSpot", result.isHasSpot());
                                assertNull("getContentByLocationIdentifier() should return a style",
                                        result.getStyle());
                                assertNull("getContentByLocationIdentifier() should return a menu",
                                        result.getMenu());

                                signal.countDown();
                            }

                            @Override
                            public void error(RetrofitError error) {
                                assertNull(error);
                                signal.countDown();
                            }
                        });

        signal.await();
    }

    /*
     * getSpotMap tests
     */

    /**
     * Check if getSpotMap returns a result, items and style.
     *
     * @throws Exception
     */
    public void testThatGetSpotMapReturnsSpots() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);

        XamoomEndUserApi.getInstance(getContext(), API_KEY).getSpotMap(
                new String[]{"allspots"}, "de", false, new APICallback<SpotMap>() {
                    @Override
                    public void finished(SpotMap result) {
                        assertNotNull("getSpotMap() should return an object", result);
                        assertNotNull("getSpotMap() should return items", result.getItems());
                        assertNotNull("getSpotMap() should return a style", result.getStyle());

                        signal.countDown();
                    }

                    @Override
                    public void error(RetrofitError error) {
                        assertNull(error);
                        signal.countDown();
                    }
                });

        signal.await();
    }

    /**
     * Check if getSpotMap returns a result, items and style.
     *
     * @throws Exception
     */
    public void testThatGetSpotMapReturnsSpotsWithContentId() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);

        XamoomEndUserApi.getInstance(getContext(), API_KEY).getSpotMap(
                new String[]{"allspots"}, "de", true, new APICallback<SpotMap>() {
                    @Override
                    public void finished(SpotMap result) {
                        List<Spot> spots = result.getItems();

                        assertNotNull("spot should have contentId", spots.get(0).getContentId());

                        signal.countDown();
                    }

                    @Override
                    public void error(RetrofitError error) {
                        assertNull(error);
                        signal.countDown();
                    }
                });

        signal.await();
    }

    /**
     * Check if getSpotMap returns spot with category.
     *
     * @throws Exception
     */
    public void testThatGetSpotMapReturnsSpotsWithCategory() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);

        XamoomEndUserApi.getInstance(getContext(), API_KEY).getSpotMap(
                new String[]{"spot1"}, "de", true, new APICallback<SpotMap>() {
                    @Override
                    public void finished(SpotMap result) {
                        List<Spot> spots = result.getItems();

                        assertNotNull("spot should have category", spots.get(0).getCategory());
                        signal.countDown();
                    }

                    @Override
                    public void error(RetrofitError error) {
                        assertNull(error);
                        signal.countDown();
                    }
                });

        signal.await();
    }

    /*
     * getClosestSpots tests
     */

    /**
     * Check if getClosestSpots returns a result, items, limit and radius.
     *
     * @throws Exception
     */
    public void testThatGetClosestSpotsReturnsResult() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);

        XamoomEndUserApi.getInstance(getContext(), API_KEY).getClosestSpots(46.615119, 14.262106,
                "de", 100, 10, new APICallback<SpotMap>() {
            @Override
            public void finished(SpotMap result) {
                assertNotNull("getClosestSpots() should return an object", result);
                assertNotNull("getSpotMap() should return items", result.getItems());
                assertNotNull("getClosestSpots() should return a limit", result.getLimit());
                assertNotNull("getClosestSpots() should return a radius", result.getRadius());

                signal.countDown();
            }

            @Override
            public void error(RetrofitError error) {
                assertNull(error);
                signal.countDown();
            }

        });

        signal.await();
    }

    /*
     * menu test
     */

    public void testThatTheMenuGetsIcons() {
        final CountDownLatch signal = new CountDownLatch(1);

        XamoomEndUserApi.getInstance(getContext(), API_KEY).getContentbyId(TESTING_CONTENT_ID,
                false, true, "de", true, false, new APICallback<ContentById>() {
                    @Override
                    public void finished(ContentById result) {

                        assertNotNull(result.getMenu());
                        MenuItem menuItem = result.getMenu().getItems().get(2);
                        assertNotNull(menuItem.getCategory());
                        signal.countDown();
                    }

                    @Override
                    public void error(RetrofitError error) {
                        assertNull(error);
                        signal.countDown();
                    }
                });

        try {
            signal.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*
     * getContentList tests
     */

    /**
     * Check if getContentList returns a result, items, cursor and isMore.
     *
     * @throws Exception
     */
    public void testThatGetContentListReturnsResult() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);

        XamoomEndUserApi.getInstance(getContext(), API_KEY).getContentList("de", 7, null,
                new String[]{"tests"}, new APICallback<ContentList>() {
            @Override
            public void finished(ContentList result) {
                assertNotNull("getContentList() should return an object", result);
                assertNotNull("getContentList() should return items", result.getItems());
                assertNotNull("getContentList() should return a cursor", result.getCursor());
                assertNotNull("getContentList() should return an isMore", result.isMore());

                signal.countDown();
            }

            @Override
            public void error(RetrofitError error) {
                assertNull(error);
                signal.countDown();
            }
        });

        signal.await();
    }

    /**
     * Check if getContentList returns a result if you pass an "umlaut".
     *
     * @throws Exception
     */
    public void testThatGetContentListWithUmlautReturnsResult() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);

        XamoomEndUserApi.getInstance(getContext(), API_KEY).getContentList("de", 7, null,
                new String[]{"Wörthersee"}, new APICallback<ContentList>() {
                    @Override
                    public void finished(ContentList result) {
                        assertNotNull("getContentList() should return an object", result);
                        assertEquals(1, result.getItems().size());
                        assertNotNull("getContentList() should return a cursor",
                                result.getCursor());
                        assertNotNull("getContentList() should return an isMore", result.isMore());

                        signal.countDown();
                    }

                    @Override
                    public void error(RetrofitError error) {
                        assertNull(error);
                        signal.countDown();
                    }
                });

        signal.await();
    }
}