package com.xamoom.android;

/**
 * Created by raphaelseher on 02.06.15.
 */
public interface APICallback<T> {
    void finished(T result);
}
