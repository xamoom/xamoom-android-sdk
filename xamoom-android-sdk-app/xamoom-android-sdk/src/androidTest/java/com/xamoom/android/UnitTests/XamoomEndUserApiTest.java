package com.xamoom.android.UnitTests;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.xamoom.android.R;
import com.xamoom.android.XamoomEndUserApi;

import java.util.Locale;

/**
 * Test the connection to the XamoomEnduserApi. (prod)
 */
public class XamoomEndUserApiTest extends ApplicationTestCase<Application> {
    public XamoomEndUserApiTest() {
        super(Application.class);
    }

    private static final String TAG = XamoomEndUserApiTest.class.getSimpleName();

    private String apikey;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        apikey = getContext().getString(R.string.prod_apiKey);
    }

    /**
     * Check if getInstance(context, apikey) returns object.
     */
    public void testThatGetInstanceReturnsObject() {
        XamoomEndUserApi api = XamoomEndUserApi.getInstance(getContext(), apikey);

        assertNotNull(api);
    }

    /*
     * systemLanguageU() tests
     */

    /**
     * Check if getSystemLanguage returns something.
     */
    public void testThatGetSystemLanguageReturnsLanguage() {
        String testLanguage = XamoomEndUserApi.getInstance(getContext(), apikey)
                .getSystemLanguage();

        assertNotNull(testLanguage);
    }

    /**
     * Check if getSystemLanguage returns device language.
     */
    public void testThatGetSystemLanguageReturnsDeviceLanguage() {
        String deviceLanguage = Locale.getDefault().getLanguage();
        String languageFromApi = XamoomEndUserApi.getInstance(getContext(), apikey)
                .getSystemLanguage();

        assertEquals(deviceLanguage, languageFromApi);
    }

    /**
     * Check if getSystemLanguage returns device language.
     *
     * Disable when using other language settings on phone.
     */
    public void testThatGetSystemLanguageReturnsEnglish() {
        String testLanguage = "en";
        String languageFromApi = XamoomEndUserApi.getInstance(getContext(), apikey)
                .getSystemLanguage();

        assertEquals(testLanguage, languageFromApi);
    }
}
