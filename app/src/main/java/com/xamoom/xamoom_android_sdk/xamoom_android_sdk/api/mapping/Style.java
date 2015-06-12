package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping;

import com.google.gson.annotations.SerializedName;

/**
 * Used for mapping style from the xamoom-cloud-api.
 * Style will have a backgroundColor, a hightlightColor, a foregroundColor,
 * a chromeHeaderColor, a customMarker and a icon.
 *
 * Use the hightlightColor for normal links.
 * Use the foregroundColor as text-color.
 * Use the backgroundColor as background-color.
 * Use the customMarker as mapMarker.
 * Use the icon, as a cooperate icon in you app.
 *
 * @author Raphael Seher
 */
public class Style {

    @SerializedName("bg_color")
    private String backgroundColor;
    @SerializedName("hl_color")
    private String highlightColor;
    @SerializedName("fg_color")
    private String foregroundColor;
    @SerializedName("ch_color")
    private String chromeHeaderColor;
    private String customMarker;
    private String icon;

    public String toString () {
        return (String.format("\nbackgroundColor: %s, \nhighlightColor: %s, \nforegroundColor: %s, \nchromeHeaderColor: %s, \ncustomMarker: %s, \nicon: %s}", backgroundColor, highlightColor, foregroundColor, chromeHeaderColor, customMarker, icon));
    }
}
