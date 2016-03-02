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
import com.xamoom.android.xamoomsdk.Resource.Relationships.ContentRelationships;

import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

/**
 * EnduserApi is the main part of the XamoomSDK. You can use it to send api request to
 * the xamoom cloud.
 *
 * Use {@link #EnduserApi(String)} to initialize.
 *
 * Change the requested language by setting {@link #mLanguage}. The users language is saved
 * in {@link #mSystemLanguage}.
 */
public class EnduserApi {
  private static final String TAG = EnduserApi.class.getSimpleName();
  private static final String API_URL = "https://xamoom-cloud.appspot.com/_api/v2/consumer";

  private EnduserApiInterface mEnduserApiInterface;
  private String mLanguage;
  private String mSystemLanguage;

  public EnduserApi(final String apikey) {
    RestAdapter restAdapter = new RestAdapter.Builder()
        .setEndpoint(API_URL)
        .setRequestInterceptor(new RequestInterceptor() {
          @Override
          public void intercept(RequestFacade request) {
            request.addHeader("APIKEY", apikey);
          }
        })
        .setConverter(new GsonConverter(new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()))
        .build();

    mEnduserApiInterface = restAdapter.create(EnduserApiInterface.class);
    mSystemLanguage = Locale.getDefault().getLanguage();
    mLanguage = mSystemLanguage;
  }

  public EnduserApi(RestAdapter restAdapter) {
    mEnduserApiInterface = restAdapter.create(EnduserApiInterface.class);
    mSystemLanguage = Locale.getDefault().getLanguage();
    mLanguage = mSystemLanguage;
  }

  /**
   * Get a content for a specific contentID.
   *
   * @param contentID ContentID from xamoom-cloud.
   * @param callback APICallback.
   */
  public void getContent(final String contentID, final APICallback<Content, ErrorMessage> callback) {
    Map<String, String> params = getUrlParameter();

    mEnduserApiInterface.getContent(contentID, params, new ResponseCallback<JsonApiMessage<EmptyMessage,
        DataMessage<ContentAttributesMessage, ContentRelationships>,
        List<DataMessage<ContentBlockAttributeMessage, EmptyMessage>>>>() {

      @Override
      public void success(JsonApiMessage<EmptyMessage, DataMessage<ContentAttributesMessage,
          ContentRelationships>, List<DataMessage<ContentBlockAttributeMessage, EmptyMessage>>>
                              jsonApiMessage, Response response) {

        Content content = Content.createFromJsonApiMessage(jsonApiMessage);
        callback.finished(content);
      }

      @Override
      public void failure(ErrorMessage error) {
        callback.error(error);
      }
    });
  }

  /**
   * Get a content for a specific contentID with possible flags.
   *
   * @param contentID ContentID from xamoom-cloud.
   * @param contentFlags Different flags {@link ContentFlags}.
   * @param callback APICallback.
   */
  public void getContent(String contentID, EnumSet<ContentFlags> contentFlags, final APICallback<Content,
      ErrorMessage> callback) {
    Map<String, String> params = getUrlParameter(contentFlags);

    mEnduserApiInterface.getContent(contentID, params, new ResponseCallback<JsonApiMessage<EmptyMessage,
        DataMessage<ContentAttributesMessage, ContentRelationships>,
        List<DataMessage<ContentBlockAttributeMessage, EmptyMessage>>>>() {

      @Override
      public void success(JsonApiMessage<EmptyMessage, DataMessage<ContentAttributesMessage,
          ContentRelationships>, List<DataMessage<ContentBlockAttributeMessage, EmptyMessage>>>
                              jsonApiMessage, Response response) {

        Content content = Content.createFromJsonApiMessage(jsonApiMessage);
        callback.finished(content);
      }

      @Override
      public void failure(ErrorMessage error) {
        callback.error(error);
      }
    });
  }

  private Map<String, String> getUrlParameter() {
    Map<String, String> params = new LinkedHashMap<>();
    params.put("lang", mLanguage);
    return params;
  }

  private Map<String, String> getUrlParameter(EnumSet<ContentFlags> contentFlags) {
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
}

