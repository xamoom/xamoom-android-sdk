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

  @Headers({"Content-Type: application/vnd.api+json",
      "Accept: application/json",
      "User-Agent: XamoomSDK Android",})
  @GET("_api/v2/consumer/contents/{id}")
  Call<ResponseBody> getContent(@Path("id") String contentID, @QueryMap Map<String, String> param);


  @Headers({"Content-Type: application/vnd.api+json",
      "Accept: application/json",
      "User-Agent: XamoomSDK Android",})
  @GET("_api/v2/consumer/contents")
  Call<ResponseBody> getContents(@QueryMap Map<String, String> param);

  @Headers({"Content-Type: application/vnd.api+json",
      "Accept: application/json",
      "User-Agent: XamoomSDK Android",})
  @GET("_api/v2/consumer/spots")
  Call<ResponseBody> getSpots(@QueryMap Map<String, String> param);

}
