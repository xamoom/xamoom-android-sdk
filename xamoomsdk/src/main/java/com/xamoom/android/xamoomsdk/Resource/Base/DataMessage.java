package com.xamoom.android.xamoomsdk.Resource.Base;

import com.google.gson.annotations.SerializedName;

public class DataMessage<T, U> {
    @SerializedName("id")
    private String id;

    @SerializedName("type")
    private String type;

    @SerializedName("attributes")
    private T attributes;

    @SerializedName("relationships")
    private U relationships;

    @Override
    public String toString() {
        return String.format("id: %s \ntype: %s \nattributes: %s \nrelationships: %s", id, type, attributes, relationships);
    }

    //getter & setter

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public T getAttributes() {
        return attributes;
    }

    public void setAttributes(T attributes) {
        this.attributes = attributes;
    }

    public U getRelationships() {
        return relationships;
    }

    public void setRelationships(U relationships) {
        this.relationships = relationships;
    }
}
