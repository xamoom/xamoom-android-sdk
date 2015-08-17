package com.xamoom.android;

import retrofit.RetrofitError;

/**
 * Interface for XamoomEndUserApi.
 */
public interface APICallback<T> {
    void finished(T result);
    void error(RetrofitError error);
}
