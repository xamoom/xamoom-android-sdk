package com.xamoom.android;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xamoom.android.mapping.ContentBlocks.ContentBlock;
import com.xamoom.android.mapping.ContentById;
import com.xamoom.android.mapping.ContentByLocation;
import com.xamoom.android.mapping.ContentByLocationIdentifier;
import com.xamoom.android.mapping.ContentList;
import com.xamoom.android.mapping.SpotMap;
import com.xamoom.android.request.Location;
import com.xamoom.android.request.RequestByLocation;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.QueryMap;

/**
 * Use XamoomEndUserApi to communicate with the xamoom-cloud-api.
 *
 * For every call use the XamoomEndUserApi.getInstance() and add a method.
 * Every method has an APICallBack to return the result.
 *
 * For more information visit us on github: https://github.com/xamoom/xamoom-android-sdk
 *
 * @author Raphael Seher (xamoom GmbH)
 *
 * @version 0.1
 *
 */
public class XamoomEndUserApi {

    private static final String TAG = "XamoomEndUserApi";
    private static final String apiToken = "4552f99b-2b34-4f18-81a1-0911e25351d7";
    private static final String apiUrl = "https://xamoom-api-dot-xamoom-cloud.appspot.com/_ah/api/";
    private static final String apiUrlDev = "https://xamoom-api-dot-xamoom-cloud-dev.appspot.com/_ah/api/";
    private static XamoomEndUserApi api;

    private XamoomApiInterface apiInterface;
    private String systemLanguage;

    //constructor
    private XamoomEndUserApi() {
        //custom gsonBuilder
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(ContentBlock.class, new ContentBlockDeserializer())
                .create();

        //create restAdapter with custom gsonConverter
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(apiUrlDev)
                //.setLogLevel(RestAdapter.LogLevel.FULL)
                //.setLog(new AndroidLog(TAG))
                .setConverter(new GsonConverter(gson))
                .build();
        apiInterface = restAdapter.create(XamoomApiInterface.class);

        //save systemLanguage
        systemLanguage = Locale.getDefault().getLanguage();
    }

    /**
     * Returns a XamoomEndUserApi singleton.
     *
     * @return XamoomEndUserApi
     */
    public static XamoomEndUserApi getInstance() {
        if (api == null) {
            api = new XamoomEndUserApi();
        }
        return api;
    }

    //getter
    public String getSystemLanguage() {
        return systemLanguage;
    }

    /**
     * Returns the content with a specific contentId.
     *
     * @param contentId Content id of an xamoom content
     * @param style True for returning xamoom style, else false
     * @param menu True for returning xamoom menu, else false
     * @param language The language for the response (if available on xamoom-cloud), else systemLanguage. For german: "de". If null == systemLanguage.
     *
     * @see ContentById
     * @see com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.Style
     * @see com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.Menu
     * @since 1.0
     */
    public void getContentById(String contentId, boolean style, boolean menu, String language, final APICallback<ContentById> callback) {
        if (language == null)
            language = systemLanguage;

        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("content_id", contentId);
        params.put("include_style", style ? "True" : "False");
        params.put("include_menu", menu ? "True" : "False");
        params.put("language", language);

        apiInterface.getContentById(params, new Callback<ContentById>() {
            @Override
            public void success(ContentById content, Response response) {
                //Log.v(TAG, "Debug Hellyeah: " + content);
                callback.finished(content);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.v(TAG, "Error Hellyeah: " + error);
            }
        });
    }

    /**
     * Returns the content with a specific contentId.
     * With full = true you get every contentBlock. If full = false unsynchronised contents wont be send.
     *
     * @param contentId Content id of an xamoom content.
     * @param style True for returning xamoom style, else false.
     * @param menu True for returning xamoom menu, else false.
     * @param language The language for the response (if available on xamoom-cloud), else systemLanguage. For german: "de". If null == systemLanguage.
     *
     * @see ContentById
     * @see com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.Style
     * @see com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.Menu
     * @since 1.0
     */
    public void getContentbyIdFull(String contentId, boolean style, boolean menu, String language, boolean full, final APICallback<ContentById> callback) {
        if (language == null)
            language = systemLanguage;

        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("content_id", contentId);
        params.put("include_style", style ? "True" : "False");
        params.put("include_menu", menu ? "True" : "False");
        params.put("language", language);
        params.put("full", full ? "True" : "False");

        apiInterface.getContentByIdFull(params, new Callback<ContentById>() {
            @Override
            public void success(ContentById content, Response response) {
                //Log.v(TAG, "Debug Hellyeah: " + content);
                callback.finished(content);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.v(TAG, "Error Hellyeah: " + error);
            }
        });
    }

    /**
     * Returns the content connected to a locationIdentifier (QR or NFC).
     *
     * @param locationIdentifier LocationIdentifier from the scanned QR or NFC.
     * @param style True for returning xamoom style, else false.
     * @param menu True for returning xamoom menu, else false.
     * @param language The language for the response (if available on xamoom-cloud), else systemLanguage. For german: "de". If null == systemLanguage.
     *
     * @see ContentByLocationIdentifier
     * @see com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.Style
     * @see com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.Menu
     * @since 1.0
     */
    public void getContentByLocationIdentifier(String locationIdentifier, boolean style, boolean menu, String language, final APICallback<ContentByLocationIdentifier> callback) {
        if (language == null)
            language = systemLanguage;

        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("location_identifier", locationIdentifier);
        params.put("include_style", style ? "True" : "False");
        params.put("include_menu", menu ? "True" : "False");
        params.put("language", language);

        apiInterface.getContentByLocationIdentifier(params, new Callback<ContentByLocationIdentifier>() {
            @Override
            public void success(ContentByLocationIdentifier content, Response response) {
                //Log.v(TAG, "Debug Hellyeah: " + content);
                callback.finished(content);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.v(TAG, "Error Hellyeah: " + error);
            }
        });
    }

    /**
     * Returns a list of contents in a 40 meter radius.
     *
     * @param lat The latitude of a location (for example user location).
     * @param lon The longitude of a location (for example user location).
     * @param language The language for the response (if available on xamoom-cloud), else systemLanguage. For german: "de". If null == systemLanguage.
     *
     * @see ContentByLocation
     * @since 1.0
     */
    public void getContentByLocation(double lat, double lon, String language, final APICallback<ContentByLocation> callback) {
        if (language == null)
            language = systemLanguage;

        Location location = new Location(lat, lon);
        RequestByLocation params = new RequestByLocation(location, language);

        apiInterface.getContentWithLocation(params, new Callback<ContentByLocation>() {
            @Override
            public void success(ContentByLocation content, Response response) {
                //Log.v(TAG, "Debug Hellyeah: " + content);
                callback.finished(content);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.v(TAG, "Error Hellyeah: " + error);
            }
        });
    }

    /**
     * Geofence analytics, call when displaying a <code>getContentByLocation()</code>.
     *
     * @param requestedLanguage The language you requested.
     * @param deliveredLanguage The returned language.
     * @param systemId The returned systemId.
     * @param systemName The returned systemName.
     * @param contentId The returned contentId.
     * @param contentName The returned contentName.
     * @param spotId The returned spotId.
     * @param spotName The returned spotName.
     *
     * @see #getContentByLocation(double, double, String, APICallback)
     * @since 1.0
     */
    public void queueGeofenceAnalytics(String requestedLanguage, String deliveredLanguage, String systemId, String systemName, String contentId, String contentName, String spotId, String spotName) {
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("requested_language", requestedLanguage);
        params.put("delivered_language", deliveredLanguage);
        params.put("system_id", systemId);
        params.put("system_name", systemName);
        params.put("content_id", contentId);
        params.put("content_name", contentName);
        params.put("spot_id", spotId);
        params.put("spot_name", spotName);

        apiInterface.queueGeofenceAnalytics(params, new Callback<Object>() {
            @Override
            public void success(Object o, Response response) {
                Log.v(TAG, "Success Hellyeah, Status " + response.getStatus());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.v(TAG, "Error Hellyeah: " + error);
            }
        });
    }

    /**
     * Returns a list of spots.
     * For example for a map.
     *
     * @param systemId When calling with an API Key, can be 0.
     * @param mapTags  StringArray with mapTags you want to display.
     * @param language The language for the response (if available on xamoom-cloud), else systemLanguage. For german: "de". If null == systemLanguage.

     * @see SpotMap
     * @since 1.0
     */
    public void getSpotMap(String systemId, String[] mapTags, String language, final APICallback<SpotMap> callback) {
        if (language == null)
            language = systemLanguage;

        if(systemId == null) {
            systemId = "0";
        }

        apiInterface.getSpotMap(systemId, TextUtils.join(",", mapTags), language, new Callback<SpotMap>() {
            @Override
            public void success(SpotMap result, Response response) {
                //Log.v(TAG, "Debug Hellyeah: " + content);
                callback.finished(result);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.v(TAG, "Error Hellyeah: " + error);
            }
        });
    }

    /**
     * Returns a list of spots near a location with a radius.
     *
     * @param lat The latitude of a location (for example user location).
     * @param lon The longitude of a location (for example user location).
     * @param language The language for the response (if available on xamoom-cloud), else systemLanguage. For german: "de". If null == systemLanguage.
     * @param radius A search radius in meter
     * @param limit A limit for results
     *
     * @see SpotMap
     * @since 1.0
     */
    public void getClosestSpots(double lat, double lon, String language, int radius, int limit, final APICallback<SpotMap> callback) {
        Location location = new Location(lat, lon);
        RequestByLocation params = new RequestByLocation(location, language);
        params.setRadius(radius);
        params.setLimit(limit);

        apiInterface.getClosestSpots(params, new Callback<SpotMap>() {
            @Override
            public void success(SpotMap result, Response response) {
                //Log.v(TAG, "Debug Hellyeah: " + result);
                callback.finished(result);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.v(TAG, "Error Hellyeah: " + error);
            }
        });
    }

    /**
     * Returns a list of contents ordered desc of specific tags.
     *
     * @param language The language for the response (if available), else systemLanguage.
     * @param pageSize Number of returned items.
     * @param cursor Send for paging, else null.
     * @param tags Tags to filter.
     *
     * @see ContentList
     * @since 1.0
     */
    public void getContentList(String language, int pageSize, String cursor, String[] tags, final APICallback<ContentList> callback) {
        if (language == null)
            language = systemLanguage;

        if (cursor == null) {
            cursor = "null";
        }

        apiInterface.getContentList(language, pageSize, cursor, TextUtils.join(",", tags), new Callback<ContentList>() {
            @Override
            public void success(ContentList result, Response response) {
                //Log.v(TAG, "Debug Hellyeah: " + result);
                callback.finished(result);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.v(TAG, "Error Hellyeah: " + error);
            }
        });
    }

    /*
     * XamoomApiInterface powered by retrofit.
     */
    private interface XamoomApiInterface {

        /**
         * Post to /xamoomEndUserApi/v1/get_content_by_content_id.
         *
         * @param params Map with the parameters for the post.
         * @param cb Callback-Method with the result as ContentById.
         *
         * @see ContentById
         */
        @Headers({
                "Accept: application/json",
                "User-Agent: xamoom-android-sdk",
                "Authorization: " + apiToken
        })
        @POST("/xamoomEndUserApi/v1/get_content_by_content_id")
        void getContentById(@QueryMap Map<String, String> params, Callback<ContentById> cb);

        /**
         * Post to /xamoomEndUserApi/v1/get_content_by_content_id_full.
         * @param params Map with the parameters for the post.
         * @param cb Callback-Method with the result as ContentById.
         *
         * @see ContentById
         */
        @Headers({
                "Accept: application/json",
                "User-Agent: xamoom-android-sdk",
                "Authorization: " + apiToken
        })
        @POST("/xamoomEndUserApi/v1/get_content_by_content_id_full")
        void getContentByIdFull(@QueryMap Map<String, String> params, Callback<ContentById> cb);

        /**
         * Post to /xamoomEndUserApi/v1/get_content_by_location_identifier.
         *
         * @param params Map with the parameters for the post.
         * @param cb Callback-Method with the result as ContentByLocationIdentifier.
         *
         * @see ContentByLocationIdentifier
         */
        @Headers({
                "Accept: application/json",
                "User-Agent: xamoom-android-sdk",
                "Authorization: " + apiToken
        })
        @POST("/xamoomEndUserApi/v1/get_content_by_location_identifier")
        void getContentByLocationIdentifier(@QueryMap Map<String, String> params, Callback<ContentByLocationIdentifier> cb);


        /**
         * Post to /xamoomEndUserApi/v1/get_content_by_location.
         *
         * @param params Map with the parameters for the post.
         * @param cb Callback-Method with the result as ContentByLocation.
         *
         * @see ContentByLocation
         */
        @Headers({
                "Accept: application/json",
                "User-Agent: xamoom-android-sdk",
                "Authorization: " + apiToken
        })
        @POST("/xamoomEndUserApi/v1/get_content_by_location")
        void getContentWithLocation(@Body RequestByLocation params, Callback<ContentByLocation> cb);

        /**
         * Post to /xamoomEndUserApi/v1/queue_geofence_analytics.
         *
         * @param params Map with the parameters for the post.
         * @param cb Callback-Method with the result as ContentByLocation.
         */
        @Headers({
                "Accept: application/json",
                "User-Agent: xamoom-android-sdk",
                "Authorization: " + apiToken
        })
        @POST("/xamoomEndUserApi/v1/queue_geofence_analytics")
        void queueGeofenceAnalytics(@QueryMap Map<String, String> params, Callback<Object> cb);

        /**
         * Get to /xamoomEndUserApi/v1/spotmap/{system_id}/{map_tag}/{language}.
         * @param systemId The systemId of the system.
         * @param mapTags MapTags seperated with comma.
         * @param language The language for the response.
         * @param cb Callback-Method with the result as ContentByLocation.
         *
         * @see SpotMap
         */
        @Headers({
                "Accept: application/json",
                "User-Agent: xamoom-android-sdk",
                "Authorization: " + apiToken
        })
        @GET("/xamoomEndUserApi/v1/spotmap/{system_id}/{map_tag}/{language}")
        void getSpotMap(@Path("system_id") String systemId, @Path("map_tag") String mapTags, @Path("language") String language, Callback<SpotMap> cb);

        /**
         * Post to /xamoomEndUserApi/v1/get_closest_spots.
         *
         * @param params Map with the parameters for the post.
         * @param cb Callback-Method with the result as ContentByLocation.
         *
         * @see SpotMap
         */
        @Headers({
                "Accept: application/json",
                "User-Agent: xamoom-android-sdk",
                "Authorization: " + apiToken
        })
        @POST("/xamoomEndUserApi/v1/get_closest_spots")
        void getClosestSpots(@Body RequestByLocation params, Callback<SpotMap> cb);

        /**
         * Get to /xamoomEndUserApi/v1/content_list/{language}/{page_size}/{cursor}/{tags}.
         *
         * @param language The language for the response.
         * @param pageSize Number of returned items.
         * @param cursor Send for paging, else null.
         * @param tags Tags to filter.
         * @param cb Callback-Method with the result as ContentByLocation.
         *
         * @see ContentList
         */
        @Headers({
                "Accept: application/json",
                "User-Agent: xamoom-android-sdk",
                "Authorization: " + apiToken
        })
        @GET("/xamoomEndUserApi/v1/content_list/{language}/{page_size}/{cursor}/{tags}")
        void getContentList(@Path("language") String language, @Path("page_size") int pageSize, @Path("cursor") String cursor, @Path("tags") String tags, Callback<ContentList> cb);
    }
}
