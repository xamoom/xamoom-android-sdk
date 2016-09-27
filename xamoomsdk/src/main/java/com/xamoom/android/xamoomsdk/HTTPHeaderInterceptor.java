package com.xamoom.android.xamoomsdk;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by raphaelseher on 27/09/2016.
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
