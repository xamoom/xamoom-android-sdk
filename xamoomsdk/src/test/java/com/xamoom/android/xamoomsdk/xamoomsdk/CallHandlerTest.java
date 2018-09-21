/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.xamoomsdk.xamoomsdk;

import com.xamoom.android.xamoomsdk.APICallback;
import com.xamoom.android.xamoomsdk.APIListCallback;
import com.xamoom.android.xamoomsdk.CallHandler;
import com.xamoom.android.xamoomsdk.EnduserApiInterface;
import com.xamoom.android.xamoomsdk.Resource.Content;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Semaphore;

import at.rags.morpheus.Error;
import at.rags.morpheus.JsonApiObject;
import at.rags.morpheus.Morpheus;
import at.rags.morpheus.Resource;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CallHandlerTest {

  private Morpheus mockMorpheus;
  private CallHandler callHandler;
  private Call mockCall;
  private CallHandler.CallHandlerListener mockEventListener;

  @Before
  public void setup() {
    mockMorpheus = mock(Morpheus.class);
    mockCall = Mockito.mock(Call.class);
    mockEventListener = Mockito.mock(CallHandler.CallHandlerListener.class);
    callHandler = new CallHandler(mockMorpheus);
    callHandler.setListener(mockEventListener);
  }

  @Test
  public void testConstructor() {
    CallHandler callHandler = new CallHandler(mockMorpheus);

    assertNotNull(callHandler);
  }

  @Test(expected = NullPointerException.class)
  public void testEnqueCallWithNull() {
    callHandler.enqueCall(null, null);
  }

  /**
   * Test if finished method gets called after successfully parsed json from the response.
   *
   * @throws InterruptedException
   */
  @Test
  public void testEnqueCallSuccessfull() throws InterruptedException {
    Headers headers = new Headers.Builder()
        .add("X-Ephemeral-Id", "1234")
        .add(EnduserApiInterface.HEADER_AUTHORIZATION, "abcd")
        .build();
    final Response<ResponseBody> response =
        Response.success(ResponseBody.create(null, ""), headers);
    final Content content = new Content();
    content.setId("1");
    JsonApiObject jsonApiObject = new JsonApiObject();
    jsonApiObject.setResource(content);

    try {
      when(mockMorpheus.parse(any(String.class))).thenReturn(jsonApiObject);
    } catch (Exception e) {
      e.printStackTrace();
    }

    doAnswer(new Answer() {
      @Override
      public Void answer(InvocationOnMock invocation) throws Throwable {
        Callback<ResponseBody> callback =
            (Callback<ResponseBody>) invocation.getArguments()[0];
            callback.onResponse(mockCall, response);
        return null;
      }
    }).when(mockCall).enqueue(any(Callback.class));


    final Semaphore semaphore = new Semaphore(1);
    callHandler.enqueCall(mockCall, new APICallback() {
      @Override
      public void finished(Object result) {
        assertEquals(content, result);
        semaphore.release();
      }

      @Override
      public void error(Object error) {
        fail();
      }
    });
    semaphore.acquire();

    Mockito.verify(mockEventListener).gotEphemeralId(eq("1234"));
    Mockito.verify(mockEventListener).gotAuthorizationId("abcd");
  }

  /**
   * Tests if an error gets returned via the callback, when there is a problem with parsing the json
   * data.
   *
   * @throws InterruptedException
   */
  @Test
  public void testEnqueCallWithParsingError() throws InterruptedException {
    final Response<ResponseBody> response =
        Response.success(ResponseBody.create(null, ""));
    final Content content = new Content();
    content.setId("1");
    JsonApiObject jsonApiObject = new JsonApiObject();
    jsonApiObject.setResource(content);

    try {
      when(mockMorpheus.parse(any(String.class))).thenThrow(new JSONException("Could not parse"));
    } catch (Exception e) {
      e.printStackTrace();
    }

    doAnswer(new Answer() {
      @Override
      public Void answer(InvocationOnMock invocation) throws Throwable {
        Callback<ResponseBody> callback =
            (Callback<ResponseBody>) invocation.getArguments()[0];
        callback.onResponse(mockCall, response);
        return null;
      }
    }).when(mockCall).enqueue(any(Callback.class));


    final Semaphore semaphore = new Semaphore(1);
    callHandler.enqueCall(mockCall, new APICallback() {
      @Override
      public void finished(Object result) {
        fail();
      }

      @Override
      public void error(Object error) {
        List<Error> errors = (List<Error>) error;
        assertEquals(CallHandler.ERROR_CODE_PARSING_JSON_FAILED, errors.get(0).getCode());
        semaphore.release();
      }
    });
    semaphore.acquire();
  }

  /**
   * Test if an error gets returned via the callback, when the parsed data is null.
   *
   * @throws InterruptedException
   */
  @Test
  public void testEnqueCallNullParsedData() throws InterruptedException {
    final Response<ResponseBody> response =
        Response.success(ResponseBody.create(null, ""));

    try {
      when(mockMorpheus.parse(any(String.class))).thenReturn(null);
    } catch (Exception e) {
      e.printStackTrace();
    }

    doAnswer(new Answer() {
      @Override
      public Void answer(InvocationOnMock invocation) throws Throwable {
        Callback<ResponseBody> callback =
            (Callback<ResponseBody>) invocation.getArguments()[0];
        callback.onResponse(mockCall, response);
        return null;
      }
    }).when(mockCall).enqueue(any(Callback.class));


    final Semaphore semaphore = new Semaphore(1);
    callHandler.enqueCall(mockCall, new APICallback() {
      @Override
      public void finished(Object result) {
        fail();
      }

      @Override
      public void error(Object error) {
        List<Error> errors = (List<Error>) error;
        assertEquals(CallHandler.ERROR_CODE_REQUEST_OR_RESPONSE_FAILURE, errors.get(0).getCode());
        semaphore.release();
      }
    });
    semaphore.acquire();
  }

  /**
   * Test if parsed errors from the backend get returned.
   *
   * @throws InterruptedException
   */
  @Test
  public void testEnqueCallSuccessReturningErrors() throws InterruptedException {
    final Response<ResponseBody> response =
        Response.success(ResponseBody.create(null, ""));

    Error error = new Error();
    error.setCode("1");
    List<Error> errors = new ArrayList<>(1);
    errors.add(error);
    JsonApiObject jsonApiObject = new JsonApiObject();
    jsonApiObject.setErrors(errors);

    try {
      when(mockMorpheus.parse(any(String.class))).thenReturn(jsonApiObject);
    } catch (Exception e) {
      e.printStackTrace();
    }

    doAnswer(new Answer() {
      @Override
      public Void answer(InvocationOnMock invocation) throws Throwable {
        Callback<ResponseBody> callback =
            (Callback<ResponseBody>) invocation.getArguments()[0];
        callback.onResponse(mockCall, response);
        return null;
      }
    }).when(mockCall).enqueue(any(Callback.class));


    final Semaphore semaphore = new Semaphore(1);
    callHandler.enqueCall(mockCall, new APICallback() {
      @Override
      public void finished(Object result) {
        fail();
      }

      @Override
      public void error(Object error) {
        List<Error> errors = (List<Error>) error;
        assertEquals("1", errors.get(0).getCode());
        semaphore.release();
      }
    });
    semaphore.acquire();
  }

  /**
   * Test if errors from the calling get returned.
   *
   * @throws InterruptedException
   */
  @Test
  public void testEnqueCallFailingReturningErrors() throws InterruptedException {
    final Response<ResponseBody> response =
        Response.success(ResponseBody.create(null, ""));

    doAnswer(new Answer() {
      @Override
      public Void answer(InvocationOnMock invocation) throws Throwable {
        Callback<ResponseBody> callback =
            (Callback<ResponseBody>) invocation.getArguments()[0];
        callback.onFailure(mockCall, new NullPointerException("Null for testing"));
        return null;
      }
    }).when(mockCall).enqueue(any(Callback.class));


    final Semaphore semaphore = new Semaphore(1);
    callHandler.enqueCall(mockCall, new APICallback() {
      @Override
      public void finished(Object result) {
        fail();
      }

      @Override
      public void error(Object error) {
        List<Error> errors = (List<Error>) error;
        assertEquals(CallHandler.ERROR_CODE_CONNECTION_FAILURE, errors.get(0).getCode());
        semaphore.release();
      }
    });
    semaphore.acquire();
  }

  @Test(expected = NullPointerException.class)
  public void testEnqueListCallWithNull() {
    callHandler.enqueListCall(null, null);
  }

  /**
   * Test if finished method gets called after successfully parsed json from the response.
   *
   * @throws InterruptedException
   */
  @Test
  public void testEnqueListCallSuccessfull() throws InterruptedException {
    Headers headers = new Headers.Builder()
        .add("X-Ephemeral-Id", "1234")
        .add(EnduserApiInterface.HEADER_AUTHORIZATION, "abcd")
        .build();
    final Response<ResponseBody> response =
        Response.success(ResponseBody.create(null, ""), headers);
    final Content content = new Content();
    content.setId("1");
    final List<Resource> contents = new ArrayList<>(1);
    contents.add(content);
    JsonApiObject jsonApiObject = new JsonApiObject();
    jsonApiObject.setResources(contents);
    HashMap<String, Object> meta = new HashMap<>(2);
    meta.put("cursor", "1");
    meta.put("has-more", true);
    jsonApiObject.setMeta(meta);

    try {
      when(mockMorpheus.parse(any(String.class))).thenReturn(jsonApiObject);
    } catch (Exception e) {
      e.printStackTrace();
    }

    doAnswer(new Answer() {
      @Override
      public Void answer(InvocationOnMock invocation) throws Throwable {
        Callback<ResponseBody> callback =
            (Callback<ResponseBody>) invocation.getArguments()[0];
        callback.onResponse(mockCall, response);
        return null;
      }
    }).when(mockCall).enqueue(any(Callback.class));


    final Semaphore semaphore = new Semaphore(1);
    callHandler.enqueListCall(mockCall, new APIListCallback() {
      @Override
      public void finished(Object result, String cursor, boolean hasMore) {
        assertEquals(contents, result);
        assertEquals("1", cursor);
        assertEquals(true, hasMore);
        semaphore.release();
      }

      @Override
      public void error(Object error) {
        fail();
      }
    });
    semaphore.acquire();

    Mockito.verify(mockEventListener).gotEphemeralId(eq("1234"));
    Mockito.verify(mockEventListener).gotAuthorizationId(eq("abcd"));
  }
}
