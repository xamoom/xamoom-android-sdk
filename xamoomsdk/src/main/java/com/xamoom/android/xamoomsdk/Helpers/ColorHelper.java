package com.xamoom.android.xamoomsdk.Helpers;

import android.content.Context;
import android.graphics.Color;
import androidx.core.graphics.ColorUtils;

public class ColorHelper {
    private static ColorHelper instance;
    private final static int DARK_UNSELECTED_COLOR = Color.argb(153, 0, 0, 0);
    private final static int LIGHT_UNSELECTED_COLOR = Color.argb(153, 255, 255, 255);

    private Context context;
    private int primaryColor = Color.WHITE;
    private int primaryDarkColor = Color.LTGRAY;
    private int accentColor = Color.BLUE;
    private int barFontColor = Color.BLACK;
    private int tabBarSelectedColor = Color.BLACK;
    private int tabbarUnselectedColor = DARK_UNSELECTED_COLOR;

    public static ColorHelper getInstance(Context context, int primaryColor, int primaryDarkColor, int accentColor) {
        if (instance == null) {
            instance = new ColorHelper();
            instance.context = context;
            instance.initProperties(primaryColor, primaryDarkColor, accentColor);
        }
        return instance;
    }

    public static ColorHelper getInstance() {
        return instance;
    }

    private void initProperties(int primaryColor, int primaryDarkColor, int accentColor) {
        barFontColor = isDark(primaryColor) ? Color.WHITE : Color.BLACK;
        tabBarSelectedColor = isDark(primaryColor) ? Color.WHITE : Color.BLACK;
        tabbarUnselectedColor = isDark(primaryColor) ? LIGHT_UNSELECTED_COLOR : DARK_UNSELECTED_COLOR;
    }

    private boolean isDark(int color){
        return (ColorUtils.calculateLuminance(color) < 0.5);
    }

    public int getPrimaryColor() {
        return primaryColor;
    }

    public int getPrimaryDarkColor() {
        return primaryDarkColor;
    }

    public int getAccentColor() {
        return accentColor;
    }

    public int getBarFontColor() {
        return barFontColor;
    }

    public int getTabBarSelectedColor() {
        return tabBarSelectedColor;
    }

    public int getTabbarUnselectedColor() {
        return tabbarUnselectedColor;
    }
}
