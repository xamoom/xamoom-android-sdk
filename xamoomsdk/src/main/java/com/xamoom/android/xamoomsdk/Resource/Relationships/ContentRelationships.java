package com.xamoom.android.xamoomsdk.Resource.Relationships;

import com.google.gson.annotations.SerializedName;
import com.xamoom.android.xamoomsdk.Resource.Attributes.ContentBlockAttributeMessage;
import com.xamoom.android.xamoomsdk.Resource.Attributes.RelationshipDataMessage;
import com.xamoom.android.xamoomsdk.Resource.Base.DataMessage;
import com.xamoom.android.xamoomsdk.Resource.Base.EmptyMessage;

import java.util.List;


public class ContentRelationships {
  @SerializedName("blocks")
  private RelationshipDataMessage<List<DataMessage<ContentBlockAttributeMessage, EmptyMessage>>> blocks;
  @SerializedName("system")
  private RelationshipDataMessage<DataMessage<EmptyMessage, EmptyMessage>> system;
  @SerializedName("spot")
  private RelationshipDataMessage<DataMessage<EmptyMessage, EmptyMessage>> spot;

  public RelationshipDataMessage<List<DataMessage<ContentBlockAttributeMessage, EmptyMessage>>> getBlocks() {
    return blocks;
  }

  public RelationshipDataMessage<DataMessage<EmptyMessage, EmptyMessage>> getSystem() {
    return system;
  }

  public RelationshipDataMessage<DataMessage<EmptyMessage, EmptyMessage>> getSpot() {
    return spot;
  }
}

