package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api;

import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentByLocationIdentifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
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
                .setConverter(new GsonConverter(gson))
                .build();

        api = restAdapter.create(XamoomApiInterface.class);
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

        api.getContentByLocationIdentifier(params, new Callback<Object>() {
            @Override
            public void success(Object content, Response response) {
                Log.v(TAG, "Debug Hellyeah: " + content);
                //listener.gotContentByLocationIdentifier(content);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.v(TAG, "Error Hellyeah: " + error);
            }
        });
    }

    //getter & setter

    public XamoomApiInterface getApi() {
        return api;
    }

    public void setListener(XamoomApiListener listener) {
        this.listener = listener;
    }

    private interface XamoomApiInterface {
        @Headers({
                "Accept: application/json",
                "User-Agent: xamoom-android-sdk",
                "Authorization: " + apiToken,
        })

        @POST("/xamoomEndUserApi/v1/get_content_by_location_identifier")
        void getContentByLocationIdentifier(@QueryMap Map<String, String> params, Callback<Object> cb);

        /*
        @POST("/xamoomEndUserApi/v1/get_content_by_id")
        void getContentById(@QueryMap Map<String, String> params, Callback<Object> cb);
        */
    }
}

