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

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Interface with all EnduserApi calls.
 * Used by retrofit.
 */
public interface EnduserApiInterface {

  @GET("_api/v2/consumer/contents/{id}")
  Call<ResponseBody> getContent(@Path("id") String contentId,
                                @QueryMap Map<String, String> param);

  @GET("_api/v2/consumer/contents")
  Call<ResponseBody> getContents(@QueryMap Map<String, String> param);

  @GET("_api/v2/consumer/spots/{id}")
  Call<ResponseBody> getSpot(@Path("id") String spotId,
                                @QueryMap Map<String, String> param);

  @GET("_api/v2/consumer/spots")
  Call<ResponseBody> getSpots(@QueryMap Map<String, String> param);

  @GET("_api/v2/consumer/systems")
  Call<ResponseBody> getSystem(@QueryMap Map<String, String> param);

  @GET("_api/v2/consumer/menus/{id}")
  Call<ResponseBody> getMenu(@Path("id") String systemId,
                             @QueryMap Map<String, String> param);

  @GET("_api/v2/consumer/settings/{id}")
  Call<ResponseBody> getSetting(@Path("id") String systemId,
                                @QueryMap Map<String, String> param);

  @GET("_api/v2/consumer/styles/{id}")
  Call<ResponseBody> getStyle(@Path("id") String systemId,
                                 @QueryMap Map<String, String> param);
}
