package com.xamoom.android;

import retrofit.RetrofitError;

/**
 * Created by raphaelseher on 02.06.15.
 */
public interface APICallback<T> {
    void finished(T result);
    void error(RetrofitError error);
}
