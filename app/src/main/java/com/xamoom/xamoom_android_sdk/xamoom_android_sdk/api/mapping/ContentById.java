package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping;

import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.APICallback;

/**
 * Used for mapping contentById responses from the xamoom-cloud-api.
 * ContentById will have a systemName, a systemUrl, a systemId, a content
 * and/or style and/or menu if you want.
 *
 * @author Raphael Seher
 *
 * @see Content
 * @see Style
 * @see Menu
 * @see com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.XamoomEndUserApi#getContentById(String, boolean, boolean, String, APICallback)
 * @see com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.XamoomEndUserApi#getContentbyIdFull(String, boolean, boolean, String, boolean, APICallback)
 */
public class ContentById {

    private String systemName;
    private String systemUrl;
    private String systemId;
    private Content content;
    private Style style;
    private Menu menu;

    @Override
    public String toString () {
        return (String.format("systemName: %s, " +
                "\nsystemUrl: %s, " +
                "\nsystemId: %s, " +
                "\ncontent: %s, " +
                "\nstyle: %s, " +
                "\nmenu: %s", systemName, systemUrl, systemId, content, style, menu));
    }

    //getter
    public String getSystemName() {
        return systemName;
    }

    public String getSystemUrl() {
        return systemUrl;
    }

    public String getSystemId() {
        return systemId;
    }

    public Content getContent() {
        return content;
    }

    public Style getStyle() {
        return style;
    }

    public Menu getMenu() {
        return menu;
    }
}
