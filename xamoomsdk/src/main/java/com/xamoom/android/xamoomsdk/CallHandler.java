/*
 * Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the root of this project.
 */

package com.xamoom.android.xamoomsdk;

import androidx.annotation.NonNull;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import at.rags.morpheus.Error;
import at.rags.morpheus.JsonApiObject;
import at.rags.morpheus.Morpheus;
import at.rags.morpheus.Resource;
import okhttp3.Headers;
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
  public static final String ERROR_CODE_CONNECTION_FAILURE = "10003";
  public static final String ERROR_CODE_REQUEST_OR_RESPONSE_FAILURE = "10003";
  public static final String ERROR_MESSAGE_RESPONSE_IS_NULL = "Parsed response is null. " +
          "Check if data really exists.";

  private static final String HEADER_EPHEMERAL = "X-Ephemeral-Id";
  private static final String HEADER_AUTHORIZATION = "Authorization";

  private Morpheus morpheus;
  private CallHandlerListener listener;

  public CallHandler(@NonNull Morpheus morpheus) {
    this.morpheus = morpheus;
  }

  public void enqueCall(@NonNull Call<ResponseBody> call, final APICallback<T, List<Error>> callback) {
    checkForNull(call);
    call.enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        // checks if there is a ephemeral id and calls listeners gotEphemeralId method
        notifyEphemeralId(response.headers());
        notifyAuthorizationId(response.headers());

        JsonApiObject jsonApiObject;
        try {
          jsonApiObject = parseResponse(response);
        } catch (Exception e) {
          callback.error(createError(e));
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
        callback.error(createError(t));
      }
    });
  }

  public void enquePasswordCall(@NonNull Call<ResponseBody> call, final APIPasswordCallback<T, List<Error>> callback) {
    checkForNull(call);
    call.enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        // checks if there is a ephemeral id and calls listeners gotEphemeralId method
        notifyEphemeralId(response.headers());
        notifyAuthorizationId(response.headers());

        JsonApiObject jsonApiObject;
        try {
          jsonApiObject = parseResponse(response);
        } catch (Exception e) {
          callback.error(createError(e));
          return;
        }

        if (jsonApiObject.getResource() != null) {
          T t = (T) jsonApiObject.getResource();
          callback.finished(t);
        } else if (jsonApiObject.getErrors() != null && jsonApiObject.getErrors().size() > 0) {
          if (isPasswordError(jsonApiObject.getErrors())) {
            callback.passwordRequested();
          } else {
            callback.error(jsonApiObject.getErrors());
          }
        }
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        callback.error(createError(t));
      }
    });
  }
  public void enqueListCall(@NonNull Call<ResponseBody> call, final APIListCallback<List<T>,
          List<Error>> callback) {
    checkForNull(call);

    call.enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        // checks if there is a ephemeral id and calls listeners gotEphemeralId method
        notifyEphemeralId(response.headers());
        notifyAuthorizationId(response.headers());

        JsonApiObject jsonApiObject;
        try {
          jsonApiObject = parseResponse(response);
        } catch (Exception e) {
          callback.error(createError(e));
          return;
        }

        if (jsonApiObject.getResources() != null) {
          List<?> contents = jsonApiObject.getResources();
          String cursor = jsonApiObject.getMeta().get("cursor").toString();
          boolean hasMore = (boolean) jsonApiObject.getMeta().get("has-more");
          callback.finished((List<T>) contents, cursor, hasMore);
        } else if (jsonApiObject.getErrors() != null && jsonApiObject.getErrors().size() > 0) {
          callback.error(jsonApiObject.getErrors());
        }
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        callback.error(createError(t));
      }
    });
  }

  private void checkForNull(Call call) {
    if (call == null) {
      throw new NullPointerException("Call object is null");
    }
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

  private JsonApiObject parseResponse(Response response) throws Exception {
    String json = getJsonFromResponse(response);

    JsonApiObject jsonApiObject;
    try {
      jsonApiObject = morpheus.parse(json);
    } catch (Exception e) {
      throw e;
    }

    if (jsonApiObject == null) {
      throw new NullPointerException(ERROR_MESSAGE_RESPONSE_IS_NULL);
    }

    return jsonApiObject;
  }

  private List<Error> createError(Throwable t) {
    String code;
    if (t instanceof IOException) {
      code = ERROR_CODE_CONNECTION_FAILURE;
    } else if (t instanceof RuntimeException) {
      code =  ERROR_CODE_REQUEST_OR_RESPONSE_FAILURE;
    } else if (t instanceof JSONException) {
      code = ERROR_CODE_PARSING_JSON_FAILED;
    } else {
      code = ERROR_CODE_CALL_FAILURE;
    }

    Error error = new Error();
    error.setDetail(t.getMessage());
    error.setCode(code);
    List<Error> errors = new ArrayList<>();
    errors.add(error);
    return errors;
  }

  private void notifyEphemeralId(Headers headers) {
    String ephemeralId = headers.get(HEADER_EPHEMERAL);
    if (ephemeralId != null
            && listener != null) {
      listener.gotEphemeralId(ephemeralId);
    }
  }

  private void notifyAuthorizationId(Headers headers) {
    String authorizationId = headers.get(HEADER_AUTHORIZATION);
    if (authorizationId != null && listener != null) {
      listener.gotAuthorizationId(authorizationId);
    }
  }

  private boolean isPasswordError(List<Error> errors) {
    for (Error error : errors) {
      StringBuilder builder = new StringBuilder();
      builder.append("code " + error.getCode());
      builder.append("\n");
      builder.append("status " + error.getStatus());
      builder.append("\n");
      builder.append("detail " + error.getDetail());
      Log.e("Error: ", "Error: " + builder.toString());

      if (Integer.parseInt(error.getStatus()) == 401 && Integer.parseInt(error.getCode()) == 92) {
        return true;
      }

      return false;
    }

    return false;
  }

  public interface CallHandlerListener {
    void gotEphemeralId(String ephemeralId);
    void gotAuthorizationId(String authorizationId);
  }

  public void setMorpheus(Morpheus morpheus) {
    this.morpheus = morpheus;
  }

  public void setListener(CallHandlerListener listener) {
    this.listener = listener;
  }
}
