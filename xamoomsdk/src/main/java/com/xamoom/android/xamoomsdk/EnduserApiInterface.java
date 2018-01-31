/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.xamoomsdk;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Interface with all EnduserApi calls.
 * Used by retrofit.
 */
public interface EnduserApiInterface {
  String HEADER_EPHEMERAL = "X-Ephemeral-Id";
  String HEADER_REASON = "X-Reason";

  @GET("_api/v2/consumer/contents/{id}")
  Call<ResponseBody> getContent(@HeaderMap Map<String, String> headers,
                                @Path("id") String contentId,
                                @QueryMap Map<String, String> param);

  @GET("_api/v2/consumer/contents")
  Call<ResponseBody> getContents(@HeaderMap Map<String, String> headers,
                                 @QueryMap Map<String, String> param);

  @GET("_api/v2/consumer/spots/{id}")
  Call<ResponseBody> getSpot(@Header(HEADER_EPHEMERAL) String ephemeralId,
                             @Path("id") String spotId,
                                @QueryMap Map<String, String> param);

  @GET("_api/v2/consumer/spots")
  Call<ResponseBody> getSpots(@Header(HEADER_EPHEMERAL) String ephemeralId,
                              @QueryMap Map<String, String> param);

  @GET("_api/v2/consumer/systems")
  Call<ResponseBody> getSystem(@Header(HEADER_EPHEMERAL) String ephemeralId,
                               @QueryMap Map<String, String> param);

  @GET("_api/v2/consumer/menus/{id}")
  Call<ResponseBody> getMenu(@Header(HEADER_EPHEMERAL) String ephemeralId,
                             @Path("id") String systemId,
                             @QueryMap Map<String, String> param);

  @GET("_api/v2/consumer/settings/{id}")
  Call<ResponseBody> getSetting(@Header(HEADER_EPHEMERAL) String ephemeralId,
                                @Path("id") String systemId,
                                @QueryMap Map<String, String> param);

  @GET("_api/v2/consumer/styles/{id}")
  Call<ResponseBody> getStyle(@Header(HEADER_EPHEMERAL) String ephemeralId,
                              @Path("id") String systemId,
                              @QueryMap Map<String, String> param);
}
