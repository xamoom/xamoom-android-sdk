package com.xamoom.android.UnitTests;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.xamoom.android.ContentBlockDeserializer;
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

/**
 * Created by raphaelseher on 17.11.15.
 */
public class ContentBlockDeserializerTest extends ApplicationTestCase<Application> {
    public ContentBlockDeserializerTest() {
        super(Application.class);
    }

    private ContentBlockDeserializer mContentBlockDeserializer;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mContentBlockDeserializer = new ContentBlockDeserializer();

    }

    /*
     * Helper
     */

    private JsonObject jsonWithContentBlockType(String type) {
        JsonObject json = new JsonObject();
        json.addProperty("content_id", "asdf");
        json.addProperty("public", "True");
        json.addProperty("content_block_type", type);
        return json;
    }


    /*
     * Tests
     */

    public void testThatDeserializerDoCB0() {
        JsonObject json = jsonWithContentBlockType("0");

        ContentBlock x = mContentBlockDeserializer.deserialize(json, ContentBlock.class, null);
        assertEquals(x.getClass().getSimpleName(), ContentBlockType0.class.getSimpleName());
    }

    public void testThatDeserializerDoCB1() {
        JsonObject json = jsonWithContentBlockType("1");

        ContentBlock x = mContentBlockDeserializer.deserialize(json, ContentBlock.class, null);
        assertEquals(x.getClass().getSimpleName(), ContentBlockType1.class.getSimpleName());
    }

    public void testThatDeserializerDoCB2() {
        JsonObject json = jsonWithContentBlockType("2");

        ContentBlock x = mContentBlockDeserializer.deserialize(json, ContentBlock.class, null);
        assertEquals(x.getClass().getSimpleName(), ContentBlockType2.class.getSimpleName());
    }

    public void testThatDeserializerDoCB3() {
        JsonObject json = jsonWithContentBlockType("3");

        ContentBlock x = mContentBlockDeserializer.deserialize(json, ContentBlock.class, null);
        assertEquals(x.getClass().getSimpleName(), ContentBlockType3.class.getSimpleName());
    }

    public void testThatDeserializerDoCB4() {
        JsonObject json = jsonWithContentBlockType("4");

        ContentBlock x = mContentBlockDeserializer.deserialize(json, ContentBlock.class, null);
        assertEquals(x.getClass().getSimpleName(), ContentBlockType4.class.getSimpleName());
    }

    public void testThatDeserializerDoCB5() {
        JsonObject json = jsonWithContentBlockType("5");

        ContentBlock x = mContentBlockDeserializer.deserialize(json, ContentBlock.class, null);
        assertEquals(x.getClass().getSimpleName(), ContentBlockType5.class.getSimpleName());
    }

    public void testThatDeserializerDoCB6() {
        JsonObject json = jsonWithContentBlockType("6");

        ContentBlock x = mContentBlockDeserializer.deserialize(json, ContentBlock.class, null);
        assertEquals(x.getClass().getSimpleName(), ContentBlockType6.class.getSimpleName());
    }

    public void testThatDeserializerDoCB7() {
        JsonObject json = jsonWithContentBlockType("7");

        ContentBlock x = mContentBlockDeserializer.deserialize(json, ContentBlock.class, null);
        assertEquals(x.getClass().getSimpleName(), ContentBlockType7.class.getSimpleName());
    }

    public void testThatDeserializerDoCB8() {
        JsonObject json = jsonWithContentBlockType("8");

        ContentBlock x = mContentBlockDeserializer.deserialize(json, ContentBlock.class, null);
        assertEquals(x.getClass().getSimpleName(), ContentBlockType8.class.getSimpleName());
    }

    public void testThatDeserializerDoCB9() {
        JsonObject json = jsonWithContentBlockType("9");

        ContentBlock x = mContentBlockDeserializer.deserialize(json, ContentBlock.class, null);
        assertEquals(x.getClass().getSimpleName(), ContentBlockType9.class.getSimpleName());
    }

    public void testThatDeserializerDoesNotBreakOnFalseInput() {
        JsonObject json = jsonWithContentBlockType("x");

        ContentBlock x = mContentBlockDeserializer.deserialize(json, ContentBlock.class, null);
        assertNull(x);
    }

    public void testThatDeserializerDoUnknownContentBlockType() {
        JsonObject json = jsonWithContentBlockType("99");

        ContentBlock x = mContentBlockDeserializer.deserialize(json, ContentBlock.class, null);
        assertEquals(x.getClass().getSimpleName(), ContentBlock.class.getSimpleName());
    }
}
