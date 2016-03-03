package com.xamoom.android.xamoomsdk.Resource.Relationships;

import com.google.gson.annotations.SerializedName;
import com.xamoom.android.xamoomsdk.Resource.Attributes.ContentBlockAttributeMessage;
import com.xamoom.android.xamoomsdk.Resource.Attributes.RelationshipDataMessage;
import com.xamoom.android.xamoomsdk.Resource.Base.DataMessage;
import com.xamoom.android.xamoomsdk.Resource.Base.EmptyMessage;

import java.util.List;


public class ContentRelationships {
  @SerializedName("blocks")
  private RelationshipDataMessage<List<ContentBlockAttributeMessage>> blocks;
  @SerializedName("system")
  private RelationshipDataMessage<DataMessage<EmptyMessage, EmptyMessage>> system;

  public RelationshipDataMessage<List<ContentBlockAttributeMessage>> getBlocks() {
    return blocks;
  }

  public RelationshipDataMessage<DataMessage<EmptyMessage, EmptyMessage>> getSystem() {
    return system;
  }
}

