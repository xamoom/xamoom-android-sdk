/*
 * Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the root of this project.
 */

package com.xamoom.android.xamoomsdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;

import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor;
import com.xamoom.android.xamoomsdk.Enums.ContentFlags;
import com.xamoom.android.xamoomsdk.Enums.ContentReason;
import com.xamoom.android.xamoomsdk.Enums.ContentSortFlags;
import com.xamoom.android.xamoomsdk.Enums.SpotFlags;
import com.xamoom.android.xamoomsdk.Enums.SpotSortFlags;
import com.xamoom.android.xamoomsdk.Offline.OfflineEnduserApi;
import com.xamoom.android.xamoomsdk.PushDevice.PushDevice;
import com.xamoom.android.xamoomsdk.PushDevice.PushDeviceUtil;
import com.xamoom.android.xamoomsdk.Resource.Content;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;
import com.xamoom.android.xamoomsdk.Resource.Marker;
import com.xamoom.android.xamoomsdk.Resource.Menu;
import com.xamoom.android.xamoomsdk.Resource.NewVoucherStatusMessage;
import com.xamoom.android.xamoomsdk.Resource.Spot;
import com.xamoom.android.xamoomsdk.Resource.Style;
import com.xamoom.android.xamoomsdk.Resource.System;
import com.xamoom.android.xamoomsdk.Resource.SystemSetting;
import com.xamoom.android.xamoomsdk.Utils.JsonListUtil;
import com.xamoom.android.xamoomsdk.Utils.UrlUtil;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import at.rags.morpheus.Deserializer;
import at.rags.morpheus.Error;
import at.rags.morpheus.JsonApiObject;
import at.rags.morpheus.Morpheus;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * EnduserApi is the main part of the XamoomSDK. You can use it to send api request to
 * the xamoom cloud.
 *
 * Use {@link #EnduserApi(String, Context, String)} to initialize.
 *
 * Change the requested language by  setting {@link #language}. The users language is saved
 * in {@link #systemLanguage}.
 *
 * Get local saved data by setting {@link #setOffline(boolean)} to true. Data must be saved
 * using {@link com.xamoom.android.xamoomsdk.Storage.OfflineStorageManager}.
 */
public class EnduserApi implements CallHandler.CallHandlerListener {
    private static final String TAG = EnduserApi.class.getSimpleName();
    private static final String API_URL_DEV = "https://xamoom-dev.appspot.com";
    private static final String API_URL_PROD = "https://api.xamoom.net";
    private static final String PREF_EPHEMERAL_ID = "com.xamoom.android.xamoomsdk.ephemeralid";
    private static final String PREF_AUTHORIZATION_ID = "com.xamoom.android.xamoomsdk.authorization";

    public static final String PREF_ENDUSER_API_KEY = "com.xamoom.android.xamoomsdk.api.key";
    public static final String PREF_ENDUSER_API_KEY_IS_PRODUCTION = "com.xamoom.android.xamoomsdk.is.production";
    public static final String PREF_ENDUSER_API_KEY_MAJOR_ID = "com.xamoom.android.xamoomsdk.major.id";
    public static final String PREF_ENDUSER_BEACON_COOLDOWN = "com.xamoom.android.xamoomsdk.beacon.cooldown";

    private static EnduserApi sharedInstance;

    private final Context context;
    private String apiKey;
    private EnduserApiInterface enduserApiInterface;
    private OfflineEnduserApi offlineEnduserApi;
    private CallHandler callHandler;
    private String language;
    private String systemLanguage;
    private boolean offline;
    private SharedPreferences sharedPref;
    private String ephemeralId;
    private String authorizationId;
    private String apiUrl;
    private String majorId;
    private int cooldown;
    private Location lastLocation;

    public EnduserApi(@NonNull final String apikey, @NonNull Context context, @Nullable String majorId, @Nullable int cooldown) {
        this.apiKey = apikey;
        this.context = context;
        this.offlineEnduserApi = new OfflineEnduserApi(context);
        this.majorId = majorId;
        this.cooldown = cooldown;

        if (this.lastLocation == null) {
            Location defaultLocation = new Location("");
            defaultLocation.setLatitude(0.0);
            defaultLocation.setLongitude(0.0);
            this.lastLocation = defaultLocation;
        }

        apiUrl = API_URL_PROD;

        initRetrofit(apiKey);
        initMorpheus();
        initSharedPreferences();
        initVars();

        sharedInstance = this;
        preserveEnduserApi(apiKey, true, majorId);
    }

    public EnduserApi(@NonNull final String apikey, @NonNull Context context, boolean isProduction, @Nullable String majorId, @Nullable int cooldown) {
        this.apiKey = apikey;
        this.context = context;
        this.offlineEnduserApi = new OfflineEnduserApi(context);
        this.majorId = majorId;
        this.cooldown = cooldown;

        if (this.lastLocation == null) {
            Location defaultLocation = new Location("");
            defaultLocation.setLatitude(0.0);
            defaultLocation.setLongitude(0.0);
            this.lastLocation = defaultLocation;
        }

        if (isProduction) {
            apiUrl = API_URL_PROD;
        } else {
            apiUrl = API_URL_DEV;
        }

        initRetrofit(apiKey);
        initMorpheus();
        initSharedPreferences();
        initVars();

        sharedInstance = this;
        preserveEnduserApi(apiKey, isProduction, majorId);
    }

    public EnduserApi(@NonNull Retrofit retrofit, @NonNull Context context) {
        this.enduserApiInterface = retrofit.create(EnduserApiInterface.class);
        this.context = context;
        this.offlineEnduserApi = new OfflineEnduserApi(context);

        if (this.lastLocation == null) {
            Location defaultLocation = new Location("");
            defaultLocation.setLatitude(0.0);
            defaultLocation.setLongitude(0.0);
            this.lastLocation = defaultLocation;
        }

        initMorpheus();
        initSharedPreferences();
        initVars();
        sharedInstance = this;
    }

    private void preserveEnduserApi(String key, boolean isProduction, @Nullable String beaconMajorId) {
        sharedPref.edit().putString(PREF_ENDUSER_API_KEY, key).putBoolean(PREF_ENDUSER_API_KEY_IS_PRODUCTION, isProduction).putString(PREF_ENDUSER_API_KEY_MAJOR_ID, beaconMajorId).putInt(PREF_ENDUSER_BEACON_COOLDOWN, cooldown).apply();
    }

    private static EnduserApi restoreEnduserApi(Context context) {
        String preferencesName = String.format("%s.xamoomsdk.preferences",
                context.getApplicationInfo().loadLabel(context.getPackageManager()).toString());
        SharedPreferences sharedPref = context.getSharedPreferences(preferencesName,
                Context.MODE_PRIVATE);

        String apiKey = sharedPref.getString(PREF_ENDUSER_API_KEY, null);
        Boolean isProduction = sharedPref.getBoolean(PREF_ENDUSER_API_KEY_IS_PRODUCTION, true);
        String majorId = sharedPref.getString(PREF_ENDUSER_API_KEY_MAJOR_ID, null);
        int cooldown = sharedPref.getInt(PREF_ENDUSER_BEACON_COOLDOWN, 0);
        if (apiKey == null) {
            return null;
        }

        return new EnduserApi(apiKey, context, isProduction, majorId, cooldown);
    }

    private void initRetrofit(@NonNull final String apiKey) {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(new OkHttpProfilerInterceptor());
        }
        builder.addInterceptor(new HTTPHeaderInterceptor(generateUserAgent(), apiKey));
        OkHttpClient httpClient = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient)
                .baseUrl(apiUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        enduserApiInterface = retrofit.create(EnduserApiInterface.class);
    }

    private void initMorpheus() {
        Morpheus morpheus = new Morpheus();
        callHandler = new CallHandler(morpheus);
        callHandler.setListener(this);

        Deserializer.registerResourceClass("contents", Content.class);
        Deserializer.registerResourceClass("content", Content.class);
        Deserializer.registerResourceClass("contentblocks", ContentBlock.class);
        Deserializer.registerResourceClass("spots", Spot.class);
        Deserializer.registerResourceClass("markers", Marker.class);
        Deserializer.registerResourceClass("systems", System.class);
        Deserializer.registerResourceClass("menus", Menu.class);
        Deserializer.registerResourceClass("settings", SystemSetting.class);
        Deserializer.registerResourceClass("styles", Style.class);
        Deserializer.registerResourceClass("push-device", PushDevice.class);
        Deserializer.registerResourceClass("voucher-status", NewVoucherStatusMessage.class);
    }

    private void initVars() {
        systemLanguage = Locale.getDefault().getLanguage();
        language = systemLanguage;
    }

    private void initSharedPreferences() {
        if (context == null) {
            throw new NullPointerException("The given context is null.");
        }
        // generate unique name for preferences => {appid}.xamoomsdk.preferences
        String preferencesName = String.format("%s.xamoomsdk.preferences",
                context.getApplicationInfo().loadLabel(context.getPackageManager()).toString());
        sharedPref = context.getSharedPreferences(preferencesName,
                Context.MODE_PRIVATE);
    }

    /**
     * Get a content for a specific contentID.
     *
     * @param contentID ContentID from xamoom-cloud
     * @param callback {@link APICallback}
     * @return Used call object
     */
    public Call getContent(String contentID, String password, APIPasswordCallback<Content,  List<Error>> callback) {
        return getContent(contentID, null, null, password, callback);
    }

    /**
     * Get a content for a specific contentID with possible flags.
     *
     * @param contentID ContentID from xamoom-cloud
     * @param contentFlags Different flags {@link ContentFlags}
     * @param callback {@link APICallback}
     * @return Used call object
     */
    public Call getContent(String contentID, EnumSet<ContentFlags> contentFlags, String password, APIPasswordCallback<Content,
            List<at.rags.morpheus.Error>> callback) {
        return getContent(contentID, contentFlags, null, password, callback);
    }

    /**
     * Get a content for a specific contentID with possible flags.
     *
     * @param contentID ContentID from xamoom-cloud
     * @param contentFlags Different flags {@link ContentFlags}
     * @param reason ContentReason to get better analytics in the xamoom dashboard
     * @param callback {@link APICallback}
     * @return Used call object
     */
    public Call getContent(final String contentID, final EnumSet<ContentFlags> contentFlags, final ContentReason reason, String password,
                           final APIPasswordCallback<Content, List<at.rags.morpheus.Error>> callback) {
        boolean isNeedToUpdateCache = offlineEnduserApi.isNeedToUpdateContentCache(contentID);
        if (offline) {
            offlineEnduserApi.getContent(contentID, callback);
            return null;
        }

        Map<String, String> params = UrlUtil.addContentParameter(UrlUtil.getUrlParameter(language),
                contentFlags);

        HashMap<String, String> headers = new HashMap<>(3);
        if (getEphemeralId() != null) {
            headers.put(EnduserApiInterface.HEADER_EPHEMERAL, getEphemeralId());
        }
        if (getAuthorizationId() != null) {
            headers.put(EnduserApiInterface.HEADER_AUTHORIZATION, getAuthorizationId());
        }
        if (reason != null) {
            headers.put(EnduserApiInterface.HEADER_REASON, String.valueOf(reason.getValue()));
        }

        if (password != null) {
            int contentIdOpens = sharedPref.getInt(contentID, 0);
            sharedPref.edit().putInt(contentID, contentIdOpens + 1).apply();
            headers.put(EnduserApiInterface.HEADER_PASSWORD, password);
        }

        Call<ResponseBody> call = enduserApiInterface.getContent(headers, contentID, params);

        APIPasswordCallback<Content, List<Error>> passwordCallback = new APIPasswordCallback<Content, List<Error>>() {
            @Override
            public void finished(Content result) {
                sharedPref.edit().putFloat(contentID+"_next_open", -1).apply();
                sharedPref.edit().putInt(contentID,  0).apply();
                callback.finished(result);
            }

            @Override
            public void error(final List<Error> error) {
                Error e = error.get(0);
                if (e.getCode().equals("93") && e.getStatus().equals("404")) {
                    ArrayList<String> tags = new ArrayList<String>();
                    tags.add("x-forbidden");

                    getContentsByTags(tags, 1, null, null, new APIListCallback<List<Content>, List<Error>>() {
                        @Override
                        public void finished(List<Content> result, String cursor, boolean hasMore) {
                            if (result.size() > 0) {
                                callback.finished(result.get(0));
                            } else {
                                callback.finished(null);
                            }
                        }

                        @Override
                        public void error(List<Error> error) {
                            callback.error(error);
                        }
                    });
                } else {
                    callback.error(error);
                }
            }

            @Override
            public void passwordRequested() {
                int contentIdOpens = sharedPref.getInt(contentID, 0);
                long contentNextOpen = (long) sharedPref.getFloat(contentID+"_next_open", -1);

                Date n = new Date();
                long now = n.getTime();
                long diff = TimeUnit.MILLISECONDS.toSeconds(now) - (15 * 60);
                long contentNextOpenSeconds = -1;
                if (contentNextOpen != -1) {
                    contentNextOpenSeconds = TimeUnit.MILLISECONDS.toSeconds(contentNextOpen);
                }

                if ((contentNextOpenSeconds == -1 || diff > contentNextOpenSeconds) && contentIdOpens <= 2) {
                    callback.passwordRequested();
                } else {
                    ArrayList<String> tags = new ArrayList<String>();
                    tags.add("x-forbidden");

                    getContentsByTags(tags, 1, null, null, new APIListCallback<List<Content>, List<Error>>() {
                        @Override
                        public void finished(List<Content> result, String cursor, boolean hasMore) {
                            if (result.size() > 0) {
                                callback.finished(result.get(0));
                            } else {
                                callback.finished(null);
                            }
                        }

                        @Override
                        public void error(List<Error> error) {
                            callback.error(error);
                        }
                    });
                    sharedPref.edit().putFloat(contentID+"_next_open", now).apply();
                    sharedPref.edit().putInt(contentID,  0).apply();
                }
            }
        };

        callHandler.enquePasswordCall(call, passwordCallback);
        return call;
    }

    /**
     * Get a content for a specific LocationIdentifier.
     *
     * @param locationIdentifier LocationIdentifier from QR or NFC
     * @param callback {@link APICallback}
     * @return Used call object
     */
    public Call getContentByLocationIdentifier(String locationIdentifier, String password, APIPasswordCallback<Content,
            List<Error>> callback) {
        return getContentByLocationIdentifier(locationIdentifier, null, password, callback);
    }

    /**
     *  Get a content for a specific LocationIdentifier with flags.
     *
     * @param locationIdentifier LocationIdentifier from QR or NFC
     * @param contentFlags Different flags {@link ContentFlags}
     * @param callback {@link APICallback}
     * @return Used call object
     */
    public Call getContentByLocationIdentifier(String locationIdentifier,
                                               EnumSet<ContentFlags> contentFlags,
                                               String password,
                                               APIPasswordCallback<Content, List<Error>> callback) {
        return getContentByLocationIdentifier(locationIdentifier, contentFlags, null, password, callback);
    }

    /**
     *  Get a content for a specific LocationIdentifier with flags.
     *
     * @param locationIdentifier LocationIdentifier from QR or NFC
     * @param contentFlags Different flags {@link ContentFlags}
     * @param conditions  HashMap with conditions to match. Allowed value types: Strings, ints, floats,
     *                    doubles and dates.
     * @param callback {@link APICallback}
     * @return Used call object
     */
    public Call getContentByLocationIdentifier(String locationIdentifier,
                                               EnumSet<ContentFlags> contentFlags,
                                               HashMap<String, Object> conditions,
                                               String password,
                                               APIPasswordCallback<Content, List<Error>> callback) {
        return getContentByLocationIdentifier(locationIdentifier, contentFlags, conditions, null, password, callback);
    }

    /**
     *  Get a content for a specific LocationIdentifier with flags.
     *
     * @param locationIdentifier LocationIdentifier from QR or NFC
     * @param contentFlags Different flags {@link ContentFlags}
     * @param conditions  HashMap with conditions to match. Allowed value types: Strings, ints, floats,
     *                    doubles and dates.
     * @param reason ContentReason to get better analytics in the xamoom dashboard
     * @param callback {@link APICallback}
     * @return Used call object
     */
    public Call getContentByLocationIdentifier(final String locationIdentifier,
                                               EnumSet<ContentFlags> contentFlags,
                                               HashMap<String, Object> conditions,
                                               ContentReason reason,
                                               String password,
                                               final APIPasswordCallback<Content, List<Error>> callback) {
        if (offline) {
            offlineEnduserApi.getContentByLocationIdentifier(locationIdentifier, callback);
            return null;
        }

        if (conditions == null) {
            conditions = new HashMap<>();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        conditions.put("x-datetime", sdf.format(new Date()));

        Map<String, String> params = UrlUtil.getUrlParameter(language);
        params.put("filter[location-identifier]", locationIdentifier);
        params = UrlUtil.addContentParameter(params, contentFlags);
        params = UrlUtil.addConditionsToUrl(params, conditions);

        HashMap<String, String> headers = new HashMap<>(3);
        if (getEphemeralId() != null) {
            headers.put(EnduserApiInterface.HEADER_EPHEMERAL, getEphemeralId());
        }
        if (getAuthorizationId() != null) {
            headers.put(EnduserApiInterface.HEADER_AUTHORIZATION, getAuthorizationId());
        }
        if (reason != null) {
            headers.put(EnduserApiInterface.HEADER_REASON, String.valueOf(reason.getValue()));
        }
        if (password != null) {
            int contentIdOpens = sharedPref.getInt(locationIdentifier, 0);
            sharedPref.edit().putInt(locationIdentifier, contentIdOpens + 1).apply();
            headers.put(EnduserApiInterface.HEADER_PASSWORD, password);
        }

        Call<ResponseBody> call = enduserApiInterface.getContents(headers, params);

        APIPasswordCallback<Content, List<Error>> passwordCallback = new APIPasswordCallback<Content, List<Error>>() {
            @Override
            public void finished(Content result) {
                sharedPref.edit().putFloat(locationIdentifier+"_next_open", -1).apply();
                sharedPref.edit().putInt(locationIdentifier,  0).apply();

                callback.finished(result);
            }

            @Override
            public void error(final List<Error> error) {
                callback.error(error);
            }

            @Override
            public void passwordRequested() {
                int contentIdOpens = sharedPref.getInt(locationIdentifier, 0);
                long contentNextOpen = (long) sharedPref.getFloat(locationIdentifier + "_next_open", -1);

                Date n = new Date();
                long now = n.getTime();
                long diff = TimeUnit.MILLISECONDS.toSeconds(now) - (15 * 60);
                long contentNextOpenSeconds = -1;
                if (contentNextOpen != -1) {
                    contentNextOpenSeconds = TimeUnit.MILLISECONDS.toSeconds(contentNextOpen);
                }

                if ((contentNextOpenSeconds == -1 || diff > contentNextOpenSeconds) && contentIdOpens <= 2) {
                    callback.passwordRequested();
                } else {
                    ArrayList<String> tags = new ArrayList<String>();
                    tags.add("x-forbidden");

                    getContentsByTags(tags, 1, null, null, new APIListCallback<List<Content>, List<Error>>() {
                        @Override
                        public void finished(List<Content> result, String cursor, boolean hasMore) {
                            if (result.size() > 0) {
                                callback.finished(result.get(0));
                            } else {
                                callback.finished(null);
                            }
                        }

                        @Override
                        public void error(List<Error> error) {
                            callback.error(error);
                        }
                    });
                    sharedPref.edit().putFloat(locationIdentifier + "_next_open", now).apply();
                    sharedPref.edit().putInt(locationIdentifier,  0).apply();
                }
            }
        };

        callHandler.enquePasswordCall(call, passwordCallback);
        return call;
    }

    /**
     * Get content for a specific beacon.
     *
     * @param major Beacon major ID
     * @param minor Beacon minor ID
     * @param callback {@link APICallback}
     * @return Used call object
     */
    public Call getContentByBeacon(int major, int minor, String password, APIPasswordCallback<Content, List<Error>>
            callback) {
        return getContentByLocationIdentifier(String.format("%s|%s", major, minor), password, callback);
    }

    /**
     * Get content for a specific beacon with options and condition.
     *
     * @param major Beacon major ID
     * @param minor Beacon minor ID
     * @param contentFlags Different flags {@link ContentFlags}
     * @param callback {@link APICallback}
     * @return Used call object
     */
    public Call getContentByBeacon(int major, int minor, EnumSet<ContentFlags> contentFlags,
                                   String password,
                                   APIPasswordCallback<Content, List<Error>> callback) {
        return getContentByLocationIdentifier(String.format("%s|%s", major, minor), contentFlags, password, callback);
    }


    /**
     * Get content for a specific beacon with options.
     *
     * @param major Beacon major ID
     * @param minor Beacon minor ID
     * @param contentFlags Different flags {@link ContentFlags}
     * @param conditions  HashMap with conditions to match. Allowed value types: Strings, ints, floats,
     *                    doubles and dates.
     * @param callback {@link APICallback}
     * @return Used call object
     */
    public Call getContentByBeacon(int major, int minor, EnumSet<ContentFlags> contentFlags,
                                   HashMap<String, Object> conditions,
                                   String password,
                                   APIPasswordCallback<Content, List<Error>> callback) {
        return getContentByLocationIdentifier(String.format("%s|%s", major, minor), contentFlags,
                conditions, password, callback);
    }

    /**
     * Get content for a specific beacon with options and condition.
     *
     * @param major Beacon major ID
     * @param minor Beacon minor ID
     * @param contentFlags Different flags {@link ContentFlags}
     * @param conditions HashMap with conditions to match. Allowed value types: Strings, ints, floats,
     *                   doubles and dates.
     * @param reason ContentReason to get better analytics in the xamoom dashboard
     * @param callback {@link APICallback}
     * @return Used call object
     */
    public Call getContentByBeacon(int major, int minor, EnumSet<ContentFlags> contentFlags,
                                   HashMap<String, Object> conditions,
                                   ContentReason reason,
                                   String password,
                                   APIPasswordCallback<Content, List<Error>> callback) {
        return getContentByLocationIdentifier(String.format("%s|%s", major, minor),contentFlags,
                conditions, reason, password, callback);
    }

    /**
     * Get list of contents with your location. Geofence radius is 40m.
     *
     * @param location Users location
     * @param pageSize PageSize for returned contents (max 100)
     * @param cursor Cursor for paging
     * @param sortFlags {@link ContentSortFlags} to sort results
     * @param callback {@link APIListCallback}
     * @return Used call object
     */
    public Call getContentsByLocation(Location location, int pageSize, @Nullable String cursor,
                                      final EnumSet<ContentSortFlags> sortFlags,
                                      APIListCallback<List<Content>,
                                              List<Error>> callback) {

        if (offline) {
            offlineEnduserApi.getContentsByLocation(location, 10, null, null, null);
            return null;
        }

        Map<String, String> params = UrlUtil.addContentSortingParameter(UrlUtil.getUrlParameter(language),
                sortFlags);
        params = UrlUtil.addPagingToUrl(params, pageSize, cursor);
        params.put("filter[lat]", Double.toString(location.getLatitude()));
        params.put("filter[lon]", Double.toString(location.getLongitude()));

        Call<ResponseBody> call = enduserApiInterface.getContents(
                getHeaders(), params);
        callHandler.enqueListCall(call, callback);
        return call;
    }

    /**
     * Get list of contents with a specific tag.
     *
     * @param tags List of strings
     * @param pageSize PageSize for returned contents (max 100)
     * @param cursor Cursor for paging
     * @param sortFlags {@link ContentSortFlags} to sort results
     * @param callback {@link APIListCallback}
     * @return Used call object
     */
    public Call getContentsByTags(List<String> tags, int pageSize, @Nullable String cursor,
                                  EnumSet<ContentSortFlags> sortFlags,
                                  APIListCallback<List<Content>, List<Error>> callback) {
        return getContentsByTags(tags, pageSize, cursor, sortFlags, null, callback);
    }

    public Call downloadContentsByTags(List<String> tags, int pageSize, @Nullable String cursor,
                                  EnumSet<ContentSortFlags> sortFlags,
                                  APIListCallback<List<Content>, List<Error>> callback) {
        return getContentsByTags(tags, pageSize, cursor, sortFlags, null, callback, true);
    }

    /**
     * Get list of contents with a specific tag.
     *
     * @param tags List of strings
     * @param pageSize PageSize for returned contents (max 100)
     * @param cursor Cursor for paging
     * @param sortFlags {@link ContentSortFlags} to sort results
     * @param filter Optional filters to add on top of filtering for tags
     * @param callback {@link APIListCallback}
     * @return Used call object
     */
    public Call getContentsByTags(List<String> tags, int pageSize, @Nullable String cursor,
                                  EnumSet<ContentSortFlags> sortFlags, @Nullable Filter filter,
                                  APIListCallback<List<Content>, List<Error>> callback) {
        if (filter != null) {
            filter = new Filter.FilterBuilder()
                    .name(filter.getName())
                    .tags((ArrayList<String>) tags)
                    .fromDate(filter.getFromDate())
                    .toDate(filter.getToDate())
                    .relatedSpotId(filter.getRelatedSpotId())
                    .build();
        } else {
            if (tags instanceof ArrayList) {
                filter = new Filter.FilterBuilder()
                        .tags((ArrayList<String>) tags)
                        .build();
            } else {
                ArrayList<String> contentTags = new ArrayList<>(Arrays.asList(tags.get(0)));
                filter = new Filter.FilterBuilder()
                        .tags(contentTags)
                        .build();
            }
        }

        boolean isNeedToUpdateCache = filter.getTags().contains("x-quiz") || offlineEnduserApi.isNeedToUpdateContentsCache(filter);
        if (offline) {
            offlineEnduserApi.getContentsByTags(tags, pageSize, cursor, sortFlags, filter, callback);
            return null;
        }

        Map<String, String> params = UrlUtil.addContentSortingParameter(UrlUtil.getUrlParameter(language),
                sortFlags);
        params = UrlUtil.addPagingToUrl(params, pageSize, cursor);
        params = UrlUtil.addFilters(params, filter);


        Call<ResponseBody> call = enduserApiInterface.getContents(getHeaders(), params);
        callHandler.enqueListCall(call, callback);
        return call;
    }

    public Call getContentsByTags(List<String> tags, int pageSize, @Nullable String cursor,
                                  EnumSet<ContentSortFlags> sortFlags, @Nullable Filter filter,
                                  APIListCallback<List<Content>, List<Error>> callback, boolean isNeedRefresh) {
        if (filter != null) {
            filter = new Filter.FilterBuilder()
                    .name(filter.getName())
                    .tags((ArrayList<String>) tags)
                    .fromDate(filter.getFromDate())
                    .toDate(filter.getToDate())
                    .relatedSpotId(filter.getRelatedSpotId())
                    .build();
        } else {
            if (tags instanceof ArrayList) {
                filter = new Filter.FilterBuilder()
                        .tags((ArrayList<String>) tags)
                        .build();
            } else {
                ArrayList<String> contentTags = new ArrayList<>(Arrays.asList(tags.get(0)));
                filter = new Filter.FilterBuilder()
                        .tags(contentTags)
                        .build();
            }
        }

        if (!isNeedRefresh) {
            offlineEnduserApi.getContentsByTags(tags, pageSize, cursor, sortFlags, filter, callback);
            return null;
        }

        Map<String, String> params = UrlUtil.addContentSortingParameter(UrlUtil.getUrlParameter(language),
                sortFlags);
        params = UrlUtil.addPagingToUrl(params, pageSize, cursor);
        params = UrlUtil.addFilters(params, filter);


        Call<ResponseBody> call = enduserApiInterface.getContents(getHeaders(), params);
        callHandler.enqueListCall(call, callback);
        return call;
    }

    /**
     * Get list of contents with full-text name search.
     *
     * @param name Name to search for
     * @param pageSize PageSize for returned contents (max 100)
     * @param cursor Cursor for paging
     * @param sortFlags {@link ContentSortFlags} to sort results
     * @param callback {@link APIListCallback}
     * @return Used call object
     */
    public Call searchContentsByName(String name, int pageSize, @Nullable String cursor,
                                     EnumSet<ContentSortFlags> sortFlags,
                                     APIListCallback<List<Content>, List<Error>> callback) {
        return searchContentsByName(name, pageSize, cursor, sortFlags, null, callback);
    }

    /**
     * Get list of contents with full-text name search.
     *
     * @param name Name to search for
     * @param pageSize PageSize for returned contents (max 100)
     * @param cursor Cursor for paging
     * @param sortFlags {@link ContentSortFlags} to sort results
     * @param filter Optional filters to add on top of filtering for name
     * @param callback {@link APIListCallback}
     * @return Used call object
     */
    public Call searchContentsByName(String name, int pageSize, @Nullable String cursor,
                                     EnumSet<ContentSortFlags> sortFlags, @Nullable Filter filter,
                                     APIListCallback<List<Content>, List<Error>> callback) {
        if (filter != null) {
            filter = new Filter.FilterBuilder()
                    .name(name)
                    .tags(filter.getTags())
                    .fromDate(filter.getFromDate())
                    .toDate(filter.getToDate())
                    .relatedSpotId(filter.getRelatedSpotId())
                    .build();
        } else {
            filter = new Filter.FilterBuilder()
                    .name(name)
                    .build();
        }

        if (offline) {
            offlineEnduserApi.searchContentsByName(name, pageSize, cursor, sortFlags, filter, callback);
            return null;
        }

        Map<String, String> params = UrlUtil.addContentSortingParameter(UrlUtil.getUrlParameter(language),
                sortFlags);
        params = UrlUtil.addPagingToUrl(params, pageSize, cursor);
        params = UrlUtil.addFilters(params, filter);

        Call<ResponseBody> call = enduserApiInterface.getContents(getHeaders(), params);
        callHandler.enqueListCall(call, callback);
        return call;
    }

    /**
     * Get list of contents with dates and relatedSpotId.
     *
     * @param fromDate Filter events from this date
     * @param toDate Filter events to this date
     * @param relatedSpotId RelatedSpot id to filter
     * @param pageSize PageSize for returned contents (max 100)
     * @param cursor Cursor for paging
     * @param sortFlags {@link ContentSortFlags} to sort results
     * @param filter Optional filters to add on top of filtering for name
     * @param callback {@link APIListCallback}
     * @return Used call object
     */
    public Call getContentByDates(@Nullable Date fromDate, @Nullable Date toDate,
                                  @Nullable String relatedSpotId, int pageSize,
                                  @Nullable String cursor, EnumSet<ContentSortFlags> sortFlags,
                                  @Nullable Filter filter,
                                  APIListCallback<List<Content>, List<Error>> callback) {

        if (filter != null) {
            filter = new Filter.FilterBuilder()
                    .name(filter.getName())
                    .tags(filter.getTags())
                    .fromDate(fromDate)
                    .toDate(toDate)
                    .relatedSpotId(relatedSpotId)
                    .build();
        } else {
            filter = new Filter.FilterBuilder()
                    .fromDate(fromDate)
                    .toDate(toDate)
                    .relatedSpotId(relatedSpotId)
                    .build();
        }

        if (offline) {
            offlineEnduserApi.getContents(filter, pageSize, cursor, sortFlags, callback);
            return null;
        }

        Map<String, String> params = UrlUtil.addContentSortingParameter(UrlUtil.getUrlParameter(language),
                sortFlags);
        params = UrlUtil.addPagingToUrl(params, pageSize, cursor);
        params = UrlUtil.addFilters(params, filter);

        Call<ResponseBody> call = enduserApiInterface.getContents(getHeaders(), params);
        callHandler.enqueListCall(call, callback);
        return call;
    }

    /**
     * Get content recommendations from backend.
     * Call not available for offline use.
     *
     * @param callback {@link APIListCallback}
     * @return Used call object
     */
    public Call getContentRecommendations(APIListCallback<List<Content>, List<Error>> callback) {
        if (offline) {
            callback.finished(new ArrayList<Content>(), "1", false);
            return null;
        }

        Map<String, String> params = UrlUtil.getUrlParameter(language);
        params = UrlUtil.addRecommend(params);

        Call<ResponseBody> call = enduserApiInterface.getContents(getHeaders(), params);
        callHandler.enqueListCall(call, callback);

        return call;
    }

    /**
     * Get spot with specific id.
     *
     * @param spotId Id of the spot
     * @param callback {@link APICallback}
     * @return Used call object
     */
    public Call getSpot(String spotId, APICallback<Spot, List<Error>> callback) {
        return getSpot(spotId, null, callback);
    }

    /**
     * Get spot with specific id.
     *
     * @param spotId Id of the spot
     * @param spotFlags {@link SpotFlags}
     * @param callback {@link APICallback}
     * @return Used call object
     */
    public Call getSpot(String spotId, EnumSet<SpotFlags> spotFlags, APICallback<Spot, List<Error>> callback) {
        if (offline) {
            offlineEnduserApi.getSpot(spotId, callback);
            return null;
        }

        Map<String, String> params = UrlUtil.getUrlParameter(this.language);
        params = UrlUtil.addSpotParameter(params, spotFlags);

        Call<ResponseBody> call = enduserApiInterface.getSpot(getHeaders(), spotId, params);
        callHandler.enqueCall(call, callback);
        return call;
    }

    /**
     * Get list of spots inside radius of a location.
     *
     * @param location User location
     * @param radius Radius to search in meter (max 5000)
     * @param spotFlags {@link SpotFlags}
     * @param sortFlags {@link SpotSortFlags}
     * @param callback {@link APIListCallback}
     * @return Used call object
     */
    public Call getSpotsByLocation(Location location, int radius, EnumSet<SpotFlags> spotFlags,
                                   @Nullable EnumSet<SpotSortFlags> sortFlags,
                                   APIListCallback<List<Spot>, List<Error>> callback) {
        return getSpotsByLocation(location, radius, 0, null, spotFlags, sortFlags, callback);
    }

    /**
     * Get list of spots inside radius of a location.
     *
     * @param location User location
     * @param radius Radius to search in meter (max 5000)
     * @param pageSize Size for pages. (max 100)
     * @param cursor Cursor of last search
     * @param spotFlags {@link SpotFlags}
     * @param sortFlags {@link SpotSortFlags}
     * @param callback {@link APIListCallback}
     * @return Used call object
     */
    public Call getSpotsByLocation(Location location, int radius, int pageSize, @Nullable String cursor,
                                   @Nullable EnumSet<SpotFlags> spotFlags,
                                   @Nullable EnumSet<SpotSortFlags> sortFlags,
                                   APIListCallback<List<Spot>, List<Error>> callback) {
        if (offline) {
            offlineEnduserApi.getSpotsByLocation(location, radius, pageSize, cursor,
                    spotFlags, sortFlags, callback);
            return null;
        }

        Map<String, String> params = UrlUtil.addSpotParameter(UrlUtil.getUrlParameter(language),
                spotFlags);
        params = UrlUtil.addSpotSortingParameter(params, sortFlags);
        params = UrlUtil.addPagingToUrl(params, pageSize, cursor);
        params.put("filter[lat]", Double.toString(location.getLatitude()));
        params.put("filter[lon]", Double.toString(location.getLongitude()));
        params.put("filter[radius]", Integer.toString(radius));

        Call<ResponseBody> call = enduserApiInterface.getSpots(getHeaders(), params);
        callHandler.enqueListCall(call, callback);
        return call;
    }

    /**
     * Get list of spots with tags.
     * Or operation used when searching with tags.
     *
     * @param tags List with tag names
     * @param spotFlags {@link SpotFlags}
     * @param sortFlags {@link SpotSortFlags}
     * @param callback {@link APIListCallback}
     * @return Used call object
     */
    public Call getSpotsByTags(List<String> tags, @Nullable EnumSet<SpotFlags> spotFlags,
                               @Nullable EnumSet<SpotSortFlags> sortFlags,
                               APIListCallback<List<Spot>, List<Error>> callback) {
        return getSpotsByTags(tags, 0, null, spotFlags, sortFlags, callback);
    }

    /**
     * Get list of spots with with tags.
     * Or operation used when searching with tags.
     *
     * @param tags List with tag names
     * @param pageSize Size for pages (max 100)
     * @param cursor Cursor of last search
     * @param spotFlags {@link SpotFlags}
     * @param sortFlags {@link SpotSortFlags}
     * @param callback {@link APIListCallback}
     * @return Used call object
     */
    public Call getSpotsByTags(List<String> tags, int pageSize, @Nullable String cursor,
                               @Nullable EnumSet<SpotFlags> spotFlags,
                               @Nullable EnumSet<SpotSortFlags> sortFlags,
                               APIListCallback<List<Spot>, List<Error>> callback) {
        if (offline) {
            offlineEnduserApi.getSpotsByTags(tags, pageSize, cursor, spotFlags, sortFlags,
                    callback);
            return null;
        }

        Map<String, String> params = UrlUtil.addSpotParameter(UrlUtil.getUrlParameter(language),
                spotFlags);
        params = UrlUtil.addSpotSortingParameter(params, sortFlags);
        params = UrlUtil.addPagingToUrl(params, pageSize, cursor);
        params.put("filter[tags]", JsonListUtil.listToJsonArray(tags, ","));

        Call<ResponseBody> call = enduserApiInterface.getSpots(getHeaders(), params);
        callHandler.enqueListCall(call, callback);
        return call;
    }

    /**
     * Get list of spots with full-text name search.
     *
     * @param name Name to searchfor
     * @param pageSize Size for pages (max 100)
     * @param cursor Cursor of last search
     * @param spotFlags {@link SpotFlags}
     * @param sortFlags {@link SpotSortFlags}
     * @param callback {@link APIListCallback}
     * @return Used call object
     */
    public Call searchSpotsByName(String name, int pageSize, @Nullable String cursor,
                                  @Nullable EnumSet<SpotFlags> spotFlags,
                                  @Nullable EnumSet<SpotSortFlags> sortFlags,
                                  APIListCallback<List<Spot>, List<Error>> callback) {
        if (offline) {
            offlineEnduserApi.searchSpotsByName(name, pageSize, cursor, spotFlags,
                    sortFlags, callback);
            return null;
        }

        Map<String, String> params = UrlUtil.addSpotParameter(UrlUtil.getUrlParameter(language),
                spotFlags);
        params = UrlUtil.addSpotSortingParameter(params, sortFlags);
        params = UrlUtil.addPagingToUrl(params, pageSize, cursor);
        params.put("filter[name]", name);

        Call<ResponseBody> call = enduserApiInterface.getSpots(getHeaders(), params);
        callHandler.enqueListCall(call, callback);
        return call;
    }

    /**
     * Get the system connected to your api key.
     *
     * @param callback {@link APIListCallback}
     * @return Used call object
     */
    public Call getSystem(final APICallback<System, List<Error>> callback) {
        if (offline) {
            offlineEnduserApi.getSystem(callback);
            return null;
        }

        Map<String, String> params = UrlUtil.getUrlParameter(language);

        Call<ResponseBody> call = enduserApiInterface.getSystem(getHeaders(), params);
        callHandler.enqueCall(call, callback);
        return call;
    }

    /**
     * Get the menu to your system.
     *
     * @param systemId Systems systemId
     * @param callback {@link APIListCallback}
     * @return Used call object
     */
    public Call getMenu(String systemId, final APICallback<Menu, List<Error>> callback) {
        if (offline) {
            offlineEnduserApi.getMenu(systemId, callback);
            return null;
        }

        Map<String, String> params = UrlUtil.getUrlParameter(language);

        Call<ResponseBody> call = enduserApiInterface.getMenu(getHeaders(), systemId, params);
        callHandler.enqueCall(call, callback);
        return call;
    }

    /**
     * Get the systemSettings to your system.
     *
     * @param systemId Systems systemId
     * @param callback {@link APIListCallback}
     * @return Used call object
     */
    public Call getSystemSetting(String systemId, final APICallback<SystemSetting, List<Error>> callback) {
        if (offline) {
            offlineEnduserApi.getSystemSetting(systemId, callback);
            return null;
        }

        Map<String, String> params = UrlUtil.getUrlParameter(language);

        Call<ResponseBody> call = enduserApiInterface.getSetting(getHeaders(), systemId, params);
        callHandler.enqueCall(call, callback);
        return call;
    }

    /**
     * Get the style to your system.
     *
     * @param systemId Systems systemId
     * @param callback {@link APIListCallback}
     * @return Used call object
     */
    public Call getStyle(String systemId, final APICallback<Style, List<Error>> callback) {
        if (offline) {
            offlineEnduserApi.getStyle(systemId, callback);
            return null;
        }

        Map<String, String> params = UrlUtil.getUrlParameter(language);

        Call<ResponseBody> call = enduserApiInterface.getStyle(getHeaders(), systemId, params);
        callHandler.enqueCall(call, callback);
        return call;
    }

    /**
     * Get a voucher status. Returns true in a callback if voucher is redeemable.
     *
     * @param contentId Content id
     * @param clientId Client id
     * @param callback {@link APIListCallback}
     * @return Used call object
     */
    public Call getVoucherStatus(String contentId, @Nullable String clientId, final APICallback<Boolean, List<Error>> callback) {
        if (offline) {
            return null;
        }

        if (clientId == null) {
            clientId = getEphemeralId();
        }

        Call<ResponseBody> call = enduserApiInterface.getVoucherStatus(getHeaders(), contentId, clientId);
        callHandler.enqueCall(call, new APICallback<NewVoucherStatusMessage, List<Error>> () {

            @Override
            public void finished(NewVoucherStatusMessage result) {
                callback.finished(result.getRedeemable());
            }

            @Override
            public void error(List<Error> error) {
                callback.error(error);
            }
        });
        return call;
    }

    /**
     * Redeems a voucher and returns true in a callback if the voucher can be redeemed next time.
     *
     * @param contentId Content id
     * @param clientId Client id
     * @param redeemCode Redemption code
     * @param callback {@link APIListCallback}
     * @return Used call object
     */
    public Call redeemVoucher(String contentId, @Nullable String clientId, String redeemCode, final APICallback<Boolean, List<Error>> callback) {
        if (offline) {
            return null;
        }

        if (clientId == null) {
            clientId = getEphemeralId();
        }

        Call<ResponseBody> call = enduserApiInterface.redeemVoucher(getHeaders(), contentId, clientId, redeemCode);
        callHandler.enqueCall(call, new APICallback<NewVoucherStatusMessage, List<Error>> () {

            @Override
            public void finished(NewVoucherStatusMessage result) {
                callback.finished(result.getRedeemable());
            }

            @Override
            public void error(List<Error> error) {
                callback.error(error);
            }
        });
        return call;
    }


    public void pushDevice(PushDeviceUtil util, boolean instantPush) {

        Long lastPush = sharedPref.getLong("xamoom-last-push-register", -1);
        Map<String, Float> location = util.getSavedLocation();
        Location l = new Location("");

        if (location != null) {
            Float lat = location.get("lat");
            Float lon = location.get("lon");

            l.setLatitude(lat);
            l.setLongitude(lon);
        } else {
            l.setLongitude(0.0);
            l.setLatitude(0.0);

            location = new HashMap<String, Float>();
            location.put("lat", 0.0F);
            location.put("lon", 0.0F);
        }

        if (!instantPush && this.lastLocation.distanceTo(l) > 100) {
            return;
        }

        if (!instantPush && lastPush >= java.lang.System.currentTimeMillis() - (30 * 60 * 1000)) {
            return;
        }

        String token = util.getSavedToken();

        String packageName = context.getPackageName();
        String sdkVersion = context.getString(R.string.sdk_version);

        String version = null;
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (token == null || version == null) {
            return;
        }

        PushDevice device = new PushDevice(token, location, version, packageName, sdkVersion, util.getSound(), util.getNoNotification());
        sharedPref.edit().putLong("xamoom-last-push-register", java.lang.System.currentTimeMillis()).apply();
        lastLocation = l;

        JsonApiObject jsonApiObject = new JsonApiObject();
        jsonApiObject.setResource(device);

        Morpheus morpheus = new Morpheus();
        String json = morpheus.createJson(jsonApiObject, false);

        final APICallback<PushDevice, List<Error>> callback = new APICallback<PushDevice, List<Error>>() {
            @Override
            public void finished(PushDevice result) {
                Log.d("Push Registration", "Success");
            }

            @Override
            public void error(List<Error> error) {
                sharedPref.edit().putLong("xamoom-last-push-register", -1).apply();
                Log.e("Push Registration", error.get(0).getCode() + " " + error.get(0).getDetail());
            }
        };

        Call<ResponseBody> call = enduserApiInterface.pushDevice(getHeaders(), device.getUid(), json);
        callHandler.enqueCall(call, callback);
    }

  /*
  //parcelable
  public static final Creator<EnduserApi> CREATOR = new Creator<EnduserApi>() {
    @Override
    public EnduserApi createFromParcel(Parcel in) {
      return new EnduserApi(in);
    }

    @Override
    public EnduserApi[] newArray(int size) {
      return new EnduserApi[size];
    }
  };

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(apiKey);
    dest.writeString(language);
  }
  */

    // ephemeral stuff

    @Override
    public void gotEphemeralId(String ephemeralId) {
        if (getEphemeralId() == null ||
                !getEphemeralId().equals(ephemeralId)) {
            this.ephemeralId = ephemeralId;

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(PREF_EPHEMERAL_ID, ephemeralId);
            editor.apply();
        }
    }

    @Override
    public void gotAuthorizationId(String authorizationId) {
        if (getAuthorizationId() == null ||
                !getAuthorizationId().equals(authorizationId)) {
            this.authorizationId = authorizationId;

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(PREF_AUTHORIZATION_ID, authorizationId);
            editor.apply();
        }
    }

    public String getEphemeralId() {
        if (ephemeralId != null) {
            return ephemeralId;
        }

        ephemeralId = sharedPref.getString(PREF_EPHEMERAL_ID, null);
        return ephemeralId;
    }

    private String getAuthorizationId() {
        if (authorizationId != null) {
            return authorizationId;
        }

        authorizationId = sharedPref.getString(PREF_AUTHORIZATION_ID, null);
        return authorizationId;
    }

    private HashMap<String, String> getHeaders() {
        HashMap<String, String> headers = new HashMap<>(2);
        if (getEphemeralId() != null) {
            headers.put(EnduserApiInterface.HEADER_EPHEMERAL, getEphemeralId());
        }
        if (getAuthorizationId() != null) {
            headers.put(EnduserApiInterface.HEADER_AUTHORIZATION, getAuthorizationId());
        }

        return headers;
    }

    // helper

    public String generateUserAgent() {
        if (this.context == null) {
            return "Xamoom SDK Android";
        }

        String sdkversion = "";
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            sdkversion = bundle.getString("com.xamoom.android.xamoomsdk.version");
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e(TAG, "Failed to load meta-data, NullPointer: " + e.getMessage());
        }

        String appName = context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
        appName = Normalizer.normalize(appName, Normalizer.Form.NFD);
        appName = appName.replaceAll("[^\\p{ASCII}]", "");

        String builder = "Xamoom SDK Android" +
                "|" +
                appName +
                "|" +
                sdkversion;

        return builder;
    }

    // getters & setters
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

    public void setCallHandler(CallHandler callHandler) {
        this.callHandler = callHandler;
    }

    public String getApiKey() {
        return apiKey;
    }

    public int getCooldown() {
        return this.cooldown;
    }

    /**
     * Use this to get your instance. It will create a new one with your api key, when
     * there is no isntance.
     *
     * @param apikey Your xamoom api key.
     * @return EnduserApi instance.
     */
    public static EnduserApi getSharedInstance(@NonNull String apikey, Context context, @Nullable String majorId, @Nullable int cooldown) {
        if (apikey == null) {
            throw new NullPointerException("Apikey should not be null.");
        }

        if (EnduserApi.sharedInstance == null) {
            EnduserApi.sharedInstance = new EnduserApi(apikey, context, majorId, cooldown);
        }
        return EnduserApi.sharedInstance;
    }

    /**
     * This will you return your current sharedInstance. Even if it is null.
     * @return EnduserApi instance.
     */
    public static EnduserApi getSharedInstance() {
        if (EnduserApi.sharedInstance == null) {
            throw new NullPointerException("Instance is null. Please use getSharedInstance(apikey, context) " +
                    "or setSharedInstance");
        }
        return EnduserApi.sharedInstance;
    }

    public static EnduserApi getSharedInstance(Context context) {
        if (EnduserApi.sharedInstance == null) {
            EnduserApi restoredApi = EnduserApi.restoreEnduserApi(context);
            if (restoredApi == null) {
                throw new NullPointerException("Instance is null. Please use getSharedInstance(apikey, context) " +
                        "or setSharedInstance");
            }

            EnduserApi.setSharedInstance(restoredApi);
        }
        return EnduserApi.sharedInstance;
    }

    /**
     * Set your the sharedInstance with your Instance of EnduserApi.
     * @param sharedInstance SharedInstance to save.
     */
    public static void setSharedInstance(EnduserApi sharedInstance) {
        EnduserApi.sharedInstance = sharedInstance;
    }

    public OfflineEnduserApi getOfflineEnduserApi() {
        return offlineEnduserApi;
    }

    public void setOfflineEnduserApi(OfflineEnduserApi offlineEnduserApi) {
        this.offlineEnduserApi = offlineEnduserApi;
    }

    public boolean isOffline() {
        return offline;
    }

    /**
     * Set offline to true to get data saved in local database.
     * @param offline Boolean to set the enduserapi to offline.
     */
    public void setOffline(boolean offline) {
        this.offline = offline;
    }

    public String getMajorId() {
        return majorId;
    }

    public void setMajorId(String majorId) {
        this.majorId = majorId;
    }
}
