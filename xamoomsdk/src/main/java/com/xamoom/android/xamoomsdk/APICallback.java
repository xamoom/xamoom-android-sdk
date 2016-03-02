package com.xamoom.android.xamoomsdk;

/**
 * Generic Callback for {@link EnduserApi} api calls.
 */
public interface APICallback<T,U> {
    void finished(T result);
    void error(U error);
}
