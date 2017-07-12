/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.xamoomsdk.xamoomsdk;

import android.location.Location;

import com.xamoom.android.xamoomsdk.APICallback;
import com.xamoom.android.xamoomsdk.APIListCallback;
import com.xamoom.android.xamoomsdk.EnduserApi;
import com.xamoom.android.xamoomsdk.Enums.ContentFlags;
import com.xamoom.android.xamoomsdk.Enums.SpotFlags;
import com.xamoom.android.xamoomsdk.HTTPHeaderInterceptor;
import com.xamoom.android.xamoomsdk.Offline.OfflineEnduserApi;
import com.xamoom.android.xamoomsdk.Resource.Content;
import com.xamoom.android.xamoomsdk.Resource.Menu;
import com.xamoom.android.xamoomsdk.Resource.Spot;
import com.xamoom.android.xamoomsdk.Resource.Style;
import com.xamoom.android.xamoomsdk.Resource.System;
import com.xamoom.android.xamoomsdk.Resource.SystemSetting;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Semaphore;

import at.rags.morpheus.Error;
import at.rags.morpheus.JsonApiObject;
import at.rags.morpheus.Morpheus;
import at.rags.morpheus.Resource;
import dalvik.annotation.TestTarget;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Call;
import retrofit2.Retrofit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EnduserApiTests {
  private static final String TAG = EnduserApiTests.class.getSimpleName();

  private MockWebServer mMockWebServer;
  private EnduserApi mEnduserApi;
  private Morpheus mMockMorpheus;
  private OfflineEnduserApi mMockedOfflineEnduserApi;

  @Captor ArgumentCaptor<Map<String, String>> mMapArgumentCaptor;

  @Before
  public void setup() {
    mMockWebServer = new MockWebServer();

    OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
    builder.addInterceptor(new HTTPHeaderInterceptor("UserAgent", "apikey"));
    OkHttpClient httpClient = builder.build();

    Retrofit retrofit = new Retrofit.Builder()
        .client(httpClient)
        .baseUrl(mMockWebServer.url(""))
        .build();
    mEnduserApi = new EnduserApi(retrofit, null);

    mMockMorpheus = mock(Morpheus.class);
    mMockedOfflineEnduserApi = mock(OfflineEnduserApi.class);
    mEnduserApi.getCallHandler().setMorpheus(mMockMorpheus);
    mEnduserApi.setOfflineEnduserApi(mMockedOfflineEnduserApi);
  }

  @After
  public void teardown() {
    try {
      mMockWebServer.shutdown();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testInitWithApikey() throws Exception {
    EnduserApi enduserApi = new EnduserApi("test", null);

    assertNotNull(enduserApi.getEnduserApiInterface());
    assertEquals(enduserApi.getSystemLanguage(), "en");
  }

  @Test
  public void testInitWithRestAdapter() throws Exception {
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl("http://www.xamoom.com/")
        .build();

    EnduserApi enduserApi = new EnduserApi(retrofit, null);

    assertNotNull(enduserApi.getEnduserApiInterface());
    assertEquals(enduserApi.getSystemLanguage(), "en");
  }

  @Test
  public void testSharedInstanceApiKey() {
    EnduserApi api = EnduserApi.getSharedInstance("key", null);

    assertNotNull(api);
  }

  @Test
  public void testSetSharedInstance() {
    EnduserApi enduserApi = new EnduserApi("test", null);

    EnduserApi.setSharedInstance(enduserApi);
    EnduserApi checkEnduserApi = EnduserApi.getSharedInstance();

    assertEquals(enduserApi, checkEnduserApi);
  }

  @Test(expected = NullPointerException.class)
  public void testSharedInstanceNull() {
    EnduserApi.setSharedInstance(null);
    EnduserApi enduserApi = EnduserApi.getSharedInstance();
  }

  @Test(expected = NullPointerException.class)
  public void testSharedInstanceWithNullApikey() {
    EnduserApi enduserApi = EnduserApi.getSharedInstance(null, null);
  }

  @Test
  public void testGetContentWithIDSuccess() throws Exception {
    mMockWebServer.enqueue(new MockResponse().setBody(""));

    final Content[] checkContent = {null};

    Content content = new Content();
    content.setTitle("Test");

    JsonApiObject jsonApiObject = new JsonApiObject();
    jsonApiObject.setResource(content);

    when(mMockMorpheus.parse(anyString())).thenReturn(jsonApiObject);

    final Semaphore semaphore = new Semaphore(0);

    Call call = mEnduserApi.getContent("123456", new APICallback<Content, List<Error>>() {
      @Override
      public void finished(Content result) {
        checkContent[0] = result;
        semaphore.release();
      }

      @Override
      public void error(List<Error> error) {
        semaphore.release();
      }
    });

    semaphore.acquire();

    assertNotNull(call);
    assertTrue(checkContent[0].getTitle().equals("Test"));
    RecordedRequest request1 = mMockWebServer.takeRequest();
    assertEquals("/_api/v2/consumer/contents/123456?lang=en", request1.getPath());

    // TEST User-Agent
    assertEquals(request1.getHeader("User-Agent"), "UserAgent");
  }

  @Test
  public void testGetContentWithIDError() throws Exception {
    mMockWebServer.enqueue(new MockResponse().setBody(""));

    final Error[] checkError = {null};

    Error error = new Error();
    error.setTitle("Fail");
    ArrayList<Error> errors = new ArrayList<>();
    errors.add(error);

    JsonApiObject jsonApiObject = new JsonApiObject();
    jsonApiObject.setErrors(errors);

    when(mMockMorpheus.parse(anyString())).thenReturn(jsonApiObject);

    final Semaphore semaphore = new Semaphore(0);

    mEnduserApi.getContent("123456", new APICallback<Content, List<at.rags.morpheus.Error>>() {
      @Override
      public void finished(Content result) {
        semaphore.release();
      }

      @Override
      public void error(List<Error> error) {
        checkError[0] = error.get(0);
        semaphore.release();
      }
    });

    semaphore.acquire();

    assertTrue(checkError[0].getTitle().equals("Fail"));
    RecordedRequest request1 = mMockWebServer.takeRequest();
    assertEquals("/_api/v2/consumer/contents/123456?lang=en", request1.getPath());
  }

  @Test
  public void testGetContentWithIDParam() throws Exception {
    mMockWebServer.enqueue(new MockResponse().setBody(""));
    JsonApiObject jsonApiObject = new JsonApiObject();
    when(mMockMorpheus.parse(anyString())).thenReturn(jsonApiObject);

    mEnduserApi.getContent("123456", EnumSet.of(ContentFlags.PREVIEW, ContentFlags.PRIVATE),
        new APICallback<Content, List<at.rags.morpheus.Error>>() {
          @Override
          public void finished(Content result) {
          }

          @Override
          public void error(List<Error> error) {
          }
        });

    RecordedRequest request1 = mMockWebServer.takeRequest();
    assertEquals("/_api/v2/consumer/contents/123456?lang=en&preview=true&public-only=true", request1.getPath());
  }

  @Test
  public void testGetContentWhenOffline() {
    mEnduserApi.setOffline(true);

    mEnduserApi.getContent("1", null);

    Mockito.verify(mMockedOfflineEnduserApi).getContent(eq("1"), Matchers.<APICallback<Content, List<Error>>>any());
  }

  @Test
  public void testGetContentWithLocationIdentifierSuccess() throws Exception {
    mMockWebServer.enqueue(new MockResponse().setBody(""));

    final Content[] checkContent = {null};

    Content content = new Content();
    content.setTitle("Test");

    JsonApiObject jsonApiObject = new JsonApiObject();
    jsonApiObject.setResource(content);

    when(mMockMorpheus.parse(anyString())).thenReturn(jsonApiObject);

    final Semaphore semaphore = new Semaphore(0);

    mEnduserApi.getContentByLocationIdentifier("1234", new APICallback<Content, List<Error>>() {
      @Override
      public void finished(Content result) {
        checkContent[0] = result;
        semaphore.release();
      }

      @Override
      public void error(List<Error> error) {
        semaphore.release();
      }
    });

    semaphore.acquire();

    assertTrue(checkContent[0].getTitle().equals("Test"));
    RecordedRequest request1 = mMockWebServer.takeRequest();
    assertEquals("/_api/v2/consumer/contents?lang=en&filter[location-identifier]=1234", request1.getPath());
  }

  @Test
  public void testGetContentWithLocationIdentifierAndConditionSuccess() throws Exception {
    mMockWebServer.enqueue(new MockResponse().setBody(""));

    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.getDefault());
    Date date = df.parse("2017-07-10T13:18:49+02:00");

    HashMap<String, Object> conditions = new HashMap<>(3);
    conditions.put("name", "myname");
    conditions.put("number", 5);
    conditions.put("float", 2.0f);
    conditions.put("double", 1.000002358923523523523523535);
    conditions.put("date", date);

    final Content[] checkContent = {null};

    Content content = new Content();
    content.setTitle("Test");

    JsonApiObject jsonApiObject = new JsonApiObject();
    jsonApiObject.setResource(content);

    when(mMockMorpheus.parse(anyString())).thenReturn(jsonApiObject);

    final Semaphore semaphore = new Semaphore(0);

    mEnduserApi.getContentByLocationIdentifier("1234", null, conditions,
        new APICallback<Content, List<Error>>() {
          @Override
          public void finished(Content result) {
            checkContent[0] = result;
            semaphore.release();
          }

          @Override
          public void error(List<Error> error) {
            semaphore.release();
          }
        });
    semaphore.acquire();

    assertTrue(checkContent[0].getTitle().equals("Test"));
    RecordedRequest request1 = mMockWebServer.takeRequest();
    assertEquals("/_api/v2/consumer/contents?lang=en&filter[location-identifier]=1234" +
            "&condition[name]=myname" +
            "&condition[date]=2017-07-10T11:18:49Z" +
            "&condition[number]=5" +
            "&condition[float]=2.0" +
            "&condition[double]=1.0000023589235236",
        request1.getPath());
  }

  @Test
  public void testGetContentWithLocationIdentifierFlagsSuccess() throws Exception {
    mMockWebServer.enqueue(new MockResponse().setBody(""));

    final Content[] checkContent = {null};

    Content content = new Content();
    content.setTitle("Test");

    JsonApiObject jsonApiObject = new JsonApiObject();
    jsonApiObject.setResource(content);

    when(mMockMorpheus.parse(anyString())).thenReturn(jsonApiObject);

    final Semaphore semaphore = new Semaphore(0);

    mEnduserApi.getContentByLocationIdentifier("1234", EnumSet.of(ContentFlags.PREVIEW,
        ContentFlags.PRIVATE), new APICallback<Content, List<Error>>() {
      @Override
      public void finished(Content result) {
        checkContent[0] = result;
        semaphore.release();
      }

      @Override
      public void error(List<Error> error) {
        semaphore.release();
      }
    });

    semaphore.acquire();

    assertTrue(checkContent[0].getTitle().equals("Test"));
    RecordedRequest request1 = mMockWebServer.takeRequest();
    assertEquals("/_api/v2/consumer/contents?lang=en&filter[location-identifier]=1234&preview=true&public-only=true",
        request1.getPath());
  }

  @Test
  public void testGetContentWithLocationIdentifierWhenOffline() {
    mEnduserApi.setOffline(true);

    mEnduserApi.getContentByLocationIdentifier("test", null, null);

    Mockito.verify(mMockedOfflineEnduserApi).getContentByLocationIdentifier(eq("test"),
        (APICallback<Content, List<Error>>) any());

    Mockito.verifyNoMoreInteractions(mMockMorpheus);
  }

  @Test
  public void testGetContentWithBeaconSuccess() throws Exception {
    mMockWebServer.enqueue(new MockResponse().setBody(""));

    final Content[] checkContent = {null};

    Content content = new Content();
    content.setTitle("Test");

    JsonApiObject jsonApiObject = new JsonApiObject();
    jsonApiObject.setResource(content);

    when(mMockMorpheus.parse(anyString())).thenReturn(jsonApiObject);

    final Semaphore semaphore = new Semaphore(0);

    mEnduserApi.getContentByBeacon(1, 2, new APICallback<Content, List<Error>>() {
      @Override
      public void finished(Content result) {
        checkContent[0] = result;
        semaphore.release();
      }

      @Override
      public void error(List<Error> error) {
        semaphore.release();
      }
    });

    semaphore.acquire();

    assertTrue(checkContent[0].getTitle().equals("Test"));
    RecordedRequest request1 = mMockWebServer.takeRequest();
    assertEquals("/_api/v2/consumer/contents?lang=en&filter[location-identifier]=1|2", request1.getPath());
  }

  @Test
  public void testGetContentWithBeaconContentFlagsSuccess() throws Exception {
    mMockWebServer.enqueue(new MockResponse().setBody(""));

    final Content[] checkContent = {null};

    Content content = new Content();
    content.setTitle("Test");

    JsonApiObject jsonApiObject = new JsonApiObject();
    jsonApiObject.setResource(content);

    when(mMockMorpheus.parse(anyString())).thenReturn(jsonApiObject);

    final Semaphore semaphore = new Semaphore(0);

    mEnduserApi.getContentByBeacon(1, 2, EnumSet.of(ContentFlags.PREVIEW, ContentFlags.PRIVATE),
        new APICallback<Content, List<Error>>() {
          @Override
          public void finished(Content result) {
            checkContent[0] = result;
            semaphore.release();
          }

          @Override
          public void error(List<Error> error) {
            semaphore.release();
          }
        });

    semaphore.acquire();

    assertTrue(checkContent[0].getTitle().equals("Test"));
    RecordedRequest request1 = mMockWebServer.takeRequest();
    assertEquals("/_api/v2/consumer/contents?lang=en&filter[location-identifier]=1|2&preview=true&public-only=true",
        request1.getPath());
  }

  @Test
  public void testGetContentWithBeaconAndConditionSuccess() throws Exception {
    mMockWebServer.enqueue(new MockResponse().setBody(""));

    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.getDefault());
    Date date = df.parse("2017-07-10T13:18:49+02:00");

    HashMap<String, Object> conditions = new HashMap<>(3);
    conditions.put("name", "myname");
    conditions.put("number", 5);
    conditions.put("float", 2.0f);
    conditions.put("double", 1.000002358923523523523523535);
    conditions.put("date", date);

    final Content[] checkContent = {null};

    Content content = new Content();
    content.setTitle("Test");

    JsonApiObject jsonApiObject = new JsonApiObject();
    jsonApiObject.setResource(content);

    when(mMockMorpheus.parse(anyString())).thenReturn(jsonApiObject);

    final Semaphore semaphore = new Semaphore(0);

    mEnduserApi.getContentByBeacon(1, 2, null, conditions,
        new APICallback<Content, List<Error>>() {
          @Override
          public void finished(Content result) {
            checkContent[0] = result;
            semaphore.release();
          }

          @Override
          public void error(List<Error> error) {
            semaphore.release();
          }
        });
    semaphore.acquire();

    assertTrue(checkContent[0].getTitle().equals("Test"));
    RecordedRequest request1 = mMockWebServer.takeRequest();
    assertEquals("/_api/v2/consumer/contents?lang=en&filter[location-identifier]=1|2" +
            "&condition[name]=myname" +
            "&condition[date]=2017-07-10T11:18:49Z" +
            "&condition[number]=5" +
            "&condition[float]=2.0" +
            "&condition[double]=1.0000023589235236",
        request1.getPath());
  }

  @Test
  public void testGetContentsWithLocationSuccess() throws Exception {
    mMockWebServer.enqueue(new MockResponse().setBody(""));

    final List<Content> checkContents = new ArrayList<>();

    Content content = new Content();
    content.setTitle("Test");
    ArrayList<Resource> contents = new ArrayList<Resource>();
    contents.add(content);

    HashMap<String, Object> meta = new HashMap<>();
    meta.put("cursor", "1");
    meta.put("has-more", true);

    JsonApiObject jsonApiObject = new JsonApiObject();
    jsonApiObject.setResources(contents);
    jsonApiObject.setMeta(meta);

    Location location = mock(Location.class);
    when(location.getLatitude()).thenReturn(1.0);
    when(location.getLongitude()).thenReturn(2.0);

    when(mMockMorpheus.parse(anyString())).thenReturn(jsonApiObject);

    final Semaphore semaphore = new Semaphore(0);

    mEnduserApi.getContentsByLocation(location, 10, null, null, new APIListCallback<List<Content>, List<Error>>() {
      @Override
      public void finished(List<Content> result, String cursor, boolean hasMore) {
        checkContents.addAll(result);
        semaphore.release();
      }

      @Override
      public void error(List<Error> error) {
        semaphore.release();
      }
    });

    semaphore.acquire();

    assertTrue(checkContents.get(0).getTitle().equals("Test"));
    RecordedRequest request1 = mMockWebServer.takeRequest();
    assertEquals("/_api/v2/consumer/contents?lang=en&page[size]=10&filter[lat]=1.0&filter[lon]=2.0", request1.getPath());
  }

  @Test
  public void testGetContentyByLocationWhenOffline() {
    mEnduserApi.setOffline(true);

    mEnduserApi.getContentsByLocation(null, 10, null, null, null);

    Mockito.verify(mMockedOfflineEnduserApi).getContentsByLocation(any(Location.class),
        anyInt(), anyString(), any(EnumSet.class),
        (APIListCallback<List<Content>, List<Error>>) any());

    Mockito.verifyNoMoreInteractions(mMockMorpheus);
  }

  @Test
  public void testGetContentsWithTagsSuccess() throws Exception {
    mMockWebServer.enqueue(new MockResponse().setBody(""));

    final List<Content> checkContents = new ArrayList<>();

    Content content = new Content();
    content.setTitle("Test");
    ArrayList<Resource> contents = new ArrayList<Resource>();
    contents.add(content);

    HashMap<String, Object> meta = new HashMap<>();
    meta.put("cursor", "1");
    meta.put("has-more", true);

    JsonApiObject jsonApiObject = new JsonApiObject();
    jsonApiObject.setResources(contents);
    jsonApiObject.setMeta(meta);

    List<String> tags = new ArrayList<>();
    tags.add("tag1");
    tags.add("tag2");

    when(mMockMorpheus.parse(anyString())).thenReturn(jsonApiObject);

    final Semaphore semaphore = new Semaphore(0);

    mEnduserApi.getContentsByTags(tags, 10, null, null, new APIListCallback<List<Content>, List<Error>>() {
      @Override
      public void finished(List<Content> result, String cursor, boolean hasMore) {
        checkContents.addAll(result);
        semaphore.release();
      }

      @Override
      public void error(List<Error> error) {
        semaphore.release();
      }
    });

    semaphore.acquire();

    assertTrue(checkContents.get(0).getTitle().equals("Test"));
    RecordedRequest request1 = mMockWebServer.takeRequest();
    assertEquals("/_api/v2/consumer/contents?lang=en&page[size]=10&filter[tags]=[%27tag1%27,%27tag2%27]", request1.getPath());
  }

  @Test
  public void testGetContentsWithTagsWhenOffline() {
    mEnduserApi.setOffline(true);

    mEnduserApi.getContentsByTags(null, 10, null, null, null);

    Mockito.verify(mMockedOfflineEnduserApi).getContentsByTags(any(List.class),
        anyInt(), anyString(), any(EnumSet.class),
        (APIListCallback<List<Content>, List<Error>>) any());

    Mockito.verifyNoMoreInteractions(mMockMorpheus);
  }

  @Test
  public void testSearchContent() throws Exception {
    mMockWebServer.enqueue(new MockResponse().setBody(""));

    final List<Content> checkContents = new ArrayList<>();

    Content content = new Content();
    content.setTitle("Test");
    ArrayList<Resource> contents = new ArrayList<Resource>();
    contents.add(content);

    HashMap<String, Object> meta = new HashMap<>();
    meta.put("cursor", "1");
    meta.put("has-more", true);

    JsonApiObject jsonApiObject = new JsonApiObject();
    jsonApiObject.setResources(contents);
    jsonApiObject.setMeta(meta);

    List<String> tags = new ArrayList<>();
    tags.add("tag1");
    tags.add("tag2");

    when(mMockMorpheus.parse(anyString())).thenReturn(jsonApiObject);

    final Semaphore semaphore = new Semaphore(0);

    mEnduserApi.searchContentsByName("do not touch", 10, null, null, new APIListCallback<List<Content>, List<Error>>() {
      @Override
      public void finished(List<Content> result, String cursor, boolean hasMore) {
        checkContents.addAll(result);
        semaphore.release();
      }

      @Override
      public void error(List<Error> error) {
        semaphore.release();
      }
    });

    semaphore.acquire();

    assertTrue(checkContents.get(0).getTitle().equals("Test"));
    RecordedRequest request1 = mMockWebServer.takeRequest();
    assertEquals("/_api/v2/consumer/contents?lang=en&page[size]=10&filter[name]=do%20not%20touch", request1.getPath());
  }

  @Test
  public void testSearchContentByName() {
    mEnduserApi.setOffline(true);

    mEnduserApi.searchContentsByName("name", 10, null, null, null);

    Mockito.verify(mMockedOfflineEnduserApi).searchContentsByName(anyString(), anyInt(),
        anyString(), any(EnumSet.class),
        (APIListCallback<List<Content>, List<Error>>) any());

    Mockito.verifyNoMoreInteractions(mMockMorpheus);
  }

  @Test
  public void testGetSpot() throws Exception {
    mMockWebServer.enqueue(new MockResponse().setBody(""));
    final Spot[] checkSpot = {null};

    Spot spot = new Spot();
    spot.setName("Test Spot");

    JsonApiObject jsonApiObject = new JsonApiObject();
    jsonApiObject.setResource(spot);

    when(mMockMorpheus.parse(anyString())).thenReturn(jsonApiObject);

    final Semaphore semaphore = new Semaphore(0);

    mEnduserApi.getSpot("1234", new APICallback<Spot, List<Error>>() {
      @Override
      public void finished(Spot result) {
        checkSpot[0] = result;
        semaphore.release();
      }

      @Override
      public void error(List<Error> error) {
        semaphore.release();
      }
    });

    semaphore.acquire();

    assertTrue(checkSpot[0].getName().equals("Test Spot"));
    RecordedRequest request1 = mMockWebServer.takeRequest();
    assertEquals("/_api/v2/consumer/spots/1234?lang=en",
        request1.getPath());
  }

  @Test
  public void testGetSpotFlags() throws Exception {
    mMockWebServer.enqueue(new MockResponse().setBody(""));
    final Spot[] checkSpot = {null};

    Spot spot = new Spot();
    spot.setName("Test Spot");

    JsonApiObject jsonApiObject = new JsonApiObject();
    jsonApiObject.setResource(spot);

    when(mMockMorpheus.parse(anyString())).thenReturn(jsonApiObject);

    final Semaphore semaphore = new Semaphore(0);

    mEnduserApi.getSpot("1234", EnumSet.of(SpotFlags.INCLUDE_CONTENT, SpotFlags.INCLUDE_MARKERS, SpotFlags.HAS_LOCATION),
        new APICallback<Spot, List<Error>>() {
          @Override
          public void finished(Spot result) {
            checkSpot[0] = result;
            semaphore.release();
          }

          @Override
          public void error(List<Error> error) {
            semaphore.release();
          }
        });

    semaphore.acquire();

    assertTrue(checkSpot[0].getName().equals("Test Spot"));
    RecordedRequest request1 = mMockWebServer.takeRequest();
    assertEquals("/_api/v2/consumer/spots/1234?lang=en&include_content=true&include_markers=true&filter[has-location]=true",
        request1.getPath());
  }

  @Test
  public void testGetSpotWhenOffline() {
    mEnduserApi.setOffline(true);

    mEnduserApi.getSpot("test", null);

    Mockito.verify(mMockedOfflineEnduserApi).getSpot(anyString(),
        (APICallback<Spot, List<Error>>) any());
    Mockito.verifyNoMoreInteractions(mMockMorpheus);
  }

  @Test
  public void testGetSpotsWithLocationSuccess() throws Exception {
    mMockWebServer.enqueue(new MockResponse().setBody(""));

    final List<Spot> checkSpots = new ArrayList<>();

    Spot spot = new Spot();
    spot.setName("Test");
    ArrayList<Resource> spots = new ArrayList<>();
    spots.add(spot);

    HashMap<String, Object> meta = new HashMap<>();
    meta.put("cursor", "1");
    meta.put("has-more", true);

    JsonApiObject jsonApiObject = new JsonApiObject();
    jsonApiObject.setResources(spots);
    jsonApiObject.setMeta(meta);

    Location location = mock(Location.class);
    when(location.getLatitude()).thenReturn(1.0);
    when(location.getLongitude()).thenReturn(2.0);

    when(mMockMorpheus.parse(anyString())).thenReturn(jsonApiObject);

    final Semaphore semaphore = new Semaphore(0);

    mEnduserApi.getSpotsByLocation(location, 100, null, null, new APIListCallback<List<Spot>, List<Error>>() {
      @Override
      public void finished(List<Spot> result, String cursor, boolean hasMore) {
        checkSpots.add(result.get(0));
        semaphore.release();
      }

      @Override
      public void error(List<Error> error) {
        semaphore.release();
      }
    });

    semaphore.acquire();

    assertTrue(checkSpots.get(0).getName().equals("Test"));
    RecordedRequest request1 = mMockWebServer.takeRequest();
    assertEquals("/_api/v2/consumer/spots?lang=en&filter[lat]=1.0&filter[lon]=2.0&filter[radius]=100", request1.getPath());
  }

  @Test
  public void testGetSpotWithLocationWhenOffline() {
    mEnduserApi.setOffline(true);

    mEnduserApi.getSpotsByLocation(null, 10, 10, null, null, null, null);

    Mockito.verify(mMockedOfflineEnduserApi).getSpotsByLocation(any(Location.class),
        anyInt(), anyInt(), anyString(), any(EnumSet.class), any(EnumSet.class),
        (APIListCallback<List<Spot>, List<Error>>) any());
    Mockito.verifyNoMoreInteractions(mMockMorpheus);
  }

  @Test
  public void testGetSpotsWithTagsSuccess() throws Exception {
    mMockWebServer.enqueue(new MockResponse().setBody(""));

    final List<Spot> checkSpots = new ArrayList<>();

    Spot spot = new Spot();
    spot.setName("Test");
    ArrayList<Resource> spots = new ArrayList<>();
    spots.add(spot);

    List<String> tags = new ArrayList<>();
    tags.add("tag1");
    tags.add("tag2");

    HashMap<String, Object> meta = new HashMap<>();
    meta.put("cursor", "1");
    meta.put("has-more", true);

    JsonApiObject jsonApiObject = new JsonApiObject();
    jsonApiObject.setResources(spots);
    jsonApiObject.setMeta(meta);

    Location location = mock(Location.class);
    when(location.getLatitude()).thenReturn(1.0);
    when(location.getLongitude()).thenReturn(2.0);

    when(mMockMorpheus.parse(anyString())).thenReturn(jsonApiObject);

    final Semaphore semaphore = new Semaphore(0);

    mEnduserApi.getSpotsByTags(tags, 0, null, null, null, new APIListCallback<List<Spot>, List<Error>>() {
      @Override
      public void finished(List<Spot> result, String cursor, boolean hasMore) {
        checkSpots.add(result.get(0));
        semaphore.release();
      }

      @Override
      public void error(List<Error> error) {
        semaphore.release();
      }
    });

    semaphore.acquire();

    assertTrue(checkSpots.get(0).getName().equals("Test"));
    RecordedRequest request1 = mMockWebServer.takeRequest();
    assertEquals("/_api/v2/consumer/spots?lang=en&filter[tags]=[%27tag1%27,%27tag2%27]", request1.getPath());
  }

  @Test
  public void testGetSpotsWithTagsSpotFlagsSuccess() throws Exception {
    mMockWebServer.enqueue(new MockResponse().setBody(""));

    final List<Spot> checkSpots = new ArrayList<>();

    Spot spot = new Spot();
    spot.setName("Test");
    ArrayList<Resource> spots = new ArrayList<>();
    spots.add(spot);

    List<String> tags = new ArrayList<>();
    tags.add("tag1");
    tags.add("tag2");

    HashMap<String, Object> meta = new HashMap<>();
    meta.put("cursor", "1");
    meta.put("has-more", true);

    JsonApiObject jsonApiObject = new JsonApiObject();
    jsonApiObject.setResources(spots);
    jsonApiObject.setMeta(meta);

    Location location = mock(Location.class);
    when(location.getLatitude()).thenReturn(1.0);
    when(location.getLongitude()).thenReturn(2.0);

    when(mMockMorpheus.parse(anyString())).thenReturn(jsonApiObject);

    final Semaphore semaphore = new Semaphore(0);

    mEnduserApi.getSpotsByTags(tags, 0, null, EnumSet.of(SpotFlags.HAS_LOCATION), null, new APIListCallback<List<Spot>, List<Error>>() {
      @Override
      public void finished(List<Spot> result, String cursor, boolean hasMore) {
        checkSpots.add(result.get(0));
        semaphore.release();
      }

      @Override
      public void error(List<Error> error) {
        semaphore.release();
      }
    });

    semaphore.acquire();

    assertTrue(checkSpots.get(0).getName().equals("Test"));
    RecordedRequest request1 = mMockWebServer.takeRequest();
    assertEquals("/_api/v2/consumer/spots?lang=en&filter[has-location]=true&filter[tags]=[%27tag1%27,%27tag2%27]", request1.getPath());
  }

  @Test
  public void testGetSpotsByTagsWhenOffline() {
    mEnduserApi.setOffline(true);

    mEnduserApi.getSpotsByTags(null, 10, null, null, null, null);

    Mockito.verify(mMockedOfflineEnduserApi).getSpotsByTags(any(List.class),
        anyInt(), anyString(), any(EnumSet.class), any(EnumSet.class),
        (APIListCallback<List<Spot>, List<Error>>) any());
    Mockito.verifyNoMoreInteractions(mMockMorpheus);
  }

  @Test
  public void testSearchSpotsWithTagsSpotFlagsSuccess() throws Exception {
    mMockWebServer.enqueue(new MockResponse().setBody(""));

    final List<Spot> checkSpots = new ArrayList<>();

    Spot spot = new Spot();
    spot.setName("Test");
    ArrayList<Resource> spots = new ArrayList<>();
    spots.add(spot);

    List<String> tags = new ArrayList<>();
    tags.add("tag1");
    tags.add("tag2");

    HashMap<String, Object> meta = new HashMap<>();
    meta.put("cursor", "1");
    meta.put("has-more", true);

    JsonApiObject jsonApiObject = new JsonApiObject();
    jsonApiObject.setResources(spots);
    jsonApiObject.setMeta(meta);

    Location location = mock(Location.class);
    when(location.getLatitude()).thenReturn(1.0);
    when(location.getLongitude()).thenReturn(2.0);

    when(mMockMorpheus.parse(anyString())).thenReturn(jsonApiObject);

    final Semaphore semaphore = new Semaphore(0);

    mEnduserApi.searchSpotsByName("do not touch", 10, null, null, null,
        new APIListCallback<List<Spot>, List<Error>>() {
          @Override
          public void finished(List<Spot> result, String cursor, boolean hasMore) {
            checkSpots.add(result.get(0));
            semaphore.release();
          }

          @Override
          public void error(List<Error> error) {
            fail();
            semaphore.release();
          }
        });

    semaphore.acquire();

    assertTrue(checkSpots.get(0).getName().equals("Test"));
    RecordedRequest request1 = mMockWebServer.takeRequest();
    assertEquals("/_api/v2/consumer/spots?lang=en&page[size]=10&filter[name]=do%20not%20touch", request1.getPath());
  }

  @Test
  public void testSearchSpotsWhenOffline() {
    mEnduserApi.setOffline(true);

    mEnduserApi.searchSpotsByName(null, 10, null, null, null, null);

    Mockito.verify(mMockedOfflineEnduserApi).searchSpotsByName(anyString(),
        anyInt(), anyString(), any(EnumSet.class), any(EnumSet.class),
        (APIListCallback<List<Spot>, List<Error>>) any());
    Mockito.verifyNoMoreInteractions(mMockMorpheus);
  }

  @Test
  public void testGetSystemSuccess() throws Exception {
    mMockWebServer.enqueue(new MockResponse().setBody(""));

    final System[] checkSystem = {null};

    final System system = new System();
    system.setName("Test");

    JsonApiObject jsonApiObject = new JsonApiObject();
    jsonApiObject.setResource(system);

    when(mMockMorpheus.parse(anyString())).thenReturn(jsonApiObject);

    final Semaphore semaphore = new Semaphore(0);

    mEnduserApi.getSystem(new APICallback<System, List<Error>>() {
      @Override
      public void finished(System result) {
        checkSystem[0] = result;
        semaphore.release();
      }

      @Override
      public void error(List<Error> error) {
        semaphore.release();
      }
    });

    semaphore.acquire();

    assertTrue(checkSystem[0].getName().equals("Test"));
    RecordedRequest request1 = mMockWebServer.takeRequest();
    assertEquals("/_api/v2/consumer/systems?lang=en", request1.getPath());
  }

  @Test
  public void testGetSystemWhenOffline() {
    mEnduserApi.setOffline(true);

    mEnduserApi.getSystem(null);

    Mockito.verify(mMockedOfflineEnduserApi)
        .getSystem((APICallback<System, List<Error>>) any());
    Mockito.verifyNoMoreInteractions(mMockMorpheus);
  }

  @Test
  public void testGetMenuSuccess() throws Exception {
    mMockWebServer.enqueue(new MockResponse().setBody(""));

    final Menu[] checkMenu = {null};

    final Menu menu = new Menu();
    menu.setId("123456");

    JsonApiObject jsonApiObject = new JsonApiObject();
    jsonApiObject.setResource(menu);

    when(mMockMorpheus.parse(anyString())).thenReturn(jsonApiObject);

    final Semaphore semaphore = new Semaphore(0);

    mEnduserApi.getMenu("123456", new APICallback<Menu, List<Error>>() {
      @Override
      public void finished(Menu result) {
        checkMenu[0] = result;
        semaphore.release();
      }

      @Override
      public void error(List<Error> error) {
        semaphore.release();
      }
    });

    semaphore.acquire();

    assertTrue(checkMenu[0].getId().equals("123456"));
    RecordedRequest request1 = mMockWebServer.takeRequest();
    assertEquals("/_api/v2/consumer/menus/123456?lang=en", request1.getPath());
  }

  @Test
  public void testGetMenuWhenOffline() {
    mEnduserApi.setOffline(true);

    mEnduserApi.getMenu(null, null);

    Mockito.verify(mMockedOfflineEnduserApi)
        .getMenu(anyString(), (APICallback<Menu, List<Error>>) any());
    Mockito.verifyNoMoreInteractions(mMockMorpheus);
  }

  @Test
  public void testGetSystemSettingsSuccess() throws Exception {
    mMockWebServer.enqueue(new MockResponse().setBody(""));

    final SystemSetting[] checkSystemSetting = {null};

    final SystemSetting systemSetting = new SystemSetting();
    systemSetting.setId("123456");

    JsonApiObject jsonApiObject = new JsonApiObject();
    jsonApiObject.setResource(systemSetting);

    when(mMockMorpheus.parse(anyString())).thenReturn(jsonApiObject);

    final Semaphore semaphore = new Semaphore(0);

    mEnduserApi.getSystemSetting("123456", new APICallback<SystemSetting, List<Error>>() {
      @Override
      public void finished(SystemSetting result) {
        checkSystemSetting[0] = result;
        semaphore.release();
      }

      @Override
      public void error(List<Error> error) {
        semaphore.release();
      }
    });

    semaphore.acquire();

    assertTrue(checkSystemSetting[0].getId().equals("123456"));
    RecordedRequest request1 = mMockWebServer.takeRequest();
    assertEquals("/_api/v2/consumer/settings/123456?lang=en", request1.getPath());
  }

  @Test
  public void testGetSystemSettingWhenOffline() {
    mEnduserApi.setOffline(true);

    mEnduserApi.getSystemSetting(null, null);

    Mockito.verify(mMockedOfflineEnduserApi).getSystemSetting(anyString(),
        (APICallback<SystemSetting, List<Error>>) any());
    Mockito.verifyNoMoreInteractions(mMockMorpheus);
  }

  @Test
  public void testGetStyleSuccess() throws Exception {
    mMockWebServer.enqueue(new MockResponse().setBody(""));

    final Style[] checkStyle = {null};

    final Style style = new Style();
    style.setId("123456");

    JsonApiObject jsonApiObject = new JsonApiObject();
    jsonApiObject.setResource(style);

    when(mMockMorpheus.parse(anyString())).thenReturn(jsonApiObject);

    final Semaphore semaphore = new Semaphore(0);

    mEnduserApi.getStyle("123456", new APICallback<Style, List<Error>>() {
      @Override
      public void finished(Style result) {
        checkStyle[0] = result;
        semaphore.release();
      }

      @Override
      public void error(List<Error> error) {

      }
    });

    semaphore.acquire();

    assertTrue(checkStyle[0].getId().equals("123456"));
    RecordedRequest request1 = mMockWebServer.takeRequest();
    assertEquals("/_api/v2/consumer/styles/123456?lang=en", request1.getPath());
  }

  @Test
  public void testGetStyleWhenOffline() {
    mEnduserApi.setOffline(true);

    mEnduserApi.getStyle(null, null);

    Mockito.verify(mMockedOfflineEnduserApi).getStyle(anyString(),
        (APICallback<Style, List<Error>>) any());
    Mockito.verifyNoMoreInteractions(mMockMorpheus);
  }
}