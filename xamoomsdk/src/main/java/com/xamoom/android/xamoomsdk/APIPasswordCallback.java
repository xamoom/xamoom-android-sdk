package com.xamoom.android.xamoomsdk;

public interface APIPasswordCallback<T,U> {
    void finished(T result);
    void error(U error);
    void passwordRequested();
}
