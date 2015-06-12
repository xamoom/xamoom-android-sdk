package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping;

import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentBlocks.ContentBlock;

import java.util.List;

/**
 * Used for mapping content responses received from the xamoom-cloud-api.
 * Content will have a title, a description, a imagePublicUrl, a language and a contentId.
 *
 * Always display the image (loaded from the imagePublicUrl), the title and the text first.
 *
 * The content from the system will be stored in the <code>contentBlocks</code>. You have to display
 * the contentBlocks in the existing order. Look up the documentation to see the different contentBlocks.
 *
 * @author Raphael Seher
 *
 * @see ContentBlock
 * @see ContentById
 * @see ContentByLocation
 * @see ContentByLocationIdentifier
 * @see ContentList
 */
public class Content {

    private String contentId;
    private String imagePublicUrl;
    private String description;
    private String language;
    private String title;
    private List<ContentBlock> contentBlocks;

    @Override
    public String toString() {
        return (String.format("\ncontentId: %s, " +
                "\nimagePublicUrl: %s, " +
                "\ndescription: %s, " +
                "\nlanguage: %s, " +
                "\ntitle: %s, " +
                "\ncontentBlocks: %s", contentId, imagePublicUrl, description, language, title, contentBlocks));
    }

    //getter

    public String getContentId() {
        return contentId;
    }

    public String getImagePublicUrl() {
        return imagePublicUrl;
    }

    public String getDescriptionOfContent() {
        return description;
    }

    public String getLanguage() {
        return language;
    }

    public String getTitle() {
        return title;
    }

    public List<ContentBlock> getContentBlocks() {
        return contentBlocks;
    }
}
