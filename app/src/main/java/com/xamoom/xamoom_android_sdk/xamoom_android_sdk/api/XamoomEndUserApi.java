package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api;

import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentBlocks.ResponseContentBlock;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentById;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentByLocationIdentifier;

import java.util.LinkedHashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.android.AndroidLog;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
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

    private static final String apiToken = "f01f9db7-c54d-4117-9161-6f0023b7057e";
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
     *
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
                Log.v(TAG, "Hellyeah!");
                listener.gotContentByLocationIdentifier(content);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.v(TAG, "Error Hellyeah: " + error);
            }
        });
    }

    /**
     *
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
                Log.v(TAG, "Hellyeah!");
                listener.gotContentById(content);
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
        @POST("/xamoomEndUserApi/v1/get_content_by_content_id")
        void getContentById(@QueryMap Map<String, String> params, Callback<ContentById> cb);

    }
}

