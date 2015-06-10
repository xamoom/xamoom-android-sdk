package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping;

/**
 * Created by raphaelseher on 29.05.15.
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
