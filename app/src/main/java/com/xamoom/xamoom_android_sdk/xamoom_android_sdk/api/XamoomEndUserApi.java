package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentById;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentByLocation;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentByLocationIdentifier;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ResponseSpotMap;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.request.APILocation;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.request.APIRequestByLocation;

import java.lang.reflect.Array;
import java.util.LinkedHashMap;
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
 *
 *
 */
public class XamoomEndUserApi {

    private static final String TAG = "XamoomEndUserApi";
    private static final String apiToken = "24ba39ae-c7ea-11e4-8731-1681e6b88ec1";
    private static final String apiUrl = "https://xamoom-api-dot-xamoom-cloud.appspot.com/_ah/api/";
    private static final String apiUrlDev = "https://xamoom-api-dot-xamoom-cloud-dev.appspot.com/_ah/api/";

    //singleton variable
    private static XamoomEndUserApi api;

    private XamoomApiInterface apiInterface;
    private XamoomApiListener listener;
    private String systemLanguage;

    //constructor
    private XamoomEndUserApi() {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(apiUrlDev)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                //.setLog(new AndroidLog(TAG))
                .setConverter(new GsonConverter(gson))
                .build();

        apiInterface = restAdapter.create(XamoomApiInterface.class);
    }

    //singleton
    public static XamoomEndUserApi getInstance() {
        if (api == null) {
            api = new XamoomEndUserApi();
        }
        return api;
    }

    //getter & setter
    public XamoomApiInterface getApi() {
        return apiInterface;
    }

    public void setListener(XamoomApiListener listener) {
        this.listener = listener;
    }

    /**
     * Returns the content with a specific contentId.
     *
     * @param contentId
     * @param style
     * @param menu
     * @param language
     */
    public void getContentById(String contentId, boolean style, boolean menu, String language) {
        LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
        params.put("content_id", contentId);
        params.put("include_style", style ? "True" : "False");
        params.put("include_menu", menu ? "True" : "False");
        params.put("language", language);

        apiInterface.getContentById(params, new Callback<ContentById>() {
            @Override
            public void success(ContentById content, Response response) {
                //Log.v(TAG, "Debug Hellyeah: " + content);
                listener.gotContentById(content);
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
     * @param contentId
     * @param style
     * @param menu
     * @param language
     */
    public void getContentbyIdFull(String contentId, boolean style, boolean menu, String language, boolean full) {
        LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
        params.put("content_id", contentId);
        params.put("include_style", style ? "True" : "False");
        params.put("include_menu", menu ? "True" : "False");
        params.put("language", language);
        params.put("full", full ? "True" : "False");

        apiInterface.getContentByIdFull(params, new Callback<ContentById>() {
            @Override
            public void success(ContentById content, Response response) {
                //Log.v(TAG, "Debug Hellyeah: " + content);
                listener.gotContentById(content);
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
     * @param locationIdentifier
     * @param style
     * @param menu
     * @param language
     */
    public void getContentByLocationIdentifier(String locationIdentifier, boolean style, boolean menu, String language) {
        LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
        params.put("location_identifier", locationIdentifier);
        params.put("include_style", style ? "True" : "False");
        params.put("include_menu", menu ? "True" : "False");
        params.put("language", language);

        apiInterface.getContentByLocationIdentifier(params, new Callback<ContentByLocationIdentifier>() {
            @Override
            public void success(ContentByLocationIdentifier content, Response response) {
                //Log.v(TAG, "Debug Hellyeah: " + content);
                listener.gotContentByLocationIdentifier(content);
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
     * @param lat
     * @param lon
     * @param language
     */
    public void getContentByLocation(double lat, double lon, String language) {
        APILocation location = new APILocation(lat, lon);
        APIRequestByLocation params = new APIRequestByLocation(location, language);

        apiInterface.getContentWithLocation(params, new Callback<ContentByLocation>() {
            @Override
            public void success(ContentByLocation content, Response response) {
                //Log.v(TAG, "Debug Hellyeah: " + content);
                listener.gotContentByLocation(content);
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
     * @param requestedLanguage
     * @param deliveredLanguage
     * @param systemId
     * @param systemName
     * @param contentId
     * @param contentName
     * @param spotId
     * @param spotName
     */
    public void queueGeofenceAnalytics(String requestedLanguage, String deliveredLanguage, String systemId, String systemName, String contentId, String contentName, String spotId, String spotName) {
        LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
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
     *
     * @param systemId - when calling with an API Key, can be 0
     * @param mapTags - StringArray with mapTags you want to display
     * @param language
     */
    public void getSpotMap(String systemId, String[] mapTags, String language) {


        apiInterface.getSpotMap(systemId, TextUtils.join(",", mapTags), language, new Callback<ResponseSpotMap>() {
            @Override
            public void success(ResponseSpotMap result, Response response) {
                //Log.v(TAG, "Debug Hellyeah: " + content);
                listener.gotSpotMap(result);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.v(TAG, "Error Hellyeah: " + error);
            }
        });
    }

    private interface XamoomApiInterface {

        /**
         *
         */
        @Headers({
                "Accept: application/json",
                "User-Agent: xamoom-android-sdk",
                "Authorization: " + apiToken
        })
        @POST("/xamoomEndUserApi/v1/get_content_by_content_id")
        void getContentById(@QueryMap Map<String, String> params, Callback<ContentById> cb);

        /**
         *
         */
        @Headers({
                "Accept: application/json",
                "User-Agent: xamoom-android-sdk",
                "Authorization: " + apiToken
        })
        @POST("/xamoomEndUserApi/v1/get_content_by_content_id_full")
        void getContentByIdFull(@QueryMap Map<String, String> params, Callback<ContentById> cb);

        /**
         *
         */
        @Headers({
                "Accept: application/json",
                "User-Agent: xamoom-android-sdk",
                "Authorization: " + apiToken
        })
        @POST("/xamoomEndUserApi/v1/get_content_by_location_identifier")
        void getContentByLocationIdentifier(@QueryMap Map<String, String> params, Callback<ContentByLocationIdentifier> cb);


        /**
         *
         */
        @Headers({
                "Accept: application/json",
                "User-Agent: xamoom-android-sdk",
                "Authorization: " + apiToken
        })
        @POST("/xamoomEndUserApi/v1/get_content_by_location")
        void getContentWithLocation(@Body APIRequestByLocation params, Callback<ContentByLocation> cb);

        /**
         *
         */
        @Headers({
                "Accept: application/json",
                "User-Agent: xamoom-android-sdk",
                "Authorization: " + apiToken
        })
        @POST("/xamoomEndUserApi/v1/queue_geofence_analytics")
        void queueGeofenceAnalytics(@QueryMap Map<String, String> params, Callback<Object> cb);

        /**
         *
         */
        @Headers({
                "Accept: application/json",
                "User-Agent: xamoom-android-sdk",
                "Authorization: " + apiToken
        })
        @GET("/xamoomEndUserApi/v1/spotmap/{system_id}/{map_tag}/{language}")
        void getSpotMap(@Path("system_id") String systemId, @Path("map_tag") String mapTags, @Path("language") String language, Callback<ResponseSpotMap> cb);
    }
}
