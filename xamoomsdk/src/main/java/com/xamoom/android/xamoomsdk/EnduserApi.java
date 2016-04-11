package com.xamoom.android.xamoomsdk;

import android.location.Location;
import android.support.annotation.Nullable;

import com.xamoom.android.xamoomsdk.Enums.ContentFlags;
import com.xamoom.android.xamoomsdk.Enums.ContentSortFlags;
import com.xamoom.android.xamoomsdk.Enums.SpotFlags;
import com.xamoom.android.xamoomsdk.Enums.SpotSortFlags;
import com.xamoom.android.xamoomsdk.Resource.Content;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;
import com.xamoom.android.xamoomsdk.Resource.Marker;
import com.xamoom.android.xamoomsdk.Resource.Menu;
import com.xamoom.android.xamoomsdk.Resource.Spot;
import com.xamoom.android.xamoomsdk.Resource.Style;
import com.xamoom.android.xamoomsdk.Resource.System;
import com.xamoom.android.xamoomsdk.Resource.SystemSetting;
import com.xamoom.android.xamoomsdk.Utils.JsonListUtil;
import com.xamoom.android.xamoomsdk.Utils.UrlUtil;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import at.rags.morpheus.Deserializer;
import at.rags.morpheus.Error;
import at.rags.morpheus.Morpheus;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * EnduserApi is the main part of the XamoomSDK. You can use it to send api request to
 * the xamoom cloud.
 *
 * Use {@link #EnduserApi(String)} to initialize.
 *
 * Change the requested language by  setting {@link #language}. The users language is saved
 * in {@link #systemLanguage}.
 */
public class EnduserApi {
  private static final String TAG = EnduserApi.class.getSimpleName();
  private static final String API_URL = "https://xamoom-cloud.appspot.com/";

  private EnduserApiInterface enduserApiInterface;
  private CallHandler callHandler;
  private Morpheus morpheus;
  private String language;
  private String systemLanguage;

  public EnduserApi(final String apikey) {
    OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
    builder.addInterceptor(new Interceptor() {
      @Override
      public okhttp3.Response intercept(Chain chain) throws IOException {
        Request request = chain.request().newBuilder()
            .addHeader("Content-Type", "application/vnd.api+json")
            .addHeader("APIKEY", apikey)
            .build();
        return chain.proceed(request);
      }
    });
    OkHttpClient httpClient = builder.build();

    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(API_URL)
        .client(httpClient)
        .build();
    enduserApiInterface = retrofit.create(EnduserApiInterface.class);

    initMorpheus();
    initVars();
  }

  public EnduserApi(Retrofit retrofit) {
    enduserApiInterface = retrofit.create(EnduserApiInterface.class);

    initMorpheus();
    initVars();
  }

  private void initMorpheus() {
    morpheus = new Morpheus();
    callHandler = new CallHandler(morpheus);

    Deserializer.registerResourceClass("contents", Content.class);
    Deserializer.registerResourceClass("content", Content.class);
    Deserializer.registerResourceClass("contentblocks", ContentBlock.class);
    Deserializer.registerResourceClass("spots", Spot.class);
    Deserializer.registerResourceClass("markers", Marker.class);
    Deserializer.registerResourceClass("systems", System.class);
    Deserializer.registerResourceClass("menus", Menu.class);
    Deserializer.registerResourceClass("settings", SystemSetting.class);
    Deserializer.registerResourceClass("styles", Style.class);
  }

  private void initVars() {
    systemLanguage = Locale.getDefault().getLanguage();
    language = systemLanguage;
  }

  /**
   * Get a content for a specific contentID.
   *
   * @param contentID ContentID from xamoom-cloud.
   * @param callback {@link APICallback}.
   */
  public void getContent(final String contentID, APICallback<Content, List<Error>> callback) {
    Map<String, String> params = UrlUtil.getUrlParameter(language);

    Call<ResponseBody> call = enduserApiInterface.getContent(contentID, params);
    callHandler.enqueCall(call, callback);
  }

  /**
   * Get a content for a specific contentID with possible flags.
   *
   * @param contentID ContentID from xamoom-cloud.
   * @param contentFlags Different flags {@link ContentFlags}.
   * @param callback {@link APICallback}.
   */
  public void getContent(String contentID, EnumSet<ContentFlags> contentFlags, APICallback<Content,
      List<at.rags.morpheus.Error>> callback) {
    Map<String, String> params = UrlUtil.addContentParameter(UrlUtil.getUrlParameter(language),
        contentFlags);

    Call<ResponseBody> call = enduserApiInterface.getContent(contentID, params);
    callHandler.enqueCall(call, callback);
  }

  /**
   * Get a content for a specific LocationIdentifier.
   *
   * @param locationIdentifier LocationIdentifier from QR or NFC.
   * @param callback {@link APICallback}.
   */
  public void getContentByLocationIdentifier(String locationIdentifier, APICallback<Content,
      List<Error>> callback) {
    Map<String, String> params = UrlUtil.getUrlParameter(language);
    params.put("filter[location-identifier]", locationIdentifier);

    Call<ResponseBody> call = enduserApiInterface.getContents(params);
    callHandler.enqueCall(call, callback);
  }

  /**
   * Get content for a specific beacon.
   *
   * @param major Beacon major ID.
   * @param minor Beacon minor ID.
   * @param callback {@link APICallback}.
   */
  public void getContentByBeacon(int major, int minor, APICallback<Content, List<Error>>
      callback) {
    getContentByLocationIdentifier(String.format("%s|%s", major, minor), callback);
  }

  /**
   * Get list of contents with your location. Geofence radius is 40m.
   *
   * @param location Users location.
   * @param pageSize PageSize for returned contents (max 100)
   * @param cursor Cursor for paging.
   * @param sortFlags {@link ContentSortFlags} to sort results.
   * @param callback {@link APIListCallback}.
   */
  public void getContentsByLocation(Location location, int pageSize, @Nullable String cursor,
                                    final EnumSet<ContentSortFlags> sortFlags,
                                    APIListCallback<List<Content>,
      List<Error>> callback) {
    Map<String, String> params = UrlUtil.addContentSortingParameter(UrlUtil.getUrlParameter(language),
        sortFlags);
    params = UrlUtil.addPagingToUrl(params, pageSize, cursor);
    params.put("filter[lat]", Double.toString(location.getLatitude()));
    params.put("filter[lon]", Double.toString(location.getLongitude()));

    Call<ResponseBody> call = enduserApiInterface.getContents(params);
    callHandler.enqueListCall(call, callback);
  }

  public void getContentsByTags(List<String> tags, int pageSize, @Nullable String cursor,
                                EnumSet<ContentSortFlags> sortFlags,
                                APIListCallback<List<Content>, List<Error>> callback) {
    Map<String, String> params = UrlUtil.addContentSortingParameter(UrlUtil.getUrlParameter(language),
        sortFlags);
    params = UrlUtil.addPagingToUrl(params, pageSize, cursor);
    params.put("filter[tags]", JsonListUtil.joinStringList(tags, ","));

    Call<ResponseBody> call = enduserApiInterface.getContents(params);
    callHandler.enqueListCall(call, callback);
  }

  /**
   * Get list of spots inside radius of a location.
   *
   * @param location User location.
   * @param radius Radius to search in meter (max 5000).
   * @param spotFlags {@link SpotFlags}.
   * @param sortFlags {@link SpotSortFlags}
   * @param callback {@link APIListCallback}
   */
  public void getSpotsByLocation(Location location, int radius, EnumSet<SpotFlags> spotFlags,
                                 @Nullable EnumSet<SpotSortFlags> sortFlags,
                                 APIListCallback<List<Spot>, List<Error>> callback) {
    getSpotsByLocation(location, radius, 0, null, spotFlags, sortFlags, callback);
  }

  /**
   * Get list of spots inside radius of a location.
   *
   * @param location User location.
   * @param radius Radius to search in meter (max 5000).
   * @param pageSize Size for pages. (max 100)
   * @param cursor Cursor of last search.
   * @param spotFlags {@link SpotFlags}.
   * @param sortFlags {@link SpotSortFlags}
   * @param callback {@link APIListCallback}
   */
  public void getSpotsByLocation(Location location, int radius, int pageSize, @Nullable String cursor,
                                 @Nullable EnumSet<SpotFlags> spotFlags,
                                 @Nullable EnumSet<SpotSortFlags> sortFlags,
                                 APIListCallback<List<Spot>, List<Error>> callback) {
    Map<String, String> params = UrlUtil.addSpotParameter(UrlUtil.getUrlParameter(language),
        spotFlags);
    params = UrlUtil.addSpotSortingParameter(params, sortFlags);
    params = UrlUtil.addPagingToUrl(params, pageSize, cursor);
    params.put("filter[lat]", Double.toString(location.getLatitude()));
    params.put("filter[lon]", Double.toString(location.getLongitude()));
    params.put("filter[radius]", Integer.toString(radius));

    Call<ResponseBody> call = enduserApiInterface.getSpots(params);
    callHandler.enqueListCall(call, callback);
  }

  /**
   * Get list of spots with tags.
   * Or operation used when searching with tags.
   *
   * @param tags List<String> with tag names
   * @param spotFlags {@link SpotFlags}.
   * @param sortFlags {@link SpotSortFlags}
   * @param callback {@link APIListCallback}
   */
  public void getSpotsByTags(List<String> tags, @Nullable EnumSet<SpotFlags> spotFlags,
                             @Nullable EnumSet<SpotSortFlags> sortFlags,
                             APIListCallback<List<Spot>, List<Error>> callback) {
    getSpotsByTags(tags, 0, null, spotFlags, sortFlags, callback);
  }

  /**
   * Get list of spots with with tags.
   * Or operation used when searching with tags.
   *
   * @param tags List<String> with tag names.
   * @param pageSize Size for pages. (max 100)
   * @param cursor Cursor of last search.
   * @param spotFlags {@link SpotFlags}.
   * @param sortFlags {@link SpotSortFlags}
   * @param callback {@link APIListCallback}
   */
  public void getSpotsByTags(List<String> tags, int pageSize, @Nullable String cursor,
                             @Nullable EnumSet<SpotFlags> spotFlags,
                             @Nullable EnumSet<SpotSortFlags> sortFlags,
                             APIListCallback<List<Spot>, List<Error>> callback) {
    Map<String, String> params = UrlUtil.addSpotParameter(UrlUtil.getUrlParameter(language),
        spotFlags);
    params = UrlUtil.addSpotSortingParameter(params, sortFlags);
    params = UrlUtil.addPagingToUrl(params, pageSize, cursor);
    params.put("filter[tags]", JsonListUtil.joinStringList(tags, ","));

    Call<ResponseBody> call = enduserApiInterface.getSpots(params);
    callHandler.enqueListCall(call, callback);
  }

  /**
   * Get the system connected to your api key.
   *
   * @param callback {@link APIListCallback}.
   */
  public void getSystem(final APICallback<System, List<Error>> callback) {
    Map<String, String> params = UrlUtil.getUrlParameter(language);
    Call<ResponseBody> call = enduserApiInterface.getSystem(params);
    callHandler.enqueCall(call, callback);
  }

  /**
   * Get the menu to your system.
   *
   * @param systemId Systems systemId.
   * @param callback {@link APIListCallback}.
   */
  public void getMenu(String systemId, final APICallback<Menu, List<Error>> callback) {
    Map<String, String> params = UrlUtil.getUrlParameter(language);
    Call<ResponseBody> call = enduserApiInterface.getMenu(systemId, params);
    callHandler.enqueCall(call, callback);
  }

  /**
   * Get the systemSettings to your system.
   *
   * @param systemId Systems systemId.
   * @param callback {@link APIListCallback}.
   */
  public void getSystemSetting(String systemId, final APICallback<SystemSetting, List<Error>> callback) {
    Map<String, String> params = UrlUtil.getUrlParameter(language);
    Call<ResponseBody> call = enduserApiInterface.getSetting(systemId, params);
    callHandler.enqueCall(call, callback);
  }

  /**
   * Get the style to your system.
   *
   * @param systemId Systems systemId.
   * @param callback {@link APIListCallback}.
   */
  public void getStyle(String systemId, final APICallback<Style, List<Error>> callback) {
    Map<String, String> params = UrlUtil.getUrlParameter(language);
    Call<ResponseBody> call = enduserApiInterface.getStyle(systemId, params);
    callHandler.enqueCall(call, callback);
  }

  //getters & setters
  public String getSystemLanguage() {
    return systemLanguage;
  }

  public String getLanguage() {
    return language;
  }

  public EnduserApiInterface getEnduserApiInterface() {
    return enduserApiInterface;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public CallHandler getCallHandler() {
    return callHandler;
  }
}

