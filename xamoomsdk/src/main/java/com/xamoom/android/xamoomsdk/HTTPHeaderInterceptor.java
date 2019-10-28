/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.xamoomsdk;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Interceptor for retrofit to change the header.
 */
public class HTTPHeaderInterceptor implements Interceptor {
  private static final String HEADER_USERAGENT = "User-Agent";
  private static final String HEADER_APIKEY = "APIKEY";
  private String userAgent;
  private String apiKey;

  public HTTPHeaderInterceptor(@NonNull String userAgent, @NonNull String apikey) {
    this.userAgent = userAgent;
    this.apiKey = apikey;
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    Request.Builder builder = chain.request().newBuilder()
        .addHeader("Content-Type", "application/vnd.api+json")
        .addHeader("Accept", "application/json");

    if (userAgent != null) {
      builder.addHeader(HEADER_USERAGENT, userAgent);
    }

    if (apiKey != null) {
      builder.addHeader(HEADER_APIKEY, apiKey);
    }
    return chain.proceed(builder.build());
  }
}
