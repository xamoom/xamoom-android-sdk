/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.xamoomsdk;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Interceptor for retrofit to change the header.
 */
public class HTTPHeaderInterceptor implements Interceptor {
  private String userAgent;
  private String apiKey;

  public HTTPHeaderInterceptor(String userAgent, String apikey) {
    this.userAgent = userAgent;
    this.apiKey = apikey;
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    Request request = chain.request().newBuilder()
        .addHeader("Content-Type", "application/vnd.api+json")
        .addHeader("Accept", "application/json")
        .addHeader("User-Agent", userAgent)
        .addHeader("APIKEY", apiKey)
        .build();

    return chain.proceed(request);
  }
}
