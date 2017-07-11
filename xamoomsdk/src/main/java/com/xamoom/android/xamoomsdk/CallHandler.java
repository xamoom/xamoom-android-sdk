/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.xamoomsdk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import at.rags.morpheus.Error;
import at.rags.morpheus.JsonApiObject;
import at.rags.morpheus.Morpheus;
import at.rags.morpheus.Resource;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Used to enque the retrofit2 calls and parse them via Morpheus.
 */
public class CallHandler <T extends Resource> {
  public static final String ERROR_CODE_CALL_FAILURE = "10000";
  public static final String ERROR_CODE_PARSING_JSON_FAILED = "10001";
  public static final String ERROR_CODE_RESPONSE_IS_NULL = "10002";
  public static final String ERROR_MESSAGE_RESPONSE_IS_NULL = "Parsed response is null. " +
      "Check if data really exists.";


  private Morpheus morpheus;

  public CallHandler(Morpheus morpheus) {
    this.morpheus = morpheus;
  }

  public void enqueCall(Call<ResponseBody> call, final APICallback<T, List<Error>> callback) {
    if (call == null) {
      throw new NullPointerException("call is null");
    }

    call.enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        String json = getJsonFromResponse(response);

        JsonApiObject jsonApiObject = null;
        try {
          jsonApiObject = morpheus.parse(json);
        } catch (Exception e) {
          callback.error(createError(e.getMessage(), ERROR_CODE_PARSING_JSON_FAILED));
          return;
        }

        if (jsonApiObject == null) {
          callback.error(createError(ERROR_MESSAGE_RESPONSE_IS_NULL, ERROR_CODE_RESPONSE_IS_NULL));
          return;
        }

        if (jsonApiObject.getResource() != null) {
          T t = (T) jsonApiObject.getResource();
          callback.finished(t);
        } else if (jsonApiObject.getErrors() != null && jsonApiObject.getErrors().size() > 0) {
          callback.error(jsonApiObject.getErrors());
        }
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        callback.error(createError(t.getMessage(), ERROR_CODE_CALL_FAILURE));
      }
    });
  }

  public void enqueListCall(Call<ResponseBody> call, final APIListCallback<List<T>,
      List<Error>> callback) {
    if (call == null) {
      throw new NullPointerException("call is null");
    }

    call.enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        String json = getJsonFromResponse(response);

        JsonApiObject jsonApiObject = null;
        try {
          jsonApiObject = morpheus.parse(json);
        } catch (Exception e) {
          callback.error(createError(e.getMessage(), ERROR_CODE_PARSING_JSON_FAILED));
          return;
        }

        if (jsonApiObject == null) {
          callback.error(createError(ERROR_MESSAGE_RESPONSE_IS_NULL, ERROR_CODE_RESPONSE_IS_NULL));
          return;
        }

        if (jsonApiObject.getResources() != null) {
          List<T> contents = (List<T>) jsonApiObject.getResources();
          String cursor = jsonApiObject.getMeta().get("cursor").toString();
          boolean hasMore = (boolean) jsonApiObject.getMeta().get("has-more");
          callback.finished(contents, cursor, hasMore);
        } else if (jsonApiObject.getErrors() != null && jsonApiObject.getErrors().size() > 0) {
          callback.error(jsonApiObject.getErrors());
        }
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        callback.error(createError(t.getMessage(), ERROR_CODE_CALL_FAILURE));
      }
    });
  }

  private String getJsonFromResponse(Response<ResponseBody> response) {
    String json = null;

    if (response.body() != null) {
      try {
        json = response.body().string();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    if (response.errorBody() != null) {
      try {
        json = response.errorBody().string();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return json;
  }

  private List<Error> createError(String message, String code) {
    Error error = new Error();
    error.setDetail(message);
    error.setCode(code);
    List<Error> errors = new ArrayList<Error>();
    errors.add(error);
    return errors;
  }

  public void setMorpheus(Morpheus morpheus) {
    this.morpheus = morpheus;
  }
}
