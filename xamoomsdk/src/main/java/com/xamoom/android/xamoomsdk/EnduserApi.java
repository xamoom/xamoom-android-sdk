package com.xamoom.android.xamoomsdk;

import android.location.Location;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.xamoom.android.xamoomsdk.Resource.Content;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;
import com.xamoom.android.xamoomsdk.Utils.ListUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import at.rags.morpheus.*;
import at.rags.morpheus.Error;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * EnduserApi is the main part of the XamoomSDK. You can use it to send api request to
 * the xamoom cloud.
 *
 * Use {@link #EnduserApi(String)} to initialize.
 *
 * Change the requested language by  setting {@link #mLanguage}. The users language is saved
 * in {@link #mSystemLanguage}.
 */
public class EnduserApi {
  private static final String TAG = EnduserApi.class.getSimpleName();
  private static final String API_URL = "https://xamoom-cloud.appspot.com/_api/v2/consumer/";

  private Morpheus mMorpheus;
  private EnduserApiInterface mEnduserApiInterface;
  private String mLanguage;
  private String mSystemLanguage;

  public EnduserApi(final String apikey) {
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(API_URL)
        /*
        .setRequestInterceptor(new RequestInterceptor() {
          @Override
          public void intercept(RequestFacade request) {
            request.addHeader("APIKEY", apikey);
          }
        })*/
        .build();

    mEnduserApiInterface = retrofit.create(EnduserApiInterface.class);

    initMorpheus();
    initVars();
  }

  public EnduserApi(Retrofit retrofit) {
    mEnduserApiInterface = retrofit.create(EnduserApiInterface.class);

    initMorpheus();
    initVars();
  }

  private void initMorpheus() {
    mMorpheus = new Morpheus();
    Deserializer.registerResourceClass("contents", Content.class);
    Deserializer.registerResourceClass("contentblocks", ContentBlock.class);
  }

  private void initVars() {
    mSystemLanguage = Locale.getDefault().getLanguage();
    mLanguage = mSystemLanguage;
  }

  /**
   * Get a content for a specific contentID.
   *
   * @param contentID ContentID from xamoom-cloud.
   * @param callback {@link APICallback}.
   */
  public void getContent(final String contentID, final APICallback<Content, List<Error>> callback) {
    Map<String, String> params = getUrlParameter();

    Call<ResponseBody> call = mEnduserApiInterface.getContent(contentID, params);
    enqueContentCall(call, callback);
  }

  /**
   * Get a content for a specific contentID with possible flags.
   *
   * @param contentID ContentID from xamoom-cloud.
   * @param contentFlags Different flags {@link ContentFlags}.
   * @param callback {@link APICallback}.
   */
  public void getContent(String contentID, EnumSet<ContentFlags> contentFlags, final APICallback<Content,
      List<at.rags.morpheus.Error>> callback) {
    Map<String, String> params = getUrlParameterContent(contentFlags);

    Call<ResponseBody> call = mEnduserApiInterface.getContent(contentID, params);
    if (call != null) {
      enqueContentCall(call, callback);
    }
  }

  /**
   * Get a content for a specific LocationIdentifier.
   *
   * @param locationIdentifier LocationIdentifier from QR or NFC.
   * @param callback {@link APICallback}.
   */
  public void getContentByLocationIdentifier(String locationIdentifier, final APICallback<Content,
      List<Error>> callback) {
    Map<String, String> params = getUrlParameter();
    params.put("filter[location-identifier]", locationIdentifier);

    Call<ResponseBody> call = mEnduserApiInterface.getContents(params);
    enqueContentCall(call, callback);
  }

  /**
   * Get content for a specific beacon.
   *
   * @param major Beacon major ID.
   * @param minor Beacon minor ID.
   * @param callback {@link APICallback}.
   */
  public void getContentByBeacon(int major, int minor, final APICallback<Content, List<Error>>
      callback) {
    getContentByLocationIdentifier(String.format("%s|%s", major, minor), callback);
  }

  /**
   * Get list of contents with your location. Geofence radius is 40m.
   *
   * @param location Users location.
   * @param pageSize PageSize for returned contents. Default 100.
   * @param cursor Cursor for paging.
   * @param sortFlags {@link ContentSortFlags} to sort results.
   * @param callback {@link APIListCallback}.
   */
  public void getContentsByLocation(Location location, int pageSize, @Nullable String cursor,
                                    final EnumSet<ContentSortFlags> sortFlags, final APIListCallback<List<Content>,
      List<Error>> callback) {
    Map<String, String> params = getUrlParameterContentSort(sortFlags);
    params = addPagingToUrl(params, pageSize, cursor);
    params.put("filter[lat]", Double.toString(location.getLatitude()));
    params.put("filter[lon]", Double.toString(location.getLongitude()));

    Call<ResponseBody> call = mEnduserApiInterface.getContents(params);
    enqueContentsCall(call, callback);
  }

  public void getContentsByTags(List<String> tags, int pageSize, String cursor,
                                EnumSet<ContentSortFlags> sortFlags,
                                APIListCallback<List<Content>, List<Error>> callback) {
    Map<String, String> params = getUrlParameterContentSort(sortFlags);
    params = addPagingToUrl(params, pageSize, cursor);
    params.put("filter[tags]", ListUtil.joinStringList(tags, ","));

    Call<ResponseBody> call = mEnduserApiInterface.getContents(params);
    enqueContentsCall(call, callback);
  }

  /**
   * Makes the call and uses Morpheus to parse the jsonapi response.
   *
   * @param call Call<ResponseBody> from Retrofit
   * @param callback {@link APICallback}
   */
  private void enqueContentCall(Call<ResponseBody> call, final APICallback<Content, List<Error>> callback) {
    call.enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        String json = getJsonFromResponse(response);

        try {
          JsonApiObject jsonApiObject = mMorpheus.parse(json);

          if (jsonApiObject.getResource() != null) {
            Content content = (Content) jsonApiObject.getResource();
            callback.finished(content);
          } else if (jsonApiObject.getErrors().size() > 0) {
            callback.error(jsonApiObject.getErrors());
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        callback.error(null);
      }
    });
  }

  private void enqueContentsCall(Call<ResponseBody> call, final APIListCallback<List<Content>,
      List<Error>> callback) {
    call.enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        String json = getJsonFromResponse(response);

        try {
          JsonApiObject jsonApiObject = mMorpheus.parse(json);

          if (jsonApiObject.getResources() != null) {
            List<Content> contents = (List<Content>) (List<?>) jsonApiObject.getResources();
            String cursor = jsonApiObject.getMeta().get("cursor").toString();
            boolean hasMore = (boolean) jsonApiObject.getMeta().get("has-more");
            callback.finished(contents, cursor, hasMore);
          } else if (jsonApiObject.getErrors().size() > 0) {
            callback.error(jsonApiObject.getErrors());
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        callback.error(null);
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

  private Map<String, String> getUrlParameter() {
    Map<String, String> params = new LinkedHashMap<>();
    params.put("lang", mLanguage);
    return params;
  }

  private Map<String, String> getUrlParameterContent(EnumSet<ContentFlags> contentFlags) {
    Map<String, String> params = getUrlParameter();

    if (contentFlags == null) {
      return params;
    }

    if (contentFlags.contains(ContentFlags.PREVIEW)) {
      params.put("preview", "true");
    }

    if (contentFlags.contains(ContentFlags.PRIVATE)) {
      params.put("public-only", "true");
    }

    return params;
  }

  private Map<String, String> getUrlParameterContentSort(EnumSet<ContentSortFlags> contentSortFlags) {
    Map<String, String> params = getUrlParameter();

    if (contentSortFlags == null) {
      return params;
    }

    ArrayList<String> sortParams = new ArrayList<String>();

    if (contentSortFlags.contains(ContentSortFlags.NAME)) {
      sortParams.add("name");
    }

    if (contentSortFlags.contains(ContentSortFlags.NAME_DESC)) {
      sortParams.add("-name");
    }

    params.put("sort", TextUtils.join(",", sortParams));
    return params;
  }

  private Map<String, String> addPagingToUrl( Map<String, String> params, int pageSize, String cursor) {
    params.put("page[size]", Integer.toString(pageSize));

    if (cursor != null) {
      params.put("page[cursor]", cursor);
    }

    return params;
  }

  //getters & setters

  public String getSystemLanguage() {
    return mSystemLanguage;
  }

  public String getLanguage() {
    return mLanguage;
  }

  public EnduserApiInterface getEnduserApiInterface() {
    return mEnduserApiInterface;
  }

  public void setLanguage(String language) {
    mLanguage = language;
  }

  public void setEnduserApiInterface(EnduserApiInterface enduserApiInterface) {
    mEnduserApiInterface = enduserApiInterface;
  }

  public void setMorpheus(Morpheus morpheus) {
    mMorpheus = morpheus;
  }
}

