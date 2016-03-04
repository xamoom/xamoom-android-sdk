package com.xamoom.android.xamoomsdk;

/**
 * Created by raphaelseher on 03/03/16.
 */
public interface APIListCallback<T, U> {
  void finished(T result, String cursor, boolean hasMore);
  void error(U error);
}
