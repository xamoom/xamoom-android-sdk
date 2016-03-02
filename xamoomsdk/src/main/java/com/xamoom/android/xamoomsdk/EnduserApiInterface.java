package com.xamoom.android.xamoomsdk;

import com.xamoom.android.xamoomsdk.Resource.Attributes.ContentAttributesMessage;
import com.xamoom.android.xamoomsdk.Resource.Attributes.ContentBlockAttributeMessage;
import com.xamoom.android.xamoomsdk.Resource.Base.DataMessage;
import com.xamoom.android.xamoomsdk.Resource.Base.EmptyMessage;
import com.xamoom.android.xamoomsdk.Resource.Base.JsonApiMessage;
import com.xamoom.android.xamoomsdk.Resource.Relationships.ContentRelationships;

import java.util.List;
import java.util.Map;

import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;
import retrofit.http.QueryMap;

/**
 * Interface with all EnduserApi calls.
 * Used by retrofit.
 */
public interface EnduserApiInterface {

  @Headers({"Content-Type: application/vnd.api+json",
      "Accept: application/json",
      "User-Agent: XamoomSDK Android",})
  @GET("/contents/{id}")
  void getContent(@Path("id") String contentID, @QueryMap Map<String, String> options,
                 ResponseCallback<JsonApiMessage<EmptyMessage, DataMessage<ContentAttributesMessage, ContentRelationships>,
                     List<DataMessage<ContentBlockAttributeMessage, EmptyMessage>>>> cb);

}
