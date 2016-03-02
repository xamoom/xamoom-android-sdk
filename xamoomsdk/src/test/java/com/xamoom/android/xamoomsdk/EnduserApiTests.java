package com.xamoom.android.xamoomsdk;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.xamoom.android.xamoomsdk.Resource.Attributes.ContentAttributesMessage;
import com.xamoom.android.xamoomsdk.Resource.Attributes.ContentBlockAttributeMessage;
import com.xamoom.android.xamoomsdk.Resource.Base.DataMessage;
import com.xamoom.android.xamoomsdk.Resource.Base.EmptyMessage;
import com.xamoom.android.xamoomsdk.Resource.Base.JsonApiMessage;
import com.xamoom.android.xamoomsdk.Resource.Content;
import com.xamoom.android.xamoomsdk.Resource.Error.ErrorMessage;
import com.xamoom.android.xamoomsdk.Resource.Error.Errors;
import com.xamoom.android.xamoomsdk.Resource.Relationships.ContentRelationships;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.converter.GsonConverter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(MockitoJUnitRunner.class)
public class EnduserApiTests {
  private static final String TAG = EnduserApiTests.class.getSimpleName();

  @Test
  public void testInitWithApikey() throws Exception {
    EnduserApi enduserApi = new EnduserApi("test");

    assertNotNull(enduserApi.getEnduserApiInterface());
    assertEquals(enduserApi.getSystemLanguage(), "en");
  }

  @Test
  public void testInitWithRestAdapter() throws Exception {
    RestAdapter restAdapter = new RestAdapter.Builder()
        .setEndpoint("URL")
        .setLogLevel(RestAdapter.LogLevel.FULL)
        .setLog(new AndroidLog(TAG))
        .setRequestInterceptor(new RequestInterceptor() {
          @Override
          public void intercept(RequestFacade request) {
            request.addHeader("APIKEY", "KEY");
            request.addHeader("X-DEVKEY", "XKEY");
          }
        })
        .setConverter(new GsonConverter(new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()))
        .build();

    EnduserApi enduserApi = new EnduserApi(restAdapter);

    assertNotNull(enduserApi.getEnduserApiInterface());
    assertEquals(enduserApi.getSystemLanguage(), "en");
  }

  @Captor
  ArgumentCaptor <ResponseCallback<JsonApiMessage<EmptyMessage, DataMessage<ContentAttributesMessage,
      ContentRelationships>, List<DataMessage<ContentBlockAttributeMessage, EmptyMessage>>>>>
      mResponseCallbackArgumentCaptor;

  @Test
  public void testGetContentWithIDCallsSuccess() throws Exception {
    EnduserApi enduserApi = new EnduserApi("123456");
    EnduserApiInterface enduserApiInterface = mock(EnduserApiInterface.class);
    enduserApi.setEnduserApiInterface(enduserApiInterface);

    enduserApi.getContent("123456", new APICallback<Content, ErrorMessage>() {
      @Override
      public void finished(Content result) {
        assertNull(result);
      }

      @Override
      public void error(ErrorMessage error) {
      }
    });

    verify(enduserApiInterface).getContent(anyString(), anyMap(), mResponseCallbackArgumentCaptor.capture());
    mResponseCallbackArgumentCaptor.getValue().success(null, null);
  }

  @Test
  public void testGetContentWithIDCallsError() throws Exception {
    EnduserApi enduserApi = new EnduserApi("123456");
    EnduserApiInterface enduserApiInterface = mock(EnduserApiInterface.class);
    enduserApi.setEnduserApiInterface(enduserApiInterface);

    enduserApi.getContent("123456", new APICallback<Content, ErrorMessage>() {
      @Override
      public void finished(Content result) {
      }

      @Override
      public void error(ErrorMessage error) {
        assertNotNull(error);
      }
    });

    verify(enduserApiInterface).getContent(anyString(), anyMap(), mResponseCallbackArgumentCaptor.capture());
    mResponseCallbackArgumentCaptor.getValue().failure(new ErrorMessage());
  }

  @Test
  public void testGetContentWithFlags() throws Exception {
    EnduserApi enduserApi = new EnduserApi("123456");
    EnduserApiInterface enduserApiInterface = mock(EnduserApiInterface.class);
    enduserApi.setEnduserApiInterface(enduserApiInterface);

    Map<String, String> params = new LinkedHashMap<>();
    params.put("lang","en");
    params.put("preview","true");
    params.put("public-only", "true");

    enduserApi.getContent("123456", EnumSet.of(ContentFlags.PREVIEW, ContentFlags.PRIVATE),
        new APICallback<Content, ErrorMessage>() {
          @Override
          public void finished(Content result) {
            assertNull(result);
          }

          @Override
          public void error(ErrorMessage error) {
          }
        });

    ArgumentCaptor<Map> argumentsCaptured = ArgumentCaptor.forClass(Map.class);

    verify(enduserApiInterface).getContent(anyString(), argumentsCaptured.capture(), mResponseCallbackArgumentCaptor.capture());
    mResponseCallbackArgumentCaptor.getValue().success(null, null);
    assertTrue(argumentsCaptured.getValue().equals(params));
  }

  @Test
  public void testGetContentWithFlagsCallsError() throws Exception {
    EnduserApi enduserApi = new EnduserApi("123456");
    EnduserApiInterface enduserApiInterface = mock(EnduserApiInterface.class);
    enduserApi.setEnduserApiInterface(enduserApiInterface);

    enduserApi.getContent("123456", null, new APICallback<Content, ErrorMessage>() {
      @Override
      public void finished(Content result) {
      }

      @Override
      public void error(ErrorMessage error) {
        assertNotNull(error);
      }
    });

    verify(enduserApiInterface).getContent(anyString(), anyMap(), mResponseCallbackArgumentCaptor.capture());
    mResponseCallbackArgumentCaptor.getValue().failure(new ErrorMessage());
  }

}