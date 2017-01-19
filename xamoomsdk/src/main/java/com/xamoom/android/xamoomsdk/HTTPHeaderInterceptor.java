/*
* Copyright 2017 by xamoom GmbH <apps@xamoom.com>
*
* This file is part of some open source application.
*
* Some open source application is free software: you can redistribute
* it and/or modify it under the terms of the GNU General Public
* License as published by the Free Software Foundation, either
* version 2 of the License, or (at your option) any later version.
*
* Some open source application is distributed in the hope that it will
* be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
* of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with xamoom-android-sdk. If not, see <http://www.gnu.org/licenses/>.
*
* author: Raphael Seher <raphael@xamoom.com>
*/

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
