package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentBlocks.ContentBlock;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentBlocks.ContentBlockType0;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentBlocks.ContentBlockType1;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentBlocks.ContentBlockType2;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentBlocks.ContentBlockType3;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentBlocks.ContentBlockType4;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentBlocks.ContentBlockType5;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentBlocks.ContentBlockType6;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentBlocks.ContentBlockType7;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentBlocks.ContentBlockType8;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentBlocks.ContentBlockType9;

import java.lang.reflect.Type;

/**
 * Created by raphaelseher on 03.06.15.
 */
public class ContentBlockDeserializer implements JsonDeserializer {
    public ContentBlock deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        ContentBlock cb;

        JsonObject jsonObject = json.getAsJsonObject();
        int cbt = jsonObject.get("content_block_type").getAsInt();

        switch (cbt) {
            case 0:
                cb = new Gson().fromJson(json, ContentBlockType0.class);
                break;

            case 1:
                cb = new Gson().fromJson(json, ContentBlockType1.class);
                break;

            case 2:
                cb = new Gson().fromJson(json, ContentBlockType2.class);
                break;

            case 3:
                cb = new Gson().fromJson(json, ContentBlockType3.class);
                break;

            case 4:
                cb = new Gson().fromJson(json, ContentBlockType4.class);
                break;

            case 5:
                cb = new Gson().fromJson(json, ContentBlockType5.class);
                break;

            case 6:
                cb = new Gson().fromJson(json, ContentBlockType6.class);
                break;

            case 7:
                cb = new Gson().fromJson(json, ContentBlockType7.class);
                break;

            case 8:
                cb = new Gson().fromJson(json, ContentBlockType8.class);
                break;

            case 9:
                cb = new Gson().fromJson(json, ContentBlockType9.class);
                break;

            default:
                cb = new Gson().fromJson(json, ContentBlock.class);
                break;
        }

        return cb;
    }
}