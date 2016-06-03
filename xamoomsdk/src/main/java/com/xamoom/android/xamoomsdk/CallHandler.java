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

        try {
          JsonApiObject jsonApiObject = morpheus.parse(json);

          if (jsonApiObject.getResource() != null) {
            T t = (T) jsonApiObject.getResource();
            callback.finished(t);
          } else if (jsonApiObject.getErrors() != null && jsonApiObject.getErrors().size() > 0) {
            callback.error(jsonApiObject.getErrors());
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        Error error = new Error();
        error.setDetail(t.getMessage());
        List<Error> errors = new ArrayList<Error>();
        errors.add(error);
        callback.error(errors);
      }
    });
  }

  public void enqueListCall(Call<ResponseBody> call, final APIListCallback<List<T>,
      List<Error>> callback) {
    if (call == null) {
      return;
    }

    call.enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        String json = getJsonFromResponse(response);

        try {
          JsonApiObject jsonApiObject = morpheus.parse(json);

          if (jsonApiObject.getResources() != null) {
            List<T> contents = (List<T>) (List<?>) jsonApiObject.getResources();
            String cursor = jsonApiObject.getMeta().get("cursor").toString();
            boolean hasMore = (boolean) jsonApiObject.getMeta().get("has-more");
            callback.finished(contents, cursor, hasMore);
          } else if (jsonApiObject.getErrors() != null && jsonApiObject.getErrors().size() > 0) {
            callback.error(jsonApiObject.getErrors());
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        Error error = new Error();
        error.setDetail(t.getMessage());
        List<Error> errors = new ArrayList<Error>();
        errors.add(error);
        callback.error(errors);
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

  public void setMorpheus(Morpheus morpheus) {
    this.morpheus = morpheus;
  }
}
