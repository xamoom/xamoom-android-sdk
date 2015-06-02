package com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api;

/**
 * Created by raphaelseher on 02.06.15.
 */
public interface APICallback<T> {
    void finished(T result);
}
