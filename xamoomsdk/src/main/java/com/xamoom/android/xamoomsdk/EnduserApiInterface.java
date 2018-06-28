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
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Interface with all EnduserApi calls.
 * Used by retrofit.
 */
public interface EnduserApiInterface {
  String HEADER_AUTHORIZATION = "Authorization";
  String HEADER_EPHEMERAL = "X-Ephemeral-Id";
  String HEADER_REASON = "X-Reason";

  @GET("consumer/contents/{id}")
  Call<ResponseBody> getContent(@HeaderMap Map<String, String> headers,
                                @Path("id") String contentId,
                                @QueryMap Map<String, String> param);

  @GET("consumer/contents")
  Call<ResponseBody> getContents(@HeaderMap Map<String, String> headers,
                                 @QueryMap Map<String, String> param);

  @GET("consumer/spots/{id}")
  Call<ResponseBody> getSpot(@HeaderMap Map<String, String> headers,
                             @Path("id") String spotId,
                                @QueryMap Map<String, String> param);

  @GET("consumer/spots")
  Call<ResponseBody> getSpots(@HeaderMap Map<String, String> headers,
                              @QueryMap Map<String, String> param);

  @GET("consumer/systems")
  Call<ResponseBody> getSystem(@HeaderMap Map<String, String> headers,
                               @QueryMap Map<String, String> param);

  @GET("consumer/menus/{id}")
  Call<ResponseBody> getMenu(@HeaderMap Map<String, String> headers,
                             @Path("id") String systemId,
                             @QueryMap Map<String, String> param);

  @GET("consumer/settings/{id}")
  Call<ResponseBody> getSetting(@HeaderMap Map<String, String> headers,
                                @Path("id") String systemId,
                                @QueryMap Map<String, String> param);

  @GET("consumer/styles/{id}")
  Call<ResponseBody> getStyle(@HeaderMap Map<String, String> headers,
                              @Path("id") String systemId,
                              @QueryMap Map<String, String> param);
  @POST("push/update/{token}")
  Call<ResponseBody> postUser(@Path("token") String token, @Body String body);
}
