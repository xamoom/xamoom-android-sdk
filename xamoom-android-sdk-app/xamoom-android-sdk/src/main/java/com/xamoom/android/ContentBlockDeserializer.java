package com.xamoom.android;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.xamoom.android.mapping.ContentBlocks.ContentBlock;
import com.xamoom.android.mapping.ContentBlocks.ContentBlockType0;
import com.xamoom.android.mapping.ContentBlocks.ContentBlockType1;
import com.xamoom.android.mapping.ContentBlocks.ContentBlockType2;
import com.xamoom.android.mapping.ContentBlocks.ContentBlockType3;
import com.xamoom.android.mapping.ContentBlocks.ContentBlockType4;
import com.xamoom.android.mapping.ContentBlocks.ContentBlockType5;
import com.xamoom.android.mapping.ContentBlocks.ContentBlockType6;
import com.xamoom.android.mapping.ContentBlocks.ContentBlockType7;
import com.xamoom.android.mapping.ContentBlocks.ContentBlockType8;
import com.xamoom.android.mapping.ContentBlocks.ContentBlockType9;

import java.lang.reflect.Type;

/**
 * Used for differ between the contentBlocks when deserializing the JSON.
 */
public class ContentBlockDeserializer implements JsonDeserializer {

    private static final String TAG = ContentBlockDeserializer.class.getSimpleName();

    public ContentBlock deserialize(JsonElement json, Type typeOfT,
                                    JsonDeserializationContext context) throws JsonParseException {
        ContentBlock cb;
        int cbt;

        JsonObject jsonObject = json.getAsJsonObject();
        try {
            cbt = jsonObject.get("content_block_type").getAsInt();
        } catch (NumberFormatException e) {
            Log.e(TAG, "content_block_type not an int.");
            e.printStackTrace();
            return null;
        }

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