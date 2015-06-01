package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api;

import android.app.DownloadManager;
import android.location.Location;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentBlocks.ResponseContentBlock;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentById;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentByLocation;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentByLocationIdentifier;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.android.AndroidLog;
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

    private XamoomApiInterface api;
    private XamoomApiListener listener;
    private String systemLanguage;

    public XamoomEndUserApi() {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(apiUrlDev)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                //.setLog(new AndroidLog(TAG))
                .setConverter(new GsonConverter(gson))
                .build();

        api = restAdapter.create(XamoomApiInterface.class);
    }

    //getter & setter
    public XamoomApiInterface getApi() {
        return api;
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

        api.getContentById(params, new Callback<ContentById>() {
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

        api.getContentByIdFull(params, new Callback<ContentById>() {
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

        api.getContentByLocationIdentifier(params, new Callback<ContentByLocationIdentifier>() {
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
     *
     */
    public void getContentByLocation(double lat, double lon, String language) {

        XLocation location = new XLocation(lat, lon);
        request params = new request(location, language);


        Log.v(TAG, "Debug Hellyeah: " + params);

        api.getContentWithLocation(params, new Callback<ContentByLocation>() {
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
        void getContentWithLocation(@Body request params, Callback<ContentByLocation> cb);
    }
}


class request {
    private XLocation location;
    private String language;

    public request (XLocation location, String language) {
        this.location = location;
        this.language = language;
    }
}

class XLocation {
    private double lat;
    private double lon;

    public XLocation (double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }
}
